package com.nexuserp.service;

import com.nexuserp.domain.Cliente;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.repository.ClienteRepository;

import java.util.List;

public class ClienteService {

    private final ClienteRepository clienteRepository = new ClienteRepository();

    public Cliente cadastrar(Cliente cliente) {
        if (cliente.getCpfCnpj() != null && clienteRepository.existsByCpfCnpj(cliente.getCpfCnpj())) {
            throw new BusinessException("CPF/CNPJ já cadastrado.");
        }
        return clienteRepository.salvar(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.listarTodos();
    }
}
