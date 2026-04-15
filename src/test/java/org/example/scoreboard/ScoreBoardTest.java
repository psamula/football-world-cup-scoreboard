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
    @DisplayName("updateScore")
    class UpdateScore {

        @Test
        @DisplayName("should update the score of an ongoing match")
        void shouldUpdateScoreOfAnOngoingMatch() {
            scoreBoard.startGame("Spain", "Brazil");

            scoreBoard.updateScore("Spain", "Brazil", 10, 2);

            Match match = scoreBoard.getSummary().get(0);
            assertThat(match.homeScore()).isEqualTo(10);
            assertThat(match.awayScore()).isEqualTo(2);
        }

        @Test
        @DisplayName("should throw when updating a match not on the board")
        void shouldThrowWhenUpdatingMatchNotOnTheBoard() {
            assertThatThrownBy(() -> scoreBoard.updateScore("Spain", "Brazil", 1, 0))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("not on the board");
        }

        @Test
        @DisplayName("should reject negative scores")
        void shouldRejectNegativeScores() {
            scoreBoard.startGame("Spain", "Brazil");

            assertThatThrownBy(() -> scoreBoard.updateScore("Spain", "Brazil", -1, 0))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should reject null or blank team names")
        void shouldRejectNullOrBlankTeamNames() {
            assertThatThrownBy(() -> scoreBoard.updateScore(null, "Brazil", 1, 0))
                    .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> scoreBoard.updateScore("Spain", " ", 1, 0))
                    .isInstanceOf(IllegalArgumentException.class);
        }
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
