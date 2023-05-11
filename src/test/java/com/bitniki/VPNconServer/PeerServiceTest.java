package com.bitniki.VPNconServer;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.EntityValidationFailedException;
import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.peer.connectHandler.exception.PeerConnectHandlerException;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.peer.exception.PeerAlreadyExistException;
import com.bitniki.VPNconServer.modules.peer.exception.PeerNotFoundException;
import com.bitniki.VPNconServer.modules.peer.model.PeerFromRequest;
import com.bitniki.VPNconServer.modules.peer.service.PeerService;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Rollback
public class PeerServiceTest extends VpNconServerApplicationTests {
    @Autowired
    private PeerService peerService;

    @Test
    public void testGetAll_Valid() {
        List<PeerEntity> modelList = List.of(
                new PeerEntity(
                        1L,
                        "test",
                        "10.8.0.10",
                        "private",
                        "public",
                        true,
                        UserEntity.builder().id(1L).build(),
                        HostEntity.builder().id(1L).build()
                ),
                new PeerEntity(
                        2L,
                        "test2",
                        "10.8.0.11",
                        "private",
                        "public",
                        true,
                        UserEntity.builder().id(1L).build(),
                        HostEntity.builder().id(1L).build()
                )
        );

        List<PeerEntity> resultList = StreamSupport.stream(peerService.getAll(), false)
                .toList();

        //Compare
        for (int i = 0; i < modelList.size(); i++) {
            var model = modelList.get(i);
            var result = resultList.get(i);

            assertEquals(model.getId(), result.getId());
            assertEquals(model.getPeerConfName(), result.getPeerConfName());
            assertEquals(model.getPeerIp(), result.getPeerIp());
            assertEquals(model.getPeerPrivateKey(), result.getPeerPrivateKey());
            assertEquals(model.getPeerPublicKey(), result.getPeerPublicKey());
            assertEquals(model.getIsActivated(), result.getIsActivated());
            assertEquals(model.getUser().getId(), result.getUser().getId());
            assertEquals(model.getHost().getId(), result.getHost().getId());
        }
    }

    @Test
    public void testGetAllByLogin_Valid() {
        List<PeerEntity> modelList = List.of(
                new PeerEntity(
                        1L,
                        "test",
                        "10.8.0.10",
                        "private",
                        "public",
                        true,
                        UserEntity.builder().id(1L).build(),
                        HostEntity.builder().id(1L).build()
                ),
                new PeerEntity(
                        2L,
                        "test2",
                        "10.8.0.11",
                        "private",
                        "public",
                        true,
                        UserEntity.builder().id(1L).build(),
                        HostEntity.builder().id(1L).build()
                )
        );

        List<PeerEntity> resultList = StreamSupport.stream(peerService.getAllByLogin("test"), false)
                .toList();

        //Compare
        assertEquals(2, resultList.size());
        for (int i = 0; i < modelList.size(); i++) {
            var model = modelList.get(i);
            var result = resultList.get(i);

            assertEquals(model.getId(), result.getId());
            assertEquals(model.getPeerConfName(), result.getPeerConfName());
            assertEquals(model.getPeerIp(), result.getPeerIp());
            assertEquals(model.getPeerPrivateKey(), result.getPeerPrivateKey());
            assertEquals(model.getPeerPublicKey(), result.getPeerPublicKey());
            assertEquals(model.getIsActivated(), result.getIsActivated());
            assertEquals(model.getUser().getId(), result.getUser().getId());
            assertEquals(model.getHost().getId(), result.getHost().getId());
        }
    }

    @Test
    public void testGetOneById_Valid() throws PeerNotFoundException {
        PeerEntity model = new PeerEntity(
                1L,
                "test",
                "10.8.0.10",
                "private",
                "public",
                true,
                UserEntity.builder().id(1L).build(),
                HostEntity.builder().id(1L).build()
        );

        PeerEntity result = peerService.getOneById(1L);

        //Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getPeerConfName(), result.getPeerConfName());
        assertEquals(model.getPeerIp(), result.getPeerIp());
        assertEquals(model.getPeerPrivateKey(), result.getPeerPrivateKey());
        assertEquals(model.getPeerPublicKey(), result.getPeerPublicKey());
        assertEquals(model.getIsActivated(), result.getIsActivated());
        assertEquals(model.getUser().getId(), result.getUser().getId());
        assertEquals(model.getHost().getId(), result.getHost().getId());
    }

    @Test
    public void testGetOneById_PeerNotFoundException() {
        assertThrows(PeerNotFoundException.class, () -> peerService.getOneById(-1L));
    }

    @Test
    public void testGetOneById_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> peerService.getOneById(null));
    }

    @Test
    public void testGetOneByLoginAndId_Valid() throws EntityNotFoundException {
        PeerEntity model = new PeerEntity(
                1L,
                "test",
                "10.8.0.10",
                "private",
                "public",
                true,
                UserEntity.builder().id(1L).login("test").build(),
                HostEntity.builder().id(1L).build()
        );

        PeerEntity result = peerService.getOneByLoginAndId( "test", 1L);

        //Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getPeerConfName(), result.getPeerConfName());
        assertEquals(model.getPeerIp(), result.getPeerIp());
        assertEquals(model.getPeerPrivateKey(), result.getPeerPrivateKey());
        assertEquals(model.getPeerPublicKey(), result.getPeerPublicKey());
        assertEquals(model.getIsActivated(), result.getIsActivated());
        assertEquals(model.getUser().getId(), result.getUser().getId());
        assertEquals(model.getUser().getLogin(), result.getUser().getLogin());
        assertEquals(model.getHost().getId(), result.getHost().getId());
    }

    @Test
    public void testGetOneByLoginAndId_PeerNotFoundException() {
        assertThrows(PeerNotFoundException.class, () -> peerService.getOneByLoginAndId("test", -1L));
    }

    @Test
    public void testGetOneByLoginAndId_PeerNotFoundException_NoPermission() {
        Exception exception = assertThrows(PeerNotFoundException.class, () -> peerService.getOneByLoginAndId("test3", 1L));
        assertTrue(exception.getMessage().contains("no permission"));
    }

    @Test
    public void testGetOneByLoginAndId_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> peerService.getOneByLoginAndId(null, null));
    }

    @Test
    public void testCreate_Valid() throws EntityValidationFailedException, EntityNotFoundException, PeerConnectHandlerException, PeerAlreadyExistException {
        var confName = "newTest" + (new Random()).nextInt(1000);
        PeerEntity model = new PeerEntity(
                null,
                confName,
                null,
                null,
                null,
                true,
                UserEntity.builder().id(1L).login("test").build(),
                HostEntity.builder()
                        .id(3L)
                        .name("test3")

                        .build()
        );

        PeerFromRequest toCreate = new PeerFromRequest(confName, null, 1L, 3L);
        PeerEntity result = peerService.create(toCreate);

        //Compare
        assertEquals(model.getPeerConfName(), result.getPeerConfName());
        assertNotEquals(model.getPeerIp(), result.getPeerIp());
        assertNotEquals(model.getPeerPrivateKey(), result.getPeerPrivateKey());
        assertNotEquals(model.getPeerPublicKey(), result.getPeerPublicKey());
        assertEquals(model.getIsActivated(), result.getIsActivated());
        assertEquals(model.getUser().getId(), result.getUser().getId());
        assertEquals(model.getUser().getLogin(), result.getUser().getLogin());
        assertEquals(model.getHost().getId(), result.getHost().getId());
    }
}
