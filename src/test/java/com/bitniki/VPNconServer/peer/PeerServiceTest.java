package com.bitniki.VPNconServer.peer;

import com.bitniki.VPNconServer.VpNconServerApplicationTests;
import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.EntityValidationFailedException;
import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.host.exception.HostNotFoundException;
import com.bitniki.VPNconServer.modules.host.service.HostService;
import com.bitniki.VPNconServer.modules.peer.connectHandler.PeerConnectHandlerService;
import com.bitniki.VPNconServer.modules.peer.connectHandler.exception.PeerConnectHandlerException;
import com.bitniki.VPNconServer.modules.peer.connectHandler.model.PeerFromHost;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.peer.exception.PeerAlreadyExistException;
import com.bitniki.VPNconServer.modules.peer.exception.PeerNotFoundException;
import com.bitniki.VPNconServer.modules.peer.exception.PeerValidationFailedException;
import com.bitniki.VPNconServer.modules.peer.model.PeerFromRequest;
import com.bitniki.VPNconServer.modules.peer.repository.PeerRepo;
import com.bitniki.VPNconServer.modules.peer.service.impl.PeerServiceImpl;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Transactional
@Rollback
public class PeerServiceTest extends VpNconServerApplicationTests {
    private final PeerConnectHandlerService peerConnectHandlerService;
    private final PeerServiceImpl peerService;

    public PeerServiceTest(@Autowired PeerRepo peerRepo,
                           @Autowired UserService userService,
                           @Autowired HostService hostService) {

        peerConnectHandlerService = mock(PeerConnectHandlerService.class);
        this.peerService = new PeerServiceImpl(peerRepo, userService, hostService, peerConnectHandlerService);
    }

    @Test
    public void testGetAll_Valid() {
        List<PeerEntity> modelList = List.of(
                new PeerEntity(
                        101L,
                        "test",
                        "10.8.0.10",
                        "private",
                        "public",
                        true,
                        UserEntity.builder().id(103L).build(),
                        HostEntity.builder().id(101L).build()
                ),
                new PeerEntity(
                        102L,
                        "test2",
                        "10.8.0.11",
                        "private",
                        "public",
                        true,
                        UserEntity.builder().id(103L).build(),
                        HostEntity.builder().id(101L).build()
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
                        101L,
                        "test",
                        "10.8.0.10",
                        "private",
                        "public",
                        true,
                        UserEntity.builder().id(103L).build(),
                        HostEntity.builder().id(101L).build()
                ),
                new PeerEntity(
                        102L,
                        "test2",
                        "10.8.0.11",
                        "private",
                        "public",
                        true,
                        UserEntity.builder().id(103L).build(),
                        HostEntity.builder().id(101L).build()
                )
        );

        List<PeerEntity> resultList = StreamSupport.stream(peerService.getAllByLogin("test"), false)
                .toList();

        //Compare
        assertEquals(4, resultList.size());
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
                101L,
                "test",
                "10.8.0.10",
                "private",
                "public",
                true,
                UserEntity.builder().id(103L).build(),
                HostEntity.builder().id(101L).build()
        );

        PeerEntity result = peerService.getOneById(101L);

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
                101L,
                "test",
                "10.8.0.10",
                "private",
                "public",
                true,
                UserEntity.builder().id(103L).login("test").build(),
                HostEntity.builder().id(101L).build()
        );

        PeerEntity result = peerService.getOneByLoginAndPeerId( "test", 101L);

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
        assertThrows(PeerNotFoundException.class, () -> peerService.getOneByLoginAndPeerId("test", -1L));
    }

    @Test
    public void testGetOneByLoginAndId_PeerNotFoundException_NoPermission() {
        Exception exception = assertThrows(PeerNotFoundException.class, () -> peerService.getOneByLoginAndPeerId("test3", 1L));
        assertTrue(exception.getMessage().contains("no permission"));
    }

    @Test
    public void testGetOneByLoginAndId_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> peerService.getOneByLoginAndPeerId(null, null));
    }

    @Test
    public void testGetFirstAvailableIpOnHost_Valid_ZeroList() throws PeerValidationFailedException {
        List<String> model = new ArrayList<>();
        String networkPrefix = "10.8.0.0";
        String result = peerService.getFirstAvailableIpOnHost(model, networkPrefix);

        assertEquals("10.8.0.2", result);
    }

    @Test
    public void testGetFirstAvailableIpOnHost_Valid_NonZeroList() throws PeerValidationFailedException {
        List<String> model = List.of("10.8.0.2", "10.8.0.3", "10.8.0.4", "10.8.0.6");
        String networkPrefix = "10.8.0.0";
        String result = peerService.getFirstAvailableIpOnHost(model, networkPrefix);

        assertEquals("10.8.0.5", result);
    }

    @Test
    public void testGetFirstAvailableIpOnHost_PeerValidationFailedException() {
        List<String> model = new ArrayList<>();
        String fOctets = "10.8.0.";
        for (int i = 2; i < 255; i++) {
            model.add(fOctets + i);
        }
        String networkPrefix = "10.8.0.0";
        assertThrows(PeerValidationFailedException.class, () -> peerService.getFirstAvailableIpOnHost(model, networkPrefix));
    }

    @Test
    public void testCreate_Valid() throws EntityValidationFailedException, EntityNotFoundException, PeerConnectHandlerException, PeerAlreadyExistException {
        PeerEntity model = new PeerEntity(
                null,
                "newTest",
                null,
                null,
                null,
                true,
                UserEntity.builder().id(103L).login("test").build(),
                HostEntity.builder().id(101L).build()
        );

        //Configure peerConnectHandlerService mock
        when(peerConnectHandlerService.createPeerOnHost(any())).thenReturn(new PeerFromHost(null, null, "prilol", "pubKek"));

        PeerFromRequest toCreate = new PeerFromRequest("newTest", null, 103L, 101L);
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

    @Test
    public void testCreate_UserValidationFailedException() {
        PeerFromRequest toCreate = new PeerFromRequest("newTest", null, null, 3L);
        Exception exception = assertThrows(UserValidationFailedException.class, () -> peerService.create(toCreate));
        assertTrue(exception.getMessage().contains("userId"));
    }

    @Test
    public void testCreate_EntityValidationFailedException() {
        PeerFromRequest toCreate = new PeerFromRequest("new_123_-eTest", "null", 103L, null);
        Exception exception = assertThrows(EntityValidationFailedException.class, () -> peerService.create(toCreate));
        assertTrue(
                exception.getMessage().contains("host id")
                && exception.getMessage().contains("peer conf name")
                && exception.getMessage().contains("peer ip")
        );
    }

    @Test
    public void testCreate_UserNotFoundException() {
        PeerFromRequest toCreate = new PeerFromRequest("newTest", "10.8.0.2", -1L, 1L);
        assertThrows(UserNotFoundException.class, () -> peerService.create(toCreate));
    }

    @Test
    public void testCreate_HostNotFoundException() {
        PeerFromRequest toCreate = new PeerFromRequest("newTest", "10.8.0.2", 103L, -1L);
        assertThrows(HostNotFoundException.class, () -> peerService.create(toCreate));
    }

    @Test
    public void testCreate_PeerAlreadyExistException_Name() {
        PeerFromRequest toCreate = new PeerFromRequest("test", "10.8.0.2", 103L, 101L);
        Exception exception = assertThrows(PeerAlreadyExistException.class, () -> peerService.create(toCreate));
        assertTrue(exception.getMessage().contains("conf name"));
    }

    @Test
    public void testCreate_PeerAlreadyExistException_peerIp() {
        PeerFromRequest toCreate = new PeerFromRequest("12test", "10.8.0.10", 103L, 101L);
        Exception exception = assertThrows(PeerAlreadyExistException.class, () -> peerService.create(toCreate));
        assertTrue(exception.getMessage().contains("Peer ip"));
    }

    @Test
    public void testCreateByLogin_Valid() throws EntityValidationFailedException, EntityNotFoundException, PeerConnectHandlerException, PeerAlreadyExistException {
        PeerEntity model = new PeerEntity(
                null,
                "newTest",
                null,
                null,
                null,
                true,
                UserEntity.builder().id(103L).login("test").build(),
                HostEntity.builder().id(103L).name("test3").build()
        );

        //Configure peerConnectHandlerService mock
        when(peerConnectHandlerService.createPeerOnHost(any())).thenReturn(new PeerFromHost(null, null, "prilol", "pubKek"));

        PeerFromRequest toCreate = new PeerFromRequest("newTest", null, 103L, 103L);
        PeerEntity result = peerService.createByLogin("test", toCreate);

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

    @Test
    public void testCreateByLogin_UserValidationFailedException() {
        PeerFromRequest toCreate = new PeerFromRequest("newTest", null, 2L, 3L);
        Exception exception = assertThrows(UserValidationFailedException.class, () -> peerService.createByLogin("test", toCreate));
        assertTrue(exception.getMessage().contains("You have no permission"));
    }

    @Test
    public void testDelete_Valid() throws PeerNotFoundException, PeerConnectHandlerException {
        PeerEntity model = new PeerEntity(
                101L,
                "test",
                "10.8.0.10",
                "private",
                "public",
                true,
                UserEntity.builder().id(103L).login("test").build(),
                HostEntity.builder().id(101L).build()
        );

        PeerEntity result = peerService.delete(101L);

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
    public void testDelete_PeerNotFoundException() {
        assertThrows(PeerNotFoundException.class, () -> peerService.delete(-1L));
    }

    @Test
    public void testDeleteByLogin_Valid() throws EntityNotFoundException, PeerConnectHandlerException {
        PeerEntity model = new PeerEntity(
                101L,
                "test",
                "10.8.0.10",
                "private",
                "public",
                true,
                UserEntity.builder().id(103L).login("test").build(),
                HostEntity.builder().id(101L).build()
        );

        PeerEntity result = peerService.deleteByLogin("test", 101L);

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
    public void testDeleteByLogin_PeerNotFoundException_Name() {
        assertThrows(PeerNotFoundException.class, () -> peerService.deleteByLogin("testdwwadwa", 1L));
    }

    @Test
    public void testDeleteByLogin_PeerNotFoundException_Id() {
        assertThrows(PeerNotFoundException.class, () -> peerService.deleteByLogin("test", -1L));
    }

    @Test
    public void testGetDownloadTokenForPeerById_Valid() throws PeerNotFoundException, PeerConnectHandlerException {
        String model = "lolKek";

        //Configure peerConnectHandlerService mock
        when(peerConnectHandlerService.getDownloadConfToken(any())).thenReturn(model);

        String result = peerService.getDownloadTokenForPeerById(101L);

        //Compare
        assertEquals(model, result);
    }

    @Test
    public void testGetDownloadTokenForPeerById_PeerNotFoundException() {
        assertThrows(PeerNotFoundException.class, () -> peerService.getDownloadTokenForPeerById(-1L));
    }

    @Test
    public void testGetDownloadTokenForPeerByLoginAndId_Valid() throws EntityNotFoundException, PeerConnectHandlerException {
        String model = "lolKek";

        //Configure peerConnectHandlerService mock
        when(peerConnectHandlerService.getDownloadConfToken(any())).thenReturn(model);

        String result = peerService.getDownloadTokenForPeerByLoginAndId("test", 101L);

        //Compare
        assertEquals(model, result);
    }

    @Test
    public void testGetDownloadTokenForPeerByLoginAndId_PeerNotFoundException_Name() {
        assertThrows(PeerNotFoundException.class, () -> peerService.getDownloadTokenForPeerByLoginAndId("test123fg", 1L));
    }

    @Test
    public void testGetDownloadTokenForPeerByLoginAndId_PeerNotFoundException_Id() {
        assertThrows(PeerNotFoundException.class, () -> peerService.getDownloadTokenForPeerByLoginAndId("test", -1L));
    }

    @Test
    public void testActivatePeerOnHost_Valid() throws PeerNotFoundException, PeerConnectHandlerException {
        PeerEntity model = new PeerEntity(
                104L,
                "test",
                "10.8.0.10",
                "private",
                "public",
                true,
                UserEntity.builder().id(103L).login("test").build(),
                HostEntity.builder().id(102L).build()
        );
        peerService.activatePeerOnHostById(104L);
        PeerEntity result = peerService.getOneById(104L);

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
    public void testActivatePeerOnHost_PeerNotFoundException() {
        assertThrows(PeerNotFoundException.class, () -> peerService.activatePeerOnHostById(-4L));
    }

    @Test
    public void testDeactivatePeerOnHost_Valid() throws PeerNotFoundException, PeerConnectHandlerException {
        PeerEntity model = new PeerEntity(
                105L,
                "test123",
                "10.8.0.14",
                "private",
                "public",
                false,
                UserEntity.builder().id(103L).login("test").build(),
                HostEntity.builder().id(102L).build()
        );
        peerService.deactivatePeerOnHostById(105L);
        PeerEntity result = peerService.getOneById(105L);

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
    public void testDeactivatePeerOnHost_PeerNotFoundException() {
        assertThrows(PeerNotFoundException.class, () -> peerService.deactivatePeerOnHostById(-4L));
    }

    @Test
    public void testGetValidationRegex() {
        Map<String, String> model = new HashMap<>();
        model.put("peerIp", "^10\\.8\\.0\\.(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");
        model.put("peerConfName", "^[A-Za-z0-9]+$");

        assertEquals(model, peerService.getValidationRegex());
    }
}
