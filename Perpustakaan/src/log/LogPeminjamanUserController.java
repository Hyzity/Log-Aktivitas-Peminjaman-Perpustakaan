package log;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.MainMenuController;

public class LogPeminjamanUserController implements Initializable {

    @FXML
    private TableView<LogPeminjaman> tableLogPeminjaman;

    @FXML
    private TableColumn<LogPeminjaman, Integer> id_buku;

    @FXML
    private TableColumn<LogPeminjaman, String> nama_buku;

    @FXML
    private TableColumn<LogPeminjaman, String> status;

    @FXML
    private TableColumn<LogPeminjaman, String> tanggal_peminjaman;

    @FXML
    private TableColumn<LogPeminjaman, String> tanggal_pengembalian;

    private ObservableList<LogPeminjaman> logList;

    private Connection connection;

    private int idAkun;

    // Setter untuk menerima ID akun
    public void setIdAkun(int idAkun) {
        this.idAkun = idAkun;
        loadLogPeminjaman(); // Memuat data baru berdasarkan ID akun
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inisialisasi kolom tabel
        id_buku.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
        nama_buku.setCellValueFactory(new PropertyValueFactory<>("namaBuku"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tanggal_peminjaman.setCellValueFactory(new PropertyValueFactory<>("tanggalPeminjaman"));
        tanggal_pengembalian.setCellValueFactory(new PropertyValueFactory<>("tanggalPengembalian"));

        try {
            // Inisialisasi koneksi database
            connection = dbConnection.getDBConnection();
        } catch (SQLException ex) {
            Logger.getLogger(LogPeminjamanUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Metode untuk memuat data log peminjaman
    public void loadLogPeminjaman() {
        if (connection == null || idAkun == 0) {
            // Cek apakah koneksi belum siap atau ID akun belum diatur
            return;
        }

        logList = FXCollections.observableArrayList();
        String query = "SELECT t.id_buku, b.nama_buku, t.status, t.tanggal_peminjaman, t.tanggal_pengembalian " +
                       "FROM transaksi t " +
                       "JOIN buku b ON t.id_buku = b.id_buku " +
                       "WHERE t.id_akun = ?"; // Filter berdasarkan ID akun

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idAkun); // Mengatur parameter ID akun
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    logList.add(new LogPeminjaman(
                            rs.getInt("id_buku"),
                            rs.getString("nama_buku"),
                            rs.getString("status"),
                            rs.getString("tanggal_peminjaman"),
                            rs.getString("tanggal_pengembalian")
                    ));
                }

                // Set data ke tabel
                tableLogPeminjaman.setItems(logList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Button backButton;

    @FXML
    void handleBackToMainPage(ActionEvent event) throws IOException {
        // Load FXML halaman utama
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainMenu.fxml"));
        Parent mainPage = loader.load();

        // Mengambil controller dari halaman utama
        MainMenuController controller = loader.getController();

        // Pass id_akun ke halaman utama
        controller.setIdAkun(this.idAkun);

        // Mendapatkan stage yang aktif dan mengubah scene
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(mainPage);
        stage.setScene(scene);
        stage.show();
    }
}
