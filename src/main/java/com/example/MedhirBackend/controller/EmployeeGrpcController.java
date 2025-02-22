package com.example.MedhirBackend.controller;

import com.example.MedhirBackend.service.EmployeeService;
import com.example.grpc.employee.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class EmployeeGrpcController extends EmployeeServiceGrpc.EmployeeServiceImplBase {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeGrpcController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void createEmployee(Employee request, StreamObserver<EmployeeResponse> responseObserver) {
        try {
            EmployeeResponse response = employeeService.createEmployee(request);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getAllEmployees(EmptyRequest request, StreamObserver<EmployeeListResponse> responseObserver) {
        try {
            EmployeeListResponse response = employeeService.getAllEmployees();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException());
        }
    }
}
