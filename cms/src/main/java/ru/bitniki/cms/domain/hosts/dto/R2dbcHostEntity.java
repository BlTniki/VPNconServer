package ru.bitniki.cms.domain.hosts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@AllArgsConstructor
@ToString
@Table(name = "hosts")
public class R2dbcHostEntity {
    @Id
    private Long id;
    private String name;
    private String ipaddress;
    private Integer port;
    private String hostPassword;
}
