/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kategori_buku;

/**
 *
 * @author Lenovo
 */
public class KategoriBuku {
    private int idKategori;
    private String namaKategori;

    public KategoriBuku(int idKategori, String namaKategori) {
        this.idKategori = idKategori;
        this.namaKategori = namaKategori;
    }

    public int getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }
}
