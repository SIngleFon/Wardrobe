package ru.wardrobe.model.items;

import jakarta.persistence.*;
import lombok.Data;
import ru.wardrobe.model.enums.EnumComposition;
import ru.wardrobe.model.Item;

import java.util.HashSet;
import java.util.Set;

/**
 * Сущность для одежды.
 */
@Entity
@Data
@DiscriminatorValue("clothing")
public class Clothing extends Item {
    @ElementCollection(targetClass = EnumComposition.class)
    @CollectionTable(name = "item_composition", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "material")
    @Enumerated(EnumType.STRING)
    private Set<EnumComposition> material = new HashSet<>();

//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "material")
//    private EnumComposition material;

    // Дополнительные свойства для одежды
}
