
package id.ac.ub.filkom.perpustakaan.boundary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import id.ac.ub.filkom.perpustakaan.control.PinjamService;
import id.ac.ub.filkom.perpustakaan.entity.AnggotaRepository;
import id.ac.ub.filkom.perpustakaan.entity.BukuRepository;

@Controller
public class HalamanPinjamController {

    private final PinjamService pinjamService;
    private final AnggotaRepository anggotaRepository;
    private final BukuRepository bukuRepository;

    public HalamanPinjamController(
            PinjamService pinjamService,
            AnggotaRepository anggotaRepository,
            BukuRepository bukuRepository) {

        this.pinjamService = pinjamService;
        this.anggotaRepository = anggotaRepository;
        this.bukuRepository = bukuRepository;
    }

    @GetMapping("/pinjam")
    public String halamanPinjam(Model model) {
        model.addAttribute("daftarAnggota", anggotaRepository.findAll());
        model.addAttribute("daftarBuku", bukuRepository.findAll());
        return "pinjam";
    }

    @PostMapping("/pinjam")
    public String prosesPinjam(
            @RequestParam String idAnggota,
            @RequestParam String idBuku,
            Model model) {

        try {
            model.addAttribute(
                "bukti", pinjamService.pinjam(idAnggota, idBuku)
            );
            return "bukti";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("daftarAnggota", anggotaRepository.findAll());
            model.addAttribute("daftarBuku", bukuRepository.findAll());
            return "pinjam";
        }
    }
}
