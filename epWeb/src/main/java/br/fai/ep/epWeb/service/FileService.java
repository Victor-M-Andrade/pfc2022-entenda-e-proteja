package br.fai.ep.epWeb.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadFile(MultipartFile file, final String newKeyFile, final String oldKeyFile);

    byte[] downloadFile(String keyFile);

    boolean deleteFile(String keyFile);
}