package com.example.bai4.controller;

import com.example.bai4.entity.Company;
import com.example.bai4.entity.Role;
import com.example.bai4.entity.User;
import com.example.bai4.repository.RoleRepository;
import com.example.bai4.repository.UserRepository;
import com.example.bai4.service.CompanyService;
import com.example.bai4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyService companyService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllRoles());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               @RequestParam(required = false) String roleName,
                               Model model) {
        String message = userService.registerUser(user, roleName);
        model.addAttribute("message", message);
        model.addAttribute("roles", userService.getAllRoles());
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/users")
    public String listUsers(Model model, Authentication authentication) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", authentication.getName());
        model.addAttribute("roles", userService.getAllRoles());
        return "users";
    }

    @GetMapping("/users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllRoles());  // Danh sách các vai trò
        model.addAttribute("companies", companyService.getAllCompanies()); // Danh sách các công ty
        return "add-user";  // Trả về trang form thêm người dùng
    }



    @PostMapping("/users/add")
    public String addUser(@ModelAttribute User user,
                          @RequestParam(value = "selectedRoles", required = false) List<Long> selectedRoleIds,
                          @RequestParam("companyId") Long companyId,
                          Model model) {
        try {
            // Role handling
            Set<Role> roles = new HashSet<>();
            if (selectedRoleIds != null) {
                for (Long roleId : selectedRoleIds) {
                    Role role = roleRepository.findById(roleId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid role ID: " + roleId));
                    roles.add(role);
                }
            }
            user.setRoles(roles);

            // Company handling
            Company company = companyService.getCompanyById(companyId);
            if (company == null) {
                model.addAttribute("error", "Invalid company selected");
                model.addAttribute("roles", userService.getAllRoles());
                model.addAttribute("companies", companyService.getAllCompanies());
                return "add-user";
            }
            user.setCompany(company);

            // Register user and handle response
            String result = userService.registerUser(user, null);
            if (!result.contains("successfully")) {
                model.addAttribute("error", result);
                model.addAttribute("roles", userService.getAllRoles());
                model.addAttribute("companies", companyService.getAllCompanies());
                return "add-user";
            }
            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("error", "Error adding user: " + e.getMessage());
            model.addAttribute("roles", userService.getAllRoles());
            model.addAttribute("companies", companyService.getAllCompanies());
            return "add-user";
        }
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/users";  // Nếu không tìm thấy user, quay lại trang danh sách người dùng
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getAllRoles());  // Danh sách các vai trò
        model.addAttribute("companies", companyService.getAllCompanies()); // Danh sách các công ty
        return "edit-user";  // Trả về trang form chỉnh sửa người dùng
    }


    @PostMapping("/users/edit")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam(value = "selectedRoles", required = false) List<Long> selectedRoleIds,
                             @RequestParam("companyId") Long companyId,
                             Model model) {
        try {
            // Verify user exists
            User existingUser = userService.getUserById(user.getId());
            if (existingUser == null) {
                model.addAttribute("error", "User not found");
                return showEditUserForm(user.getId(), model);
            }

            // Role handling
            Set<Role> roles = new HashSet<>();
            if (selectedRoleIds != null) {
                for (Long roleId : selectedRoleIds) {
                    Role role = roleRepository.findById(roleId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid role ID: " + roleId));
                    roles.add(role);
                }
            }
            user.setRoles(roles);

            // Company handling
            Company company = companyService.getCompanyById(companyId);
            if (company == null) {
                model.addAttribute("error", "Invalid company selected");
                return showEditUserForm(user.getId(), model);
            }
            user.setCompany(company);

            userService.updateUser(user);
            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("error", "Error updating user: " + e.getMessage());
            return showEditUserForm(user.getId(), model);
        }
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, Model model) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                model.addAttribute("error", "User not found");
                return "redirect:/users?error=userNotFound";
            }
            userService.deleteUser(id);
            return "redirect:/users?success=deleted";
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting user: " + e.getMessage());
            return "redirect:/users?error=deleteFailed";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }

    // Thêm các phương thức liên quan đến Company
    @GetMapping("/companies")
    public String listCompanies(Model model, Authentication authentication) {
        List<Company> companies = companyService.getAllCompanies();
        model.addAttribute("companies", companies);
        model.addAttribute("currentUser", authentication.getName());
        return "companies";
    }

    @GetMapping("/companies/register")
    public String showRegisterCompanyForm(Model model, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);

        model.addAttribute("company", new Company());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("currentUserId", user != null ? user.getId() : null);
        return "register-company";
    }

    @PostMapping("/companies/register")
    public String registerCompany(@ModelAttribute Company company,
                                  @RequestParam Long userId) {
        companyService.registerCompany(company, userId);
        return "redirect:/companies";
    }

    @GetMapping("/companies/edit/{id}")
    public String showEditCompanyForm(@PathVariable Long id, Model model) {
        Company company = companyService.getCompanyById(id);
        if (company == null) {
            return "redirect:/companies";
        }
        model.addAttribute("company", company);
        model.addAttribute("users", userService.getAllUsers());
        return "edit-company";
    }

    @PostMapping("/companies/edit")
    public String updateCompany(@ModelAttribute Company company,
                                @RequestParam Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            company.setUser(user);
            companyService.saveCompany(company);
        }
        return "redirect:/companies";
    }

    @GetMapping("/companies/delete/{id}")
    public String deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return "redirect:/companies";
    }
}