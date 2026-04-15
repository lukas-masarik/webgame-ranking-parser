# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Kotlin/Maven CLI tool for parsing and analyzing rankings from the online strategy game https://webgame.cz. Ranking data (tab-separated text files) is stored per epoch in `src/main/resources/rankings/{lands,alliances}/`. The UI and all output are in Czech.

## Build Commands

- **Compile**: `mvn clean compile`
- **Run tests**: `mvn test`
- **Run a single test class**: `mvn test -Dtest=LandsRankingParserTest`
- **Package fat JAR**: `mvn clean package` → produces `target/WebgameRankingParser-<version>.jar`
- **Run JAR**: `java -jar target/WebgameRankingParser-<version>.jar`

## Architecture

Entry point: `WebgameRankingParser.kt` — detects IDE vs JAR execution context (`EAppTrigger`), then hands off to `SimpleInputReader` which drives a CLI menu flow.

Three processing modes per ranking type (lands / alliances):
- **List** — sort and display rankings
- **Filter** — narrow by a parameter (player, alliance, state system, etc.)
- **Aggregate** — group and aggregate across epochs

Key layers:
- **`services/inputreaders/`** — `InputReader` interface + `SimpleInputReader` (CLI prompts, creates the appropriate processor)
- **`services/parsers/`** — `AbstractRankingParser<T>` handles resource loading for both IDE and JAR modes; `LandsRankingParser` and `AlliancesRankingParser` use regex to parse tab-separated ranking files
- **`services/processors/`** — strategy pattern: `List*`, `Filter*`, `Aggregate*` processors per ranking type, with shared filtering logic in `Abstract*RankingProcessor` base classes
- **`dto/`** — `LandsRankingRow`, `AlliancesRankingRow` and their parent ranking objects
- **`enums/`** — configuration enums for ranking type, sort attributes, filtering/grouping parameters

## Adding a New Epoch

1. Add ranking text files to `src/main/resources/rankings/lands/<N>.txt` and `src/main/resources/rankings/alliances/<N>.txt`
2. Bump version in `pom.xml` (format: `2.4.<epoch_number>`)
3. Update "Last supported epoch" in `README.md`

## Conventions

- Kotlin 2.1 targeting JVM 23, runs on Java 25, official Kotlin code style
- JUnit 5 for tests (parser regex tests in `src/test/kotlin/me/masi/services/parsers/`)
- Dual execution context: resource loading differs between IDE (filesystem) and JAR (zip filesystem) — both paths must be maintained in parsers
