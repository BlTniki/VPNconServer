package com.bitniki.VPNconServer.modules.host.service.impl;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.host.exception.HostAlreadyExistException;
import com.bitniki.VPNconServer.modules.host.exception.HostNotFoundException;
import com.bitniki.VPNconServer.modules.host.exception.HostValidationFailedException;
import com.bitniki.VPNconServer.modules.host.model.HostFromRequest;
import com.bitniki.VPNconServer.modules.host.repository.HostRepo;
import com.bitniki.VPNconServer.modules.host.service.HostService;
import com.bitniki.VPNconServer.modules.host.validator.HostValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Spliterator;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HostServiceImpl implements HostService {
    private final HostRepo hostRepo;

    public Spliterator<HostEntity> getAll () {
        return hostRepo.findAll().spliterator();
    }

    public HostEntity getOneById(@NotNull Long id) throws HostNotFoundException {
        return hostRepo.findById(id)
                .orElseThrow(
                        () -> new HostNotFoundException("Host with id %d not found".formatted(id))
                );
    }

    public HostEntity create (@NotNull HostFromRequest model) throws HostAlreadyExistException, HostValidationFailedException {
        // valid entity
        HostValidator hostValidator = HostValidator.validateAllFields(model);
        if(hostValidator.hasFails()) {
            throw new HostValidationFailedException(hostValidator.toString());
        }

        // check name unique
        if(hostRepo.findByName(model.getName()).isPresent()) {
            throw new HostAlreadyExistException(
                    "Host with name %s already exist!".formatted(model.getName())
            );
        }

        // check ipaddress and port pair unique
        if(hostRepo.findByIpaddressAndPort(model.getIpaddress(), model.getPort()).isPresent()) {
            throw new HostAlreadyExistException(
                    "Host with ip %s and port %d already exist!".formatted(model.getIpaddress(), model.getPort())
            );
        }

        //Create entity
        HostEntity entity = HostEntity.builder()
                .name(model.getName())
                .ipaddress(model.getIpaddress())
                .port(model.getPort())
                .hostInternalNetworkPrefix(model.getHostInternalNetworkPrefix())
                .hostPassword(model.getHostPassword())
                .hostPublicKey(model.getHostPublicKey())
                .build();

        return hostRepo.save(entity);
    }

    public HostEntity updateById(@NotNull Long id, @NotNull HostFromRequest newHost) throws HostNotFoundException, HostAlreadyExistException, HostValidationFailedException {
        // valid entity non null fields
        HostValidator hostValidator = HostValidator.validateNonNullFields(newHost);
        if(hostValidator.hasFails()) {
            throw new HostValidationFailedException(hostValidator.toString());
        }

        // load old entity
        HostEntity oldHost = hostRepo.findById(id).orElseThrow(
                () -> new HostNotFoundException("Host with id %d not found".formatted(id))
        );

        // if we have new name check its unique
        if(newHost.getName() != null) {
            if (hostRepo.findByName(newHost.getName()).isPresent()) {
                throw new HostAlreadyExistException(
                        "Host with name %s already exist!".formatted(newHost.getName())
                );
            } else {
                oldHost.setName(newHost.getName());
            }
        }

        //if we have new ipaddress and port pair check its unique
        if (newHost.getIpaddress() != null || newHost.getPort() != null) {
            String ipaddress = newHost.getIpaddress() != null ? newHost.getIpaddress() : oldHost.getIpaddress();
            Integer port = newHost.getPort() != null ? newHost.getPort() : oldHost.getPort();
            if (hostRepo.findByIpaddressAndPort( ipaddress, port ).isPresent()) {
                throw new HostAlreadyExistException(
                        "Host with ip %s and port %d already exist!".formatted(newHost.getIpaddress(), newHost.getPort())
                );
            } else {
                oldHost.setIpaddress(ipaddress);
                oldHost.setPort(port);
            }
        }

        //Set other not null fields
        if (newHost.getHostInternalNetworkPrefix() != null) {
            oldHost.setHostInternalNetworkPrefix(newHost.getHostInternalNetworkPrefix());
        }
        if (newHost.getHostPassword() != null) {
            oldHost.setHostPassword(newHost.getHostPassword());
        }
        if (newHost.getHostPublicKey() != null) {
            oldHost.setHostPublicKey(newHost.getHostPublicKey());
        }

        return hostRepo.save(oldHost);
    }

    public HostEntity deleteById(@NotNull Long id) throws HostNotFoundException {
        // load entity
        HostEntity host = hostRepo.findById(id).orElseThrow(
                () -> new HostNotFoundException("Host with id %d not found".formatted(id))
        );

        hostRepo.delete(host);
        return host;
    }

    public Map<String, String> getValidationRegex() {
        //Get regex from validator and cook answer
        Map<String, String> patterns = new HashMap<>();
        patterns.put("ipaddress", HostValidator.ipaddressPattern.pattern());
        patterns.put("networkPrefix", HostValidator.hostInternalNetworkPrefixPattern.pattern());

        return patterns;
    }
}
