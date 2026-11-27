package com.nexuserp.service;

import com.nexuserp.domain.Categoria;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.repository.CategoriaRepository;

import java.util.List;

public class CategoriaService {

    private final CategoriaRepository categoriaRepository = new CategoriaRepository();

    public Categoria cadastrar(Categoria categoria) {
        if (categoriaRepository.existsByNome(categoria.getNome())) {
            throw new BusinessException("Categoria já cadastrada.");
        }
        return categoriaRepository.salvar(categoria);
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.listarTodos();
    }
}
