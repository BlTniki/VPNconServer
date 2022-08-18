package com.bitniki.VPNconServer.service;

import com.bitniki.VPNconServer.entity.HostEntity;
import com.bitniki.VPNconServer.exception.HostAlreadyExistException;
import com.bitniki.VPNconServer.exception.HostNotFoundException;
import com.bitniki.VPNconServer.model.Host;
import com.bitniki.VPNconServer.repository.HostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class HostService {
    @Autowired
    private HostRepo hostRepo;

    public List<Host> getAll () {
        List<HostEntity> hosts = new ArrayList<>();
        hostRepo.findAll().forEach(hosts::add);
        return hosts.stream().map(Host::toModel).collect(Collectors.toList());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Host getOne (Long id) throws HostNotFoundException {
        try {
            return Host.toModel(hostRepo.findById(id).get());
        } catch (NoSuchElementException e) {
            throw new HostNotFoundException("User not found");
        }
    }

    public Host create (HostEntity host) throws HostAlreadyExistException {
        if(hostRepo.findByIpadress(host.getIpadress()) != null) {
            throw new HostAlreadyExistException("Host with that ip already exist!");
        }
        return Host.toModel(hostRepo.save(host));
    }
}
