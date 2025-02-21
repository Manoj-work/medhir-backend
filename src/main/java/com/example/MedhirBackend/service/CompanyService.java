package com.example.MedhirBackend.service;

import com.example.MedhirBackend.exception.ValidationException;
import com.example.MedhirBackend.repository.CompanyRepository;
import com.example.grpc.company.*;
import com.example.grpcdemo.model.CompanyModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private Validator validator;

    public CompanyService() {
        System.out.println("CompanyService is initialized and running!");
    }

    public CompanyResponse createCompany(Company request) {
        System.out.println("Received createCompany request: " + request);

        // Create and save company
        CompanyModel company = CompanyModel.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .gst(request.getGst())
                .regAdd(request.getRegAdd())
                .build();

        // Validate all fields at once
        Set<ConstraintViolation<CompanyModel>> violations = validator.validate(company);
        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));

            System.err.println("Validation Errors: " + errorMessages);
            throw new ValidationException("Validation Error", errorMessages);
        }

        // Check if email already exists
        if (companyRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidationException("email", "Email is already registered");
        }

        // Check if phone number already exists
        if (companyRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new ValidationException("phone", "Phone number is already registered");
        }

        CompanyModel savedCompany = companyRepository.save(company);
        System.out.println("Company created successfully: " + savedCompany);

        return CompanyResponse.newBuilder()
                .setMessage("Company Created Successfully")
                .setCompany(savedCompany.toGrpcCompany())
                .build();
    }

    public CompanyListResponse getAllCompanies() {
        List<Company> grpcCompanies = companyRepository.findAll().stream()
                .map(CompanyModel::toGrpcCompany)
                .collect(Collectors.toList());

        return CompanyListResponse.newBuilder()
                .setMessage(grpcCompanies.isEmpty() ? "No companies found" : "Companies retrieved successfully")
                .addAllCompanies(grpcCompanies)
                .build();
    }
    public CompanyResponse updateCompany(UpdateCompanyRequest request) {
        Optional<CompanyModel> optionalCompany = companyRepository.findById(request.getId());

        if (optionalCompany.isEmpty()) {
            throw new ValidationException("company", "Company not found");
        }

        CompanyModel company = optionalCompany.get();
        company.setName(request.getName());
        company.setEmail(request.getEmail());
        company.setPhone(request.getPhone());
        company.setGst(request.getGst());
        company.setRegAdd(request.getRegAdd());

        Set<ConstraintViolation<CompanyModel>> violations = validator.validate(company);
        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));

            System.err.println("Validation Errors: " + errorMessages);
            throw new ValidationException("Validation Error", errorMessages);
        }

        CompanyModel updatedCompany = companyRepository.save(company);

        return CompanyResponse.newBuilder()
                .setMessage("Company Updated Successfully")
                .setCompany(updatedCompany.toGrpcCompany())
                .build();
    }

    public DeleteCompanyResponse deleteCompany(DeleteCompanyRequest request) {
        Optional<CompanyModel> optionalCompany = companyRepository.findById(request.getId());

        if (optionalCompany.isEmpty()) {
            throw new ValidationException("company", "Company not found");
        }

        companyRepository.deleteById(request.getId());

        return DeleteCompanyResponse.newBuilder()
                .setMessage("Company Deleted Successfully")
                .build();
    }
}
