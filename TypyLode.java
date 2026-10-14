/**
 * Enum TypyLode zabezpečuje vytváranie priamo definovaných inštancii triedy Lod.
 * 
 * @author Emma Lujza Rothova
 */
public enum TypyLode {
    CLN(1, "#A3C0E6", "cln"),
    HLIADKOVA_LOD(2, "#506985", "hliadkovaLod"),
    TORPEDOBOREC(3, "#314256", "torpedoborec"),
    LIETADLOVA_LOD(4, "#1C2634", "lietadlovaLod");

    private final int dlzka;
    private final String farba;
    private final String slovo;
    
    /**
     * Konštruktor pre enum TypyLode, ktorý inicializuje atribúty a ukladá do nich 
     * hodnoty zadaných formálnych parametrov.
     */
    TypyLode(int dlzka, String farba, String slovo) {
        this.dlzka = dlzka;
        this.farba = farba;
        this.slovo = slovo;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.dlzka.
     */
    public int getDlzkaLode() {
        return this.dlzka;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.farba.
     */
    public String getFarbaLode() {
        return this.farba;
    }
    
    /**
     * Getter, ktorý vracia hodnotu atribútu this.slovo.
     */
    public String getSlovo() {
        return this.slovo;
    }
}
