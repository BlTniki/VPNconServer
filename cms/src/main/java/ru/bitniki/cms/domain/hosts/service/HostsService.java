package ru.bitniki.cms.domain.hosts.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.bitniki.cms.domain.hosts.dto.Host;

public interface HostsService {
    /**
     * Return list of {@link Host}.
     * @return list of {@link Host}
     */
    Flux<Host> getAll();

    /**
     * Return host by given id.
     * @param id host id
     * @return {@link Host} or
     * {@link ru.bitniki.cms.domain.exception.EntityNotFoundException} if a host with this id not exists
     */
    Mono<Host> getById(long id);
}
