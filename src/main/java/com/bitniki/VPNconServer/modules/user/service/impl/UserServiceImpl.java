package com.bitniki.VPNconServer.modules.user.service.impl;

import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserAlreadyExistException;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.model.UserFromRequest;
import com.bitniki.VPNconServer.modules.user.repository.UserRepo;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import com.bitniki.VPNconServer.modules.user.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Spliterator;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
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

    private UserEntity updateUser(UserEntity oldUser, UserFromRequest newUserModel) throws UserValidationFailedException, UserAlreadyExistException {
        // valid new entity
        UserValidator userValidator = UserValidator.validateNonNullFields(newUserModel);
        if(userValidator.hasFails()){
            throw new UserValidationFailedException(userValidator.toString());
        }

        //if we have new login check its unique then set it
        if (newUserModel.getLogin() != null && userRepo.findByLogin(newUserModel.getLogin()).isPresent()) {
            throw new UserAlreadyExistException(
                    "User with login \"%s\" already exist!".formatted(newUserModel.getLogin())
            );
        }
        oldUser.setLogin(newUserModel.getLogin());

        //if we have new password encode it then set it
        if(newUserModel.getPassword() != null) {
            oldUser.setPassword(passwordEncoder.encode(newUserModel.getPassword()));
        }

        //update, save and return entity
        return userRepo.save(oldUser);
    }

    private UserEntity deleteUser(UserEntity user) {
        userRepo.delete(user);
        return user;
    }

    public Spliterator<UserEntity> getAll() {
        return userRepo.findAll().spliterator();
    }

    public UserEntity getOne (Long id) throws UserNotFoundException {
        return userRepo.findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException("User with id %d not found".formatted(id))
                );
    }

    public UserEntity getOne (String login) throws UserNotFoundException {
        return userRepo.findByLogin(login)
                .orElseThrow(
                        () -> new UserNotFoundException("User not found")
                );
    }

    public UserEntity getOneByTelegramId (Long telegramId) throws UserNotFoundException {
        return userRepo.findByTelegramId(telegramId)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "User with telegramId %d not found".formatted(telegramId)
                        )
                );
    }

    public UserEntity create (UserFromRequest model) throws UserAlreadyExistException, UserValidationFailedException {
        // valid entity
        UserValidator userValidator = UserValidator.validateAllFields(model);
        if(userValidator.hasFails()){
            throw new UserValidationFailedException(userValidator.toString());
        }

        // check in repo login unique
        if(userRepo.findByLogin(model.getLogin()).isPresent()) {
            throw new UserAlreadyExistException(
                    "User with login \"%s\" already exist!".formatted(model.getLogin())
            );
        }

        // create entity
        UserEntity entity = UserEntity.builder()
                .login(model.getLogin())
                .password(passwordEncoder.encode(model.getPassword())) // encode password
//                .role(Role.ACTIVATED_USER); // Set default role
                .build();

        return userRepo.save(entity);
    }

    public UserEntity update (Long id, UserFromRequest newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException {
        // load old entity
        UserEntity oldUser = userRepo.findById(id).orElseThrow(
                () -> new UserNotFoundException(
                        "User with id %d not found".formatted(id)
                )
        );

        return updateUser(oldUser, newUser);
    }

    public UserEntity update (String login, UserFromRequest newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException {
        // load old entity
        UserEntity oldUser = userRepo.findByLogin(login).orElseThrow(
                () -> new UserNotFoundException(
                        "User with login \"%s\" not found".formatted(login)
                )
        );

        return updateUser(oldUser, newUser);
    }

    public UserEntity delete(Long id) throws UserNotFoundException {
        // load entity
        UserEntity user = userRepo.findById(id).orElseThrow(
                () -> new UserNotFoundException(
                        "User with id %d not found".formatted(id)
                )
        );

        return deleteUser(user);
    }

    public UserEntity delete(String login) throws UserNotFoundException {
        // load entity
        UserEntity user = userRepo.findByLogin(login).orElseThrow(
                () -> new UserNotFoundException(
                        "User with login \"%s\" not found".formatted(login)
                )
        );

        return deleteUser(user);
    }
}
