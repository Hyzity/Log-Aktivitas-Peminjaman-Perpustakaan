package admin;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LibraryDashboardController extends Application {

    private Stage primaryStage; // Menyimpan primary stage untuk navigasi

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Sidebar (VBox)
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(200); // Lebar sidebar
        sidebar.setSpacing(15);   // Jarak antar elemen
        sidebar.setAlignment(Pos.TOP_CENTER); // Elemen rata tengah
        sidebar.setStyle("-fx-background-color: #34495e; -fx-padding: 20;"); // Warna dan padding sidebar

        // ImageView untuk gambar
        Image image = new Image("file:D:\\Semester III\\Admin\\src\\admin\\download.jpg"); // Path gambar
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(180); // Lebar gambar
        imageView.setFitHeight(120); // Tinggi gambar
        imageView.setPreserveRatio(true); // Menjaga proporsi gambar
        sidebar.getChildren().add(imageView); // Tambahkan gambar ke sidebar

        // Label menu
        Label menuLabel = new Label("Menu");
        menuLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 18px; -fx-font-weight: bold;");
        sidebar.getChildren().add(menuLabel);

        // Tombol menu
        Button btnJenisBuku = createStyledButton("Jenis Buku");
        Button btnKategoriBuku = createStyledButton("Kategori Buku");
        Button btnPeminjaman = createStyledButton("Peminjaman");

        // Event untuk tombol Kategori Buku
        btnKategoriBuku.setOnAction(e -> openKategoriBuku());

        // Tambahkan tombol ke sidebar
        sidebar.getChildren().addAll(btnJenisBuku, btnKategoriBuku, btnPeminjaman);

        // Bagian tengah dengan daftar statistik
        VBox centerContent = new VBox();
        centerContent.setSpacing(20); // Jarak antar elemen
        centerContent.setAlignment(Pos.TOP_CENTER); // Rata tengah
        centerContent.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 20;");

        // Label "Daftar Statistik"
        Label statistikLabel = new Label("Daftar Statistik");
        statistikLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 20px; -fx-font-weight: bold;");
        centerContent.getChildren().add(statistikLabel);

        // Barisan tombol statistik
        HBox statistikButtons = new HBox();
        statistikButtons.setSpacing(15); // Jarak antar tombol
        statistikButtons.setAlignment(Pos.CENTER);

        Button btnStatistikBuku = createColoredButton("Jenis Buku", "#3498db");
        Button btnStatistikKategori = createColoredButton("Kategori Buku", "#2ecc71");
        Button btnStatistikPeminjaman = createColoredButton("Peminjaman", "#e74c3c");

        statistikButtons.getChildren().addAll(btnStatistikBuku, btnStatistikKategori, btnStatistikPeminjaman);
        centerContent.getChildren().add(statistikButtons);

        // Layout utama menggunakan BorderPane
        BorderPane root = new BorderPane();
        root.setLeft(sidebar); // Tempatkan sidebar di sisi kiri
        root.setCenter(centerContent); // Tempatkan konten di tengah

        // Scene
        Scene scene = new Scene(root, 800, 600); // Ukuran jendela
        primaryStage.setTitle("Library Dashboard"); // Judul aplikasi
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Fungsi untuk membuat tombol dengan gaya tambahan.
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(180); // Lebar tombol
        button.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; "
                + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10;");
        return button;
    }

    /**
     * Fungsi untuk membuat tombol statistik berwarna.
     */
    private Button createColoredButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(150); // Lebar tombol
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: #ffffff; "
                + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10;");
        return button;
    }

    /**
     * Membuka halaman Kategori Buku.
     */
    private void openKategoriBuku() {
        Label headerLabel = new Label("Kategori Buku");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10;");

        // Tombol kembali ke dashboard
        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-padding: 10; -fx-background-color: #3498db; -fx-text-fill: white;");
        backButton.setOnAction(e -> start(primaryStage));

        // Layout untuk halaman kategori buku
        VBox vbox = new VBox();
        vbox.getChildren().addAll(headerLabel, new Label("Halaman Kategori Buku"), backButton);
        vbox.setStyle("-fx-padding: 20; -fx-spacing: 15; -fx-alignment: center;");

        // Scene untuk halaman kategori buku
        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
    }
    
    private void openJenisBuku() {
        Label headerLabel = new Label("Jenis Buku");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10;");

        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-padding: 10; -fx-background-color: #3498db; -fx-text-fill: white;");
        backButton.setOnAction(e -> start(primaryStage));

       
        VBox vbox = new VBox();
        vbox.getChildren().addAll(headerLabel, new Label("Halaman Jenis Buku"), backButton);
        vbox.setStyle("-fx-padding: 20; -fx-spacing: 15; -fx-alignment: center;");

        // Scene untuk halaman kategori buku
        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Main method.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
