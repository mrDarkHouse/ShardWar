package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;

import java.util.ArrayList;

public class Dispell extends Spell {

    public static class P extends SpellPrototype{

        public P() {
            super("dispell", new TargetData[]{
                    new TargetData(TargetType.ALL, FieldTarget.ALL, Tower.class, Wall.class, Player.class)
            });
        }

        @Override
        public String getTooltip() {
            return "";
        }

        @Override
        public Spell createSpell(Player player) {
            return new Dispell(player, this);
        }
    }


    public Dispell(Player owner, P prototype) {
        super(owner, prototype);
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (ArrayList<GameObject> a:targets) {
            for (GameObject g:a){
//                g.dispell();
            }
        }
    }
}
