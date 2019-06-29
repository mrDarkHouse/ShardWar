package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class ShieldsUp extends Spell {

    public static class P extends SpellPrototype{

        private int duration;
        private int shieldAmount;

        public P(int duration, int shieldAmount) {
            super("shieldup", new TargetData[]{
                    new TargetData(NonTargetType.FRAME, FieldTarget.FRIENDLY, Wall.class)
            });
            this.duration = duration;
            this.shieldAmount = shieldAmount;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("shieldupTooltip1") + " " +
                    FontLoader.colorString(String.valueOf(shieldAmount), 3) + " " +
                    l.getWord("shieldupTooltip2") + " " +
                    FontLoader.colorString(String.valueOf(duration), 5) + " " +
                    l.getWord("shieldupTooltip3") +
                    System.getProperty("line.separator") +
                    l.getWord("shieldupTooltip4");
        }

        @Override
        public Spell createSpell(Player player) {
            return new ShieldsUp(player, this);
        }
    }

    private class ShieldUpEffect extends Effect<Wall> implements Effect.IGetDmg {

        private int shieldAmount;
        private int currShield;

        public ShieldUpEffect(int duration, int shieldAmount) {
            super("shieldup", true, duration, IGetDmg.class);
            this.shieldAmount = shieldAmount;
        }

        @Override
        public int getDmg(int dmg) {
            if (currShield > 0){
                if(shieldAmount - dmg < 0){
                    shieldAmount = 0;
                    return dmg - shieldAmount;
                }else {
                    shieldAmount -= dmg;
                    return 0;
                }
            }else return dmg;
        }

        @Override
        public void apply() {
            currShield = shieldAmount;
        }

        @Override
        public void updateDuration(int duration) {
            super.updateDuration(duration);
            currShield = shieldAmount;
        }

        @Override
        public void dispell() {

        }
    }

    private int duration;
    private int shieldAmount;

    public ShieldsUp(Player owner, P prototype) {
        super(owner, prototype);
        this.duration = prototype.duration;
        this.shieldAmount = prototype.shieldAmount;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
            g.addEffect(new ShieldUpEffect(duration, shieldAmount).setOwner(((Wall) g)));
        }
    }
}
