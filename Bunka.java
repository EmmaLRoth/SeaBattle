import fri.shapesge.Stvorec;

/**
 * Trieda Bunka zabezpečuje vytvárenie jedného štvorčeka v hracej ploche.
 * 
 * @author Emma Lujza Rothova
 */
public class Bunka {
    private Stvorec bunka;
    private int pozX;
    private int pozY;
    private int x;
    private int ozn;
    private boolean obsadena;
    private boolean zasiahnuta;
    
    /**
     * Konštruktor pre triedu Bunka, ktorý inicializuje štvorec na danej pozícii a o danej veľkosti
     * a inicializuje atribúty a ukladá do nich hodnoty zadaných formálnych parametrov. 
     */
    public Bunka(int pozX, int pozY, int x) {
        this.bunka = new Stvorec(pozX, pozY);
        this.bunka.zmenStranu(x);
        
        this.pozX = pozX;
        this.pozY = pozY;
        this.x = x;
        this.ozn = 0;
        
        this.obsadena = false;
        this.zasiahnuta = false;
    }
    
    /**
     * Metóda, ktorá volá metódu zmenFarbu() objektu Štvorec 
     * a nastaví farbu na zadanú farbu.
     */
    public void zmenFarbu(String farba) {
        this.bunka.zmenFarbu(farba);
    }
    
    /**
     * Metóda, ktorá volá metódu zobraz() objeku Štvorec
     * a zabezpečí vykreslenie Bunky do hracej plochy.
     */
    public void zobrazBunku() {
        this.bunka.zobraz();
    }
    
    /** 
     * Metóda, ktorá volá metódu skry() objektu Štvorec
     * a zabezpečí odstránenie Bunky z hracej plochy.
     */
    public void skryBunku() {
        this.bunka.skry();
    }
    
    /**
     * Setter, ktorý nastavuje hodnotu atribútu this.obsadena.
     */
    public void setObsadena(boolean o) {
        this.obsadena = o;
    }
    
    /**
     * Setter, ktorý nastavuje hodnotu atribútu this.zasiahnuta.
     */
    public void setZasiahnuta(boolean z) {
        this.zasiahnuta = z;
    }
    
    /**
     * Setter, ktorý nastavuje hodnotu atribútu this.ozn.
     */
    public void setOzn(int ozn) {
        this.ozn = ozn;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.pozX.
     */
    public int getPoziciaX() {
        return this.pozX;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.pozY.
     */
    public int getPoziciaY() {
        return this.pozY;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.x predstavujúceho veľkosť Bunky.
     */
    public int getStranuX() {
        return this.x;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.obsadena.
     */
    public boolean getObsadena() {
        return this.obsadena;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.zasiahnuta.
     */
    public boolean getZasiahnuta() {
        return this.zasiahnuta;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.ozn.
     */
    public int getOzn() {
        return this.ozn;
    }
}
