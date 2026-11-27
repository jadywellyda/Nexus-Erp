package com.nexuserp.presentation.controller;

import com.nexuserp.domain.Produto;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.service.EstoqueService;
import com.nexuserp.util.AlertaUtil;
import com.nexuserp.util.NavigacaoUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class TelaEstoqueController {

    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, Long> colId;
    @FXML private TableColumn<Produto, String> colSku;
    @FXML private TableColumn<Produto, String> colNome;
    @FXML private TableColumn<Produto, Integer> colEstoqueAtual;
    @FXML private TableColumn<Produto, Integer> colEstoqueMinimo;
    @FXML private ComboBox<Produto> cmbProduto;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtReferencia;
    @FXML private Button btnEntrada;
    @FXML private Button btnSaida;
    @FXML private Button btnAtualizar;
    @FXML private Button btnVoltar;

    private final EstoqueService estoqueService = new EstoqueService();
    private ObservableList<Produto> produtosObs = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSku.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEstoqueAtual.setCellValueFactory(new PropertyValueFactory<>("estoqueAtual"));
        colEstoqueMinimo.setCellValueFactory(new PropertyValueFactory<>("estoqueMinimo"));

        carregarTabela();
        carregarComboProdutos();
    }

    private void carregarTabela() {
        List<Produto> produtos = estoqueService.listarProdutos();
        produtosObs = FXCollections.observableArrayList(produtos);
        tabelaProdutos.setItems(produtosObs);
    }

    private void carregarComboProdutos() {
        List<Produto> produtos = estoqueService.listarProdutos();
        cmbProduto.setItems(FXCollections.observableArrayList(produtos));
    }

    @FXML
    private void handleEntrada() {
        try {
            Produto p = cmbProduto.getValue();
            if (p == null) {
                AlertaUtil.aviso("Atenção", "Selecione um produto.");
                return;
            }
            String qtdTexto = txtQuantidade.getText();
            if (qtdTexto == null || qtdTexto.isBlank()) {
                AlertaUtil.aviso("Atenção", "Informe a quantidade.");
                return;
            }
            int quantidade = Integer.parseInt(qtdTexto);
            String referencia = txtReferencia.getText();

            estoqueService.registrarEntrada(p.getId(), quantidade, referencia);
            AlertaUtil.sucesso("Sucesso", "Entrada de estoque registrada!");
            limparCamposMovimento();
            carregarTabela();
        } catch (BusinessException e) {
            AlertaUtil.erro("Erro", e.getMessage());
        } catch (Exception e) {
            AlertaUtil.erro("Erro", e.getMessage());
        }
    }

    @FXML
    private void handleSaida() {
        try {
            Produto p = cmbProduto.getValue();
            if (p == null) {
                AlertaUtil.aviso("Atenção", "Selecione um produto.");
                return;
            }
            String qtdTexto = txtQuantidade.getText();
            if (qtdTexto == null || qtdTexto.isBlank()) {
                AlertaUtil.aviso("Atenção", "Informe a quantidade.");
                return;
            }
            int quantidade = Integer.parseInt(qtdTexto);
            String referencia = txtReferencia.getText();

            estoqueService.registrarSaida(p.getId(), quantidade, referencia);
            AlertaUtil.sucesso("Sucesso", "Saída de estoque registrada!");
            limparCamposMovimento();
            carregarTabela();
        } catch (BusinessException e) {
            AlertaUtil.erro("Erro", e.getMessage());
        } catch (Exception e) {
            AlertaUtil.erro("Erro", e.getMessage());
        }
    }

    @FXML
    private void handleAtualizar() {
        carregarTabela();
        carregarComboProdutos();
    }

    private void limparCamposMovimento() {
        txtQuantidade.clear();
        txtReferencia.clear();
    }

    @FXML
    private void handleVoltar() {
        NavigacaoUtil.trocarTela(new Stage(),
                "/com/nexuserp/presentation/view/main.fxml", "Menu Principal");
    }
}
