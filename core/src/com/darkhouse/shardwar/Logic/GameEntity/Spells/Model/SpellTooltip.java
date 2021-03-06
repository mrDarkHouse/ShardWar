package com.darkhouse.shardwar.Logic.GameEntity.Spells.Model;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Spell;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Logic.Slot.PlayerSlot;
import com.darkhouse.shardwar.Logic.Slot.Slot;
import com.darkhouse.shardwar.Logic.Slot.TowerSlot;
import com.darkhouse.shardwar.Logic.Slot.WallSlot;
import com.darkhouse.shardwar.Model.Tooltip.AbstractTooltip;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class SpellTooltip extends AbstractTooltip {

    private Spell.SpellPrototype spell;
    private Player owner;

    public SpellTooltip(Spell.SpellPrototype spell, Player owner) {
        super("", ShardWar.main.getAssetLoader().getSkin());
        this.spell = spell;
        this.owner = owner;
    }
    public void init(){
        AssetLoader a = ShardWar.main.getAssetLoader();
        getTitleLabel().setText(a.getWord(spell.getName()));
        defaults().align(Align.left);
        add(getTooltipTable(spell)).row();
        Label spellTooltip = new Label(spell.getTooltip(),
                a.getSkin(), "spell-tooltip");

//                add(new Actor()).row();
        add(spellTooltip).row();
        pack();

    }
    public static Table getTooltipTable(Spell.SpellPrototype spell){
        AssetLoader a = ShardWar.main.getAssetLoader();
        Table t = new Table();
        t.defaults().left();
        for (int i = 0; i < spell.getTargetData().length; i++) {
            if (spell.getTargetData().length > 1){
                String td = a.getWord("targeting");
                Label targetData = new Label(td + " " +(i + 1) + ":",
                        ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
                t.add(targetData).row();
            }
            String ft = FontLoader.colorString(a.getWord("fieldTarget") + ": ", 1);
            Label fieldTarget = new Label(ft + a.getWord(spell.getTargetData()[i].getFieldTarget().toString().toLowerCase()),
                    ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
            String st = FontLoader.colorString(a.getWord("targetType") + ": ", 0);
            Label spellType = new Label(st + a.getWord(spell.getTargetData()[i].getSpellType().toString().toLowerCase()),
                    ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
            spellType.getStyle().font.getData().markupEnabled = true;
            String s = "";
            for (int j = 0; j < spell.getTargetData()[i].getAffectedTypes().size(); j++) {
                s += a.getWord(spell.getTargetData()[i].getAffectedTypes().get(j).getSimpleName().toLowerCase());
                if(j != spell.getTargetData()[i].getAffectedTypes().size() - 1) s += ", ";
            }
            String at = FontLoader.colorString(a.getWord("affectedUnits") +":", 2);
            Label affectedTypes = new Label(at + s,
                    ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
            affectedTypes.getStyle().font.getData().markupEnabled = true;

            t.add(spellType).row();
            t.add(fieldTarget).row();
            t.add(affectedTypes).padBottom(5).row();
        }
        return t;
    }


    @Override
    public void show() {
        super.show();
        if(spell.getTargetData()[0].getSpellType() instanceof Spell.NonTargetType){
            if(owner == ShardWar.fightScreen.getCurrentPlayerObject()) showTargets();
        }
    }

    @Override
    public void hide() {
        super.hide();
        if(spell.getTargetData()[0].getSpellType() instanceof Spell.NonTargetType){
            if(owner == ShardWar.fightScreen.getCurrentPlayerObject()) clearTargets();
        }
    }
    protected void showTargets(){
        ArrayList<Slot> targets = new ArrayList<Slot>();
        Spell.NonTargetType t = (Spell.NonTargetType) spell.getTargetData()[0].getSpellType();
        if(spell.getTargetData()[0].getFieldTarget() == Spell.FieldTarget.ENEMY) {
            targets.addAll(t.getTargets(ShardWar.fightScreen.getOppositeField()));
        }
        if(spell.getTargetData()[0].getFieldTarget() == Spell.FieldTarget.FRIENDLY) {
            targets.addAll(t.getTargets(ShardWar.fightScreen.getCurrentField()));
        }
        for (Slot s:targets){
            SpellPanel.chooseCorrectTargets(s, spell.getTargetData()[0].getAffectedTypes());
        }
    }

    protected void clearTargets(){
        FightScreen.Field f1 = ShardWar.fightScreen.getField(ShardWar.fightScreen.getOppositePlayer());
        for (Slot s:f1.getObjects()){
            s.unChoose();
        }
        FightScreen.Field f2 = ShardWar.fightScreen.getField(ShardWar.fightScreen.getCurrentPlayer());
        for (Slot s:f2.getObjects()){
            s.unChoose();
        }
    }

    @Override
    public void hasChanged() {

    }
}
