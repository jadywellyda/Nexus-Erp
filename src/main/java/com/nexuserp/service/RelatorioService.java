package com.nexuserp.service;

import com.nexuserp.domain.Venda;
import com.nexuserp.repository.VendaRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.math.BigDecimal;

public class RelatorioService {

    private static final String FONTE_NORMAL = "C:\\Windows\\Fonts\\arial.ttf";
    private static final String FONTE_NEGRITO = "C:\\Windows\\Fonts\\arialbd.ttf";

    private final VendaRepository vendaRepository = new VendaRepository();

    public void gerarRelatorioVendasPDF(LocalDateTime inicio, LocalDateTime fim, String caminhoArquivo) {
        List<Venda> vendas = vendaRepository.findByPeriodo(inicio, fim);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;

            PDType0Font fonteTitulo = PDType0Font.load(document, new File(FONTE_NEGRITO));
            PDType0Font fonteNormal = PDType0Font.load(document, new File(FONTE_NORMAL));
            PDType0Font fonteNegrito = PDType0Font.load(document, new File(FONTE_NEGRITO));

            // Título
            contentStream.beginText();
            contentStream.setFont(fonteTitulo, 18);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Relatório de Vendas");
            contentStream.endText();
            yPosition -= 30;

            // Período
            contentStream.beginText();
            contentStream.setFont(fonteNormal, 12);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Período: "
                    + inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    + " até "
                    + fim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            contentStream.endText();
            yPosition -= 20;

            // Gerado em
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Gerado em: "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            contentStream.endText();
            yPosition -= 30;

            // Cabeçalho da tabela
            float[] colWidths = {80, 150, 110, 110, 80};
            float x = margin;
            contentStream.beginText();
            contentStream.setFont(fonteNegrito, 11);
            String[] headers = {"Nº Venda", "Cliente", "Data", "Usuário", "Total"};
            for (int i = 0; i < headers.length; i++) {
                contentStream.newLineAtOffset(x, yPosition);
                contentStream.showText(headers[i]);
                contentStream.newLineAtOffset(-x, 0);
                x += colWidths[i];
            }
            contentStream.endText();
            yPosition -= 15;

            // Linha divisória
            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(margin + tableWidth, yPosition);
            contentStream.stroke();
            yPosition -= 15;

            BigDecimal faturamento = BigDecimal.ZERO;

            // Linhas de dados
            for (Venda v : vendas) {
                x = margin;
                contentStream.beginText();
                contentStream.setFont(fonteNormal, 11);

                String[] valores = {
                        v.getNumero() != null ? v.getNumero() : "-",
                        v.getCliente() != null ? v.getCliente().getNomeRazaoSocial() : "-",
                        v.getDataVenda() != null
                                ? v.getDataVenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                                : "-",
                        v.getUsuario() != null ? v.getUsuario().getNome() : "-",
                        v.getValorTotal() != null ? "R$ " + v.getValorTotal() : "R$ 0,00"
                };

                for (int i = 0; i < valores.length; i++) {
                    contentStream.newLineAtOffset(x, yPosition);
                    contentStream.showText(valores[i]);
                    contentStream.newLineAtOffset(-x, 0);
                    x += colWidths[i];
                }
                contentStream.endText();
                yPosition -= 18;

                if (v.getValorTotal() != null) {
                    faturamento = faturamento.add(v.getValorTotal());
                }

                // Quebra de página se necessário
                if (yPosition < margin + 60) {
                    contentStream.endText();
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = yStart;
                }
            }

            yPosition -= 10;

            // Total
            contentStream.beginText();
            contentStream.setFont(fonteNegrito, 12);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Faturamento total no período: R$ "
                    + faturamento.stripTrailingZeros().toPlainString());
            contentStream.endText();

            contentStream.close();

            document.save(new File(caminhoArquivo));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar PDF: " + e.getMessage(), e);
        }
    }
}
