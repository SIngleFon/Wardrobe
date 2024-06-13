package ru.wardrobe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.wardrobe.model.*;
import ru.wardrobe.model.enums.EnumColor;
import ru.wardrobe.model.enums.EnumComposition;
import ru.wardrobe.model.enums.EnumSeason;
import ru.wardrobe.model.items.*;
import ru.wardrobe.service.AccountService;
import ru.wardrobe.service.FileStorageService;
import ru.wardrobe.service.ItemService;
import ru.wardrobe.service.WardrobeService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST контроллер для работы с предметами одежды (items).
 */
@RestController
@RequiredArgsConstructor
public class ItemController {
    private final WardrobeService wardrobeService;
    private final ItemService itemService;
    private final FileStorageService fileStorageService;
    private final AccountService aService;

    /**
     * Создает новый предмет одежды в указанном шкафу.
     * @param username Имя пользователя.
     * @param wardrobeId ID шкафа, в который добавляется предмет.
     * @param file Файл изображения предмета (необязательный).
     * @param type Тип предмета (UPPERWEAR, ACCESSORY, BOTTOMWEAR, FOOTWEAR, CLOTHING).
     * @param description Описание предмета.
     * @param colorsList Список цветов предмета в формате строк.
     * @param season Сезон предмета (ВЕСНА, ЛЕТО, ОСЕНЬ, ЗИМА).
     * @param materialList Список материалов предмета (для типа CLOTHING, необязательный).
     * @return ResponseEntity с сообщением о результате операции.
     */
    @PostMapping(value = "/api/{username}/wardrobe/{wardrobeId}/items", consumes = {"multipart/form-data"})
    public ResponseEntity<String> createItem(@PathVariable String username,
            @PathVariable Long wardrobeId,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam("type") String type,
            @RequestParam("description") String description,
            @RequestParam("colors") List<String> colorsList,
            @RequestParam("season") String season,
            @RequestParam(value = "material", required = false) List<String> materialList) {

        // Получаем шкаф по его ID
        Optional<Wardrobe> wardrobeOptional = wardrobeService.findById(wardrobeId);
        if (wardrobeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wardrobe not found");
        }

        Wardrobe wardrobe = wardrobeOptional.get();

        // Получаем общие свойства
        Set<EnumColor> colors = colorsList.stream().map(EnumColor::valueOf).collect(Collectors.toSet());

        Item item;
        EnumSeason seasonEnum = EnumSeason.valueOf(season);

        switch (type) {
            case "UPPERWEAR":
                item = new Upperwear();
                break;
            case "ACCESSORY":
                item = new Accessory();
                break;
            case "BOTTOMWEAR":
                item = new Bottomwear();
                break;
            case "FOOTWEAR":
                item = new Footwear();
                break;
            case "CLOTHING":
                item = new Clothing();
                if (materialList != null && !materialList.isEmpty()) {
                    Set<EnumComposition> compositions = materialList.stream()
                            .map(EnumComposition::valueOf)
                            .collect(Collectors.toSet());
                    ((Clothing) item).setMaterial(compositions);
                }
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid item type");
        }
        // Обработка изображения предмета
        if (file != null && !file.isEmpty()) {
            String imageUrl = fileStorageService.storeFile(file);
            item.setImageUrl(imageUrl);
        }else {
            String imageUrl = "https://business-click.it/images/portfolio/cappelledelcommiatofirenze.png";
            item.setImageUrl(imageUrl);
        }
        // Установка остальных свойств предмета
        item.setDescription(description);
        item.setColors(colors);
        item.setWardrobe(wardrobe);
        item.setSeason(seasonEnum);
        itemService.save(item);

        return ResponseEntity.ok("Item created successfully");
    }
    /**
     * Получает предмет одежды по его ID.
     * @param username Имя пользователя.
     * @param itemId ID предмета.
     * @return Найденный предмет одежды или null, если предмет не найден.
     */
    @GetMapping("/api/{username}/item/{itemId}")
    public Item getItem(@PathVariable String username, @PathVariable Long itemId) {
        return itemService.findById(itemId).orElse(null);
    }

    /**
     * Обновляет предмет одежды по его ID.
     * @param username Имя пользователя.
     * @param itemId ID предмета.
     * @param description Новое описание предмета.
     * @param colors Список новых цветов предмета.
     * @param season Новый сезон предмета.
     * @param file Новое изображение предмета (необязательный).
     * @return ResponseEntity с обновленным предметом или статусом NOT_FOUND, если предмет не найден.
     */
    @PutMapping("/api/{username}/item/{itemId}")
    public ResponseEntity<Item> updateItem(
            @PathVariable String username,
            @PathVariable Long itemId,
            @RequestParam String description,
            @RequestParam List<String> colors,
            @RequestParam String season,
            @RequestParam(required = false) MultipartFile file) {
        // Находим предмет по его ID
        Optional<Item> optionalItem = itemService.findById(itemId);
        if (!optionalItem.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        // Обновляем свойства предмета
        Item item = optionalItem.get();
        item.setDescription(description);
        item.setSeason(EnumSeason.valueOf(season.toUpperCase()));
        // Обновляем цвета предмета
        Set<EnumColor> colorSet = colors.stream()
                .map(color -> EnumColor.valueOf(color.toUpperCase()))
                .collect(Collectors.toSet());
        item.setColors(colorSet);


        // Обработка нового изображения предмета
        String oldImageUrl = item.getImageUrl();
        if (file != null && !file.isEmpty()) {
            // Сохраняем новое изображение
            String newImageUrl = fileStorageService.storeFile(file);
            item.setImageUrl(newImageUrl);

            // Удаляем старое изображение, если оно существует
            if (oldImageUrl != null && !oldImageUrl.equals("https://business-click.it/images/portfolio/cappelledelcommiatofirenze.png")) {
                fileStorageService.deleteFile(oldImageUrl);
            }
        }
        // Сохраняем обновленный предмет
        itemService.save(item);

        return ResponseEntity.ok(item);
    }

    /**
     * Удаляет предмет одежды по его ID.
     * @param username Имя пользователя.
     * @param itemId ID предмета.
     * @return ResponseEntity с сообщением об успешном удалении или статусом NOT_FOUND, если предмет не найден.
     */
    @DeleteMapping("/api/{username}/item/{itemId}")
    public ResponseEntity<String> deleteWardrobe(@PathVariable String username, @PathVariable Long itemId) {
        // Находим пользователя по его имени
        Account user = aService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        // Находим предмет по его ID
        Item item = itemService.findById(itemId).orElse(null);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wardrobe not found");
        }

        // Проверяем, что пользователь имеет право удалить этот предмет
        if (!item.getWardrobe().getAccount().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this wardrobe");
        }

        // Удаляем изображение предмета, если оно не является ссылкой по умолчанию
        String oldImageUrl = item.getImageUrl();
        if (oldImageUrl != null && !oldImageUrl.equals("https://business-click.it/images/portfolio/cappelledelcommiatofirenze.png")) {
            fileStorageService.deleteFile(oldImageUrl);
        }
        itemService.deleteById(item.getId());

        return ResponseEntity.ok("Item deleted successfully");
    }
}
