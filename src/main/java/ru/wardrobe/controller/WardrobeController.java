package ru.wardrobe.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.wardrobe.model.Account;
import ru.wardrobe.model.Wardrobe;
import ru.wardrobe.service.AccountService;
import ru.wardrobe.service.FileStorageService;
import ru.wardrobe.service.WardrobeService;

import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для управления шкафами пользователей.
 */
@RestController
@AllArgsConstructor
public class WardrobeController {

    private final AccountService accountService;
    private final WardrobeService wardrobeService;
    private final FileStorageService fileStorageService;
    /**
     * Получение всех шкафов пользователя по его имени пользователя.
     *
     * @param username Имя пользователя.
     * @return Список шкафов пользователя.
     */
    @GetMapping("/api/{username}/wardrobes")
    public List<Wardrobe> getWardrobes(@PathVariable String username) {
        Account user = accountService.getUserByUsername(username);
        if (user != null) {
            return user.getWardrobes();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Получение конкретного шкафа по идентификатору шкафа и имени пользователя.
     *
     * @param username   Имя пользователя.
     * @param wardrobeId Идентификатор шкафа.
     * @return Найденный шкаф или null, если не найден.
     */
    @GetMapping("/api/{username}/wardrobe/{wardrobeId}")
    public Wardrobe getWardrobe(@PathVariable String username, @PathVariable Long wardrobeId) {
        return wardrobeService.findById(wardrobeId).orElse(null);
    }
    /**
     * Обновление данных шкафа.
     *
     * @param username       Имя пользователя.
     * @param wardrobeId     Идентификатор шкафа.
     * @param updatedWardrobe Обновленные данные шкафа.
     * @param image          Новое изображение шкафа (опционально).
     * @return Ответ с сообщением об успешном обновлении или ошибкой.
     */
    @PutMapping("/api/{username}/wardrobe/{wardrobeId}")
    public ResponseEntity<String> updateWardrobe(
            @PathVariable String username,
            @PathVariable Long wardrobeId,
            @RequestPart("wardrobe") Wardrobe updatedWardrobe,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        Account user = accountService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Wardrobe wardrobe = wardrobeService.findById(wardrobeId).orElse(null);
        if (wardrobe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wardrobe not found");
        }

        if (!wardrobe.getAccount().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this wardrobe");
        }

        // Обновляем данные шкафа
        wardrobe.setName(updatedWardrobe.getName());

        // Обработка нового изображения предмета
        String oldImageUrl = wardrobe.getImageUrl();
        if (image != null && !image.isEmpty()) {
            // Сохраняем новое изображение
            String newImageUrl = fileStorageService.storeFile(image);
            wardrobe.setImageUrl(newImageUrl);

            // Удаляем старое изображение, если оно существует
            if (oldImageUrl != null && !oldImageUrl.equals("https://business-click.it/images/portfolio/cappelledelcommiatofirenze.png")) {
                fileStorageService.deleteFile(oldImageUrl);
            }
        }

        wardrobeService.save(wardrobe);

        return ResponseEntity.ok("Wardrobe updated successfully");
    }

    /**
     * Создание нового шкафа для пользователя.
     *
     * @param username    Имя пользователя.
     * @param newWardrobe Данные нового шкафа.
     * @param image       Изображение нового шкафа (опционально).
     * @return Ответ с сообщением об успешном создании или ошибкой.
     */
    @PostMapping("/api/{username}/wardrobes")
    public ResponseEntity<String> createWardrobe(
            @PathVariable String username,
            @RequestPart("wardrobe") Wardrobe newWardrobe,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        Account user = accountService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Устанавливаем владельца шкафа
        newWardrobe.setAccount(user);

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.storeFile(image);
            newWardrobe.setImageUrl(imageUrl);
        }else {
            String imageUrl = "https://business-click.it/images/portfolio/cappelledelcommiatofirenze.png";
            newWardrobe.setImageUrl(imageUrl);
        }

        // Сохраняем новый шкаф
        wardrobeService.save(newWardrobe);

        return ResponseEntity.ok("Wardrobe created successfully");
    }

    /**
     * Удаление шкафа пользователя.
     *
     * @param username   Имя пользователя.
     * @param wardrobeId Идентификатор шкафа для удаления.
     * @return Ответ с сообщением об успешном удалении или ошибкой.
     */
    @DeleteMapping("/api/{username}/wardrobe/{wardrobeId}")
    public ResponseEntity<String> deleteWardrobe(@PathVariable String username, @PathVariable Long wardrobeId) {
        Account user = accountService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Wardrobe wardrobe = wardrobeService.findById(wardrobeId).orElse(null);
        if (wardrobe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wardrobe not found");
        }

        if (!wardrobe.getAccount().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this wardrobe");
        }
        // Удаляем изображение предмета, если оно не является ссылкой по умолчанию
        String oldImageUrl = wardrobe.getImageUrl();
        if (oldImageUrl != null && !oldImageUrl.equals("https://business-click.it/images/portfolio/cappelledelcommiatofirenze.png")) {
            fileStorageService.deleteFile(oldImageUrl);
        }
        wardrobeService.deleteById(wardrobe.getId());

        return ResponseEntity.ok("Wardrobe deleted successfully");
    }
}