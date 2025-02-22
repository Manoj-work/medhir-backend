package com.example.MedhirBackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdProofs {
    private String aadharNo;
    private String panNo;
    private String passport;
    private String drivingLicense;
    private String voterId;

    public com.example.grpc.employee.IDProofs toGrpcIDProofs() {
        return com.example.grpc.employee.IDProofs.newBuilder()
                .setAadharNo(aadharNo == null ? "" : aadharNo)
                .setPanNo(panNo == null ? "" : panNo)
                .setPassport(passport == null ? "" : passport)
                .setDrivingLicense(drivingLicense == null ? "" : drivingLicense)
                .setVoterId(voterId == null ? "" : voterId)
                .build();
    }
}
