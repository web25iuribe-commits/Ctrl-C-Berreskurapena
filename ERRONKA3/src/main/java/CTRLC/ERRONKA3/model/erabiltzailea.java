package CTRLC.ERRONKA3.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Jakarta Persistence API. Hibernatek datu-basearekin lan egiteko erabiltzen dituen arau multzoak.

// import jakarta.persistence.Entity;
// @Entity etiketa erabili ahal izateko. EZ da klase arrunt bat, datu-baseko taula baten irudikapena da".
// Hibernatek hau ikusten duenean, badaki klase horrekin MySQL taula bat kudeatu behar duela.

// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// GenerationType eta GeneratedValue batera doaz. Erabiltzaile berri bat sortzen den bakoitzean balio bat sortuko du.

// import jakarta.persistence.Id; @Id etiketa erabili ahal izateko. Gako nagusia zein den adierazten da.

// import jakarta.persistence.Table; @Table etiketa erabili ahal izateko.
// Datu baseko zein taula erabili behar duen adieraziko zaio.

@Entity
@Table(name = "erabiltzailea")
public class erabiltzailea {
    @Id
    private String id_erab;
    private String NAN;
    private String izena;
    @Column(name = "abizena")
    private String abizena;
    private String helbide_elektronikoa;
    private String pasahitza;
    private String erabiltzaile_mota;
    private LocalDate alta_data;

    // Getter-ak eta Setter-ak (Lombok baduzu @Data jarri dezakezu gainean)

    public String getId_erab() {
        return id_erab;
    }

    public void setId_erab(String id_erab) {
        this.id_erab = id_erab;
    }

    public String getNAN() {
        return NAN;
    }

    public void setNAN(String NAN) {
        this.NAN = NAN;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public String getAbizena() {
        return abizena;
    }

    public void setAbizena(String abizena) {
        this.abizena = abizena;
    }

    public String getHelbide_elektronikoa() {
        return helbide_elektronikoa;
    }

    public void setHelbide_elektronikoa(String helbide_elektronikoa) {
        this.helbide_elektronikoa = helbide_elektronikoa;
    }

    public String getPasahitza() {
        return pasahitza;
    }

    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }

    public String getErabiltzaile_mota() {
        return erabiltzaile_mota;
    }

    public void setErabiltzaile_mota(String erabiltzaile_mota) {
        this.erabiltzaile_mota = erabiltzaile_mota;
    }

    public LocalDate getAlta_data() {
        return alta_data;
    }

    public void setAlta_data(LocalDate alta_data) {
        this.alta_data = alta_data;
    }
}
