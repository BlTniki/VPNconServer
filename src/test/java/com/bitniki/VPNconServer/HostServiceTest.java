package com.bitniki.VPNconServer;

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

import java.util.List;
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
                new HostEntity(1L, "test", "127.0.0.1", 1, "127.0.0.0", "123456", "lolkek"),
                new HostEntity(2L, "test2", "127.0.0.1", 2, "127.0.0.0", "123456", "lolkek")
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
        HostEntity model = new HostEntity(1L, "test", "127.0.0.1", 1, "127.0.0.0", "123456", "lolkek");

        HostEntity result = hostService.getOneById(1L);

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
}
