# Blackjack
Et spill av "Blackjack" mot "Magnus" som representeres av datamaskinen.

[https://nav-deckofcards.herokuapp.com/#/](https://nav-deckofcards.herokuapp.com/#/)

## Implementasjon
Dette er en mikrotjeneste (REST-applikasjon) med spring-boot som er dokumentert i swagger

### Bygging
Denne applikasjonen bygges med [gradle](https://gradle.org).

Fra prosjektets rotkatalog (Linux/Unix-bruker)
```
./gradlew build
```
Windows (stå i rotkatalong)
```
gradlew build
```

### Start Applikasjon
Man kan starte applikasjonen på flere måter. Se eksempler nedenfor:

#### Bruk IntelliJ
Høyreklikk på klassen `BlackjackApplication` og velg `Run 'BlackjackApplication...'` 

#### Kjør som javaprogram
Kjør metoden `BlackjackApplication.main(...)`

#### Bruk gradle
Bruk gradle script direkte på rotkatalogen til prosjektet:
```
./gradlew bootRun
```
Kommandoen skrevet er for Linux/Unix brukere, for Windows så brukes `gradlew.bat` altså `gradles bootRun`

... eller gjør et gradle-bygg (`./gradlew build`), for deretter å kjøre jar-modulen
```
java -jar build/lib/blackjack-<version>-SNAPSHOT.jar
```

### Hvordan & Forutsetninger
* Hvis totalsummen til spiller og "Magnus" er like, så vinner spilleren
* Front-end er ikke implementert og et spill kjøres via swagger
    1) start applikasjonen (se avsnittet for starte applikasjonen)
    2) gå til swagger-grensesnitt:  [http://localhost:8080/blackjack/swagger-ui/index.html](http://localhost:8080/blackjack/swagger-ui/index.html)
    3) Åpne endepunktet `/play/{nick}` (`POST`) og trykk på knappen `Try It Out`
       * Fyll ut feltet `nick` som er kallenavnet til spilleren (kan ikke inneholde mellomrom)
       * Trykk på knappen `Execute`. Resultatet blir vist som json under `Server response`, samt at resultatet blir printet til konsoll

### Kjøring av enhetstester

#### Bruk IntelliJ
Høyreklikk på prosjektet `blackjack` og velg `Run 'Tests in 'blackjack''`

#### Bruk gradle
Linux/Unix:
```
./gradlew test --tests "*"
```
Windows:
```
gradlew test --tests "*"
```