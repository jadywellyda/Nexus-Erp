package com.nexuserp.service;

import com.nexuserp.domain.Produto;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.repository.ProdutoRepository;

import java.math.BigDecimal;
import java.util.List;

public class ProdutoService {

    private final ProdutoRepository produtoRepository = new ProdutoRepository();

    public Produto cadastrar(Produto produto) {
        if (produtoRepository.existsBySku(produto.getSku())) {
            throw new BusinessException("SKU já cadastrado.");
        }
        if (produto.getPrecoVenda().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Preço de venda deve ser maior que zero.");
        }
        return produtoRepository.salvar(produto);
    }

    public void darBaixaEstoque(Long produtoId, int quantidade) {
        Produto p = produtoRepository.buscarPorId(produtoId)
                .orElseThrow(() -> new BusinessException("Produto não encontrado."));
        if (p.getEstoqueAtual() < quantidade) {
            throw new BusinessException("Estoque insuficiente para o produto: " + p.getNome());
        }
        p.setEstoqueAtual(p.getEstoqueAtual() - quantidade);
        produtoRepository.atualizar(p);
    }

    public void entradaEstoque(Long produtoId, int quantidade) {
        Produto p = produtoRepository.buscarPorId(produtoId)
                .orElseThrow(() -> new BusinessException("Produto não encontrado."));
        p.setEstoqueAtual(p.getEstoqueAtual() + quantidade);
        produtoRepository.atualizar(p);
    }

    public List<Produto> listarBaixoEstoque() {
        return produtoRepository.findByEstoqueBaixo();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.buscarPorId(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado."));
    }

    public List<Produto> listarTodos() {
        return produtoRepository.listarTodos();
    }
}
