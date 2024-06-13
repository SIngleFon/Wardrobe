package ru.wardrobe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.wardrobe.model.Account;
import ru.wardrobe.model.Wardrobe;
import ru.wardrobe.repository.AccountRepository;
import ru.wardrobe.service.AccountService;
import ru.wardrobe.service.WardrobeService;
/**
 * REST контроллер для аутентификации и регистрации пользователей.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AccountService service;
    private final WardrobeService wService;
    private final AccountRepository accountRepository;


    /**
     * Обработка запроса на вход пользователя (аутентификация).
     * @param username Логин пользователя.
     * @param password Пароль пользователя.
     * @param redirectAttributes Атрибуты для перенаправления и передачи сообщений.
     * @return ResponseEntity с сообщением об успешной аутентификации или ошибке.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Проверяем валидность пользователя
            if (isValidUser(username, password)) {
                redirectAttributes.addFlashAttribute("message", "Вы успешно авторизовались !");
                return ResponseEntity.ok("success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Ошибка. Проверьте корректность введенных данных!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message",
                    "Ошибка. Проверьте корректность введенных данных!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    /**
     * Проверяет валидность пользователя по логину и паролю.
     * @param username Логин пользователя.
     * @param password Пароль пользователя.
     * @return true, если пользователь валиден, иначе false.
     */
    private boolean isValidUser(String username, String password) {
        try {
            Account account = service.getUserByUsername(username);
            return password.equals(account.getPassword());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Обработка запроса на регистрацию нового пользователя.
     * @param username Логин нового пользователя.
     * @param email Адрес электронной почты нового пользователя.
     * @param password Пароль нового пользователя.
     * @param redirectAttributes Атрибуты для перенаправления и передачи сообщений.
     * @return RedirectView для перенаправления на главную страницу с сообщением об успешной регистрации или ошибке.
     */
    @PostMapping("/register")
    public RedirectView register(@RequestParam("name") String username,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 RedirectAttributes redirectAttributes) {
        // Проверяем уникальность логина и адреса электронной почты
        if (accountRepository.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("message", "Ошибка. Пользователь с таким логином уже существует!");
            return new RedirectView("/");
        }

        if (accountRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("message", "Ошибка. Пользователь с таким адресом электронной почты уже существует!");
            return new RedirectView("/");
        }

        // Создаем новую учетную запись
        Account account = new Account(username, email, password, "USER");

        // Создаем новый шкаф
        Wardrobe wardrobe = new Wardrobe("Шкаф_1");
        wardrobe.setImageUrl("https://business-click.it/images/portfolio/cappelledelcommiatofirenze.png");
        // Устанавливаем связь между учетной записью и шкафом
        wardrobe.setAccount(account);

        // Добавляем шкаф к списку шкафов учетной записи (это автоматически добавится в базу данных)
        account.getWardrobes().add(wardrobe);

        // Сохраняем учетную запись (это также сохранит все связанные шкафы)
        Account savedAccount = service.save(account);

        RedirectView redirectView = new RedirectView("/");
        redirectAttributes.addFlashAttribute("message", "Registration successful!");
        return redirectView;
    }

}