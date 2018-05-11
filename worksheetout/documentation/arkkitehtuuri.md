# Arkkitehtuurikuvaus

## Rakenne
Ohjelman rakenne noudattelee kolmitasoista kerrosarkkitehtuuria, ja koodin pakkausrakenne on seuraava:

<img src="https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/documentation/package-diagram.png" width="160">

Pakkaus _worksheetout.ui_ sisältää graafisen käyttöliittymän, _worksheetout.domain_ sovelluslogiikan ja _worksheetout.dao_ tietojen tallennuksesta vastaavan koodin.

## Käyttöliittymä
Käyttöliittymä on melko yksinkertainen graafinen käyttöliittymä, joka koostuu yhteensä seitsemästä erilaisesta näkymästä. Näiden lisäksi käyttäjän pitää luoda erikseen Google Sheetsiin dokumentti ja antaa sen id ohjelmalle, sekä antaa ohjelman käynnistyessä sille oikeus muokata omia Google Sheets-dokumentteja.

Käyttöliittymä kutsuu workoutService-olion metodeja, joiden avulla käyttäjän syöttämät tiedot tallennetaan.

## Sovelluslogiikka
Sovelluksen loogisen datamallin muodostavat luokat [User](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/User.java), [Exercise](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/Exercise.java), [DoneExercise](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/DoneExercise.java), [Routine](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/Routine.java) ja [WorkoutSession](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/WorkoutSession.java), jotka kuvaavat käyttäjiä, harjoituksia, harjoitusohjelmia ja harjoituskertoja.

Toiminnallisista kokonaisuuksista vastaa luokkan [WorkoutService](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/WorkoutService.java) ainoa olio.

_WorkoutService_ pääsee käsiksi harjoituksiin, harjoitusohjelmiin ja harjoituskertoihin tietojen tallennuksesta vastaavan pakkauksessa _worksheetout.dao_ sijaitsevien rajapinnat _RoutineDao_ ja _WorkoutSessionDao_ toteuttavien luokkien kautta.

<img src="https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/documentation/otm-project-class-diagram.png" width="450">

## Tietojen pysyväistallennus
Pakkauksen _worksheetout.dao_ luokat _RoutineDao_, _SheetRoutineDao_, _WorkoutSessionDao_ ja _SheetWorkoutSessionDao_ huolehtivat tietojen tallettamisesta tiedostoihin.

## Päätoiminnallisuudet
### Harjoitusohjelman ja harjoituksen luominen
<img src="https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/documentation/add_routine_add_exercise.png" width="450">

## Ohjelman rakenteeseen jääneet heikkoudet
