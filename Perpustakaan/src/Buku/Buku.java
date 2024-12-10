/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Buku;

/**
 *
 * @author Lenovo
 */
public class Buku {
private int idBuku;
    private int idKategori;
    private String namaBuku;
    private String penulis;
    private int tahunTerbit;
    private String pathGambar;

    public Buku(int idBuku, int idKategori, String namaBuku, String penulis, int tahunTerbit, String pathGambar) {
        this.idBuku = idBuku;
        this.idKategori = idKategori;
        this.namaBuku = namaBuku;
        this.penulis = penulis;
        this.tahunTerbit = tahunTerbit;
        this.pathGambar = pathGambar;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public void setIdBuku(int idBuku) {
        this.idBuku = idBuku;
    }

    public int getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
    }

    public String getNamaBuku() {
        return namaBuku;
    }

    public void setNamaBuku(String namaBuku) {
        this.namaBuku = namaBuku;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public int getTahunTerbit() {
        return tahunTerbit;
    }

    public void setTahunTerbit(int tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }

    public String getPathGambar() {
        return pathGambar;
    }

    public void setPathGambar(String pathGambar) {
        this.pathGambar = pathGambar;
    }
}

