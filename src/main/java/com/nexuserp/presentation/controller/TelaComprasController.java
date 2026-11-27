package com.nexuserp.presentation.controller;

import com.nexuserp.domain.Fornecedor;
import com.nexuserp.domain.ItemCompra;
import com.nexuserp.domain.Produto;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.presentation.dto.ItemCompraDTO;
import com.nexuserp.service.CompraService;
import com.nexuserp.service.FornecedorService;
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

public class TelaComprasController {

    @FXML private ComboBox<Fornecedor> cmbFornecedor;
    @FXML private ComboBox<Produto> cmbProduto;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtPrecoCusto;
    @FXML private Button btnAdicionar;
    @FXML private TableView<ItemCompraDTO> tabelaCarrinho;
    @FXML private TableColumn<ItemCompraDTO, String> colProduto;
    @FXML private TableColumn<ItemCompraDTO, Integer> colQuantidade;
    @FXML private TableColumn<ItemCompraDTO, BigDecimal> colPreco;
    @FXML private TableColumn<ItemCompraDTO, BigDecimal> colSubtotal;
    @FXML private Button btnRemoverItem;
    @FXML private Label lblTotal;
    @FXML private Button btnFinalizar;
    @FXML private Button btnVoltar;

    private final ObservableList<ItemCompraDTO> carrinho = FXCollections.observableArrayList();
    private final CompraService compraService = new CompraService();
    private final FornecedorService fornecedorService = new FornecedorService();
    private final ProdutoService produtoService = new ProdutoService();

    @FXML
    public void initialize() {
        colProduto.setCellValueFactory(new PropertyValueFactory<>("produtoNome"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        carregarFornecedores();
        carregarProdutos();
        lblTotal.setText("R$ 0,00");
    }

    private void carregarFornecedores() {
        List<Fornecedor> fornecedores = fornecedorService.listarTodos();
        cmbFornecedor.setItems(FXCollections.observableArrayList(fornecedores));
    }

    private void carregarProdutos() {
        List<Produto> produtos = produtoService.listarTodos();
        cmbProduto.setItems(FXCollections.observableArrayList(produtos));
    }

    @FXML
    private void handleAdicionar() {
        Fornecedor fornecedor = cmbFornecedor.getValue();
        Produto produto = cmbProduto.getValue();
        String qtdTexto = txtQuantidade.getText();
        String precoTexto = txtPrecoCusto.getText();

        if (fornecedor == null) {
            AlertaUtil.aviso("Atenção", "Selecione um fornecedor.");
            return;
        }
        if (produto == null) {
            AlertaUtil.aviso("Atenção", "Selecione um produto.");
            return;
        }
        if (qtdTexto == null || qtdTexto.isBlank()) {
            AlertaUtil.aviso("Atenção", "Informe a quantidade.");
            return;
        }
        if (precoTexto == null || precoTexto.isBlank()) {
            AlertaUtil.aviso("Atenção", "Informe o preço de custo.");
            return;
        }

        int quantidade;
        BigDecimal preco;
        try {
            quantidade = Integer.parseInt(qtdTexto);
            if (quantidade <= 0) throw new NumberFormatException();
            preco = new BigDecimal(precoTexto.replace(",", "."));
            if (preco.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            AlertaUtil.erro("Erro", "Quantidade ou preço inválidos.");
            return;
        }

        ItemCompraDTO existente = carrinho.stream()
                .filter(i -> i.getProduto().getId().equals(produto.getId()))
                .findFirst()
                .orElse(null);

        if (existente != null) {
            existente.setQuantidade(existente.getQuantidade() + quantidade);
        } else {
            carrinho.add(new ItemCompraDTO(produto, quantidade, preco));
        }

        tabelaCarrinho.setItems(carrinho);
        atualizarTotal();
        txtQuantidade.clear();
        txtPrecoCusto.clear();
    }

    @FXML
    private void handleRemoverItem() {
        ItemCompraDTO selecionado = tabelaCarrinho.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            carrinho.remove(selecionado);
            atualizarTotal();
        }
    }

    private void atualizarTotal() {
        BigDecimal total = carrinho.stream()
                .map(ItemCompraDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        lblTotal.setText("R$ " + total.stripTrailingZeros().toPlainString());
    }

    @FXML
    private void handleFinalizar() {
        if (carrinho.isEmpty()) {
            AlertaUtil.aviso("Atenção", "Adicione itens ao carrinho antes de finalizar.");
            return;
        }

        Fornecedor fornecedor = cmbFornecedor.getValue();
        if (fornecedor == null) {
            AlertaUtil.aviso("Atenção", "Selecione um fornecedor.");
            return;
        }

        try {
            com.nexuserp.domain.Compra compra = new com.nexuserp.domain.Compra();
            compra.setNumero("C" + System.currentTimeMillis());
            compra.setFornecedor(fornecedor);
            compra.setStatus("FINALIZADA");

            List<ItemCompra> itens = carrinho.stream().map(item -> {
                ItemCompra ic = new ItemCompra();
                ic.setProduto(item.getProduto());
                ic.setQuantidade(item.getQuantidade());
                ic.setPrecoUnitario(item.getPrecoUnitario());
                ic.setSubtotal(item.getSubtotal());
                return ic;
            }).toList();

            compraService.finalizarCompra(compra, itens);
            AlertaUtil.sucesso("Sucesso", "Compra finalizada e estoque atualizado!");
            carrinho.clear();
            tabelaCarrinho.setItems(carrinho);
            lblTotal.setText("R$ 0,00");
            cmbFornecedor.setValue(null);
        } catch (BusinessException e) {
            AlertaUtil.erro("Erro ao finalizar compra", e.getMessage());
        } catch (Exception e) {
            AlertaUtil.erro("Erro", e.getMessage());
        }
    }

    @FXML
    private void handleVoltar() {
        NavigacaoUtil.trocarTela(new Stage(),
                "/com/nexuserp/presentation/view/main.fxml", "Menu Principal");
    }
}
