/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package perpustakaan;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import java.sql.*;

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

    private Connection connection;

    public void initialize() {
        connectToDatabase();
        loadBooks("");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> loadBooks(newValue));
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/perpustakaan", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBooks(String keyword) {
        tilePane.getChildren().clear();

        String query = "SELECT * FROM buku WHERE nama_buku LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idBuku = rs.getInt("id_buku");
                    String namaBuku = rs.getString("nama_buku");
                    String pathGambar = rs.getString("path_gambar");
                    tilePane.getChildren().add(createBookCard(idBuku, namaBuku, pathGambar));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createBookCard(int idBuku, String namaBuku, String pathGambar) {
        VBox card = new VBox(5);
        card.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-alignment: center;");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        try {
            imageView.setImage(new Image(getClass().getResource(pathGambar).toExternalForm()));
        } catch (NullPointerException e) {
            imageView.setImage(new Image(getClass().getResource("/resources/default.jpg").toExternalForm()));
        }

        Label titleLabel = new Label(namaBuku);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button borrowButton = new Button("Pinjam");
        borrowButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        borrowButton.setOnAction((ActionEvent e) -> handleBorrowBook(idBuku));

        card.getChildren().addAll(imageView, titleLabel, borrowButton);
        return card;
    }

    private void handleBorrowBook(int idBuku) {
        System.out.println("Buku dengan ID " + idBuku + " dipinjam!");
    }
}
