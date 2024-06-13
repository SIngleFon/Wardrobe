package ru.wardrobe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.wardrobe.model.Item;

/**
 * Репозиторий для сущности "Предмет".
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
