package com.bitniki.VPNconServer;

import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest extends VpNconServerApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void testGetAllUsers_Valid() {
        // Create model
        List<UserEntity> modelList = List.of(
                new UserEntity(1L, "test", "aA123456", null, 1L, "test", "test"),
                new UserEntity(2L, "test2", "aA123456", null, 2L, "test2", "test2")
        );

        // Get result
        List<UserEntity> resultList = StreamSupport.stream(userService.getAll(), false)
                .toList();

        // Compare
        //assertEquals(model., result.);
        for (int i = 0; i < modelList.size(); i++) {
            var model = modelList.get(i);
            var result = resultList.get(i);

            assertEquals(model.getId(), result.getId());
            assertEquals(model.getLogin(), result.getLogin());
            assertEquals(model.getPassword(), result.getPassword());
            assertEquals(model.getTelegramId(), result.getTelegramId());
            assertEquals(model.getTelegramFirstName(), result.getTelegramFirstName());
            assertEquals(model.getTelegramNickname(), result.getTelegramNickname());
        }
    }

    @Test
    public void testGetOneById_Valid() throws UserNotFoundException {
        UserEntity model = new UserEntity(1L, "test", "aA123456", null, 1L, "test", "test");

        UserEntity result = userService.getOneById(1L);

        // Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getTelegramId(), result.getTelegramId());
        assertEquals(model.getTelegramFirstName(), result.getTelegramFirstName());
        assertEquals(model.getTelegramNickname(), result.getTelegramNickname());
    }

    @Test
    public void testGetOneById_UserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.getOneById(-1L));
    }

    @Test
    public void testGetOneById_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.getOneById(null));
    }

    @Test
    public void testGetOneByLogin_Valid() throws UserNotFoundException {
        UserEntity model = new UserEntity(1L, "test", "aA123456", null, 1L, "test", "test");

        UserEntity result = userService.getOneByLogin("test");

        // Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getTelegramId(), result.getTelegramId());
        assertEquals(model.getTelegramFirstName(), result.getTelegramFirstName());
        assertEquals(model.getTelegramNickname(), result.getTelegramNickname());
    }

    @Test
    public void testGetOneByLogin_UserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.getOneByLogin("HeyImNotExist"));
    }

    @Test
    public void testGetOneByLogin_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.getOneByLogin(null));
    }

    @Test
    public void testGetOneByTelegramId_Valid() throws UserNotFoundException {
        UserEntity model = new UserEntity(1L, "test", "aA123456", null, 1L, "test", "test");

        UserEntity result = userService.getOneByTelegramId(1L);

        // Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getTelegramId(), result.getTelegramId());
        assertEquals(model.getTelegramFirstName(), result.getTelegramFirstName());
        assertEquals(model.getTelegramNickname(), result.getTelegramNickname());
    }

    @Test
    public void testGetOneByTelegramId_UserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.getOneByTelegramId(-1L));
    }

    @Test
    public void testGetOneByTelegramId_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.getOneByTelegramId(null));
    }
}
