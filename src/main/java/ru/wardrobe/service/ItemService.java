package ru.wardrobe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.wardrobe.model.Item;
import ru.wardrobe.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

/**
 * Слой сервиса для управления сущностями "Предмет".
 * Этот класс предоставляет операции CRUD для объектов "Предмет".
 */
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * Сохраняет сущность "Предмет".
     *
     * @param item Объект "Предмет" для сохранения.
     * @return Сохраненный объект "Предмет".
     */
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    /**
     * Ищет сущность "Предмет" по её ID.
     *
     * @param id ID предмета для поиска.
     * @return Optional, содержащий предмет, если найден, иначе пустой.
     */
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    /**
     * Возвращает список всех сущностей "Предмет".
     *
     * @return Список всех предметов.
     */
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    /**
     * Удаляет сущность "Предмет" по её ID.
     *
     * @param id ID предмета для удаления.
     */
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }
}