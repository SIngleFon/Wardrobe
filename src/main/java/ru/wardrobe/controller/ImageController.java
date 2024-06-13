package ru.wardrobe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wardrobe.service.FileStorageService;
import org.springframework.core.io.Resource;

import java.io.IOException;
/**
 * REST контроллер для обработки запросов изображений.
 */
@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final FileStorageService fileStorageService;
    /**
     * Конструктор контроллера ImageController.
     * @param fileStorageService Сервис для хранения файлов.
     */
    public ImageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }
    /**
     * Получает изображение по его имени.
     * @param imageName Имя изображения.
     * @return ResponseEntity с ресурсом изображения или статусом NOT_FOUND, если изображение не найдено.
     * @throws IOException Исключение, которое может возникнуть при загрузке ресурса изображения.
     */
    @GetMapping("/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws IOException {
        // Загружаем ресурс изображения по его имени
        Resource resource = (Resource) fileStorageService.loadFileAsResource(imageName);
        return ResponseEntity.ok()
                .body(resource);
    }
}