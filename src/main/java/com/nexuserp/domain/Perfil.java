package com.nexuserp.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "perfis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Perfil extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String nome;

    @Column(length = 200)
    private String descricao;

    @OneToMany(mappedBy = "perfil", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Usuario> usuarios;
}
