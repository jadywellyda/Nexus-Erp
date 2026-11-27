package com.nexuserp.presentation.dto;

import com.nexuserp.domain.Produto;
import java.math.BigDecimal;

public class ItemCompraDTO {

    private Produto produto;
    private Integer quantidade;
    private BigDecimal precoUnitario;

    public ItemCompraDTO() {
    }

    public ItemCompraDTO(Produto produto, Integer quantidade, BigDecimal precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public BigDecimal getSubtotal() {
        if (quantidade == null || precoUnitario == null) {
            return BigDecimal.ZERO;
        }
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public String getProdutoNome() {
        return produto != null ? produto.getNome() : "-";
    }
}
