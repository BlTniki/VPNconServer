package com.bitniki.VPNconServer.service;

import com.bitniki.VPNconServer.entity.HostEntity;
import com.bitniki.VPNconServer.exception.HostAlreadyExistException;
import com.bitniki.VPNconServer.model.Host;
import com.bitniki.VPNconServer.repository.HostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HostService {
    @Autowired
    private HostRepo hostRepo;

    public Host create (HostEntity host) throws HostAlreadyExistException {
        if(hostRepo.findByIpadress(host.getIpadress()) != null) {
            throw new HostAlreadyExistException("Host with that ip already exist!");
        }
        return Host.toModel(hostRepo.save(host));
    }
}
