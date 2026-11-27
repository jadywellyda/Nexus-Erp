package com.nexuserp.presentation.controller;

import com.nexuserp.service.ProdutoService;
import com.nexuserp.service.VendaService;
import com.nexuserp.util.NavigacaoUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DashboardController {

    @FXML private Label lblTotalVendasHoje;
    @FXML private Label lblProdutosBaixoEstoque;
    @FXML private Label lblFaturamentoMes;

    private final VendaService vendaService = new VendaService();
    private final ProdutoService produtoService = new ProdutoService();

    @FXML
    public void initialize() {
        LocalDateTime inicioDia = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime fimDia = inicioDia.plusDays(1).minusSeconds(1);

        lblTotalVendasHoje.setText(String.valueOf(
                vendaService.vendasPorPeriodo(inicioDia, fimDia).size()));

        lblProdutosBaixoEstoque.setText(String.valueOf(
                produtoService.listarBaixoEstoque().size()));

        lblFaturamentoMes.setText("R$ 0,00");
    }

    @FXML
    private void handleSair() {
        NavigacaoUtil.trocarTela((Stage) lblTotalVendasHoje.getScene().getWindow(),
                "/com/nexuserp/presentation/view/main.fxml", "Menu Principal");
    }
}
