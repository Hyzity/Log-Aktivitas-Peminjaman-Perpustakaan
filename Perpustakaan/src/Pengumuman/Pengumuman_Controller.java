/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pengumuman;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.sql.*;
import dbConnection.dbConnection;

/**
 *
 * @author Lenovo
 */
public class Pengumuman_Controller {

    @FXML
    private Label jumlahPengumuman;
    @FXML
    private TextField inputNamaPengumuman;
    @FXML
    private TextField inputDeskripsi;
    @FXML
    private TableView<Pengumuman> tablePengumuman;
    @FXML
    private TableColumn<Pengumuman, String> columnNamaPengumuman;
    @FXML
    private TableColumn<Pengumuman, String> columnDeskripsi;
    @FXML
    private Button logoutButton;
    @FXML
    private Button menuPengumuman;
    @FXML
    private Button menuKategori;

    private ObservableList<Pengumuman> pengumumanList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        columnNamaPengumuman.setCellValueFactory(new PropertyValueFactory<>("judulPengumuman"));
        columnDeskripsi.setCellValueFactory(new PropertyValueFactory<>("isiPengumuman"));

        loadPengumuman();
        updateStatistics();
    }

    private void loadPengumuman() {
        pengumumanList.clear();
        String query = "SELECT * FROM pengumuman";
        try (Connection conn = dbConnection.getDBConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                pengumumanList.add(new Pengumuman(
                        rs.getInt("id_pengumuman"),
                        rs.getInt("id_akun"),
                        rs.getString("judul_pengumuman"),
                        rs.getString("isi_pengumuman")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memuat data pengumuman: " + e.getMessage());
        }

        tablePengumuman.setItems(pengumumanList);
    }

    private void updateStatistics() {
        jumlahPengumuman.setText(String.valueOf(pengumumanList.size()));
    }

    @FXML
    private void handleTambahPengumuman() {
        String judulPengumuman = inputNamaPengumuman.getText().trim();
        String isiPengumuman = inputDeskripsi.getText().trim();

        if (judulPengumuman.isEmpty() || isiPengumuman.isEmpty()) {
            showAlert("Error", "Judul dan isi pengumuman harus diisi!");
            return;
        }

        String query = "INSERT INTO pengumuman (id_akun, judul_pengumuman, isi_pengumuman) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.getDBConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, 999999); 
            pstmt.setString(2, judulPengumuman);
            pstmt.setString(3, isiPengumuman);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert("Sukses", "Pengumuman berhasil ditambahkan!");
                clearFields();
                loadPengumuman();
                updateStatistics();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal menambahkan pengumuman: " + e.getMessage());
        }
    }

    @FXML
    private void handleHapusPengumuman() {
        Pengumuman selectedPengumuman = tablePengumuman.getSelectionModel().getSelectedItem();
        if (selectedPengumuman == null) {
            showAlert("Error", "Pilih pengumuman yang akan dihapus!");
            return;
        }

        String query = "DELETE FROM pengumuman WHERE id_pengumuman = ?";
        try (Connection conn = dbConnection.getDBConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, selectedPengumuman.getIdPengumuman());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                showAlert("Sukses", "Pengumuman berhasil dihapus!");
                loadPengumuman();
                updateStatistics();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal menghapus pengumuman: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditPengumuman() {
        Pengumuman selectedPengumuman = tablePengumuman.getSelectionModel().getSelectedItem();
        if (selectedPengumuman == null) {
            showAlert("Error", "Pilih pengumuman yang akan diedit!");
            return;
        }

        String judulPengumuman = inputNamaPengumuman.getText().trim();
        String isiPengumuman = inputDeskripsi.getText().trim();

        if (judulPengumuman.isEmpty() || isiPengumuman.isEmpty()) {
            showAlert("Error", "Judul dan isi pengumuman harus diisi!");
            return;
        }

        String query = "UPDATE pengumuman SET judul_pengumuman = ?, isi_pengumuman = ? WHERE id_pengumuman = ?";
        try (Connection conn = dbConnection.getDBConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, judulPengumuman);
            pstmt.setString(2, isiPengumuman);
            pstmt.setInt(3, selectedPengumuman.getIdPengumuman());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert("Sukses", "Pengumuman berhasil diupdate!");
                clearFields();
                loadPengumuman();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal mengupdate pengumuman: " + e.getMessage());
        }
    }

    @FXML
    private void handleMenuDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/admin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) menuPengumuman.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuka halaman Dashboard: " + e.getMessage());
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
            showAlert("Error", "Failed to load Pilih Kategori Buku page: " + e.getMessage());
        }
    }

    @FXML
    private void handleMenuKategori() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kategori_buku/kategori_buku.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) menuKategori.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuka halaman Kategori: " + e.getMessage());
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
            showAlert("Error", "Failed to load Pilih Tambah Pengumuman page: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/Logintes.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal logout: " + e.getMessage());
        }
    }

    private void clearFields() {
        inputNamaPengumuman.clear();
        inputDeskripsi.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Inner class to represent Pengumuman
    public static class Pengumuman {

        private final int idPengumuman;
        private final int idAkun;
        private final String judulPengumuman;
        private final String isiPengumuman;

        public Pengumuman(int idPengumuman, int idAkun, String judulPengumuman, String isiPengumuman) {
            this.idPengumuman = idPengumuman;
            this.idAkun = idAkun;
            this.judulPengumuman = judulPengumuman;
            this.isiPengumuman = isiPengumuman;
        }

        public int getIdPengumuman() {
            return idPengumuman;
        }

        public int getIdAkun() {
            return idAkun;
        }

        public String getJudulPengumuman() {
            return judulPengumuman;
        }

        public String getIsiPengumuman() {
            return isiPengumuman;
        }
    }
}
