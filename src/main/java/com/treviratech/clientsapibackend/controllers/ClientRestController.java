package com.treviratech.clientsapibackend.controllers;

import com.treviratech.clientsapibackend.models.entity.Client;
import com.treviratech.clientsapibackend.models.services.IClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@CrossOrigin(origins = {"http://localhost:4200"})//Giving access to Angular app in port 4200 to this API
@RestController
@RequestMapping("/api") // This is optional
public class ClientRestController {

    @Autowired
    private IClientService clientService;

    //The object BindingResult must be placed right after the object to be validated and it allows us
    //to know if there was any problem in the validation process
    @PostMapping("/clients")
    public ResponseEntity<?> create(@RequestBody @Valid Client client, BindingResult result) {
        Client savedClient = null;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()){
            List<String> errors = new ArrayList<>();
            result.getFieldErrors().forEach(err -> errors.add(err.getDefaultMessage()));
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

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

    //Pageable
    @GetMapping("/clients/page/{page}")
    public Page<Client> readAll(@PathVariable Integer page) {

        Pageable pageable = PageRequest.of(page, 4);

        return clientService.findAll(pageable);
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
    public ResponseEntity<?> update(@RequestBody @Valid Client client, BindingResult result, @PathVariable Long id) {

        Map<String, Object> response = new HashMap<>();
        Client currentClient = clientService.findById(id);

        if (result.hasErrors()){
            List<String> errors = new ArrayList<>();
            result.getFieldErrors().forEach(err -> errors.add(err.getDefaultMessage()));
            response.put("errors", errors);
            System.out.println("errors: " + errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        if (currentClient == null) {
            response.put("error", "Not Found");
            response.put("message", "Client ID: ".concat(id.toString()).concat(" does not exist"));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            currentClient.setName(client.getName());
            currentClient.setLastName(client.getLastName());
            currentClient.setEmail(client.getEmail());
            currentClient.setCreatedAt(client.getCreatedAt());
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
            String oldFileName = client.getPhoto();
            if (oldFileName != null && !oldFileName.isEmpty()){
                Path oldFile = Paths.get("uploads").resolve(oldFileName).toAbsolutePath();
                File oldFileInServer = oldFile.toFile();
                if (oldFileInServer.exists() && oldFileInServer.canRead()){
                    oldFileInServer.delete();
                }
            }
            clientService.delete(id);
            response.put("message", "Client deleted successfully");

        }catch (DataAccessException e){
            response.put("message", "Error trying to delete the client from the DB" );
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    //Upload user's image
    @PostMapping("/clients/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id){

        Map<String, Object> response = new HashMap<>();
        Client client = clientService.findById(id);

        if (client == null) {
            response.put("error", "Not Found");
            response.put("message", "Client ID: ".concat(id.toString()).concat(" does not exist"));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        if (!file.isEmpty()){
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "");
            String path = "uploads";
            Path savePath = Paths.get(path).resolve(fileName).toAbsolutePath();
            //log.info(path.toString());
            try {
                Files.copy(file.getInputStream(), savePath);
            } catch (IOException e) {
                response.put("message", "Error trying to upload the image " + file.getOriginalFilename() + " to the server" );
                response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String oldFileName = client.getPhoto();
            if (oldFileName != null && !oldFileName.isEmpty()){
                Path oldFile = Paths.get(path).resolve(oldFileName).toAbsolutePath();
                File oldFileInServer = oldFile.toFile();
                if (oldFileInServer.exists() && oldFileInServer.canRead()){
                    oldFileInServer.delete();
                }
            }
            client.setPhoto(fileName);
            clientService.save(client);
            response.put("client", client);
            response.put("message", "You have uploaded the image successfully: " + file.getOriginalFilename());
        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    //Download the image
    @GetMapping("/uploads/img/{fileName:.+}")
    public ResponseEntity<Resource> seePhoto(@PathVariable String fileName){

        Path path = Paths.get("uploads").resolve(fileName).toAbsolutePath();
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        if (!resource.exists() && !resource.isReadable()){
            path = Paths.get("src/main/resources/static/images").resolve("no_user.png").toAbsolutePath();

            try {
                resource = new UrlResource(path.toUri());
            }catch (MalformedURLException e){
                e.printStackTrace();
            }

//            throw new RuntimeException("Error trying to load the image: " + path.toString());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");

        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }

}


