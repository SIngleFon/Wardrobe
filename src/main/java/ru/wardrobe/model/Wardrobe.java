package ru.wardrobe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Сущность "Шкаф".
 */
@Entity
@Data
public class Wardrobe {
    //region Переменные
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "image_url")  // Поле для хранения URL изображения
    private String imageUrl;
     /* Переменные */
    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @OneToMany(mappedBy = "wardrobe", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();
    //endregion
    /**
     * Конструктор по умолчанию.
     */
    public Wardrobe(){

    }

    /**
     * Конструктор с параметром для установки имени шкафа.
     *
     * @param name Имя шкафа.
     */
    public Wardrobe(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Wardrobe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}


