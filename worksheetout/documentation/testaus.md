# Testausdokumentti

Ohjelmaa on testattu sekä automatisoiduin yksikkö- ja integraatiotestein JUnitilla sekä manuaalisesti tapahtunein järjestelmätason testein.

## Yksikkö- ja integraatiotestaus

### sovelluslogiikka

Automatisoitujen testien ytimen moudostavat sovelluslogiikkaa, eli pakkauksen [worksheetout.domain](https://github.com/sainikumara/otm-harjoitustyo/tree/master/worksheetout/src/main/java/worksheetout/domain) luokkia testaavat integraatiotestit luokassa [WorkoutServiceIntegrationTest](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/test/java/worksheetout/domain/WorkoutServiceIntegrationTest.java), joiden määrittelemät testitapaukset simuloivat käyttöliittymän [WorkoutService](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/WorkoutService.java)-olion avulla suorittamia toiminnallisuuksia.

Integraatiotestit käyttävät datan pysyväistallennukseen samoja DAO-luokkia, joita sovellus käyttää muutenkin. Käyttäjiä koskeva data talletetaan paikallisesti tiedostoon [FileUserDaon](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/dao/FileUserDao.java) avulla, ja harjoitusohjelmia ja harjoituskertoja koskeva data talletetaan Google Sheetsiin [SheetsRoutineDaon](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/dao/SheetRoutineDao.java) ja [SheetsWorkoutSessionDaon](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/dao/SheetWorkoutSessionDao.java) avulla.

Sovelluslogiikkakerroksen luokille [User](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/User.java), [Routine](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/Routine.java), [Exercise](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/Exercise.java) ja [DoneExercise](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/DoneExercise.java) on tehty yksikkötestejä.

### DAO-luokat

DAO-luokkien toiminnallisuutta testattiin integraatiotestein ja manuaalisesti.

### Testauskattavuus

Käyttöliittymäkerrosta lukuunottamatta sovelluksen testauksen rivikattavuus on 87% ja haarautumakattavuus 68%

<img src="https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/documentation/jacoco.png" width="800">

Testaamatta jäi osin WorkoutSession-luokka ja molemmat Dao-luokat niiltä osin, jotka eivät kuulu ohjelman keskeisimpiin toiminnallisuuksiin.

## Järjestelmätestaus

Sovelluksen järjestelmätestaus on suoritettu manuaalisesti.

### Asennus ja kanfigurointi

Sovellus on haettu ja sitä on testattu [käyttöohjeen](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/documentation/kayttoohje.md) kuvaamalla tavalla Linux-ympäristössä.

### Toiminnallisuudet

Kaikki [määrittelydokumentin](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/documentation/vaatimusmaarittely.md) ja käyttöohjeen listaamat toiminnallisuudet on käyty läpi. Kaikkien toiminnallisuuksien yhteydessä on syötekentät yritetty täyttää myös virheellisillä arvoilla kuten tyhjillä.

## Sovellukseen jääneet laatuongelmat

Sovellus ei anna tällä hetkellä järkeviä virheilmoituksia, seuraavissa tilanteissa
- käyttäjä ei anna toimivaa Google spreadsheet id:tä, jonka muokkaamiseen hänellä on oikeudet
Sovellus näyttää ajoittain näkymiä kokonaan mustina siirryttyään niihin. Tähän auttaa usein hiirellä kliksuttelu, jolloin tarpeellisia kenttiä ilmestyy näkyviin.
