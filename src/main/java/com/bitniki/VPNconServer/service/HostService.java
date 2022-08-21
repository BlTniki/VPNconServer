package com.bitniki.VPNconServer.service;

import com.bitniki.VPNconServer.entity.HostEntity;
import com.bitniki.VPNconServer.exception.HostAlreadyExistException;
import com.bitniki.VPNconServer.exception.HostNotFoundException;
import com.bitniki.VPNconServer.model.Host;
import com.bitniki.VPNconServer.model.HostWithRelations;
import com.bitniki.VPNconServer.repository.HostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HostService {
    @Autowired
    private HostRepo hostRepo;

    public List<HostWithRelations> getAll () {
        List<HostEntity> hosts = new ArrayList<>();
        hostRepo.findAll().forEach(hosts::add);
        return hosts.stream().map(HostWithRelations::toModel).collect(Collectors.toList());
    }

    public HostWithRelations getOne (Long id) throws HostNotFoundException {
        Optional<HostEntity> hostEntityOptional = hostRepo.findById(id);
        if(hostEntityOptional.isPresent()) return HostWithRelations.toModel(hostEntityOptional.get());
        else throw new HostNotFoundException("Host not found");
    }

    public Host create (HostEntity host) throws HostAlreadyExistException {
        if(hostRepo.findByIpadress(host.getIpadress()) != null) {
            throw new HostAlreadyExistException("Host with that ip already exist!");
        }
        return Host.toModel(hostRepo.save(host));
    }

    public Host update (Long id, HostEntity newHost) throws HostNotFoundException, HostAlreadyExistException {
        //if we have new ipadress check its unique
        if (newHost.getIpadress() != null && hostRepo.findByIpadress(newHost.getIpadress()) != null) {
            throw new HostAlreadyExistException("Host with that ip already exist!");
        }

        Optional<HostEntity> oldHostEntityOptional;
        HostEntity oldHost;
        oldHostEntityOptional = hostRepo.findById(id);
        if(oldHostEntityOptional.isPresent()) oldHost = oldHostEntityOptional.get();
        else throw new HostNotFoundException("Host not found");


        return Host.toModel(hostRepo.save(HostEntity.updateHost(oldHost, newHost)));
    }

    public Host delete (Long id) throws HostNotFoundException {
        Optional<HostEntity> hostEntityOptional;
        HostEntity host;
        hostEntityOptional = hostRepo.findById(id);
        if(hostEntityOptional.isPresent()) host = hostEntityOptional.get();
        else throw new HostNotFoundException("Host not found");

        hostRepo.delete(host);
        return Host.toModel(host);
    }
}
