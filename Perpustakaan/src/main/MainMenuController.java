/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import perpustakaan.DetailBukudgnGambarController;

/**
 * FXML Controller class
 *
 * @author noelt
 */
public class MainMenuController {

    @FXML
    private TilePane tilePane;

    @FXML
    public void initialize() {
        loadCategories();
    }

    private int idAkun;

    public void setIdAkun(int idAkun) {
        this.idAkun = idAkun;

        // Gunakan idAkun untuk mengambil data pengguna atau menyesuaikan tampilan
        System.out.println("ID Akun yang login: " + idAkun);
    }

    private void loadCategories() {
        String query = "SELECT id_kategori, nama_kategori FROM kategori_buku";
        try (Connection connection = dbConnection.getDBConnection(); PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int idKategori = resultSet.getInt("id_kategori");
                String namaKategori = resultSet.getString("nama_kategori");

                // Membuat card kategori
                VBox card = createCategoryCard(idKategori, namaKategori);
                tilePane.getChildren().add(card);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createCategoryCard(int idKategori, String namaKategori) {
        VBox card = new VBox(10);
        card.setStyle("-fx-padding: 15; -fx-background-color: #f9f9f9; -fx-border-color: #CCCCCC; -fx-border-width: 1;");
        card.setPrefSize(150, 100);

        Label label = new Label(namaKategori);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button button = new Button("Lihat Buku");
        button.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;");
        button.setOnAction(event -> openDetailPage(idKategori, idAkun));

        card.getChildren().addAll(label, button);
        return card;
    }

    private void openDetailPage(int idKategori, int idAkun) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/perpustakaan/DetailBukudgnGambar.fxml"));
            Stage stage = (Stage) tilePane.getScene().getWindow();
            Scene scene = new Scene(loader.load());

            // Mengirimkan idKategori dan idAkun ke controller detail
            DetailBukudgnGambarController controller = loader.getController();
            controller.setKategoriId(idKategori);
            controller.setIdAkun(idAkun);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
