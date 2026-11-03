
package id.ac.ub.filkom.perpustakaan.control;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.ac.ub.filkom.perpustakaan.entity.Anggota;
import id.ac.ub.filkom.perpustakaan.entity.AnggotaRepository;
import id.ac.ub.filkom.perpustakaan.entity.Buku;
import id.ac.ub.filkom.perpustakaan.entity.BukuRepository;
import id.ac.ub.filkom.perpustakaan.entity.TransaksiPinjam;
import id.ac.ub.filkom.perpustakaan.entity.TransaksiPinjamRepository;

@Service
public class PinjamService {

    private final AnggotaRepository anggotaRepository;
    private final BukuRepository bukuRepository;
    private final TransaksiPinjamRepository transaksiRepository;

    public PinjamService(
            AnggotaRepository anggotaRepository,
            BukuRepository bukuRepository,
            TransaksiPinjamRepository transaksiRepository) {

        this.anggotaRepository = anggotaRepository;
        this.bukuRepository = bukuRepository;
        this.transaksiRepository = transaksiRepository;
    }

    @Transactional
    public Bukti pinjam(String idAnggota, String idBuku) {

        // Mencari data anggota
        Anggota anggota = anggotaRepository.findById(idAnggota)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Anggota tidak ditemukan"));

        // Mencari data buku
        Buku buku = bukuRepository.findById(idBuku)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Buku tidak ditemukan"));

        // Memeriksa batas peminjaman anggota
        if (!anggota.cekBatasPinjam()) {
            throw new IllegalArgumentException(
                "Batas peminjaman anggota sudah tercapai");
        }

        // Memeriksa ketersediaan stok buku
        if (buku.getStok() <= 0) {
            throw new IllegalArgumentException(
                "Stok buku habis");
        }

        // Mengurangi stok buku
        buku.kurangiStok();
        bukuRepository.save(buku);

        // Membuat transaksi peminjaman
        TransaksiPinjam transaksi =
                TransaksiPinjam.buat(anggota, buku);

        transaksiRepository.save(transaksi);

        // Menghasilkan bukti peminjaman
        return Bukti.dari(transaksi);
    }
}
