package com.nexuserp.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class AuditoriaLog extends BaseEntity {

    @Column(length = 100)
    private String tabela;

    @Column(length = 20)
    private String acao;

    @Column(name = "registro_id")
    private Integer registroId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "data_acao")
    private LocalDateTime dataAcao;

    @Column(columnDefinition = "TEXT")
    private String valoresAntigos;

    @Column(columnDefinition = "TEXT")
    private String valoresNovos;
}
