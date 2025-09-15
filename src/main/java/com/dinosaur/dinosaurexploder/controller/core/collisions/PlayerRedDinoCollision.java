package com.dinosaur.dinosaurexploder.controller.core.collisions;

import com.dinosaur.dinosaurexploder.constants.EntityType;
import com.dinosaur.dinosaurexploder.constants.GameConstants;
import com.dinosaur.dinosaurexploder.controller.core.GameActions;
import com.dinosaur.dinosaurexploder.utils.AudioManager;

import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;

public class PlayerRedDinoCollision implements CollisionHandlerInterface{

    private final GameActions gameActions;

    public PlayerRedDinoCollision(GameActions gameActions) {
        this.gameActions = gameActions;
    }

    @Override
    public void register() {
        onCollisionBegin(EntityType.PLAYER, EntityType.RED_DINO, (player, redDino) -> {
            AudioManager.getInstance().playSound(GameConstants.PLAYER_HIT_SOUND);
            System.out.println("You touched a red dino !");
            gameActions.damagePlayer();
        });
    }
}
