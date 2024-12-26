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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kategori_buku.dbConnection;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class AdminController {

    @FXML
    private Label jumlahUser;
    @FXML
    private Label jumlahKategori;
    @FXML
    private Label jumlahBuku;
    @FXML
    private Label jumlahPemesanan;
    @FXML
    private Button logoutButton;
    @FXML
    private Button menuKategori;
    @FXML
    private Button menuPengumuman;
    @FXML
    private Button menuPemesanan;

    @FXML
    public void initialize() {
        updateStatistics();
    }

    private void updateStatistics() {
        jumlahUser.setText(String.valueOf(getJumlahMember()));
        jumlahKategori.setText(String.valueOf(getJumlahKategori()));
        jumlahBuku.setText(String.valueOf(getJumlahBuku()));
        jumlahPemesanan.setText(String.valueOf(getJumlahPesanan()));
    }

    private int getJumlahMember() {
        String query = "SELECT COUNT(*) FROM account WHERE role = 'anggota'";
        return executeCountQuery(query);
    }

    private int getJumlahKategori() {
        String query = "SELECT COUNT(*) FROM kategori_buku";
        return executeCountQuery(query);
    }

    private int getJumlahBuku() {
        String query = "SELECT COUNT(*) FROM buku";
        return executeCountQuery(query);
    }

    private int getJumlahPesanan() {
        String query = "SELECT COUNT(*) FROM transaksi";
        return executeCountQuery(query);
    }

    private int executeCountQuery(String query) {
        try (Connection conn = dbConnection.getDBConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database Error", "Failed to fetch data: " + e.getMessage());
        }
        return 0;
    }

    @FXML
    private void handleTambahKategori() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kategori_buku/kategori_buku.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) menuKategori.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Failed to load Kategori page: " + e.getMessage());
        }
    }

    @FXML
    private void handleTambahBuku() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Buku/pilih_kategori_buku.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) menuKategori.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Failed to load Pilih Kategori Buku page: " + e.getMessage());
        }
    }

    @FXML
    private void handleTambahPengumuman() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pengumuman/admin_pengumuman.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) menuKategori.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Failed to load Pilih Tambah Pengumuman page: " + e.getMessage());
        }
    }

    @FXML
    private void handleDetailRequest() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Transaksi/admin_peminjaman.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) menuKategori.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Failed to load Pilih Tambah Pengumuman page: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/Logintes.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Failed to logout: " + e.getMessage());
        }
    }

    private void showNotImplementedAlert(String feature) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Feature Not Implemented");
        alert.setHeaderText(null);
        alert.setContentText("The " + feature + " feature has not been implemented yet.");
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
