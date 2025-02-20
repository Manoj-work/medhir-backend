package com.example.MedhirBackend.repository;

import com.example.grpcdemo.model.CompanyModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends MongoRepository<CompanyModel,String> {
    Optional<CompanyModel> findByEmail(String email);
    Optional<CompanyModel> findByPhone(String phone);

}