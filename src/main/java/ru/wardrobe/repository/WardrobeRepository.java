package ru.wardrobe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.wardrobe.model.Wardrobe;

/**
 * Репозиторий для сущности "Шкаф".
 */
@Repository
public interface WardrobeRepository extends JpaRepository<Wardrobe, Long> {
}
