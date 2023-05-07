package com.bitniki.VPNconServer;

import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserAlreadyExistException;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.model.UserFromRequest;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Rollback
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

    @Test
    public void testCreate_Valid() throws UserAlreadyExistException, UserValidationFailedException {
        UserEntity model = new UserEntity(null, "test3", "aA123456", null, 1L, "test", "test");

        UserFromRequest toCreate = new UserFromRequest("test3", "aA123456");
        UserEntity result = userService.create(toCreate);

        //Compare
        assertEquals(model.getLogin(), result.getLogin());
        assertNotEquals(model.getPassword(), result.getPassword());
        assertEquals(60, result.getPassword().length());
    }

    @Test
    public void testCreate_UserValidationFailedException_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.create(null));

        UserFromRequest toCreate = new UserFromRequest(null, null);
        Exception exception = assertThrows(
                UserValidationFailedException.class,
                () -> userService.create(toCreate)
        );
        assertTrue(exception.getMessage().contains("login") && exception.getMessage().contains("password"));
    }

    @Test
    public void testCreate_UserValidationFailedException_Incorrect_Fields() {
        UserFromRequest toCreate = new UserFromRequest("re2./.!#$$%^^", "a");
        Exception exception = assertThrows(
                UserValidationFailedException.class,
                () -> userService.create(toCreate)
        );
        assertTrue(exception.getMessage().contains("login") && exception.getMessage().contains("password"));
    }

    @Test
    public void testCreate_UserAlreadyExistException() {
        UserFromRequest toCreate = new UserFromRequest("test", "aA123456");
        assertThrows(UserAlreadyExistException.class, () -> userService.create(toCreate));
    }

    @Test
    public void testUpdateById_Valid() throws UserNotFoundException, UserAlreadyExistException, UserValidationFailedException {
        UserEntity model = new UserEntity(1L, "newTest", "aA654321", null, 1L, "test", "test");

        UserFromRequest toUpdate = new UserFromRequest("newTest", "aA654321");
        UserEntity result = userService.updateById(1L, toUpdate);

        //Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertNotEquals(model.getPassword(), result.getPassword());
        assertEquals(60, result.getPassword().length());
    }

    @Test
    public void testUpdateById_NullField() throws UserNotFoundException, UserAlreadyExistException, UserValidationFailedException {
        UserFromRequest toUpdate = new UserFromRequest(null, null);
        UserEntity result = userService.updateById(1L, toUpdate);

        //Compare
        assertEquals(1L, result.getId());
        assertNotEquals(null, result.getLogin());
        assertNotEquals(null, result.getPassword());
    }

    @Test
    public void testUpdateById_UserValidationFailedException_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.updateById( 1L,null));
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.updateById( null,new UserFromRequest()));
    }

    @Test
    public void testUpdateById_UserValidationFailedException() {
        UserFromRequest toCreate = new UserFromRequest("re2./.!#$$%^^", "a");
        Exception exception = assertThrows(
                UserValidationFailedException.class,
                () -> userService.updateById(1L, toCreate)
        );
        assertTrue(exception.getMessage().contains("login") && exception.getMessage().contains("password"));
    }

    @Test
    public void testUpdateById_UserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.updateById( -1L,new UserFromRequest()));
    }

    @Test
    public void testUpdateById_UserAlreadyExistException() {
        assertThrows(UserAlreadyExistException.class, () -> userService.updateById( 1L,new UserFromRequest("test2", "aA123456")));
    }

    @Test
    public void testUpdateByLogin_Valid() throws UserNotFoundException, UserAlreadyExistException, UserValidationFailedException {
        UserEntity model = new UserEntity(1L, "newTest", "aA654321", null, 1L, "test", "test");

        UserFromRequest toUpdate = new UserFromRequest("newTest", "aA654321");
        UserEntity result = userService.updateByLogin("test", toUpdate);

        //Compare
        assertEquals(model.getLogin(), result.getLogin());
        assertNotEquals(model.getPassword(), result.getPassword());
        assertEquals(60, result.getPassword().length());
    }

    @Test
    public void testUpdateByLogin_UserValidationFailedException_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.updateByLogin( "test",null));
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.updateByLogin( null,new UserFromRequest()));
    }

    @Test
    public void testUpdateByLogin_UserValidationFailedException() {
        UserFromRequest toCreate = new UserFromRequest("re2./.!#$$%^^", "a");
        Exception exception = assertThrows(
                UserValidationFailedException.class,
                () -> userService.updateByLogin("test", toCreate)
        );
        assertTrue(exception.getMessage().contains("login") && exception.getMessage().contains("password"));
    }

    @Test
    public void testUpdateByLogin_UserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.updateByLogin( "-1L",new UserFromRequest()));
    }

    @Test
    public void testUpdateByLogin_UserAlreadyExistException() {
        assertThrows(UserAlreadyExistException.class, () -> userService.updateByLogin( "test",new UserFromRequest("test2", "aA123456")));
    }

    @Test
    public void testDeleteById_Valid() throws UserNotFoundException {
        UserEntity model = new UserEntity(1L, "test", "aA123456", null, 1L, "test", "test");

        UserEntity result = userService.deleteById(1L);
        assertThrows(UserNotFoundException.class, () -> userService.deleteById(1L));

        // Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getTelegramId(), result.getTelegramId());
        assertEquals(model.getTelegramFirstName(), result.getTelegramFirstName());
        assertEquals(model.getTelegramNickname(), result.getTelegramNickname());
    }

    @Test
    public void testDeleteById_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.deleteById(null));
    }

    @Test
    public void testDeleteByLogin_Valid() throws UserNotFoundException {
        UserEntity model = new UserEntity(1L, "test", "aA123456", null, 1L, "test", "test");

        UserEntity result = userService.deleteByLogin("test");
        assertThrows(UserNotFoundException.class, () -> userService.deleteByLogin("test"));

        // Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getTelegramId(), result.getTelegramId());
        assertEquals(model.getTelegramFirstName(), result.getTelegramFirstName());
        assertEquals(model.getTelegramNickname(), result.getTelegramNickname());
    }

    @Test
    public void testDeleteByLogin_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.deleteByLogin(null));
    }
}
