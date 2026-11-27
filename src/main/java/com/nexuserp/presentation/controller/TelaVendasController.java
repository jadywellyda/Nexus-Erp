package com.nexuserp.presentation.controller;

import com.nexuserp.domain.Cliente;
import com.nexuserp.domain.ItemVenda;
import com.nexuserp.domain.Produto;
import com.nexuserp.domain.Usuario;
import com.nexuserp.domain.Venda;
import com.nexuserp.exception.BusinessException;
import com.nexuserp.presentation.dto.VendaItemDTO;
import com.nexuserp.service.ClienteService;
import com.nexuserp.service.ProdutoService;
import com.nexuserp.service.UsuarioService;
import com.nexuserp.service.VendaService;
import com.nexuserp.util.AlertaUtil;
import com.nexuserp.util.NavigacaoUtil;
import com.nexuserp.util.SessaoUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.List;

public class TelaVendasController {

    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<Produto> cmbProduto;
    @FXML private TextField txtQuantidade;
    @FXML private Button btnAdicionar;
    @FXML private TableView<VendaItemDTO> tabelaCarrinho;
    @FXML private TableColumn<VendaItemDTO, String> colProduto;
    @FXML private TableColumn<VendaItemDTO, Integer> colQuantidade;
    @FXML private TableColumn<VendaItemDTO, BigDecimal> colPreco;
    @FXML private TableColumn<VendaItemDTO, BigDecimal> colSubtotal;
    @FXML private Button btnRemoverItem;
    @FXML private Label lblTotal;
    @FXML private Button btnFinalizar;
    @FXML private Button btnVoltar;

    private final ObservableList<VendaItemDTO> carrinho = FXCollections.observableArrayList();
    private final VendaService vendaService = new VendaService();
    private final ClienteService clienteService = new ClienteService();
    private final ProdutoService produtoService = new ProdutoService();
    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    public void initialize() {
        colProduto.setCellValueFactory(new PropertyValueFactory<>("produtoNome"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        carregarClientes();
        carregarProdutos();

        lblTotal.setText("R$ 0,00");
    }

    // Adicionado para binding da coluna nome do produto no DTO
    private void carregarClientes() {
        List<Cliente> clientes = clienteService.listarTodos();
        cmbCliente.setItems(FXCollections.observableArrayList(clientes));
    }

    private void carregarProdutos() {
        List<Produto> produtos = produtoService.listarTodos();
        cmbProduto.setItems(FXCollections.observableArrayList(produtos));
    }

    @FXML
    private void handleAdicionar() {
        Cliente cliente = cmbCliente.getValue();
        Produto produto = cmbProduto.getValue();
        String qtdTexto = txtQuantidade.getText();

        if (cliente == null) {
            AlertaUtil.aviso("Atenção", "Selecione um cliente.");
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

        int quantidade;
        try {
            quantidade = Integer.parseInt(qtdTexto);
            if (quantidade <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            AlertaUtil.erro("Erro", "Quantidade inválida.");
            return;
        }

        if (quantidade > produto.getEstoqueAtual()) {
            AlertaUtil.erro("Estoque insuficiente",
                    "Disponível: " + produto.getEstoqueAtual());
            return;
        }

        // Verifica se já existe no carrinho e soma quantidade
        VendaItemDTO existente = carrinho.stream()
                .filter(i -> i.getProduto().getId().equals(produto.getId()))
                .findFirst()
                .orElse(null);

        if (existente != null) {
            int novaQtd = existente.getQuantidade() + quantidade;
            if (novaQtd > produto.getEstoqueAtual()) {
                AlertaUtil.erro("Estoque insuficiente",
                        "Quantidade total excede o estoque disponível (" + produto.getEstoqueAtual() + ").");
                return;
            }
            existente.setQuantidade(novaQtd);
        } else {
            VendaItemDTO item = new VendaItemDTO(produto, quantidade, produto.getPrecoVenda());
            carrinho.add(item);
        }

        tabelaCarrinho.setItems(carrinho);
        atualizarTotal();
        txtQuantidade.clear();
    }

    @FXML
    private void handleRemoverItem() {
        VendaItemDTO selecionado = tabelaCarrinho.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            carrinho.remove(selecionado);
            atualizarTotal();
        }
    }

    private void atualizarTotal() {
        BigDecimal total = carrinho.stream()
                .map(VendaItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        lblTotal.setText("R$ " + total.stripTrailingZeros().toPlainString());
    }

    @FXML
    private void handleFinalizar() {
        if (carrinho.isEmpty()) {
            AlertaUtil.aviso("Atenção", "Adicione itens ao carrinho antes de finalizar.");
            return;
        }

        Cliente cliente = cmbCliente.getValue();
        if (cliente == null) {
            AlertaUtil.aviso("Atenção", "Selecione um cliente.");
            return;
        }

        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();
        if (usuarioLogado == null) {
            AlertaUtil.erro("Erro", "Usuário não logado.");
            return;
        }

        Venda venda = new Venda();
        venda.setNumero("V" + System.currentTimeMillis());
        venda.setCliente(cliente);
        venda.setUsuario(usuarioLogado);
        venda.setStatus("FINALIZADA");

        List<ItemVenda> itens = carrinho.stream().map(item -> {
            ItemVenda iv = new ItemVenda();
            iv.setProduto(item.getProduto());
            iv.setQuantidade(item.getQuantidade());
            iv.setPrecoUnitario(item.getPrecoUnitario());
            iv.setSubtotal(item.getSubtotal());
            return iv;
        }).toList();

        try {
            vendaService.finalizarVenda(venda, itens);
            AlertaUtil.sucesso("Sucesso", "Venda finalizada com sucesso!");
            carrinho.clear();
            tabelaCarrinho.setItems(carrinho);
            lblTotal.setText("R$ 0,00");
            cmbCliente.setValue(null);
        } catch (BusinessException e) {
            AlertaUtil.erro("Erro ao finalizar venda", e.getMessage());
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
