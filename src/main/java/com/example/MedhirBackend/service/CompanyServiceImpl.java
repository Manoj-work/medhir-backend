package com.example.MedhirBackend.service;

import com.example.MedhirBackend.exception.ValidationException;
import com.example.MedhirBackend.repository.CompanyRepository;
import com.example.grpc.company.*;
import com.example.grpcdemo.model.CompanyModel;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@GrpcService
public class CompanyServiceImpl extends CompanyServiceGrpc.CompanyServiceImplBase {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private Validator validator;

    public CompanyServiceImpl() {
        System.out.println("CompanyService is initialized and running!");
    }

    @Override
    public void getCompany(CompanyRequest request, StreamObserver<CompanyResponse> responseObserver) {
        companyRepository.findById(request.getId()).ifPresentOrElse(company -> {
            CompanyResponse response = CompanyResponse.newBuilder()
                    .setMessage("Company found")
                    .setCompany(company.toGrpcCompany())
                    .build();
            responseObserver.onNext(response);
        }, () -> responseObserver.onNext(CompanyResponse.newBuilder().setMessage("Company Not Found").build()));

        responseObserver.onCompleted();
    }

    @Override
    public void createCompany(Company request, StreamObserver<CompanyResponse> responseObserver) {
        try {
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
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription(errorMessages)
                        .asRuntimeException());
                return;
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

            // Return success response
            CompanyResponse response = CompanyResponse.newBuilder()
                    .setMessage("Company Created Successfully")
                    .setCompany(savedCompany.toGrpcCompany())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (ValidationException ex) {
            System.err.println("Validation Error: " + ex.getField() + " - " + ex.getMessage());
            responseObserver.onError(Status.ALREADY_EXISTS
                    .withDescription(ex.getField() + ": " + ex.getMessage())
                    .asRuntimeException());
        } catch (Exception ex) {
            System.err.println("Unexpected Error: " + ex.getMessage());
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Unexpected error: " + ex.getMessage())
                    .asRuntimeException());
        }
    }



    @Override
    public void getAllCompanies(EmptyRequest request, StreamObserver<CompanyListResponse> responseObserver) {
        List<Company> grpcCompanies = companyRepository.findAll().stream()
                .map(CompanyModel::toGrpcCompany)
                .collect(Collectors.toList());

        responseObserver.onNext(CompanyListResponse.newBuilder()
                .setMessage(grpcCompanies.isEmpty() ? "No companies found" : "Companies retrieved successfully")
                .addAllCompanies(grpcCompanies)
                .build());

        responseObserver.onCompleted();
    }
}
