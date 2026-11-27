package com.nexuserp.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigacaoUtil {

    public static void trocarTela(Stage stage, String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigacaoUtil.class.getResource(fxml));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 1024, 768));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            AlertaUtil.erro("Erro de navegação", "Não foi possível carregar a tela: " + fxml);
        }
    }
}
