package com.treviratech.clientsapibackend.models.services;

import com.treviratech.clientsapibackend.models.entity.Client;

import java.util.List;

public interface IClientService {

    public List<Client> findAll();

}
