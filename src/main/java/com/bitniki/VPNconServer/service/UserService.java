package com.bitniki.VPNconServer.service;

import com.bitniki.VPNconServer.entity.UserEntity;
import com.bitniki.VPNconServer.exception.UserAlreadyExistException;
import com.bitniki.VPNconServer.exception.UserNotFoundException;
import com.bitniki.VPNconServer.model.User;
import com.bitniki.VPNconServer.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public List<User> getAll() {
        List<UserEntity> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);
        return users.stream().map(User::toModel).collect(Collectors.toList());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public User getOne (Long id) throws UserNotFoundException {
        try {
            return User.toModel(userRepo.findById(id).get());
        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("User not found");
        }
    }

    public User create (UserEntity user) throws UserAlreadyExistException {
        if(userRepo.findByLogin(user.getLogin()) != null) {
            throw new UserAlreadyExistException("That login already taken!");
        }
        return User.toModel(userRepo.save(user));
    }

    public User update (Long id, UserEntity newUser) throws UserAlreadyExistException, UserNotFoundException {
        //if we have new login check its unique
        if (newUser.getLogin() != null && userRepo.findByLogin(newUser.getLogin()) != null) {
            throw new UserAlreadyExistException("User with that login already exist!");
        }

        Optional<UserEntity> oldEntity;
        UserEntity oldUser;
        oldEntity = userRepo.findById(id);
        if(oldEntity.isPresent()) oldUser = oldEntity.get();
        else throw new UserNotFoundException("User not found");

        UserEntity.updateEntity(oldUser,newUser);
        return User.toModel(userRepo.save(oldUser));
    }
}
