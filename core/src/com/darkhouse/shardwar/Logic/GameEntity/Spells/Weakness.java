package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Player;

import java.util.ArrayList;

public class Weakness extends Spell{

    public static class P extends SpellPrototype {

        private int dmgReduction;

        public P(int dmgReduction) {
            super("weakness", NonTargetType.ALL, FieldTarget.ENEMY, Tower.class);
            this.dmgReduction = dmgReduction;
        }

        @Override
        public String getTooltip() {
            return "Reduce all enemy towers damage by " + dmgReduction;
        }

        @Override
        public Spell createSpell(Player player) {
            return new Weakness(player, this);
        }
    }

    private class WeakEffect extends Effect<Tower>{

        private int startDmg;

        public WeakEffect(int duration) {
            super("weakness", false, duration, INone.class);
        }

        @Override
        public void apply() {
            startDmg = owner.getDmg();
            owner.setDmg(startDmg/2);
        }

        @Override
        public void dispell() {
            owner.setDmg(startDmg);
        }
    }

    public Weakness(Player owner, P prototype) {
        super(owner, prototype);
    }

    @Override
    public void use(ArrayList<GameObject> targets) {
        for (GameObject o:targets){
            o.addEffect(new WeakEffect(1).setOwner((Tower) o));
        }
    }
}
