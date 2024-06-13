package ru.wardrobe.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
/**
 * Сервис для управления загрузкой и хранением файлов.
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    /**
     * Конструктор, инициализирующий местоположение хранилища файлов и создающий директорию, если она не существует.
     */
    public FileStorageService() {
        this.fileStorageLocation = Paths.get("src/main/resources/static/fileRepository").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    /**
     * Сохраняет загруженный файл в хранилище.
     *
     * @param file Загруженный файл в формате MultipartFile.
     * @return Имя файла после сохранения.
     */
    public String storeFile(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    /**
     * Загружает файл из хранилища как ресурс.
     *
     * @param fileName Имя файла для загрузки.
     * @return Ресурс файла.
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }
    /**
     * Удаляет файл из хранилища.
     *
     * @param fileName Имя файла для удаления.
     */
    public void deleteFile(String fileName) {
        try {
            // Добавляем фильтр для игнорирования определенного имени файла
            if (!fileName.equals("cappelledelcommiatofirenze.png")) {
                Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
                File file = filePath.toFile();
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not delete file " + fileName, ex);
        }
    }
}