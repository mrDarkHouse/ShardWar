package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Player;

import java.util.ArrayList;

public class Combiner extends Spell{

    public Combiner(Player owner, SpellPrototype prototype) {
        super(owner, prototype);
    }

    @Override
    public void use(ArrayList<GameObject> targets) {

    }
}
