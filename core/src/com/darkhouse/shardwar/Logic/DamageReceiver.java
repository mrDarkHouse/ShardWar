package com.darkhouse.shardwar.Logic;

import com.badlogic.gdx.math.Vector2;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;

public interface DamageReceiver {

    void dmg(int dmg, GameObject source);


}
