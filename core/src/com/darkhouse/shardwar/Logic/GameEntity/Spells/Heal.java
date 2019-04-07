package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;

import java.util.ArrayList;

public class Heal extends Spell{

    public static class P extends SpellPrototype{

        private int healAmount;

        public P(int healAmount) {
            super("heal", NonTargetType.PLUS, FieldTarget.FRIENDLY, Tower.class, Wall.class);
            this.healAmount = healAmount;
        }

        @Override
        public String getTooltip() {
            return "Heal friendly units for " + healAmount + " hp";
        }

        @Override
        public Spell createSpell(Player player) {
            return new Heal(player, this);
        }
    }

    private int healAmount;

    public Heal(Player owner, P prototype) {
        super(owner, prototype);
        healAmount = prototype.healAmount;
    }

    @Override
    public void use(ArrayList<GameObject> targets) {
        for (GameObject g:targets){
            g.heal(healAmount);
        }
    }
}
