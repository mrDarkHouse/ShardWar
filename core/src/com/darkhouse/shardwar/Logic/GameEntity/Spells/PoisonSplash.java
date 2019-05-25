package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class PoisonSplash extends Spell {

    public static class P extends SpellPrototype{

        private int duration;
        private int dmg;
        private int finalDmg;

        public P(int duration, int dmg, int finalDmg) {
            super("poisonsplash", new TargetData[]{
                    new TargetData(TargetType.CORNER, FieldTarget.ENEMY, Tower.class, Wall.class)
            });
            this.duration = duration;
            this.dmg = dmg;
            this.finalDmg = finalDmg;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("poisonsplashTooltip1") + System.getProperty("line.separator") +
                    l.getWord("poisonsplashTooltip2") + " " +
                    FontLoader.colorString(String.valueOf(dmg), 7) + " " +
                    l.getWord("poisonsplashTooltip3") + " " +
                    FontLoader.colorString(String.valueOf(duration), 2) + " " +
                    l.getWord("poisonsplashTooltip4") + System.getProperty("line.separator") +
                    l.getWord("poisonsplashTooltip5") +
                    FontLoader.colorString(String.valueOf(finalDmg), 7) +
                    l.getWord("poisonsplashTooltip6");
        }

        @Override
        public Spell createSpell(Player player) {
            return new PoisonSplash(player,this);
        }
    }

    private class PoisonSplashEffect extends Effect{

        private int dmg;
        private int finalDmg;

        public PoisonSplashEffect(int duration, int dmg, int finalDmg) {
            super("poisonsplash", false, duration, INone.class);
            this.dmg = dmg;
            this.finalDmg = finalDmg;
        }

        @Override
        protected void act() {
            owner.dmg(dmg, PoisonSplash.this);
        }

        @Override
        public void apply() {

        }

        @Override
        public void dispell() {
            owner.dmg(finalDmg, PoisonSplash.this);
        }
    }

    private int duration;
    private int dmg;
    private int finalDmg;

    public PoisonSplash(Player owner, P prototype) {
        super(owner, prototype);
        this.duration = prototype.duration;
        this.dmg = prototype.dmg;
        this.finalDmg = prototype.finalDmg;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
            g.addEffect(new PoisonSplashEffect(duration, dmg, finalDmg).setOwner(g));
        }
    }
}
