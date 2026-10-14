import fri.shapesge.BlokTextu;

/**
 * Trieda HraciaDoska zabezpečuje vykreslenie hracej dosky.
 * 
 * @author Emma Lujza Rothova
 */
public class HraciaDoska {
    private Bunka bunka;
    private BlokTextu text;
    
    private int pozX;
    private int pozY;
    private int x;
    
    private Bunka[][] bunkyAll;
    private BlokTextu[] oznacenia;
    
    /**
     * Konštruktor pre triedu HraciaDoska, ktorý inicializuje atribúty a ukladá do nich 
     * hodnoty zadaných formálnych parametrov.
     */
    public HraciaDoska(int pozX, int pozY, int x) {
        this.bunka = null;
        this.pozX = pozX;
        this.pozY = pozY;
        this.x = x;
    }
    
    /**
     * Setter, ktorý zabezpečuje vykreslovanie hracej plochy na základe zadanéj veľkosti
     * a následné ukladanie pozícii Buniek do dvojdimenzionálneho pola.
     */
    public void setVelkost(int velkost) {
        this.bunkyAll = new Bunka[velkost][velkost];
        this.oznacenia = new BlokTextu[2 * velkost];
        
        for (int i = 0; i < velkost; i++) {
            for (int j = 0; j < velkost; j++) {
                this.bunka = new Bunka(this.pozX + (this.x + 5) * i, this.pozY + (this.x + 5) * j, this.x);
                this.bunka.zmenFarbu("white");
                this.bunka.zobrazBunku();
                this.bunkyAll[i][j] = this.bunka;
            }
        }
        
        for (int a = 0; a < velkost; a++) {
            this.text = new BlokTextu(String.valueOf(a), (this.pozX + (this.x / 2)) + (this.x + 5) * a, this.pozY - 5);
            this.text.zmenFarbu("#9dc0eb");
            this.text.zobraz();
            this.oznacenia[a] = this.text;
        }
        
        for (int b = 0; b < velkost; b++) {
            this.text = new BlokTextu(String.valueOf(b), this.pozX - 10, (this.pozY + (this.x / 2)) + 5 + (this.x + 5) * b);
            this.text.zmenFarbu("#9dc0eb");
            this.text.zobraz();
            this.oznacenia[velkost + b] = this.text;
        }
    }
    
    /**
     * Metóda, ktorá vracia dvojdimenzionálne pole všetkých buniek tvoriacich hraciu dosku.
     */
    public Bunka[][] getListBuniek() {
        return this.bunkyAll;
    }
    
    /**
     * Metóda, ktorá zabezpečuje vymazanie hracej dosky z hry.
     */
    public void skryHraciuDosku() {
        for (Bunka[] stlpec : this.bunkyAll) {
            for (Bunka b : stlpec) {
                b.skryBunku();
            }
        }
        
        for (BlokTextu t : this.oznacenia) {
            t.skry();
        }
    }
    
    /**
     * Metóda, ktorá zabezpečuje resetovanie hracej dosky do stavu, v ktorom sa nachádzala pred začiatkom hry.
     */
    public void zobrazHraciuDosku() {
        for (Bunka[] stlpec : this.bunkyAll) {
            for (Bunka b : stlpec) {
                b.zmenFarbu("white");
                b.setObsadena(false);
                b.setZasiahnuta(false);
                b.setOzn(0);
                b.zobrazBunku();
            }
        }
        
        for (BlokTextu t : this.oznacenia) {
            t.zobraz();
        }
    }
}
