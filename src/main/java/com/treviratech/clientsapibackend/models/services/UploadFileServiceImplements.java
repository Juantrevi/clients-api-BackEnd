package com.treviratech.clientsapibackend.models.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileServiceImplements implements IUploadFileService{

    private final static String UPLOADS_ROUTE = "uploads";
    private final static String NO_USER_PHOTO_PATH = "src/main/resources/static/images";
    private final static String NO_USER_PHOTO_NAME = "no-user.png";

    @Override
    public Resource upload(String fileName) throws MalformedURLException {

        Path path = getPath(fileName);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists() && !resource.isReadable()){
            path = Paths.get(NO_USER_PHOTO_PATH).resolve(NO_USER_PHOTO_NAME).toAbsolutePath();

            resource = new UrlResource(path.toUri());


        }
        return resource;
    }

    @Override
    public String copy(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "");
        String path = UPLOADS_ROUTE;
        Path savePath = getPath(fileName);

        Files.copy(file.getInputStream(), savePath);

        return fileName;
    }

    @Override
    public boolean delete(String fileName) {
        if (fileName != null && !fileName.isEmpty()){
            Path oldFile = Paths.get(UPLOADS_ROUTE).resolve(fileName).toAbsolutePath();
            File oldFileInServer = oldFile.toFile();
            if (oldFileInServer.exists() && oldFileInServer.canRead()){
                oldFileInServer.delete();
                return true;
            }
        }
        return false;
    }

    @Override
    public Path getPath(String fileName) {
        return Paths.get(UPLOADS_ROUTE).resolve(fileName).toAbsolutePath();
    }
}
