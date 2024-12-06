/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package perpustakaan;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author noelt
 */
public class DetailBukuController implements Initializable {

    @FXML
    private TableView<Buku> tbvBuku;
    @FXML
    private TableColumn<Buku, Integer> id_buku;
    @FXML
    private TableColumn<Buku, String> nama_buku;
    @FXML
    private TableColumn<Buku, String> penulis;
    @FXML
    private TableColumn<Buku, Integer> tahun_terbit;
    @FXML
    private Label lblJudulKategori;
    @FXML
    private Button btnPinjam;

    private ObservableList<Buku> data = FXCollections.observableArrayList();

    private Buku selectedBuku;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id_buku.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
        nama_buku.setCellValueFactory(new PropertyValueFactory<>("namaBuku"));
        penulis.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        tahun_terbit.setCellValueFactory(new PropertyValueFactory<>("tahunTerbit"));

        String id_kategori = "2"; // ==================
                                  // Value ini berdasarkan pass value dari halaman sebelumnya (Dari halaman Kategori)
                                  // ==================
        setJudulKategori(id_kategori);

        loadDataFromDatabase(id_kategori);

        tbvBuku.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedBuku = newValue;
        });
    }

    private void loadDataFromDatabase(String id_kategori) {
        String query = "SELECT b.id_buku, b.nama_buku, b.penulis, b.tahun_terbit "
                + "FROM Buku b "
                + "JOIN kategori_buku k ON b.id_kategori = k.id_kategori "
                + "WHERE b.id_kategori = ?";
        data.clear(); // Bersihkan data lama
        try (Connection connection = dbConnection.getDBConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameter id_kategori
            preparedStatement.setString(1, id_kategori);

            // Eksekusi query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    data.add(new Buku(
                            resultSet.getInt("id_buku"),
                            resultSet.getString("nama_buku"),
                            resultSet.getString("penulis"),
                            resultSet.getInt("tahun_terbit")
                    ));
                }
            }

            // Set data ke TableView
            tbvBuku.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setJudulKategori(String id_kategori) {
        String query = ""
                + "SELECT k.nama_kategori "
                + "FROM buku "
                + "b JOIN kategori_buku k ON b.id_kategori = k.id_kategori "
                + "WHERE b.id_kategori = ?";
        try (Connection connection = dbConnection.getDBConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, id_kategori);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String namaKategori = resultSet.getString("nama_kategori");
                    lblJudulKategori.setText(namaKategori);
                } else {
                    lblJudulKategori.setText("Kategori Tidak Ditemukan");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lblJudulKategori.setText("Kesalahan: Tidak Bisa Memuat Kategori");
        }
    }

    @FXML
    private void handlePinjam(ActionEvent event) {
        if (selectedBuku == null) {
            System.out.println("Silakan pilih buku terlebih dahulu.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detail_pinjam.fxml"));
            Parent root = loader.load();

            Detail_pinjamController controller = loader.getController();
            controller.setBuku(selectedBuku);

            Stage stage = new Stage();
            stage.setTitle("Detail Pinjaman");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
