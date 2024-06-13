package ru.wardrobe.model.items;

import jakarta.persistence.*;
import lombok.Data;
import ru.wardrobe.model.Item;

/**
 * Сущность для нижней одежды (штаны/юбки).
 */
@Entity
@Data
@DiscriminatorValue("bottomwear")
public class Bottomwear extends Item {


    // Дополнительные свойства для штанов/юбок
}
