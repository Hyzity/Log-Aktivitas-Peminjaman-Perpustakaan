package Transaksi;

public class LogPeminjaman {
    private int IdBuku;
    private int idTransaksi;
    private String namaBuku;  // Menambahkan nama buku
    private String status;
    private String tanggalPeminjaman;
    private String tanggalPengembalian;

    // Constructor
    public LogPeminjaman(int IdBuku, int idTransaksi, String status, String namaBuku, String tanggalPeminjaman, String tanggalPengembalian) {
        this.IdBuku = IdBuku;
        this.idTransaksi = idTransaksi;
        this.namaBuku = namaBuku;
        this.status = status;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.tanggalPengembalian = tanggalPengembalian;
    }

    // Getter dan Setter
    public int getIdBuku() {
        return IdBuku;
    }

    public void setIdBuku(int IdBuku) {
        this.IdBuku = IdBuku;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggalPeminjaman() {
        return tanggalPeminjaman;
    }

    public void setTanggalPeminjaman(String tanggalPeminjaman) {
        this.tanggalPeminjaman = tanggalPeminjaman;
    }

    public String getTanggalPengembalian() {
        return tanggalPengembalian;
    }

    public void setTanggalPengembalian(String tanggalPengembalian) {
        this.tanggalPengembalian = tanggalPengembalian;
    }

    public String getNamaBuku() {
        return namaBuku;
    }

    public void setNamaBuku(String namaBuku) {
        this.namaBuku = namaBuku;
    }
}
