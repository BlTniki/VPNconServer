package com.bitniki.VPNconServer.modules.user.service.impl;

import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserAlreadyExistException;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.model.User;
import com.bitniki.VPNconServer.modules.user.repository.UserRepo;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import com.bitniki.VPNconServer.modules.user.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    @Value("${tg.user.password}")
    String tgPassword;
    @Value("${accountant.user.password}")
    String accountantPassword;

    @PostConstruct
    public void createDefaultUsersIfNotExist()
            throws UserAlreadyExistException, UserValidationFailedException {
//        if(userRepo.findByLogin("telegramBot").isEmpty() && tgPassword != null) {
//            UserEntity bot = UserEntity.builder().login("telegramBot").password(tgPassword).build();
//            Long botId = create(bot).getId();
//            //Добавить к верхнему билдеру           !!!!
//            //Change role
////            userRepo.findByLogin("telegramBot").map(u -> u.setRole(Role.ADMIN)).map(u -> userRepo.save(u));
//        }
//        if(userRepo.findByLogin("telegramBot").isEmpty() && accountantPassword != null) {
//            UserEntity bot = UserEntity.builder().login("accountant").password(accountantPassword).build();
//            Long botId = create(bot).getId();
//
//            //Добавить к верхнему билдеру           !!!!
//            //Change role
////            userRepo.findByLogin("accountant").map(u -> u.setRole(Role.ADMIN)).map(u -> userRepo.save(u));
//        }
    }

    private UserEntity updateUser(UserEntity oldUser, UserEntity newUser) throws UserValidationFailedException, UserAlreadyExistException {
        // valid new entity
        UserValidator userValidator = UserValidator.validateNonNullFields(newUser);
        if(userValidator.hasFails()){
            throw new UserValidationFailedException(userValidator.toString());
        }

        //if we have new login check its unique
        if (newUser.getLogin() != null && userRepo.findByLogin(newUser.getLogin()).isPresent()) {
            throw new UserAlreadyExistException(
                    "User with login \"%s\" already exist!".formatted(newUser.getLogin())
            );
        }
        //if we have new password encode it
        if(newUser.getPassword() != null) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }

        //update, save and return entity
        return userRepo.save(oldUser.updateWith(newUser));
    }

    private UserEntity deleteUser(UserEntity user) {
        userRepo.delete(user);
        return user;
    }

    public List<User> getAll() {
        List<UserEntity> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);
        return users.stream().map(User::toModel).collect(Collectors.toList());
    }

    public User getOne (Long id) throws UserNotFoundException {
        return userRepo.findById(id)
                .map(User::toModel)
                .orElseThrow(
                        () -> new UserNotFoundException("User with id %d not found".formatted(id))
                );
    }

    public User getOne (Principal principal) throws UserNotFoundException {
        return userRepo.findByLogin(principal.getName())
                .map(User::toModel)
                .orElseThrow(
                        () -> new UserNotFoundException("User not found")
                );
    }

    public User getOneByTelegramId (Long telegramId) throws UserNotFoundException {
//        return User.toModel(userRepo.findByTelegramId(telegramId).orElseThrow(
//                () -> new UserNotFoundException("User not found")
//        ));
        return userRepo.findByTelegramId(telegramId)
                .map(User::toModel)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "User with telegramId %d not found".formatted(telegramId)
                        )
                );
    }

    public User create (UserEntity user) throws UserAlreadyExistException, UserValidationFailedException {
        // valid entity
        UserValidator userValidator = UserValidator.validateAllFields(user);
        if(userValidator.hasFails()){
            throw new UserValidationFailedException(userValidator.toString());
        }

        // check in repo login unique
        if(userRepo.findByLogin(user.getLogin()).isPresent()) {
            throw new UserAlreadyExistException(
                    "User with login \"%s\" already exist!".formatted(user.getLogin())
            );
        }

        // configure entity
        // Set default role
//        user.setRole(Role.ACTIVATED_USER);

        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return User.toModel(userRepo.save(user));
    }

    public User update (Long id, UserEntity newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException {
        // load old entity
        UserEntity oldUser = userRepo.findById(id).orElseThrow(
                () -> new UserNotFoundException(
                        "User with id %d not found".formatted(id)
                )
        );

        return User.toModel(updateUser(oldUser, newUser));
    }

    public User update (Principal principal, UserEntity newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException {
        // load old entity
        UserEntity oldUser = userRepo.findByLogin(principal.getName()).orElseThrow(
                () -> new UserNotFoundException(
                        "User with login \"%s\" not found".formatted(principal.getName())
                )
        );

        return User.toModel(updateUser(oldUser, newUser));
    }

    public User delete(Long id) throws UserNotFoundException {
        // load entity
        UserEntity user = userRepo.findById(id).orElseThrow(
                () -> new UserNotFoundException(
                        "User with id %d not found".formatted(id)
                )
        );

        return User.toModel(deleteUser(user));
    }

    public User delete(Principal principal) throws UserNotFoundException {
        // load entity
        UserEntity user = userRepo.findByLogin(principal.getName()).orElseThrow(
                () -> new UserNotFoundException(
                        "User with login \"%s\" not found".formatted(principal.getName())
                )
        );

        return User.toModel(deleteUser(user));
    }
}
