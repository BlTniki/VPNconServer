package ru.bitniki.sms.domen.roles.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.bitniki.sms.domen.roles.dto.RoleEntity;

public interface R2dbcRolesDao extends ReactiveCrudRepository<RoleEntity, String> {
}
