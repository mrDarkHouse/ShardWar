package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Player;

import java.util.ArrayList;

public class Disarm extends Spell {

    public static class P extends SpellPrototype{

        private int duration;

        public P(int duration) {
            super("disarm", TargetType.SINGLE, FieldTarget.ENEMY, Tower.class);
            this.duration = duration;
        }

        @Override
        public String getTooltip() {
            return "Disarm selected tower for " + duration + " seconds";
        }

        @Override
        public Spell createSpell(Player player) {
            return new Disarm(player, this);
        }

    }
    public class DisarmEffect extends Effect<Tower>{

        public DisarmEffect(int duration) {
            super("disarm", false, duration, INone.class);
            this.owner = owner;
        }

        @Override
        public void apply() {
            owner.setDisarm(true);
        }

        @Override
        public void dispell() {
            owner.setDisarm(false);
        }
    }

    private int duration;

    public Disarm(Player owner, P prototype) {
        super(owner, prototype);
        duration = prototype.duration;
    }

    @Override
    public void use(ArrayList<GameObject> targets) {
        for (GameObject o:targets){
            o.addEffect(new DisarmEffect(duration).setOwner(((Tower) o)));//TODO remove cast
        }
    }


}
