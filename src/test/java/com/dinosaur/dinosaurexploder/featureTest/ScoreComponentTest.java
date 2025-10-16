package com.dinosaur.dinosaurexploder.featureTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.dinosaur.dinosaurexploder.components.ScoreComponent;
import com.dinosaur.dinosaurexploder.constants.GameConstants;
import com.dinosaur.dinosaurexploder.model.HighScore;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScoreComponentTest {
    private static HighScore highScore;
    private static ScoreComponent scoreComponent;

    @BeforeEach
    void createScoreComponent() {
        highScore = new HighScore();
        resetStaticHighScore();
        this.scoreComponent = new ScoreComponent();
    }

    private void resetStaticHighScore() {
        try {
            Field f = ScoreComponent.class.getDeclaredField("highScore");
            f.setAccessible(true);
            f.set(null, new HighScore());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HighScore getStaticHighScore() {
        try {
            Field f = ScoreComponent.class.getDeclaredField("highScore");
            f.setAccessible(true);
            return (HighScore) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Sets highscore to 10 and compare highscore with expected value")
    void constructorHighScore_SetHighScoreToTen_ShouldAssertEqualsToTen() {
        // Arrange
        int score = 10;
        highScore = new HighScore(score);

        // Act
        int fetchedHighScore = highScore.getHigh();

        // Assert
        assertEquals(10, fetchedHighScore);
    }

    @Test
    @DisplayName("Set highscore to 0 and compares if the fetchedHighScore is 0.")
    void constructorHighScore_WhenInitialized_ShouldBeZero() {
        // Arrange
        int score = 0;
        highScore = new HighScore(score);

        // Act
         int fetchedHighScore = highScore.getHigh();

        // Assert
        assertEquals(0, fetchedHighScore);
    }

    // TODO Should not be possible to reverse the score, should be an error thrown if the value is negative.
    // TODO Should fail, change assert to build better code. Should change to highscore above current highscore
    @Test
    @DisplayName("Set highscore number to a negative number, should throw an IllegalArgumentException.")
    @Disabled("Enable after implementing validation")
    void constructorHighScore_WithNegativeValue_ShouldThrowIllegalArgumentException() {
        // Arrange
        int negativeScore = -10;
        highScore = new HighScore(negativeScore);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new HighScore(negativeScore);
        });
        assertTrue(exception.getMessage().contains("negative") ||
                    exception.getMessage().contains("must be positive"));
    }

    @Test
    @DisplayName("Uses ScoreComponent to increment score and set a new highscore")
    void incrementScore_ByFive_ShouldSetNewHighscore() {
        // Arrange
        int score = 5;

        // Act
        scoreComponent.incrementScore(score);
        int currentHighScore = getStaticHighScore().getHigh();

        // Assert
        assertEquals(5, scoreComponent.getScore());
        assertEquals(5, currentHighScore);
    }

    @Test
    @DisplayName("Uses ScoreComponent to set a new highscore after a set highscore")
    void incrementScore_ByFiveTwice_HighScoreShouldBeTen() {
        // Arrange
        int score = 5;
        scoreComponent.incrementScore(score);
        int currentHighScore = getStaticHighScore().getHigh();

        // Act
        scoreComponent.incrementScore(score);
        currentHighScore = getStaticHighScore().getHigh();

        // Assert
        assertEquals(10, scoreComponent.getScore());
        assertEquals(10, currentHighScore);
    }

    @Test
    @DisplayName("IncrementScore should not be allowed to set a negative number. "+
            "The method should throw an IllegalArgumentException in case of negative number")
    @Disabled("Enable after implementing validation")
    void incrementScore_ByNegativeValue_ShouldThrowIllegalArgumentException() {
        // Arrange
        int score = 10;
        int negativeScore = -5;
        scoreComponent.incrementScore(score);
        int currentHighScore = getStaticHighScore().getHigh();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            scoreComponent.incrementScore(negativeScore);
        });

        assertTrue(exception.getMessage().contains("negative") ||
                    exception.getMessage().contains("must be positive"));
    }

    @Test
    @DisplayName("Checks if the file highscore is created.")
    void saveHighScore_CheckIfFileExists_ShouldBeTrue() {
        // Arrange
        scoreComponent.incrementScore(1);
        Path filePath = Paths.get(GameConstants.HIGH_SCORE_FILE);
        
        // Act
        boolean exists = Files.exists(filePath);
        
        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Initial score should be zero")
    void getScore_WhenInitialized_ShouldBeZero() {
        assertEquals(0, scoreComponent.getScore());
    }

    @Test
    @DisplayName("SetScore should update score value")
    void setScore_WithFifty_ShouldUpdateScore() {
        // Arrange
        int score = 50;

        // Act
        scoreComponent.setScore(score);

        // Assert
        assertEquals(50, scoreComponent.getScore());
    }

    @Test
    @DisplayName("IncrementScore should add to current score")
    void incrementScore_ByFive_ShouldIncreaseTotalScore() {
        // Arrange
        int score = 10;
        int addedValue = 5;
        scoreComponent.setScore(score);

        // Act
        scoreComponent.incrementScore(addedValue);

        // Assert
        assertEquals(15, scoreComponent.getScore());
    }

    @Test
    @DisplayName("High score should persist across component instances")
    void newComponent_AfterHighScoreSaved_ShouldLoadPreviousHighScore() {
        // Arrange
        int score = 150;
        scoreComponent.incrementScore(score);

        // Act
        int newScore = 1;
        ScoreComponent newComponent = new ScoreComponent();
        newComponent.incrementScore(newScore);
        int currentHighScore = getStaticHighScore().getHigh();
        Path filePath = Paths.get(GameConstants.HIGH_SCORE_FILE);

        // Assert
        assertEquals(150, currentHighScore);
        assertTrue(Files.exists(filePath));
    }

    @Test
    @DisplayName("Multiple increments should accumulate correctly")
    void incrementScore_MultipleTimes_ShouldAccumulateCorrectly() {
        // Arrange & Act
        scoreComponent.incrementScore(10);
        scoreComponent.incrementScore(20);
        scoreComponent.incrementScore(30);

        // Assert
        assertEquals(60, scoreComponent.getScore());
        assertEquals(60, getStaticHighScore().getHigh());
    }

    @Test
    @DisplayName("Large score values should be handled") 
    void incremetScore_WithLargeValues_ShouldWork() {
        // Arrange
        int largeScore = 1000000000;

        // Act
        scoreComponent.incrementScore(largeScore);

        // Assert
        assertEquals(1000000000, getStaticHighScore().getHigh());
        assertEquals(1000000000, scoreComponent.getScore());
    }

    @Test
    @DisplayName("Maximum value of integer as score should be handled") 
    void incremetScore_WithMaximumIntegerValues_ShouldWork() {
        // Arrange
        int maxValue = Integer.MAX_VALUE;
        int largeScore = maxValue;

        // Act
        scoreComponent.incrementScore(largeScore);

        // Assert
        assertEquals(maxValue, getStaticHighScore().getHigh());
        assertEquals(maxValue, scoreComponent.getScore());
    }

    @Test
    @DisplayName("Score near max value should not overflow")
    @Disabled("Enable after implementing overflow protection")
    void incrementScore_NearMaxValue_ShouldNotOverflow() {
        // Arrange
        int largeScore = Integer.MAX_VALUE -10;
        scoreComponent.incrementScore(largeScore);

        // Act & Assert
        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> {
            scoreComponent.incrementScore(20); // Would overflow the integer value to Integer.MIN_VALUE;
        });
        assertTrue(exception.getMessage().contains("overflow") ||
                    exception.getMessage().contains("exceeds maximum")); 
    }
    
    @Test
    @DisplayName("Safe increment near max value should succeed")
    void incrementScore_SafeIncrementNearMax_ShouldWork() {
        // Arrange
        int maxValue = Integer.MAX_VALUE;
        int largeScore = maxValue - 100;
        scoreComponent.incrementScore(largeScore);

        // Act
        scoreComponent.incrementScore(50);

        // Assert
        assertEquals(maxValue - 50, scoreComponent.getScore());
        assertEquals(maxValue - 50, getStaticHighScore().getHigh());
    }
}
