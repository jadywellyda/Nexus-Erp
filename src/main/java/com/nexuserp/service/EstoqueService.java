package com.nexuserp.service;

import com.nexuserp.domain.MovimentacaoEstoque;
import com.nexuserp.domain.Produto;
import com.nexuserp.domain.Usuario;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.repository.MovimentacaoEstoqueRepository;
import com.nexuserp.repository.ProdutoRepository;
import com.nexuserp.util.SessaoUsuario;

import java.time.LocalDateTime;
import java.util.List;

public class EstoqueService {

    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private final MovimentacaoEstoqueRepository movimentacaoRepository = new MovimentacaoEstoqueRepository();

    public List<Produto> listarProdutos() {
        return produtoRepository.listarTodos();
    }

    public List<Produto> listarBaixoEstoque() {
        return produtoRepository.findByEstoqueBaixo();
    }

    public void registrarEntrada(Long produtoId, int quantidade, String referencia) {
        Produto p = produtoRepository.buscarPorId(produtoId)
                .orElseThrow(() -> new BusinessException("Produto não encontrado."));
        if (quantidade <= 0) {
            throw new BusinessException("Quantidade deve ser maior que zero.");
        }

        p.setEstoqueAtual(p.getEstoqueAtual() + quantidade);
        produtoRepository.atualizar(p);

        registrarMovimentacao(p, "ENTRADA", quantidade, referencia);
    }

    public void registrarSaida(Long produtoId, int quantidade, String referencia) {
        Produto p = produtoRepository.buscarPorId(produtoId)
                .orElseThrow(() -> new BusinessException("Produto não encontrado."));
        if (quantidade <= 0) {
            throw new BusinessException("Quantidade deve ser maior que zero.");
        }
        if (p.getEstoqueAtual() < quantidade) {
            throw new BusinessException("Estoque insuficiente para o produto: " + p.getNome());
        }

        p.setEstoqueAtual(p.getEstoqueAtual() - quantidade);
        produtoRepository.atualizar(p);

        registrarMovimentacao(p, "SAIDA", quantidade, referencia);
    }

    private void registrarMovimentacao(Produto produto, String tipo, int quantidade, String referencia) {
        Usuario usuario = SessaoUsuario.getUsuarioLogado();
        MovimentacaoEstoque m = MovimentacaoEstoque.builder()
                .produto(produto)
                .tipo(tipo)
                .quantidade(quantidade)
                .dataMovimento(LocalDateTime.now())
                .usuario(usuario)
                .referencia(referencia)
                .build();
        movimentacaoRepository.salvar(m);
    }

    public List<MovimentacaoEstoque> listarMovimentacoes() {
        return movimentacaoRepository.listarTodos();
    }
}
