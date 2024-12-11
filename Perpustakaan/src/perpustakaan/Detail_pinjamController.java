/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package perpustakaan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * FXML Controller class
 *
 * @author noelt
 */
public class Detail_pinjamController implements Initializable {

    @FXML
    private Label lblIdBuku;
    @FXML
    private Label lblNamaBuku;
    @FXML
    private Label lblPenulis;
    @FXML
    private Label lblTahunTerbit;
    @FXML
    private Label lblTanggalSekarang;

    private int idAkun;

    public void setIdAkun(int idAkun) {
        this.idAkun = idAkun;
        System.out.println("ID Akun yang diterima: " + idAkun);
    }

    public void setBuku(Buku buku) {
        lblIdBuku.setText(String.valueOf(buku.getIdBuku()));
        lblNamaBuku.setText(buku.getNamaBuku());
        lblPenulis.setText(buku.getPenulis());
        lblTahunTerbit.setText(String.valueOf(buku.getTahunTerbit()));
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) lblIdBuku.getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Format tanggal
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        // Ambil tanggal saat ini
        String tanggalSekarang = LocalDate.now().format(formatter);
        // Set nilai ke Label
        lblTanggalSekarang.setText(tanggalSekarang);
    }

    @FXML
    private void handleBorrowBook(int idBuku) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/perpustakaan/detail_pinjam.fxml"));
            Parent root = loader.load();

            // Kirimkan id_akun ke Detail_pinjamController
            Detail_pinjamController controller = loader.getController();
            controller.setIdAkun(this.idAkun); // Pass id_akun yang sudah diterima sebelumnya

            // Tampilkan popup
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddTransaction() {
        // Ambil data dari Label
        int idBuku = Integer.parseInt(lblIdBuku.getText());
        String status = "Dipinjam"; // Status default
        LocalDate tanggalPeminjaman = LocalDate.now();

        // SQL untuk memasukkan data
        String query = "INSERT INTO transaksi (id_akun, id_buku, id_transaksi, STATUS, tanggal_peminjaman, tanggal_pengembalian) "
                + "VALUES (?, ?, ?, ?, ?, NULL)";

        try (Connection connection = dbConnection.getDBConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set parameter
            stmt.setInt(1, idAkun);
            stmt.setInt(2, idBuku);
            stmt.setInt(3, generateTransactionId(connection)); // Metode untuk membuat ID transaksi unik
            stmt.setString(4, status);
            stmt.setObject(5, tanggalPeminjaman); // LocalDate otomatis dikonversi ke datetime

            // Eksekusi perintah SQL
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data berhasil dimasukkan ke tabel transaksi.");
            }

            // Tutup jendela setelah berhasil
            closeWindow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// Metode untuk menghasilkan ID transaksi unik
    private int generateTransactionId(Connection connection) {
        // Contoh: Mengambil nilai maksimal ID transaksi dari database dan menambahkannya
        String query = "SELECT MAX(id_transaksi) AS max_id FROM transaksi";
        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // Default jika tabel kosong
    }

}
