package CTRLC.ERRONKA3.model;

public class HistorikoaStats {

    private final int guztira;
    private final int sortuta;
    private final int aldatuta;
    private final int ezabatuta;

    public HistorikoaStats(int guztira, int sortuta, int aldatuta, int ezabatuta) {
        this.guztira   = guztira;
        this.sortuta   = sortuta;
        this.aldatuta  = aldatuta;
        this.ezabatuta = ezabatuta;
    }

    public int getGuztira()   { return guztira;   }
    public int getSortuta()   { return sortuta;   }
    public int getAldatuta()  { return aldatuta;  }
    public int getEzabatuta() { return ezabatuta; }
}
