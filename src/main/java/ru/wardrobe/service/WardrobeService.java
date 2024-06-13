package ru.wardrobe.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.wardrobe.model.Wardrobe;
import ru.wardrobe.repository.WardrobeRepository;

import java.util.List;
import java.util.Optional;

/**
 * Слой сервиса для управления сущностями "Шкаф".
 * Этот класс предоставляет операции CRUD для объектов "Шкаф".
 */
@Service
@RequiredArgsConstructor
public class WardrobeService {

    private final WardrobeRepository wardrobeRepository;

    /**
     * Сохраняет сущность "Шкаф".
     *
     * @param wardrobe Объект "Шкаф" для сохранения.
     * @return Сохраненный объект "Шкаф".
     */
    public Wardrobe save(Wardrobe wardrobe) {
        return wardrobeRepository.save(wardrobe);
    }

    /**
     * Ищет сущность "Шкаф" по её ID.
     *
     * @param id ID шкафа для поиска.
     * @return Optional, содержащий шкаф, если найден, иначе пустой.
     */
    public Optional<Wardrobe> findById(Long id) {
        return wardrobeRepository.findById(id);
    }

    /**
     * Возвращает список всех сущностей "Шкаф".
     *
     * @return Список всех шкафов.
     */
    public List<Wardrobe> findAll() {
        return wardrobeRepository.findAll();
    }

    /**
     * Удаляет сущность "Шкаф" по её ID.
     *
     * @param id ID шкафа для удаления.
     */
    public void deleteById(Long id) {
        wardrobeRepository.deleteById(id);
    }
}