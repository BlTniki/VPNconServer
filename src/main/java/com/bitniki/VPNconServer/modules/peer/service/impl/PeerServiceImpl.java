package com.bitniki.VPNconServer.modules.peer.service.impl;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.EntityValidationFailedException;
import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.host.exception.HostNotFoundException;
import com.bitniki.VPNconServer.modules.host.service.HostService;
import com.bitniki.VPNconServer.modules.peer.connectHandler.PeerConnectHandler;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.peer.exception.PeerAlreadyExistException;
import com.bitniki.VPNconServer.modules.peer.exception.PeerNotFoundException;
import com.bitniki.VPNconServer.modules.peer.exception.PeerValidationFailedException;
import com.bitniki.VPNconServer.modules.peer.model.PeerFromRequest;
import com.bitniki.VPNconServer.modules.peer.repository.PeerRepo;
import com.bitniki.VPNconServer.modules.peer.service.PeerService;
import com.bitniki.VPNconServer.modules.peer.validator.PeerValidator;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PeerServiceImpl implements PeerService {
    private final PeerRepo peerRepo;
    private final UserService userService;
    private final HostService hostService;

    public Spliterator<PeerEntity> getAll() {
        return peerRepo.findAll().spliterator();
    }

    public Spliterator<PeerEntity> getAllByLogin(String login) {
        return peerRepo.findAllWithUserLogin(login).spliterator();
    }

    public PeerEntity getOne(Long id) throws PeerNotFoundException {
        return peerRepo.findById(id)
                .orElseThrow(
                        () -> new PeerNotFoundException("Peer with id %d not found".formatted(id))
                );
    }

    public PeerEntity getOne(String login, Long id) throws EntityNotFoundException {
        return peerRepo.findByIdAndUserLogin(id, login)
                .orElseThrow(
                        () -> new PeerNotFoundException("Peer with id %d not exist or you have no permission for this peer".formatted(id))
                );
    }

    /**
     * Генерирует новый айпи пира на основе списка пиров с одинаковым хостом.
     * @param peers {@link List}<{@link PeerEntity}> у которых одинаковый хост
     * @return Новый peerIp
     * @throws PeerValidationFailedException если хост полон и не получается сгенерировать новый peerIp
     */
    private String getFirstAvailableIpOnHost(List<PeerEntity> peers) throws PeerValidationFailedException {
        //get last dot index in host network prefix
        int lastDotIndex = peers.get(0).getHost().getHostInternalNetworkPrefix().lastIndexOf('.');

        // get last octets of existing peerIps on host
        List<Integer> existingOctets = peers.stream().map(
                peer -> Integer.valueOf( peer.getPeerIp().substring(lastDotIndex + 1) )
        ).toList();

        // get first octet that missing in existingOctets or throw error
        Integer missingOctet = Stream.iterate(2, i -> i + 1).limit(255)
                .filter(i -> !existingOctets.contains(i))
                .findFirst()
                .orElseThrow(
                        () -> new PeerValidationFailedException("Host named %s is full!".formatted(peers.get(0).getHost().getName()))
                );

        //return ipaddress like <host network prefix>.<missing octet>
        return peers.get(0).getHost().getHostInternalNetworkPrefix().substring(0, lastDotIndex + 1) + missingOctet;
    }

    private PeerEntity createPeer(UserEntity user, PeerFromRequest model)
            throws PeerAlreadyExistException, EntityValidationFailedException, HostNotFoundException {
//        //validate user subscription
//        if(user.getSubscription() == null
//                || user.getPeers().size() >= user.getSubscription().getPeersAvailable()) {
//            throw new SubscriptionValidationFailedException(
//                    "Your subscription does not allow the creation of a new peer"
//            );
//        }

        // validate peer
        PeerValidator peerValidator = PeerValidator.validateAllFields(model);
        if(peerValidator.hasFails()) {
            throw new PeerValidationFailedException(peerValidator.toString());
        }

        // load host
        HostEntity host = hostService.getOneById(model.getHostId());

        //check the uniqueness of the confName for a specific host and user
        if(peerRepo.findByPeerConfNameAndUserIdAndHostId(model.getPeerConfName(), user.getId(), host.getId()).isPresent()) {
            throw new PeerAlreadyExistException("Peer already exist");
        }
        //if peerIp not null — check the uniqueness of the peerIp on this host
        if(model.getPeerIp() != null && peerRepo.findByPeerIpAndHostId(model.getPeerIp(), model.getHostId()).isPresent()) {
            throw new PeerAlreadyExistException(
                    "Peer ip %s on host with id %d already exist".formatted(model.getPeerIp(), model.getHostId())
            );
        }
        //if peerIp null — generate one
        if (model.getPeerIp() == null) {
            String newPeerIp = getFirstAvailableIpOnHost(peerRepo.findAllByHostId(model.getHostId()));
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
//        //create peer on host and complete peer entity
//        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
//        peerConnectHandler.createPeerOnHostAndFillEntity();

        return peerRepo.save(peer);
    }

    public PeerEntity create(PeerFromRequest model)
            throws EntityValidationFailedException, PeerAlreadyExistException, EntityNotFoundException {
        //Load user
        if (model.getUserId() == null) {
            throw new UserValidationFailedException("Wrong userId");
        }
        UserEntity user = userService.getOneById(model.getUserId());

        return createPeer(user, model);
    }

    public PeerEntity create(String login, PeerFromRequest model)
            throws EntityNotFoundException, PeerAlreadyExistException, EntityValidationFailedException {
        // load user
        UserEntity user = userService.getOneByLogin(login);
        //make sure that userId in model is null or equal to user id
        if ( model.getUserId() != null && !model.getUserId().equals(user.getId()) ) {
            throw new UserValidationFailedException("You have no permission to this user");
        }

        return createPeer(user, model);
    }

    private PeerEntity updatePeer(PeerEntity oldPeer, PeerFromRequest newPeerModel)
            throws PeerAlreadyExistException, PeerValidationFailedException {
        // validate new peer
        PeerValidator peerValidator = PeerValidator.validateNonNullFields(newPeerModel);
        if(peerValidator.hasFails()) {
            throw new PeerValidationFailedException(peerValidator.toString());
        }



        //check the uniqueness of the confName for a specific host and user
        //this is placeholder because we cant properly update confName on host
        if ( newPeerModel.getPeerConfName() != null
                && peerRepo.findByPeerConfNameAndUserIdAndHostId(
                        newPeerModel.getPeerConfName(), newPeerModel.getUserId(), newPeerModel.getHostId()
                    ).isPresent() ) {
            throw new PeerAlreadyExistException("Peer already exist");
        }

        //if we have new peer ip – check unique
        if ( newPeerModel.getPeerIp() != null
                && peerRepo.findByPeerIpAndHostId(
                        newPeerModel.getPeerIp(), newPeerModel.getHostId()
                    ).isPresent() ) {
            throw new PeerAlreadyExistException(
                    "Peer ip %s on host with id %d already exist".formatted(newPeerModel.getPeerIp(), oldPeer.getHost().getId())
            );
        }
        // set new peerIp if present
        if (newPeerModel.getPeerIp() != null) {
            oldPeer.setPeerIp(newPeerModel.getPeerIp());
        }

//        //update peer on host
//        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(oldPeer);
//        peerConnectHandler.updatePeerOnHost();

        return peerRepo.save(oldPeer);
    }


    public PeerEntity update(Long id, PeerFromRequest newPeerModel)
            throws PeerNotFoundException, PeerAlreadyExistException, PeerValidationFailedException {
        // load old peer
        PeerEntity oldPeer = peerRepo.findById(id)
                .orElseThrow( () -> new PeerNotFoundException("Peer with id %d not found".formatted(id)) );

        return updatePeer(oldPeer, newPeerModel);
    }

    public PeerEntity update(String login, Long id, PeerFromRequest newPeerModel)
            throws EntityNotFoundException, PeerAlreadyExistException, PeerValidationFailedException {
        //find old peer
        PeerEntity oldPeer = peerRepo.findByIdAndUserLogin(id, login)
                        .orElseThrow(
                            () -> new PeerNotFoundException(
                                "Peer with id %d does not exist or you have no permission for this peer".formatted(id)
                            )
                        );

        return updatePeer(oldPeer, newPeerModel);
    }

    private PeerEntity deletePeer(PeerEntity peer) {
//        //deleting peer on host
//        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
//        peerConnectHandler.deletePeerOnHost();
        //deleting in db
        peerRepo.delete(peer);
        return peer;
    }

    public PeerEntity delete(Long id) throws PeerNotFoundException {
        // find peer
        PeerEntity peer = peerRepo.findById(id)
                .orElseThrow( () -> new PeerNotFoundException("Peer with id %d not found".formatted(id)) );

        return deletePeer(peer);
    }

    public PeerEntity delete(String login, Long id) throws EntityNotFoundException {
        //find peer
        PeerEntity peer = peerRepo.findByIdAndUserLogin(id, login)
                .orElseThrow(
                        () -> new PeerNotFoundException(
                                "Peer with id %d does not exist or you have no permission for this peer".formatted(id)
                        )
                );

        return deletePeer(peer);
    }

    private String makeRequestToHostForDownloadToken(PeerEntity peer) {
        //make request to host
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
        return peerConnectHandler.getDownloadConfToken();
    }

    public String getDownloadTokenForPeer(Long id) throws PeerNotFoundException {
        PeerEntity peer = peerRepo.findById(id)
                .orElseThrow( () -> new PeerNotFoundException("Peer with id %d not found".formatted(id)) );

        return makeRequestToHostForDownloadToken(peer);
    }

    public String getDownloadTokenForPeer(String login, Long id) throws EntityNotFoundException {
        //find peer
        PeerEntity peer = peerRepo.findByIdAndUserLogin(id, login)
                .orElseThrow(
                        () -> new PeerNotFoundException(
                                "Peer with id %d does not exist or you have no permission for this peer".formatted(id)
                        )
                );

        return makeRequestToHostForDownloadToken(peer);
    }

    public Boolean activatePeerOnHost(Long id) throws PeerNotFoundException {
        //load peer
        PeerEntity entity = peerRepo.findById(id)
                .orElseThrow(
                        () -> new PeerNotFoundException("Peer " + id + " not Found!")
                );
        entity.setIsActivated(true);
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(entity);
        peerConnectHandler.activateOnHost();
        return true;
    }

    public Boolean deactivatePeerOnHost(Long id) throws PeerNotFoundException {
        //load peer
        PeerEntity entity = peerRepo.findById(id)
                .orElseThrow(
                        () -> new PeerNotFoundException("Peer " + id + " not Found!")
                );
        entity.setIsActivated(false);
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(entity);
        peerConnectHandler.deactivateOnHost();
        return true;
    }
}
