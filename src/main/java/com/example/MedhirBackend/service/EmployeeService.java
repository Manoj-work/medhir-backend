package com.example.MedhirBackend.service;

import com.example.MedhirBackend.exception.ValidationException;
import com.example.MedhirBackend.repository.EmployeeRepository;
import com.example.grpc.employee.*;
import com.example.MedhirBackend.model.EmployeeModel;
import com.example.MedhirBackend.model.IdProofs;
import com.example.MedhirBackend.model.BankDetails;
import com.example.MedhirBackend.model.SalaryDetails;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private Validator validator;

    public EmployeeService() {
        System.out.println("EmployeeService is initialized and running!");
    }

    public EmployeeResponse createEmployee(Employee request) {
        System.out.println("Received createEmployee request: " + request);

        // Create and save employee
        EmployeeModel employee = EmployeeModel.builder()
                .name(request.getName())
                .title(request.getTitle())
                .email(request.getEmail())
                .phone(request.getPhone())
                .department(request.getDepartment())
                .gender(request.getGender())
                .reportingManager(request.getReportingManager())
                .permanentAddress(request.getPermanentAddress())
                .currentAddress(request.getCurrentAddress())
                .idProofs(IdProofs.builder()
                        .aadharNo(request.getIdProofs().getAadharNo())
                        .panNo(request.getIdProofs().getPanNo())
                        .passport(request.getIdProofs().getPassport())
                        .drivingLicense(request.getIdProofs().getDrivingLicense())
                        .voterId(request.getIdProofs().getVoterId())
                        .build())
                .bankDetails(BankDetails.builder()
                        .accountNumber(request.getBankDetails().getAccountNumber())
                        .accountHolderName(request.getBankDetails().getAccountHolderName())
                        .ifscCode(request.getBankDetails().getIfscCode())
                        .bankName(request.getBankDetails().getBankName())
                        .branchName(request.getBankDetails().getBranchName())
                        .build())
                .salaryDetails(SalaryDetails.builder()
                        .totalCtc(request.getSalaryDetails().getTotalCtc())
                        .basic(request.getSalaryDetails().getBasic())
                        .allowances(request.getSalaryDetails().getAllowances())
                        .hra(request.getSalaryDetails().getHra())
                        .pf(request.getSalaryDetails().getPf())
                        .build())
                .build();

        // Validate all fields
        Set<ConstraintViolation<EmployeeModel>> violations = validator.validate(employee);
        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));

            System.err.println("Validation Errors: " + errorMessages);
            throw new ValidationException("Validation Error", errorMessages);
        }

//        // Check if email already exists
//        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
//            throw new ValidationException("email", "Email is already registered");
//        }

//        // Check if phone number already exists
//        if (employeeRepository.findByPhone(request.getPhone()).isPresent()) {
//            throw new ValidationException("phone", "Phone number is already registered");
//        }

        EmployeeModel savedEmployee = employeeRepository.save(employee);
        System.out.println("Employee created successfully: " + savedEmployee);

        return EmployeeResponse.newBuilder()
                .setMessage("Employee Created Successfully")
                .setEmployee(savedEmployee.toGrpcEmployee())
                .build();
    }

    public EmployeeListResponse getAllEmployees() {
        List<Employee> grpcEmployees = employeeRepository.findAll().stream()
                .map(EmployeeModel::toGrpcEmployee)
                .collect(Collectors.toList());

        return EmployeeListResponse.newBuilder()
                .setMessage(grpcEmployees.isEmpty() ? "No employees found" : "Employees retrieved successfully")
                .addAllEmployees(grpcEmployees)
                .build();
    }
}
