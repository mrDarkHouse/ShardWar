package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;

import java.util.ArrayList;

public class Rejuvenation extends Spell {

    public static class P extends SpellPrototype{
        private int duration;
        private int value;

        public P(int duration, int value) {
            super("rejuvenation", new TargetData[]{
                    new TargetData(NonTargetType.ALL, FieldTarget.FRIENDLY, Tower.class, Wall.class)
            });
            this.duration = duration;
            this.value = value;
        }

        @Override
        public String getTooltip() {
            return "";
        }

        @Override
        public Spell createSpell(Player player) {
            return new Rejuvenation(player, this);
        }
    }
    private static class RejuvenationEffect extends Effect{
        private int value;

        public RejuvenationEffect(int duration, int value) {
            super("rejuvenation", true, duration, INone.class);
            this.value = value;
        }

        @Override
        public void apply() {

        }

        @Override
        public void dispell() {

        }

        @Override
        protected void act() {
            owner.heal(value);
            super.act();
        }
    }

    private int duration;
    private int value;

    public Rejuvenation(Player owner, P prototype) {
        super(owner, prototype);
        this.duration = prototype.duration;
        this.value = prototype.value;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
            g.addEffect(new RejuvenationEffect(duration, value).setOwner(g));
        }
    }
}
