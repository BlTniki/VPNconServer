package ru.bitniki.sms.domain.roles.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.bitniki.sms.domain.roles.dto.RoleEntity;

public interface R2dbcRolesDao extends ReactiveCrudRepository<RoleEntity, String> {
}
