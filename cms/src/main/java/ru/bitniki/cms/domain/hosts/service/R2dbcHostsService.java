package ru.bitniki.cms.domain.hosts.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.bitniki.cms.domain.exception.EntityNotFoundException;
import ru.bitniki.cms.domain.hosts.dao.R2dbcHostsDao;
import ru.bitniki.cms.domain.hosts.dto.Host;
import ru.bitniki.cms.domain.hosts.dto.R2dbcHostEntity;

@Service
@Transactional
public class R2dbcHostsService implements HostsService {
    private static final Logger LOGGER = LogManager.getLogger();

    private final R2dbcHostsDao hostsDao;

    @Autowired
    public R2dbcHostsService(R2dbcHostsDao hostsDao) {
        this.hostsDao = hostsDao;
    }

    private static void logEntity(Host entity) {
        LOGGER.debug("Found host `{}`", entity);
    }

    private Host toHost(R2dbcHostEntity entity) {
        return new Host(
            entity.getId(),
            entity.getName(),
            entity.getIpaddress(),
            entity.getPort(),
            entity.getHostPassword()
        );
    }

    @Override
    public Flux<Host> getAll() {
        LOGGER.debug("Getting all hosts...");
        return hostsDao.findAll()
                .map(this::toHost)
                .doOnNext(R2dbcHostsService::logEntity);
    }

    @Override
    public Mono<Host> getById(long id) {
        LOGGER.debug("Getting host with id `{}`", id);
        return hostsDao.findById(id)
                .map(this::toHost)
                .switchIfEmpty(
                    Mono.error(new EntityNotFoundException("Host with id `%d` not found".formatted(id)))
                )
                .doOnNext(R2dbcHostsService::logEntity);
    }
}
