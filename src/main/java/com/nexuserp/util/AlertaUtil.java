package com.nexuserp.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertaUtil {

    public static void sucesso(String titulo, String mensagem) {
        mostrar(AlertType.INFORMATION, titulo, mensagem);
    }

    public static void erro(String titulo, String mensagem) {
        mostrar(AlertType.ERROR, titulo, mensagem);
    }

    public static void aviso(String titulo, String mensagem) {
        mostrar(AlertType.WARNING, titulo, mensagem);
    }

    private static void mostrar(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
