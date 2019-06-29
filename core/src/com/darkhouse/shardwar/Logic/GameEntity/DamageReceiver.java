package com.darkhouse.shardwar.Logic.GameEntity;

public interface DamageReceiver {

    int dmg(int dmg, DamageSource source, boolean ignoreDefSpells);


}
