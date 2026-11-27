package com.nexuserp.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Produto extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precoCusto;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precoVenda;

    @Column(nullable = false)
    private Integer estoqueMinimo;

    @Column(nullable = false)
    private Integer estoqueAtual;

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<ItemVenda> itensVenda;

    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<ItemCompra> itensCompra;

    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<MovimentacaoEstoque> movimentacoes;
}
