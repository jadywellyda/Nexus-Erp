package com.nexuserp.service;

import com.nexuserp.domain.Compra;
import com.nexuserp.domain.Fornecedor;
import com.nexuserp.domain.ItemCompra;
import com.nexuserp.domain.Usuario;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.repository.CompraRepository;
import com.nexuserp.util.SessaoUsuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CompraService {

    private final CompraRepository compraRepository = new CompraRepository();
    private final ProdutoService produtoService = new ProdutoService();

    public Compra finalizarCompra(Compra compra, List<ItemCompra> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new BusinessException("A compra deve conter ao menos um item.");
        }

        BigDecimal total = BigDecimal.ZERO;
        for (ItemCompra item : itens) {
            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new BusinessException("Quantidade inválida para o item: "
                        + (item.getProduto() != null ? item.getProduto().getNome() : "desconhecido"));
            }
            item.setSubtotal(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
            total = total.add(item.getSubtotal());
        }

        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();
        if (usuarioLogado == null) {
            throw new BusinessException("Usuário não logado.");
        }

        compra.setValorTotal(total);
        compra.setDataCompra(LocalDateTime.now());
        compra.setStatus("FINALIZADA");
        compra.setUsuario(usuarioLogado);
        compra.getItens().addAll(itens);

        Compra salva = compraRepository.salvar(compra);

        // Entrada de estoque para cada item
        for (ItemCompra item : itens) {
            produtoService.entradaEstoque(item.getProduto().getId(), item.getQuantidade());
        }

        return salva;
    }

    public List<Compra> listarTodas() {
        return compraRepository.listarTodos();
    }
}
