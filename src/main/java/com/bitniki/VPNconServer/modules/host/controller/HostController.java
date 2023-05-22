package com.bitniki.VPNconServer.modules.host.controller;

import com.bitniki.VPNconServer.modules.host.exception.HostAlreadyExistException;
import com.bitniki.VPNconServer.modules.host.exception.HostNotFoundException;
import com.bitniki.VPNconServer.modules.host.exception.HostValidationFailedException;
import com.bitniki.VPNconServer.modules.host.model.Host;
import com.bitniki.VPNconServer.modules.host.model.HostFromRequest;
import com.bitniki.VPNconServer.modules.host.service.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/hosts")
public class HostController {
    @Autowired
    private HostService hostService;

    /**
     * Получает список всех хостов. Для использования требуется авторизация с ролью "host:read"
     * @return ResponseEntity с объектом типа List и статусом ответа.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('host:read')")
    public ResponseEntity<List<Host>> getAllHosts() {
        return ResponseEntity.ok(
                StreamSupport.stream(hostService.getAll(), false)
                        .map(Host::toModel)
                        .toList()
        );
    }

    /**
     * Получает хост по его идентификатору. Для использования требуется авторизация с ролью "host:read"
     * @param id Идентификатор хоста.
     * @return ResponseEntity с объектом типа Host и статусом ответа.
     * @throws HostNotFoundException Если хост не найден.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('host:read')")
    public ResponseEntity<Host> getHost (@PathVariable Long id)
            throws HostNotFoundException {
        return ResponseEntity.ok(
                Host.toModel(hostService.getOneById(id))
        );
    }

    /**
     * Создает новый хост на основе переданных данных. Для использования требуется авторизация с ролью "host:write"
     * @param host Данные нового хоста. Имя и комбинация айпи и порта должна быть уникальна.
     * @return ResponseEntity с объектом типа Host и статусом ответа.
     * @throws HostAlreadyExistException Если хост с данными именем или ip уже существует.
     * @throws HostValidationFailedException Если данные хоста не прошли валидацию.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('host:write')")
    public ResponseEntity<Host> createHost(@RequestBody HostFromRequest host)
            throws HostAlreadyExistException, HostValidationFailedException {
        return ResponseEntity.ok(
                Host.toModel(hostService.create(host))
        );
    }

    /**
     * Обновляет данные существующего хоста. Для использования требуется авторизация с ролью "host:write"
     * @param id Идентификатор хоста.
     * @param host Новые данные хоста. Имя и комбинация айпи и порта должна быть уникальна.
     * @return ResponseEntity с объектом типа Host и статусом ответа.
     * @throws HostNotFoundException Если хост не найден.
     * @throws HostAlreadyExistException Если хост с данными именем или ip уже существует.
     * @throws HostValidationFailedException Если данные хоста не прошли валидацию.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('host:write')")
    public ResponseEntity<Host> updateHost (@PathVariable Long id, @RequestBody HostFromRequest host)
            throws HostNotFoundException, HostAlreadyExistException, HostValidationFailedException {
        return ResponseEntity.ok(
                Host.toModel(hostService.updateById(id, host))
        );
    }

    /**
     * Удаляет хост по его идентификатору.
     * @param id Идентификатор хоста.
     * @return ResponseEntity с объектом типа Host, который был удален, и статусом ответа.
     * @throws HostNotFoundException Если хост не найден.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('host:write')")
    public ResponseEntity<Host> deleteHost (@PathVariable Long id) throws HostNotFoundException {
        return ResponseEntity.ok(
                Host.toModel(hostService.deleteById(id))
        );
    }

    /**
     * Метод для получения паттернов валидации полей {@link String} login и {@link String} password.
     * Используется клиентами для получения актуальных правил валидации, чтобы валидировать ввод на месте, не отправляя на сервер.
     * @return Карту в виде {"ipaddress": pattern, "networkPrefix": pattern}.
     */
    @GetMapping("/validator")
    @PreAuthorize("hasAuthority('any')")
    public ResponseEntity<Map<String, String>> getValidatorPatterns() {
        return ResponseEntity.ok(
                hostService.getValidationRegex()
        );
    }
}
