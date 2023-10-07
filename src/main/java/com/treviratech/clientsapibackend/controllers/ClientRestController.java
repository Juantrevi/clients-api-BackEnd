package com.treviratech.clientsapibackend.controllers;

import com.treviratech.clientsapibackend.models.entity.Client;
import com.treviratech.clientsapibackend.models.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientRestController {

    @Autowired
    private IClientService clientService;

    @GetMapping("/clients")
    public List<Client> index() {
        return clientService.findAll();
    }

}
