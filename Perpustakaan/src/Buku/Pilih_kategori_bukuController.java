/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Buku;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;


/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class Pilih_kategori_bukuController {

    @FXML
    private FlowPane kategoriContainer;

    @FXML
    public void initialize() {
        loadKategori();
    }

    private void loadKategori() {
        String query = "SELECT * FROM kategori_buku";
        try (Connection conn = dbConnection.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idKategori = rs.getInt("id_kategori");
                String namaKategori = rs.getString("nama_kategori");
                
                Button categoryButton = new Button(namaKategori);
                categoryButton.setStyle("-fx-pref-width: 150px; -fx-pref-height: 100px;");
                categoryButton.setOnAction(e -> openTambahBukuForm(idKategori, namaKategori));
                
                kategoriContainer.getChildren().add(categoryButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // You might want to show an alert here
        }
    }

    private void openTambahBukuForm(int idKategori, String namaKategori) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Buku/tambah_buku.fxml"));
            Parent root = loader.load();
            
            Tambah_bukuController controller = loader.getController();
            controller.initData(idKategori, namaKategori);
            
            Stage stage = (Stage) kategoriContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tambah Buku - " + namaKategori);
        } catch (IOException e) {
            e.printStackTrace();
            // You might want to show an alert here
        }
    }
}

