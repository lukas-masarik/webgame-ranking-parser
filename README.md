# Webgame Ranking Parser

Tool for browsing and analyzing historical rankings from the online strategy game [webgame.cz](https://webgame.cz).

Last supported epoch: **#189**.

## What it can do

Two rankings are supported: **lands** (zebricek zemi) and **alliances** (zebricek alianci). For each one you can:

- **List** — browse the ranking, sorted by prestige, area, or epoch, limited to any rank or epoch range.
- **Filter** — search within the ranking:
    - lands — by player, alliance, state system (vlada), or land number
    - alliances — by alliance tag, chairman, or member count
- **Aggregate** — group across epochs and aggregate by occurrence, prestige, or area:
    - lands — group by player, alliance, state system, or land number
    - alliances — group by tag, chairman, or member count

## How to run

Download the packaged JAR and run it:

```
java -jar WebgameRankingParser-<version>.jar
```

The app guides you through a simple Czech menu — pick a ranking, pick a mode, answer the prompts.

## Build from source

Requires Java 25 and Maven.

```
mvn clean package
java -jar target/WebgameRankingParser-<version>.jar
```
