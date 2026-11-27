package com.nexuserp.presentation.controller;

import com.nexuserp.domain.Usuario;
import com.nexuserp.util.AlertaUtil;
import com.nexuserp.util.NavigacaoUtil;
import com.nexuserp.util.SessaoUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainController {

    @FXML private Label lblUsuario;
    @FXML private Label lblPerfil;

    @FXML
    public void initialize() {
        Usuario u = SessaoUsuario.getUsuarioLogado();
        if (u != null) {
            lblUsuario.setText("Usuário: " + u.getNome());
            lblPerfil.setText("Perfil: " + u.getPerfil().getNome());
        }
    }

    @FXML
    private void abrirDashboard() {
        NavigacaoUtil.trocarTela((Stage) lblUsuario.getScene().getWindow(),
                "/com/nexuserp/presentation/view/dashboard.fxml", "Dashboard");
    }

    @FXML
    private void abrirCadastroProduto() {
        NavigacaoUtil.trocarTela((Stage) lblUsuario.getScene().getWindow(),
                "/com/nexuserp/presentation/view/cadastro_produto.fxml", "Cadastro de Produto");
    }

    @FXML
    private void abrirCadastroUsuario() {
        if (!SessaoUsuario.temPermissao("ADMINISTRADOR")) {
            AlertaUtil.erro("Acesso negado", "Apenas administradores podem gerenciar usuários.");
            return;
        }
        NavigacaoUtil.trocarTela((Stage) lblUsuario.getScene().getWindow(),
                "/com/nexuserp/presentation/view/cadastro_usuario.fxml", "Cadastro de Usuário");
    }

    @FXML
    private void abrirCadastroCliente() {
        NavigacaoUtil.trocarTela((Stage) lblUsuario.getScene().getWindow(),
                "/com/nexuserp/presentation/view/cadastro_cliente.fxml", "Cadastro de Cliente");
    }

    @FXML
    private void abrirCadastroFornecedor() {
        NavigacaoUtil.trocarTela((Stage) lblUsuario.getScene().getWindow(),
                "/com/nexuserp/presentation/view/cadastro_fornecedor.fxml", "Cadastro de Fornecedor");
    }

    @FXML
    private void abrirVendas() {
        NavigacaoUtil.trocarTela((Stage) lblUsuario.getScene().getWindow(),
                "/com/nexuserp/presentation/view/vendas.fxml", "Vendas");
    }

    @FXML
    private void abrirCompras() {
        NavigacaoUtil.trocarTela((Stage) lblUsuario.getScene().getWindow(),
                "/com/nexuserp/presentation/view/compras.fxml", "Compras");
    }

    @FXML
    private void abrirEstoque() {
        NavigacaoUtil.trocarTela((Stage) lblUsuario.getScene().getWindow(),
                "/com/nexuserp/presentation/view/estoque.fxml", "Estoque");
    }

    @FXML
    private void abrirFinanceiro() {
        NavigacaoUtil.trocarTela((Stage) lblUsuario.getScene().getWindow(),
                "/com/nexuserp/presentation/view/financeiro.fxml", "Financeiro");
    }

    @FXML
    private void abrirRelatorios() {
        NavigacaoUtil.trocarTela((Stage) lblUsuario.getScene().getWindow(),
                "/com/nexuserp/presentation/view/relatorios.fxml", "Relatórios");
    }

    @FXML
    private void logout() {
        SessaoUsuario.limpar();
        NavigacaoUtil.trocarTela((Stage) lblUsuario.getScene().getWindow(),
                "/com/nexuserp/presentation/view/login.fxml", "NexusERP - Login");
    }
}
