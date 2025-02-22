package com.example.MedhirBackend.model;

import com.example.grpc.employee.Employee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "employees")
public class EmployeeModel {
    @Id
    private String id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    private String title;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;
    private String department;
    private String gender;
    private String reportingManager;
    private String permanentAddress;
    private String currentAddress;

    private Map<String, String> idProofs;
    private BankDetails bankDetails;
    private SalaryDetails salaryDetails;

    public Employee toGrpcEmployee() {
        return Employee.newBuilder()
                .setId(id == null ? "" : id)
                .setName(name)
                .setTitle(title == null ? "" : title)
                .setEmail(email == null ? "" : email)
                .setPhone(phone == null ? "" : phone)
                .setDepartment(department == null ? "" : department)
                .setGender(gender == null ? "" : gender)
                .setReportingManager(reportingManager == null ? "" : reportingManager)
                .setPermanentAddress(permanentAddress == null ? "" : permanentAddress)
                .setCurrentAddress(currentAddress == null ? "" : currentAddress)
                .build();
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class BankDetails {
    private String accountNumber;
    private String accountHolderName;
    private String ifscCode;
    private String bankName;
    private String branchName;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class SalaryDetails {
    private double totalCtc;
    private double basic;
    private double allowances;
    private double hra;
    private double pf;
}
