package ru.wardrobe.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.wardrobe.service.AccountService;


/**
 * Контроллер для управления представлениями, связанными с аккаунтами пользователей.
 */
@Controller
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    /**
     * Получение профиля пользователя.
     * @param username Логин пользователя.
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для отображения профиля пользователя.
     */
    @GetMapping("/{username}/lk")
    public String getProfile(@PathVariable String username, Model model) {
        model.addAttribute("username", username);
        return "lk"; // Возвращает имя представления "lk" для отображения профиля пользователя
    }

    /**
     * Получение предметов из шкафа пользователя.
     * @param username Логин пользователя.
     * @param wardrobeId Идентификатор шкафа.
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для отображения содержимого шкафа.
     */
    @GetMapping("/{username}/lk/{wardrobeId}")
    public String getWardrobeItems(@PathVariable String username, @PathVariable Long wardrobeId, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("wardrobeId", wardrobeId);
        return "wardrobe"; // Возвращает имя представления "wardrobe" для отображения содержимого шкафа
    }

    /**
     * Получение главной страницы.
     * @return Имя представления для отображения главной страницы.
     */
    @GetMapping("/index")
    public String getIndex(){
         return "index"; // Возвращает имя представления "index" для отображения главной страницы
    }

}

