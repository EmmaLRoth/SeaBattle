import fri.shapesge.Manazer;
import fri.shapesge.Obdlznik;
import fri.shapesge.BlokTextu;
import fri.shapesge.StylFontu;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 * Trieda SinglePlayer zabezpečuje definovanie základnej hernej logiky pre hru pre jedného hráča.
 * 
 * @author Emma Lujza Rothova
 */
public class SinglePlayer {
    private static SinglePlayer instancia;
    
    private HraciaDoska doska;
    private Manazer manazer;
    private Obdlznik obdlznik;
    private BlokTextu text;
    private BlokTextu hrac;
    private String meno;
    private HernyVysledok vysledokHry;
    
    private int uspech;
    private int zapamataj;
    
    private ArrayList<TypyLode> typy;
    private ArrayList<Lod> lodky;
    private ArrayList<Zasahy> zoznamZasahov;
    private ArrayList<String> zaznamHry;
    private BlokTextu[] zapisky1;
    private BlokTextu[] zapisky2;
    
    /**
     * Konštruktor pre triedu SinglePlayer, ktorý inicializuje atribúty.
     */
    private SinglePlayer() {
        this.meno = "Jednooký Hrom";
        this.vysledokHry = HernyVysledok.NEROZHODNUTA;
        
        this.doska = new HraciaDoska(400, 25, 40);
        this.doska.setVelkost(10);
        
        this.uspech = 0;
        this.zapamataj = 0;

        this.lodky = new ArrayList<Lod>();
        this.zoznamZasahov = new ArrayList<Zasahy>();
        
        this.typy = new ArrayList<TypyLode>();
        for (int i = 0; i < 4; i++) {
            this.typy.add(TypyLode.CLN);
        }
        for (int i = 0; i < 3; i++) {
            this.typy.add(TypyLode.HLIADKOVA_LOD);
        }
        for (int i = 0; i < 2; i++) {
            this.typy.add(TypyLode.TORPEDOBOREC);
        }
        this.typy.add(TypyLode.LIETADLOVA_LOD);
        
        this.manazer = new Manazer();
        this.manazer.spravujObjekt(this);
        
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
        
        this.zaznamHry = new ArrayList<String>();
        this.zapisky1 = new BlokTextu[25];
        this.zapisky2 = new BlokTextu[25];
    }
    
    /**
     * Metóda, ktorá implementuje návrhový vzor singleton, čo zabezpečuje vytvorenie 
     * len jednej ištancie triedy SinglePlayer.
     */
    public static SinglePlayer getInstancia() {
        if (SinglePlayer.instancia == null) {
            SinglePlayer.instancia = new SinglePlayer();
        }
        return SinglePlayer.instancia;
    }
    
    /**
     * Metóda, ktorá zabezpečuje umiestnenie a vykreslenie lode na hraciu dosku.
     */
    public void umiestniLod(int riadok, int stlpec, TypyLode typ, boolean horizontal) {
        Bunka[][] list = this.doska.getListBuniek();
        int pozX = list[stlpec][riadok].getPoziciaX();
        int pozY = list[stlpec][riadok].getPoziciaY();
        int x = list[stlpec][riadok].getStranuX();
        
        Lod lodka = new Lod(pozX, pozY, x, riadok, stlpec);
        lodka.setTypLode(typ, horizontal);
        this.lodky.add(lodka);
    }
    
    /**
     * Metóda, ktorá zabezpečuje umiestnenie všetkých lodí na náhodných pozíciách,
     * tak aby nedošlo k ich prekrývaniu.
     */
    public void umiestniLodeRandom() {
        if (this.doska == null) {
            return;
        }
        
        Random riadok = new Random();
        Random stlpec = new Random();
        Random horizontal = new Random();
        
        int i = 0;
            
        while (i < this.typy.size()) {
            boolean kontrola = true;
            boolean otocenie = horizontal.nextBoolean();

            if (otocenie) {
                int rlodka = riadok.nextInt(10);
                int slodka = stlpec.nextInt(10);

                if ((this.typy.get(i).equals(TypyLode.HLIADKOVA_LOD) && slodka >= 9) || (this.typy.get(i).equals(TypyLode.TORPEDOBOREC) && slodka >= 8) || (this.typy.get(i).equals(TypyLode.LIETADLOVA_LOD) && slodka >= 7)) {
                    continue;
                } else {
                    switch (this.typy.get(i).getSlovo()) {
                        case "cln":
                            if (this.doska.getListBuniek()[slodka][rlodka].getObsadena()) {
                                kontrola = false;
                            }
                            break;

                        case "hliadkovaLod":
                            if (this.doska.getListBuniek()[slodka][rlodka].getObsadena() || this.doska.getListBuniek()[slodka + 1][rlodka].getObsadena()) {
                                kontrola = false;
                            }
                            break;
                                    
                        case "torpedoborec":
                            if (this.doska.getListBuniek()[slodka][rlodka].getObsadena() || this.doska.getListBuniek()[slodka + 1][rlodka].getObsadena() || this.doska.getListBuniek()[slodka + 2][rlodka].getObsadena()) {
                                kontrola = false;
                            }
                            break;
                                
                        case "lietadlovaLod":
                            if (this.doska.getListBuniek()[slodka][rlodka].getObsadena() || this.doska.getListBuniek()[slodka + 1][rlodka].getObsadena() || this.doska.getListBuniek()[slodka + 2][rlodka].getObsadena() || this.doska.getListBuniek()[slodka + 3][rlodka].getObsadena()) {
                                kontrola = false;
                            }
                            break;

                        default:
                            System.out.println("chyba1");
                    }

                    if (kontrola) {
                        this.umiestniLod(rlodka, slodka, this.typy.get(i), otocenie);
                        switch (this.typy.get(i).getSlovo()) {
                            case "cln":
                                this.doska.getListBuniek()[slodka][rlodka].setObsadena(true);
                                
                                this.doska.getListBuniek()[slodka][rlodka].setOzn(1);
                                break;

                            case "hliadkovaLod":
                                this.doska.getListBuniek()[slodka][rlodka].setObsadena(true);
                                this.doska.getListBuniek()[slodka + 1][rlodka].setObsadena(true);
                                
                                this.doska.getListBuniek()[slodka][rlodka].setOzn(2);
                                this.doska.getListBuniek()[slodka + 1][rlodka].setOzn(2);
                                break;

                            case "torpedoborec":
                                this.doska.getListBuniek()[slodka][rlodka].setObsadena(true);
                                this.doska.getListBuniek()[slodka + 1][rlodka].setObsadena(true);
                                this.doska.getListBuniek()[slodka + 2][rlodka].setObsadena(true);
                                
                                this.doska.getListBuniek()[slodka][rlodka].setOzn(3);
                                this.doska.getListBuniek()[slodka + 1][rlodka].setOzn(3);
                                this.doska.getListBuniek()[slodka + 2][rlodka].setOzn(3);
                                break;
                                        
                            case "lietadlovaLod":
                                this.doska.getListBuniek()[slodka][rlodka].setObsadena(true);
                                this.doska.getListBuniek()[slodka + 1][rlodka].setObsadena(true);
                                this.doska.getListBuniek()[slodka + 2][rlodka].setObsadena(true);
                                this.doska.getListBuniek()[slodka + 3][rlodka].setObsadena(true);
                                
                                this.doska.getListBuniek()[slodka][rlodka].setOzn(4);
                                this.doska.getListBuniek()[slodka + 1][rlodka].setOzn(4);
                                this.doska.getListBuniek()[slodka + 2][rlodka].setOzn(4);
                                this.doska.getListBuniek()[slodka + 3][rlodka].setOzn(4);
                                break;
    
                            default:
                                System.out.println("chyba2");
                        }
                    } else {
                        continue;
                    }
                }
            } else {
                int rlodka = riadok.nextInt(10);
                int slodka = stlpec.nextInt(10);

                if ((this.typy.get(i).equals(TypyLode.HLIADKOVA_LOD) && rlodka >= 9) || (this.typy.get(i).equals(TypyLode.TORPEDOBOREC) && rlodka >= 8) || (this.typy.get(i).equals(TypyLode.LIETADLOVA_LOD) && rlodka >= 7)) {
                    continue;
                } else {
                    switch (this.typy.get(i).getSlovo()) {
                        case "cln":
                            if (this.doska.getListBuniek()[slodka][rlodka].getObsadena()) {
                                kontrola = false;
                            }
                            break;

                        case "hliadkovaLod":
                            if (this.doska.getListBuniek()[slodka][rlodka].getObsadena() || this.doska.getListBuniek()[slodka][rlodka + 1].getObsadena()) {
                                kontrola = false;
                            }
                            break;
                                    
                        case "torpedoborec":
                            if (this.doska.getListBuniek()[slodka][rlodka].getObsadena() || this.doska.getListBuniek()[slodka][rlodka + 1].getObsadena() || this.doska.getListBuniek()[slodka][rlodka + 2].getObsadena()) {
                                kontrola = false;
                            }
                            break;
                                
                        case "lietadlovaLod":
                            if (this.doska.getListBuniek()[slodka][rlodka].getObsadena() || this.doska.getListBuniek()[slodka][rlodka + 1].getObsadena() || this.doska.getListBuniek()[slodka][rlodka + 2].getObsadena() || this.doska.getListBuniek()[slodka][rlodka + 3].getObsadena()) {
                                kontrola = false;
                            }
                            break;
                                    
                        default:
                            System.out.println("chyba3");
                    }

                    if (kontrola) {
                        this.umiestniLod(rlodka, slodka, this.typy.get(i), otocenie);
                        switch (this.typy.get(i).getSlovo()) {
                            case "cln":
                                this.doska.getListBuniek()[slodka][rlodka].setObsadena(true);
                                this.doska.getListBuniek()[slodka][rlodka].setOzn(1);
                                
                                break;
                                        
                            case "hliadkovaLod":
                                this.doska.getListBuniek()[slodka][rlodka].setObsadena(true);
                                this.doska.getListBuniek()[slodka][rlodka + 1].setObsadena(true);
                                
                                this.doska.getListBuniek()[slodka][rlodka].setOzn(2);
                                this.doska.getListBuniek()[slodka][rlodka + 1].setOzn(2);
                                break;
                                        
                            case "torpedoborec":
                                this.doska.getListBuniek()[slodka][rlodka].setObsadena(true);
                                this.doska.getListBuniek()[slodka][rlodka + 1].setObsadena(true);
                                this.doska.getListBuniek()[slodka][rlodka + 2].setObsadena(true);
                                
                                this.doska.getListBuniek()[slodka][rlodka].setOzn(3);
                                this.doska.getListBuniek()[slodka][rlodka + 1].setOzn(3);
                                this.doska.getListBuniek()[slodka][rlodka + 2].setOzn(3);
                                break;
                                        
                            case "lietadlovaLod":
                                this.doska.getListBuniek()[slodka][rlodka].setObsadena(true);
                                this.doska.getListBuniek()[slodka][rlodka + 1].setObsadena(true);
                                this.doska.getListBuniek()[slodka][rlodka + 2].setObsadena(true);
                                this.doska.getListBuniek()[slodka][rlodka + 3].setObsadena(true);
                                
                                this.doska.getListBuniek()[slodka][rlodka].setOzn(4);
                                this.doska.getListBuniek()[slodka][rlodka + 1].setOzn(4);
                                this.doska.getListBuniek()[slodka][rlodka + 2].setOzn(4);
                                this.doska.getListBuniek()[slodka][rlodka + 3].setOzn(4);
                                break;
                                        
                            default:
                                System.out.println("chyba4");
                        }
                    } else {
                        continue;
                    }
                }
            }
            i++;
        }
    }
    
    /**
     * Metóda, ktorá zabezpečuje zaklikávanie pozícii na hracej doske.
     */
    public void vyberSuradnice(int x, int y) {
        if (this.doska == null) {
            return;
        }
        
        if (this.lodky.isEmpty()) {
            return;
        }
        
        if (HlavneMenu.getInstancia().getAktivnaVs()) {
            return;
        }
        
        if (!HlavneMenu.getInstancia().getZacala()) {
            return;
        }
        
        if (this.mozeTrafit(x, y)) {
            this.najdenyZasah(x, y);
        }
    }
    
    /**
     * Metóda, ktorá zaznamenáva zakliknuté zásahy a zabezpečuje ich správne 
     * vykreslanie na hracej doske, spolu aj s kontrolou konca hry.
     * Pre správne volanie konca hry bolo použité SwingUtilities.invokeLater, ktoré zabezpečilo 
     * správne poradie jednotlivých krokov v programe. 
     * Mojim zdrojom na vytvorenie niečoho takého bola táto stránka:
     * https://www.javamex.com/tutorials/threads/invokelater.shtml
     */
    public void najdenyZasah(int x, int y) {
        if (this.uspech != 20) {
            boolean kontrola1 = false;
            int indexR = 0;
            int indexS = 0;
            
            for (Bunka[] i : this.doska.getListBuniek()) {
                for (Bunka umiestnenia : i) {
                    if ((x >= umiestnenia.getPoziciaX() && x <= umiestnenia.getPoziciaX() + 40) && (y >= umiestnenia.getPoziciaY() && y <= umiestnenia.getPoziciaY() + 40)) {
                        indexR = (umiestnenia.getPoziciaY() - 25) / (40 + 5);    
                        indexS = (umiestnenia.getPoziciaX() - 400) / (40 + 5);
                        if (umiestnenia.getObsadena()) {
                            kontrola1 = true;
                            this.nastavZasah(x, y);
                        } else {
                            kontrola1 = false;
                        }
                        break;
                    }
                }
            }
            
            if (!this.doska.getListBuniek()[indexS][indexR].getZasiahnuta()) {
                this.doska.getListBuniek()[indexS][indexR].setZasiahnuta(true);
                if (kontrola1) {
                    this.doska.getListBuniek()[indexS][indexR].zmenFarbu("#f5697e");
                    this.uspech++;
                    for (Lod ship : this.lodky) {
                        if (ship.getCelaZasiahnuta()) {
                            ship.zobrazLodku();
                            ship.zasiahnutaLodka();
                        }
                    }
                    this.zoznamZasahov.add(new Zasahy(indexR, indexS, "H", true));
                    this.vypisNaPlochuSingle();
                } else {
                    this.doska.getListBuniek()[indexS][indexR].zmenFarbu("#2081f7");
                    this.zoznamZasahov.add(new Zasahy(indexR, indexS, "H", false));
                    this.vypisNaPlochuSingle();
                }
            }
        } 
        
        SwingUtilities.invokeLater(new Runnable() { 
            @Override
            public void run() {
                if (SinglePlayer.this.kontrolaKoncaHry()) {
                    SinglePlayer.this.ukoncenie();
                    return;
                }
            }
        });
    }
    
    /**
     * Metóda, ktorá kontroluje, či zakliknuté súradnice zodpovedajú hracej ploche, na ktorej je zaklikávanie dovolené.
     */
    public boolean mozeTrafit(int x, int y) {
        if (x < 400 || x > 845) {
            return false;
        }
        
        if (y < 25 || y > 470) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Metóda, ktorá zabezpečuje zaznamenanie úspešného zásahu na lodi.
     */
    public void nastavZasah(int x, int y) {
        for (Lod lodka : this.lodky) {
            for (Bunka policko : lodka.getZoznamObsadenychBuniek()) {
                if ((x >= policko.getPoziciaX() && x <= policko.getPoziciaX() + 50) && (y >= policko.getPoziciaY() && y <= policko.getPoziciaY() + 50)) {
                    policko.setZasiahnuta(true);
                }
            }
        }
    }
    
    /**
     * Metóda, ktorá zabezpečuje výpis záznamu hry na hraciu plochu a jeho aktualizovanie pri každom zásahu.
     */
    public void vypisNaPlochuSingle() {
        if (this.zapamataj >= 25) {
            this.zapisky1[0].skry();
            this.zapisky2[0].skry();
            for (int k = 0; k < this.zapisky1.length - 1; k++) {
                this.zapisky1[k] = this.zapisky1[k + 1];
                this.zapisky1[k].zmenPolohu(1020, 90 + (15 * k));
            }
            
            this.zapisky1[this.zapisky1.length - 1] = null;
            
            for (int l = 0; l < this.zapisky2.length - 1; l++) {
                this.zapisky2[l] = this.zapisky2[l + 1];
                this.zapisky2[l].zmenPolohu(1180, 90 + (15 * l));
            }
            
            this.zapisky2[this.zapisky2.length - 1] = null;
            
            String z = "riadok:  " + this.zoznamZasahov.get(this.zapamataj).getRiadok() + "    stĺpec:  " + this.zoznamZasahov.get(this.zapamataj).getStlpec();
            BlokTextu vypis = new BlokTextu(z, 1020, 90 + 15 * 24);
            BlokTextu uspesnost = null;
                    
            if (this.zoznamZasahov.get(this.zapamataj).getUspech()) {
                z = z + "    ZÁSAH";
                uspesnost = new BlokTextu("ZÁSAH", 1180, 90 + 15 * 24);
            } else {
                z = z + "    MIMO";
                uspesnost = new BlokTextu("MIMO", 1180, 90 + 15 * 24);
            }
            
            vypis.zmenFarbu("#E59500");
            uspesnost.zmenFarbu("#E59500");

            vypis.zmenFont("Arial", StylFontu.BOLD, 15);
            uspesnost.zmenFont("Arial", StylFontu.BOLD, 15);
    
            vypis.zobraz();
            uspesnost.zobraz();
            
            this.zapamataj++;
            this.zapisky1[this.zapisky1.length - 1] = vypis;
            this.zapisky2[this.zapisky2.length - 1] = uspesnost;
            this.zaznamHry.add(z);
            return;
        }
        
        String z = "riadok:  " + this.zoznamZasahov.get(this.zapamataj).getRiadok() + "    stĺpec:  " + this.zoznamZasahov.get(this.zapamataj).getStlpec();
        BlokTextu vypis = new BlokTextu(z, 1020, 90 + 15 * (this.zapamataj % 25));
        BlokTextu uspesnost = null;
                
        if (this.zoznamZasahov.get(this.zapamataj).getUspech()) {
            z = z + "    ZÁSAH";
            uspesnost = new BlokTextu("ZÁSAH", 1180, 90 + 15 * (this.zapamataj % 25));
        } else {
            z = z + "    MIMO";
            uspesnost = new BlokTextu("MIMO", 1180, 90 + 15 * (this.zapamataj % 25));
        }
        
        vypis.zmenFarbu("#E59500");
        uspesnost.zmenFarbu("#E59500");

        vypis.zmenFont("Arial", StylFontu.BOLD, 15);
        uspesnost.zmenFont("Arial", StylFontu.BOLD, 15);

        vypis.zobraz();
        uspesnost.zobraz();
        
        this.zapisky1[this.zapamataj % 25] = vypis;
        this.zapisky2[this.zapamataj % 25] = uspesnost;
        this.zapamataj++;
        this.zaznamHry.add(z);
    }
    
    /**
     * Metóda, ktorá volá metódu umiestniLodeRandom a zabezpečuje spustenie hry pre hru pre jedného hráča.
     */
    public void zacniHru() {
        this.umiestniLodeRandom();
    }
    
    /**
     * Metóda, ktorá zabezpečuje kontrolu konca hry, pri dosiahnutí 20 úspešných zásahov - zasiahnutie všetkých lodí.
     */
    public boolean kontrolaKoncaHry() {
        if (this.uspech == 20) {
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
        if (this.uspech == 20 ) {
            this.vysledokHry = HernyVysledok.VYHRA_HRACA;
            JOptionPane.showMessageDialog(null, "KONIEC HRY! VYHRAL SI :D");
            
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
     * Metóda, ktorá zabezpečuje výpis záznamu hry do súboru.
     */
    public void zapisDoSuboru() throws IOException {
        File subor = new File("zaznamHry.txt");
        PrintWriter zapisovac = new PrintWriter(subor);
        Bunka[][] novyZoznam = new Bunka[10][10];
        
        zapisovac.println("Umiestnenie lodí: ");
        for (int k = 0; k < 10; k++) {
            for (int l = 0; l < 10; l++) {
                zapisovac.print("[" + this.doska.getListBuniek()[l][k].getOzn() + "]");
            }
            zapisovac.println();
        }
        
        zapisovac.println();
        zapisovac.println("Záznam hry:");
        
        for (String priebeh : this.zaznamHry) {
            zapisovac.println(priebeh);
        }
        zapisovac.close();
    }
    
    /**
     * Metóda, ktorá zabezpečuje vymazanie hry pre jedného hrača.
     */
    public void skryVsetko() {
        this.obdlznik.skry();
        this.text.skry();
        this.hrac.skry();
        
        for (BlokTextu zapis1 : this.zapisky1) {
            if (zapis1 != null) { 
                zapis1.skry();
            }
        }
        
        for (BlokTextu zapis2 : this.zapisky2) {
            if (zapis2 != null) { 
                zapis2.skry();
            }
        }
        
        this.doska.skryHraciuDosku();

        for (Lod l : this.lodky) {
            l.skryLodku();
        }
        
        this.zapisky1 = new BlokTextu[25];
        this.zapisky2 = new BlokTextu[25];
    }
    
    /**
     * Metóda, ktorá zabezpečuje premazanie hodnôt hlavných atribútov triedy SinglePlayer, 
     * tak aby hra bola znova spustiteľná.
     */
    public void resetHrySingle() {
        this.doska.zobrazHraciuDosku();
        this.obdlznik.zobraz();
        this.text.zobraz();
        this.hrac.zobraz();
        this.zoznamZasahov.clear();
        this.zaznamHry.clear();
        this.lodky.clear();
        this.uspech = 0;
        this.zapamataj = 0;
        this.vysledokHry = HernyVysledok.NEROZHODNUTA;
    }
}
