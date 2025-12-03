package com.proyectoweb.proyectos.domain.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads/lotes}")
    private String uploadDir;

    public String storeFile(MultipartFile file, UUID loteId) throws IOException {
        // Crear directorio si no existe
        Path uploadPath = Paths.get(uploadDir, loteId.toString());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generar nombre único para el archivo
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName != null && originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : "";
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Guardar archivo
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Retornar ruta relativa
        return loteId.toString() + "/" + uniqueFileName;
    }

    public void deleteFile(String fileUrl) throws IOException {
        Path filePath = Paths.get(uploadDir, fileUrl);
        Files.deleteIfExists(filePath);
    }

    public String getFileUrl(String relativePath) {
        return "/api/lotes/files/" + relativePath;
    }
}
