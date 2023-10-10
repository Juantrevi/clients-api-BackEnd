package com.treviratech.clientsapibackend.controllers;

import com.treviratech.clientsapibackend.models.entity.Client;
import com.treviratech.clientsapibackend.models.services.IClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin(origins = {"http://localhost:4200"})//Giving access to Angular app in port 4200 to this API
@RestController
@RequestMapping("/api") // This is optional
public class ClientRestController {

    @Autowired
    private IClientService clientService;

    @PostMapping("/clients")
    public ResponseEntity<?> create(@RequestBody @Valid Client client) {
        Client savedClient = null;
        Map<String, Object> response = new HashMap<>();
        try{
            savedClient = clientService.save(client);
            response.put("message", "Client created successfully");
            response.put("client", savedClient);
        }catch (DataAccessException e){
            response.put("message", "Error trying to insert into the DB" );
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping("/clients")
    public List<Client> readAll() {
        return clientService.findAll();
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        Client client = null;
        Map<String, Object> response = new HashMap<>();
        try {
            client = clientService.findById(id);
        }catch (DataAccessException e){
            response.put("message", "Error querying database" );
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        if (client == null) {
            response.put("error", "Not Found");
            response.put("message", "Client ID: ".concat(id.toString()).concat(" does not exist"));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Client>(client, HttpStatus.OK);
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<?> update(@RequestBody Client client, @PathVariable Long id) {

        Map<String, Object> response = new HashMap<>();
        Client currentClient = clientService.findById(id);

        if (currentClient == null) {
            response.put("error", "Not Found");
            response.put("message", "Client ID: ".concat(id.toString()).concat(" does not exist"));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            currentClient.setName(client.getName());
            currentClient.setLastName(client.getLastName());
            currentClient.setEmail(client.getEmail());
            clientService.save(currentClient);
            response.put("message", "Client updated successfully");
            response.put("client", currentClient);

        }catch (DataAccessException e){

            response.put("message", "Error trying to update the client into the DB" );
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }


        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Client client = clientService.findById(id);

        if (client == null) {
            response.put("error", "Not Found");
            response.put("message", "Client ID: ".concat(id.toString()).concat(" does not exist"));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        try {
            clientService.delete(id);
            response.put("message", "Client deleted successfully");

        }catch (DataAccessException e){
            response.put("message", "Error trying to delete the client from the DB" );
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

}


