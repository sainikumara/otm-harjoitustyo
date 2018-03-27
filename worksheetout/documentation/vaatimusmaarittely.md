# Vaatimusmäärittely

## Soveluksen tarkoitus

Sovelluksen avulla käyttäjien on mahdollista pitää kirjaa harjoittelustaan ja seurata edistymistään kvantitatiivisesti. Sovellusta on mahdollista käyttää useamman rekisteröityneen käyttäjän, jotka voivat luoda omat harjoitusohjelmansa ja muokata niitä.


## Käyttäjät

Sovelluksella on vain peruskäyttäjiä.


## Käyttöliittymäluonnos


## Perusversion tarjoama toiminnallisuus

### Ennen kirjautumista

- Käyttäjä voi kirjautua sovellukseen Google-tunnuksillaan
  - [Google OAuth 2.0](https://developers.google.com/api-client-library/java/google-api-java-client/oauth2)
  
  
### Kirjautumisen jälkeen

- Käyttäjä näkee ensimmäisellä kirjautumiskerralla alkunäkymässä linkit kolmeen valmiiseen harjoituskokonaisuuspohjaan
  - käyttäjä voi tarkastella ja muokata valmiita pohjia tai luoda uusia harjoituskokonaisuuksia
  - käyttäjä voi poistaa harjoituskokonaisuuksia
  
- Käyttäjä voi luoda uusia harjoituksia ja poistaa vanhoja
  - harjoituksella on nimi ja tieto siitä, mihin harjoituskokonaisuuteen se kuuluu
  - harjoitukselle määritellään kaksi numeroarvoista kenttää; oletusarvoisesti harjoitusvastus ja toistojen lukumäärä
  
- Käyttäjä voi luoda harjoituskertoja harjoituskokonaisuuksille
  - harjoituskerroilla on päivämäärä
  - käyttäjä merkkaa tekemänsä yksittäiset harjoitukset antamalla arvot sen kenttiin
  - käyttäjä voi poistaa harjoituskertoja
  
- Käyttäjä voi selata vanhojen harjoituskertojensa tietoja
  
- Käyttäjä voi kirjautua ulos sovelluksesta


## Jatkokehitysideoita

- Ensimmäisessä versiossa tiedot talletetaan paikallisesti tiedostoon, mutta sovelluksen on tarkoitus siirtyä käyttämään tallennusmuotonaan [Google Sheetsia](https://developers.google.com/api-client-library/java/apis/sheets/v4)
- Käyttäjällä pääsy Sheetsissa olevaan dataan ja mahdollisuus käyttää sitä mielensä mukaan esim. harjoittelun analyysointiin
- Sovelluksessa näytetään yhteenvetotietoja harjoittelun edistymisestä esim. viikoittain, kuukausittain, harjoituksittain
- Sovelluksessa näytetään graafisia yhteenvetotietoja harjoittelun edistymisestä
- Yksityiskohtaisempi harjoituksen merkkaus: esim. sarjojen lukumäärä ja kunkin sarjan toistojen lukumäärä
- Harjoitusohjelmien rakentaminen, yhdistäminen kalenteriin
- Käyttäjä voi sallia sovelluksessa näytettävän säätietoja, ja voi valita yhdistää säädataa harjoitteludataansa 
