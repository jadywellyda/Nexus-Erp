package com.nexuserp.service;

import com.nexuserp.domain.ItemVenda;
import com.nexuserp.domain.Venda;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.repository.VendaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VendaService {

    private final VendaRepository vendaRepository = new VendaRepository();
    private final ProdutoService produtoService = new ProdutoService();

    public Venda finalizarVenda(Venda venda, List<ItemVenda> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new BusinessException("A venda deve conter ao menos um item.");
        }

        BigDecimal total = BigDecimal.ZERO;
        for (ItemVenda item : itens) {
            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new BusinessException("Quantidade inválida para o item: "
                        + (item.getProduto() != null ? item.getProduto().getNome() : "desconhecido"));
            }
            item.setSubtotal(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
            total = total.add(item.getSubtotal());
        }

        venda.setValorTotal(total);
        venda.setDataVenda(LocalDateTime.now());
        venda.setStatus("FINALIZADA");
        venda.getItens().addAll(itens);

        Venda salva = vendaRepository.salvar(venda);

        for (ItemVenda item : itens) {
            produtoService.darBaixaEstoque(item.getProduto().getId(), item.getQuantidade());
        }

        return salva;
    }

    public List<Venda> vendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vendaRepository.findByPeriodo(inicio, fim);
    }
}
