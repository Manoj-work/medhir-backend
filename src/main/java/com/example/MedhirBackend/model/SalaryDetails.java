package com.example.MedhirBackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryDetails {
    private double totalCtc;
    private double basic;
    private double allowances;
    private double hra;
    private double pf;

    public com.example.grpc.employee.SalaryDetails toGrpcSalaryDetails() {
        return com.example.grpc.employee.SalaryDetails.newBuilder()
                .setTotalCtc(totalCtc)
                .setBasic(basic)
                .setAllowances(allowances)
                .setHra(hra)
                .setPf(pf)
                .build();
    }
}
