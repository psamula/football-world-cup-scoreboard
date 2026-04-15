package org.example.scoreboard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Match")
class MatchTest {

    @Test
    @DisplayName("should start with 0-0 score")
    void shouldStartWithZeroZeroScore() {
        Match match = Match.start("Mexico", "Canada");

        assertThat(match.homeTeam()).isEqualTo("Mexico");
        assertThat(match.awayTeam()).isEqualTo("Canada");
        assertThat(match.homeScore()).isZero();
        assertThat(match.awayScore()).isZero();
    }

    @Test
    @DisplayName("should trim whitespace from team names")
    void shouldTrimWhitespaceFromTeamNames() {
        Match match = Match.start("  Mexico ", "\tCanada\n");

        assertThat(match.homeTeam()).isEqualTo("Mexico");
        assertThat(match.awayTeam()).isEqualTo("Canada");
    }

    @Test
    @DisplayName("should return new instance with updated score")
    void shouldReturnNewInstanceWithUpdatedScore() {
        Match match = Match.start("Mexico", "Canada").withScore(0, 5);

        assertThat(match.homeScore()).isZero();
        assertThat(match.awayScore()).isEqualTo(5);
    }

    @Test
    @DisplayName("should calculate total score as sum of both sides")
    void shouldCalculateTotalScoreAsSumOfBothSides() {
        assertThat(Match.start("Spain", "Brazil").withScore(10, 2).totalScore())
                .isEqualTo(12);
    }

    @Test
    @DisplayName("should reject null or blank team names")
    void shouldRejectNullOrBlankTeamNames() {
        assertThatThrownBy(() -> Match.start(null, "Canada"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Match.start("Mexico", " "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("should reject same team on both sides")
    void shouldRejectSameTeamOnBothSides() {
        assertThatThrownBy(() -> Match.start("Mexico", "mexico"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("same team");
    }

    @Test
    @DisplayName("should reject negative scores")
    void shouldRejectNegativeScores() {
        Match match = Match.start("Mexico", "Canada");
        assertThatThrownBy(() -> match.withScore(-1, 0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> match.withScore(0, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
