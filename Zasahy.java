/**
 * Trieda Zasahy zabezpečuje ukladanie informácii o jednotlivých zásahoch.
 * 
 * @author Emma Lujza Rothova
 */
public class Zasahy {
    private int x;
    private int y;
    private String oznPolicka;
    private boolean uspech;
    
    /**
     * Konštruktor pre triedu Zasahy, ktorý inicializuje atribúty a ukladá do nich 
     * hodnoty zadaných formálnych parametrov.
     */
    public Zasahy(int riadok, int stlpec, String ozn, boolean uspech) {
        this.x = riadok;
        this.y = stlpec;
        this.oznPolicka = ozn;
        this.uspech = uspech;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.x predstavujúci riadok.
     */
    public int getRiadok() {
        return this.x;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.y predstavujúci stĺpec.
     */
    public int getStlpec() {
        return this.y;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.oznPolicka.
     */
    public String getOznPolicka() {
        return this.oznPolicka;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.uspech predstavujúci úspešnosť daného zásahu.
     */
    public boolean getUspech() {
        return this.uspech;
    }
}
