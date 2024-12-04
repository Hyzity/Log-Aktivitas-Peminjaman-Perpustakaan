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

        // Simple validation to check if fields are empty
        if (username.isEmpty() || password.isEmpty()) {
            label.setText("*Username dan Password tidak boleh kosong!");
            return;
        }

        // Logic to verify the username and password (this is a mock-up)
        if (username.equals("admin") && password.equals("admin123")) {
            label.setText("Login Berhasil!");

            // Navigate to the main page (can be replaced with your actual page)
            navigateToMainPage();
        } else {
            label.setText("Username atau Password salah.");
        }
    }



    // Method to navigate to the main page after login
    private void navigateToMainPage() throws IOException {
        // Muat halaman utama (ganti dengan file FXML halaman utama Anda)
        Parent mainPage = FXMLLoader.load(getClass().getResource("/main/MainPage.fxml"));

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
