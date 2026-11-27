package com.nexuserp.service;

import com.nexuserp.domain.Perfil;
import com.nexuserp.repository.PerfilRepository;

import java.util.List;

public class PerfilService {

    private final PerfilRepository perfilRepository = new PerfilRepository();

    public Perfil salvar(Perfil perfil) {
        return perfilRepository.salvar(perfil);
    }

    public List<Perfil> listarTodos() {
        return perfilRepository.listarTodos();
    }
}
