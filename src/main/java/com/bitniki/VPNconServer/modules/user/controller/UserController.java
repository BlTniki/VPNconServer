package com.bitniki.VPNconServer.modules.user.controller;

import com.bitniki.VPNconServer.modules.user.exception.UserAlreadyExistException;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.model.Token;
import com.bitniki.VPNconServer.modules.user.model.User;
import com.bitniki.VPNconServer.modules.user.model.UserFromRequest;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Получение списка всех пользователей. Для использования требуется авторизация с ролью "user:read" и "any".
     * @return ResponseEntity со списком пользователей и статусом ответа
     */
    @GetMapping
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('user:read')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(
                StreamSupport.stream(userService.getAll(),false)
                        .map(User::toModel)
                        .toList()
        );
    }

    /**
     * Получение пользователя по его ID. Для использования требуется авторизация с ролью "user:read" и "any".
     * @param id ID пользователя
     * @return ResponseEntity с найденным пользователем и статусом ответа
     * @throws UserNotFoundException если пользователь не найден в базе данных
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('user:read')")
    public ResponseEntity<User> getUser(@PathVariable Long id)
            throws UserNotFoundException {
        return ResponseEntity.ok(
                User.toModel(userService.getOneById(id))
        );
    }

    /**
     * Получение пользователя по его telegramId. Для использования требуется авторизация с ролью "user:read" и "any".
     *
     * @param telegramId telegramId пользователя
     * @return ResponseEntity с найденным пользователем и статусом ответа
     * @throws UserNotFoundException если пользователь не найден в базе данных
     */
    @GetMapping("/tg/{telegramId}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('user:read')")
    public ResponseEntity<User> getUserByTelegramId(@PathVariable Long telegramId)
            throws UserNotFoundException {
        return ResponseEntity.ok(
                User.toModel(userService.getOneByTelegramId(telegramId))
        );
    }

    /**
     * Получение текущего пользователя. Для использования требуется авторизация с ролью "user:read" и "personal".
     * @param principal объект Principal, содержащий информацию о текущем пользователе
     * @return ResponseEntity с найденным пользователем и статусом ответа
     * @throws UserNotFoundException если текущий пользователь не найден в базе данных
     */
    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('user:read')")
    public ResponseEntity<User> getMineUser(Principal principal)
            throws UserNotFoundException {
        return ResponseEntity.ok(
                User.toModel(userService.getOneByLogin(principal.getName()))
        );
    }

    /**
     * Создание нового пользователя.
     * В теле запроса необходимо указать {@link String} login и {@link String} password.
     * @param user объект {@link UserFromRequest}, содержащий {@link String} login и {@link String} password нового пользователя. Правила для полей можно найти в описании полей.
     * @return ResponseEntity с созданным пользователем и статусом ответа.
     * @throws UserAlreadyExistException если юзер с данным логином уже существует.
     * @throws UserValidationFailedException если введенные данные пользователя не проходят валидацию.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserFromRequest user)
            throws UserAlreadyExistException, UserValidationFailedException {
        return ResponseEntity.ok(
                User.toModel(userService.create(user))
        );
    }

    /**
     * PUT метод для обновления пользователя по его ID. Для использования требуется авторизация с ролью "user:write" и "any".
     * @param id ID пользователя, которого требуется обновить.
     * @param user Объект {@link UserFromRequest}, содержащий обновленные данные пользователя. Правила для полей можно найти в описании полей.
     * @return ResponseEntity<User> Объект ResponseEntity, содержащий обновленный объект пользователя и код 200 в случае успеха.
     * @throws UserNotFoundException Исключение, возникающее при попытке обновления несуществующего пользователя.
     * @throws UserAlreadyExistException если юзер с данным логином уже существует.
     * @throws UserValidationFailedException Исключение, возникающее при попытке обновления пользователя с некорректными данными.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('user:write')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserFromRequest user)
            throws UserNotFoundException, UserAlreadyExistException, UserValidationFailedException {
        return ResponseEntity.ok(
                User.toModel(userService.updateById(id, user))
        );
    }

    /**
     * PUT метод для обновления текущего пользователя. Для использования требуется авторизация с ролью "user:write" и "personal".
     * @param principal Объект Principal, содержащий информацию о текущем пользователе.
     * @param user Объект {@link UserFromRequest}, содержащий обновленные данные пользователя. Правила для полей можно найти в описании полей.
     * @return ResponseEntity<User> Объект ResponseEntity, содержащий обновленный объект пользователя и код 200 в случае успеха.
     * @throws UserNotFoundException Исключение, возникающее при попытке обновления несуществующего пользователя.
     * @throws UserAlreadyExistException если юзер с данным логином уже существует.
     * @throws UserValidationFailedException Исключение, возникающее при попытке обновления пользователя с некорректными данными.
     */
    @PutMapping("/mine")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('user:write')")
    public ResponseEntity<User> updateMineUser(Principal principal, @RequestBody UserFromRequest user)
            throws UserNotFoundException, UserAlreadyExistException, UserValidationFailedException {
        return ResponseEntity.ok(
                User.toModel(userService.updateByLogin(principal.getName(), user))
        );
    }

    /**
     * DELETE метод для удаления пользователя по его ID. Для использования требуется авторизация с ролью "user:write" и "any".
     * @param id ID пользователя, которого требуется удалить.
     * @return ResponseEntity<User> Объект ResponseEntity, содержащий удаленный объект пользователя и код 200 в случае успеха.
     * @throws UserNotFoundException Исключение, возникающее при попытке удаления несуществующего пользователя.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('user:write')")
    public ResponseEntity<User> deleteUser(@PathVariable Long id)
            throws UserNotFoundException {
        return ResponseEntity.ok(
                User.toModel(userService.deleteById(id))
        );
    }

    /**
     * DELETE метод для удаления текущего пользователя. Для использования требуется авторизация с ролью "user:write" и "personal".
     * @param principal Объект Principal, содержащий информацию о текущем пользователе.
     * @return ResponseEntity<User> Объект ResponseEntity, содержащий удаленный объект пользователя и код 200 в случае успеха.
     * @throws UserNotFoundException Исключение, возникающее при попытке удаления несуществующего пользователя.
     */
    @DeleteMapping("/mine")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('user:write')")
    public ResponseEntity<User> deleteMineUser(Principal principal)
            throws UserNotFoundException {
        return ResponseEntity.ok(
                User.toModel(userService.deleteByLogin(principal.getName()))
        );
    }

    /**
     * Аутентифицирует пользователя и создает токен.
     *
     * @param request Запрос с данными пользователя.
     * @return Модель, содержащая логин авторизированного пользователя и сгенерированный JWT токен.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserValidationFailedException Если валидация данных пользователя не удалась.
     * @throws AuthenticationException Если был отправлен неверный пароль.
     */
    @PostMapping("/login")
    public ResponseEntity<Token> auth(@RequestBody UserFromRequest request)
            throws UserNotFoundException, UserValidationFailedException, AuthenticationException {
        return ResponseEntity.ok(userService.authAndCreateToken(request));
    }

    /**
     * Выполняет выход пользователя.
     *
     * @param request HTTP-запрос, связанный с пользователем.
     * @return Ответ с сообщением "Success", если выход выполнен успешно.
     * @throws UserNotFoundException Если пользователь не найден.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws UserNotFoundException {
        userService.logout(request.getUserPrincipal().getName());
        return ResponseEntity.ok("Success");
    }

    /**
     * Связывает пользователя с Telegram аккаунтом.
     *
     * @param user Данные пользователя из запроса.
     * @return Сущность пользователя, после связывания с Telegram.
     * @throws UserNotFoundException Если пользователь не найден.
     * @throws UserValidationFailedException Если валидация полей в user не пройдена.
     */
    @PostMapping("/tg")
    @PreAuthorize("hasAuthority('any')")
    public ResponseEntity<User> associateTelegramIdWithUser(@RequestBody UserFromRequest user)
            throws UserNotFoundException, UserValidationFailedException {
        return ResponseEntity.ok(
                User.toModel( userService.associateTelegram(user) )
        );
    }

    /**
     * Удаляет связь пользователя с Telegram аккаунтом.
     * @param user Данные пользователя из запроса. Должен содержать {@link String} login и {@link String} password.
     * @return Сущность пользователя, после удаления связи с Telegram.
     * @throws UserNotFoundException Если пользователь не найден.
     * @throws UserValidationFailedException Если валидация полей в user не пройдена.
     */
    @DeleteMapping("/tg")
    @PreAuthorize("hasAuthority('any')")
    public ResponseEntity<User> dissociateTelegramIdWithUser(@RequestBody UserFromRequest user)
            throws UserNotFoundException, UserValidationFailedException {
        return ResponseEntity.ok(
                User.toModel( userService.dissociateTelegram(user) )
        );
    }

    /**
     * Метод для получения паттернов валидации полей {@link String} ipaddress и {@link String} networkPrefix.
     * Используется клиентами для получения актуальных правил валидации, чтобы валидировать ввод на месте, не отправляя на сервер.
     * @return Карту в виде {"login": pattern, "password": pattern}.
     */
    @GetMapping("/validator")
    @PreAuthorize("hasAuthority('any')")
    public ResponseEntity< Map<String, String> > getValidatorPatterns() {
        return ResponseEntity.ok(
                userService.getValidationRegex()
        );
    }
}
