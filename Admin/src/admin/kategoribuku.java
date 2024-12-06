package admin;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class kategoribuku extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Membuat GridPane untuk tata letak grid
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20)); // Padding di sekitar grid
        gridPane.setHgap(20); // Jarak horizontal antar elemen
        gridPane.setVgap(20); // Jarak vertikal antar elemen
        gridPane.setAlignment(Pos.CENTER); // Grid rata tengah

        // Menambahkan 6 kartu ke dalam grid (2 baris x 3 kolom)
        int rows = 2; // Jumlah baris
        int cols = 3; // Jumlah kolom
        int cardWidth = 150; // Lebar kartu
        int cardHeight = 200; // Tinggi kartu

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Pane card = createCard(cardWidth, cardHeight);
                gridPane.add(card, col, row); // Tambahkan kartu ke grid
            }
        }

        // Membuat scene dan menambahkan gridPane ke dalamnya
        Scene scene = new Scene(gridPane, 600, 400); // Ukuran jendela
        primaryStage.setTitle("Grid View Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Membuat sebuah kartu berbentuk persegi panjang dengan warna dan border.
     * 
     * @param width  Lebar kartu
     * @param height Tinggi kartu
     * @return Pane yang merepresentasikan kartu
     */
    private Pane createCard(int width, int height) {
        Pane card = new Pane();
        card.setPrefSize(width, height);
        card.setStyle("-fx-background-color: #ffffcc; -fx-border-color: black; -fx-border-width: 1;");
        return card;
    }

    /**
     * Main method untuk menjalankan aplikasi.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
