package com.bitniki.VPNconServer.metacode;

import com.bitniki.VPNconServer.VpNconServerApplicationTests;
import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.metacodes.model.MetacodeToUse;
import com.bitniki.VPNconServer.modules.metacodes.service.MetacodeService;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.role.Role;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Rollback
public class MetacodeServiceTest extends VpNconServerApplicationTests {
    @Autowired
    private MetacodeService metacodeService;
    @Autowired
    private UserService userService;

    @Test
    public void testResolvingCodes_Valid() throws EntityNotFoundException {
        Long MODEL_USER_ID = 105L;
        String ADMIN_CODE = "blabla2";

        UserEntity user = userService.getOneById(MODEL_USER_ID);

        MetacodeToUse metacodeToUse = MetacodeToUse.builder().code(ADMIN_CODE).build();
        metacodeService.resolveMetacodeByLogin(user.getLogin(), metacodeToUse);

        user = userService.getOneById(MODEL_USER_ID);

        assertEquals(user.getRole(), Role.ADMIN);
    }
}
