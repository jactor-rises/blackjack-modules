# Blackjack
Et spill av "Blackjack" mot "Magnus" som representeres av datamaskinen.

[https://nav-deckofcards.herokuapp.com/#/](https://nav-deckofcards.herokuapp.com/#/)

## Implementasjon
Dette er en mikrotjeneste (REST-applikasjon) med spring-boot som er dokumentert i swagger

### Bygging
Denne applikasjonen bygges med [gradle](https://gradle.org).

Fra prosjektets rotkatalog
```
./gradlew build
```

### Kjøring
Man kan starte applikasjonen på flere måter. Se blant annet avsnittene nedenfor og deretter bruk 

#### Bruk IntelliJ
Høyreklikk på klassen `BlackjackApplication` og velg `Run 'BlackjackApplication...'` 

#### Kjør som javaprogram
Kjør metoden `BlackjackApplication.main(...)`

#### Bruk [gradle](https://gradle.org):
Kjør direkte
```
./gradlew bootRun
```
... eller etter et gradle-bygg (`./gradlew build`), for deretter å kjøre jar-modulen
```
java -jar build/lib/blackjack-<version>-SNAPSHOT.jar
```

### Forutsetninger
