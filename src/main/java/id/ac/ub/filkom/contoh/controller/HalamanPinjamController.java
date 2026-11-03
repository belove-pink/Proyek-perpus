
package id.ac.ub.filkom.contoh.controller;

import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.service.PinjamService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
