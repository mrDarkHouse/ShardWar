package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TimerTask;

public class FanOfKnives extends Spell {

    public static class P extends SpellPrototype{
        private int[] dmg;

        public P(int dmg1, int dmg2) {
            super("fanofknives", new TargetData[]{
                    new TargetData(TargetType.PLUS, FieldTarget.ENEMY, Tower.class, Wall.class)
            });
            this.dmg = new int[]{dmg1, dmg2};
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("fanofknivesTooltip1") + System.getProperty("line.separator") +
                    l.getWord("fanofknivesTooltip2") + " " +
                    FontLoader.colorString(String.valueOf(dmg[0]), 4) + " " +
                    l.getWord("fanofknivesTooltip3") + System.getProperty("line.separator") +
                    l.getWord("fanofknivesTooltip4") + " " +
                    FontLoader.colorString(String.valueOf(dmg[1]), 4) + " " +
                    l.getWord("fanofknivesTooltip5");
        }

        @Override
        public Spell createSpell(Player player) {
            return new FanOfKnives(player, this);
        }
    }

    public FanOfKnives(Player owner, P prototype) {
        super(owner, prototype);
        this.dmg = prototype.dmg;
    }

    private int[] dmg;

    private void useWave(ArrayList<GameObject> targets, int i){
        for (GameObject g : targets) {
            g.dmg(dmg[i], this, true);
        }
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        useWave(new ArrayList<>(Collections.singleton(targets.get(0).get(0))), 0);
        ShardWar.fightScreen.t.schedule(new TimerTask() {
            @Override
            public void run() {
                useWave(targets.get(0), 1);
                cancel();
            }
        }, 500);
    }
}
