import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Trieda Hrac zabezpečuje definovanie základných herných funkcií pre hráča.
 * 
 * @author Emma Lujza Rothova
 */
public class Hrac {
    private static Hrac instancia;
    
    private String meno;
    private HraciaDoska doska;
    private Pocitac enemy;
    
    private ArrayList<Zasahy> zoznamZasahov;
    private ArrayList<Lod> lodky;
    private ArrayList<TypyLode> typy;
    
    private int uspech;
    
    /**
     * Konštruktor pre triedu Hrac, ktorý inicializuje atribúty.
     */
    private Hrac() {
        this.meno = "Jednooký Hrom";
        this.doska = new HraciaDoska(25, 25, 40);
        this.doska.setVelkost(10);
        
        this.enemy = null;
        
        this.uspech = 0;
        
        this.zoznamZasahov = new ArrayList<Zasahy>();
        this.lodky = new ArrayList<Lod>();

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
    }
    
    /**
     * Metóda, ktorá implementuje návrhový vzor singleton, čo zabezpečuje vytvorenie 
     * len jednej ištancie triedy Hrac.
     */
    public static Hrac getInstancia() { 
        if (Hrac.instancia == null) { 
            Hrac.instancia = new Hrac(); 
        } 
        return Hrac.instancia; 
    }
    
    /**
     * Metóda, ktorá zabezpečuje premazanie hlavných atribútov triedy Hrac,
     * tak aby bola hra dvoch hráčov znova spustiteľná.
     */
    public void resetujHrac() {
        this.doska.skryHraciuDosku();
        this.lodky.clear();
        this.uspech = 0;
        this.zoznamZasahov.clear();
    }
    
    /**
     * Metóda, ktorá zabezpečuje zobrazenie hracej dosky hráča.
     */
    public void zobrazDosku() {
        this.doska.zobrazHraciuDosku();
    }
    
    /**
     * Metóda, ktorá vracia dvojdimenzionálne pole, ktoré obsahuje objekty typu Bunka, z ktorých je zložená
     * hracia doska.
     */
    public Bunka[][] getDoskaHrac() {
        return this.doska.getListBuniek();
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
        lodka.zobrazLodku();
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
        
        if (this.doska != null && !this.lodky.isEmpty()) {
            this.vymazVsetko();
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
     * Metóda, ktorá zabezpečuje premazanie lodí z hracej plochy, v prípade, 
     * že ich hráč nechal umiestniť náhodne a nie je s umiestnením spokojný.
     */
    public void vymazVsetko() {
        for (Bunka[] stlpec : this.doska.getListBuniek()) {
            for (Bunka bunka : stlpec) {
                bunka.setObsadena(false);
                bunka.setOzn(0);
            }
        }
        
        for (Lod lodka : this.lodky) {
            lodka.skryLodku();
        }
        
        this.lodky.clear();
    }
    
    /**
     * Metóda, ktorá umožňuje hráčovi si sám umiestniť lode na nim zadané pozície,
     * pričom sa kontrolu, či nedošlo k ich prekrytiu.
     */
    public void umiestniLodeSam() {
        int i = 0;
        while (i < this.typy.size()) {
            TypyLode typ = this.typy.get(i);
            JOptionPane.showMessageDialog(null, "Umiestňuješ: " + typ.getSlovo());
            String riadok = JOptionPane.showInputDialog("Zadaj riadok (0-9):");
            String stlpec = JOptionPane.showInputDialog("Zadaj stlpec (0-9):");
            String orientacia = JOptionPane.showInputDialog("Zadaj orientaciu loďky (horizontal/vertikal):");
            
            int rlodka = Integer.parseInt(riadok);
            int slodka = Integer.parseInt(stlpec);
            if ((rlodka < 0 && rlodka > 10) || (slodka < 0 && slodka > 10)) {
                JOptionPane.showMessageDialog(null, "Zle zadaný argument riadok alebo stlpec.");
                continue;
            }
            
            boolean kontrola = true;
            
            if (orientacia.equals("horizontal")) {
                if ((typ.equals(TypyLode.HLIADKOVA_LOD) && slodka >= 9) || (typ.equals(TypyLode.TORPEDOBOREC) && slodka >= 8) || (typ.equals(TypyLode.LIETADLOVA_LOD) && slodka >= 7)) {
                    continue;
                } else {
                    switch (typ.getSlovo()) {
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
                        this.umiestniLod(rlodka, slodka, typ, true);
                        switch (typ.getSlovo()) {
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
                        JOptionPane.showMessageDialog(null, "Loď sa nedá bezpečne umiestniť.");
                        continue;
                    }
                }
            } else if (orientacia.equals("vertikal")) {
                if ((typ.equals(TypyLode.HLIADKOVA_LOD) && rlodka >= 9) || (typ.equals(TypyLode.TORPEDOBOREC) && rlodka >= 8) || (typ.equals(TypyLode.LIETADLOVA_LOD) && rlodka >= 7)) {
                    continue;
                } else {
                    switch (typ.getSlovo()) {
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
                        this.umiestniLod(rlodka, slodka, typ, false);
                        switch (typ.getSlovo()) {
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
                        JOptionPane.showMessageDialog(null, "Loď sa nedá bezpečne umiestniť.");
                        continue;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Zle zadaný argument orientacia.");
                continue;
            }
            i++;
        }
    }
    
    /**
     * Metóda, ktorá kontroluje úspešnosť zásahu na základe zakliknutých súradníc.
     */
    public void najdenyZasah(int x, int y) {
        if (this.uspech != 20) {
            boolean kontrola1 = false;
            int indexR = 0;
            int indexS = 0;
            
            for (Bunka[] i : this.enemy.getDoskaPC()) {
                for (Bunka umiestnenia : i) {
                    if ((x >= umiestnenia.getPoziciaX() && x <= umiestnenia.getPoziciaX() + 40) && (y >= umiestnenia.getPoziciaY() && y <= umiestnenia.getPoziciaY() + 40)) {
                        indexR = (umiestnenia.getPoziciaY() - 25) / (40 + 5);    
                        indexS = (umiestnenia.getPoziciaX() - 525) / (40 + 5);
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
            
            if (!this.enemy.getDoskaPC()[indexS][indexR].getZasiahnuta()) {
                this.enemy.getDoskaPC()[indexS][indexR].setZasiahnuta(true);
                if (kontrola1) {
                    this.enemy.getDoskaPC()[indexS][indexR].zmenFarbu("#f5697e");
                    this.uspech++;
                    for (Lod ship : this.enemy.getLodky()) {
                        if (ship.getCelaZasiahnuta()) {
                            ship.zobrazLodku();
                            ship.zasiahnutaLodka();
                        }
                    }
                    this.zoznamZasahov.add(new Zasahy(indexR, indexS, "H", true));
                } else {
                    this.enemy.getDoskaPC()[indexS][indexR].zmenFarbu("#2081f7");
                    this.zoznamZasahov.add(new Zasahy(indexR, indexS, "H", false));
                }
                return;
            }
        } 
    }
    
    /**
     * Metóda, ktorá kontroluje, či zakliknuté súradnice zodpovedajú hracej ploche, na ktorej je zaklikávanie dovolené.
     */
    public boolean mozeTrafit(int x, int y) {
        if (x < 525 || x > 970) {
            return false;
        }
        
        if (y < 25 || y > 470) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Metóda, ktorá zabezpečuje nastavenie zásahu na lodi nepriateľa, v prípade úspešneho zásahu.
     */
    public void nastavZasah(int x, int y) {
        for (Lod lodka : this.enemy.getLodky()) {
            for (Bunka policko : lodka.getZoznamObsadenychBuniek()) {
                if ((x >= policko.getPoziciaX() && x <= policko.getPoziciaX() + 40) && (y >= policko.getPoziciaY() && y <= policko.getPoziciaY() + 40)) {
                    policko.setZasiahnuta(true);
                }
            }
        }
    }
    
    /**
     * Getter, ktorý vracia neupraviteľný zoznam lodí.
     * Mojim zdrojom na vytvorenie takéhoto zoznamu bola táto stránka:
     * https://www.geeksforgeeks.org/collections-unmodifiablelist-method-in-java-with-examples/
     */
    public List<Lod> getLodky() {
        List<Lod> lode = this.lodky;
        return Collections.unmodifiableList(lode);
    }
    
    /**
     * Getter, ktorý vracia neupraviteľný zoznam zásahov hráča.
     * Mojim zdrojom na vytvorenie takéhoto zoznamu bola táto stránka:
     * https://www.geeksforgeeks.org/collections-unmodifiablelist-method-in-java-with-examples/
     */
    public List<Zasahy> getZoznamZasahov() {
        List<Zasahy> zasah = this.zoznamZasahov;
        return Collections.unmodifiableList(zasah);
    }
    
    /**
     * Setter, ktorý zabezpečuje nastavenie protihráča.
     */
    public void setEnemy(Pocitac pocitac) {
        this.enemy = pocitac;
    }
}
