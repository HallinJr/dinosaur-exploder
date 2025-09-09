package com.dinosaur.dinosaurexploder.controller.core.collisions;

import com.dinosaur.dinosaurexploder.constants.EntityType;
import com.dinosaur.dinosaurexploder.constants.GameConstants;
import com.dinosaur.dinosaurexploder.utils.AudioManager;

import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class ProjectileEnemyProjectileCollision implements CollisionHandlerInterface{
    @Override
    public void register() {
        onCollisionBegin(EntityType.PROJECTILE, EntityType.ENEMY_PROJECTILE, (projectile, enemyProjectile) -> {
            spawn("explosion", enemyProjectile.getX() - 25, enemyProjectile.getY() - 30);
            AudioManager.getInstance().playSound(GameConstants.ENEMY_EXPLODE_SOUND);
            projectile.removeFromWorld();
            enemyProjectile.removeFromWorld();
        });
    }
}
