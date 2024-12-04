package Register;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.layout.BorderPane;

public class RegistertesController implements Initializable {
    @FXML
    private Label label;

    @FXML
    private PasswordField daftarPassword;

    @FXML
    private TextField daftarUsername;

    @FXML
    void handleRegister(ActionEvent event) throws IOException {
        String username = daftarUsername.getText().trim();
        String password = daftarPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            label.setText("*Username dan Password tidak boleh kosong!");
            return;
        }

        try (Connection conn = dbConnection.getDBConnection()) {
            String sql = "INSERT INTO Account (nama_akun, password_akun) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                label.setText("Berhasil daftar!");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            label.setText("Terjadi kesalahan saat mendaftar.");
        }
        navigateToLoginPage();
    }
    @FXML
    private void navigateToLoginPage() throws IOException {
        // Muat file FXML untuk halaman login
        Parent loginPage = FXMLLoader.load(getClass().getResource("/login/Logintes.fxml"));

        // Ambil stage saat ini
        Stage stage = (Stage) daftarUsername.getScene().getWindow();

        // Buat scene baru dengan halaman login
        Scene loginScene = new Scene(loginPage);

        // Set scene baru ke stage
        stage.setScene(loginScene);
        stage.setTitle("Halaman Login"); // Set judul stage
        stage.show();
    }
    @FXML
    void masuk(ActionEvent event) {
        try {
            // Load halaman register
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/Logintes.fxml"));
            BorderPane loginPage = loader.load();

            // Buat scene baru untuk halaman register
            Scene loginScene = new Scene(loginPage);

            // Ambil stage (window) saat ini dan set scene-nya menjadi register
            Stage stage = (Stage) label.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Method untuk mengosongkan input field setelah berhasil daftar
    private void clearFields() {
        daftarUsername.clear();
        daftarPassword.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization logic (if needed)
    }
}
