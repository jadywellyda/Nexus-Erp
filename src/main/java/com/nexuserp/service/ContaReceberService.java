package com.nexuserp.service;

import com.nexuserp.domain.Cliente;
import com.nexuserp.domain.ContaReceber;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.repository.ContaReceberRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ContaReceberService {

    private final ContaReceberRepository contaReceberRepository = new ContaReceberRepository();

    public ContaReceber cadastrar(ContaReceber conta) {
        if (conta.getValor() == null || conta.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor deve ser maior que zero.");
        }
        if (conta.getVencimento() == null) {
            throw new BusinessException("Informe a data de vencimento.");
        }
        return contaReceberRepository.salvar(conta);
    }

    public List<ContaReceber> listarTodas() {
        return contaReceberRepository.listarTodos();
    }

    public List<ContaReceber> listarPendentes() {
        return contaReceberRepository.listarTodos().stream()
                .filter(c -> "PENDENTE".equalsIgnoreCase(c.getStatus()))
                .toList();
    }

    public List<ContaReceber> listarVencidas() {
        LocalDate hoje = LocalDate.now();
        return contaReceberRepository.listarTodos().stream()
                .filter(c -> "PENDENTE".equalsIgnoreCase(c.getStatus())
                        && c.getVencimento().isBefore(hoje))
                .toList();
    }

    public void baixarConta(Long id) {
        ContaReceber conta = contaReceberRepository.buscarPorId(id)
                .orElseThrow(() -> new BusinessException("Conta a receber não encontrada."));
        conta.setStatus("RECEBIDA");
        conta.setDataPagamento(LocalDate.now());
        contaReceberRepository.atualizar(conta);
    }

    public BigDecimal totalPendente() {
        return listarPendentes().stream()
                .map(ContaReceber::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
