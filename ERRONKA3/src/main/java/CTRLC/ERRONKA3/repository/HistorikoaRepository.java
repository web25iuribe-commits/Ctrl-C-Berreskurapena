package CTRLC.ERRONKA3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import CTRLC.ERRONKA3.model.historikoa;

@Repository
public interface HistorikoaRepository extends JpaRepository<historikoa, Integer> {

    // JPA native query: sp_historikoa_guztiak() prozedurak egiten duena
    // (HistorikoaService-k SP deia egiten du JdbcTemplate bidez; hau fallback gisa)
    @Query(value = "SELECT * FROM historikoa ORDER BY data_aldaketa DESC",
           nativeQuery = true)
    List<historikoa> findAllOrderByDataDesc();
}
