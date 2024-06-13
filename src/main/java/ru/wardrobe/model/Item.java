package ru.wardrobe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;
import ru.wardrobe.model.enums.EnumColor;
import ru.wardrobe.model.enums.EnumSeason;
import ru.wardrobe.model.items.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Абстрактный класс для предметов.
 * Определяет основные свойства и отношения для всех типов предметов.
 */
@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Accessory.class, name = "accessory"),
        @JsonSubTypes.Type(value = Bottomwear.class, name = "bottomwear"),
        @JsonSubTypes.Type(value = Clothing.class, name = "clothing"),
        @JsonSubTypes.Type(value = Footwear.class, name = "footwear"),
        @JsonSubTypes.Type(value = Upperwear.class, name = "upperwear")
})
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "wardrobe_id")
    @JsonIgnore
    private Wardrobe wardrobe;

    @ElementCollection(targetClass = EnumColor.class)
    @CollectionTable(name = "item_colors", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private Set<EnumColor> colors = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "season")
    private EnumSeason season; // Поле для сезона

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", image='" + imageUrl + '\'' +
                '}';
    }

}

