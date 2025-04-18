package com.example.day4.controller;

import com.example.day4.model.Company;
import com.example.day4.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public String listCompanies(Model model) {
        List<Company> companies = companyService.getAllCompanies();
        model.addAttribute("companies", companies);
        return "companies"; // Assumes there's a companies.html file to display the list
    }

    @GetMapping("/add")
    public String showAddCompanyForm(Model model) {
        model.addAttribute("company", new Company());
        return "add-company"; // Assumes there's an add-company.html file for input
    }

    @PostMapping("/add")
    public String addCompany(@ModelAttribute Company company) {
        companyService.saveCompany(company);
        return "redirect:/companies";
    }

    @GetMapping("/edit/{id}")
    public String showEditCompanyForm(@PathVariable Long id, Model model) {
        Optional<Company> company = companyService.getCompanyById(id);
        if (company.isPresent()) {
            model.addAttribute("company", company.get());
            return "edit-company"; // Assumes there's an edit-company.html file
        }
        return "redirect:/companies?error=CompanyNotFound";
    }

    @PostMapping("/edit")
    public String updateCompany(@ModelAttribute Company company) {
        companyService.saveCompany(company);
        return "redirect:/companies";
    }

    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Long id) {
        Optional<Company> company = companyService.getCompanyById(id);
        if (company.isPresent()) {
            companyService.deleteCompany(id);
        }
        return "redirect:/companies";
    }
}