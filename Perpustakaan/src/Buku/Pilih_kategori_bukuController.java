/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Buku;

import java.io.File;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class Pilih_kategori_bukuController {

    @FXML
    private FlowPane kategoriContainer;
    @FXML
    private Button logoutButton;
    @FXML
    private Button menuKategori;
    @FXML
    private Button menuBuku;
    @FXML
    private Button menuPeminjaman;
    @FXML
    private AnchorPane centerAnchorPane;
    @FXML
    private Label headerLabel;

    private int selectedKategoriId;
    private String selectedKategoriNama;

    @FXML
    public void initialize() {
        loadKategori();
    }

    private void loadKategori() {
        String query = "SELECT * FROM kategori_buku";
        try (Connection conn = dbConnection.getDBConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idKategori = rs.getInt("id_kategori");
                String namaKategori = rs.getString("nama_kategori");

                Button categoryButton = createCategoryButton(idKategori, namaKategori);
                kategoriContainer.getChildren().add(categoryButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memuat kategori buku: " + e.getMessage());
        }
    }

    private Button createCategoryButton(int idKategori, String namaKategori) {
        Button categoryButton = new Button(namaKategori);
        categoryButton.setStyle("-fx-pref-width: 150px; -fx-pref-height: 100px; -fx-font-size: 14px; -fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 1px;");
        categoryButton.setOnAction(e -> showBukuForm(idKategori, namaKategori));
        return categoryButton;
    }

    private void showBukuForm(int idKategori, String namaKategori) {
        selectedKategoriId = idKategori;
        selectedKategoriNama = namaKategori;
        headerLabel.setText("Tambah Buku - " + namaKategori);

        centerAnchorPane.getChildren().clear();

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idKategoriField = new TextField(String.valueOf(idKategori));
        idKategoriField.setEditable(false);
        TextField namaBukuField = new TextField();
        TextField penulisField = new TextField();
        TextField tahunTerbitField = new TextField();
        ImageView bookImageView = new ImageView();
        bookImageView.setFitHeight(150);
        bookImageView.setFitWidth(200);
        bookImageView.setPreserveRatio(true);
        Label selectedImageLabel = new Label("Tidak ada gambar dipilih");

        grid.add(new Label("ID Kategori:"), 0, 0);
        grid.add(idKategoriField, 1, 0);
        grid.add(new Label("Nama Buku:"), 0, 1);
        grid.add(namaBukuField, 1, 1);
        grid.add(new Label("Penulis:"), 0, 2);
        grid.add(penulisField, 1, 2);
        grid.add(new Label("Tahun Terbit:"), 0, 3);
        grid.add(tahunTerbitField, 1, 3);
        grid.add(new Label("Gambar:"), 0, 4);

        HBox imageBox = new HBox(10);
        Button pilihGambarButton = new Button("Pilih Gambar");
        pilihGambarButton.setOnAction(e -> handlePilihGambar(selectedImageLabel, bookImageView));
        imageBox.getChildren().addAll(pilihGambarButton, selectedImageLabel);
        grid.add(imageBox, 1, 4);
        grid.add(bookImageView, 1, 5);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        Button tambahButton = new Button("Tambah");
        Button editButton = new Button("Edit");
        Button hapusButton = new Button("Hapus");
        Button batalButton = new Button("Batal");
        buttonBox.getChildren().addAll(tambahButton, editButton, hapusButton, batalButton);

        TableView<Buku> bukuTableView = new TableView<>();
        TableColumn<Buku, Integer> idBukuColumn = new TableColumn<>("ID Buku");
        TableColumn<Buku, String> namaBukuColumn = new TableColumn<>("Nama Buku");
        TableColumn<Buku, String> penulisColumn = new TableColumn<>("Penulis");
        TableColumn<Buku, Integer> tahunTerbitColumn = new TableColumn<>("Tahun Terbit");

        idBukuColumn.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
        namaBukuColumn.setCellValueFactory(new PropertyValueFactory<>("namaBuku"));
        penulisColumn.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        tahunTerbitColumn.setCellValueFactory(new PropertyValueFactory<>("tahunTerbit"));

        bukuTableView.getColumns().addAll(idBukuColumn, namaBukuColumn, penulisColumn, tahunTerbitColumn);
        bukuTableView.setItems(loadBukuData());

        VBox vbox = new VBox(20);
        vbox.getChildren().addAll(grid, buttonBox, bukuTableView);
        centerAnchorPane.getChildren().add(vbox);

        tambahButton.setOnAction(e -> handleTambahBuku(idKategori, namaBukuField, penulisField, tahunTerbitField, selectedImageLabel, bukuTableView));
        editButton.setOnAction(e -> handleEditBuku(bukuTableView, namaBukuField, penulisField, tahunTerbitField, selectedImageLabel));
        hapusButton.setOnAction(e -> handleHapusBuku(bukuTableView));
        batalButton.setOnAction(e -> handleBatal());
    }

    private ObservableList<Buku> loadBukuData() {
        ObservableList<Buku> bukuList = FXCollections.observableArrayList();
        String query = "SELECT * FROM buku WHERE id_kategori = ?";
        try (Connection conn = dbConnection.getDBConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, selectedKategoriId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bukuList.add(new Buku(
                        rs.getInt("id_buku"),
                        rs.getInt("id_kategori"),
                        rs.getString("nama_buku"),
                        rs.getString("penulis"),
                        rs.getInt("tahun_terbit"),
                        rs.getString("path_gambar")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memuat data buku: " + e.getMessage());
        }
        return bukuList;
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

    private void handlePilihGambar(Label selectedImageLabel, ImageView bookImageView) {
        // Membuat instance FileChooser untuk memilih file gambar
        FileChooser fileChooser = new FileChooser();

        // Menambahkan filter ekstensi untuk hanya memilih file gambar (jpg, png, gif)
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));

        // Menampilkan dialog pemilihan file
        File file = fileChooser.showOpenDialog(null);

        // Jika file dipilih
        if (file != null) {
            // Menampilkan path file gambar di label
            selectedImageLabel.setText(file.getAbsolutePath());

            // Membaca file gambar ke dalam ImageView
            Image image = new Image(file.toURI().toString());
            bookImageView.setImage(image); // Menampilkan gambar pada ImageView
        }
    }

    private void handleTambahBuku(int idKategori, TextField namaBukuField, TextField penulisField, TextField tahunTerbitField, Label selectedImageLabel, TableView<Buku> bukuTableView) {
        // Mengambil informasi dari input form
        String namaBuku = namaBukuField.getText();
        String penulis = penulisField.getText();
        String tahunTerbit = tahunTerbitField.getText();
        String imagePath = selectedImageLabel.getText(); // Path gambar

        // Mengecek apakah semua field sudah diisi
        if (namaBuku.isEmpty() || penulis.isEmpty() || tahunTerbit.isEmpty() || imagePath.isEmpty()) {
            showAlert("Error", "Semua field harus diisi!");
            return;
        }

        // Membuat nama file gambar baru
        String gambarNama = imagePath.substring(imagePath.lastIndexOf(File.separator) + 1); // Mendapatkan nama file gambar
        String targetPath = "/resources/" + gambarNama; // Menentukan path gambar di dalam folder resources

        // Menyalin gambar ke folder resources
        File sourceFile = new File(imagePath);
        File destinationFile = new File(System.getProperty("user.dir") + ("/resources/"));
        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal menyimpan gambar: " + e.getMessage());
            return;
        }

        // Menambahkan buku ke database
        String query = "INSERT INTO buku (id_kategori, nama_buku, penulis, tahun_terbit, path_gambar) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getDBConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idKategori);
            pstmt.setString(2, namaBuku);
            pstmt.setString(3, penulis);
            pstmt.setInt(4, Integer.parseInt(tahunTerbit));
            pstmt.setString(5, targetPath);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Buku berhasil ditambahkan!");
                loadBukuData(); // Refresh data buku setelah penambahan
            } else {
                showAlert("Error", "Gagal menambahkan buku ke database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal menyimpan data buku: " + e.getMessage());
        }
    }

    private void handleEditBuku(TableView<Buku> bukuTableView, TextField namaBukuField, TextField penulisField, TextField tahunTerbitField, Label selectedImageLabel) {
        // Implement edit book logic here
    }

    private void handleHapusBuku(TableView<Buku> bukuTableView) {
        // Implement delete book logic here
    }

    private void handleBatal() {
        centerAnchorPane.getChildren().clear();
        kategoriContainer.setVisible(true);
        headerLabel.setText("Pilih Kategori Buku");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
