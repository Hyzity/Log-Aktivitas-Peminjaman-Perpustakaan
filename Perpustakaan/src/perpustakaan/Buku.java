/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package perpustakaan;

/**
 *
 * @author noelt
 */
public class Buku {
    private int idBuku;
    private String namaBuku;
    private String penulis;
    private int tahunTerbit;
    private String nama;

    public Buku(int idBuku, String namaBuku, String penulis, int tahunTerbit) {
        this.idBuku = idBuku;
        this.namaBuku = namaBuku;
        this.penulis = penulis;
        this.tahunTerbit = tahunTerbit;
    }
    public void setNamaAkun(String nama) {
        this.nama = nama;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public String getNamaBuku() {
        return namaBuku;
    }

    public String getPenulis() {
        return penulis;
    }

    public int getTahunTerbit() {
        return tahunTerbit;
    }
    public String getNamaAkun(){
        return nama;
    }
}
