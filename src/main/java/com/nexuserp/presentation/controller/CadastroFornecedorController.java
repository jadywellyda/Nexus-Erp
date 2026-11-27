package com.nexuserp.presentation.controller;

import com.nexuserp.domain.Fornecedor;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.service.FornecedorService;
import com.nexuserp.util.AlertaUtil;
import com.nexuserp.util.NavigacaoUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CadastroFornecedorController {

    @FXML private TextField txtRazaoSocial;
    @FXML private TextField txtCnpj;
    @FXML private TextField txtContato;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEndereco;
    @FXML private Button btnSalvar;
    @FXML private Button btnVoltar;

    private final FornecedorService fornecedorService = new FornecedorService();

    @FXML
    private void handleSalvar() {
        try {
            Fornecedor f = new Fornecedor();
            f.setRazaoSocial(txtRazaoSocial.getText());
            f.setCnpj(txtCnpj.getText());
            f.setContato(txtContato.getText());
            f.setEmail(txtEmail.getText());
            f.setTelefone(txtTelefone.getText());
            f.setEndereco(txtEndereco.getText());

            fornecedorService.cadastrar(f);
            AlertaUtil.sucesso("Sucesso", "Fornecedor cadastrado com sucesso!");
            limpar();
        } catch (BusinessException e) {
            AlertaUtil.erro("Erro", e.getMessage());
        } catch (Exception e) {
            AlertaUtil.erro("Erro", e.getMessage());
        }
    }

    private void limpar() {
        txtRazaoSocial.clear();
        txtCnpj.clear();
        txtContato.clear();
        txtEmail.clear();
        txtTelefone.clear();
        txtEndereco.clear();
    }

    @FXML
    private void handleVoltar() {
        NavigacaoUtil.trocarTela((Stage) btnSalvar.getScene().getWindow(),
                "/com/nexuserp/presentation/view/main.fxml", "Menu Principal");
    }
}
