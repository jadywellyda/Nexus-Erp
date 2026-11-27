package com.nexuserp.presentation.controller;

import com.nexuserp.domain.Cliente;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.service.ClienteService;
import com.nexuserp.util.AlertaUtil;
import com.nexuserp.util.NavigacaoUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CadastroClienteController {

    @FXML private ComboBox<String> cmbTipoPessoa;
    @FXML private TextField txtNomeRazaoSocial;
    @FXML private TextField txtCpfCnpj;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtEndereco;
    @FXML private Button btnSalvar;
    @FXML private Button btnVoltar;

    private final ClienteService clienteService = new ClienteService();

    @FXML
    public void initialize() {
        cmbTipoPessoa.getItems().addAll("FISICA", "JURIDICA");
        cmbTipoPessoa.setValue("FISICA");
    }

    @FXML
    private void handleSalvar() {
        try {
            Cliente c = new Cliente();
            c.setTipoPessoa(cmbTipoPessoa.getValue());
            c.setNomeRazaoSocial(txtNomeRazaoSocial.getText());
            c.setCpfCnpj(txtCpfCnpj.getText());
            c.setTelefone(txtTelefone.getText());
            c.setEmail(txtEmail.getText());
            c.setEndereco(txtEndereco.getText());

            clienteService.cadastrar(c);
            AlertaUtil.sucesso("Sucesso", "Cliente cadastrado com sucesso!");
            limpar();
        } catch (BusinessException e) {
            AlertaUtil.erro("Erro", e.getMessage());
        } catch (Exception e) {
            AlertaUtil.erro("Erro", e.getMessage());
        }
    }

    private void limpar() {
        txtNomeRazaoSocial.clear();
        txtCpfCnpj.clear();
        txtTelefone.clear();
        txtEmail.clear();
        txtEndereco.clear();
    }

    @FXML
    private void handleVoltar() {
        NavigacaoUtil.trocarTela((Stage) btnSalvar.getScene().getWindow(),
                "/com/nexuserp/presentation/view/main.fxml", "Menu Principal");
    }
}
