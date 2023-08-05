package com.bitniki.VPNconServer.modules.peer.controller;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.EntityValidationFailedException;
import com.bitniki.VPNconServer.modules.peer.connectHandler.exception.PeerConnectHandlerException;
import com.bitniki.VPNconServer.modules.peer.exception.PeerAlreadyExistException;
import com.bitniki.VPNconServer.modules.peer.exception.PeerNotFoundException;
import com.bitniki.VPNconServer.modules.peer.model.Peer;
import com.bitniki.VPNconServer.modules.peer.model.PeerFromRequest;
import com.bitniki.VPNconServer.modules.peer.service.PeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/peers")
public class PeerController {
    @Autowired
    private PeerService peerService;

    /**
     * Получение списка всех пиров. Для использования требуется авторизация с ролью "user:read" и "any".
     * @return ResponseEntity со списком пиров и статусом ответа
     */
    @GetMapping
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<List<Peer>> getAllPeers() {
        return ResponseEntity.ok(
                StreamSupport.stream(peerService.getAll(), false)
                        .map(Peer::toModel)
                        .toList()
        );
    }

    /**
     * Получение списка всех пиров по Id юзера. Для использования требуется авторизация с ролью "user:read" и "any".
     * @param userId Id юзера.
     * @return ResponseEntity со списком пиров и статусом ответа.
     */
    @GetMapping("/byUser/{userId}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<List<Peer>> getAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                peerService.getAllByUserId(userId).stream()
                        .map(Peer::toModel)
                        .toList()
        );
    }

    /**
     * Получение списка всех пиров текущего пользователя. Для использования требуется авторизация с ролью "user:read" и "personal".
     * @param principal Принципал текущего пользователя.
     * @return ResponseEntity со списком пиров и статусом ответа.
     */
    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<List<Peer>> getAllMinePeers(Principal principal) {
        return ResponseEntity.ok(
                StreamSupport.stream(peerService.getAllByLogin(principal.getName()), false)
                        .map(Peer::toModel)
                        .toList()
        );
    }

    /**
     * Получение пира по его ID. Для использования требуется авторизация с ролью "peer:read" и "any".
     * @param id ID пира
     * @return ResponseEntity с найденным пиром и статусом ответа
     * @throws PeerNotFoundException если пир не найден в базе данных
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<Peer> getOnePeer(@PathVariable Long id)
            throws PeerNotFoundException {
        return ResponseEntity.ok(
                Peer.toModel(peerService.getOneById(id))
        );
    }

    /**
     * Получение пира текущего пользователя по его ID. Для использования требуется авторизация с ролью "peer:read" и "personal".
     * @param id ID пира.
     * @param principal Принципал текущего пользователя.
     * @return ResponseEntity с найденным пиром и статусом ответа.
     * @throws PeerNotFoundException Если пир среди пиров текущего пользователя не найден.
     */
    @GetMapping("/mine/{id}")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<Peer> getOneMinePeer(@PathVariable Long id, Principal principal)
            throws EntityNotFoundException {
        return ResponseEntity.ok(
                Peer.toModel(peerService.getOneByLoginAndPeerId(principal.getName(), id))
        );
    }

    /**
     * Создание нового пира.
     * В {@link PeerFromRequest} должны быть указанны все поля. Ограничения на поля можно найти в самом {@link PeerFromRequest}.
     * @param model объект {@link PeerFromRequest}.
     * @return ResponseEntity с созданным пиром и статусом ответа.
     * @throws EntityNotFoundException Если данные userId и/или hostId не найдены.
     * @throws PeerAlreadyExistException Если пир с комбинацией peerConfName + userId + hostId или peerIp + hostId уже существует.
     * @throws EntityValidationFailedException При провале проверки полей в PeerFromRequest.
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Peer> createPeer(@RequestBody PeerFromRequest model)
            throws EntityNotFoundException, PeerAlreadyExistException, EntityValidationFailedException, PeerConnectHandlerException {
        return ResponseEntity.ok(
                Peer.toModel(peerService.create(model))
        );
    }

    /**
     * Создание нового пира текущим пользователем.
     * В {@link PeerFromRequest} должны быть указанны все поля. Ограничения на поля можно найти в самом {@link PeerFromRequest}.
     * Юзер текущего пользователя должен совпадать с юзером, указанным в {@link PeerFromRequest}.
     * @param principal Принципал текущего пользователя.
     * @param model объект {@link PeerFromRequest}.
     * @return ResponseEntity с созданным пиром и статусом ответа.
     * @throws EntityNotFoundException Если данные userId и/или hostId не найдены.
     * @throws PeerAlreadyExistException Если пир с комбинацией peerConfName + userId + hostId или peerIp + hostId уже существует.
     * @throws EntityValidationFailedException При провале проверки полей в {@link PeerFromRequest} или переданный логин не совпадает с логином юзера в {@link PeerFromRequest}.
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    @PostMapping("/mine")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Peer> createMinePeer(Principal principal, @RequestBody PeerFromRequest model)
            throws EntityNotFoundException, PeerAlreadyExistException, EntityValidationFailedException, PeerConnectHandlerException {
        return ResponseEntity.ok(
                Peer.toModel(peerService.createByLogin(principal.getName(), model))
        );
    }

    /**
     * Удаление пира по id.
     * @param id Id пира.
     * @return ResponseEntity с удалённым пиром и статусом ответа.
     * @throws PeerNotFoundException Если пир не найден.
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Peer> deletePeer(@PathVariable Long id) throws PeerNotFoundException, PeerConnectHandlerException {
        return ResponseEntity.ok(
                Peer.toModel(peerService.delete(id))
        );
    }

    /**
     * Удаление пира текущего пользователя по id пира.
     * @param principal Принципал текущего пользователя.
     * @param id Id пира.
     * @return ResponseEntity с удалённым пиром и статусом ответа.
     * @throws PeerNotFoundException Если пир среди пиров текущего пользователя не найден.
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    @DeleteMapping("/mine/{id}")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Peer> deleteMinePeer(Principal principal, @PathVariable Long id)
            throws EntityNotFoundException, PeerConnectHandlerException {
        return ResponseEntity.ok(
                Peer.toModel(peerService.deleteByLogin(principal.getName(), id))
        );
    }

    /**
     * Получение токена для скачивания конфига пира с хоста по id пира.
     * @param id Id пира.
     * @return ResponseEntity с токеном и статусом ответа.
     * @throws PeerNotFoundException Если пир не найден.
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    @GetMapping("/conf/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<String> getDownloadToken(@PathVariable Long id) throws PeerNotFoundException, PeerConnectHandlerException {
        return ResponseEntity.ok(peerService.getDownloadTokenForPeerById(id));
    }

    /**
     * Получение токена для скачивания конфига пира с хоста текущего пользователя по id пира.
     * @param id Id пира.
     * @return ResponseEntity с токеном и статусом ответа.
     * @throws PeerNotFoundException Если пир среди пиров текущего пользователя не найден.
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    @GetMapping("/conf/mine/{id}")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<String> getMineDownloadToken(Principal principal,@PathVariable Long id) throws EntityNotFoundException, PeerConnectHandlerException {
        return ResponseEntity.ok(peerService.getDownloadTokenForPeerByLoginAndId(principal.getName(), id));
    }

    /**
     * Активация пира по его id.
     * @param id Id пира.
     * @return ResponseEntity с истиной и статусом ответа.
     * @throws PeerNotFoundException Если пир не найден.
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    @PostMapping("/activate/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Boolean> activatePeer(@PathVariable Long id)
            throws PeerNotFoundException, PeerConnectHandlerException {
        return ResponseEntity.ok(peerService.activatePeerOnHostById(id));
    }

    /**
     * Деактивация пира по его id.
     * @param id Id пира.
     * @return ResponseEntity с истиной и статусом ответа.
     * @throws PeerNotFoundException Если пир не найден.
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    @PostMapping("/deactivate/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Boolean> deactivatePeer(@PathVariable Long id)
            throws PeerNotFoundException, PeerConnectHandlerException {
        return ResponseEntity.ok(peerService.deactivatePeerOnHostById(id));
    }

    /**
     * Метод для получения паттернов валидации полей {@link String} peerIp и {@link String} peerConfName.
     * Используется клиентами для получения актуальных правил валидации, чтобы валидировать ввод на месте, не отправляя на сервер.
     * @return Карту в виде {"ipaddress": pattern, "networkPrefix": pattern}.
     */
    @GetMapping("/validator")
    @PreAuthorize("hasAuthority('any')")
    public ResponseEntity<Map<String, String>> getValidatorPatterns() {
        return ResponseEntity.ok(
                peerService.getValidationRegex()
        );
    }
}
