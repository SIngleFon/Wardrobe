package ru.wardrobe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.wardrobe.model.Account;

/**
 * Репозиторий для сущности "Учетная запись".
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    /**
     * Находит учетную запись по имени пользователя.
     *
     * @param username Имя пользователя.
     * @return Учетная запись, соответствующая имени пользователя.
     */
    Account findByUsername(String username);

    /**
     * Проверяет существование учетной записи по имени пользователя.
     *
     * @param username Имя пользователя для проверки.
     * @return true, если учетная запись существует, иначе false.
     */
    boolean existsByUsername(String username);

    /**
     * Проверяет существование учетной записи по адресу электронной почты.
     *
     * @param email Адрес электронной почты для проверки.
     * @return true, если учетная запись существует с указанным адресом электронной почты, иначе false.
     */
    boolean existsByEmail(String email);
}
