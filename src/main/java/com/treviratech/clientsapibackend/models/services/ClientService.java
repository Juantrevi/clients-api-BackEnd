package com.treviratech.clientsapibackend.models.services;

import com.treviratech.clientsapibackend.models.entity.Client;
import com.treviratech.clientsapibackend.models.repository.IClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService implements IClientService{

    @Autowired
    private IClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
