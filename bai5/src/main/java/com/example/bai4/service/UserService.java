package com.example.bai4.service;

import com.example.bai4.entity.Role;
import com.example.bai4.entity.User;
import com.example.bai4.repository.RoleRepository;
import com.example.bai4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Đăng ký người dùng mới với vai trò
     *
     * @param user     thông tin người dùng cần đăng ký
     * @param roleName tên vai trò được chỉ định
     * @return thông báo kết quả
     */
    public String registerUser(User user, String roleName) {
        // Kiểm tra email đã tồn tại trong hệ thống
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email đã tồn tại!";
        }

        // Mã hóa mật khẩu người dùng
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Tìm và gán vai trò cho người dùng
        Role userRole = getRole(roleName);

        // Gán vai trò cho người dùng
        user.setRoles(Collections.singleton(userRole));

        // Kiểm tra thông tin người dùng trước khi lưu
        System.out.println("User Info: " + user.toString());

        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(user);
        return "User registered successfully!";
    }


    /**
     * Lấy danh sách tất cả người dùng
     *
     * @return danh sách người dùng
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Lấy thông tin người dùng theo ID
     *
     * @param id ID người dùng
     * @return thông tin người dùng hoặc null nếu không tìm thấy
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Xóa người dùng theo ID
     *
     * @param id ID người dùng
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Cập nhật thông tin người dùng
     *
     * @param user đối tượng người dùng chứa thông tin cập nhật
     */
    public void updateUser(User user) {
        Optional<User> existingUserOptional = userRepository.findById(user.getId());

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            // Cập nhật các thông tin người dùng
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                existingUser.setEmail(user.getEmail());
            }

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                existingUser.setRoles(user.getRoles());
            }

            if (user.getCompany() != null) {
                existingUser.setCompany(user.getCompany());
            }

            userRepository.save(existingUser);
        }
    }

    /**
     * Lấy danh sách tất cả vai trò
     *
     * @return danh sách vai trò
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Kiểm tra email đã tồn tại trong cơ sở dữ liệu
     *
     * @param email email người dùng cần kiểm tra
     * @return true nếu email đã tồn tại, false nếu chưa tồn tại
     */
    private boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Lấy vai trò từ tên vai trò, nếu không tồn tại sẽ tạo mới vai trò với tên mặc định
     *
     * @param roleName tên vai trò
     * @return vai trò tương ứng
     */
    private Role getRole(String roleName) {
        if (roleName == null || roleName.isEmpty()) {
            roleName = "USER"; // Vai trò mặc định là USER
        }

        String finalRoleName = roleName;
        return roleRepository.findByName(roleName).orElseGet(() -> createRole(finalRoleName));
    }

    /**
     * Tạo vai trò mới và lưu vào cơ sở dữ liệu
     *
     * @param roleName tên vai trò cần tạo
     * @return vai trò mới
     */
    private Role createRole(String roleName) {
        Role newRole = new Role();
        newRole.setName(roleName);
        return roleRepository.save(newRole);
    }
}
