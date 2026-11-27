package com.nexuserp.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "fornecedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Fornecedor extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String razaoSocial;

    @Column(unique = true, length = 20)
    private String cnpj;

    @Column(length = 100)
    private String contato;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(length = 500)
    private String endereco;

    @OneToMany(mappedBy = "fornecedor", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Compra> compras;

    @OneToMany(mappedBy = "fornecedor", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<ContaPagar> contasPagar;
}
