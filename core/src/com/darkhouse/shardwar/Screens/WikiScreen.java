package com.darkhouse.shardwar.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.*;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Model.SpellTooltip;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.*;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.BrickWall;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.EnergyWall;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.SpikeWall;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Model.AutoAnimationActor;
import com.darkhouse.shardwar.Model.BackButton;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;

import java.util.ArrayList;

public class WikiScreen extends AbstractScreen {

    private int currentWindow;
    private static final float ICON_SIZE = 96;
    private static final float SPELL_ICON_SIZE = 60;

    public WikiScreen() {
        super(ShardWar.main.getAssetLoader().getMainMenuBg());
        init();
    }

    private WWindow[] pages;

    private class WWindow extends Window{
        public ScrollPane scrollPane;
        public WWindow(String title, Skin skin) {
            super(title, skin);
        }

    }

    private void init(){
        stage.addActor(new BackButton(true));

        pages = new WWindow[4];
        pages[0] = new WWindow("Basics", ShardWar.main.getAssetLoader().getSkin());
        pages[1] = new WWindow("Walls", ShardWar.main.getAssetLoader().getSkin());
        pages[2] = new WWindow("Towers", ShardWar.main.getAssetLoader().getSkin());
        pages[3] = new WWindow("Spells", ShardWar.main.getAssetLoader().getSkin());

        for (int i = 0; i < pages.length; i++) {
            pages[i].setMovable(false);
            pages[i].getTitleLabel().setAlignment(Align.center);
        }

        IconContainer[] changeButtons = new IconContainer[pages.length];
        changeButtons[0] = new IconContainer(ShardWar.main.getAssetLoader().get("towerSlot.png", Texture.class),
                ShardWar.main.getAssetLoader().get("Wiki/basics.png", Texture.class));
        changeButtons[1] = new IconContainer(ShardWar.main.getAssetLoader().get("towerSlot.png", Texture.class),
                ShardWar.main.getAssetLoader().get("walls/brickWallOriginal.png", Texture.class));
        changeButtons[2] = new IconContainer(ShardWar.main.getAssetLoader().get("towerSlot.png", Texture.class),
                ShardWar.main.getAssetLoader().get("towers/assault.png", Texture.class));
        changeButtons[3] = new IconContainer(ShardWar.main.getAssetLoader().get("towerSlot.png", Texture.class),
                ShardWar.main.getAssetLoader().get("Ability/Spells/darkritual.png", Texture.class));
//        changeButtons[0] = new Image(ShardWar.main.getAssetLoader().get("walls/brickWallOriginal.png", Texture.class));
//        changeButtons[1] = new Image(ShardWar.main.getAssetLoader().get("towers/assault.png", Texture.class));
//        changeButtons[2] = new Image(ShardWar.main.getAssetLoader().get("Ability/Spells/darkritual.png", Texture.class));

        for (int i = 0; i < changeButtons.length; i++) {
            int finalI = i;
            changeButtons[i].addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    switchWindow(finalI);
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        }

        initBasics(0);
        initWalls(1);
        initTowers(2);
        initSpells(3);


        Stack stack = new Stack(pages);
        VerticalGroup group = new VerticalGroup();
        float ratio = Gdx.graphics.getHeight()/768f;
        for (int i = 0; i < changeButtons.length; i++) {
            changeButtons[i].setSize(120*ratio, 120*ratio);
            group.addActor(changeButtons[i]);
        }
        group.expand();
//        group.pack();

        Table mainTable = new Table();
        mainTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        mainTable.add(group);
        mainTable.add(stack);

//        pages[0].debug();
//        mainTable.pack();
        switchWindow(0);
        stage.addActor(mainTable);
    }
    private String getWallInfo(Wall.WallPrototype w){
        String s = "";
        s += "Health: " + w.getMaxHealth();
        return s;
    }
    private String getTowerInfo(Tower.TowerPrototype t){
        StringBuilder s = new StringBuilder();
        s.append("Health: ").append(t.getMaxHealth());
        s.append(System.getProperty("line.separator"));
        s.append("Damage: ").append(t.getDmg());
//        if(t.getAbilities().size() != 0) s.append(System.getProperty("line.separator"));
        for (int i = 0; i < t.getAbilities().size(); i++) {
            s.append(System.getProperty("line.separator"));
            s.append(t.getAbilities().get(i).getName()).append(": ");
//            s.append(System.getProperty("line.separator"));
            s.append(t.getAbilities().get(i).getTooltip());
        }
        return s.toString();
    }

    private Label generateMultiLineLabel(int index, int lines){
        AssetLoader l = ShardWar.main.getAssetLoader();
        String s = l.getWord("basicsInfo" + index);
        for (int i = 2; i <= lines; i++) {
            s += System.getProperty("line.separator") +
                    l.getWord("basicsInfo" + index + "_" + i);
        }
        return new Label(s, l.getSkin(), "spell-tooltip");
    }

    private AutoAnimationActor[] aa;

    private void initBasics(int index){
        pages[index].defaults().pad(10f);
        AssetLoader l = ShardWar.main.getAssetLoader();
        Image shard = new Image(l.get("shard.png", Texture.class));
        Image img1 = new Image(l.get("Wiki/info1.png", Texture.class));
        Image img2 = new Image(l.get("Wiki/info2.png", Texture.class));
        Image img3 = new Image(l.get("Wiki/info3.png", Texture.class));
        Image img4 = new Image(l.get("Wiki/info4.png", Texture.class));
        Label[] info = new Label[26];
        aa = new AutoAnimationActor[10];//[0]
        aa[1] = new AutoAnimationActor("Wiki/BuildGuide.gif", 0.05f);
        aa[2] = new AutoAnimationActor("Wiki/SelectLineGuide.gif", 0.05f);
        aa[3] = new AutoAnimationActor("Wiki/ProjectileFlyGuide.gif", 0.05f);
        aa[4] = new AutoAnimationActor("Wiki/GlobalShotGuide.gif", 0.05f);
        aa[5] = new AutoAnimationActor("Wiki/MultiShotGuide.gif", 0.05f);
        aa[6] = new AutoAnimationActor("Wiki/ShotOrderGuide.gif", 0.05f);
        aa[7] = new AutoAnimationActor("Wiki/EndTurnGuide.gif", 0.05f);
        aa[8] = new AutoAnimationActor("Wiki/BuyTurnGuide.gif", 0.05f);
//        aa[9] = new AutoAnimationActor("Wiki/SpellBuyPanelGuide.gif", 0.05f);

        Table spellChances = new Table();
        spellChances.defaults().space(5f);
        spellChances.add(new Label("Round", l.getSkin(), "spell-tooltip"));
        for (int i = 0; i < FightScreen.CHANCES[0].length; i++) {
            spellChances.add(new Label("Tier " + (i + 1), l.getSkin(), "spell-tooltip"));
        }
        spellChances.row();
        for (int i = 0; i < FightScreen.CHANCES.length; i++) {
            String f = String.valueOf((i+1)*3);
            if(i == FightScreen.CHANCES.length - 1) f += "+";
            spellChances.add(new Label(f, l.getSkin(), "spell-tooltip"));
            for (int j = 0; j < FightScreen.CHANCES[i].length; j++) {
                spellChances.add(new Label((int)(FightScreen.CHANCES[i][j]*100) + "%", l.getSkin(), "spell-tooltip"));
            }
            spellChances.row();
        }


        for (int i = 0; i < info.length; i++) {
            info[i] = new Label(l.getWord("basicsInfo" + i), l.getSkin(), "spell-tooltip");
        }

        int headers[] = new int[]{0, 4, 14, 18};
        for (int i = 0; i < headers.length; i++) {
            info[headers[i]] = new Label(l.getWord("basicsInfo" + headers[i]) , l.getSkin(), "description-main");
        }

//        info[0] = new Label(l.getWord("basicsInfo0"), l.getSkin(), "description-main");
//        info[18] = new Label(l.getWord("basicsInfo18"), l.getSkin(), "description-main");
//        info[27] = new Label(l.getWord("basicsInfo27"), l.getSkin(), "description-main");

        String l8 = Gdx.app.getType() == Application.ApplicationType.Android ?
                l.getWord("basicsInfo8_2Android") : l.getWord("basicsInfo8_2");
        info[8] = new Label(l.getWord("basicsInfo8") + " " + System.getProperty("line.separator") +
                l8 + System.getProperty("line.separator") +
                l.getWord("basicsInfo8_3") + " " + System.getProperty("line.separator") +
                l.getWord("basicsInfo8_4"),
                l.getSkin(), "spell-tooltip");
        info[9] = new Label(l.getWord("basicsInfo9") + System.getProperty("line.separator")
                + l.getWord("basicsInfo9_2"),
                l.getSkin(), "spell-tooltip");
        info[10] = new Label(l.getWord("basicsInfo10") + System.getProperty("line.separator")
                + l.getWord("basicsInfo10_2") + System.getProperty("line.separator")
                + l.getWord("basicsInfo10_3"),
                l.getSkin(), "spell-tooltip");

        String l13 = Gdx.app.getType() == Application.ApplicationType.Android ?
                l.getWord("basicsInfo13Android") : l.getWord("basicsInfo13");
        info[13] =  new Label(l13, l.getSkin(), "spell-tooltip");
//        info[15] = new Label(l.getWord("basicsInfo15") + System.getProperty("line.separator")
//                + l.getWord("basicsInfo15_2"),
//                l.getSkin(), "spell-tooltip");
        info[15] = generateMultiLineLabel(15, 2);
        info[16] = new Label(l.getWord("basicsInfo16") + " " + (int)FightScreen.TURN_TIME + " "
                + l.getWord("basicsInfo16_2") + System.getProperty("line.separator")
                + l.getWord("basicsInfo16_3"),
                l.getSkin(), "spell-tooltip");
        info[17] = new Label(l.getWord("basicsInfo17") + System.getProperty("line.separator")
                + l.getWord("basicsInfo17_2") + " " + FightScreen.MINIMUM_VALUE + " "
                + l.getWord("basicsInfo17_3") + System.getProperty("line.separator")
                + l.getWord("basicsInfo17_4") + System.getProperty("line.separator")
                + l.getWord("basicsInfo17_5"),
                l.getSkin(), "spell-tooltip");

        info[19] = new Label(l.getWord("basicsInfo19") + " " + FightScreen.ROLL_ROUND + " "
                + l.getWord("basicsInfo19_2") + System.getProperty("line.separator")
                + l.getWord("basicsInfo19_3") + System.getProperty("line.separator")
                + l.getWord("basicsInfo19_4"),
                l.getSkin(), "spell-tooltip");
        info[21] = generateMultiLineLabel(21, 2);

//        info[21] = new Label(l.getWord("basicsInfo21") + " " +  (int)FightScreen.TURN_TIME
//                + " " + l.getWord("basicsInfo21_2"),
//                l.getSkin(), "spell-tooltip");
//        info[24] = new Label(l.getWord("basicsInfo24") + " " +  FightScreen.MINIMUM_VALUE
//                + " " + l.getWord("basicsInfo24_2"),
//                l.getSkin(), "spell-tooltip");


        Table allTable = new Table();
        Table main = new Table();
        Table battle = new Table();
        Table turns = new Table();
        Table spells = new Table();
//        Table combat
        main.align(Align.left);
        allTable.pad(10f);
//        main.defaults().space(15f);
        battle.defaults().space(15f);
        turns.defaults().space(15f);
        spells.defaults().space(15f);
        main.pad(10f);
        battle.pad(10f);
        turns.pad(10f);
        spells.pad(10f);

//        float ratio = Gdx.graphics.getWidth()/1300f;
        float ratio = Gdx.graphics.getHeight()/768f;
        if(ratio > 1) ratio = 1;

        allTable.add(info[0]).row();
        Table t1 = new Table();
        t1.add(info[1]).space(10f);
        t1.add(shard);
        main.add(t1).row();
        main.add(info[2]).row();
        main.add(info[3]).row();

        battle.add(info[4]).row();
        battle.add(info[5]).row();
        battle.add(img1).size(img1.getWidth()*ratio, img1.getHeight()*ratio).row();
        battle.add(info[6]).row();
        battle.add(aa[1]).size(aa[1].getWidth()*ratio, aa[1].getHeight()*ratio).row();
        battle.add(info[7]).row();
        battle.add(img2).size(img2.getWidth()*ratio, img2.getHeight()*ratio).row();
        battle.add(info[8]).row();
        battle.add(aa[2]).size(aa[2].getWidth()*ratio, aa[2].getHeight()*ratio).row();
        battle.add(info[9]).row();
        battle.add(aa[3]).size(aa[3].getWidth()*ratio, aa[3].getHeight()*ratio).row();
        battle.add(info[10]).row();
        battle.add(aa[4]).size(aa[4].getWidth()*ratio, aa[4].getHeight()*ratio).row();
        battle.add(info[11]).row();
        battle.add(aa[5]).size(aa[5].getWidth()*ratio, aa[5].getHeight()*ratio).row();
        battle.add(info[12]).row();
        battle.add(aa[6]).size(aa[6].getWidth()*ratio, aa[6].getHeight()*ratio).row();
        battle.add(info[13]).row();

        turns.add(info[14]).row();
        turns.add(info[15]).row();
        turns.add(img3).size(img3.getWidth()*ratio, img3.getHeight()*ratio).row();
        turns.add(info[16]).row();
        turns.add(aa[7]).size(aa[7].getWidth()*ratio, aa[7].getHeight()*ratio).row();
        turns.add(info[17]).row();
        turns.add(aa[8]).size(aa[8].getWidth()*ratio, aa[8].getHeight()*ratio).row();

        spells.add(info[18]).row();
        spells.add(info[19]).row();
//        spells.add(aa[9]).size(aa[9].getWidth()*ratio, aa[9].getHeight()*ratio).row();
        spells.add(info[20]).row();
        spells.add(img4).size(img4.getWidth()*ratio, img4.getHeight()*ratio).row();
        spells.add(info[21]).row();
        spells.add(spellChances).row();
        //table

        allTable.add(main).row();
        allTable.add(battle).row();
        allTable.add(turns).row();
        allTable.add(spells);


//        allTable.debug();


        pages[index].scrollPane = new ScrollPane(allTable, ShardWar.main.getAssetLoader().getSkin());
        pages[index].scrollPane.setScrollingDisabled(true, false);
        pages[index].scrollPane.setFadeScrollBars(false);
        pages[index].scrollPane.cancelTouchFocus();

        pages[index].add(pages[index].scrollPane);

        pages[index].pack();


    }
    private void initWalls(int index){
        pages[index].defaults().pad(10f);

        Table t1 = new Table();
        Table t2 = new Table();
        Table t3 = new Table();

        Table cost1 = new Table();
        Table cost2 = new Table();
        Table cost3 = new Table();

        BrickWall.P p1 = new BrickWall.P();
        Image w1 = new Image(p1.getOriginalTexture());
        Label name1 = new Label(p1.getName(), ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
        Label info1 = new Label(getWallInfo(p1), ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");

        t1.add(name1).row();
        t1.add(info1);

        Label money1 = new Label(" " + p1.getCost(), ShardWar.main.getAssetLoader().getSkin(), "description-main");
        Image shard1 = new Image(ShardWar.main.getAssetLoader().get("shard.png", Texture.class));
        cost1.add(money1);
        cost1.add(shard1);

        EnergyWall.P p2 = new EnergyWall.P(1);
        Image w2 = new Image(p2.getOriginalTexture());
        Label name2 = new Label(p2.getName(), ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
        Label info2 = new Label(getWallInfo(p2), ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
        Label ability2 = new Label("Block 1 damage", ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");

        t2.add(name2).row();
        t2.add(info2).row();
        t2.add(ability2);

        Label money2 = new Label(" " + p2.getCost(), ShardWar.main.getAssetLoader().getSkin(), "description-main");
        Image shard2 = new Image(ShardWar.main.getAssetLoader().get("shard.png", Texture.class));
        cost2.add(money2);
        cost2.add(shard2);

        SpikeWall.P p3 = new SpikeWall.P(2);
        Image w3 = new Image(p3.getOriginalTexture());
        Label name3 = new Label(p3.getName(), ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
        Label info3 = new Label(getWallInfo(p3), ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
        Label ability3 = new Label("Return 2 damage to attacker", ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");

        t3.add(name3).row();
        t3.add(info3).row();
        t3.add(ability3);

        Label money3 = new Label(" " + p3.getCost(), ShardWar.main.getAssetLoader().getSkin(), "description-main");
        Image shard3 = new Image(ShardWar.main.getAssetLoader().get("shard.png", Texture.class));
        cost3.add(money3);
        cost3.add(shard3);


        Table allTable = new Table();
        allTable.defaults().space(10f);
        allTable.pad(20f);
//        pages[0].debugAll();

        allTable.add(w1).size(ICON_SIZE, ICON_SIZE);
        allTable.add(cost1);
        allTable.add(t1).row();
        allTable.add(w2).size(ICON_SIZE, ICON_SIZE);
        allTable.add(cost2);
        allTable.add(t2).row();
        allTable.add(w3).size(ICON_SIZE, ICON_SIZE);
        allTable.add(cost3);
        allTable.add(t3);

        pages[index].scrollPane = new ScrollPane(allTable, ShardWar.main.getAssetLoader().getSkin());
        pages[index].scrollPane.setScrollingDisabled(true, false);
        pages[index].scrollPane.setFadeScrollBars(false);
//        scrollPane.setForceScroll(false, true);

        pages[index].add(pages[index].scrollPane);
    }
    private void initTowers(int index){
//        pages[1].debug();
        pages[index].defaults().pad(10f);
        pages[index].defaults().top();

        Table defaultTowers = new Table();
        defaultTowers.defaults().space(10f);

        addDefaultTower(new Shotgun.P(), defaultTowers);
        addDefaultTower(new Assault.P(), defaultTowers);
        addDefaultTower(new Rocket.P(), defaultTowers);

        Label combined = new Label("Next towers you can get with Combiner ability", ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
        Image combiner = new Image(ShardWar.main.getAssetLoader().get("Ability/Spells/combiner.png", Texture.class));

        Table combinerInfo = new Table();
        combinerInfo.add(combined).center().space(10f);
        combinerInfo.add(combiner).center();
        combinerInfo.pack();

        Table combinerTowers = new Table();
        combinerTowers.defaults().space(10f);
        AssetLoader l = ShardWar.main.getAssetLoader();
        addCombinedTower(new Musket.P(), combinerTowers, l.get("towers/shotgun.png", Texture.class),
                                                         l.get("towers/shotgun.png", Texture.class));
        addCombinedTower(new DoubleBarrel.P(), combinerTowers, l.get("towers/shotgun.png", Texture.class),
                                                               l.get("towers/assault.png", Texture.class));
        addCombinedTower(new Sniper.P(), combinerTowers, l.get("towers/shotgun.png", Texture.class),
                                                         l.get("towers/rocket.png", Texture.class));
        addCombinedTower(new MachineGun.P(), combinerTowers, l.get("towers/assault.png", Texture.class),
                                                             l.get("towers/assault.png", Texture.class));
        addCombinedTower(new DoubleRocket.P(), combinerTowers, l.get("towers/assault.png", Texture.class),
                                                               l.get("towers/rocket.png", Texture.class));
        addCombinedTower(new Cannon.P(), combinerTowers, l.get("towers/rocket.png", Texture.class),
                                                         l.get("towers/rocket.png", Texture.class));


        Table allTable = new Table();
//        allTable.debugAll();
        allTable.pad(20f);
        allTable.padRight(30f);
        allTable.add(defaultTowers).row();
        allTable.add(combinerInfo).row();
        allTable.add(combinerTowers);
//        defaultTowers.debug();
//        combinerTowers.debug();

        pages[index].scrollPane = new ScrollPane(allTable, ShardWar.main.getAssetLoader().getSkin());
        pages[index].scrollPane.setScrollingDisabled(true, false);
        pages[index].scrollPane.setFadeScrollBars(false);

        pages[index].add(pages[index].scrollPane);

        pages[index].pack();

    }

    private void addDefaultTower(Tower.TowerPrototype prototype, Table table){
        Image w1 = new Image(prototype.getTexture());
        Label name1 = new Label(prototype.getName(), ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
        Label info1 = new Label(getTowerInfo(prototype), ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");

        Table t1 = new Table();
        t1.add(name1).center().row();
        t1.add(info1).left();

        Table cost1 = new Table();
        Label money1 = new Label(" " + prototype.getCost(), ShardWar.main.getAssetLoader().getSkin(), "description-main");
        Image shard1 = new Image(ShardWar.main.getAssetLoader().get("shard.png", Texture.class));
        cost1.add(money1);
        cost1.add(shard1);

        table.add(w1).size(96, 96).center();
        table.add(cost1).center();
        table.add(t1).expand().fill().left().row();
    }
    private void addCombinedTower(Tower.TowerPrototype prototype, Table table, Texture component1, Texture component2){
        Image w = new Image(prototype.getTexture());
        Label name4 = new Label(prototype.getName(), ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
        Label info4 = new Label(getTowerInfo(prototype), ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");

        Table t = new Table();
        t.add(name4).center().row();
        t.add(info4).left();

        Image combo11 = new Image(component1);
        Image combo12 = new Image(component2);
        Label plus1 = new Label("+", ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
        Table recipe = new Table();
        recipe.add(combo11).size(ICON_SIZE, ICON_SIZE);
        recipe.add(plus1);
        recipe.add(combo12).size(ICON_SIZE, ICON_SIZE);

        table.add(w).size(ICON_SIZE, ICON_SIZE).center();
        table.add(recipe).center();
        table.add(t).center().row();
    }

    private void initSpells(int index){
//        pages[2].defaults().pad(10f);

        Table spellTable = new Table();
//        spellTable.debug();
        spellTable.pad(10f);
        spellTable.padRight(30f);
        spellTable.defaults().space(30f);

        Label t1 = new Label("Tier 1", ShardWar.main.getAssetLoader().getSkin(), "description-main");
        spellTable.add(t1).colspan(4).center().row();

        ArrayList<Spell.SpellPrototype> sp1 = FightScreen.allSpells.get(1);
        for(Spell.SpellPrototype p:sp1){
            addSpellOnTable(p, spellTable);
        }

        Label t2 = new Label("Tier 2", ShardWar.main.getAssetLoader().getSkin(), "description-main");
        spellTable.add(t2).colspan(4).center().row();

        ArrayList<Spell.SpellPrototype> sp2 = FightScreen.allSpells.get(2);
        for(Spell.SpellPrototype p:sp2){
            addSpellOnTable(p, spellTable);
        }

        Label t3 = new Label("Tier 3", ShardWar.main.getAssetLoader().getSkin(), "description-main");
        spellTable.add(t3).colspan(4).center().row();

        ArrayList<Spell.SpellPrototype> sp3 = FightScreen.allSpells.get(3);
        for(Spell.SpellPrototype p:sp3){
            addSpellOnTable(p, spellTable);
        }

        Label t4 = new Label("Tier 4", ShardWar.main.getAssetLoader().getSkin(), "description-main");
        spellTable.add(t4).colspan(4).center().row();

        ArrayList<Spell.SpellPrototype> sp4 = FightScreen.allSpells.get(4);
        for(Spell.SpellPrototype p:sp4){
            addSpellOnTable(p, spellTable);
        }


        pages[index].scrollPane = new ScrollPane(spellTable, ShardWar.main.getAssetLoader().getSkin());
        pages[index].scrollPane.setScrollingDisabled(true, false);
        pages[index].scrollPane.setFadeScrollBars(false);

        pages[index].add(pages[index].scrollPane);
    }

    private static class IconContainer extends Actor {
        private Texture slotTexture;
        private Texture iconTexture;
        private final int drawOffset = 5;

        public IconContainer(Texture slotTexture, Texture iconTexture) {
            this.slotTexture = slotTexture;
            this.iconTexture = iconTexture;
        }
        public void draw(Batch batch, float parentAlpha) {
            if(slotTexture != null) batch.draw(slotTexture, getX(), getY(), getWidth(), getHeight());
            batch.draw(iconTexture, getX() + drawOffset, getY() + drawOffset,
                    getWidth() - drawOffset*2, getHeight() - drawOffset*2);
        }
    }

    private static class SpellContainer extends Actor {
        private Texture slotTexture;
        private Texture spellTexture;
        private final int drawOffset = 2;

        public SpellContainer(Spell.SpellPrototype spell) {
//            this.spell = spell;
            spellTexture = ShardWar.main.getAssetLoader().getSpell(spell.getName());
            slotTexture = ShardWar.main.getAssetLoader().getSpellSlotBgTexture(spell.getTier());
        }
        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(slotTexture, getX(), getY(), getWidth(), getHeight());
            batch.draw(spellTexture, getX() + drawOffset, getY() + drawOffset,
                        getWidth() - drawOffset*2, getHeight() - drawOffset*2);

        }
    }

    private void addSpellOnTable(Spell.SpellPrototype spell, Table table){
        AssetLoader a = ShardWar.main.getAssetLoader();
//        Image w = new Image(a.getSpell(spell.getName()));
        SpellContainer w = new SpellContainer(spell);

//        Texture slotTexture = ShardWar.main.getAssetLoader().getSpellSlotBgTexture(spell.getTier());

        Label name = new Label(a.getWord(spell.getName()), a.getSkin(), "spell-tooltip");
//        String inf = "";

        Table info = SpellTooltip.getTooltipTable(spell);
        info.defaults().left();
        Label spellTooltip = new Label(spell.getTooltip(),
                ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
//        Table spellContainer = new Table();
//        spellContainer.setBackground(ShardWar.main.getAssetLoader().getSkin().getDrawable("info-panel"));
//        spellContainer.add(w);
//        spellContainer.add(name);
//        spellContainer.add(info).left();
//        spellContainer.add(spellTooltip).left().row();
//        table.add(spellContainer);
        table.add(w).size(SPELL_ICON_SIZE, SPELL_ICON_SIZE);
        table.add(name);
        table.add(info).left();
        table.add(spellTooltip).left().row();
    }


    private void switchWindow(int window){
//        if(currentWindow != window) {
            currentWindow = window;
            update();
//        }
    }
    private void update(){
        for (int i = 0; i < pages.length; i++){
            if(i != currentWindow){
                pages[i].setVisible(false);
            }else {
                pages[i].setVisible(true);
                pages[i].scrollPane.toFront();

//                pages[i].scrollPane.scrollTo(0, pages[i].scrollPane.getMaxY(), 0, 0);
//                pages[i].scrollPane.hit(20, 20, true);
            }
        }
    }

//    @Override
//    public void hide() {
//        super.hide();
//    }
}
