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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author noelt
 */
public class Detail_pinjamController {

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
    @FXML
    private Button okButton;
    @FXML
    private Button kembaliButton;

    private int idAkun;
    private int kategoriId;

    public void initialize() {
        lblTanggalSekarang.setText(LocalDate.now().toString());
    }

    public void setBuku(Buku buku) {
        lblIdBuku.setText(String.valueOf(buku.getIdBuku()));
        lblNamaBuku.setText(buku.getNamaBuku());
        lblPenulis.setText(buku.getPenulis());
        lblTahunTerbit.setText(String.valueOf(buku.getTahunTerbit()));
    }

    public void setIdAkun(int idAkun) {
        this.idAkun = idAkun;
    }

    public void setKategoriId(int kategoriId) {
        this.kategoriId = kategoriId;
    }

    @FXML
    private void handleOkButton() {
        int idBuku = Integer.parseInt(lblIdBuku.getText());
        String tanggalPeminjaman = lblTanggalSekarang.getText();

        if (insertTransaksi(idBuku, tanggalPeminjaman)) {
            showConfirmationPopup();
        } else {
            showErrorPopup();
        }
    }

    private boolean insertTransaksi(int idBuku, String tanggalPeminjaman) {
        String query = "INSERT INTO transaksi (id_akun, id_buku, status, tanggal_peminjaman) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getDBConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idAkun);
            pstmt.setInt(2, idBuku);
            pstmt.setString(3, "Dipinjam");
            pstmt.setString(4, tanggalPeminjaman);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showConfirmationPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Konfirmasi Peminjaman");
        alert.setHeaderText(null);
        alert.setContentText("Peminjaman berhasil dilakukan");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                closeWindow(); // Menutup jendela setelah pop-up ditutup
            }
        });
    }

    private void showErrorPopup() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Terjadi kesalahan saat melakukan peminjaman");
        alert.showAndWait();
    }

    private void navigateToBookList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailBukudgnGambar.fxml"));
            Parent root = loader.load();

            DetailBukudgnGambarController controller = loader.getController();
            controller.setIdAkun(idAkun);
            controller.setKategoriId(kategoriId);

            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPopup();
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) kembaliButton.getScene().getWindow();
        stage.close();
    }
}

