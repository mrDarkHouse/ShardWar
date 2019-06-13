package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class Vampire extends Spell {

    public static class P extends SpellPrototype{
        private int duration;
        private float healPercent;

        public P(int duration, float healPercent) {
            super("vampire", new TargetData[]{
                    new TargetData(TargetType.SINGLE, FieldTarget.FRIENDLY, Tower.class)
            });
            this.duration = duration;
            this.healPercent = healPercent;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("vampireTooltip1") + " " + FontLoader.colorString(String.valueOf(duration),5) + " " +
                    l.getWord("vampireTooltip2") + System.getProperty("line.separator") +
                    l.getWord("vampireTooltip3") + " " +
                    FontLoader.colorString(String.valueOf((healPercent)), 3) + "x " +
                    l.getWord("vampireTooltip4");
        }

        @Override
        public Spell createSpell(Player player) {
            return new Vampire(player, this);
        }
    }
    private static class VampireEffect extends Effect<Tower> implements Effect.IAfterAttack {
        private float healPercent;

        public VampireEffect(int duration, float healPercent) {
            super("vampire", true, duration, IAfterAttack.class);
            this.healPercent = healPercent;
        }

        @Override
        public void apply() {

        }

        @Override
        public void dispell() {

        }

        @Override
        public void afterAttack(GameObject target, int dmg) {
            owner.heal((int) (dmg*healPercent));
        }
    }

    private int duration;
    private float healPercent;

    public Vampire(Player owner, P prototype) {
        super(owner, prototype);
        this.duration = prototype.duration;
        this.healPercent = prototype.healPercent;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        Tower t = ((Tower) targets.get(0).get(0));
        t.addEffect(new VampireEffect(duration, healPercent).setOwner(t));
    }
}
