package com.treviratech.clientsapibackend.models.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public interface IUploadFileService {
    public Resource upload (String fileName) throws MalformedURLException;

    public String copy(MultipartFile file) throws IOException;

    public boolean delete(String fileName);

    public Path getPath(String fileName);

}
