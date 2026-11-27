package com.nexuserp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@lombok.Data
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @jakarta.persistence.Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    @jakarta.persistence.PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
    }
}
