
package CTRLC.ERRONKA3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import CTRLC.ERRONKA3.repository.ErabiltzaileRepository;
import CTRLC.ERRONKA3.repository.EraikinaRepository;
import CTRLC.ERRONKA3.repository.GailuaRepository;
import CTRLC.ERRONKA3.repository.GelaRepository;
import CTRLC.ERRONKA3.repository.HistorikoaRepository;
import CTRLC.ERRONKA3.repository.SolairuaRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminPageController {

    private final ErabiltzaileRepository erabiltzaileRepository;
    private final EraikinaRepository eraikinaRepository;
    private final GelaRepository gelaRepository;
    private final GailuaRepository gailuaRepository;
    private final SolairuaRepository solairuaRepository;
    private final HistorikoaRepository historikoaRepository;

    public AdminPageController(ErabiltzaileRepository erabiltzaileRepository,
            EraikinaRepository eraikinaRepository,
            GelaRepository gelaRepository,
            GailuaRepository gailuaRepository,
            SolairuaRepository solairuaRepository,
            HistorikoaRepository historikoaRepository) {
        this.erabiltzaileRepository = erabiltzaileRepository;
        this.eraikinaRepository = eraikinaRepository;
        this.gelaRepository = gelaRepository;
        this.gailuaRepository = gailuaRepository;
        this.solairuaRepository = solairuaRepository;
        this.historikoaRepository = historikoaRepository;
    }

    @GetMapping("/admin")
    public String adminWelcomePage(Model model, HttpSession session) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", session.getAttribute("loggedUser"));
        model.addAttribute("role", session.getAttribute("role"));
        return "welcome_admin";
    }

    @GetMapping("/admin/kontsulta")
    public String adminKontsultaPage(Model model, HttpSession session) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login";
        }
        loadAdminData(model, session);
        return "Kontsulta_admi";
    }

    private void loadAdminData(Model model, HttpSession session) {
        model.addAttribute("currentUser", session.getAttribute("loggedUser"));
        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("erabiltzaileak", erabiltzaileRepository.findAll());
        model.addAttribute("eraikinak", eraikinaRepository.findAll());
        model.addAttribute("gelak", gelaRepository.findAll());
        model.addAttribute("gailuak", gailuaRepository.findAll());
        model.addAttribute("solairuak", solairuaRepository.findAll());
        model.addAttribute("historikoak", historikoaRepository.findAll());
    }

    @GetMapping("/admin/editatu")
    public String adminEditorea(Model model, HttpSession session) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login";
        }
        loadAdminData(model, session);
        return "admin";
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedUser") != null;
    }

    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return role != null && role.contains("admin");
    }
}