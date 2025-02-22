package com.example.MedhirBackend.model;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankDetails {
    private String accountNumber;
    private String accountHolderName;

    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code")
    private String ifscCode;

    private String bankName;
    private String branchName;

    public com.example.grpc.employee.BankDetails toGrpcBankDetails() {
        return com.example.grpc.employee.BankDetails.newBuilder()
                .setAccountNumber(accountNumber == null ? "" : accountNumber)
                .setAccountHolderName(accountHolderName == null ? "" : accountHolderName)
                .setIfscCode(ifscCode == null ? "" : ifscCode)
                .setBankName(bankName == null ? "" : bankName)
                .setBranchName(branchName == null ? "" : branchName)
                .build();
    }
}
