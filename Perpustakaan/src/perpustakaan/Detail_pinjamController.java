/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package perpustakaan;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

}
