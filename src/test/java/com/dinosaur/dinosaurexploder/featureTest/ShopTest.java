package com.dinosaur.dinosaurexploder.featureTest;

import com.dinosaur.dinosaurexploder.exception.LockedShipException;
import com.dinosaur.dinosaurexploder.exception.LockedWeaponException;
import com.dinosaur.dinosaurexploder.model.HighScore;
import com.dinosaur.dinosaurexploder.model.TotalCoins;
import com.dinosaur.dinosaurexploder.utils.DataProvider;
import com.dinosaur.dinosaurexploder.utils.ShipUnlockChecker;
import com.dinosaur.dinosaurexploder.utils.WeaponUnlockChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;



public class ShopTest {

    private ShipUnlockChecker shipChecker;
    private MockDataProvider dataProvider;

    static class MockDataProvider implements DataProvider {
        private int score;
        private int coins;

        public void setHighScore(int score) {
            this.score = score;
        }

        public void setTotalCoins(int coins) {
            this.coins = coins;
        }

        @Override
        public HighScore getHighScore() {
            return new HighScore(score);
        }

        @Override
        public TotalCoins getTotalCoins() {
            return new TotalCoins(coins);
        }
    }

    @BeforeEach
    void setUp() {
        dataProvider = new MockDataProvider();
        shipChecker = new ShipUnlockChecker(dataProvider);
    }

    @Test
    void shipUnlocks_whenHighScoreAndCoinsAreEnough() {
        dataProvider.setHighScore(350);
        dataProvider.setTotalCoins(150);
        assertDoesNotThrow(() -> shipChecker.check(5));
    }

    @Test
    void shipLocked_whenLowScoreButEnoughCoins() {
        dataProvider.setHighScore(200);
        dataProvider.setTotalCoins(200);
        LockedShipException ex = assertThrows(LockedShipException.class, () -> shipChecker.check(5));
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isEmpty());
    }

    @Test
    void shipLocked_whenEnoughScoreButLowCoins() {
        dataProvider.setHighScore(350);
        dataProvider.setTotalCoins(50);
        LockedShipException ex = assertThrows(LockedShipException.class, () -> shipChecker.check(5));
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isEmpty());
    }

    @Test
    void shipLocked_whenLowScoreAndLowCoins() {
        dataProvider.setHighScore(100);
        dataProvider.setTotalCoins(100);
        LockedShipException ex = assertThrows(LockedShipException.class, () -> shipChecker.check(6));
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isEmpty());
    }

    @Test
    void shipUnlocks_whenNoRequirements() {
        dataProvider.setHighScore(0);
        dataProvider.setTotalCoins(0);
        assertDoesNotThrow(() -> shipChecker.check(1));
    }

    @Test
    void weaponUnlocks_whenHighScoreAndCoinsAreEnough() {
        dataProvider.setHighScore(150);
        dataProvider.setTotalCoins(10);
        WeaponUnlockChecker weaponChecker = new WeaponUnlockChecker(dataProvider);
        assertDoesNotThrow(() -> weaponChecker.check(3));
    }

    @Test
    void weaponLocked_whenLowScoreButEnoughCoins() {
        dataProvider.setHighScore(50);
        dataProvider.setTotalCoins(10);
        WeaponUnlockChecker weaponChecker = new WeaponUnlockChecker(dataProvider);
        LockedWeaponException ex = assertThrows(LockedWeaponException.class, () -> weaponChecker.check(3));
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isEmpty());
    }

    @Test
    void weaponLocked_whenEnoughScoreButLowCoins() {
        dataProvider.setHighScore(150);
        dataProvider.setTotalCoins(5);
        WeaponUnlockChecker weaponChecker = new WeaponUnlockChecker(dataProvider);
        LockedWeaponException ex = assertThrows(LockedWeaponException.class, () -> weaponChecker.check(3));
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isEmpty());
    }

    @Test
    void weaponLocked_whenLowScoreAndLowCoins() {
        dataProvider.setHighScore(50);
        dataProvider.setTotalCoins(5);
        WeaponUnlockChecker weaponChecker = new WeaponUnlockChecker(dataProvider);
        LockedWeaponException ex = assertThrows(LockedWeaponException.class, () -> weaponChecker.check(3));
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isEmpty());
    }

    @Test
    void weaponUnlocks_whenNoRequirements() {
        dataProvider.setHighScore(0);
        dataProvider.setTotalCoins(0);
        WeaponUnlockChecker weaponChecker = new WeaponUnlockChecker(dataProvider);
        assertDoesNotThrow(() -> weaponChecker.check(1));
    }

}
