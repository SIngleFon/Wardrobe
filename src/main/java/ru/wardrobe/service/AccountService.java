package ru.wardrobe.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import ru.wardrobe.model.Account;
import ru.wardrobe.model.Wardrobe;
import ru.wardrobe.repository.AccountRepository;
import ru.wardrobe.repository.WardrobeRepository;
/**
 * Сервис для управления учетными записями.
 */
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository repository;
    private final WardrobeRepository wardrobeRepository;
    /**
     * Сохраняет учетную запись.
     *
     * @param account Учетная запись для сохранения.
     * @return Сохраненная учетная запись.
     */
    @Transactional
    public Account save(Account account){
        Account savedAccount = repository.save(account);
        return savedAccount;
    }
    /**
     * Возвращает все учетные записи.
     *
     * @return Итерируемая коллекция учетных записей.
     */
    public Iterable<Account> getAll(){
        return repository.findAll();
    }
    /**
     * Возвращает учетную запись по имени пользователя.
     *
     * @param username Имя пользователя для поиска.
     * @return Найденная учетная запись или null, если не найдена.
     */
    public Account getUserByUsername(String username) {
        return repository.findByUsername(username);
    }
}


