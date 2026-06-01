package CTRLC.ERRONKA3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import CTRLC.ERRONKA3.model.erabiltzailea;
import CTRLC.ERRONKA3.model.eraikina;
import CTRLC.ERRONKA3.repository.ErabiltzaileRepository;
import CTRLC.ERRONKA3.repository.EraikinaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller // Spring-i esaten dio klase honek HTTP eskariak (URLak) jasoko dituela
public class HasieraController {

    private final ErabiltzaileRepository erabiltzaileRepository;
    private final EraikinaRepository eraikinaRepository;

    @Autowired // Lotura automatikoa.  Spring-ek automatikoki bilatuko du Repository-aren inplementazioa
    public HasieraController(ErabiltzaileRepository erabiltzaileRepository,
                             EraikinaRepository eraikinaRepository) {
        this.erabiltzaileRepository = erabiltzaileRepository;
        this.eraikinaRepository = eraikinaRepository;
    }

    @GetMapping("/login")
    public String loginForm(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login";
    }

    @PostMapping("/login")
    public String autenticar(@RequestParam String email,
                             @RequestParam String pasahitza,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String emaila = email == null ? "" : email.trim();
        String password = pasahitza == null ? "" : pasahitza.trim();

        if (emaila.isEmpty() || password.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Emaila edo Pasahitza ez da zuzena.");
            return "redirect:/login";
        }

        erabiltzailea user = erabiltzaileRepository.findByEmailAndPassword(emaila, password).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Emaila edo Pasahitza ez da zuzena.");
            return "redirect:/login";
        }

        session.setAttribute("loggedUser", user);
        session.setAttribute("role", user.getErabiltzaile_mota());
        if (isAdminRole(user.getErabiltzaile_mota())) {
            return "redirect:/admin";
        }
        return "redirect:/user";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping({"/", "/eraikina", "/usuarios"})
    public String kaixo(Model model,
                        HttpSession session,
                        HttpServletRequest request,
                        @RequestParam(value = "accessDenied", required = false) String accessDenied) {
        erabiltzailea loggedUser = (erabiltzailea) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        String role = (String) session.getAttribute("role");
        if (isAdminRole(role)) {
            return "redirect:/admin";
        }
        return "redirect:/user";
    }

    @PostMapping("/eraikina/save")
    public String saveEraikina(@RequestParam String id_eraikina,
                               @RequestParam String izena,
                               @RequestParam String helbidea,
                               @RequestParam String hiria,
                               @RequestParam String posta_kodea,
                               HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/?accessDenied=true";
        }

        eraikina eraikina = new eraikina();
        eraikina.setId_eraikina(id_eraikina);
        eraikina.setIzena(izena);
        eraikina.setHelbidea(helbidea);
        eraikina.setHiria(hiria);
        eraikina.setPosta_kodea(posta_kodea);
        eraikinaRepository.save(eraikina);
        return "redirect:/";
    }

    @PostMapping("/eraikina/delete")
    public String deleteEraikina(@RequestParam String id_eraikina,
                                 HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/?accessDenied=true";
        }
        eraikinaRepository.deleteById(id_eraikina);
        return "redirect:/";
    }

    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return isAdminRole(role);
    }

    private boolean isAdminRole(String role) {
        return role != null && role.toLowerCase().contains("admin");
    }
}
