package CTRLC.ERRONKA3.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import CTRLC.ERRONKA3.model.HistorikoaStats;
import CTRLC.ERRONKA3.model.historikoa;
import CTRLC.ERRONKA3.repository.HistorikoaRepository;
import CTRLC.ERRONKA3.service.HistorikoaService;
import jakarta.servlet.http.HttpSession;

@Controller
public class HistorikoaController {

    private final HistorikoaRepository historikoaRepository;
    private final HistorikoaService    historikoaService;

    public HistorikoaController(HistorikoaRepository historikoaRepository,
                                HistorikoaService historikoaService) {
        this.historikoaRepository = historikoaRepository;
        this.historikoaService    = historikoaService;
    }

    @GetMapping("/admin/historikoa")
    public String historikoaPage(Model model, HttpSession session) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login";
        }

        // sp_historikoa_guztiak() Stored Procedure deia (JdbcTemplate bidez)
        List<historikoa> historikoak = historikoaService.getHistorikoaGuztiak();

        // fn_historikoa_kopurua / fn_historikoa_ekintza_kopurua Function deiak
        HistorikoaStats stats = historikoaService.getStats();

        model.addAttribute("historikoak", historikoak);
        model.addAttribute("stats",       stats);
        model.addAttribute("currentUser", session.getAttribute("loggedUser"));
        model.addAttribute("role",        session.getAttribute("role"));
        return "historikoa";
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedUser") != null;
    }

    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return role != null && role.contains("admin");
    }
}
