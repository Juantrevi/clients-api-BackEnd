package com.treviratech.clientsapibackend.controllers;

import com.treviratech.clientsapibackend.models.entity.Client;
import com.treviratech.clientsapibackend.models.services.IClientService;
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

    @GetMapping("/clients")
    public List<Client> index() {
        return clientService.findAll();
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
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

    @PostMapping("/clients")
    public ResponseEntity<?> create(@RequestBody Client client) {
        Client savedClient = null;
        Map<String, Object> response = new HashMap<>();
        try{
            savedClient = clientService.save(client);
        }catch (DataAccessException e){
            response.put("message", "Error trying to insert into the DB" );
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "Client created successfully");
        response.put("client", savedClient);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
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

}
