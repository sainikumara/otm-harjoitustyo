# OTM-harjoitustyö

## worksheetout
Treenien seurannan helpottamista varten rakennettu yksinkertainen sovellus.

### Dokumentaatio

[Vaatimusmäärittely](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/documentation/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/documentation/arkkitehtuuri.md)

[Työaikakirjanpito](https://github.com/sainikumara/otm-harjoitustyo/blob/master/worksheetout/documentation/tuntikirjanpito.md)

## Releaset

[Viikko 5](https://github.com/sainikumara/otm-harjoitustyo/releases/tag/v0.1-alpha)

## Komentorivitoiminnot

### Ohjelman suorittaminen
Ohjelman voi suorittaa hakemistossa worksheetout komennolla
```
mvn compile exec:java -Dexec.mainClass=worksheetout.ui.TestUI
```

### Testaus

Testit suoritetaan komennolla

```
mvn test
```

Testikattavuusraportti luodaan komennolla

```
mvn jacoco:report
```

Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto _target/site/jacoco/index.html_
