
package id.ac.ub.filkom.contoh.service;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** Service: tempat logika bisnis/aturan aplikasi. */
@Service
public class AnggotaService {

    private final AnggotaRepository anggotaRepository;

    // Dependency injection lewat constructor
    public AnggotaService(AnggotaRepository anggotaRepository) {
        this.anggotaRepository = anggotaRepository;
    }

    public List<Anggota> cariAnggota(String kata) {
        if (kata == null || kata.isBlank()) {
            return anggotaRepository.findAll();
        }
        return anggotaRepository.findAll().stream()
                .filter(anggota -> anggota.getNama()
                        .toLowerCase()
                        .contains(kata.trim().toLowerCase()))
                .toList();
    }

    public Anggota tambahAnggota(String idAnggota, String nama, int batasPinjam) {
        if (batasPinjam < 0) {
            throw new IllegalArgumentException(
                    "Batas pinjam tidak boleh negatif"
            );
        }
        return anggotaRepository.save(
                new Anggota(idAnggota, nama, batasPinjam)
        );
    }
}
