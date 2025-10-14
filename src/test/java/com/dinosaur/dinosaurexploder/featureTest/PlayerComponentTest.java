package com.dinosaur.dinosaurexploder.featureTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.dinosaur.dinosaurexploder.components.PlayerComponent;
import com.dinosaur.dinosaurexploder.view.DinosaurGUI;

public class PlayerComponentTest {

    static class TestPlayerComponent extends PlayerComponent { //Expose protected methods for testing
        void attachTo(Entity entity) {
            this.entity = entity;
        }
    }
    
    private TestPlayerComponent player;
    private ViewComponent viewComponent;
    private Entity entity; 

    @BeforeEach
    void setup() {
        player = new TestPlayerComponent();

        entity = mock(Entity.class, RETURNS_DEEP_STUBS);
        viewComponent = mock(ViewComponent.class); 

        when(entity.getViewComponent()).thenReturn(viewComponent);
        when(entity.getWidth()).thenReturn(50.0); //Example width
        when(entity.getHeight()).thenReturn(50.0); //Example height

        player.attachTo(entity); //Attach the mocked entity to the player
    }

    @Test //kanske ta bort eftersom inte används men kan vara relevant för rapport som hittad bug eller vidar utveckling
    void invincibleModeChangesOpacity() {
        player.setInvincible(true);

        assertTrue(player.isInvincible());
        verify(viewComponent).setOpacity(0.5);
    }

    @Test //Samma kommentar som ovan
    void invincibleModeRestoresOpacity() {
        player.setInvincible(true);
        player.setInvincible(false);

        assertFalse(player.isInvincible());
        verify(viewComponent).setOpacity(1.0);
    }

    @Test
    void moveUpStopsAtTop() {
        when(entity.getY()).thenReturn(-0.1);
        player.moveUp();
        verify(entity, never()).translateY(anyDouble());
    }

    @Test
    void moveDownStopsAtBottom() {
        double height = 100.0;
        when(entity.getHeight()).thenReturn(height);
        when(entity.getY()).thenReturn(DinosaurGUI.HEIGHT - height);

        player.moveDown();
        verify(entity, never()).translateY(anyDouble());
    }

    @Test
    void moveLeftStopsAtLeftEdge() {
        when(entity.getX()).thenReturn(-0.1);
        player.moveLeft();
        verify(entity,never()).translateX(anyDouble());
    }

    @Test
    void moveRightStopsAtRightEdge() {
        double width = 100.0;
        when(entity.getWidth()).thenReturn(width);
        when(entity.getX()).thenReturn(DinosaurGUI.WIDTH - width);

        player.moveRight();
        verify(entity,never()).translateX(anyDouble());
    }

    //Eventuellt tester för när man rör sig inom bounds?
    //@Test
    //void moveUp_whenInBounds() { //javafx/fxgl animation graphics error
    //    when(entity.getY()).thenReturn(0.0);
    //    player.moveUp();
    //    verify(entity).translateY(-8.0);
    //}
}
