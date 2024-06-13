package ru.wardrobe.model.items;

import jakarta.persistence.*;
import lombok.Data;
import ru.wardrobe.model.Item;
/**
 * Сущность для верхней одежды.
 */
@Entity
@Data
@DiscriminatorValue("upperwear")
public class Upperwear extends Item {

    // Дополнительные свойства для верхней одежды
}
