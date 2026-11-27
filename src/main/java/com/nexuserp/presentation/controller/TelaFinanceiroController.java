package com.nexuserp.presentation.controller;

import com.nexuserp.domain.Cliente;
import com.nexuserp.domain.ContaPagar;
import com.nexuserp.domain.ContaReceber;
import com.nexuserp.domain.Fornecedor;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.service.ClienteService;
import com.nexuserp.service.ContaPagarService;
import com.nexuserp.service.ContaReceberService;
import com.nexuserp.service.FornecedorService;
import com.nexuserp.util.AlertaUtil;
import com.nexuserp.util.NavigacaoUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TelaFinanceiroController {

    // Contas a Receber
    @FXML private ComboBox<Cliente> cmbClienteReceber;
    @FXML private TextField txtDescricaoReceber;
    @FXML private TextField txtValorReceber;
    @FXML private DatePicker dtVencimentoReceber;
    @FXML private Button btnCadastrarReceber;

    // Contas a Pagar
    @FXML private ComboBox<Fornecedor> cmbFornecedorPagar;
    @FXML private TextField txtDescricaoPagar;
    @FXML private TextField txtValorPagar;
    @FXML private DatePicker dtVencimentoPagar;
    @FXML private Button btnCadastrarPagar;

    // Labels de resumo
    @FXML private Label lblTotalReceber;
    @FXML private Label lblTotalPagar;

    // Tabelas
    @FXML private TableView<ContaReceber> tabelaReceber;
    @FXML private TableView<ContaPagar> tabelaPagar;

    private final ContaReceberService contaReceberService = new ContaReceberService();
    private final ContaPagarService contaPagarService = new ContaPagarService();
    private final ClienteService clienteService = new ClienteService();
    private final FornecedorService fornecedorService = new FornecedorService();

    @FXML
    public void initialize() {
        carregarClientes();
        carregarFornecedores();
        carregarTabelas();
        atualizarResumo();
    }

    private void carregarClientes() {
        List<Cliente> clientes = clienteService.listarTodos();
        cmbClienteReceber.setItems(FXCollections.observableArrayList(clientes));
    }

    private void carregarFornecedores() {
        List<Fornecedor> fornecedores = fornecedorService.listarTodos();
        cmbFornecedorPagar.setItems(FXCollections.observableArrayList(fornecedores));
    }

    private void carregarTabelas() {
        tabelaReceber.setItems(FXCollections.observableArrayList(contaReceberService.listarTodas()));
        tabelaPagar.setItems(FXCollections.observableArrayList(contaPagarService.listarTodas()));
    }

    private void atualizarResumo() {
        BigDecimal totalReceber = contaReceberService.totalPendente();
        BigDecimal totalPagar = contaPagarService.totalPendente();
        lblTotalReceber.setText("R$ " + totalReceber.stripTrailingZeros().toPlainString());
        lblTotalPagar.setText("R$ " + totalPagar.stripTrailingZeros().toPlainString());
    }

    @FXML
    private void handleCadastrarReceber() {
        try {
            ContaReceber conta = new ContaReceber();
            conta.setCliente(cmbClienteReceber.getValue());
            conta.setDescricao(txtDescricaoReceber.getText());
            conta.setValor(new BigDecimal(txtValorReceber.getText().replace(",", ".")));
            conta.setVencimento(dtVencimentoReceber.getValue());
            conta.setStatus("PENDENTE");

            contaReceberService.cadastrar(conta);
            AlertaUtil.sucesso("Sucesso", "Conta a receber cadastrada!");
            limparReceber();
            carregarTabelas();
            atualizarResumo();
        } catch (BusinessException e) {
            AlertaUtil.erro("Erro", e.getMessage());
        } catch (Exception e) {
            AlertaUtil.erro("Erro", "Dados inválidos: " + e.getMessage());
        }
    }

    @FXML
    private void handleCadastrarPagar() {
        try {
            ContaPagar conta = new ContaPagar();
            conta.setFornecedor(cmbFornecedorPagar.getValue());
            conta.setDescricao(txtDescricaoPagar.getText());
            conta.setValor(new BigDecimal(txtValorPagar.getText().replace(",", ".")));
            conta.setVencimento(dtVencimentoPagar.getValue());
            conta.setStatus("PENDENTE");

            contaPagarService.cadastrar(conta);
            AlertaUtil.sucesso("Sucesso", "Conta a pagar cadastrada!");
            limparPagar();
            carregarTabelas();
            atualizarResumo();
        } catch (BusinessException e) {
            AlertaUtil.erro("Erro", e.getMessage());
        } catch (Exception e) {
            AlertaUtil.erro("Erro", "Dados inválidos: " + e.getMessage());
        }
    }

    private void limparReceber() {
        cmbClienteReceber.setValue(null);
        txtDescricaoReceber.clear();
        txtValorReceber.clear();
        dtVencimentoReceber.setValue(null);
    }

    private void limparPagar() {
        cmbFornecedorPagar.setValue(null);
        txtDescricaoPagar.clear();
        txtValorPagar.clear();
        dtVencimentoPagar.setValue(null);
    }

    @FXML
    private void handleVoltar() {
        NavigacaoUtil.trocarTela(new Stage(),
                "/com/nexuserp/presentation/view/main.fxml", "Menu Principal");
    }
}
