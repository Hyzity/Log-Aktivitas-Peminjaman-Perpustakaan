/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homepage;

/**
 *
 * @author Lenovo
 */
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private Label label;

    // Fungsi untuk buttonMasuk - Redirect ke halaman Login
    @FXML
    private void buttonMasuk(ActionEvent event) {
        try {
            // Load halaman login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/Logintes.fxml"));
            BorderPane loginPage = loader.load();  // Ganti AnchorPane dengan BorderPane
            Scene loginScene = new Scene(loginPage);
            Stage stage = (Stage) label.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk buttonDaftar - Redirect ke halaman Register
    @FXML
    private void buttonDaftar(ActionEvent event) {
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
}
