package com.treviratech.clientsapibackend.models.repository;

import com.treviratech.clientsapibackend.models.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IClientRepository extends JpaRepository<Client, Long> {




}
