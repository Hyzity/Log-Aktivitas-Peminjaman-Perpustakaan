/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Lenovo
 */
public class LogintesController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label label;
    @FXML
    void daftar(ActionEvent event) {
        try {
            // Load halaman register
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Register/Registertes.fxml"));
            BorderPane registerPage = loader.load();

            // Buat scene baru untuk halaman register
            Scene registerScene = new Scene(registerPage);

            // Ambil stage (window) saat ini dan set scene-nya menjadi register
            Stage stage = (Stage) label.getScene().getWindow();
            stage.setScene(registerScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle login action
    @FXML
void handleLogin(ActionEvent event) throws IOException {
    String username = usernameField.getText().trim();
    String password = passwordField.getText().trim();

    // Validasi jika field kosong
    if (username.isEmpty() || password.isEmpty()) {
        label.setText("*Username dan Password tidak boleh kosong!");
        return;
    }

    // Logika khusus untuk admin
    if (username.equals("admin") && password.equals("admin123")) {
        label.setText("Login Berhasil sebagai Admin!");

        // Navigasi ke halaman admin
        navigateToMainPage();
        return;
    }

    // Cek username dan password dari database
    if (authenticateUser(username, password)) {
        label.setText("Login Berhasil!");

        // Navigasi ke halaman utama
        navigateToMainPage();
    } else {
        label.setText("Username atau Password salah.");
    }
}

/**
 * Memeriksa username dan password di database.
 * 
 * @param username Username yang diinput
 * @param password Password yang diinput
 * @return true jika valid, false jika tidak valid
 */
private boolean authenticateUser(String username, String password) {
    String query = "SELECT COUNT(*) AS count FROM account WHERE nama_akun = ? AND password_akun = ?";

    try (Connection connection = dbConnection.getDBConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0; // Jika ada hasil, login valid
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        label.setText("Terjadi kesalahan saat menghubungkan ke database.");
    }

    return false; // Login tidak valid
}

    // Method to navigate to the main page after login
    private void navigateToMainPage() throws IOException {
        // Muat halaman utama (ganti dengan file FXML halaman utama Anda)
        Parent mainPage = FXMLLoader.load(getClass().getResource("/main/mainMenu.fxml"));

        // Ambil stage saat ini
        Stage stage = (Stage) usernameField.getScene().getWindow();

        // Buat scene baru untuk halaman utama
        Scene mainScene = new Scene(mainPage);

        // Set scene baru ke stage
        stage.setScene(mainScene);
        stage.setTitle("Halaman Utama"); // Set judul stage
        stage.show();
    }
}
