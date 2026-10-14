import javax.swing.JOptionPane;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import fri.shapesge.Manazer;
import fri.shapesge.Obdlznik;
import fri.shapesge.BlokTextu;
import fri.shapesge.StylFontu;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 * Trieda PlayerVsBot zabezpečuje definovanie základnej hernej logiky pre hru medzi hráčom a počítačom.
 * 
 * @author Emma Lujza Rothova
 */
public class PlayerVsBot {
    private static PlayerVsBot instancia;
    
    private Hrac player;
    private Pocitac bot;
    private Manazer manazer;
    private Obdlznik obdlznik;
    private BlokTextu text;
    private BlokTextu hrac;
    private BlokTextu pc;
    
    private boolean hracovTah;
    private boolean zacala;
    
    private int x;
    private int y;
    private int i;
    private int zapamatajVS;
    private int pocetUspechoPlayer;
    private int pocetUspechoBot;
    private int pocetKlikov;
    
    private HernyVysledok vysledokHry;
    
    private ArrayList<Zasahy> strely;
    private ArrayList<String> zaznamHry;
    private BlokTextu[] zapisky1VS;
    private BlokTextu[] zapisky2VS;
    
    /**
     * Konštruktor pre triedu PlayerVsBot, ktorý inicializuje atribúty.
     */
    private PlayerVsBot() {
        this.player = Hrac.getInstancia();
        
        this.bot = Pocitac.getInstancia();
        
        this.hracovTah = true;
        this.zacala = false;
        
        this.manazer = new Manazer();
        this.manazer.spravujObjekt(this);
        
        this.i = 0;
        this.pocetUspechoPlayer = 0;
        this.pocetUspechoBot = 0;
        this.pocetKlikov = 0;
        this.zapamatajVS = 0;
        
        this.vysledokHry = HernyVysledok.NEROZHODNUTA;
        
        this.obdlznik = new Obdlznik(1010, 25);
        this.obdlznik.zmenStrany(235, 445);
        this.obdlznik.zmenFarbu("white");
        this.obdlznik.zobraz();
        
        this.text = new BlokTextu("Záznam hry: ", 1020, 45);
        this.text.zmenFarbu("black");
        this.text.zmenFont("Arial", StylFontu.BOLD, 15);
        this.text.zobraz();
        
        this.hrac = new BlokTextu("Hráč", 1020, 65);
        this.hrac.zmenFarbu("#E59500");
        this.hrac.zmenFont("Arial", StylFontu.BOLD, 15);
        this.hrac.zobraz();
        
        this.pc = new BlokTextu("Počítač", 1075, 65);
        this.pc.zmenFarbu("#944654");
        this.pc.zmenFont("Arial", StylFontu.BOLD, 15);
        this.pc.zobraz();
        
        this.strely = new ArrayList<Zasahy>();
        this.zaznamHry = new ArrayList<String>();
        this.zapisky1VS = new BlokTextu[25];
        this.zapisky2VS = new BlokTextu[25];
    }
    
    /**
     * Metóda, ktorá implementuje návrhový vzor singleton, čo zabezpečuje vytvorenie 
     * len jednej ištancie triedy PlayerVsBot.
     */
    public static PlayerVsBot getInstancia() {
        if (PlayerVsBot.instancia == null) {
            PlayerVsBot.instancia = new PlayerVsBot();
        } 
        return PlayerVsBot.instancia;
    }
    
    /**
     * Metóda, ktorá zabezpečuje spustenie hry pre hru medzi hráčom a počítačom hru.
     */
    public void zacniHru() {
        if (!this.zacala) {
            this.zacala = true;
            this.bot.umiestniLodeRandom();
            this.bot.setEnemy(this.player);
            
            this.player.setEnemy(this.bot);
            int playerRozmiestnenieLodi = JOptionPane.showConfirmDialog(null, "Chceš si loďky umiestniť sám?");
            
            if (playerRozmiestnenieLodi == 0) {
                this.player.umiestniLodeSam();
            } else if (playerRozmiestnenieLodi == 1) {
                this.player.umiestniLodeRandom();
                int spokojnost = JOptionPane.showConfirmDialog(null, "Si spokojný/á s rozmiestnením?");
                while (spokojnost == 1) {
                    this.player.umiestniLodeRandom();
                    spokojnost = JOptionPane.showConfirmDialog(null, "Si už spokojný/á s rozmiestnením?");
                }
            } else {
                return;
            }
            
            this.bot.setZasahPC();
        } else {
            return;
        }
    }
    
    /**
     * Metóda, ktorá zabezpečuje zaklikávanie pozícii na hracej doske počítača.
     */
    public void vyberSuradnice(int x, int y) {
        if (this.bot.getDoskaPC() == null) {
            return;
        }
        
        if (HlavneMenu.getInstancia().getAktivnaSingle()) {
            return;
        }
        
        if (!HlavneMenu.getInstancia().getZacala()) {
            return;
        }
        
        this.x = x;
        this.y = y;
        this.pocetKlikov++;
        this.tahPlayer();
    }
    
    /**
     * Metóda, ktorá zabezpečuje ťah hráča v hre s kontrolou konca hry.
     * Pre správne volanie konca hry bolo použité SwingUtilities.invokeLater, ktoré zabezpečilo 
     * správne poradie jednotlivých krokov v programe. 
     * Mojim zdrojom na vytvorenie niečoho takého bola táto stránka:
     * https://www.javamex.com/tutorials/threads/invokelater.shtml
     */
    public void tahPlayer() {
        if (this.hracovTah) {
            if (this.player.mozeTrafit(this.x, this.y)) {
                this.player.najdenyZasah(this.x, this.y);
                if (!this.player.getZoznamZasahov().get(this.player.getZoznamZasahov().size() - 1).getUspech()) {
                    if (this.pocetKlikov == this.player.getZoznamZasahov().size()) {
                        this.strely.add(this.player.getZoznamZasahov().get(this.player.getZoznamZasahov().size() - 1));
                        this.vypisNaPlochuVS();
                        this.hracovTah = false;
                        this.tahBot();
                    } else {
                        this.pocetKlikov = this.player.getZoznamZasahov().size();
                    }
                } else {
                    if (this.pocetKlikov == this.player.getZoznamZasahov().size()) {
                        this.strely.add(this.player.getZoznamZasahov().get(this.player.getZoznamZasahov().size() - 1));
                        this.vypisNaPlochuVS();
                        this.pocetUspechoPlayer++;
                    } else {
                        this.pocetKlikov = this.player.getZoznamZasahov().size();
                    }
                }
            } else {
                return;
            }
        } else {
            return;
        }
        
        SwingUtilities.invokeLater(new Runnable() { 
            @Override
            public void run() {
                if (PlayerVsBot.this.kontrolaKoncaHry()) {
                    PlayerVsBot.this.ukoncenie();
                    return;
                }
            }
        });
    }
    
    /**
     * Metóda, ktorá zabezpečuje ťah počítača v hre s kontrolou konca hry.
     * Pre správne volanie konca hry bolo použité SwingUtilities.invokeLater, ktoré zabezpečilo 
     * správne poradie jednotlivých krokov v programe. 
     * Mojim zdrojom na vytvorenie niečoho takého bola táto stránka:
     * https://www.javamex.com/tutorials/threads/invokelater.shtml
     */
    public void tahBot() {
        if (!this.hracovTah) { 
            while (this.i < this.bot.getZoznamZasahov().size() && this.bot.getZoznamZasahov().get(this.i).getUspech()) { 
                this.bot.vykresliZasahy(this.i);
                this.strely.add(this.bot.getZoznamZasahov().get(this.i));
                this.vypisNaPlochuVS();
                this.pocetUspechoBot++; 
                this.i++;
                
                if (this.kontrolaKoncaHry()) {
                    return;
                }
            } 
            if (this.i < this.bot.getZoznamZasahov().size() && !this.bot.getZoznamZasahov().get(this.i).getUspech()) { 
                this.bot.vykresliZasahy(this.i);
                this.strely.add(this.bot.getZoznamZasahov().get(this.i));
                this.vypisNaPlochuVS();
                this.i++; 
            } 
            this.hracovTah = true; 
        } 
        
        SwingUtilities.invokeLater(new Runnable() { 
            @Override
            public void run() {
                if (PlayerVsBot.this.kontrolaKoncaHry()) {
                    PlayerVsBot.this.ukoncenie();
                    return;
                }
            }
        });
    }
    
    /**
     * Metóda, ktorá zabezpečuje kontrolu konca hry, pri dosiahnutí 20 úspešných zásahov - zasiahnutie všetkých lodí.
     */
    public boolean kontrolaKoncaHry() {
        if (this.pocetUspechoPlayer == 20 || this.pocetUspechoBot == 20) {
            return true;
        }
        return false;
    }
    
    /**
     * Metóda, ktorá zabezpečuje ukončenie hry a zabezpečuje zápis záznamu hry do súboru.
     * Pre správne volanie metódy pre zápis záznamu hry do súboru bolo použité try...catch, 
     * ktoré zabezpečuje hladanie chýb v danom bloku kódu.
     * Mojim zdrojom na vytvorenie niečoho takého bola táto stránka:
     * https://www.w3schools.com/java/java_try_catch.asp
     */
    public void ukoncenie() {
        if (this.pocetUspechoPlayer == 20 || this.pocetUspechoBot == 20) {
            if (this.pocetUspechoPlayer == 20) {
                JOptionPane.showMessageDialog(null, "KONIEC HRY! VYHRAL SI :D");
                this.vysledokHry = HernyVysledok.VYHRA_HRACA;
            } else {
                JOptionPane.showMessageDialog(null, "KONIEC HRY! PRERAL SI :(");
                this.vysledokHry = HernyVysledok.VYHRA_POCITACA;
            }
            
            try { 
                this.zapisDoSuboru(); 
            } catch (Exception e) { 
                System.out.println("chyba");
            }
            
            this.skryVsetko();
            HlavneMenu menu = HlavneMenu.getInstancia();
            menu.resetMenu();
            menu.zobrazMenu();
        }
    }
    
    /**
     * Metóda, ktorá zabezpečuje výpis záznamu hry na hraciu plochu a jeho aktualizovanie pri každom zásahu.
     */
    public void vypisNaPlochuVS() {
        if (this.zapamatajVS >= 25) {
            this.zapisky1VS[0].skry();
            this.zapisky2VS[0].skry();
            for (int k = 0; k < this.zapisky1VS.length - 1; k++) {
                this.zapisky1VS[k] = this.zapisky1VS[k + 1];
                this.zapisky1VS[k].zmenPolohu(1020, 90 + (15 * k));
            }
            
            this.zapisky1VS[this.zapisky1VS.length - 1] = null;
            
            for (int l = 0; l < this.zapisky2VS.length - 1; l++) {
                this.zapisky2VS[l] = this.zapisky2VS[l + 1];
                this.zapisky2VS[l].zmenPolohu(1180, 90 + (15 * l));
            }
            
            this.zapisky2VS[this.zapisky2VS.length - 1] = null;
            
            String z = "riadok:  " + this.strely.get(this.zapamatajVS).getRiadok() + "    stĺpec:  " + this.strely.get(this.zapamatajVS).getStlpec();
            BlokTextu vypisVS = new BlokTextu(z, 1020, 90 + 15 * 24);
            BlokTextu uspechVS = null;
                    
            if (this.strely.get(this.zapamatajVS).getUspech()) {
                z = z + "    ZÁSAH";
                uspechVS = new BlokTextu("ZÁSAH", 1180, 90 + 15 * 24);
            } else {
                z = z + "    MIMO";
                uspechVS = new BlokTextu("MIMO", 1180, 90 + 15 * 24);
            }
    
            if (this.strely.get(this.zapamatajVS).getOznPolicka().equals("H")) {
                z = "Hráč:       " + z;
                vypisVS.zmenFarbu("#E59500");
                uspechVS.zmenFarbu("#E59500");
            } else {
                z = "Počítač:    " + z;
                vypisVS.zmenFarbu("#944654");
                uspechVS.zmenFarbu("#944654");
            }
    
            vypisVS.zmenFont("Arial", StylFontu.BOLD, 15);
            uspechVS.zmenFont("Arial", StylFontu.BOLD, 15);
    
            vypisVS.zobraz();
            uspechVS.zobraz();
            this.zapamatajVS++;
            this.zapisky1VS[this.zapisky1VS.length - 1] = vypisVS;
            this.zapisky2VS[this.zapisky2VS.length - 1] = uspechVS;
            this.zaznamHry.add(z);
            return;
        }
        
        String z = "riadok:  " + this.strely.get(this.zapamatajVS).getRiadok() + "    stĺpec:  " + this.strely.get(this.zapamatajVS).getStlpec();
        BlokTextu vypisVS = new BlokTextu(z, 1020, 90 + 15 * (this.zapamatajVS % 25));
        BlokTextu uspechVS = null;
                
        if (this.strely.get(this.zapamatajVS).getUspech()) {
            z = z + "    ZÁSAH";
            uspechVS = new BlokTextu("ZÁSAH", 1180, 90 + 15 * (this.zapamatajVS % 25));
        } else {
            z = z + "    MIMO";
            uspechVS = new BlokTextu("MIMO", 1180, 90 + 15 * (this.zapamatajVS % 25));
        }

        if (this.strely.get(this.zapamatajVS).getOznPolicka().equals("H")) {
            z = "Hráč:       " + z;
            vypisVS.zmenFarbu("#E59500");
            uspechVS.zmenFarbu("#E59500");
        } else {
            z = "Počítač:    " + z;
            vypisVS.zmenFarbu("#944654");
            uspechVS.zmenFarbu("#944654");
        }

        vypisVS.zmenFont("Arial", StylFontu.BOLD, 15);
        uspechVS.zmenFont("Arial", StylFontu.BOLD, 15);

        vypisVS.zobraz();
        uspechVS.zobraz();
        this.zapisky1VS[this.zapamatajVS % 25] = vypisVS;
        this.zapisky2VS[this.zapamatajVS % 25] = uspechVS;
        this.zaznamHry.add(z);
        this.zapamatajVS++;
    }
    
    /**
     * Metóda, ktorá zabezpečuje výpis záznamu hry do súboru.
     */
    public void zapisDoSuboru() throws IOException {
        File subor = new File("zaznamHry.txt");
        PrintWriter zapisovac = new PrintWriter(subor);
        Bunka[][] novyZoznam = new Bunka[10][10];
        
        zapisovac.println("Hráčovo umiestnenie lodí: ");
        for (int k = 0; k < 10; k++) {
            for (int l = 0; l < 10; l++) {
                zapisovac.print("[" + this.player.getDoskaHrac()[l][k].getOzn() + "]");
            }
            zapisovac.println();
        }
        
        zapisovac.println();
        
        zapisovac.println("Počítačovo umiestnenie lodí: ");
        for (int a = 0; a < 10; a++) {
            for (int b = 0; b < 10; b++) {
                zapisovac.print("[" + this.bot.getDoskaPC()[b][a].getOzn() + "]");
            }
            zapisovac.println();
        }
        
        zapisovac.println();
        
        if (this.vysledokHry.equals(HernyVysledok.VYHRA_HRACA)) {
            zapisovac.println("Výsledok hry: vyhral hráč");
        } else {
            zapisovac.println("Výsledok hry: vyhral počítač");
        }
        
        zapisovac.println();
        zapisovac.println("Záznam hry:");
        
        for (String priebeh : this.zaznamHry) {
            zapisovac.println(priebeh);
        }
        zapisovac.close();
    }
    
    /**
     * Metóda, ktorá zabezpečuje vymazanie hry pre hru medzi hračom a počítačom.
     */
    public void skryVsetko() {
        this.obdlznik.skry();
        this.text.skry();
        this.hrac.skry();
        this.pc.skry();
        
        for (BlokTextu zapis1VS : this.zapisky1VS) {
            if (zapis1VS != null) { 
                zapis1VS.skry();
            }
        }
        
        for (BlokTextu zapis2VS : this.zapisky2VS) {
            if (zapis2VS != null) { 
                zapis2VS.skry();
            }
        }
        
        for (Lod l : this.player.getLodky()) {
            l.skryLodku();
        }
        
        for (Lod l : this.bot.getLodky()) {
            l.skryLodku();
        }
        
        this.player.resetujHrac();
        this.bot.resetujPocitac();
        this.zaznamHry.clear();
        this.strely.clear();
    }
    
    /**
     * Metóda, ktorá zabezpečuje premazanie hodnôt hlavných atribútov triedy PlayerVsBot, 
     * tak aby hra bola znova spustiteľná.
     */
    public void resetHryVS() {
        this.player.zobrazDosku();
        this.bot.zobrazDosku();
        this.obdlznik.zobraz();
        this.text.zobraz();
        this.hrac.zobraz();
        this.pc.zobraz();    
        this.pocetUspechoPlayer = 0;
        this.pocetUspechoBot = 0;
        this.pocetKlikov = 0;
        this.zapamatajVS = 0;
        this.i = 0;
        this.zacala = false;
        this.hracovTah = true;
        this.vysledokHry = HernyVysledok.NEROZHODNUTA;
    }
}
