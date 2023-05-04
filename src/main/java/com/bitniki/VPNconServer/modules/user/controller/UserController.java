package com.bitniki.VPNconServer.modules.user.controller;

import com.bitniki.VPNconServer.modules.user.exception.UserAlreadyExistException;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.model.User;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.model.UserFromRequest;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
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
                User.toModel(userService.getOne(id))
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
                User.toModel(userService.getOne(principal.getName()))
        );
    }

    /**
     * Создание нового пользователя.
     * В теле запроса необходимо указать {@link String} login и {@link String} password.
     * @param user объект {@link UserFromRequest}, содержащий {@link String} login и {@link String} password нового пользователя.
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
     * @param user Объект {@link UserFromRequest}, содержащий обновленные данные пользователя.
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
                User.toModel(userService.update(id, user))
        );
    }

    /**
     * PUT метод для обновления текущего пользователя. Для использования требуется авторизация с ролью "user:write" и "personal".
     * @param principal Объект Principal, содержащий информацию о текущем пользователе.
     * @param user Объект {@link UserFromRequest}, содержащий обновленные данные пользователя.
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
                User.toModel(userService.update(principal.getName(), user))
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
                User.toModel(userService.delete(id))
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
                User.toModel(userService.delete(principal.getName()))
        );
    }


}
