package com.darkhouse.shardwar.Logic.GameEntity.Spells.Model;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
        String st = FontLoader.colorString(a.getWord("targetType") + ": ", 4);
        Label spellType = new Label(st + a.getWord(spell.getSpellType().toString().toLowerCase()),
                ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
        spellType.getStyle().font.getData().markupEnabled = true;
        String s = "";
        for (int i = 0; i < spell.getAffectedTypes().size(); i++) {
            s += a.getWord(spell.getAffectedTypes().get(i).getSimpleName().toLowerCase());
            if(i != spell.getAffectedTypes().size() - 1) s += ", ";
        }
        String at = FontLoader.colorString(a.getWord("affectedUnits") +":", 6);
        Label affectedTypes = new Label(at + s,
                ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
        affectedTypes.getStyle().font.getData().markupEnabled = true;
        Label spellTooltip = new Label(spell.getTooltip(),
                ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
        add(spellType).row();
        add(affectedTypes).padBottom(5).row();
//                add(new Actor()).row();
        add(spellTooltip).row();
        pack();

    }

    @Override
    public void show() {
        super.show();
        if(spell.getSpellType() instanceof Spell.NonTargetType){
            if(owner == ShardWar.fightScreen.getCurrentPlayerObject()) showTargets();
        }
    }

    @Override
    public void hide() {
        super.hide();
        if(spell.getSpellType() instanceof Spell.NonTargetType){
            if(owner == ShardWar.fightScreen.getCurrentPlayerObject()) clearTargets();
        }
    }
    protected void showTargets(){
        ArrayList<Slot> targets = new ArrayList<Slot>();
        Spell.NonTargetType t = (Spell.NonTargetType) spell.getSpellType();
        if(spell.getFieldTarget() == Spell.FieldTarget.ENEMY) {
            targets.addAll(t.getTargets(ShardWar.fightScreen.getOppositeField()));
        }
        if(spell.getFieldTarget() == Spell.FieldTarget.FRIENDLY) {
            targets.addAll(t.getTargets(ShardWar.fightScreen.getCurrentField()));
        }
        for (Slot s:targets){
            SpellPanel.chooseCorrectTargets(s, spell.getAffectedTypes());
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
