package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.*;
import com.darkhouse.shardwar.Logic.Slot.Slot;
import com.darkhouse.shardwar.Player;

import java.util.ArrayList;

public class Combiner extends Spell{

    public static class P extends SpellPrototype{

        public P() {
            super("combiner", new TargetData[]{
                    new TargetData(TargetType.SINGLE, FieldTarget.FRIENDLY, Tower.class),
                    new TargetData(TargetType.SINGLE, FieldTarget.FRIENDLY, Tower.class)
            });
        }

        @Override
        public String getTooltip() {
            return "";
        }

        @Override
        public Spell createSpell(Player player) {
            return new Combiner(player, this);
        }
    }


    public Combiner(Player owner, SpellPrototype prototype) {
        super(owner, prototype);
        towers = new Tower[2];
    }

    private Tower[] towers;

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        towers[0] = ((Tower) targets.get(0).get(0));
        towers[1] = ((Tower) targets.get(1).get(0));

//        System.out.println(towers[0].getClass());
//        System.out.println(towers[1].getClass());

        Tower.TowerPrototype newTower = null;

        if(towers[0].getClass() == Shotgun.class && towers[1].getClass() == Shotgun.class){
            newTower = new Musket.P();
        }
        if(towers[0].getClass() == Assault.class && towers[1].getClass() == Assault.class){
            newTower = new MachineGun.P();
        }
        if(towers[0].getClass() == Rocket.class && towers[1].getClass() == Rocket.class){
            newTower = new Cannon.P();
        }
        if(towers[0].getClass() == Rocket.class && towers[1].getClass() == Assault.class ||
                towers[1].getClass() == Rocket.class && towers[0].getClass() == Assault.class){
            newTower = new DoubleRocket.P();
        }
        if(towers[0].getClass() == Rocket.class && towers[1].getClass() == Shotgun.class ||
                towers[1].getClass() == Rocket.class && towers[0].getClass() == Shotgun.class){
            newTower = new Sniper.P();
        }
        if(towers[0].getClass() == Shotgun.class && towers[1].getClass() == Assault.class ||
                towers[1].getClass() == Shotgun.class && towers[0].getClass() == Assault.class){
            newTower = new DoubleBarrel.P();
        }



        if(newTower != null) {
            Slot<Tower.TowerPrototype, Tower> targetSlot = towers[1].getSlot();
            targetSlot.getObject().die(null, false, false);
            towers[0].die(null, false, false);
            targetSlot.build(newTower);
        }

//        System.out.println(towers[0]);
//        System.out.println(towers[1]);
    }





}
