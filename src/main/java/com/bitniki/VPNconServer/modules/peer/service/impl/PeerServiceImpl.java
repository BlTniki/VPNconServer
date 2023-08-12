package com.bitniki.VPNconServer.modules.peer.service.impl;

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
import com.bitniki.VPNconServer.modules.peer.service.PeerService;
import com.bitniki.VPNconServer.modules.peer.validator.PeerValidator;
import com.bitniki.VPNconServer.modules.subscription.exception.UserSubscriptionValidationFailedException;
import com.bitniki.VPNconServer.modules.subscription.service.UserSubscriptionService;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
public class PeerServiceImpl implements PeerService {
    private final PeerRepo peerRepo;
    private final UserService userService;
    private final HostService hostService;
    private final UserSubscriptionService userSubscriptionService;
    private final PeerConnectHandlerService peerConnectHandlerService;

    public Spliterator<PeerEntity> getAll() {
        return peerRepo.findAll().spliterator();
    }

    @Override
    public List<PeerEntity> getAllByUserId(@NotNull Long userId) {
        return peerRepo.findAllWithUserId(userId);
    }

    public Spliterator<PeerEntity> getAllByLogin(@NotNull String login) {
        return peerRepo.findAllWithUserLogin(login).spliterator();
    }

    public PeerEntity getOneById(@NotNull Long id) throws PeerNotFoundException {
        return peerRepo.findById(id)
                .orElseThrow(
                        () -> new PeerNotFoundException("Peer with id %d not found".formatted(id))
                );
    }

    public PeerEntity getOneByLoginAndPeerId(@NotNull String login, @NotNull Long id) throws EntityNotFoundException {
        return peerRepo.findByIdAndUserLogin(id, login)
                .orElseThrow(
                        () -> new PeerNotFoundException("Peer with id %d not exist or you have no permission for this peer".formatted(id))
                );
    }

    @Override
    public PeerEntity getOneByPeerIpAndHostId(@NotNull String peerIp, @NotNull Long hostId) throws EntityNotFoundException {
        return peerRepo.findByPeerIpAndHostId(peerIp, hostId)
                .orElseThrow(
                        () -> new PeerNotFoundException(
                                "Peer with ip %s and host id %d not exist or you have no permission for this peer"
                                        .formatted(peerIp, hostId)
                        )
                );
    }

    /**
     * Генерирует новый айпи пира на основе списка пиров с одинаковым хостом.
     * @param peerIps {@link List}<{@link String}> Список peerIp с одним хостом.
     * @param networkPrefix Префикс внутренней сети хоста в виде Х.Х.Х.0
     * @return Новый peerIp
     * @throws PeerValidationFailedException если хост полон и не получается сгенерировать новый peerIp
     */
    public String getFirstAvailableIpOnHost(List<String> peerIps, String networkPrefix) throws PeerValidationFailedException {
        //get last dot index in host network prefix
        int lastDotIndex = networkPrefix.lastIndexOf('.');

        // get last octets of existing peerIps on host
        List<Integer> existingOctets = peerIps.stream().map(
                peerIp -> Integer.valueOf( peerIp.substring(lastDotIndex + 1) )
        ).toList();

        // get first octet that missing in existingOctets or throw error
        // limit(253) -- last number will be 254 (it's just some joke that im didn't get it)
        Integer missingOctet = Stream.iterate(2, i -> i + 1).limit(253)
                .filter(i -> !existingOctets.contains(i))
                .findFirst()
                .orElseThrow(
                        () -> new PeerValidationFailedException("Host is full!")
                );

        //return ipaddress like <host network prefix>.<missing octet>
        return networkPrefix.substring(0, lastDotIndex + 1) + missingOctet;
    }

    private PeerEntity createPeer(UserEntity user, PeerFromRequest model)
            throws PeerAlreadyExistException, EntityValidationFailedException, HostNotFoundException, PeerConnectHandlerException {
        //validate user subscription
        int existingPeerNumber = peerRepo.findAllWithUserId(user.getId()).size();
        if(!userSubscriptionService.isUserCanCreatePeer(user.getId(), existingPeerNumber)) {
            throw new UserSubscriptionValidationFailedException(
                    "Subscription does not allow the creation of a new peer for user with id %d".formatted(user.getId())
            );
        }

        // validate peer
        PeerValidator peerValidator = PeerValidator.validateAllFields(model);
        if(peerValidator.hasFails()) {
            throw new PeerValidationFailedException(peerValidator.toString());
        }

        // load host
        HostEntity host = hostService.getOneById(model.getHostId());

        //check the uniqueness of the confName for a specific host and user
        if(peerRepo.findByPeerConfNameAndUserIdAndHostId(model.getPeerConfName(), user.getId(), host.getId()).isPresent()) {
            throw new PeerAlreadyExistException("Peer with conf name %s, user id %d and host id %d already exist".formatted(model.getPeerConfName(), user.getId(), host.getId()));
        }
        //if peerIp not null — check the uniqueness of the peerIp on this host
        if(model.getPeerIp() != null && peerRepo.findByPeerIpAndHostId(model.getPeerIp(), model.getHostId()).isPresent()) {
            throw new PeerAlreadyExistException(
                    "Peer ip %s on host with id %d already exist".formatted(model.getPeerIp(), model.getHostId())
            );
        }
        //if peerIp null — generate one
        if (model.getPeerIp() == null) {
            String newPeerIp = getFirstAvailableIpOnHost(
                    peerRepo.findAllByHostId(model.getHostId()).stream()
                            .map(PeerEntity::getPeerIp).toList(),
                    host.getHostInternalNetworkPrefix()
            );
            model.setPeerIp(newPeerIp);
        }

        // Create new peerEntity
        PeerEntity peer = PeerEntity.builder()
                .peerConfName(model.getPeerConfName())
                .peerIp(model.getPeerIp())
                .isActivated(true)
                .user(user)
                .host(host)
                .build();

        //create peer on host
        PeerFromHost answer = peerConnectHandlerService.createPeerOnHost(peer);

        //complete peer entity
        peer.setPeerPrivateKey( answer.getPeerPrivateKey() );
        peer.setPeerPublicKey( answer.getPeerPublicKey() );

        return peerRepo.save(peer);
    }

    public PeerEntity create(@NotNull PeerFromRequest model)
            throws EntityValidationFailedException, PeerAlreadyExistException, EntityNotFoundException, PeerConnectHandlerException {
        //Load user
        if (model.getUserId() == null) {
            throw new UserValidationFailedException("Wrong userId");
        }
        UserEntity user = userService.getOneById(model.getUserId());

        return createPeer(user, model);
    }

    public PeerEntity createByLogin(@NotNull String login, @NotNull PeerFromRequest model)
            throws EntityNotFoundException, PeerAlreadyExistException, EntityValidationFailedException, PeerConnectHandlerException {
        // load user
        UserEntity user = userService.getOneByLogin(login);
        //make sure that userId in model is null or equal to user id
        if ( model.getUserId() != null && !model.getUserId().equals(user.getId()) ) {
            throw new UserValidationFailedException("You have no permission to this user");
        }

        return createPeer(user, model);
    }

    private PeerEntity deletePeer(PeerEntity peer) throws PeerConnectHandlerException {
        //deleting peer on host
        peerConnectHandlerService.deletePeerOnHost(peer);

        //deleting in db
        peerRepo.delete(peer);
        return peer;
    }

    public PeerEntity delete(@NotNull Long id) throws PeerNotFoundException, PeerConnectHandlerException {
        // find peer
        PeerEntity peer = peerRepo.findById(id)
                .orElseThrow( () -> new PeerNotFoundException("Peer with id %d not found".formatted(id)) );

        return deletePeer(peer);
    }

    public PeerEntity deleteByLogin(@NotNull String login, @NotNull Long id) throws EntityNotFoundException, PeerConnectHandlerException {
        //find peer
        PeerEntity peer = peerRepo.findByIdAndUserLogin(id, login)
                .orElseThrow(
                        () -> new PeerNotFoundException(
                                "Peer with id %d does not exist or you have no permission for this peer".formatted(id)
                        )
                );

        return deletePeer(peer);
    }

    private String makeRequestToHostForDownloadToken(PeerEntity peer) throws PeerConnectHandlerException {
        //make request to host
        return peerConnectHandlerService.getDownloadConfToken(peer);
    }

    public String getDownloadTokenForPeerById(@NotNull Long id) throws PeerNotFoundException, PeerConnectHandlerException {
        PeerEntity peer = peerRepo.findById(id)
                .orElseThrow( () -> new PeerNotFoundException("Peer with id %d not found".formatted(id)) );

        return makeRequestToHostForDownloadToken(peer);
    }

    public String getDownloadTokenForPeerByLoginAndId(@NotNull String login, @NotNull Long id) throws EntityNotFoundException, PeerConnectHandlerException {
        //find peer
        PeerEntity peer = peerRepo.findByIdAndUserLogin(id, login)
                .orElseThrow(
                        () -> new PeerNotFoundException(
                                "Peer with id %d does not exist or you have no permission for this peer".formatted(id)
                        )
                );

        return makeRequestToHostForDownloadToken(peer);
    }

    public Boolean activatePeerOnHostById(@NotNull Long id) throws PeerNotFoundException, PeerConnectHandlerException {
        //load peer
        PeerEntity entity = peerRepo.findById(id)
                .orElseThrow(
                        () -> new PeerNotFoundException("Peer " + id + " not Found!")
                );
        //activate on host
        peerConnectHandlerService.activateOnHost(entity);

        //update entity
        entity.setIsActivated(true);
        peerRepo.save(entity);

        return true;
    }

    public Boolean deactivatePeerOnHostById(@NotNull Long id) throws PeerNotFoundException, PeerConnectHandlerException {
        //load peer
        PeerEntity entity = peerRepo.findById(id)
                .orElseThrow(
                        () -> new PeerNotFoundException("Peer " + id + " not Found!")
                );
        //activate on host
        peerConnectHandlerService.deactivateOnHost(entity);

        //update entity
        entity.setIsActivated(false);
        peerRepo.save(entity);

        return true;
    }

    public Map<String, String> getValidationRegex() {
        //Get regex from validator and cook answer
        Map<String, String> patterns = new HashMap<>();
        patterns.put("peerIp", PeerValidator.peerIpPattern.pattern());
        patterns.put("peerConfName", PeerValidator.peerConfNamePattern.pattern());

        return patterns;
    }
}
