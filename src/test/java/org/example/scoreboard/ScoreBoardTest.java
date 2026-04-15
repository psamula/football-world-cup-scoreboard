package org.example.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ScoreBoard")
class ScoreBoardTest {

    private ScoreBoard scoreBoard;

    @BeforeEach
    void setUp() {
        scoreBoard = new ScoreBoard();
    }

    @Nested
    @DisplayName("startGame")
    class StartGame {

        @Test
        @DisplayName("should appear in summary with 0-0 score")
        void shouldAppearInSummaryWithZeroZeroScore() {
            scoreBoard.startGame("Mexico", "Canada");

            assertThat(scoreBoard.getSummary()).hasSize(1);
            Match match = scoreBoard.getSummary().get(0);
            assertThat(match.homeTeam()).isEqualTo("Mexico");
            assertThat(match.awayTeam()).isEqualTo("Canada");
            assertThat(match.homeScore()).isZero();
            assertThat(match.awayScore()).isZero();
        }

        @Test
        @DisplayName("should throw when starting the same match twice")
        void shouldThrowWhenStartingSameMatchTwice() {
            scoreBoard.startGame("Mexico", "Canada");

            assertThatThrownBy(() -> scoreBoard.startGame("Mexico", "Canada"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("already");
        }

        @Test
        @DisplayName("should throw when a team is already playing")
        void shouldThrowWhenTeamIsAlreadyPlaying() {
            scoreBoard.startGame("Mexico", "Canada");

            assertThatThrownBy(() -> scoreBoard.startGame("Mexico", "Brazil"))
                    .isInstanceOf(IllegalStateException.class);
            assertThatThrownBy(() -> scoreBoard.startGame("Spain", "Canada"))
                    .isInstanceOf(IllegalStateException.class);
        }
    }
}
