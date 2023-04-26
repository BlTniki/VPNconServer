package com.bitniki.VPNconServer.modules.user.service;


import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserAlreadyExistException;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.model.User;

import java.security.Principal;
import java.util.List;

@SuppressWarnings("unused")
public interface UserService {
    List<User> getAll();
    User getOne (Long id) throws UserNotFoundException;
    User getOne (Principal principal) throws UserNotFoundException;
    User getOneByTelegramId (Long telegramId) throws UserNotFoundException;
    User create (UserEntity user) throws UserAlreadyExistException, UserValidationFailedException;
    User update (Long id, UserEntity newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException;
    User update (Principal principal, UserEntity newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException;
    User delete(Long id) throws UserNotFoundException;
    User delete(Principal principal) throws UserNotFoundException;
}
