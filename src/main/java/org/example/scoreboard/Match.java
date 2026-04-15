package org.example.scoreboard;

public record Match(String homeTeam, String awayTeam, int homeScore, int awayScore) {

    public Match {
        requireTeam(homeTeam, "home team");
        homeTeam = homeTeam.trim();
        awayTeam = awayTeam.trim();
        requireTeam(awayTeam, "away team");
        requireDifferentTeams(homeTeam, awayTeam);
        requireNonNegative(homeScore, "home score");
        requireNonNegative(awayScore, "away score");
    }

    public static Match start(String homeTeam, String awayTeam) {
        return new Match(homeTeam, awayTeam, 0, 0);
    }

    public Match withScore(int newHomeScore, int newAwayScore) {
        return new Match(homeTeam, awayTeam, newHomeScore, newAwayScore);
    }

    public int totalScore() {
        return homeScore + awayScore;
    }

    @Override
    public String toString() {
        return homeTeam + " " + homeScore + " - " + awayTeam + " " + awayScore;
    }

    private static void requireTeam(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }

    private static void requireDifferentTeams(String home, String away) {
        if (home.equalsIgnoreCase(away)) {
            throw new IllegalArgumentException("Home and away cannot be the same team");
        }
    }

    private static void requireNonNegative(int score, String label) {
        if (score < 0) {
            throw new IllegalArgumentException(label + " must be non-negative");
        }
    }
}
