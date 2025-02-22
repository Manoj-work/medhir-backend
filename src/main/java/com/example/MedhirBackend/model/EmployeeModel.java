package com.example.MedhirBackend.model;

import com.example.grpc.employee.Employee;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    @Size(min = 0, max = 10) // Allows empty but restricts length
    @Pattern(regexp = "^$|^[0-9]{10}$", message = "Phone number must be 10 digits if provided")
    private String phone;


    private String department;
    private String gender;
    private String reportingManager;
    private String permanentAddress;
    private String currentAddress;

    private IdProofs idProofs;
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
                .setIdProofs(idProofs == null ? IdProofs.builder().build().toGrpcIDProofs() : idProofs.toGrpcIDProofs())
                .setBankDetails(bankDetails == null ? BankDetails.builder().build().toGrpcBankDetails() : bankDetails.toGrpcBankDetails())
                .setSalaryDetails(salaryDetails == null ? SalaryDetails.builder().build().toGrpcSalaryDetails() : salaryDetails.toGrpcSalaryDetails())
                .build();
    }
}
