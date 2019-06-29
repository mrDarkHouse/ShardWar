package com.darkhouse.shardwar.Logic.GameEntity.Spells.Model;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.darkhouse.shardwar.Logic.GameEntity.Empty;
import com.darkhouse.shardwar.Logic.GameEntity.Entity;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Spell;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Logic.Slot.PlayerSlot;
import com.darkhouse.shardwar.Logic.Slot.Slot;
import com.darkhouse.shardwar.Logic.Slot.TowerSlot;
import com.darkhouse.shardwar.Logic.Slot.WallSlot;
import com.darkhouse.shardwar.Model.ShapeTarget;
import com.darkhouse.shardwar.Model.Tooltip.TooltipListener;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;

import java.util.ArrayList;

public class SpellPanel extends Table {

    private final static int drawOffset = 2;

    private class SpellSlot extends Actor {

        public class SoloTargeter extends InputListener {
//            private Array<Class<? extends GameObject>> targetTypes;
            private ShapeTarget circle;
            private int currentTargeting;
            private ArrayList<ArrayList<GameObject>> allTargets;

            public SoloTargeter(/*Array<Class<? extends GameObject>> targetTypes, */
                    ArrayList<ArrayList<GameObject>> allTargets, Vector2 startCoord, int currentTargeting) {
//            this.targetTypes = targetTypes;
                circle = new ShapeTarget(new ShapeRenderer(), 20);
                circle.setPosition(startCoord.x, startCoord.y);
                SpellPanel.this.getStage().addActor(circle);
//            SpellThrower.this.stage.addActor(circle);
                isTargeting = true;
                this.currentTargeting = currentTargeting;
                this.allTargets = allTargets;
                ShardWar.fightScreen.getField(player).setTouchable(Touchable.disabled);
            }

            public void cancelTargeting(){
                clearSelect();
                SpellPanel.this.getStage().removeListener(this);
                isTargeting = false;
                circle.remove();
                unChooseTargetsForDoubleUse();
                ShardWar.fightScreen.getField(player).setTouchable(Touchable.enabled);
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                circle.setPosition(event.getStageX() - 10, event.getStageY() - 10);
                Slot target = getTarget(new Vector2(x, y));
                if(target != null && (spell.getPrototype().isSameTargets() || !target.isReserved())) {
                    ArrayList<Slot> all = new ArrayList<Slot>();
                    ArrayList<Slot> targets = new ArrayList<Slot>();
                    if(spell.getTargetData()[currentTargeting].getFieldTarget() == Spell.FieldTarget.ALL) {
                        if(ShardWar.fightScreen.getEnemyField(player).getObjects().contains(target)){
                            all.addAll(ShardWar.fightScreen.getEnemyField(player).getObjects());
                            targets.addAll(spell.getTargetData()[currentTargeting].getSpellType().
                                    getTargets(ShardWar.fightScreen.getEnemyField(player), target));
                        }else if(ShardWar.fightScreen.getField(player).getObjects().contains(target)){
                            all.addAll(ShardWar.fightScreen.getField(player).getObjects());
                            targets.addAll(spell.getTargetData()[currentTargeting].getSpellType().
                                    getTargets(ShardWar.fightScreen.getField(player), target));
                        }
                    }
                    if(spell.getTargetData()[currentTargeting].getFieldTarget() == Spell.FieldTarget.ENEMY) {
                        all.addAll(ShardWar.fightScreen.getEnemyField(player).getObjects());
                        targets.addAll(spell.getTargetData()[currentTargeting].getSpellType().
                                getTargets(ShardWar.fightScreen.getEnemyField(player), target));
                    }
                    if(spell.getTargetData()[currentTargeting].getFieldTarget() == Spell.FieldTarget.FRIENDLY) {
                        all.addAll(ShardWar.fightScreen.getField(player).getObjects());
                        targets.addAll(spell.getTargetData()[currentTargeting].getSpellType().
                                getTargets(ShardWar.fightScreen.getField(player), target));
                    }
                    for (Slot s:all){
                        if(targets.contains(s)){
                            chooseCorrectTargets(s, spell.getTargetData()[currentTargeting].getAffectedTypes());
                        }else s.unChoose();
                    }
//                    for (Slot s:targets) {
//                        s.choose();
//                    }
                }else clearSelect();
                return true;
            }
            private void clearSelect(){
                FightScreen.Field f1 = ShardWar.fightScreen.getEnemyField(player);
                for (Slot s:f1.getObjects()){
                    s.unChoose();
                }
                FightScreen.Field f2 = ShardWar.fightScreen.getField(player);
                for (Slot s:f2.getObjects()){
                    s.unChoose();
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(button == 0) use(new Vector2(x, y));
                else cancelTargeting();
                return true;
            }
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                if(button == 0) use(new Vector2(x, y));
//                else cancelTargeting();
//            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.ESCAPE){
                    cancelTargeting();
                    return true;
                }else return false;
            }

            private Slot getTarget(Vector2 point){
//            Effectable target = Level.getMap().getTargetUnit(point, targetTypes);
                Slot target = null;
                if(spell.getTargetData()[currentTargeting].getFieldTarget() == Spell.FieldTarget.ALL) {
                    target = ShardWar.fightScreen.getEnemyField(player).getByCoords(point.x, point.y);
                    if(target == null)target = ShardWar.fightScreen.getField(player).getByCoords(point.x, point.y);
                }
                if(spell.getTargetData()[currentTargeting].getFieldTarget() == Spell.FieldTarget.ENEMY) {
                    target = ShardWar.fightScreen.getEnemyField(player).getByCoords(point.x, point.y);
                }
                if(spell.getTargetData()[currentTargeting].getFieldTarget() == Spell.FieldTarget.FRIENDLY) {
                    target = ShardWar.fightScreen.getField(player).getByCoords(point.x, point.y);
                }
//                if(spell.getFieldTarget() == Spell.FieldTarget.ALL) {
//                    target = ShardWar.fightScreen.getEnemyField(player).getByCoords(point.x, point.y);
//                }
                if(target != null) return target;
                else return null;
            }

            private void use(Vector2 point){
                Slot target = getTarget(point);
                if(target != null) {
                    ArrayList<Slot> targets = new ArrayList<Slot>();
                    if(spell.getTargetData()[currentTargeting].getFieldTarget() == Spell.FieldTarget.ALL) {
                        if(ShardWar.fightScreen.getEnemyField(player).getObjects().contains(target)){
                            targets.addAll(spell.getTargetData()[currentTargeting].getSpellType().getTargets(
                                    ShardWar.fightScreen.getEnemyField(player), target));
                        }else if(ShardWar.fightScreen.getField(player).getObjects().contains(target)){
                            targets.addAll(spell.getTargetData()[currentTargeting].getSpellType().getTargets(
                                    ShardWar.fightScreen.getField(player), target));
                        }
                    }
                    if(spell.getTargetData()[currentTargeting].getFieldTarget() == Spell.FieldTarget.ENEMY) {
                         targets = spell.getTargetData()[currentTargeting].getSpellType().getTargets(
                                ShardWar.fightScreen.getEnemyField(player), target);
                    }
                    if(spell.getTargetData()[currentTargeting].getFieldTarget() == Spell.FieldTarget.FRIENDLY) {
                        targets = spell.getTargetData()[currentTargeting].getSpellType().getTargets(
                                ShardWar.fightScreen.getField(player), target);
                    }



                    ArrayList<GameObject> trueTargets = sortTargets(targets, spell, currentTargeting);


                    if(!trueTargets.isEmpty()) {//optional
//                        spell.use(trueTargets);
//                        removeSpell();
                        cancelTargeting();
                        allTargets.add(trueTargets);
                        chooseTargetsForDoubleUse(trueTargets);
                        nextTargetData(allTargets, point, currentTargeting + 1);
                    }
                }
            }
        }

        private Spell spell;
        private Texture bgTexture;
        private Texture texture;
        private SpellTooltip tooltip;
        private TooltipListener l;
        private int index;

        public void setSpell(Spell.SpellPrototype prototype) {
            this.spell = prototype.createSpell(player);
            texture = ShardWar.main.getAssetLoader().getSpell(prototype.getName());
            tooltip = new SpellTooltip(prototype, player);
            getStage().addActor(tooltip);
            tooltip.init();
            l = new TooltipListener(tooltip, true);
            addListener(l);
        }

        public void removeSpell(){
            this.spell = null;
            tooltip.hide();
//            tooltip = null;
            removeListener(l);
        }

        public SpellSlot(int index) {
            this.index = index;
            bgTexture = ShardWar.main.getAssetLoader().get("towerSlot.png", Texture.class);
        }
        public void init(){
            addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if(ShardWar.fightScreen.getCurrentPlayerObject() == player && spell != null) {
                        useSpell(spell, new Vector2(x, y));
                    }
                    return true;
                }

//                @Override
//                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                    super.touchUp(event, x, y, pointer, button);
//                    if(ShardWar.fightScreen.getCurrentPlayerObject() == player && spell != null) {
//                        useSpell(spell);
////                        removeSpell();
//                    }
//                }
            });
//            buyWindow = new SpellTooltip();
//            getStage().addActor(buyWindow);
//            addListener(new TooltipListener(buyWindow, true));
        }


        private ArrayList<GameObject> sortTargets(ArrayList<Slot> targets, Spell spell, int currentTargeting){
            ArrayList<GameObject> trueTargets = new ArrayList<GameObject>();
            for (Slot g:targets){
                if(g.getSomeObject() != null && (spell.getPrototype().isSameTargets() || !g.isReserved()) &&
                        (!g.getSomeObject().isImmune() ||
                        spell.getTargetData()[currentTargeting].getFieldTarget() == Spell.FieldTarget.FRIENDLY)){
                    if(spell.getTargetData()[currentTargeting].getAffectedTypes().contains(
                            g.getSomeObject().getClass().getSuperclass()) ||//TODO rework
                    spell.getTargetData()[currentTargeting].getAffectedTypes().contains(
                            g.getSomeObject().getClass())){
                        trueTargets.add(g.getSomeObject());
                    }
                }
            }
            return trueTargets;
        }


        private void useSpell(Spell spell, Vector2 useCoord){
            if(player.isSilenced()) return;
            ArrayList<ArrayList<GameObject>> allTargets = new ArrayList<>();
            useCoord.x = useCoord.x + getParent().getX() + index*getWidth() + 5/*- 5*/;
            useCoord.y = useCoord.y + getParent().getY() + 5/*- 5*/;

            nextTargetData(allTargets, useCoord, 0);
//            for (int i = 0; i < spell.getTargetData().length; i++) {
//
//            }

        }

        public void nextTargetData(ArrayList<ArrayList<GameObject>> allTargets, Vector2 useCoord, int i){
            if(spell.getTargetData().length <= i){
                spell.use(allTargets);
                unChooseTargetsForDoubleUse();
                removeSpell();
                return;
            }
            if(spell.getTargetData()[i].getSpellType() instanceof Spell.NonTargetType) {
                FightScreen f = ShardWar.fightScreen;
                ArrayList<Slot> targetSlots = new ArrayList<Slot>();
                if(spell.getTargetData()[i].getFieldTarget() == Spell.FieldTarget.ALL){
                    targetSlots.addAll(((Spell.NonTargetType) spell.getTargetData()[i].getSpellType()).
                            getTargets(f.getEnemyField(player)));
                    targetSlots.addAll(((Spell.NonTargetType) spell.getTargetData()[i].getSpellType()).
                            getTargets(f.getField(player)));
                }
                if(spell.getTargetData()[i].getFieldTarget() == Spell.FieldTarget.ENEMY){
                    targetSlots.addAll(((Spell.NonTargetType) spell.getTargetData()[i].getSpellType()).
                            getTargets(f.getEnemyField(player)));
                }
                if(spell.getTargetData()[i].getFieldTarget() == Spell.FieldTarget.FRIENDLY){
                    targetSlots.addAll(((Spell.NonTargetType) spell.getTargetData()[i].getSpellType()).
                            getTargets(f.getField(player)));
                }
                ArrayList<GameObject> targets = sortTargets(targetSlots, spell, i);
                allTargets.add(targets);
                chooseTargetsForDoubleUse(targets);
                nextTargetData(allTargets, useCoord, i + 1);
            }else if(spell.getTargetData()[i].getSpellType() instanceof Spell.TargetType) {
                SoloTargeter st = new SoloTargeter(allTargets, new Vector2(
                        useCoord.x - 10/* + getParent().getX() + index*getWidth() - 5*/,
                        useCoord.y - 10 /*+ getParent().getY() - 5*/), i);
                //5 - size between top of table and actor (getHeight() - 10, getHeight() - 10)
                getStage().addListener(st);
                listeners.add(st);
            }
        }

        private void chooseTargetsForDoubleUse(ArrayList<GameObject> targets){
            for (GameObject g:targets){
                g.getSlot().reserve();
            }
        }
        private void unChooseTargetsForDoubleUse(){
            FightScreen.Field f1 = ShardWar.fightScreen.getEnemyField(player);
            for (Slot s:f1.getObjects()){
                s.unReserve();
            }
            FightScreen.Field f2 = ShardWar.fightScreen.getField(player);
            for (Slot s:f2.getObjects()){
                s.unReserve();
            }
        }



        public boolean isEmpty(){
            return spell == null;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(bgTexture, getX(), getY(), getWidth(), getHeight());
            if(spell != null) {
                batch.draw(texture, getX() + drawOffset, getY() + drawOffset,
                        getWidth() - drawOffset*2, getHeight() - drawOffset*2);
            }
        }
    }




    private boolean isTargeting;

    private Player player;
    private SpellSlot[] slots;
//    private ArrayList<SpellSlot> spells;

    public SpellPanel(Player player) {
        super();
        this.player = player;
    }
    public void init(){
        setBackground(ShardWar.main.getAssetLoader().getSkin().getDrawable("info-panel"));
        slots = new SpellSlot[4];
        listeners = new ArrayList<>();
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new SpellSlot(i);
            slots[i].setSize(getHeight() - 10, getHeight() - 10);
            add(slots[i]);
            slots[i].init();
        }


        player.setSpellPanel(this);
//        pack();
    }

    public static void chooseCorrectTargets(Slot s, ArrayList<Class<? extends GameObject>> affectedTypes) {
        if(s instanceof TowerSlot && affectedTypes.contains(Tower.class)) {
            s.choose();
        }else if(s instanceof WallSlot && affectedTypes.contains(Wall.class)){
            s.choose();
        }else if(s instanceof PlayerSlot && affectedTypes.contains(Player.class)){
            s.choose();
        }else if(s != null && affectedTypes.contains(Empty.class)){
            s.choose();
        }
    }

    private ArrayList<SpellSlot.SoloTargeter> listeners;
    public void clearListeners(){
        for (SpellSlot.SoloTargeter s:listeners){
            s.cancelTargeting();
        }
        listeners.clear();
    }

    public boolean addSpell(Spell.SpellPrototype spellPrototype){
//        SpellSlot s = new SpellSlot();
//        add(s);
        for (int i = 0; i < slots.length; i++) {
            if(slots[i].isEmpty()) {
                slots[i].setSpell(spellPrototype);
                return true;
            }
        }
        return false;
//        spells.add(s);
    }

//    public void deleteSpell(SpellSlot spell){
//        spell.remove();
////        spells.remove(spell);
//    }
}
