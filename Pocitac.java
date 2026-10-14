import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;

/**
 * Trieda Pocitac zabezpečuje definovanie základných herných funkcií pre počítač.
 * 
 * @author Emma Lujza Rothova 
 */
public class Pocitac {
    private static Pocitac instancia;
    
    private String meno;
    private HraciaDoska doska;
    private Hrac enemy;
    private Lod zasiahnutaLodka;
    private Lod kontrolaZasiahnutaLodka;
    
    private ArrayList<Lod> lodky;
    private ArrayList<TypyLode> typy;
    private ArrayList<Zasahy> zoznamZasahov;
    private ArrayList<int[]> susedneBunky;
    
    private int uspech;
    private int zapamatenyRZasah;
    private int zapamatenySZasah;
    
    /**
     * Konštruktor pre triedu Pocitac, ktorý inicializuje atribúty.
     */
    private Pocitac() {
        this.meno = "Búrkový drak";
        this.doska = new HraciaDoska(25 + 500, 25, 40);
        this.doska.setVelkost(10);
        
        this.enemy = null;
        
        this.zoznamZasahov = new ArrayList<Zasahy>();
        this.lodky = new ArrayList<Lod>();
        this.susedneBunky = new ArrayList<int[]>();

        this.typy = new ArrayList<TypyLode>();
        for (int l = 0; l < 4; l++) {
            this.typy.add(TypyLode.CLN);
        }
        for (int j = 0; j < 3; j++) {
            this.typy.add(TypyLode.HLIADKOVA_LOD);
        }
        for (int k = 0; k < 2; k++) {
            this.typy.add(TypyLode.TORPEDOBOREC);
        }
        this.typy.add(TypyLode.LIETADLOVA_LOD);
        
        this.uspech = 0;
        
        this.zapamatenyRZasah = 0;
        this.zapamatenySZasah = 0;
        this.zasiahnutaLodka = null;
        this.kontrolaZasiahnutaLodka = null;
    }
    
    /**
     * Metóda, ktorá implementuje návrhový vzor singleton, čo zabezpečuje vytvorenie 
     * len jednej ištancie triedy Pocitac.
     */
    public static Pocitac getInstancia() { 
        if (Pocitac.instancia == null) { 
            Pocitac.instancia = new Pocitac(); 
        } 
        return Pocitac.instancia; 
    }
    
    /**
     * Metóda, ktorá zabezpečuje premazanie hodnôt hlavných atribútov triedy Pocitac,
     * tak aby bola hra dvoch hráčov znova spustiteľná.
     */
    public void resetujPocitac() {
        this.doska.skryHraciuDosku();
        this.lodky.clear();
        this.uspech = 0;
        this.zoznamZasahov.clear();
    }
    
    /**
     * Metóda, ktorá zabezpečuje zobrazenie hracej dosky počítača.
     */
    public void zobrazDosku() {
        this.doska.zobrazHraciuDosku();
    }
    
    /**
     * Metóda, ktorá vracia dvojdimenzionálne pole, ktoré obsahuje objekty typu Bunka, z ktorých je zložená
     * hracia doska.
     */
    public Bunka[][] getDoskaPC() {
        return this.doska.getListBuniek();
    }
    
    /**
     * Metóda, ktorá zabezpečuje umiestnenie a vykreslenie lode na hraciu dosku.
     */
    private void umiestniLod(int riadok, int stlpec, TypyLode typ, boolean horizontal) {
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
     * Metóda, ktorá zabezpečuje náhodné generovanie zásahov, 
     * pričom pri každom úspešnom zásahu sa kontroluje aj okolie.
     */
    public void setZasahPC() {
        Random riadok = new Random();
        Random stlpec = new Random();
        Random cislo = new Random();
        
        while (this.uspech != 20) {
            int rZasah = riadok.nextInt(10);
            int sZasah = stlpec.nextInt(10);
            boolean okolie = false;
            
            if (this.kontrolaPouzitychZasahov(rZasah, sZasah)) {
                if (this.enemy.getDoskaHrac()[sZasah][rZasah].getObsadena()) {
                    this.zoznamZasahov.add(new Zasahy(rZasah, sZasah, "P", true));
                    this.uspech++;
                    okolie = true;
                    this.susedneBunky.add(new int[]{sZasah + 1, rZasah});
                    this.susedneBunky.add(new int[]{sZasah - 1, rZasah});
                    this.susedneBunky.add(new int[]{sZasah, rZasah + 1});
                    this.susedneBunky.add(new int[]{sZasah, rZasah - 1});
                } else {
                    this.zoznamZasahov.add(new Zasahy(rZasah, sZasah, "P", false));
                    okolie = false;
                }
            } else {
                continue;
            }
            
            if (okolie) {
                while (!this.susedneBunky.isEmpty()) {
                    int nahoda = cislo.nextInt(this.susedneBunky.size());
                    int s = this.susedneBunky.get(nahoda)[0];
                    int r = this.susedneBunky.get(nahoda)[1];
                    this.susedneBunky.remove(nahoda);
                    
                    this.kontrolaOkolia(r, s);
                }
            }
        }
    }
    
    /**
     * Metóda, ktorá zabezpečuje kontrolu okolia pri úspešnom zásahu.
     */
    public void kontrolaOkolia(int rZasah, int sZasah) {
        if ((rZasah >= 0 && rZasah < 10) && (sZasah >= 0 && sZasah < 10)) {
            if (this.kontrolaPouzitychZasahov(rZasah, sZasah)) {
                if (this.enemy.getDoskaHrac()[sZasah][rZasah].getObsadena()) {
                    this.zoznamZasahov.add(new Zasahy(rZasah, sZasah, "P", true));
                    this.uspech++;
                    this.susedneBunky.add(new int[]{sZasah + 1, rZasah});
                    this.susedneBunky.add(new int[]{sZasah - 1, rZasah});
                    this.susedneBunky.add(new int[]{sZasah, rZasah + 1});
                    this.susedneBunky.add(new int[]{sZasah, rZasah - 1});
                } else {
                    this.zoznamZasahov.add(new Zasahy(rZasah, sZasah, "P", false));
                }
            } else {
                return;
            }
        } else {
            return;
        }
    }
    
    /**
     * Metóda (getter), ktorá kontrolu, či daný zásah už nebol použitý 
     * a vracia na základe toho true alebo false.
     */
    public boolean kontrolaPouzitychZasahov(int rZasah, int sZasah) {
        for (Zasahy zasah: this.zoznamZasahov) {
            if (zasah.getStlpec() == sZasah && zasah.getRiadok() == rZasah) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Metóda, ktorá zabezpečuje vykreslenie zásahu do hracej plochy hráča.
     */
    public void vykresliZasahy(int i) {
        if (this.zoznamZasahov.isEmpty()) {
            return;
        }
        
        Zasahy zasah = this.zoznamZasahov.get(i);
        if (zasah.getUspech()) {
            this.enemy.getDoskaHrac()[zasah.getStlpec()][zasah.getRiadok()].zmenFarbu("#f5697e");
            int x = 25 + (zasah.getStlpec() * (40 + 5));
            int y = 25 + (zasah.getRiadok() * (40 + 5));
                    
            for (Lod lodka : this.enemy.getLodky()) {
                for (Bunka policko : lodka.getZoznamObsadenychBuniek()) {
                    if (x == policko.getPoziciaX() && y == policko.getPoziciaY()) {
                        policko.skryBunku();
                        policko.setZasiahnuta(true);
                    }
                }
            }
            
            for (Lod lodka : this.enemy.getLodky()) {
                if (lodka.getCelaZasiahnuta()) {
                    lodka.zasiahnutaLodka();
                    lodka.zobrazLodku();
                }
            }
            
        } else {
            this.enemy.getDoskaHrac()[zasah.getStlpec()][zasah.getRiadok()].zmenFarbu("#2081f7");
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
     * Getter, ktorý vracia neupraviteľný zoznam zásahov počítača.
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
    public void setEnemy(Hrac hrac) {
        this.enemy = hrac;
    }
}
