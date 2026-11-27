package com.nexuserp.service;

import com.nexuserp.domain.ContaPagar;
import com.nexuserp.domain.Fornecedor;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.repository.ContaPagarRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ContaPagarService {

    private final ContaPagarRepository contaPagarRepository = new ContaPagarRepository();

    public ContaPagar cadastrar(ContaPagar conta) {
        if (conta.getValor() == null || conta.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor deve ser maior que zero.");
        }
        if (conta.getVencimento() == null) {
            throw new BusinessException("Informe a data de vencimento.");
        }
        return contaPagarRepository.salvar(conta);
    }

    public List<ContaPagar> listarTodas() {
        return contaPagarRepository.listarTodos();
    }

    public List<ContaPagar> listarPendentes() {
        return contaPagarRepository.listarTodos().stream()
                .filter(c -> "PENDENTE".equalsIgnoreCase(c.getStatus()))
                .toList();
    }

    public List<ContaPagar> listarVencidas() {
        LocalDate hoje = LocalDate.now();
        return contaPagarRepository.listarTodos().stream()
                .filter(c -> "PENDENTE".equalsIgnoreCase(c.getStatus())
                        && c.getVencimento().isBefore(hoje))
                .toList();
    }

    public void baixarConta(Long id) {
        ContaPagar conta = contaPagarRepository.buscarPorId(id)
                .orElseThrow(() -> new BusinessException("Conta a pagar não encontrada."));
        conta.setStatus("PAGA");
        conta.setDataPagamento(LocalDate.now());
        contaPagarRepository.atualizar(conta);
    }

    public BigDecimal totalPendente() {
        return listarPendentes().stream()
                .map(ContaPagar::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
