package ru.bitniki.cms.domain.hosts.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.bitniki.cms.IntegrationTest;
import ru.bitniki.cms.domain.hosts.dao.R2dbcHostsDao;
import ru.bitniki.cms.domain.hosts.dto.R2dbcHostEntity;
import ru.bitniki.cms.rollback.TrxStepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class R2dbcHostsServiceTest extends IntegrationTest {
    @Autowired
    TrxStepVerifier stepVerifier;
    @Autowired
    R2dbcHostsService hostsService;
    @Autowired
    R2dbcHostsDao hostsDao;

    @Test
    void getAll() {
        hostsDao.save(new R2dbcHostEntity(null, "lol1", "lol1", 123, "kek"))
                .then(hostsDao.save(new R2dbcHostEntity(null, "lol2", "lol2", 123, "kek")))
                .then(hostsDao.save(new R2dbcHostEntity(null, "lol3", "lol3", 123, "kek")))
                .thenMany(hostsService.getAll())
                .as(stepVerifier::create)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void getById() {
        hostsDao.save(new R2dbcHostEntity(null, "lol1", "lol1", 123, "kek"))
                .flatMap(entity -> hostsService.getById(entity.getId()))
                .as(stepVerifier::create)
                .assertNext(dto -> {
                    assertThat(dto.id()).isEqualTo(1L);
                    assertThat(dto.name()).isEqualTo("lol1");
                })
                .verifyComplete();
    }
}