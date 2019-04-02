package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;

import java.util.ArrayList;

public class FireBreath extends Spell{

    public static class P extends Spell.SpellPrototype{

        private int dmg;

        public P(int dmg) {
            super("firebreath", TargetType.HLINE, Tower.class, Wall.class);
            this.dmg = dmg;
        }

        @Override
        public Spell createSpell(Player player) {
            return new FireBreath(this, player);
        }
    }

    private int dmg;

    public FireBreath(P prototype, Player owner) {
        super(owner, prototype);
        this.dmg = prototype.dmg;
    }

    @Override
    public void use(ArrayList<GameObject> targets) {
        for (GameObject g: targets) {
            g.dmg(dmg, this);
        }
    }

}
