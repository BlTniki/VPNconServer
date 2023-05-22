package com.bitniki.VPNconServer.hosts;

import com.bitniki.VPNconServer.VpNconServerApplicationTests;
import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.host.exception.HostAlreadyExistException;
import com.bitniki.VPNconServer.modules.host.exception.HostNotFoundException;
import com.bitniki.VPNconServer.modules.host.exception.HostValidationFailedException;
import com.bitniki.VPNconServer.modules.host.model.HostFromRequest;
import com.bitniki.VPNconServer.modules.host.service.HostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Rollback
public class HostServiceTest extends VpNconServerApplicationTests {

    @Autowired
    private HostService hostService;

    @Test
    public void testGetAll_Valid() {
        List<HostEntity> modelList = List.of(
                new HostEntity(101L, "test", "127.0.0.1", 1, "127.0.0.0", "123456", "lolkek"),
                new HostEntity(102L, "test2", "127.0.0.1", 2, "127.0.0.0", "123456", "lolkek")
        );

        List<HostEntity> resultList = StreamSupport.stream(hostService.getAll(), false)
                .toList();

        //Compare
        for (int i = 0; i < modelList.size(); i++) {
            var model = modelList.get(i);
            var result = resultList.get(i);

            assertEquals(model.getId(), result.getId());
            assertEquals(model.getName(), result.getName());
            assertEquals(model.getIpaddress(), result.getIpaddress());
            assertEquals(model.getPort(), result.getPort());
            assertEquals(model.getHostInternalNetworkPrefix(), result.getHostInternalNetworkPrefix());
            assertEquals(model.getHostPassword(), result.getHostPassword());
            assertEquals(model.getHostPublicKey(), result.getHostPublicKey());
        }
    }

    @Test
    public void testGetOne_Valid() throws HostNotFoundException {
        HostEntity model = new HostEntity(101L, "test", "127.0.0.1", 1, "127.0.0.0", "123456", "lolkek");

        HostEntity result = hostService.getOneById(101L);

        //Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getName(), result.getName());
        assertEquals(model.getIpaddress(), result.getIpaddress());
        assertEquals(model.getPort(), result.getPort());
        assertEquals(model.getHostInternalNetworkPrefix(), result.getHostInternalNetworkPrefix());
        assertEquals(model.getHostPassword(), result.getHostPassword());
        assertEquals(model.getHostPublicKey(), result.getHostPublicKey());
    }

    @Test
    public void testGetOne_HostNotFoundException() {
        assertThrows(HostNotFoundException.class, () -> hostService.getOneById(-1L));
    }

    @Test
    public void testGetOne_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> hostService.getOneById(null));
    }

    @Test
    public void testCreate_Valid() throws HostAlreadyExistException, HostValidationFailedException {
        HostEntity model = new HostEntity(null, "newTest", "127.0.0.1", 3, "127.0.1.0", "654321", "keklol");

        HostFromRequest toCreate = new HostFromRequest(
                "newTest",
                "127.0.0.1",
                3,
                "127.0.1.0",
                "654321",
                "keklol"
        );
        HostEntity result = hostService.create(toCreate);

        //Compare
        assertEquals(model.getName(), result.getName());
        assertEquals(model.getIpaddress(), result.getIpaddress());
        assertEquals(model.getPort(), result.getPort());
        assertEquals(model.getHostInternalNetworkPrefix(), result.getHostInternalNetworkPrefix());
        assertEquals(model.getHostPassword(), result.getHostPassword());
        assertEquals(model.getHostPublicKey(), result.getHostPublicKey());
    }

    @Test
    public void testCreate_HostAlreadyExistException() {
        // Name
        Exception exception = assertThrows(HostAlreadyExistException.class, () -> hostService.create(
                new HostFromRequest(
                        "test",
                        "127.0.0.1",
                        3,
                        "127.0.1.0",
                        "654321",
                        "keklol"
                )
        ));
        assertTrue( exception.getMessage().contains("Host with name") );

        // ip and port pair
        Exception exception2 = assertThrows(HostAlreadyExistException.class, () -> hostService.create(
                new HostFromRequest(
                        "newTest",
                        "127.0.0.1",
                        1,
                        "127.0.1.0",
                        "654321",
                        "keklol"
                )
        ));
        assertTrue( exception2.getMessage().contains("Host with ip") );
    }

    @Test
    public void testCreate_HostValidationFailedException() {
        Exception exception = assertThrows(HostValidationFailedException.class, () -> hostService.create(
                new HostFromRequest(
                        "newTest",
                        "127 0.0.1",
                        -1,
                        "127.0.1.1",
                        null,
                        null
                )
        ));
        assertTrue( exception.getMessage().contains("ipaddress") );
        assertTrue( exception.getMessage().contains("port") );
        assertTrue( exception.getMessage().contains("host internal network prefix") );
        assertTrue( exception.getMessage().contains("host password") );
        assertTrue( exception.getMessage().contains("host public key") );
    }

    @Test
    public void testCreate_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> hostService.create(null));

        assertThrows(HostValidationFailedException.class, () -> hostService.create(
                new HostFromRequest(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        ));
    }

    @Test
    public void testUpdateById_Valid() throws HostNotFoundException, HostAlreadyExistException, HostValidationFailedException {
        HostEntity model = new HostEntity(101L, "newTest", "127.0.0.1", 3, "127.0.1.0", "654321", "keklol");

        HostFromRequest toUpdate = new HostFromRequest(
                "newTest",
                "127.0.0.1",
                3,
                "127.0.1.0",
                "654321",
                "keklol"
        );
        HostEntity result = hostService.updateById(101L, toUpdate);

        //Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getName(), result.getName());
        assertEquals(model.getIpaddress(), result.getIpaddress());
        assertEquals(model.getPort(), result.getPort());
        assertEquals(model.getHostInternalNetworkPrefix(), result.getHostInternalNetworkPrefix());
        assertEquals(model.getHostPassword(), result.getHostPassword());
        assertEquals(model.getHostPublicKey(), result.getHostPublicKey());
    }

    @Test
    public void testUpdateById_NullField() throws HostNotFoundException, HostAlreadyExistException, HostValidationFailedException {
        HostEntity model = new HostEntity(101L, "test", "127.0.0.1", 1, "127.0.0.0", "123456", "lolkek");

        HostFromRequest toUpdate = new HostFromRequest(
                null,
                null,
                null,
                null,
                null,
                null
        );
        HostEntity result = hostService.updateById(101L, toUpdate);

        //Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getName(), result.getName());
        assertEquals(model.getIpaddress(), result.getIpaddress());
        assertEquals(model.getPort(), result.getPort());
        assertEquals(model.getHostInternalNetworkPrefix(), result.getHostInternalNetworkPrefix());
        assertEquals(model.getHostPassword(), result.getHostPassword());
        assertEquals(model.getHostPublicKey(), result.getHostPublicKey());
    }

    @Test
    public void testUpdateById_HostNotFoundException() {
        HostFromRequest toUpdate = new HostFromRequest(
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertThrows(HostNotFoundException.class, () -> hostService.updateById(-1L, toUpdate) );
    }

    @Test
    public void testUpdateById_HostAlreadyExistException() {
        // Name
        Exception exception = assertThrows( HostAlreadyExistException.class, () -> hostService.updateById(
                101L,
                new HostFromRequest(
                        "test2",
                        null,
                        null,
                        null,
                        null,
                        null
                )
        ) );
        assertTrue( exception.getMessage().contains("Host with name") );

        // ip and port pair
        Exception exception2 = assertThrows( HostAlreadyExistException.class, () -> hostService.updateById(
                101L,
                new HostFromRequest(
                        null,
                        "127.0.0.1",
                        2,
                        null,
                        null,
                        null
                )
        ) );
        assertTrue( exception2.getMessage().contains("Host with ip") );
    }

    @Test
    public void testUpdateById_HostValidationFailedException() {
        Exception exception = assertThrows(HostValidationFailedException.class, () -> hostService.updateById(
                101L,
                new HostFromRequest(
                        "newTest",
                        "127 0.0.1",
                        -1,
                        "127.0.1.1",
                        null,
                        null
                )
        ));
        assertTrue( exception.getMessage().contains("ipaddress") );
        assertTrue( exception.getMessage().contains("port") );
        assertTrue( exception.getMessage().contains("host internal network prefix") );
    }

    @Test
    public void testUpdateById_Null() {
        //noinspection DataFlowIssue
        assertThrows(IllegalArgumentException.class, () -> hostService.updateById(null, null));
    }

    @Test
    public void testDeleteById_Valid() throws HostNotFoundException {
        HostEntity model = new HostEntity(101L, "test", "127.0.0.1", 1, "127.0.0.0", "123456", "lolkek");

        HostEntity result = hostService.deleteById(101L);

        //Compare
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getName(), result.getName());
        assertEquals(model.getIpaddress(), result.getIpaddress());
        assertEquals(model.getPort(), result.getPort());
        assertEquals(model.getHostInternalNetworkPrefix(), result.getHostInternalNetworkPrefix());
        assertEquals(model.getHostPassword(), result.getHostPassword());
        assertEquals(model.getHostPublicKey(), result.getHostPublicKey());
    }

    @Test
    public void testDeleteById_HostNotFoundException() {
        assertThrows( HostNotFoundException.class, () -> hostService.deleteById(-1L) );
    }

    @Test
    public void testDeleteById_Null() {
        //noinspection DataFlowIssue
        assertThrows( IllegalArgumentException.class, () -> hostService.deleteById(null) );
    }

    @Test
    public void testGetValidationRegex() {
        Map<String, String> model = new HashMap<>();
        model.put("ipaddress", "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        model.put("networkPrefix", "^(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.0$");

        assertEquals(model, hostService.getValidationRegex());
    }
}
