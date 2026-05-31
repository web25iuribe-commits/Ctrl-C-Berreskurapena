package CTRLC.ERRONKA3.controller;

import java.time.LocalDate;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import CTRLC.ERRONKA3.model.erabiltzailea;
import CTRLC.ERRONKA3.repository.ErabiltzaileRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class ErabiltzaileaController {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern NAN_PATTERN = Pattern.compile("^[0-9]{8}[A-Za-z]$");
    private static final Set<String> ALLOWED_ROLES = Set.of("erabiltzaile", "usuario", "user", "administrador",
            "admin");

    private final ErabiltzaileRepository erabiltzaileRepository;

    public ErabiltzaileaController(ErabiltzaileRepository erabiltzaileRepository) {
        this.erabiltzaileRepository = erabiltzaileRepository;
    }

    @PostMapping("/admin/erabiltzailea/save")
    public String saveErabiltzailea(@RequestParam String id_erab,
            @RequestParam String NAN,
            @RequestParam String izena,
            @RequestParam String abizena,
            @RequestParam String helbide_elektronikoa,
            @RequestParam String pasahitza,
            @RequestParam String erabiltzaile_mota,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/admin/editatu?accessDenied=true";
        }

        String validationError = validateErabiltzaileaInput(id_erab, NAN, izena, abizena, helbide_elektronikoa,
                pasahitza, erabiltzaile_mota);
        if (validationError != null) {
            redirectAttributes.addFlashAttribute("error", validationError);
            return "redirect:/admin/editatu";
        }

        String normalizedId = id_erab.trim();
        if (erabiltzaileRepository.existsById(normalizedId)) {
            redirectAttributes.addFlashAttribute("error",
                    "ID hori dagoeneko existitzen da. Mesedez, jarri existitzen ez den ID bat.");
            return "redirect:/admin/editatu";
        }

        try {
            erabiltzailea erab = new erabiltzailea();
            erab.setId_erab(normalizedId);
            erab.setNAN(NAN.trim().toUpperCase());
            erab.setIzena(izena.trim());
            erab.setAbizena(abizena.trim());
            erab.setHelbide_elektronikoa(helbide_elektronikoa.trim().toLowerCase());
            erab.setPasahitza(pasahitza.trim());
            erab.setErabiltzaile_mota(normalizeRole(erabiltzaile_mota));
            erab.setAlta_data(LocalDate.now());
            erabiltzaileRepository.save(erab);
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("error",
                    "Ezin izan da erabiltzailea gorde: datuak ez dira baliodunak edo errepikatuak dira.");
            return "redirect:/admin/editatu";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error",
                    "Ezin izan da erabiltzailea gorde. Saiatu berriro datuak egiaztatuta.");
            return "redirect:/admin/editatu";
        }

        redirectAttributes.addFlashAttribute("success", "Erabiltzailea ondo sortu da.");
        return "redirect:/admin/editatu";
    }

    @PostMapping("/admin/erabiltzailea/update")
    public String updateErabiltzailea(@RequestParam String id_erab,
            @RequestParam String NAN,
            @RequestParam String izena,
            @RequestParam String abizena,
            @RequestParam String helbide_elektronikoa,
            @RequestParam String pasahitza,
            @RequestParam String erabiltzaile_mota,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/admin/editatu?accessDenied=true";
        }

        String validationError = validateErabiltzaileaInput(id_erab, NAN, izena, abizena, helbide_elektronikoa,
                pasahitza, erabiltzaile_mota);
        if (validationError != null) {
            redirectAttributes.addFlashAttribute("error", validationError);
            return "redirect:/admin/editatu";
        }

        try {
            erabiltzailea erab = new erabiltzailea();
            String normalizedId = id_erab.trim();
            erab.setId_erab(normalizedId);
            erab.setNAN(NAN.trim().toUpperCase());
            erab.setIzena(izena.trim());
            erab.setAbizena(abizena.trim());
            erab.setHelbide_elektronikoa(helbide_elektronikoa.trim().toLowerCase());
            erab.setPasahitza(pasahitza.trim());
            erab.setErabiltzaile_mota(normalizeRole(erabiltzaile_mota));
            LocalDate altaData = erabiltzaileRepository.findById(normalizedId)
                    .map(erabiltzailea::getAlta_data)
                    .orElse(LocalDate.now());
            erab.setAlta_data(altaData);
            erabiltzaileRepository.save(erab);
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("error",
                    "Ezin izan da erabiltzailea eguneratu: datuak ez dira baliodunak edo errepikatuak dira.");
            return "redirect:/admin/editatu";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error",
                    "Ezin izan da erabiltzailea eguneratu. Saiatu berriro datuak egiaztatuta.");
            return "redirect:/admin/editatu";
        }

        redirectAttributes.addFlashAttribute("success", "Erabiltzailea ondo eguneratu da.");
        return "redirect:/admin/editatu";
    }

    @PostMapping("/admin/erabiltzailea/delete")
    public String deleteErabiltzailea(@RequestParam String id_erab,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/admin/editatu?accessDenied=true";
        }

        try {
            erabiltzaileRepository.deleteById(id_erab);
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("error",
                    "Ezin izan da erabiltzailea ezabatu: beste erregistro batzuekin lotuta dago.");
            return "redirect:/admin/editatu";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", "Ezin izan da erabiltzailea ezabatu. Saiatu berriro.");
            return "redirect:/admin/editatu";
        }

        redirectAttributes.addFlashAttribute("success", "Erabiltzailea ondo ezabatu da.");
        return "redirect:/admin/editatu";
    }

    private String validateErabiltzaileaInput(String idErab,
            String nan,
            String izena,
            String abizena,
            String email,
            String pasahitza,
            String erabiltzaileMota) {
        if (isBlank(idErab) || isBlank(nan) || isBlank(izena) || isBlank(abizena)
                || isBlank(email) || isBlank(pasahitza) || isBlank(erabiltzaileMota)) {
            return "Eremu guztiak bete behar dira.";
        }

        if (idErab.trim().length() > 30) {
            return "ID eremua luzeegia da (max 30 karaktere).";
        }

        if (!NAN_PATTERN.matcher(nan.trim()).matches()) {
            return "NAN formatu baliogabea da. Adibidea: 12345678A";
        }

        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "Email formatua ez da zuzena.";
        }

        if (pasahitza.trim().length() < 4) {
            return "Pasahitzak gutxienez 4 karaktere izan behar ditu.";
        }

        if (normalizeRole(erabiltzaileMota) == null) {
            return "Erabiltzaile mota baliogabea da.";
        }

        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String normalizeRole(String role) {
        if (isBlank(role)) {
            return null;
        }

        String normalized = role.trim().toLowerCase();
        if (!ALLOWED_ROLES.contains(normalized)) {
            return null;
        }

        if ("admin".equals(normalized) || "administrador".equals(normalized)) {
            return "admin";
        }
        return "erabiltzaile";
    }

    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return role != null && role.contains("admin");
    }
}
