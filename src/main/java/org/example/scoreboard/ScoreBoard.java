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

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        Match currentMatch = findOnScoreBoard(homeTeam, awayTeam);
        matchByKey.put(keyOf(currentMatch), currentMatch.withScore(homeScore, awayScore));
    }

    public List<Match> getSummary() {
        return List.copyOf(matchByKey.values());
    }

    private Match findOnScoreBoard(String homeTeam, String awayTeam) {
        requireTeamName(homeTeam, "home team");
        requireTeamName(awayTeam, "away team");
        Match currentMatch = matchByKey.get(MatchKey.of(homeTeam, awayTeam));
        if (currentMatch == null) {
            throw new IllegalStateException(
                    "Match between " + homeTeam + " and " + awayTeam + " is not on the board");
        }
        return currentMatch;
    }

    private static void requireTeamName(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
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
