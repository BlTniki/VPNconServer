package com.bitniki.VPNconServer.modules.metacodes.entity;

import com.bitniki.VPNconServer.modules.metacodes.operation.Operation;
import lombok.*;

import javax.persistence.*;

/**
 * Сущность Metacode, который содержит код и действие, которе следует выполнить при применении этого кода.
 */
@Entity
@Table (name = "metacodes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MetacodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Код, по которому проверяется разрешение на действие.
     */
    @Column(nullable = false, unique = true)
    private String code;

    /**
     * Действие, которое следует сделать.
     */
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Operation operation;
}
