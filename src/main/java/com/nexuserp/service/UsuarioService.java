package com.nexuserp.service;

import com.nexuserp.domain.Usuario;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.repository.UsuarioRepository;
import com.nexuserp.util.BCryptUtil;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();

    public Usuario cadastrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new BusinessException("E-mail já cadastrado.");
        }
        usuario.setSenhaHash(BCryptUtil.hash(usuario.getSenhaHash()));
        return usuarioRepository.salvar(usuario);
    }

    public Usuario autenticar(String email, String senha) {
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new BusinessException("E-mail ou senha inválidos."));
        if (!Boolean.TRUE.equals(usuario.getAtivo())) {
            throw new BusinessException("Usuário inativo. Procure o administrador.");
        }
        if (!BCryptUtil.check(senha, usuario.getSenhaHash())) {
            throw new BusinessException("E-mail ou senha inválidos.");
        }
        return usuario;
    }

    public void alterarSenha(Long id, String senhaNova) {
        Usuario u = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));
        u.setSenhaHash(BCryptUtil.hash(senhaNova));
        usuarioRepository.atualizar(u);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));
    }
}
