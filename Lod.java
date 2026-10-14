import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Trieda Lod zabezpečuje vykreslovanie lodnej jednodky do hracej plochy.
 * 
 * @author Emma Lujza Rothova
 */
public class Lod {
    private Bunka part;
    private TypyLode typ;
    
    private int pozX;
    private int pozY;
    private int x;
    
    private ArrayList<Bunka> zoznamBuniek;
    private boolean celaZasiahnuta;
   
    
    /**
     * Konštruktor pre triedu Lod, ktorý inicializuje atribúty a ukladá do nich hodnoty zadaných formálnych parametrov.
     */
    public Lod(int pozX, int pozY, int x, int riadok, int stlpec) {
        this.part = null;
        this.pozX = pozX;
        this.pozY = pozY;
        this.x = x;
        this.zoznamBuniek = new ArrayList<Bunka>();
        this.celaZasiahnuta = false;
    }
    
    /**
     * Setter, ktorý nastavuje typ lode podľa zadaného formálneho parametra a určuje otočenie 
     * lode buď horizontálne alebo vertikálne a následne vytvorenie objektu Loď.
     */
    public void setTypLode(TypyLode typ, boolean horizontal) {
        this.typ = typ;
        if (horizontal) {
            for (int i = 0; i < this.typ.getDlzkaLode(); i++) {
                this.part = new Bunka(this.pozX + (this.x + 5) * i, this.pozY, this.x);
                this.part.zmenFarbu(this.typ.getFarbaLode());
                this.part.setObsadena(true);
                this.zoznamBuniek.add(this.part);
            }
        } else {
            for (int i = 0; i < this.typ.getDlzkaLode(); i++) {
                this.part = new Bunka(this.pozX, this.pozY + (this.x + 5) * i, this.x);
                this.part.zmenFarbu(this.typ.getFarbaLode());
                this.part.setObsadena(true);
                this.zoznamBuniek.add(this.part);
            }
        }
    }
    
    /**
     * Metóda, ktorá v zabezpečí vykreslenie Lode do hracej plochy.
     */
    public void zobrazLodku() {
        for (Bunka cast : this.zoznamBuniek) {
            cast.zobrazBunku();
        }
    }
    
    /**
     * Metóda, ktorá v zabezpečí vymazanie Lode z hracej plochy.
     */
    public void skryLodku() {
        for (Bunka cast : this.zoznamBuniek) {
            cast.skryBunku();
        }
    }
    
    /**
     * Metóda, ktorá v zabezpečí prefarbenie lode, ak bola celá zasiahnutá.
     */
    public void zasiahnutaLodka() {
        if (this.celaZasiahnuta) {
            for (Bunka policko : this.zoznamBuniek) {
                policko.zmenFarbu("#f72040");
            }
        }
    }
    
    /**
     * Getter, ktorý vracia informáciu o tom, či loď bolá celá zasiahnutá.
     */
    public boolean getCelaZasiahnuta() {
        int zasahySpolu = 0;
        
        for (Bunka cast : this.zoznamBuniek) {
            if (cast.getZasiahnuta()) {
                zasahySpolu++;
            }
        }
        
        if (zasahySpolu == this.typ.getDlzkaLode()) {
            this.celaZasiahnuta = true;
            return true;
        }
        
        return false;
    }
    
    /**
     * Getter, ktorý vracia neupraviteľný zoznam buniek, z ktorých sa loď skladá.
     * Mojim zdrojom na vytvorenie takéhoto zoznamu bola táto stránka:
     * https://www.geeksforgeeks.org/collections-unmodifiablelist-method-in-java-with-examples/
     */
    public List<Bunka> getZoznamObsadenychBuniek() {
        List<Bunka> b = this.zoznamBuniek;
        return Collections.unmodifiableList(b);
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.typ.
     */
    public TypyLode getTypLode() {
        return this.typ;
    }
}
