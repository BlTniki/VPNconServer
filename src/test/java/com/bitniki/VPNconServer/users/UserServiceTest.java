package com.bitniki.VPNconServer.users;

import com.bitniki.VPNconServer.VpNconServerApplicationTests;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserAlreadyExistException;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.model.UserFromRequest;
import com.bitniki.VPNconServer.modules.role.Role;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Transactional
@Rollback
public class UserServiceTest extends VpNconServerApplicationTests {

    @Autowired
    private UserService userService;
    @Value("${tg.user.password}")
    private String tgBotPassword;
    @Value("${accountant.user.password}")
    private String accountantBotPassword;

    @Test
    public void testGetAllUsers_Valid() {
        // Create model
        List<UserEntity> modelList = List.of(
                new UserEntity(101L, "telegramBot", "$2a$12$wZxtK/hdO9ylNN3BrvrB4uXPWF3JHukBEuTnbx2x60F62wvQTFnTO", Role.ADMIN, null, null, null),
                new UserEntity(102L, "accountant", "$2a$12$OmT1v3R579qFuryy6Hms0e/DxjYP0Ad8Wi6lEdSpZRQ.GK2BVyREi", Role.ADMIN, null, null, null),
                new UserEntity(103L, "test", "aA123456", Role.ACTIVATED_USER, null, 1L, "test"),
                new UserEntity(104L, "test2", "aA123456", Role.ACTIVATED_USER, null, 2L, "test2")
        );

        // Get result
        List<UserEntity> resultList = StreamSupport.stream(userService.getAll(), false)
                .toList();

        // Compare
        for (int i = 0; i < modelList.size(); i++) {
            var model = modelList.get(i);
            var result = resultList.get(i);

            assertEquals(model.getLogin(), result.getLogin());
            assertEquals(model.getPassword(), result.getPassword());
            assertEquals(model.getRole(), result.getRole());
            assertEquals(model.getTelegramId(), result.getTelegramId());
            assertEquals(model.getTelegramNickname(), result.getTelegramNickname());
        }
    }

    @Test
    public void testGetOneById_Valid() throws UserNotFoundException {
        UserEntity model = new UserEntity(103L, "test", "aA123456", Role.ACTIVATED_USER, null, 1L, "test");

        UserEntity result = userService.getOneById(103L);

        // Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getRole(), result.getRole());
        assertEquals(model.getTelegramId(), result.getTelegramId());
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
        UserEntity model = new UserEntity(103L, "test", "aA123456", Role.ACTIVATED_USER, null, 1L, "test");

        UserEntity result = userService.getOneByLogin("test");

        // Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getRole(), result.getRole());
        assertEquals(model.getTelegramId(), result.getTelegramId());
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
        UserEntity model = new UserEntity(103L, "test", "aA123456", Role.ACTIVATED_USER, null, 1L, "test");

        UserEntity result = userService.getOneByTelegramId(1L);

        // Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getRole(), result.getRole());
        assertEquals(model.getTelegramId(), result.getTelegramId());
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
        UserEntity model = new UserEntity(null, "test3", "aA123456", Role.ACTIVATED_USER, null, 1L, "test");

        UserFromRequest toCreate = new UserFromRequest("test3", "aA123456", null, null);
        UserEntity result = userService.create(toCreate);

        //Compare
        assertEquals(model.getLogin(), result.getLogin());
        assertNotEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getRole(), result.getRole());
        assertEquals(60, result.getPassword().length());
    }

    @Test
    public void testCreate_UserValidationFailedException_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.create(null));

        UserFromRequest toCreate = new UserFromRequest(null, null, null, null);
        Exception exception = assertThrows(
                UserValidationFailedException.class,
                () -> userService.create(toCreate)
        );
        assertTrue(exception.getMessage().contains("login") && exception.getMessage().contains("password"));
    }

    @Test
    public void testCreate_UserValidationFailedException_Incorrect_Fields() {
        UserFromRequest toCreate = new UserFromRequest("re2./.!#$$%^^", "a", null, null);
        Exception exception = assertThrows(
                UserValidationFailedException.class,
                () -> userService.create(toCreate)
        );
        assertTrue(exception.getMessage().contains("login") && exception.getMessage().contains("password"));
    }

    @Test
    public void testCreate_UserAlreadyExistException() {
        UserFromRequest toCreate = new UserFromRequest("test", "aA123456", null, null);
        assertThrows(UserAlreadyExistException.class, () -> userService.create(toCreate));
    }

    @Test
    public void testUpdateById_Valid() throws UserNotFoundException, UserAlreadyExistException, UserValidationFailedException {
        UserEntity model = new UserEntity(103L, "newTest", "aA654321", Role.ACTIVATED_USER, null, 1L, "test");

        UserFromRequest toUpdate = new UserFromRequest("newTest", "aA654321", null, null);
        UserEntity result = userService.updateById(103L, toUpdate);

        //Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertNotEquals(model.getPassword(), result.getPassword());
        assertEquals(60, result.getPassword().length());
    }

    @Test
    public void testUpdateById_NullField() throws UserNotFoundException, UserAlreadyExistException, UserValidationFailedException {
        UserFromRequest toUpdate = new UserFromRequest(null, null, null, null);
        UserEntity result = userService.updateById(103L, toUpdate);

        //Compare
        assertEquals(103L, result.getId());
        assertNotEquals(null, result.getLogin());
        assertNotEquals(null, result.getPassword());
    }

    @Test
    public void testUpdateById_UserValidationFailedException_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.updateById( 103L,null));
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.updateById( null,new UserFromRequest()));
    }

    @Test
    public void testUpdateById_UserValidationFailedException() {
        UserFromRequest toCreate = new UserFromRequest("re2./.!#$$%^^", "a", null, null);
        Exception exception = assertThrows(
                UserValidationFailedException.class,
                () -> userService.updateById(103L, toCreate)
        );
        assertTrue(exception.getMessage().contains("login") && exception.getMessage().contains("password"));
    }

    @Test
    public void testUpdateById_UserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.updateById( -1L,new UserFromRequest()));
    }

    @Test
    public void testUpdateById_UserAlreadyExistException() {
        assertThrows(UserAlreadyExistException.class, () ->
                userService.updateById( 103L,new UserFromRequest("test2", "aA123456", null, null))
        );
    }

    @Test
    public void testUpdateByLogin_Valid() throws UserNotFoundException, UserAlreadyExistException, UserValidationFailedException {
        UserEntity model = new UserEntity(103L, "newTest", "aA654321", Role.ACTIVATED_USER, null, 1L, "test");

        UserFromRequest toUpdate = new UserFromRequest("newTest", "aA654321", null, null);
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
        UserFromRequest toCreate = new UserFromRequest("re2./.!#$$%^^", "a", null, null);
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
        assertThrows(UserAlreadyExistException.class, () ->
                userService.updateByLogin( "test",new UserFromRequest("test2", "aA123456", null, null))
        );
    }

    @Test
    public void testDeleteById_Valid() throws UserNotFoundException {
        UserEntity model = new UserEntity(103L, "test", "aA123456", Role.ACTIVATED_USER, null, 1L, "test");

        UserEntity result = userService.deleteById(103L);
        assertThrows(UserNotFoundException.class, () -> userService.deleteById(103L));

        // Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getTelegramId(), result.getTelegramId());
        assertEquals(model.getTelegramNickname(), result.getTelegramNickname());
    }

    @Test
    public void testDeleteById_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.deleteById(null));
    }

    @Test
    public void testDeleteByLogin_Valid() throws UserNotFoundException {
        UserEntity model = new UserEntity(103L, "test", "aA123456", Role.ACTIVATED_USER, null, 1L, "test");

        UserEntity result = userService.deleteByLogin("test");
        assertThrows(UserNotFoundException.class, () -> userService.deleteByLogin("test"));

        // Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getTelegramId(), result.getTelegramId());
        assertEquals(model.getTelegramNickname(), result.getTelegramNickname());
    }

    @Test
    public void testDeleteByLogin_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> userService.deleteByLogin(null));
    }

    @Test
    public void testAuthAndCreateToken_Valid() throws UserNotFoundException, UserValidationFailedException {
        var model = new HashMap<String, String>();
        model.put("login", "telegramBot");
        model.put("token", null);

        UserFromRequest toAuth = UserFromRequest.builder().login("telegramBot").password(tgBotPassword).build();
        Map<String, String> result = userService.authAndCreateToken(toAuth);

        //Compare
        assertEquals(model.get("login"), result.get("login"));
        assertNotEquals(model.get("token"), result.get("token"));
        assertEquals(result.get("token"), userService.getOneByLogin("telegramBot").getToken());
    }

    @Test
    public void testAuthAndCreateToken_BadCredentialsException() {
        var toAuth = UserFromRequest.builder().login("telegramBot").password("aA123456").build();
        assertThrows( BadCredentialsException.class, () -> userService.authAndCreateToken(toAuth) );
    }

    @Test
    public void testAuthAndCreateToken_UserValidationFailedException() {
        UserFromRequest toAuth = new UserFromRequest("re2./.!#$$%^^", "a", null, null);
        Exception exception = assertThrows(
                UserValidationFailedException.class,
                () -> userService.authAndCreateToken(toAuth)
        );
        assertTrue(exception.getMessage().contains("login") && exception.getMessage().contains("password"));
    }

    @Test
    public void testLogout_Valid() throws UserNotFoundException {
        //auth
        UserFromRequest toAuth = UserFromRequest.builder().login("telegramBot").password(tgBotPassword).build();
        UserEntity authenticatedUser = userService.getOneByLogin(toAuth.getLogin());

        //logout
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn(toAuth.getLogin());
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getUserPrincipal()).thenReturn(mockPrincipal);
        userService.logout(mockRequest);

        //check is token null
        assertNull(authenticatedUser.getToken());
    }

    @Test
    public void testLogout_UserNotFoundException() {
        UserFromRequest toAuth = new UserFromRequest("re2./.!#$$%^^", "a", null, null);
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn(toAuth.getLogin());
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getUserPrincipal()).thenReturn(mockPrincipal);

        assertThrows( UserNotFoundException.class, () -> userService.logout(mockRequest) );
    }

    @Test
    public void testAssociateTelegram_Valid() throws UserNotFoundException, UserValidationFailedException {
        UserEntity model = new UserEntity(103L, "test", "aA123456", Role.ACTIVATED_USER, null, 103L, "dwatest");

        UserFromRequest toAssociate = UserFromRequest.builder().login("test").telegramId(103L).telegramNickname("dwatest").build();
        UserEntity result = userService.associateTelegram(toAssociate);

        // Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getRole(), result.getRole());
        assertEquals(model.getTelegramId(), result.getTelegramId());
        assertEquals(model.getTelegramNickname(), result.getTelegramNickname());
    }

    @Test
    public void testAssociateTelegram_UserValidationFailedException_Null() {
        UserFromRequest toAssociate = UserFromRequest.builder().login(null).telegramId(103L).telegramNickname("dwatest").build();
        Exception exception = assertThrows(
                UserValidationFailedException.class,
                () -> userService.associateTelegram(toAssociate)
        );
        assertTrue(exception.getMessage().contains("No login"));

        UserFromRequest toAssociate2 = UserFromRequest.builder().login("test").telegramId(null).telegramNickname("dwatest").build();
        Exception exception2 = assertThrows(
                UserValidationFailedException.class,
                () -> userService.associateTelegram(toAssociate2)
        );
        assertTrue(exception2.getMessage().contains("No telegramId or"));
    }

    @Test
    public void testAssociateTelegram_UserNotFoundException() {
        UserFromRequest toAssociate = UserFromRequest.builder().login("test12345").telegramId(103L).telegramNickname("dwatest").build();
        assertThrows( UserNotFoundException.class, () -> userService.associateTelegram(toAssociate) );
    }

    @Test
    public void testDissociateTelegram_Valid() throws UserNotFoundException, UserValidationFailedException {
        UserEntity model = new UserEntity(103L, "test", "aA123456", Role.ACTIVATED_USER, null, null, null);

        UserFromRequest toAssociate = UserFromRequest.builder().login("test").build();
        UserEntity result = userService.dissociateTelegram(toAssociate);

        // Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getRole(), result.getRole());
        assertEquals(model.getTelegramId(), result.getTelegramId());
        assertEquals(model.getTelegramNickname(), result.getTelegramNickname());
    }

    @Test
    public void testDissociateTelegram_UserValidationFailedException_Null() {
        UserFromRequest toAssociate = UserFromRequest.builder().login(null).build();
        Exception exception = assertThrows(
                UserValidationFailedException.class,
                () -> userService.dissociateTelegram(toAssociate)
        );
        assertTrue(exception.getMessage().contains("No login"));
    }

    @Test
    public void testDissociateTelegram_UserNotFoundException() {
        UserFromRequest toAssociate = UserFromRequest.builder().login("test312frwp").build();
        assertThrows( UserNotFoundException.class, () -> userService.dissociateTelegram(toAssociate) );
    }

    @Test
    public void testGetValidationRegex() {
        Map<String, String> model = new HashMap<>();
        model.put("login", "^[a-zA-Z][a-zA-Z0-9-_.]{1,20}$");
        model.put("password", "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$");

        assertEquals(model, userService.getValidationRegex());
    }

    @Test
    public void testUpdateUserRole() throws UserNotFoundException {
        UserEntity model = new UserEntity(103L, "test", "aA123456", Role.ACTIVATED_CLOSE_USER, null, null, null);

        UserEntity result = userService.updateUserRole(103L, Role.ACTIVATED_CLOSE_USER);

        // Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getLogin(), result.getLogin());
        assertEquals(model.getRole(), result.getRole());
    }
}
