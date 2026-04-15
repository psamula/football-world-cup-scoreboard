package org.example.scoreboard;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class ScoreBoard {

    private final Map<MatchKey, Match> matchByKey = new LinkedHashMap<>();

    public void startGame(String homeTeam, String awayTeam) {
        Match match = Match.start(homeTeam, awayTeam);
        requireNotAlreadyOnBoard(match);
        requireBothTeamsAreFree(match);
        matchByKey.put(keyOf(match), match);
    }

    public List<Match> getSummary() {
        return List.copyOf(matchByKey.values());
    }

    private void requireNotAlreadyOnBoard(Match match) {
        if (matchByKey.containsKey(keyOf(match))) {
            throw new IllegalStateException(
                    "Match between " + match.homeTeam() + " and " + match.awayTeam()
                            + " is already on the board");
        }
    }

    private void requireBothTeamsAreFree(Match match) {
        Stream.of(match.homeTeam(), match.awayTeam())
                .forEach(this::requireTeamIsFree);
    }

    private void requireTeamIsFree(String team) {
        if (isPlaying(team)) {
            throw new IllegalStateException(team + " is already playing in another match");
        }
    }

    private boolean isPlaying(String team) {
        String normalizedTeam = normalize(team);
        return matchByKey.values().stream()
                .flatMap(match -> Stream.of(match.homeTeam(), match.awayTeam()))
                .map(ScoreBoard::normalize)
                .anyMatch(normalizedTeam::equals);
    }

    private static MatchKey keyOf(Match match) {
        return MatchKey.of(match.homeTeam(), match.awayTeam());
    }

    private static String normalize(String team) {
        return team.trim().toLowerCase(Locale.ROOT);
    }

    private record MatchKey(String home, String away) {
        static MatchKey of(String home, String away) {
            return new MatchKey(normalize(home), normalize(away));
        }
    }
}
