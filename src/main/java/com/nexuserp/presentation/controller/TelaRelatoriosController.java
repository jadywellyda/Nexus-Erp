package com.nexuserp.presentation.controller;

import com.nexuserp.service.RelatorioService;
import com.nexuserp.util.AlertaUtil;
import com.nexuserp.util.NavigacaoUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TelaRelatoriosController {

    @FXML private DatePicker dtInicio;
    @FXML private DatePicker dtFim;
    @FXML private Button btnGerarPDF;
    @FXML private Button btnVoltar;

    @FXML
    public void initialize() {
        dtInicio.setValue(LocalDate.now().withDayOfMonth(1));
        dtFim.setValue(LocalDate.now());
    }

    @FXML
    private void handleGerarPDF() {
        if (dtInicio.getValue() == null || dtFim.getValue() == null) {
            AlertaUtil.aviso("Atenção", "Selecione as datas de início e fim.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Relatório PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf"));
        fileChooser.setInitialFileName(
                "relatorio_vendas_" + dtInicio.getValue() + "_a_" + dtFim.getValue() + ".pdf");

        File arquivo = fileChooser.showSaveDialog(btnGerarPDF.getScene().getWindow());
        if (arquivo == null) {
            return;
        }

        try {
            LocalDateTime inicio = dtInicio.getValue().atStartOfDay();
            LocalDateTime fim = dtFim.getValue().atTime(LocalTime.MAX);

            RelatorioService relatorioService = new RelatorioService();
            relatorioService.gerarRelatorioVendasPDF(inicio, fim, arquivo.getAbsolutePath());

            AlertaUtil.sucesso("Sucesso", "Relatório gerado com sucesso!\n" + arquivo.getAbsolutePath());
        } catch (Exception e) {
            AlertaUtil.erro("Erro ao gerar PDF", e.getMessage());
        }
    }

    @FXML
    private void handleVoltar() {
        NavigacaoUtil.trocarTela(new Stage(),
                "/com/nexuserp/presentation/view/main.fxml", "Menu Principal");
    }
}
