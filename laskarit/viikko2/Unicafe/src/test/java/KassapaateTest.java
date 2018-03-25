import com.mycompany.unicafe.Kassapaate;
import com.mycompany.unicafe.Maksukortti;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class KassapaateTest {
    
    Kassapaate kassa;
    Maksukortti kortti;

    @Before
    public void setUp() {
        kassa = new Kassapaate();
        kortti = new Maksukortti(1000);
    }

    @Test
    public void luodussaKassapaatteessaTuhatEuroa() {
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void alussaMyytyNollaEdullistaLounasta() {
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void alussaMyytyNollaMaukastaLounasta() {
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void syoEdullisestiKateistaRiittavastiSaldoKasvaaLounaanHinnalla() {
        kassa.syoEdullisesti(250);
        assertEquals(100240, kassa.kassassaRahaa());
    }
    
    @Test
    public void syoMaukkaastiKateistaRiittavastiSaldoKasvaaLounaanHinnalla() {
        kassa.syoMaukkaasti(500);
        assertEquals(100400, kassa.kassassaRahaa());
    }
    
    @Test
    public void syoEdullisestiKateistaRiittavastiVaihtorahaOikein() {
        assertEquals(10, kassa.syoEdullisesti(250));
    }
    
    @Test
    public void syoMaukkaastiKateistaRiittavastiVaihtorahaOikein() {
        assertEquals(100, kassa.syoMaukkaasti(500));
    }
    
    @Test
    public void syoEdullisestiKateistaRiittavastiYksiEdullinenMyyty() {
        kassa.syoEdullisesti(250);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }

    
    @Test
    public void syoMaukkaastistiKateistaRiittavastiYksiMaukasMyyty() {
        kassa.syoMaukkaasti(500);
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void syoEdullisestiKateistaLiianVahanSaldoEiMuutu() {
        kassa.syoEdullisesti(230);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void syoMaukkaastiKateistaLiianVahanSaldoEiMuutu() {
        kassa.syoMaukkaasti(230);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void syoEdullisestiKateistaLiianVahanKaikkiRahatPalautetaan() {
        assertEquals(230, kassa.syoEdullisesti(230));
    }
    
    
    @Test
    public void syoMaukkaastiKateistaLiianVahanKaikkiRahatPalautetaan() {
        assertEquals(230, kassa.syoMaukkaasti(230));
    }
    
    @Test
    public void syoEdullisestiKateistaLiianVahanMyytyjenEdullistenMaaraEiMuutu() {
        kassa.syoEdullisesti(230);
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void syoMaukkaastiKateistaLiianVahanMyytyjenMaukkaidenMaaraEiMuutu() {
        kassa.syoMaukkaasti(230);
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void syoEdullisestiKortillaRiittavastiRahaaVeloitetaanKortilta() {
        kassa.syoEdullisesti(kortti);
        assertEquals(1000-240, kortti.saldo());
    }
    
    @Test
    public void syoMaukkaastiKortillaRiittavastiRahaaVeloitetaanKortilta() {
        kassa.syoMaukkaasti(kortti);
        assertEquals(1000-400, kortti.saldo());
    }
    
    @Test
    public void syoEdullisestiKortillaRiittavastiRahaaPalautetaanTrue() {
        assertEquals(true, kassa.syoEdullisesti(kortti));
    }
    
    @Test
    public void syoMaukkaastiKortillaRiittavastiRahaaPalautetaanTrue() {
        assertEquals(true, kassa.syoMaukkaasti(kortti));
    }
    
    @Test
    public void syoEdullisestiKortillaRiittavastiMyytyjenEdullistenMaaraKasvaa() {
        kassa.syoEdullisesti(kortti);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void syoMaukkaastiKortillaRiittavastiMyytyjenMaukkaidenMaaraKasvaa() {
        kassa.syoMaukkaasti(kortti);
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void syoEdullisestiKortillaRiittavastiSaldoEiMuutu() {
        kassa.syoEdullisesti(kortti);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    
    @Test
    public void syoMaukkaastiKortillaRiittavastiSaldoEiMuutu() {
        kassa.syoMaukkaasti(kortti);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void syoEdullisestiKortillaLiianVahanEiVeloitetaKortilta() {
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        assertEquals(1000-4*240, kortti.saldo());
    }
    
    @Test
    public void syoMaukkaastiKortillaLiianVahanEiVeloitetaKortilta() {
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        assertEquals(1000-2*400, kortti.saldo());
    }
    
    @Test
    public void syoEdullisestiKortillaLiianVahanPalauttaaFalse() {
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        assertTrue(!kassa.syoEdullisesti(kortti));
    }
    
    @Test
    public void syoMaukkaastiKortillaLiianVahanPalauttaaFalse() {
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        assertTrue(!kassa.syoMaukkaasti(kortti));
    }
    
    @Test
    public void syoEdullisestiKortillaLiianVahanEdullistenMaaraEiMuutu() {
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        assertEquals(4, kassa.edullisiaLounaitaMyyty());
    }
    
    
    @Test
    public void syoMaukkaastiKortillaLiianVahanMaukkaidenMaaraEiMuutu() {
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        assertEquals(2, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void syoEdullisestiKortillaLiianVahanSaldoEiMuutu() {
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void syoMaukkaastiKortillaLiianVahanSaldoEiMuutu() {
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void lataaRahaaMuuttaaKortinSaldoaOikein() {
        kassa.lataaRahaaKortille(kortti, 200);
        assertEquals(1200, kortti.saldo());
    }
    
    @Test
    public void lataaRahaaMuuttaaKassanSaldoaOikein() {
        kassa.lataaRahaaKortille(kortti, 200);
        assertEquals(100200, kassa.kassassaRahaa());
    }
    
    
    @Test
    public void lataaRahaaNegatiivinenEiMuutaKortinSaldoa() {
        kassa.lataaRahaaKortille(kortti, -200);
        assertEquals(1000, kortti.saldo());
    }
    
    @Test
    public void lataaRahaaNegatiivinenEiMuutaKassanSaldoa() {
        kassa.lataaRahaaKortille(kortti, -200);
        assertEquals(100000, kassa.kassassaRahaa());
    }
}
