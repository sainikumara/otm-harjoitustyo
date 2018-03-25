package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }
    
    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void konstruktoriAsettaaSaldonOikein() {
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void rahanLataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(32);
        assertEquals(42, kortti.saldo());
    }
    
    @Test
    public void saldoVaheneeOikeinJosRahaaTarpeeksi() {
        kortti.lataaRahaa(230);
        kortti.otaRahaa(240);
        assertEquals(0, kortti.saldo());
    }
    
    @Test
    public void otaRahaaPalauttaaTrueJosRahaaTarpeeksi() {
        kortti.lataaRahaa(230);
        assertEquals(true, kortti.otaRahaa(240));
    }
    
    @Test
    public void saldoEiMuutuElleiRahaaTarpeeksi() {
        kortti.otaRahaa(240);
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void otaRahaaPalauttaaFalseElleiRahaaTarpeeksi() {
        assertEquals(false, kortti.otaRahaa(240));
    }
    
    @Test
    public void toStringToimiiOikein() {
        kortti.lataaRahaa(250);
        assertEquals("saldo: " + 260/100 + "." + 260%100, kortti.toString());
    }
}
