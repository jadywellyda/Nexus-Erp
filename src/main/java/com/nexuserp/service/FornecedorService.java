package com.nexuserp.service;

import com.nexuserp.domain.Fornecedor;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.repository.FornecedorRepository;

import java.util.List;

public class FornecedorService {

    private final FornecedorRepository fornecedorRepository = new FornecedorRepository();

    public Fornecedor cadastrar(Fornecedor fornecedor) {
        if (fornecedorRepository.existsByCnpj(fornecedor.getCnpj())) {
            throw new BusinessException("CNPJ já cadastrado.");
        }
        return fornecedorRepository.salvar(fornecedor);
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.listarTodos();
    }
}
