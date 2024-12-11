package perpustakaan;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import main.MainMenuController;
import java.io.IOException;

/**
 * FXML Controller class
 *
 * @author noelt
 */
public class DetailBukudgnGambarController {

    @FXML
    private Label lblKategori;

    @FXML
    private TextField searchField;

    @FXML
    private TilePane tilePane;

    private int kategoriId;

    private Connection connection;
    private int idAkun;

    public void setIdAkun(int idAkun) {
        this.idAkun = idAkun;

        // Opsional: Lakukan sesuatu dengan idAkun, misalnya memuat data pengguna
        System.out.println("ID Akun diterima: " + idAkun);
    }

    /**
     * Inisialisasi controller dan mengatur listener pada field pencarian.
     */
    public void initialize() {
        connectToDatabase();

//        searchField.textProperty().addListener((observable, oldValue, newValue) -> loadBooks(newValue));
    }

    public void setKategoriId(int kategoriId) {
        this.kategoriId = kategoriId;

        setJudulKategori(kategoriId);
        // Misalnya, kita ingin memuat buku berdasarkan kategori yang diterima
        loadBooks(kategoriId);

    }

    /**
     * Membuka koneksi ke database MySQL.
     */
    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/perpustakaan", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Memuat buku berdasarkan keyword pencarian.
     *
     * @param keyword Kata kunci pencarian
     */
//    private void loadBooks(int keyword) {
//        tilePane.getChildren().clear();
//
//        String query = "SELECT * FROM buku WHERE nama_buku LIKE ?";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, "%" + keyword + "%");
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    int idBuku = rs.getInt("id_buku");
//                    String namaBuku = rs.getString("nama_buku");
//                    String pathGambar = rs.getString("path_gambar");
//                    tilePane.getChildren().add(createBookCard(idBuku, namaBuku, pathGambar));
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    private void loadBooks(int kategoriId) {
        tilePane.getChildren().clear();

        String query = "SELECT * "
                + "FROM Buku b "
                + "JOIN kategori_buku k ON b.id_kategori = k.id_kategori "
                + "WHERE b.id_kategori = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, kategoriId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idBuku = rs.getInt("id_buku");
                    String namaBuku = rs.getString("nama_buku");
                    String pathGambar = rs.getString("path_gambar");
                    String namaKategori = rs.getString("nama_kategori");
                    lblKategori.setText(namaKategori);

                    tilePane.getChildren().add(createBookCard(idBuku, namaBuku, pathGambar));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setJudulKategori(int id_kategori) {

        String query = ""
                + "SELECT k.nama_kategori "
                + "FROM buku "
                + "b JOIN kategori_buku k ON b.id_kategori = k.id_kategori "
                + "WHERE b.id_kategori = ?";
        try (Connection stmt = dbConnection.getDBConnection(); PreparedStatement preparedStatement = stmt.prepareStatement(query)) {

            preparedStatement.setInt(1, id_kategori);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String namaKategori = resultSet.getString("nama_kategori");
                    lblKategori.setText(namaKategori);
                } else {
                    lblKategori.setText("Kategori Tidak Ditemukan");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lblKategori.setText("Kesalahan: Tidak Bisa Memuat Kategori");
        }
    }

    /**
     * Membuat kartu buku untuk ditampilkan.
     *
     * @param idBuku ID Buku
     * @param namaBuku Nama Buku
     * @param pathGambar Path gambar dari database
     * @return VBox kartu buku
     */
    private VBox createBookCard(int idBuku, String namaBuku, String pathGambar) {
        VBox card = new VBox(3);
        card.setStyle("-fx-padding: 5; -fx-background-color: #f9f9f9; -fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-alignment: center;");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        try {
            imageView.setImage(new Image(getClass().getResource(pathGambar).toExternalForm()));
        } catch (NullPointerException e) {
            imageView.setImage(new Image(getClass().getResource("/resources/default.jpg").toExternalForm()));
        }

        Label titleLabel = new Label(namaBuku);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;-fx-wrap-text: true; -fx-alignment: center;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(140);

        Button borrowButton = new Button("Pinjam");
        borrowButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        borrowButton.setOnAction((ActionEvent e) -> handleBorrowBook(idBuku, idAkun)); // Pass id_akun

        card.getChildren().addAll(imageView, titleLabel, borrowButton);
        return card;
    }

    private void handleBorrowBook(int idBuku, int idAkun) {
        try {
            // Muat file FXML untuk popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/perpustakaan/detail_pinjam.fxml"));
            Parent root = loader.load();

            // Ambil controller detail_pinjam.fxml
            Detail_pinjamController controller = loader.getController();

            // Ambil data buku dari database
            Buku buku = getBukuById(idBuku);
            if (buku != null) {
                // Kirim data buku dan id_akun ke controller
                controller.setBuku(buku);
                controller.setIdAkun(idAkun);

                // Buat stage baru untuk popup
                Stage stage = new Stage();
                stage.setTitle("Detail Buku");
                stage.setScene(new Scene(root));
                stage.initOwner(tilePane.getScene().getWindow()); // Set pemilik stage utama
                stage.show();
            } else {
                System.out.println("Buku tidak ditemukan.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// Metode untuk mengambil data buku dari database
    private Buku getBukuById(int idBuku) {
        String query = "SELECT id_buku, nama_buku, penulis, tahun_terbit FROM buku WHERE id_buku = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idBuku);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Buku(
                            rs.getInt("id_buku"),
                            rs.getString("nama_buku"),
                            rs.getString("penulis"),
                            rs.getInt("tahun_terbit")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
