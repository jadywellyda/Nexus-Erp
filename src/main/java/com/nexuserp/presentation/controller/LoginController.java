package com.nexuserp.presentation.controller;

import com.nexuserp.domain.Usuario;
import com.nexuserp.service.UsuarioService;
import com.nexuserp.util.AlertaUtil;
import com.nexuserp.util.NavigacaoUtil;
import com.nexuserp.util.SessaoUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        if (email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
            AlertaUtil.aviso("Atenção", "Preencha e-mail e senha.");
            return;
        }

        try {
            Usuario usuario = usuarioService.autenticar(email, senha);
            SessaoUsuario.setUsuarioLogado(usuario);
            AlertaUtil.sucesso("Bem-vindo", "Olá, " + usuario.getNome() + "!");
            abrirMain((Stage) txtEmail.getScene().getWindow());
        } catch (Exception e) {
            AlertaUtil.erro("Erro de login", e.getMessage());
        }
    }

    private void abrirMain(Stage stage) {
        NavigacaoUtil.trocarTela(stage,
                "/com/nexuserp/presentation/view/main.fxml", "NexusERP - Menu Principal");
    }
}
