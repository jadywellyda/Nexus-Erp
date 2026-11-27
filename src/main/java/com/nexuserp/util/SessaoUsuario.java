package com.nexuserp.util;

import com.nexuserp.domain.Usuario;

public class SessaoUsuario {

    private static Usuario usuarioLogado;

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static void limpar() {
        usuarioLogado = null;
    }

    public static boolean temPermissao(String perfilNome) {
        return usuarioLogado != null
                && usuarioLogado.getPerfil() != null
                && perfilNome.equalsIgnoreCase(usuarioLogado.getPerfil().getNome());
    }
}
