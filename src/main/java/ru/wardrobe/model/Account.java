package ru.wardrobe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Сущность "Учетная запись".
 */
@Entity
@Data
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "roles")
    private String roles;


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Wardrobe> wardrobes = new ArrayList<>();


    /**
     * Конструктор по умолчанию.
     */
    public Account(){

    }
    /**
     * Конструктор с параметрами для инициализации учетной записи.
     *
     * @param username Имя пользователя.
     * @param email    Адрес электронной почты.
     * @param password Пароль.
     * @param roles    Роли пользователя.
     */
    public Account(String username, String email, String password, String roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
