package com.example.grpcdemo.model;

import com.example.grpc.company.Company;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "companies")
public class CompanyModel {
    @Id
    private String id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phone;

    @NotBlank(message = "GST cannot be empty")
    @Pattern(regexp = "^[A-Za-z0-9]{15}$", message = "GST must be a 15-digit alphanumeric code")
    private String gst;

    @NotBlank(message = "Registered address cannot be empty")
    private String regAdd;

    public Company toGrpcCompany() {
        return Company.newBuilder()
                .setId(id == null ? "" : id)
                .setName(name)
                .setEmail(email)
                .setPhone(phone)
                .setGst(gst)
                .setRegAdd(regAdd)
                .build();
    }
}
