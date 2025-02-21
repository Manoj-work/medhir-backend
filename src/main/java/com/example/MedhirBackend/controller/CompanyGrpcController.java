package com.example.MedhirBackend.controller;

import com.example.MedhirBackend.service.CompanyService;
import com.example.grpc.company.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class CompanyGrpcController extends CompanyServiceGrpc.CompanyServiceImplBase {

    private final CompanyService companyService;

    @Autowired
    public CompanyGrpcController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public void createCompany(Company request, StreamObserver<CompanyResponse> responseObserver) {
        try {
            CompanyResponse response = companyService.createCompany(request);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getAllCompanies(EmptyRequest request, StreamObserver<CompanyListResponse> responseObserver) {
        try {
            CompanyListResponse response = companyService.getAllCompanies();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException());
        }
    }
}
