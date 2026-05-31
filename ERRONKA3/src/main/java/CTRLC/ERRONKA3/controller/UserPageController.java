package CTRLC.ERRONKA3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import CTRLC.ERRONKA3.repository.ErabiltzaileRepository;
import CTRLC.ERRONKA3.repository.EraikinaRepository;
import CTRLC.ERRONKA3.repository.GailuaRepository;
import CTRLC.ERRONKA3.repository.GelaRepository;
import CTRLC.ERRONKA3.repository.SolairuaRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserPageController {

    private final ErabiltzaileRepository erabiltzaileRepository;
    private final EraikinaRepository eraikinaRepository;
    private final GelaRepository gelaRepository;
    private final GailuaRepository gailuaRepository;
    private final SolairuaRepository solairuaRepository;

    public UserPageController(ErabiltzaileRepository erabiltzaileRepository,
            EraikinaRepository eraikinaRepository,
            GelaRepository gelaRepository,
            GailuaRepository gailuaRepository,
            SolairuaRepository solairuaRepository) {
        this.erabiltzaileRepository = erabiltzaileRepository;
        this.eraikinaRepository = eraikinaRepository;
        this.gelaRepository = gelaRepository;
        this.gailuaRepository = gailuaRepository;
        this.solairuaRepository = solairuaRepository;
    }

    @GetMapping("/user")
    public String userWelcomePage(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        if (isAdmin(session)) {
            return "redirect:/admin";
        }

        model.addAttribute("currentUser", session.getAttribute("loggedUser"));
        model.addAttribute("role", session.getAttribute("role"));
        return "welcome_user";
    }

    @GetMapping("/user/kontsulta")
    public String userKontsultaPage(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        if (isAdmin(session)) {
            return "redirect:/admin";
        }

        model.addAttribute("currentUser", session.getAttribute("loggedUser"));
        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("erabiltzaileak", erabiltzaileRepository.findAll());
        model.addAttribute("eraikinak", eraikinaRepository.findAll());
        model.addAttribute("gelak", gelaRepository.findAll());
        model.addAttribute("gailuak", gailuaRepository.findAll());
        model.addAttribute("solairuak", solairuaRepository.findAll());
        return "Kontsulta_arrunta";
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedUser") != null;
    }

    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return role != null && role.contains("admin");
    }
}