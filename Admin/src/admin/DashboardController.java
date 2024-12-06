/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package admin;

/**
 *
 * @author jonathan
 */


import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label jenisbukulabel;

    @FXML
    private Label kategoribukulabel;

    @FXML
    private Label peminjamanlabel;

  
    @FXML
    public void initialize() {
        // Set nilai statistik
        jenisbukulabel.setText("2");
        kategoribukulabel.setText("3");
        peminjamanlabel.setText("3");
    }
}
