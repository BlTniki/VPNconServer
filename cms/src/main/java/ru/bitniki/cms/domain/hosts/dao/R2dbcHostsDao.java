package ru.bitniki.cms.domain.hosts.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.bitniki.cms.domain.hosts.dto.R2dbcHostEntity;

public interface R2dbcHostsDao extends ReactiveCrudRepository<R2dbcHostEntity, Long> {
}
