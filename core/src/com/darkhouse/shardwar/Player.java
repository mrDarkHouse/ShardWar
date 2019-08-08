package com.darkhouse.shardwar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.darkhouse.shardwar.Logic.GameEntity.DamageSource;
import com.darkhouse.shardwar.Logic.GameEntity.Entity;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Spell;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Model.SpellPanel;
import com.darkhouse.shardwar.Screens.EndGameScreen;
import com.darkhouse.shardwar.Tools.AssetLoader;

import java.util.Arrays;

public class Player extends GameObject {
    private int shards;
    private float[] lineX;
//    private ProgressBar healthBar;
    private final int maxShards = 40;
    private boolean silenced;

    public void setSilenced(boolean silenced) {
        this.silenced = silenced;
        if(silenced){
            AlphaAction a = new AlphaAction();
            a.setAlpha(0.4f);
            spellPanel.addAction(a);
        }else {
            AlphaAction a = new AlphaAction();
            a.setAlpha(1f);
            spellPanel.addAction(a);
        }
    }
    public boolean isSilenced() {
        return silenced;
    }

    private SpellPanel spellPanel;

    public void setSpellPanel(SpellPanel spellPanel) {
        this.spellPanel = spellPanel;
    }

    public boolean addSpell(Spell.SpellPrototype spellPrototype){
        return spellPanel.addSpell(spellPrototype);
    }


    public int getShards() {
        return shards;
    }
    public boolean deleteShards(int cost){
        if(shards >= cost){
            shards -= cost;
            return true;
        }else return false;
    }
    public void addShards(int value){
        if(shards + value <= maxShards) shards += value;
        else shards = maxShards;
        updateBar();
    }

//    @Override
//    public Vector2 getShootPosition(int line) {
//        return new Vector2(lineX[line], getY());//TODO java.lang.ArrayIndexOutOfBoundsException: -1
//    }
//
//    @Override
//    public boolean isExist() {
//        return true;
//    }

    public static class PlayerPrototype extends ObjectPrototype{

        public PlayerPrototype(Texture texture, String name) {
            super(texture, texture, name, 0, 0, 0);
        }

        @Override
        public Player getObject() {
            return new Player(this);
        }
    }


    public Player(PlayerPrototype p) {
        super(p);
//        super(ShardWar.main.getAssetLoader().get("shard.png", Texture.class));


    }


    public void initShards(){
        addShards(10);
    }


    @Override
    public String getTooltip() {
        AssetLoader l = ShardWar.main.getAssetLoader();
        String s = "";

        s += getEffectTooltip();

        return s;
    }

    @Override
    public Vector2 getShootPosition(int line) {
//        System.out.println(line);
        try {
//            System.out.println(Arrays.toString(lineX) + " (" + line + ")");
            return new Vector2(lineX[line], slot.getY() + slot.getParent().getY() + slot.getHeight()/2);
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            System.out.println(line);
//            System.out.println(Arrays.toString(lineX));
            return new Vector2(lineX[0], slot.getY() + slot.getParent().getY() + slot.getHeight()/2);
        }


        //TODO java.lang.ArrayIndexOutOfBoundsException: -1
    }

    public void init(){
        super.init();
//        healthBar = new ProgressBar(0, maxShards, 0.1f, true, ShardWar.main.getAssetLoader().getSkin(), "health-bar");
//        healthBar.setSize(Gdx.graphics.getWidth(), 40);
//        healthBar.getStyle().background.setMinWidth(Gdx.graphics.getWidth());
//        healthBar.getStyle().knobBefore.setMinWidth(Gdx.graphics.getWidth() - 2);
//        healthBar.setPosition(0, 0);
//        System.out.println(healthBar.getX() + " " + getX() + " "  +
//                healthBar.getY() + " " + getY());
//        updateBar();
//        healthBar.pack();
    }

    public void setShootCoord(float[] lineX){
        this.lineX = lineX;
    }

    @Override
    public int dmg(int dmg, DamageSource source, boolean ignoreDefSpells) {
        if(dmg >= getShards()){
            shards = 0;
            loose();
        }else deleteShards(dmg);
        updateBar();
        return dmg;
    }

    @Override
    public void physic(float delta) {

    }

    private void updateBar(){
//        healthBar.setValue(shards);
    }


    private void loose(){
        ShardWar.main.setScreen(new EndGameScreen(
                ShardWar.fightScreen.getEnemyPlayer(ShardWar.fightScreen.getPlayerIndex(this))));
    }

//    @Override
//    public void draw(Batch batch, float parentAlpha) {
////        super.draw(batch, parentAlpha);
////        healthBar.draw(batch, parentAlpha);
//    }
}
