package com.treviratech.clientsapibackend.models.services;

import com.treviratech.clientsapibackend.models.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClientService {

    public List<Client> findAll();

    public Page<Client> findAll(Pageable pageable);

    public Client findById(Long id);

    public Client save(Client client);

    public void delete(Long id);


}
