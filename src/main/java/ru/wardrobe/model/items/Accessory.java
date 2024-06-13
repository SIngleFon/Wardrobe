package ru.wardrobe.model.items;

import jakarta.persistence.*;
import lombok.Data;
import ru.wardrobe.model.Item;

/**
 * Сущность для аксессуаров.
 */
@Entity
@Data
@DiscriminatorValue("accessory")
public class Accessory extends Item {

    // Дополнительные свойства для аксессуаров
}
