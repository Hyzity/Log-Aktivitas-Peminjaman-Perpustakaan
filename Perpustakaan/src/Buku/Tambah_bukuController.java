/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Buku;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import java.sql.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class Tambah_bukuController {

    @FXML private Label titleLabel;
    @FXML private TextField idKategoriField;
    @FXML private TextField namaBukuField;
    @FXML private TextField penulisField;
    @FXML private TextField tahunTerbitField;
    @FXML private TableView<Buku> bukuTableView;
    @FXML private TableColumn<Buku, Integer> idBukuColumn;
    @FXML private TableColumn<Buku, String> namaBukuColumn;
    @FXML private TableColumn<Buku, String> penulisColumn;
    @FXML private TableColumn<Buku, Integer> tahunTerbitColumn;
    @FXML private ImageView bookImageView;
    @FXML private Label selectedImageLabel;

    private int idKategori;
    private String namaKategori;
    private ObservableList<Buku> bukuList = FXCollections.observableArrayList();
    private File selectedImageFile;

    public void initData(int idKategori, String namaKategori) {
        this.idKategori = idKategori;
        this.namaKategori = namaKategori;
        titleLabel.setText("Tambah Buku - " + namaKategori);
        idKategoriField.setText(String.valueOf(idKategori));
        loadBukuData();
    }

    @FXML
    public void initialize() {
        idBukuColumn.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
        namaBukuColumn.setCellValueFactory(new PropertyValueFactory<>("namaBuku"));
        penulisColumn.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        tahunTerbitColumn.setCellValueFactory(new PropertyValueFactory<>("tahunTerbit"));

        bukuTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                namaBukuField.setText(newSelection.getNamaBuku());
                penulisField.setText(newSelection.getPenulis());
                tahunTerbitField.setText(String.valueOf(newSelection.getTahunTerbit()));
                Image image = new Image(getClass().getResourceAsStream(newSelection.getPathGambar()));
                bookImageView.setImage(image);
                selectedImageLabel.setText(newSelection.getPathGambar());
            }
        });
    }

    private void loadBukuData() {
        bukuList.clear();
        String query = "SELECT * FROM buku WHERE id_kategori = ?";
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idKategori);
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

        bukuTableView.setItems(bukuList);
    }

    @FXML
    private void handleTambahBuku() {
        String namaBuku = namaBukuField.getText().trim();
        String penulis = penulisField.getText().trim();
        String tahunTerbit = tahunTerbitField.getText().trim();

        if (namaBuku.isEmpty() || penulis.isEmpty() || tahunTerbit.isEmpty() || selectedImageFile == null) {
            showAlert("Error", "Semua field harus diisi dan gambar harus dipilih!");
            return;
        }

        String imagePath = saveImage(selectedImageFile);
        if (imagePath == null) {
            showAlert("Error", "Gagal menyimpan gambar!");
            return;
        }

        String query = "INSERT INTO buku (id_kategori, nama_buku, penulis, tahun_terbit, path_gambar) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idKategori);
            pstmt.setString(2, namaBuku);
            pstmt.setString(3, penulis);
            pstmt.setInt(4, Integer.parseInt(tahunTerbit));
            pstmt.setString(5, imagePath);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert("Sukses", "Buku berhasil ditambahkan!");
                clearFields();
                loadBukuData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal menambahkan buku: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditBuku() {
        Buku selectedBuku = bukuTableView.getSelectionModel().getSelectedItem();
        if (selectedBuku == null) {
            showAlert("Error", "Pilih buku yang akan diedit!");
            return;
        }

        String namaBuku = namaBukuField.getText().trim();
        String penulis = penulisField.getText().trim();
        String tahunTerbit = tahunTerbitField.getText().trim();

        if (namaBuku.isEmpty() || penulis.isEmpty() || tahunTerbit.isEmpty()) {
            showAlert("Error", "Semua field harus diisi!");
            return;
        }

        String imagePath = selectedBuku.getPathGambar();
        if (selectedImageFile != null) {
            imagePath = saveImage(selectedImageFile);
            if (imagePath == null) {
                showAlert("Error", "Gagal menyimpan gambar!");
                return;
            }
        }

        String query = "UPDATE buku SET nama_buku = ?, penulis = ?, tahun_terbit = ?, path_gambar = ? WHERE id_buku = ?";
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, namaBuku);
            pstmt.setString(2, penulis);
            pstmt.setInt(3, Integer.parseInt(tahunTerbit));
            pstmt.setString(4, imagePath);
            pstmt.setInt(5, selectedBuku.getIdBuku());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert("Sukses", "Buku berhasil diupdate!");
                clearFields();
                loadBukuData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal mengupdate buku: " + e.getMessage());
        }
    }

    @FXML
    private void handleHapusBuku() {
        Buku selectedBuku = bukuTableView.getSelectionModel().getSelectedItem();
        if (selectedBuku == null) {
            showAlert("Error", "Pilih buku yang akan dihapus!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Hapus Buku");
        alert.setContentText("Apakah Anda yakin ingin menghapus buku ini?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String query = "DELETE FROM buku WHERE id_buku = ?";
                try (Connection conn = dbConnection.getDBConnection();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setInt(1, selectedBuku.getIdBuku());

                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        showAlert("Sukses", "Buku berhasil dihapus!");
                        clearFields();
                        loadBukuData();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Error", "Gagal menghapus buku: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleBatal() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }

    private void clearFields() {
        namaBukuField.clear();
        penulisField.clear();
        tahunTerbitField.clear();
        selectedImageFile = null;
        selectedImageLabel.setText("Tidak ada gambar dipilih");
        bookImageView.setImage(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handlePilihGambar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Gambar Buku");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectedImageFile = selectedFile;
            selectedImageLabel.setText(selectedFile.getName());
            Image image = new Image(selectedFile.toURI().toString());
            bookImageView.setImage(image);
        }
    }

    private String saveImage(File file) {
        try {
            String resourcesPath = "src/resources/";
            File dir = new File(resourcesPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = System.currentTimeMillis() + "_" + file.getName();
            Path targetPath = Paths.get(resourcesPath + fileName);
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return "resources/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

