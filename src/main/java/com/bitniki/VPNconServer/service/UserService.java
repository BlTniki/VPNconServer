package com.bitniki.VPNconServer.service;

import com.bitniki.VPNconServer.entity.UserEntity;
import com.bitniki.VPNconServer.exception.alreadyExistException.UserAlreadyExistException;
import com.bitniki.VPNconServer.exception.notFoundException.UserNotFoundException;
import com.bitniki.VPNconServer.exception.validationFailedException.UserValidationFailedException;
import com.bitniki.VPNconServer.model.User;
import com.bitniki.VPNconServer.model.UserWithRelations;
import com.bitniki.VPNconServer.repository.UserRepo;
import com.bitniki.VPNconServer.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserEntity updateUser(UserEntity oldUser, UserEntity newUser) throws UserValidationFailedException, UserAlreadyExistException {
        // valid new entity
        UserValidator userValidator = UserValidator.validateNonNullFields(newUser);
        if(userValidator.hasFails()){
            throw new UserValidationFailedException(userValidator.toString());
        }

        //if we have new login check its unique
        if (newUser.getLogin() != null && userRepo.findByLogin(newUser.getLogin()) != null) {
            throw new UserAlreadyExistException("User with that login already exist!");
        }
        //if we have new password encode it
        if(newUser.getPassword() != null) newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        //update, save and return entity
        return userRepo.save(UserEntity.updateEntity(oldUser,newUser));
    }

    private UserEntity deleteUser(UserEntity user) {
        userRepo.delete(user);
        return user;
    }

    public List<UserWithRelations> getAll() {
        List<UserEntity> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);
        return users.stream().map(UserWithRelations::toModel).collect(Collectors.toList());
    }

    public UserWithRelations getOne (Long id) throws UserNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepo.findById(id);
        if(userEntityOptional.isPresent()) return UserWithRelations.toModel(userEntityOptional.get());
        else throw new UserNotFoundException("User not found");
    }

    public UserWithRelations getOne (Principal principal) throws UserNotFoundException {
        UserEntity user = userRepo.findByLogin(principal.getName());
        if(user == null) throw new UserNotFoundException("User not found");

        return UserWithRelations.toModel(user);
    }

    public UserWithRelations getOneByTelegramId (Long telegramId) throws UserNotFoundException {
        return UserWithRelations.toModel(userRepo.findByTelegramId(telegramId).orElseThrow(
                () -> new UserNotFoundException("User not found")
        ));
    }

    public User create (UserEntity user) throws UserAlreadyExistException, UserValidationFailedException {
        // valid entity
        UserValidator userValidator = UserValidator.validateAllFields(user);
        if(userValidator.hasFails()){
            throw new UserValidationFailedException(userValidator.toString());
        }
        // check in repo login unique
        if(userRepo.findByLogin(user.getLogin()) != null) {
            throw new UserAlreadyExistException("That login already taken!");
        }

        // configure entity
        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return User.toModel(userRepo.save(user));
    }

    public User update (Long id, UserEntity newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException {
        // load old entity
        Optional<UserEntity> oldUserEntityOptional;
        UserEntity oldUser;
        oldUserEntityOptional = userRepo.findById(id);
        if(oldUserEntityOptional.isPresent()) oldUser = oldUserEntityOptional.get();
        else throw new UserNotFoundException("User not found");

        return User.toModel(updateUser(oldUser, newUser));
    }

    public User update (Principal principal, UserEntity newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException {
        // load old entity
        UserEntity oldUser = userRepo.findByLogin(principal.getName());
        if(oldUser == null) throw new UserNotFoundException("User not found");

        return User.toModel(updateUser(oldUser, newUser));
    }

    public User delete(Long id) throws UserNotFoundException {
        // load entity
        Optional<UserEntity> userEntityOptional;
        UserEntity user;
        userEntityOptional = userRepo.findById(id);
        if(userEntityOptional.isPresent()) user = userEntityOptional.get();
        else throw new UserNotFoundException("User not found");

        return User.toModel(deleteUser(user));
    }

    public User delete(Principal principal) throws UserNotFoundException {
        // load entity
        UserEntity user = userRepo.findByLogin(principal.getName());
        if(user == null) throw new UserNotFoundException("User not found");

        return User.toModel(deleteUser(user));
    }
}
