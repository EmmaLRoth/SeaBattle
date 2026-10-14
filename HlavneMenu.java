import fri.shapesge.BlokTextu;
import fri.shapesge.StylFontu;
import fri.shapesge.Obdlznik;
import fri.shapesge.Manazer;
import javax.swing.SwingUtilities;

/**
 * Trieda HlavneMenu zabezpečuje vykreslenie menu hry s možnosťou výberu režimu hry.
 * 
 * @author Emma Lujza Rothova
 */
public class HlavneMenu {
    private static HlavneMenu instancia;
    
    private BlokTextu nazov;
    private BlokTextu textVs;
    private BlokTextu textSingle;
    private Obdlznik vs;
    private Obdlznik single;
    private Manazer manazer;
    
    private PlayerVsBot hraVS;
    private SinglePlayer hraSingle;
    
    private boolean typHry;
    private boolean zacala;
    private boolean vsAktivna;
    private boolean singleAktivna;
    
    /**
     * Konštruktor pre triedu HlavneMenu, ktorý inicializuje atribúty a zabezpečuje vykreslenie menu.
     */
    private HlavneMenu() {
        this.nazov = new BlokTextu("SeaBattle", 440, 230);
        this.nazov.zmenFarbu("#9dc0eb");
        this.nazov.zmenFont("Arial", StylFontu.BOLD, 80);
        this.nazov.zobraz();
        
        this.vs = new Obdlznik(425, 260);
        this.vs.zmenStrany(200, 100);
        this.vs.zmenFarbu("#9dc0eb");
        this.vs.zobraz();
        
        this.textVs = new BlokTextu("Player VS Bot", 475, 285);
        this.textVs.zmenFarbu("black");
        this.textVs.zmenFont("Arial", StylFontu.BOLD, 15);
        this.textVs.zobraz();
        
        this.single = new Obdlznik(630, 260);
        this.single.zmenStrany(200, 100);
        this.single.zmenFarbu("#9dc0eb");
        this.single.zobraz();
        
        this.textSingle = new BlokTextu("Single Player", 680, 285);
        this.textSingle.zmenFarbu("black");
        this.textSingle.zmenFont("Arial", StylFontu.BOLD, 15);
        this.textSingle.zobraz();
        
        this.hraVS = null;
        this.hraSingle = null;
        
        this.typHry = false;
        this.zacala = false;
        this.vsAktivna = false;
        this.singleAktivna = false;
        
        this.manazer = new Manazer();
        this.manazer.spravujObjekt(this);
    }
    
    /**
     * Metóda, ktorá implementuje návrhový vzor singleton, čo zabezpečuje vytvorenie 
     * len jednej ištancie triedy HlavneMenu.
     */
    public static HlavneMenu getInstancia() {
        if (HlavneMenu.instancia == null) {
            HlavneMenu.instancia = new HlavneMenu();
        }
        return HlavneMenu.instancia;
    }
    
    /**
     * Metóda, ktorá zabezpečuje zakliknutie režimu hry v menu a zabezpečuje jeho správne spustenie.
     */
    public void vyberSuradnice(int x, int y) {
        if (!this.zacala) {
            if (this.kontrolaZakliknutiaVS(x, y)) {
                this.skryMenu();
                this.typHry = true;
                this.vsAktivna = true;
                this.spusti();
            }
            
            if (this.kontrolaZakliknutiaSingle(x, y)) {
                this.skryMenu();
                this.typHry = false;
                this.singleAktivna = true;
                this.spusti();
            }
        }
    }
    
    /**
     * Metóda, ktorá kontroluje, či zakliknuté súradnice zodpovedajú políčku s možnosťou hry pre hráča a počítač.
     */
    public boolean kontrolaZakliknutiaVS(int x, int y) {
        if (x < 425 || x > 625) {
            return false;
        }
        
        if (y < 260 || y > 360) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Metóda, ktorá kontroluje, či zakliknuté súradnice zodpovedajú políčku s možnosťou hry pre jedného hráča.
     */
    public boolean kontrolaZakliknutiaSingle(int x, int y) {
        if (x < 630 || x > 830) {
            return false;
        }
        
        if (y < 260 || y > 360) {
            return false;
        }
       
        return true;
    }
    
    /**
     * Metóda, ktorá zabezpečuje spustenie určitého herného režimu.
     * V tejto metóde bolo použité SwingUtilities.invokeLater, ktoré zabezpečilo správne poradie 
     * jednotlivých krokov v programe. 
     * Mojim zdrojom na vytvorenie niečoho takého bola táto stránka:
     * https://www.javamex.com/tutorials/threads/invokelater.shtml
     */
    public void spusti() {
        SwingUtilities.invokeLater(new Runnable() { 
            @Override 
            public void run() { 
                if (!HlavneMenu.this.zacala) {
                    HlavneMenu.this.zacala = true;
                    if (HlavneMenu.this.typHry) {
                        HlavneMenu.this.hraVS = PlayerVsBot.getInstancia();
                        HlavneMenu.this.hraVS.resetHryVS();
                        HlavneMenu.this.hraVS.zacniHru(); 
                    } else {
                        HlavneMenu.this.hraSingle = SinglePlayer.getInstancia();
                        HlavneMenu.this.hraSingle.resetHrySingle();
                        HlavneMenu.this.hraSingle.zacniHru(); 
                    }
                } else {
                    return;
                }
            } 
        });
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.vsAktivna, 
     * ktorý reprezentuje či je spustena vs hra.
     */
    public boolean getAktivnaVs() {
        return this.vsAktivna;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.vsAktivna, 
     * ktorý reprezentuje či je spustena vs hra.
     */
    public boolean getAktivnaSingle() {
        return this.singleAktivna;
    }
    
    /**
     * Metóda, ktorá zabezpečuje vymazanie menu.
     */
    public void skryMenu() {
        this.nazov.skry();
        this.vs.skry();
        this.single.skry();
        this.textVs.skry();
        this.textSingle.skry();
    }
    
    /**
     * Metóda, ktorá zabezpečuje premazanie hodnôt hlavných atribútov triedy HlavneMenu, 
     * tak aby hra bola znova spustiteľná.
     */
    public void resetMenu() {
        this.typHry = false;
        this.zacala = false;
        this.vsAktivna = false;
        this.singleAktivna = false;
        this.manazer.spravujObjekt(this);
    }
    
    /**
     * Metóda, ktorá zabezpečuje zobrazenie menu hry.
     */
    public void zobrazMenu() {
        this.nazov.zobraz();
        this.vs.zobraz();
        this.single.zobraz();
        this.textVs.zobraz();
        this.textSingle.zobraz();
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.zacala.
     */
    public boolean getZacala() {
        return this.zacala;
    }
    
    /**
     * Metóda, ktorá zabezpečí vypnutie programu pomocou close-button.
     */
    public void vypniCele() {
        System.exit(0);
    }
}
