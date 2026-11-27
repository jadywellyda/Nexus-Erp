package com.nexuserp.presentation.controller;

import com.nexuserp.domain.Perfil;
import com.nexuserp.domain.Usuario;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.service.PerfilService;
import com.nexuserp.service.UsuarioService;
import com.nexuserp.util.AlertaUtil;
import com.nexuserp.util.NavigacaoUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class CadastroUsuarioController {

    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private ComboBox<Perfil> cmbPerfil;
    @FXML private CheckBox chkAtivo;
    @FXML private Button btnSalvar;
    @FXML private Button btnVoltar;

    private final UsuarioService usuarioService = new UsuarioService();
    private final PerfilService perfilService = new PerfilService();

    @FXML
    public void initialize() {
        List<Perfil> perfis = perfilService.listarTodos();
        ObservableList<Perfil> lista = FXCollections.observableArrayList(perfis);
        cmbPerfil.setItems(lista);
        chkAtivo.setSelected(true);
    }

    @FXML
    private void handleSalvar() {
        try {
            Usuario u = new Usuario();
            u.setNome(txtNome.getText());
            u.setEmail(txtEmail.getText());
            u.setSenhaHash(txtSenha.getText());
            u.setPerfil(cmbPerfil.getValue());
            u.setAtivo(chkAtivo.isSelected());

            usuarioService.cadastrar(u);
            AlertaUtil.sucesso("Sucesso", "Usuário cadastrado com sucesso!");
            limpar();
        } catch (BusinessException e) {
            AlertaUtil.erro("Erro", e.getMessage());
        } catch (Exception e) {
            AlertaUtil.erro("Erro", e.getMessage());
        }
    }

    private void limpar() {
        txtNome.clear();
        txtEmail.clear();
        txtSenha.clear();
        cmbPerfil.setValue(null);
        chkAtivo.setSelected(true);
    }

    @FXML
    private void handleVoltar() {
        NavigacaoUtil.trocarTela((Stage) btnSalvar.getScene().getWindow(),
                "/com/nexuserp/presentation/view/main.fxml", "Menu Principal");
    }
}
