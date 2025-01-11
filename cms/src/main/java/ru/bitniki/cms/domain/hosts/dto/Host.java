package ru.bitniki.cms.domain.hosts.dto;

import jakarta.validation.constraints.NotNull;

public record Host(
        @NotNull Long id,
        @NotNull String name,
        @NotNull String ipaddress,
        @NotNull Integer port,
        @NotNull String hostPassword
) {}
