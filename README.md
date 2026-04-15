# Football World Cup Score Board

A simple in-memory library that tracks live football matches and produces an
ordered summary. Implemented in Java 21 with JUnit 5 and AssertJ, following TDD.

## Build & Test

```
mvn test
```

Requires JDK 21 and Maven.

## Usage

```java
ScoreBoard board = new ScoreBoard();

board.startGame("Mexico", "Canada");
board.updateScore("Mexico", "Canada", 0, 5);

board.startGame("Spain", "Brazil");
board.updateScore("Spain", "Brazil", 10, 2);

List<Match> summary = board.getSummary();
// -> [Spain 10 - Brazil 2, Mexico 0 - Canada 5]
```

## Public API

| Method | Description |
| --- | --- |
| `startGame(home, away)` | Adds a new match at 0-0. |
| `updateScore(home, away, homeScore, awayScore)` | Replaces the score of an ongoing match. |
| `finishGame(home, away)` | Removes a match from the board. |
| `getSummary()` | Unmodifiable snapshot ordered by total score desc, ties broken by most recently added. |

## Design Notes

- **Match** is a Java `record`, giving us immutability, accessors, `equals`,
  `hashCode` and a canonical constructor for free.
- **ScoreBoard** stores matches in a `LinkedHashMap` keyed by a
  case-insensitive `(home, away)` pair. `LinkedHashMap` preserves insertion
  order on `put`-replace, so updating a score does not affect the recency
  ordering used for tie-breaking.
- **Summary ordering** relies on stable sort: the insertion-ordered list is
  reversed (so "most recently added" comes first), then sorted by total score
  descending. Ties naturally retain the reversed-insertion order.

## Assumptions

- **"Most recently added" means the moment `startGame` was called** - not
  the moment of the last score update. `updateScore` replaces the match in
  place and does *not* bump its recency. I chose this interpretation because
  (a) the brief says "added", not "updated", (b) otherwise a late-game goal
  would reshuffle the board in ways that feel wrong for a live scoreboard,
  and (c) `LinkedHashMap`'s replace-in-place semantics express it naturally.
- **Team identity is case-insensitive** ("Mexico" and "mexico" are the same
  team) but the original casing is preserved for display.
- **A team can only participate in one match at a time.** Starting a game
  for a team that is already playing throws.
- **Scores can change freely in either direction.** The brief does not
  require monotonic updates, and forbidding decreases would add API surface
  with no business justification.
- **The library is not thread-safe.** The brief does not mention concurrency,
  so callers are expected to synchronise externally if they need it.
- **No framework dependency.** The brief asks for a simple library, not a
  web service or microservice. Consumers own the lifecycle.
- **No persistent storage.** The brief asks for an in-memory collection, so
  the board's state lives for the lifetime of the `ScoreBoard` instance.
