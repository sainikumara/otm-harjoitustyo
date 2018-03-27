# Vaatimusmäärittely

## Soveluksen tarkoitus
Sovelluksen avulla käyttäjien on mahdollista pitää kirjaa harjoittelustaan ja seurata edistymistään. Sovellusta on mahdollista käyttää useamman rekisteröityneen käyttäjän, jotka voivat luoda omat harjoitusohjelmansa ja muokata niitä.

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
  
- Käyttäjä voi luoda uusia harjoituksia
  - harjoituksella on nimi ja tieto siitä, mihin harjoituskokonaisuuteen se kuuluu
  - harjoitukselle määritellään kaksi numeroarvoista kenttää; oletusarvoisesti harjoitusvastus ja toistojen lukumäärä
  
- Käyttäjä voi luoda harjoituskertoja harjoituskokonaisuuksille
  - harjoituskerroilla on päivämäärä
  - käyttäjä merkkaa tekemänsä yksittäiset harjoitukset antamalla arvot sen kenttiin
  
- Käyttäjä voi selata vanhoja harjoituskertojaan
  
- Käyttäjä voi kirjautua ulos sovelluksesta

## Jatkokehitysideoita

- Yhteenvetotietoja harjoittelun edistymisestä esim. viikoittain, kuukausittain, harjoituksittain
- Graafisia yhteenvetotietoja harjoittelun edistymisestä
