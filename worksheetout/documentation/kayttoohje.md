# Käyttöohje

Lataa tiedosto [worksheetout.jar](https://github.com/sainikumara/otm-harjoitustyo/releases/tag/v0.3beta)

## Konfigurointi

Ohjelma ei vaadi konfigurointia

## Ohjelman käynnistäminen

Ohjelma käynnistetään komennolla 

```
java -jar todoapp.jar
```

## Kirjautuminen
Käynnistyessään ohjelma vaatii käyttäjää kirjautumaan sisään Google-tilille ja antamaan sitä kautta oikeuden muokata tämän omia Google Sheets -tiedostoja. Käyttäjän on luotava Google spreadsheet id ja annettava sen tunnus ohjelmalle. Ensimmäisellä kirjautumiskerralla käyttän on luotava tunnus, jolloin ohjelma tallettaa käyttäjän antaman nimimerkin ja spreadsheet id:n. Tämän jälkeen kirjautuminen tapahtuu tällä nimimerkillä. Salasanoja ei tarvita, koska käyttäjän on joka kerta ohjelman käynnistyessä annettava sille erikseen lupa päästä käsiksi spreadsheet-tiedostoonsa.

## Sovelluksen käyttö
Kirjautumisen jälkeen käyttäjä pääsee näkymään, jossa ei aluksi ole muuta sisältöä kuin logout-nappi ja mahdollisuus lisätä uusia harjoitusohjelmia. Harjoitusohjelmia lisättäessä tulevat ne näkyviin näkymän keskelle. Kunkin harjoitusohjelman nimen oikealla puolella on nappi harjoitteiden lisäämistä varten ja toinen nappi, josta pääsee tarkastelemaan tähän harjoitusohjelmaan liittyviä harjoituskertoja.

Aluksi harjoitteita ja harjoituskeroja ei tietenkään näy. Lisättyään ensin harjoitteita harjoitusohjelmaan voi käyttäjä siirtyä lisäämään harjoituskertanäkymien alareunasta uuden harjoituskerran. Tällöin hän päätyy näkymään, jossa annetaan lukuarvot jokaiselle harjoitusohjelmaan määriteltyjen harjoitteiden parametrille. Kullakin harjoitteella on kaksi parametria, jotka käyttäjä valitsee vapaasti luodessaan harjoitteen.

Sovellus tallettaa kaiken harjoitusohjelmiin ja harjoituskertoihin liittyvän datan Google Sheets -tiedostoon, jossa käyttäjä voi analysoida, muokata tai poistaa sitä mielensä mukaan.
