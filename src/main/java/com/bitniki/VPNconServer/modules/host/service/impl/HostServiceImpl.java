package com.bitniki.VPNconServer.modules.host.service.impl;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.host.exception.HostAlreadyExistException;
import com.bitniki.VPNconServer.modules.host.exception.HostNotFoundException;
import com.bitniki.VPNconServer.modules.host.exception.HostValidationFailedException;
import com.bitniki.VPNconServer.modules.host.model.Host;
import com.bitniki.VPNconServer.modules.host.repository.HostRepo;
import com.bitniki.VPNconServer.modules.host.service.HostService;
import com.bitniki.VPNconServer.modules.host.validator.HostValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class HostServiceImpl implements HostService {
    @Autowired
    private HostRepo hostRepo;

    public List<Host> getAll () {
        return StreamSupport.stream(hostRepo.findAll().spliterator(), false)
                .map(Host::toModel)
                .toList();
    }

    public Host getOne (Long id) throws HostNotFoundException {
        return hostRepo.findById(id)
                .map(Host::toModel)
                .orElseThrow(
                        () -> new HostNotFoundException("Host with id %d not found".formatted(id))
                );
    }

    public Host create (HostEntity host) throws HostAlreadyExistException, HostValidationFailedException {
        // valid entity
        HostValidator hostValidator = HostValidator.validateAllFields(host);
        if(hostValidator.hasFails()) {
            throw new HostValidationFailedException(hostValidator.toString());
        }

        // check name unique
        if(hostRepo.findByName(host.getName()).isPresent()) {
            throw new HostAlreadyExistException(
                    "Host with name %s already exist!".formatted(host.getName())
            );
        }

        // check ipaddress unique
        if(hostRepo.findByIpaddress(host.getIpaddress()).isPresent()) {
            throw new HostAlreadyExistException(
                    "Host with ip %s already exist!".formatted(host.getIpaddress())
            );
        }
        return Host.toModel(hostRepo.save(host));
    }

    public Host update (Long id, HostEntity newHost) throws HostNotFoundException, HostAlreadyExistException, HostValidationFailedException {
        // valid entity non null fields
        HostValidator hostValidator = HostValidator.validateNonNullFields(newHost);
        if(hostValidator.hasFails()) {
            throw new HostValidationFailedException(hostValidator.toString());
        }

        // if we have new name check its unique
        if(hostRepo.findByName(newHost.getName()).isPresent()) {
            throw new HostAlreadyExistException(
                    "Host with name %s already exist!".formatted(newHost.getName())
            );
        }

        //if we have new ipaddress check its unique
        if (newHost.getIpaddress() != null && hostRepo.findByIpaddress(newHost.getIpaddress()).isPresent()) {
            throw new HostAlreadyExistException(
                    "Host with ip %s already exist!".formatted(newHost.getIpaddress())
            );
        }

        // load old entity
        HostEntity oldHost = hostRepo.findById(id).orElseThrow(
                () -> new HostNotFoundException("Host with id %d not found".formatted(id))
        );

        return Host.toModel(hostRepo.save(oldHost.updateWith(newHost)));
    }

    public Host delete (Long id) throws HostNotFoundException {
        // load entity
        HostEntity host = hostRepo.findById(id).orElseThrow(
                () -> new HostNotFoundException("Host with id %d not found".formatted(id))
        );

        hostRepo.delete(host);
        return Host.toModel(host);
    }
}