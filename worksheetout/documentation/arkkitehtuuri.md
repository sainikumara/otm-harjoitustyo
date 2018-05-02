# Arkkitehtuurikuvaus

## Rakenne
Ohjelman rakenne noudattelee kolmitasoista kerrosarkkitehtuuria, ja koodin pakkausrakenne on seuraava:

<img src="https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/documentation/package-diagram.png" width="160">

Pakkaus _worksheetout.ui_ sisältää tekstikäyttöliittymän, _worksheetout.domain_ sovelluslogiikan ja _worksheetout.dao_ tietojen tallennuksesta vastaavan koodin.

## Käyttöliittymä
Käyttöliittymä on yksinkertainen tekstikäyttöliittymä, joka pyytää käyttäjä syöttämään ensin omat tietonsa, sitten harjoitusohjelman tiedot ja sitten harjoitusohjelmaan liittyvien harjoituskertojen tiedot.

Käyttöliittymä kutsuu workoutService-olion metodeja, joiden avulla käyttäjän syöttämät tiedot tallennetaan.

## Sovelluslogiikka
Sovelluksen loogisen datamallin muodostavat luokat [User](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/User.java), [Exercise](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/Exercise.java), [DoneExercise](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/DoneExercise.java), [Routine](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/Routine.java) ja [WorkoutSession](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/src/main/java/worksheetout/domain/WorkoutSession.java), jotka kuvaavat käyttäjiä, harjoituksia, harjoitusohjelmia ja harjoituskertoja:

<img src="https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/documentation/otm-project-class-diagram.png" width="450">

## Tietojen pysyväistallennus
Pakkauksen _worksheetout.dao_ luokat _RoutineDao_, _SheetRoutineDao_, _WorkoutSessionDao_ ja _SheetWorkoutSessionDao_ huolehtivat tietojen tallettamisesta tiedostoihin.

## Päätoiminnallisuudet
<img src="https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/documentation/sequence_diagram-01.png" width="450">

## Ohjelman rakenteeseen jääneet heikkoudet
