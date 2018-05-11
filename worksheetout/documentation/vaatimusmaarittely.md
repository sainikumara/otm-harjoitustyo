# Vaatimusmäärittely

## Soveluksen tarkoitus

Sovelluksen avulla käyttäjien on mahdollista pitää kirjaa harjoittelustaan ja seurata edistymistään kvantitatiivisesti. Sovellusta on mahdollista käyttää useamman rekisteröityneen käyttäjän, jotka voivat luoda omat harjoitusohjelmansa ja muokata niitä.


## Käyttäjät

Sovelluksella on vain peruskäyttäjiä.


## Perusversion tarjoama toiminnallisuus

### Ennen kirjautumista

- Käyttäjä voi kirjautua sovellukseen Google-tunnuksillaan
  - [Google OAuth 2.0](https://developers.google.com/api-client-library/java/google-api-java-client/oauth2)
- Sovellus tallentaa käyttäjän antaman nimimerkin ja Google spreadsheetin id:n paikallisesti
  
  
### Kirjautumisen jälkeen

- Kirjautumisen jälkeen ensimmäisessä näkymässä on lista käyttäjän luomista harjoitusohjelmista
  - käyttäjä voi tarkastella ja muokata harjoitusohjelmia ja luoda uusia
  - harjoituskokonaisuudella on nimi ja siihen voi lisätä harjoitteita
  
- Harjoitteella on nimi ja kaksi käyttäjän vapaasti valitsemaa attribuuttia, esimerkiksi vastus ja toistojen lukumäärä
  
- Käyttäjä voi luoda harjoituskertoja harjoituskokonaisuuksille
  - harjoituskerralla on päivämäärä ja harjoitusohjelmaan kuuluvien harjoitteiden attribuuttien arvot
  - käyttäjä merkkaa tekemänsä yksittäiset harjoitteet antamalla arvot sen kenttiin
  
- Käyttäjä voi selata vanhojen harjoituskertojensa tietoja
  
- Käyttäjä voi kirjautua ulos sovelluksesta

- Tiedot tallennetaan Google Sheetsiin käyttäjän luomalle ja valitsemalle spreadsheetille

- Käyttäjällä pääsy Sheetsissa olevaan dataan ja mahdollisuus käyttää ja muokata sitä mielensä mukaan esim. harjoittelun analyysointiin


## Jatkokehitysideoita

- Käyttäjä voi poistaa ja muokata syöttämäänsä tietoa suoraan sovelluksen kautta, eikä hänen tarvise käydä editoimassa Sheetsissa olevaa dataa käsin

- Sovelluksesta tehdään mobiiliversio, jolloin se kulkee kätevästi mukana esimerksi kuntosalille

- Sovelluksessa näytetään yhteenvetotietoja harjoittelun edistymisestä esim. viikoittain, kuukausittain, harjoituksittain

- Sovelluksessa näytetään graafisia yhteenvetotietoja harjoittelun edistymisestä

- Yksityiskohtaisempi harjoituksen merkkaus: esim. sarjojen lukumäärä ja kunkin sarjan toistojen lukumäärä

- Harjoitusohjelmien rakentaminen, yhdistäminen kalenteriin

- Käyttäjä voi sallia sovelluksessa näytettävän säätietoja, ja voi valita yhdistää säädataa harjoitteludataansa 
