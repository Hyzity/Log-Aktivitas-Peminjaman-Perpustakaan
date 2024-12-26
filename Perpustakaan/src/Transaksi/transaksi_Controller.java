package Transaksi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class transaksi_Controller {

    @FXML
    private Label jumlahPeminjaman;

    @FXML
    private TextField inputNamaPeminjam;

    @FXML
    private Button menuKategori;

    @FXML
    private TextField inputJudulBuku;

    @FXML
    private DatePicker inputTanggalPinjam;

    @FXML
    private DatePicker inputTanggalKembali;

    @FXML
    private TableView<LogPeminjaman> tableLogPeminjaman;

    @FXML
    private TableColumn<LogPeminjaman, Integer> id_transaksi;

    @FXML
    private TableColumn<LogPeminjaman, Integer> id_buku;

    @FXML
    private TableColumn<LogPeminjaman, String> nama_buku; // Kolom untuk nama buku

    @FXML
    private TableColumn<LogPeminjaman, String> status;

    @FXML
    private TableColumn<LogPeminjaman, String> tanggal_peminjaman;

    @FXML
    private TableColumn<LogPeminjaman, String> tanggal_pengembalian;

    private ObservableList<LogPeminjaman> logList;

    @FXML
    private Button logoutButton;

    private Connection connection;

    private int selectedIdTransaksi; // Menyimpan id_transaksi dari baris yang dipilih

    @FXML
    public void initialize() {
        id_buku.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
        id_transaksi.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
        nama_buku.setCellValueFactory(new PropertyValueFactory<>("namaBuku"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tanggal_peminjaman.setCellValueFactory(new PropertyValueFactory<>("tanggalPeminjaman"));
        tanggal_pengembalian.setCellValueFactory(new PropertyValueFactory<>("tanggalPengembalian"));

        try {
            connection = dbConnection.getDBConnection();
            loadLogPeminjaman();
        } catch (SQLException ex) {
            Logger.getLogger(transaksi_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Listener untuk menangkap id_transaksi dari baris yang dipilih
        tableLogPeminjaman.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedIdTransaksi = newValue.getIdTransaksi(); // Ambil id_transaksi dari baris yang dipilih
                System.out.println("Selected ID Transaksi: " + selectedIdTransaksi);
            }
        });
    }

    public void loadLogPeminjaman() {
        logList = FXCollections.observableArrayList();
        String query = """
            SELECT t.id_buku, t.id_transaksi, b.nama_buku, t.status, t.tanggal_peminjaman, t.tanggal_pengembalian
            FROM transaksi t
            JOIN buku b ON t.id_buku = b.id_buku
            ORDER BY t.tanggal_peminjaman DESC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                logList.add(new LogPeminjaman(
                        rs.getInt("id_buku"),
                        rs.getInt("id_transaksi"),
                        rs.getString("status"),
                        rs.getString("nama_buku"),
                        rs.getString("tanggal_peminjaman"),
                        rs.getString("tanggal_pengembalian")
                ));
            }

            // Jika data kosong, tampilkan log
            if (logList.isEmpty()) {
                System.out.println("Tidak ada data log peminjaman ditemukan.");
            }

            // Set data ke TableView
            tableLogPeminjaman.setItems(logList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        navigateTo("/login/Logintes.fxml");
    }

    private void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuka halaman: " + e.getMessage());
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
    private void handleBerandaAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/admin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) menuKategori.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuka halaman Beranda Admin: " + e.getMessage());
        }
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
            showAlert("Error", "Failed to load Kategori page: " + e.getMessage());
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
            showAlert("Error", "Failed to load Pilih Tambah Pengumuman page: " + e.getMessage());
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
    public void handleReturnBook() {
        if (selectedIdTransaksi == 0) {
            System.out.println("Tidak ada transaksi yang dipilih.");
            return;
        }

        String query = """
        UPDATE transaksi
        SET status = 'Dikembalikan', tanggal_pengembalian = CURRENT_TIMESTAMP
        WHERE id_transaksi = ?
    """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, selectedIdTransaksi);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Transaksi berhasil diperbarui.");
                loadLogPeminjaman(); // Refresh tabel
            } else {
                System.out.println("Gagal memperbarui transaksi.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
