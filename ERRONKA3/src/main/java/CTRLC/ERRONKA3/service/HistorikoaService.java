package CTRLC.ERRONKA3.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import CTRLC.ERRONKA3.model.HistorikoaStats;
import CTRLC.ERRONKA3.model.historikoa;
import CTRLC.ERRONKA3.repository.HistorikoaRepository;

@Service
public class HistorikoaService {

    private final JdbcTemplate jdbcTemplate;
    private final HistorikoaRepository historikoaRepository;

    public HistorikoaService(JdbcTemplate jdbcTemplate,
                             HistorikoaRepository historikoaRepository) {
        this.jdbcTemplate        = jdbcTemplate;
        this.historikoaRepository = historikoaRepository;
    }

    // ── Stored Procedure deia: historiko guztiak data DESC ──────────────────
    public List<historikoa> getHistorikoaGuztiak() {
        try {
            return jdbcTemplate.query("CALL sp_historikoa_guztiak()", (rs, n) -> {
                historikoa h = new historikoa();
                h.setId_hist(rs.getInt("id_hist"));
                h.setTaula(rs.getString("taula"));
                h.setEkintza(rs.getString("ekintza"));
                h.setData_aldaketa(rs.getTimestamp("data_aldaketa"));
                h.setOharra(rs.getString("oharra"));
                return h;
            });
        } catch (Exception e) {
            // SP aurkitu ezean JPA bidezko fallback-a
            return historikoaRepository.findAllOrderByDataDesc();
        }
    }

    // ── Function deiak: estatistikak ─────────────────────────────────────────
    public HistorikoaStats getStats() {
        try {
            Integer guztira   = jdbcTemplate.queryForObject(
                    "SELECT fn_historikoa_kopurua()", Integer.class);
            Integer sortuta   = jdbcTemplate.queryForObject(
                    "SELECT fn_historikoa_ekintza_kopurua('Sortu')", Integer.class);
            Integer aldatuta  = jdbcTemplate.queryForObject(
                    "SELECT fn_historikoa_ekintza_kopurua('Aldatu')", Integer.class);
            Integer ezabatuta = jdbcTemplate.queryForObject(
                    "SELECT fn_historikoa_ekintza_kopurua('Ezabatu')", Integer.class);

            return new HistorikoaStats(
                    nvl(guztira),
                    nvl(sortuta),
                    nvl(aldatuta),
                    nvl(ezabatuta));

        } catch (Exception e) {
            // Funtziorik ez badago JPA bidez kalkulatu
            return buildStatsFallback();
        }
    }

    // ── Laguntzaileak ────────────────────────────────────────────────────────

    private int nvl(Integer v) {
        return v != null ? v : 0;
    }

    private boolean matches(String ekintza, String euskara, String english) {
        if (ekintza == null) return false;
        String upper = ekintza.toUpperCase();
        return upper.contains(euskara.toUpperCase())
            || upper.contains(english.toUpperCase());
    }

    private HistorikoaStats buildStatsFallback() {
        List<historikoa> all = historikoaRepository.findAll();
        int total     = all.size();
        int sortuta   = (int) all.stream().filter(h -> matches(h.getEkintza(), "Sortu",   "INSERT")).count();
        int aldatuta  = (int) all.stream().filter(h -> matches(h.getEkintza(), "Aldatu",  "UPDATE")).count();
        int ezabatuta = (int) all.stream().filter(h -> matches(h.getEkintza(), "Ezabatu", "DELETE")).count();
        return new HistorikoaStats(total, sortuta, aldatuta, ezabatuta);
    }
}
