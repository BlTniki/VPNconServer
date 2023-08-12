package com.bitniki.VPNconServer.modules.user.service.impl;

import com.bitniki.VPNconServer.modules.role.Role;
import com.bitniki.VPNconServer.modules.security.jwt.JwtTokenProvider;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserAlreadyExistException;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.model.Token;
import com.bitniki.VPNconServer.modules.user.model.UserFromRequest;
import com.bitniki.VPNconServer.modules.user.repository.UserRepo;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import com.bitniki.VPNconServer.modules.user.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Spliterator;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${tg.user.password}")
    String tgPassword;
    @Value("${accountant.user.password}")
    String accountantPassword;

    /**
     *
     * Создает пользователей по умолчанию, если они не существуют.
     *
     */
    @PostConstruct
    public void createDefaultUsersIfNotExist() {
        if(userRepo.findByLogin("telegramBot").isEmpty() && tgPassword != null) {
            //Create user for telegram bot
            UserFromRequest bot = UserFromRequest.builder().login("telegramBot").password(tgPassword).build();
            UserEntity botEntity;
            try {
                botEntity = create(bot);
            } catch (UserAlreadyExistException | UserValidationFailedException e) {
                throw new RuntimeException(e);
            }

            //Change default role
            botEntity.setRole(Role.ADMIN);

            //save changes
            userRepo.save(botEntity);
        }
        if(userRepo.findByLogin("accountant").isEmpty() && accountantPassword != null) {
            //Create user for accountant bot
            UserFromRequest bot = UserFromRequest.builder().login("accountant").password(accountantPassword).build();
            UserEntity botEntity;
            try {
                botEntity = create(bot);
            } catch (UserAlreadyExistException | UserValidationFailedException e) {
                throw new RuntimeException(e);
            }

            //Change default role
            botEntity.setRole(Role.ADMIN);

            //save changes
            userRepo.save(botEntity);
        }
    }

    private UserEntity updateUser(@NotNull UserEntity oldUser, @NotNull UserFromRequest newUserModel) throws UserValidationFailedException, UserAlreadyExistException {
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
        if (newUserModel.getLogin() != null) {
            oldUser.setLogin(newUserModel.getLogin());
        }

        //if we have new password encode it then set it
        if(newUserModel.getPassword() != null) {
            oldUser.setPassword(passwordEncoder.encode(newUserModel.getPassword()));
        }

        //update, save and return entity
        return userRepo.save(oldUser);
    }

    private UserEntity deleteUser(@NotNull UserEntity user) {
        userRepo.delete(user);
        return user;
    }

    public Spliterator<UserEntity> getAll() {
        return userRepo.findAll().spliterator();
    }

    public UserEntity getOneById(@NotNull Long id) throws UserNotFoundException {
        return userRepo.findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException("User with id %d not found".formatted(id))
                );
    }

    public UserEntity getOneByLogin(@NotNull String login) throws UserNotFoundException {
        return userRepo.findByLogin(login)
                .orElseThrow(
                        () -> new UserNotFoundException("User not found")
                );
    }

    public UserEntity getOneByTelegramId (@NotNull Long telegramId) throws UserNotFoundException {
        return userRepo.findByTelegramId(telegramId)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "User with telegramId %d not found".formatted(telegramId)
                        )
                );
    }

    public UserEntity create (@NotNull UserFromRequest model) throws UserAlreadyExistException, UserValidationFailedException {
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
                .role(Role.ACTIVATED_USER) // Set default role
                .build();

        return userRepo.save(entity);
    }

    public UserEntity updateById(@NotNull Long id, @NotNull UserFromRequest newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException {
        // load old entity
        UserEntity oldUser = userRepo.findById(id).orElseThrow(
                () -> new UserNotFoundException(
                        "User with id %d not found".formatted(id)
                )
        );

        return updateUser(oldUser, newUser);
    }

    public UserEntity updateByLogin(@NotNull String login, @NotNull UserFromRequest newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException {
        // load old entity
        UserEntity oldUser = userRepo.findByLogin(login).orElseThrow(
                () -> new UserNotFoundException(
                        "User with login \"%s\" not found".formatted(login)
                )
        );

        return updateUser(oldUser, newUser);
    }

    public UserEntity deleteById(@NotNull Long id) throws UserNotFoundException {
        // load entity
        UserEntity user = userRepo.findById(id).orElseThrow(
                () -> new UserNotFoundException(
                        "User with id %d not found".formatted(id)
                )
        );

        return deleteUser(user);
    }

    public UserEntity deleteByLogin(@NotNull String login) throws UserNotFoundException {
        // load entity
        UserEntity user = userRepo.findByLogin(login).orElseThrow(
                () -> new UserNotFoundException(
                        "User with login \"%s\" not found".formatted(login)
                )
        );

        return deleteUser(user);
    }

    public Token authAndCreateToken(@NotNull UserFromRequest model) throws UserNotFoundException,
            UserValidationFailedException, AuthenticationException {
        // validate model
        UserValidator userValidator = UserValidator.validateAllFields(model);
        if(userValidator.hasFails()){
            throw new UserValidationFailedException(userValidator.toString());
        }

        // auth user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(model.getLogin(), model.getPassword())
        );

        // load user from repo
        UserEntity user = getOneByLogin(model.getLogin());

        // generate token
        String token = jwtTokenProvider.createToken(model.getLogin(), user.getRole().name());
        // set token_expired false and save
        user.setToken(token);
        userRepo.save(user);

        // make response
        return Token.builder()
                .login(user.getLogin())
                .token(user.getToken())
                .build();
    }

    public void logout(@NotNull String login) throws UserNotFoundException {
        // load user from repo
        UserEntity user = userRepo.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User with login %s not found".formatted(login)));

        // set null and save
        user.setToken(null);
        userRepo.save(user);
    }

    public UserEntity associateTelegram(@NotNull UserFromRequest model) throws UserNotFoundException, UserValidationFailedException {
        // validate model
        if(model.getLogin() == null){
            throw new UserValidationFailedException("No login are given");
        }
        if(model.getTelegramId() == null || model.getTelegramNickname() == null)
            throw new UserValidationFailedException("No telegramId or telegramNickname are given");

        // load user from repo
        UserEntity user = userRepo.findByLogin(model.getLogin())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // set telegram id and save
        user.setTelegramId(model.getTelegramId());
        user.setTelegramNickname(model.getTelegramNickname());
        return userRepo.save(user);
    }

    public UserEntity dissociateTelegram(@NotNull String login) throws UserNotFoundException {

        // load user from repo
        UserEntity user = userRepo.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User with login %s not found".formatted(login)));

        // del telegram id and save
        user.setTelegramId(null);
        user.setTelegramNickname(null);
        return userRepo.save(user);
    }

    public Map<String, String> getValidationRegex() {
        //Get regex from validator and cook answer
        Map<String, String> patterns = new HashMap<>();
        patterns.put("login", UserValidator.loginPattern.pattern());
        patterns.put("password", UserValidator.passwordPattern.pattern());

        return patterns;
    }

    public UserEntity updateUserRole(@NotNull Long id, @NotNull Role newRole) throws UserNotFoundException {
        // load user
        UserEntity user = userRepo.findById(id).orElseThrow(
                () -> new UserNotFoundException(
                        "User with id %d not found".formatted(id)
                )
        );

        // update role
        user.setRole(newRole);

        // save and return
        return userRepo.save(user);
    }
}
