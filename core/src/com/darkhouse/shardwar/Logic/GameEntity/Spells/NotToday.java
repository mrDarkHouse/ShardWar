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

public class NotToday extends Spell {

    public static class P extends SpellPrototype{

        private int duration;

        public P(int duration) {
            super("nottoday", new TargetData[]{
                    new TargetData(TargetType.HLINE, FieldTarget.FRIENDLY, Tower.class, Wall.class, Player.class)
            });
            this.duration = duration;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("nottodayTooltip1") + " " +
                    FontLoader.colorString(String.valueOf(duration), 2) + " " +
                    l.getWord("nottodayTooltip2") +
                    System.getProperty("line.separator") +
                    l.getWord("nottodayTooltip3");
        }

        @Override
        public Spell createSpell(Player player) {
            return new NotToday(player, this);
        }
    }

    private class NotTodayEffect extends Effect implements Effect.IGetDmg {

        public NotTodayEffect(int duration) {
            super("nottoday", true, duration, IGetDmg.class);
        }

        @Override
        public int getDmg(int dmg) {
            if(owner.getHealth() == 0){
                return 0;
            }else if(owner.getHealth() - dmg <= 0){
                return owner.getHealth() - 1;
            }else return dmg;
        }

        @Override
        public void apply() {

        }

        @Override
        public void dispell() {

        }
    }

    private int duration;

    public NotToday(Player owner, P prototype) {
        super(owner, prototype);
        this.duration = prototype.duration;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
            g.addEffect(new NotTodayEffect(duration).setOwner(g));
        }
    }
}
