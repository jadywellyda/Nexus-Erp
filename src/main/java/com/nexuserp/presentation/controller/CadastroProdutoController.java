package com.nexuserp.presentation.controller;

import com.nexuserp.domain.Categoria;
import com.nexuserp.domain.Produto;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.service.CategoriaService;
import com.nexuserp.service.ProdutoService;
import com.nexuserp.util.AlertaUtil;
import com.nexuserp.util.NavigacaoUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.List;

public class CadastroProdutoController {

    @FXML private TextField txtSku;
    @FXML private TextField txtNome;
    @FXML private TextField txtDescricao;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private TextField txtPrecoCusto;
    @FXML private TextField txtPrecoVenda;
    @FXML private TextField txtEstoqueMinimo;
    @FXML private TextField txtEstoqueAtual;
    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, Long> colId;
    @FXML private TableColumn<Produto, String> colSku;
    @FXML private TableColumn<Produto, String> colNome;
    @FXML private TableColumn<Produto, BigDecimal> colPrecoVenda;
    @FXML private TableColumn<Produto, Integer> colEstoque;
    @FXML private Button btnSalvar;
    @FXML private Button btnVoltar;

    private final ProdutoService produtoService = new ProdutoService();
    private final CategoriaService categoriaService = new CategoriaService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSku.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPrecoVenda.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("estoqueAtual"));

        carregarCategorias();
        carregarTabela();
    }

    private void carregarCategorias() {
        List<Categoria> categorias = categoriaService.listarTodas();
        ObservableList<Categoria> lista = FXCollections.observableArrayList(categorias);
        cmbCategoria.setItems(lista);
    }

    private void carregarTabela() {
        tabelaProdutos.setItems(FXCollections.observableArrayList(
                produtoRepositoryListar()));
    }

    private List<Produto> produtoRepositoryListar() {
        try {
            return new com.nexuserp.repository.ProdutoRepository().listarTodos();
        } catch (Exception e) {
            return List.of();
        }
    }

    @FXML
    private void handleSalvar() {
        try {
            Produto p = new Produto();
            p.setSku(txtSku.getText());
            p.setNome(txtNome.getText());
            p.setDescricao(txtDescricao.getText());
            p.setCategoria(cmbCategoria.getValue());
            p.setPrecoCusto(new BigDecimal(txtPrecoCusto.getText().replace(",", ".")));
            p.setPrecoVenda(new BigDecimal(txtPrecoVenda.getText().replace(",", ".")));
            p.setEstoqueMinimo(Integer.parseInt(txtEstoqueMinimo.getText()));
            p.setEstoqueAtual(Integer.parseInt(txtEstoqueAtual.getText()));
            p.setAtivo(true);

            produtoService.cadastrar(p);
            AlertaUtil.sucesso("Sucesso", "Produto cadastrado com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (BusinessException e) {
            AlertaUtil.erro("Erro de validação", e.getMessage());
        } catch (Exception e) {
            AlertaUtil.erro("Erro", "Dados inválidos: " + e.getMessage());
        }
    }

    private void limparCampos() {
        txtSku.clear();
        txtNome.clear();
        txtDescricao.clear();
        txtPrecoCusto.clear();
        txtPrecoVenda.clear();
        txtEstoqueMinimo.clear();
        txtEstoqueAtual.clear();
        cmbCategoria.setValue(null);
    }

    @FXML
    private void handleVoltar() {
        NavigacaoUtil.trocarTela((Stage) btnSalvar.getScene().getWindow(),
                "/com/nexuserp/presentation/view/main.fxml", "Menu Principal");
    }
}
