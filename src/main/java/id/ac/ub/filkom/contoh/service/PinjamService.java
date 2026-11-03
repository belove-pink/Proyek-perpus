
package id.ac.ub.filkom.contoh.service;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.TransaksiPinjam;

import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.repository.TransaksiPinjamRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Anggota anggota = anggotaRepository.findById(idAnggota)
                .orElseThrow(() ->
                    new IllegalArgumentException("Anggota tidak ditemukan"));

        Buku buku = bukuRepository.findById(idBuku)
                .orElseThrow(() ->
                    new IllegalArgumentException("Buku tidak ditemukan"));

        if (!anggota.cekBatasPinjam()) {
            throw new IllegalArgumentException(
                "Batas peminjaman anggota sudah tercapai");
        }

        if (buku.getStok() <= 0) {
            throw new IllegalArgumentException("Stok buku habis");
        }

        buku.kurangiStok();
        bukuRepository.save(buku);

        TransaksiPinjam transaksi =
                TransaksiPinjam.buat(anggota, buku);

        transaksiRepository.save(transaksi);

        return Bukti.dari(transaksi);
    }
}
