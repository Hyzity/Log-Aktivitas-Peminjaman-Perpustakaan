/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package kategori_buku;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class Kategori_bukuController {

    @FXML
    private TextField kategoriTextField;
    @FXML
    private TableView<KategoriBuku> kategoriTableView;
    @FXML
    private TableColumn<KategoriBuku, Integer> idKategoriColumn;
    @FXML
    private TableColumn<KategoriBuku, String> namaKategoriColumn;
    @FXML
    private Label jumlahKategori;
    @FXML
    private Button logoutButton;

    private ObservableList<KategoriBuku> kategoriList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idKategoriColumn.setCellValueFactory(new PropertyValueFactory<>("idKategori"));
        namaKategoriColumn.setCellValueFactory(new PropertyValueFactory<>("namaKategori"));

        loadKategoriBuku();
        updateJumlahKategori();
    }

    private void loadKategoriBuku() {
        kategoriList.clear();
        String query = "SELECT * FROM kategori_buku";
        try (Connection conn = dbConnection.getDBConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                kategoriList.add(new KategoriBuku(
                        rs.getInt("id_kategori"),
                        rs.getString("nama_kategori")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memuat data kategori buku: " + e.getMessage());
        }

        kategoriTableView.setItems(kategoriList);
    }

    private void updateJumlahKategori() {
        jumlahKategori.setText(String.valueOf(kategoriList.size()));
    }

    @FXML
    private void handleTambahKategori() {
        String namaKategori = kategoriTextField.getText().trim();
        if (namaKategori.isEmpty()) {
            showAlert("Error", "Nama kategori tidak boleh kosong!");
            return;
        }

        String query = "INSERT INTO kategori_buku (nama_kategori) VALUES (?)";
        try (Connection conn = dbConnection.getDBConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, namaKategori);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                showAlert("Sukses", "Kategori buku berhasil ditambahkan!");
                kategoriTextField.clear();
                loadKategoriBuku();
                updateJumlahKategori();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal menambahkan kategori buku: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditKategori() {
        KategoriBuku selectedKategori = kategoriTableView.getSelectionModel().getSelectedItem();
        if (selectedKategori == null) {
            showAlert("Error", "Pilih kategori yang akan diedit!");
            return;
        }

        String newNamaKategori = kategoriTextField.getText().trim();
        if (newNamaKategori.isEmpty()) {
            showAlert("Error", "Nama kategori baru tidak boleh kosong!");
            return;
        }

        String query = "UPDATE kategori_buku SET nama_kategori = ? WHERE id_kategori = ?";
        try (Connection conn = dbConnection.getDBConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newNamaKategori);
            pstmt.setInt(2, selectedKategori.getIdKategori());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                showAlert("Sukses", "Kategori buku berhasil diupdate!");
                kategoriTextField.clear();
                loadKategoriBuku();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal mengupdate kategori buku: " + e.getMessage());
        }
    }

    @FXML
    private void handleHapusKategori() {
        KategoriBuku selectedKategori = kategoriTableView.getSelectionModel().getSelectedItem();
        if (selectedKategori == null) {
            showAlert("Error", "Pilih kategori yang akan dihapus!");
            return;
        }

        String query = "DELETE FROM kategori_buku WHERE id_kategori = ?";
        try (Connection conn = dbConnection.getDBConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, selectedKategori.getIdKategori());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                showAlert("Sukses", "Kategori buku berhasil dihapus!");
                loadKategoriBuku();
                updateJumlahKategori();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal menghapus kategori buku: " + e.getMessage());
        }
    }

    @FXML
    private void handleBerandaAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/admin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) kategoriTextField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuka halaman Beranda Admin: " + e.getMessage());
        }
    }

    @FXML
    private void handleTambahBuku() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Buku/pilih_kategori_buku.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) kategoriTextField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuka halaman Tambah Buku: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleTambahPengumuman() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pengumuman/admin_pengumuman.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) kategoriTextField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Pilih Tambah Pengumuman page: " + e.getMessage());
        }
    }

    @FXML
    private void handleDetailRequest() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Transaksi/admin_peminjaman.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) kategoriTextField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Pilih Tambah Pengumuman page: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
            showAlert("Error", "Failed to logout: " + e.getMessage());
        }
    }

}
