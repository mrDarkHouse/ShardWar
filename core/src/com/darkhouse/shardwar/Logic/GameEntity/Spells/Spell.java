package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.DamageSource;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.Slot.Slot;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public abstract class Spell implements DamageSource {


//    public enum SpellType{
//        Target, NonTarget;
//    }
    private Player owner;


    @Override
    public Player getOwnerPlayer() {
        return owner;
    }




    public interface Type{

//        ArrayList<GameObject> getTargets(FightScreen.Field field, Slot pointer, ArrayList<Class<? extends GameObject>> affectedTypes){
//            ArrayList<GameObject> gameObjects = getTargets(field, pointer);
//
//        }

        ArrayList<Slot> getTargets(FightScreen.Field field, Slot pointer);
    }

    public enum TargetType implements Type{
        SINGLE{
            @Override
            public ArrayList<Slot> getTargets(FightScreen.Field field, Slot pointer) {
                ArrayList<Slot> list = new ArrayList<Slot>();
                list.add(pointer);
//                list.removeAll(Collections.singleton(null));
                return list;
            }
        }, HLINE {
            @Override
            public ArrayList<Slot> getTargets(FightScreen.Field field, Slot pointer) {
                int row = pointer.getRow();
                ArrayList<Slot> list = new ArrayList<Slot>(field.getOnRow(row));
//                list.removeAll(Collections.singleton(null));
                return list;
            }
        }, VLINE {
            @Override
            public ArrayList<Slot> getTargets(FightScreen.Field field, Slot pointer) {
                int column = pointer.getColumn();
                ArrayList<Slot> list = new ArrayList<Slot>(field.getOnColumn(column));
//                list.removeAll(Collections.singleton(null));
                return list;
            }
        }, CORNER {
            @Override
            public ArrayList<Slot> getTargets(FightScreen.Field field, Slot pointer) {
                int row = pointer.getRow();
                int column = pointer.getColumn();
                ArrayList<Slot> list = new ArrayList<Slot>();

                if((row != 0 && row != 2) || (column != 0 && column != 2)) {
                    return list;
                }
                int r = -1, c = -1;//
                if(row == 0) r = row + 1;
                if(row == 2) r = row - 1;
                if(column == 0) c = column + 1;
                if(column == 2) c = column - 1;

                list.add(pointer);
                list.add(field.get(r, column));
                list.add(field.get(row, c));

//                list.removeAll(Collections.singleton(null));
                return list;
            }
        }, PLUS {
            @Override
            public ArrayList<Slot> getTargets(FightScreen.Field field, Slot pointer) {
                ArrayList<Slot> list = new ArrayList<Slot>();
                int row = pointer.getRow();
                int column = pointer.getColumn();

                list.add(pointer);
                if(row != 0) list.add(field.get(row - 1, column));
                if(row != 2) list.add(field.get(row + 1, column));
                if(column != 0) list.add(field.get(row, column - 1));
                if(column != 2) list.add(field.get(row, column + 1));

                return list;
            }
        },
        ALL{
            @Override
            public ArrayList<Slot> getTargets(FightScreen.Field field, Slot pointer) {
                Slot s = pointer;//
                ArrayList<Slot> list = new ArrayList<Slot>();
                list.addAll(field.getOnRow(0));
                list.addAll(field.getOnRow(1));
                list.addAll(field.getOnRow(2));
                list.add(field.getPlayerSlot());
                return list;
            }
        }


    }
    public enum NonTargetType implements Type{
        ALL{
            @Override
            public ArrayList<Slot> getTargets(FightScreen.Field field) {
                ArrayList<Slot> list = new ArrayList<Slot>();
                list.addAll(field.getOnColumn(0));
                list.addAll(field.getOnColumn(1));
                list.addAll(field.getOnColumn(2));
                list.add(field.getPlayerSlot());
//                list.removeAll(Collections.singleton(null));
                return list;
            }
        },
        PLUS{
            @Override
            public ArrayList<Slot> getTargets(FightScreen.Field field) {
                ArrayList<Slot> list = new ArrayList<Slot>();
                list.add(field.get(0, 1));
                list.addAll(field.getOnRow(1));
                list.add(field.get(2, 1));
//                list.removeAll(Collections.singleton(null));
                return list;
            }
        },
        FRAME{
            @Override
            public ArrayList<Slot> getTargets(FightScreen.Field field) {
                ArrayList<Slot> list = new ArrayList<Slot>();
                list.addAll(field.getOnRow(0));
                list.add(field.get(1, 0));
                list.add(field.get(1, 2));
                list.addAll(field.getOnRow(2));
//                list.removeAll(Collections.singleton(null));
                return list;
            }
        },
        X{
            @Override
            public ArrayList<Slot> getTargets(FightScreen.Field field) {
                ArrayList<Slot> list = new ArrayList<Slot>();
                list.add(field.get(0, 0));
                list.add(field.get(0, 2));
                list.add(field.get(1, 1));
                list.add(field.get(2, 0));
                list.add(field.get(2, 2));
                return list;
            }
        },
        PLAYER{
            @Override
            public ArrayList<Slot> getTargets(FightScreen.Field field) {
                ArrayList<Slot> list = new ArrayList<Slot>();
                list.add(field.getPlayerSlot());
                return new ArrayList<>(list);
            }
        };

        public abstract ArrayList<Slot> getTargets(FightScreen.Field field);

        @Override
        public ArrayList<Slot> getTargets(FightScreen.Field field, Slot pointer) {
            return getTargets(field);
        }

    }

    public enum FieldTarget{
        FRIENDLY, ENEMY, ALL //All not completed
    }

    public static class TargetData{
        private Type spellType;
        private FieldTarget fieldTarget;
        private ArrayList<Class<? extends GameObject>> affectedTypes;

        public Type getSpellType() {
            return spellType;
        }
        public FieldTarget getFieldTarget() {
            return fieldTarget;
        }
        public ArrayList<Class<? extends GameObject>> getAffectedTypes() {
            return affectedTypes;
        }

        @SafeVarargs
        public TargetData(Type spellType, FieldTarget fieldTarget, Class<? extends GameObject>... affectedTypes) {
            this.spellType = spellType;
            this.fieldTarget = fieldTarget;
            this.affectedTypes = new ArrayList<>(Arrays.asList(affectedTypes));
        }
    }

    public static abstract class SpellPrototype{
        private String name;
//        private Type spellType;
//        private FieldTarget fieldTarget;
//        private ArrayList<Class<? extends GameObject>> affectedTypes;
        private TargetData[] targetData;
        private int tier;
        private boolean sameTargets;

        public int getTier() {
            return tier;
        }
        public void setTier(int tier) {
            this.tier = tier;
        }
        public String getName() {
            return name;
        }
        public TargetData[] getTargetData() {
            return targetData;
        }

        public boolean isSameTargets() {
            return sameTargets;
        }

        public void setSameTargets() {
            this.sameTargets = true;
        }
        public void lockTargeting(){

        }
        //        public Type getSpellType() {
//            return spellType;
//        }
//        public FieldTarget getFieldTarget() {
//            return fieldTarget;
//        }
//        public ArrayList<Class<? extends GameObject>> getAffectedTypes() {
//            return affectedTypes;
//        }

        public SpellPrototype(String name, TargetData[] targetData) {
            this.name = name;
            this.targetData = targetData;
//            this.spellType = spellType;
//            this.fieldTarget = fieldTarget;
//            this.affectedTypes = new ArrayList<Class<? extends GameObject>>(Arrays.asList(affectedTypes));
        }

        public abstract String getTooltip();

        public abstract Spell createSpell(Player player);
    }




//    protected Type spellType;
//    protected FieldTarget fieldTarget;
//    private ArrayList<Class<? extends GameObject>> affectedTypes;
    private TargetData[] targetData;

    public TargetData[] getTargetData() {
        return targetData;
    }

    //    public Type getSpellType() {
//        return spellType;
//    }
//    public FieldTarget getFieldTarget() {
//        return fieldTarget;
//    }
//    public ArrayList<Class<? extends GameObject>> getAffectedTypes() {
//        return affectedTypes;
//    }
    protected SpellPrototype prototype;

    public SpellPrototype getPrototype() {
        return prototype;
    }

    public Spell(Player owner, SpellPrototype prototype) {
        this.owner = owner;
        this.targetData = prototype.targetData;

//        this.spellType = prototype.spellType;
//        this.fieldTarget = prototype.fieldTarget;
//        this.affectedTypes = prototype.affectedTypes;

        this.prototype = prototype;
    }

    public abstract void use(ArrayList<ArrayList<GameObject>> targets);

//    public void use(FightScreen.Field field){
//        use(spellType.getTargets(field));
//    }

}
