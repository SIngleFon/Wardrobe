package ru.wardrobe.model.items;

import jakarta.persistence.*;
import lombok.Data;
import ru.wardrobe.model.Item;
/**
 * Сущность для обуви.
 */
@Entity
@Data
@DiscriminatorValue("footwear")
public class Footwear extends Item {

//    @Enumerated(EnumType.STRING)
//    @Column(name = "shoe_size")
//    private EnumShoeSize shoeSize;

    // Дополнительные свойства для обуви
}
