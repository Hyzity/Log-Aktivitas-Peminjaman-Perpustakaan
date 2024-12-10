/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class AdminController{

@FXML
    private void handleTambahKategori() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kategori_buku/kategori_buku.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Manajemen Kategori Buku");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // You might want to show an alert here
        }
    }

    @FXML
    private void handleTambahBuku() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Buku/pilih_kategori_buku.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Pilih Kategori Buku");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Gagal membuka halaman Pilih Kategori Buku: " + e.getMessage());
        }
    }

    @FXML
    private void handleDetailRequest() {
        showNotImplementedAlert("Detail Request Pinjam Buku");
    }

    private void showNotImplementedAlert(String feature) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Fitur Belum Diimplementasikan");
        alert.setHeaderText(null);
        alert.setContentText("Fitur " + feature + " belum diimplementasikan.");
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

