package com.darkhouse.shardwar.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.darkhouse.shardwar.Logic.*;
import com.darkhouse.shardwar.Logic.GameEntity.Empty;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.*;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Model.SpellBuy;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Model.SpellPanel;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Model.SpellRollPanel;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.Slot.PlayerSlot;
import com.darkhouse.shardwar.Logic.Slot.Slot;
import com.darkhouse.shardwar.Logic.Slot.TowerSlot;
import com.darkhouse.shardwar.Logic.Slot.WallSlot;
import com.darkhouse.shardwar.Model.ShardPanel;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;

import java.util.*;

public class FightScreen extends AbstractScreen {

    private static final int CORNER_SPACE = 10;
    private static final int INCOME = 4;
    public static final int ROLL_ROUND = 3;
    public static final int PREROLL_ROUNDS = 5;
    private static final int[] heights = {200, 260, 320};

    public static final ShapeRenderer sp = new ShapeRenderer();

//    private Field field1;
//    private Field field2;
    private Field fields[];
    private Image turnTexture[];

    public Array<Projectile> projectiles;

    public static class Field extends Table{
//        private Slot[][] objects;
        private TowerSlot[] towers;//TODO rework with array[][]
        private Array<WallSlot> walls;
        private PlayerSlot playerSlot;

        public ArrayList<Slot> getMainObjects(){
            ArrayList<Slot> list = new ArrayList<>();
            for(TowerSlot t:towers){
                /*if(t.getObject() != null) */list.add(t);
            }
            for(WallSlot w:walls){
                /*if(w.getObject() != null) */list.add(w);
            }
            return list;
        }

        public ArrayList<Slot> getObjects(){
            ArrayList<Slot> list = getMainObjects();
            list.add(playerSlot);
            return list;
        }
        public ArrayList<GameObject> getGameObjects(){
            ArrayList<GameObject> list = new ArrayList<>();
            for (Slot s: getObjects()){
                list.add(s.getSomeObject());
            }
            return list;
        }
        public ArrayList<GameObject> getNotEmptyGameObjects(){
            ArrayList<GameObject> list = new ArrayList<>();
            for (Slot s: getNotEmptySlots()){
                list.add(s.getObject());
            }
            return list;
        }

        public ArrayList<Slot> getNotEmptySlots(){
            ArrayList<Slot> list = new ArrayList<>();
            for (Slot s:getMainObjects()){
                if(!s.empty())list.add(s);
            }
            return list;
        }

        public ArrayList<Slot> getOnColumn(int column){
            ArrayList<Slot> curr = new ArrayList<Slot>();
            for (Slot g:getObjects()){
                if(g.getColumn() == column) curr.add(g);
            }
            return curr;
        }
        public ArrayList<Slot> getOnRow(int row){
            ArrayList<Slot> curr = new ArrayList<Slot>();
            for (Slot g:getObjects()){
                if(g.getRow() == row) curr.add(g);
            }
            return curr;
        }
        public Slot get(int row, int column){
            for (Slot g:getObjects()){
                if(g.getRow() == row && g.getColumn() == column) return g;
            }
            throw new IllegalArgumentException("wrong row (" + row + ") or column (" + column + ")");
        }
//        public Slot getRelative(int row, int column){
//            return objects[row][column];
//        }

        public Slot getByCoords(float x, float y){
            for (Slot s:getObjects()){
                if(s.contains(x, y)) return s;
            }
            return null;
        }

        public float getYOfRow(int row, boolean withHeightOffset){
            Slot s = getOnRow(row).get(0);
            float f = s.getY() + s.getParent().getY();
            if(!withHeightOffset)return f;
            else return f + s.getHeight();//can be faster
        }
        public float getXofColumn(int column){
            Slot s = getOnColumn(column).get(0);
            float f = s.getX() + s.getParent().getX();
            return f + s.getWidth();//can be faster
        }

        public void setTowers(TowerSlot[] towers) {
            this.towers = towers;
        }
        public void setWalls(Array<WallSlot> walls) {
            this.walls = walls;
        }
        public void setPlayerSlot(PlayerSlot playerSlot) {
            this.playerSlot = playerSlot;
        }

        public TowerSlot[] getTowers() {
            return towers;
        }
        public Array<WallSlot> getWalls() {
            return walls;
        }

        public PlayerSlot getPlayerSlot() {
            return playerSlot;
        }
    }


//    private Player p1;
//    private Player p2;
    private Player[] players;

    private int round;

    public int getRound() {
        return round;
    }

    private int currentPlayer;


    public Player getCurrentPlayerObject(){
        return players[currentPlayer - 1];
    }
    public Player getOppositePlayerObject(){
        return players[getOppositePlayer() - 1];
    }

    public int getCurrentPlayer() {//1-2
        return currentPlayer;
    }
    public int getEnemyPlayer(int i){
        return 3 - i;
    }
    public int getOppositePlayer(){
        return getEnemyPlayer(currentPlayer);
    }
    public int getPlayerIndex(Player player){
        if(player == players[0]) return 1;
        if(player == players[1]) return 2;
        else return -1;
    }

//    public Field getField(Player player){
//        if(player == players[0]) return fields[0];
//        if(player == players[1]) return fields[1];
//        return null;
//    }
    public Field getCurrentField(){
        return getField(currentPlayer);
    }
    public Field getOppositeField(){
        return getField(getOppositePlayer());
    }

    public Field getField(int i){
        return fields[i - 1];
    }
    public Field getField(Player player){
        return getField(getPlayerIndex(player));
    }
    public Field getEnemyField(Player player){
        return getField(getEnemyPlayer(getPlayerIndex(player)));
    }


    private ProgressBar timeBar;
    private float turnTime = 45f;
    private Dialog turnDialog;
//    private Label turnLabel;
//    private Window turnWindow;

    private Slot<Tower.TowerPrototype, Tower> targetSlot;
//    private Image targeter;
    private Array<Image> targetSelected;
    private SpellBuy spellBuy;

    private SpellPanel spellPanel1;
    private SpellPanel spellPanel2;

    private SpellRollPanel spellRollPanel;
    private TurnBuyButton turnBuyButton;
    private TextButton currentRound;

    public void setTargetSlot(Slot<Tower.TowerPrototype, Tower> targetSlot) {
        this.targetSlot = targetSlot;
        ShardWar.fightScreen.getCurrentField().setTouchable(Touchable.disabled);
        if(targetSlot.getNumberSelected() < targetSlot.getSelected().length) {
            /*if(targetSlot.getObject().isGlobal()){

            }else*/ {
                targetSlot.setTargeter(new Image(ShardWar.main.getAssetLoader().getCenterTarget(getCurrentPlayer())));
                targetSlot.select(targetSlot.getColumn());
                targetSlot.getTargeter().setHeight(heights[0]);
                if (currentPlayer == 1) {
                    targetSlot.getTargeter().setDrawable(ShardWar.main.getAssetLoader().getCenterTarget(1));
                    targetSlot.getTargeter().setPosition(targetSlot.getX() + targetSlot.getParent().getX(),
                            targetSlot.getY() + targetSlot.getParent().getY() + 52);
                }
                if (currentPlayer == 2) {
                    targetSlot.getTargeter().setDrawable(ShardWar.main.getAssetLoader().getCenterTarget(2));
                    targetSlot.getTargeter().setPosition(targetSlot.getX() + targetSlot.getParent().getX(),
                            targetSlot.getY() + targetSlot.getParent().getY() - targetSlot.getTargeter().getHeight() - 4);
                }
                targetSlot.getTargeter().setTouchable(Touchable.disabled);
                stage.addActor(targetSlot.getTargeter());
            }
        }else {
            AlphaAction a = new AlphaAction();
            a.setAlpha(1f);
//            System.out.println(targetSlot);
//            System.out.println(targetSlot.getTargeter());
            targetSlot.getTargeter().addAction(a);
            targetSlot.getTargeter().toFront();
        }
    }
//    private AlphaAction a;

    public FightScreen() {
        super(ShardWar.main.getAssetLoader().getMainMenuBg());
    }

    @Override
    public void show() {
        HideListener hideListener = new HideListener();
        AttackPathListener attackPathListener = new AttackPathListener();
        Gdx.input.setInputProcessor(new InputMultiplexer(attackPathListener, stage, hideListener));


        startTurn();
    }

    public void hasChanged(){
        for (Slot s:fields[0].getWalls()){
            s.hasChanged();
        }
        for (Slot s:fields[0].getTowers()){
            s.hasChanged();
        }
        for (Slot s:fields[1].getWalls()){
            s.hasChanged();
        }
        for (Slot s:fields[1].getTowers()){
            s.hasChanged();
        }
    }

    public class HideListener extends InputAdapter {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            hideTooltips();
            return true;
        }
    }
    public class AttackPathListener extends InputAdapter{
        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            if(targetSlot != null){
                int width = 48;
                float x = targetSlot.getX() + targetSlot.getParent().getX();
                Drawable texture = null;
                if (screenX > targetSlot.getX() + targetSlot.getParent().getX() + targetSlot.getWidth()) {
                    if (targetSlot.getColumn() != 2) {
                        targetSlot.getTargeter().setDrawable(ShardWar.main.getAssetLoader().getRightTarget(currentPlayer));
                         if(targetSlot.getObject().isGlobal() && targetSlot.getColumn() == 0 && screenX > getCurrentField().getXofColumn(1)){
                             texture = ShardWar.main.getAssetLoader().getRightTarget2(currentPlayer);
                             width = 48*5;
//                             x = x - 30;
                             targetSlot.select(targetSlot.getColumn() + 2);
                         }else {
                             texture = ShardWar.main.getAssetLoader().getRightTarget(currentPlayer);
                             width = 48*3;
                             targetSlot.select(targetSlot.getColumn() + 1);
                         }
                    }
                } else if (screenX < targetSlot.getX() + targetSlot.getParent().getX()) {
                    if (targetSlot.getColumn() != 0) {
                        if(targetSlot.getObject().isGlobal() && targetSlot.getColumn() == 2 && screenX < getCurrentField().getXofColumn(0)){
                            texture = ShardWar.main.getAssetLoader().getLeftTarget2(currentPlayer);
                            width = 48*5;
                            x = x - 48 * 4;
//                            x = x - 30;
                            targetSlot.select(targetSlot.getColumn() - 2);
                        }else {
                            texture = ShardWar.main.getAssetLoader().getLeftTarget(currentPlayer);
                            width = 48 * 3;
                            x = x - 48 * 2;
//                        targetSlot.getTargeter().setX(targetSlot.getX() + targetSlot.getParent().getX() - 48 * 2);
                            targetSlot.select(targetSlot.getColumn() - 1);
                        }
                    }
                } else {
                    texture = ShardWar.main.getAssetLoader().getCenterTarget(currentPlayer);
                    targetSlot.select(targetSlot.getColumn());
                }
                if(texture == null)return false;
                targetSlot.getTargeter().setWidth(width);
                targetSlot.getTargeter().setX(x);
                targetSlot.getTargeter().setDrawable(texture);
                globalMove(screenY);
//                }
            }
            return super.mouseMoved(screenX, screenY);
        }

        private void globalMove(int screenY) {
            if(targetSlot.getObject().isGlobal()){
                if(currentPlayer == 1) {
                    if (screenY > getCurrentField().getYOfRow(1, true)) {
                        targetSlot.getTargeter().setHeight(heights[0]);
                        targetSlot.selectRow(0);
                    } else if (screenY > getCurrentField().getYOfRow(2, true)) {
                        targetSlot.getTargeter().setHeight(heights[1]);
                        targetSlot.selectRow(1);
                    } else {
                        targetSlot.getTargeter().setHeight(heights[2]);
                        targetSlot.selectRow(2);
                    }
                }else {
                    if (screenY < getCurrentField().getYOfRow(1, false)) {
                        targetSlot.getTargeter().setHeight(heights[0]);
                        targetSlot.selectRow(0);
                    } else if (screenY < getCurrentField().getYOfRow(2, false)) {
                        targetSlot.getTargeter().setHeight(heights[1]);
                        targetSlot.selectRow(1);
                    } else {
                        targetSlot.getTargeter().setHeight(heights[2]);
                        targetSlot.selectRow(2);
                    }
                    targetSlot.getTargeter().setY(targetSlot.getY() + targetSlot.getParent().getY() -
                            targetSlot.getTargeter().getHeight() - 4);
                }
            }else targetSlot.getTargeter().setHeight(heights[0]);
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if(button == 1 && targetSlot != null){
                for (int i = 0; i < targetSlot.getAllTargeters().length; i++) {
                    if(targetSlot.getAllTargeters()[i] != null) {
                        targetSlot.getAllTargeters()[i].remove();
                        targetSelected.removeValue(targetSlot.getAllTargeters()[i], true);
                    }
                }
                targetSlot.flushSelect();
                targetSlot.flushTargeter();
                targetSlot = null;
                ShardWar.fightScreen.getCurrentField().setTouchable(Touchable.enabled);
                return true;
            }
            if(button == 0 && targetSlot != null && targetSlot.getTargeter() != null){

//                targeter.setVisible(false);

//                targeter.remove();

//                targetSlot.select(-1);
                targetSelected.add(targetSlot.getTargeter());
                AlphaAction a = new AlphaAction();
                a.setAlpha(0.4f);
                targetSlot.getTargeter().addAction(a);
                targetSlot.endSelect();
                targetSlot = null;
                ShardWar.fightScreen.getCurrentField().setTouchable(Touchable.enabled);
                return true;
            }
            return false;
        }
    }


    public void init(){
        targetSelected = new Array<>();
        players = new Player[2];
        fields = new Field[2];
        projectiles = new Array<>();
        t = new Timer();
        buyedInit = new int[2];
//        a = new AlphaAction();

        initUsers();
        initPlayerField();
        initEnemyFiled();
        setPlayerShootPositions();
        initTimeBar();
        initTurnImages();
        createDialog();
        initTimeSkipButton();
        initBuyTurnInitiative();
        addSpellPanel();
        initSpellDialog();
        initSpellRollPanel();
        initCurrentRoundText();
    }

    private void initTurnImages(){//after timeBar
        turnTexture = new Image[2];
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(0xedb2293f);
        p.fill();
        Texture t = new Texture(p);
        p.dispose();
        turnTexture[0] = new Image(/*ShardWar.main.getAssetLoader().get("mobHpBarKnob.png", Texture.class)*/t);
        turnTexture[1] = new Image(/*ShardWar.main.getAssetLoader().get("mobHpBarKnob.png", Texture.class)*/t);
        turnTexture[0].setBounds(0, 0, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()/2f - timeBar.getHeight()/2);
        turnTexture[1].setBounds(0, Gdx.graphics.getHeight()/2f + timeBar.getHeight()/2, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()/2f - timeBar.getHeight()/2);
        stage.addActor(turnTexture[0]);
        stage.addActor(turnTexture[1]);
        turnTexture[0].setVisible(false);
        turnTexture[1].setVisible(false);
        turnTexture[0].toBack();
        turnTexture[1].toBack();
        background.toBack();
    }

    private void initSpellRollPanel(){
        spellRollPanel = new SpellRollPanel(spellPool);
        stage.addActor(spellRollPanel);
        spellRollPanel.init();
        spellRollPanel.setPosition(30, Gdx.graphics.getHeight()/2f - spellRollPanel.getHeight()/2);
    }

    private void initCurrentRoundText(){
        AssetLoader l = ShardWar.main.getAssetLoader();
        String text = l.getWord("currentRound") + ": " + round + " (" + (numberChange + 1) + "/2)";
        currentRound = new TextButton(text, l.getSkin(), "description");

        currentRound.setTouchable(Touchable.disabled);
        currentRound.setPosition(Gdx.graphics.getWidth() - currentRound.getWidth(), 0);
        stage.addActor(currentRound);
    }
    private void updateCurrentRoundPanel(){
        AssetLoader l = ShardWar.main.getAssetLoader();
        String text = l.getWord("currentRound") + ": " + round + " (" + (numberChange + 1) + "/2)";
        currentRound.setText(text);
    }


    private void initTimeSkipButton(){
        AssetLoader l = ShardWar.main.getAssetLoader();
        TextButton timeSkip = new TextButton(l.getWord("endTurn"), l.getSkin());
        timeSkip.setPosition(Gdx.graphics.getWidth() - /*timeSkip.getWidth() - */ 130,
                Gdx.graphics.getHeight()/2f - timeSkip.getHeight() - 10);
        timeSkip.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                timeBar.setValue(turnTime);
                endTime();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        stage.addActor(timeSkip);
    }

    private int[] buyedInit;

    private class TurnBuyButton extends Table{
        private static final int minimumValue = 3;

        private TextButton buyTurn;
        private AlphaAction a;
//        private Image shard;
        private ImageButton less;
        private ImageButton more;
        private int num;
        private Label numShards;

        public TurnBuyButton() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            defaults().spaceBottom(5f);
            a = new AlphaAction();
            num = minimumValue;
            buyTurn = new TextButton(l.getWord("buyNext"), l.getSkin());
            buyTurn.setTransform(false);
//            shard = new Image(ShardWar.main.getAssetLoader().get("shard.png", Texture.class));
            less = new ImageButton(l.generateImageButtonSkin
                    (l.get("less.png", Texture.class)));
            more = new ImageButton(l.generateImageButtonSkin
                    (l.get("more.png", Texture.class)));
            less.addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if(num > minimumValue) {
                        num--;
                        updateNum();
                    }
                    return true;
                }
            });
            more.addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if(getCurrentPlayerObject().getShards() > num) {
                        num++;
                        updateNum();
                    }
                    return true;
                }
            });
            buyTurn.addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if(getCurrentPlayerObject().deleteShards(num)) {
                        buyedInit[currentPlayer - 1] = num;
//                        AlphaAction a = new AlphaAction();
                        a.setAlpha(0.4f);
                        a.restart();
                        addAction(a);
                        setTouchable(Touchable.disabled);
                    }
                    return true;
                }
            });
            numShards = new Label(String.valueOf(num), l.getSkin());
            add(less);
            add(numShards);
            add(more);
            row();
            add(buyTurn).colspan(3);
            pack();
        }
        private void updateNum(){
            numShards.setText(String.valueOf(num));
        }
        public void reset(){
            num = minimumValue;
            updateNum();
//            AlphaAction a = new AlphaAction();
//            a.reset();
            a.setAlpha(1f);
            a.restart();
            addAction(a);
            setTouchable(Touchable.enabled);
        }
    }


    private void initBuyTurnInitiative(){
        turnBuyButton = new TurnBuyButton();
        turnBuyButton.setPosition(Gdx.graphics.getWidth() - /*turnBuyButton.getWidth() - */130,
                Gdx.graphics.getHeight()/2f - turnBuyButton.getHeight() + 75);
        stage.addActor(turnBuyButton);
    }

    private void initTimeBar(){
        int height = 10;
        timeBar = new ProgressBar(0, turnTime, 0.01f, false,
                ShardWar.main.getAssetLoader().getTimeBarStyle(height)){
            @Override
            public void act(float delta) {
                if(allowStartTurn) {
                    setValue(getValue() - delta);
                    if (getValue() == 0) {
                        endTime();
//                        setValue(turnTime);
                    }
                }
            }

//            @Override
//            public float getPrefHeight() {
//                return 20;
//            }
        };
        timeBar.setPosition(0, Gdx.graphics.getHeight()/2f - height/2f);
        timeBar.setValue(turnTime);
//        timeBar.pack();
//        timeBar.debug();


        timeBar.setSize(Gdx.graphics.getWidth(), height);
//        timeBar.pack();
//        timeBar.pack();
//        timeBar.getStyle().background.setMinHeight(height);
//        timeBar.getStyle().knobBefore.setMinHeight(height - 2);
//        System.out.println(timeBar.getStyle());

        stage.addActor(timeBar);
    }

    private void initDialog(int buyed){
        String roundText = "Round " + round + " (" + (numberChange + 1) + "/2)";
        String turnText = "Now turn of ";
        turnText += "Player " + currentPlayer;
        if(buyed != -1) turnText += System.getProperty("line.separator") + "(Buyed by player " + buyed + ")";
//        if(buyedInit[0] > buyedInit[1])turnText += " (Buyed by player 1)";
//        if(buyedInit[0] < buyedInit[1])turnText += " (Buyed by player 2)";
//        turnDialog.text(text);
//        turnLabel.setText(text);
//        turnLabel.pack();
//        turnLabel.setPosition(Gdx.graphics.getWidth()/2f - turnLabel.getWidth()/2,
//                Gdx.graphics.getHeight()/2f - turnLabel.getHeight()/2);
//        turnDialog = new Dialog(roundText, ShardWar.main.getAssetLoader().getSkin(), "description");

        turnDialog.getTitleLabel().setText(roundText);
//        turnDialog.getContentTable().clear();
        turnDialog.clear();
//        turnDialog.text(roundText).row();
        Label l = new Label(turnText, ShardWar.main.getAssetLoader().getSkin(), "description-main");
        turnDialog.add(l).align(Align.center).pad(5f).row();

//        turnDialog.text(turnText).align(Align.center);//java.lang.IndexOutOfBoundsException: index can't be >= size: 1 >= 0
        turnDialog.pack();
//        turnDialog.getButtonTable().getCells().get(0).getActor() =
    }
    private void createDialog(){
        turnDialog = new Dialog("", ShardWar.main.getAssetLoader().getSkin(), "description");
        turnDialog.getTitleLabel().setAlignment(Align.center);

//        turnLabel = new Label("", ShardWar.main.getAssetLoader().getSkin(), "turn-info");

//        stage.addActor(turnLabel);
//        turnWindow = new Window("", ShardWar.main.getAssetLoader().getSkin());
//        turnDialog = new Dialog("", ShardWar.main.getAssetLoader().getSkin());
    }

    private boolean allowStartTurn = false;

    private void startTurn(){
        offTouch();

        int buyedTurn = -1;
        if(buyedInit[0] != buyedInit[1]){
            if(buyedInit[0] > buyedInit[1]) currentPlayer = 1;
            else currentPlayer = 2;
            buyedTurn = currentPlayer;
        }else {
            Random r = new Random();
            int n = r.nextInt(2);
            currentPlayer = n + 1;
        }
        round++;
//        updateCurrentRoundPanel();
        buyedInit[0] = 0;
        buyedInit[1] = 0;

        if(round % ROLL_ROUND == 0 && numberChange == 0){
            rollSpells();
        }
        showPlayerTurn(buyedTurn);
    }
    private void showTurnLabel(){
        Color color = turnDialog.getColor();//Libgdx bug #3920 when dialog flick
        color.a = 0;
        turnDialog.setColor(color);
        turnDialog.show(stage);
//        turnLabel.setVisible(true);
    }
    private void hideTurnLabel(){
        turnDialog.hide();
//        turnLabel.setVisible(false);
    }
    private void clearTargetSelected(){
        if(targetSlot != null) targetSlot.getTargeter().remove();
        for (Image i:targetSelected){
            i.remove();
        }
    }

    private void actEffects(){
        for (Slot s:fields[currentPlayer - 1].getObjects()){
            if(s.getSomeObject() != null) s.getSomeObject().actEffects();
        }
    }

    private void changePlayerTurnHighlighter(){
        if(currentPlayer == 1) {
            turnTexture[0].setVisible(true);
            turnTexture[1].setVisible(false);
        } else {
            turnTexture[0].setVisible(false);
            turnTexture[1].setVisible(true);
        }
    }

    private void showPlayerTurn(int buyed){
        initDialog(buyed);
        showTurnLabel();
        changePlayerTurnHighlighter();
        updateCurrentRoundPanel();

//        Timer t = new Timer();
//        hideTurnLabel();
//        if(round % ROLL_ROUND == 0) showSpells();
//        allowStartTurn = true;
//        onTouch();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                hideTurnLabel();
                if(round % ROLL_ROUND == 0) showSpells();
                allowStartTurn = true;
                onTouch();
                cancel();
            }
        }, 1000);
    }

    private void addSpellPanel(){
        spellPanel1 = new SpellPanel(players[0]);
        spellPanel2 = new SpellPanel(players[1]);

        spellPanel1.setSize(250, 70);
        spellPanel2.setSize(250, 70);
//        spellPanel.debug();
//        spellPane2.debug();

        spellPanel1.setPosition(50, 30);
        spellPanel2.setPosition(50, Gdx.graphics.getHeight() - spellPanel2.getHeight() - 30);

        stage.addActor(spellPanel1);
        stage.addActor(spellPanel2);

        spellPanel1.init();
        spellPanel2.init();
    }

//    private int spellRoll = 0;
    private static float chances[][] = new float[][]{
            {1f, 0, 0, 0},
            {0.7f, 0.3f, 0, 0},
            {0.6f, 0.3f, 0.1f, 0},
            {0.4f, 0.3f, 0.2f, 0.1f},
            {0.3f, 0.3f, 0.3f, 0.1f},
            {0.2f, 0.3f, 0.3f, 0.2f},
            {0.1f, 0.3f, 0.3f, 0.3f},
    };



    private void showSpells(){
//        spellBuy.clear();

//        System.out.println(spellBuy.getChildren());
        Color color = spellBuy.getColor();//Libgdx bug #3920 when dialog flick
        color.a = 0;
        spellBuy.setColor(color);
        spellBuy.show(stage);
    }
//    private static Spell.SpellPrototype[] spellPool = new Spell.SpellPrototype[10];
    private static ArrayList<Spell.SpellPrototype> spellPool = new ArrayList<>();
    private static HashMap<Integer, ArrayList<Spell.SpellPrototype>> allSpells = new HashMap<>();

    private Spell.SpellPrototype getSpell(int i){
        return spellPool.get(i%spellPool.size());
    }

    private void initSpellDialog(){
        spellBuy = new SpellBuy();
        spellBuy.setSize(200, 200);
        spellBuy.init(stage);

        allSpells.put(1, new ArrayList<>(
                Arrays.asList(
                        new FireBreath.P(4),
                        new Disarm.P(2),
                        new Heal.P(5),
                        new Greed.P(8),
                        new Weakness.P(1, 3)
                )
        ));
        allSpells.put(2, new ArrayList<>(
                Arrays.asList(
                        new Vulnerability.P(1, 2),
                        new Silence.P(3),
                        new ShieldsUp.P(4, 8),
                        new LifeDrain.P(6),
                        new DisableField.P(3),
                        new FanOfKnives.P(2, 4)
                )
        ));
        allSpells.put(3, new ArrayList<>(
                Arrays.asList(
                        new FatalBlow.P(4),
                        new PoisonSplash.P(3, 2, 4),
                        new NotToday.P(3),
                        new ClawSmash.P(2, 2, 3),
                        new Vampire.P(3, 1f),
                        new AdditionalRocket.P(3, 2),
                        new Dispel.P()
                )
        ));
        allSpells.put(4, new ArrayList<>(
                Arrays.asList(
                        new OpticalSight.P(5, 2),
                        new FireBullets.P(4, 3, 2),
                        new Combiner.P(),
                        new DarkRitual.P(2f),
                        new Rejuvenation.P(3, 3),
                        new Immune.P(5)
                )
        ));
        for(Map.Entry<Integer, ArrayList<Spell.SpellPrototype>> s:allSpells.entrySet()){
            for (Spell.SpellPrototype prototype:s.getValue()){
                prototype.setTier(s.getKey());
            }
        }

        initSpellsInNextRounds(PREROLL_ROUNDS);

    }
    private void initSpellsInNextRounds(int num){
        Random r = new Random();
        for (int t = 0; t < num; t++) {
            int m = t + round/ROLL_ROUND;
            if(spellPool.size()/3 >= PREROLL_ROUNDS) m += PREROLL_ROUNDS - 1;
            if(m >= chances.length) m = chances.length - 1;
            for (int i = 0; i < 3; i++) {
                float f = r.nextFloat();
                int currTier;
                if(f < chances[m][0]){
                    currTier = 1;
                }else if (f < chances[m][1] + chances[m][0]){
                    currTier = 2;
                }else if(f < chances[m][2] + chances[m][1] + chances[m][0]){
                    currTier = 3;
                }else {
                    currTier = 4;
                }
                ArrayList<Spell.SpellPrototype> h = allSpells.get(currTier);
                int sp = r.nextInt(h.size());
                spellPool.add(h.get(sp));
//                System.out.println("randoms " + t + " " + currTier + " " + f + " " + sp);
            }
        }
        if(num == 1) {
            spellPool.remove(2);
            spellPool.remove(1);
            spellPool.remove(0);
        }
    }

    private void rollSpells(){
        spellBuy.clearSpells();
//        spellBuy.setSize(200, 200);
        spellBuy.addSpell(getSpell(/*spellRoll*/0));
        spellBuy.addSpell(getSpell(/*spellRoll*/ 1));
        spellBuy.addSpell(getSpell(/*spellRoll*/ 2));
//        spellRoll += 3;
        spellBuy.pack();
    }

    private int numberChange;

    private void offTouch(){
        fields[0].setTouchable(Touchable.disabled);
        fields[1].setTouchable(Touchable.disabled);
    }
    private void onTouch(){
        /*if(currentPlayer == p1)*/ fields[0].setTouchable(Touchable.enabled);
        /*if(currentPlayer == p2)*/ fields[1].setTouchable(Touchable.enabled);
    }

    private void clearListeners(){
        targetSlot = null;
    }

    public Timer t;



    private void endTime(){
        allowStartTurn = false;
        hideTooltips();
        hideSpellTargeters();
        clearTargetSelected();
        clearListeners();
        spellBuy.hide();
        turnBuyButton.reset();
        offTouch();

        if(round % ROLL_ROUND == 0 && numberChange == 1){
            initSpellsInNextRounds(1);
            spellRollPanel.nextRoll();
        }

        if(round != 1){
            fight();
//            shot = 0;


//            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(projectiles.size == 0){
                        actEffects();
                        nextPlayer();
                        cancel();
                    }
                }
            }, 500, 500);
        }else nextPlayer();
    }

    private void hideSpellTargeters(){
        spellPanel1.clearListeners();
        spellPanel2.clearListeners();
    }

    private void nextPlayer(){
        if(numberChange >= 1) {
            numberChange = 0;
            income();
            startTurn();
        }else changePlayerTurn();
        timeBar.setValue(turnTime);
    }

//    private int shot = 0;

    private void fight(){
        for (final Slot<Tower.TowerPrototype, Tower> s:fields[getCurrentPlayer() - 1].getTowers()){
            if(/*s.getSelected() != -1*/!s.empty() && !s.isNoSelected()) {
//                System.out.println(Arrays.toString(s.getSelected()));
//                for (int i = 0; i < s.getSelected().length; i++) {
//                    shootProjectile(s, 0);

                s.getObject().setCanShoot(true);
//                Timer t = new Timer();
//                t.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        s.getObject().setCanShoot(true);
//                    }
//                }, 100/* *shot*/);
//                shot++;
            }
        }

//        clearTargetSelected();
    }

//    private void shootProjectile(final Slot<Tower.TowerPrototype, Tower> s, final int i){
//        System.out.println(Arrays.toString(s.getSelected()));
//        Entity dr = searchTarget(s, s.getSelected()[i]);
//        System.out.println(dr);
//        Vector2 startLocation = new Vector2(s.getX() + s.getParent().getX() + s.getWidth() / 2,
//                s.getY() + s.getParent().getY() + s.getHeight() / 2);
//        Projectile pr = new Projectile(s, startLocation, dr);
//        stage.addActor(pr);
//        projectiles.add(pr);
//
//        if(i < s.getSelected().length - 1){
//            final Timer t = new Timer();
//            t.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    shootProjectile(s, i+1);
//                }
//            }, shootDelay);
//        }
//    }

    public Slot searchTarget(Slot<Tower.TowerPrototype, Tower> attacker, int line, int row){
//        int line = attacker.getSelected()[index];//
        if(/*attacker.getObject().isGlobal()*/row != -1){
            return fields[getOppositePlayer() - 1].getOnRow(row).get(line);
//            return fields[getOppositePlayer() - 1].getOnColumn(line).get(row/*attacker.selectedRow[index]*/);
        }
        for (int i = 0; i < 6; i ++){
            WallSlot curr = fields[getOppositePlayer() - 1].getWalls().get(i);
            if(!curr.empty() && curr.getColumn() == line && curr.getRow() == 0){
                return curr;
            }
        }
        for (int i = 0; i < 6; i ++){
            WallSlot curr = fields[getOppositePlayer() - 1].getWalls().get(i);
            if(!curr.empty() && curr.getColumn() == line && curr.getRow() == 1){
                return curr;
            }
        }
        for (int i = 0; i < 3; i ++){
            TowerSlot curr = fields[getOppositePlayer() - 1].getTowers()[i];
            if(!curr.empty() && curr.getColumn() == line){
                return curr;
            }
        }
        return fields[getOppositePlayer() - 1].getPlayerSlot();
//        return players[getOppositePlayer() - 1];
    }

    private void income(){
        for (int i = 0; i < 2; i++) {
            players[i].addShards(INCOME);
        }
    }

    private void changePlayerTurn(){
        currentPlayer = 3 - currentPlayer;
//        if(currentPlayer == p1) currentPlayer = p2;
//        else currentPlayer = p1;
        numberChange++;
        showPlayerTurn(-1);
    }

    private Field generateField(boolean player){
        Field field = new Field();
//        Table field = new Table();
        field.center();
        field.defaults().space(20, 50, 10, 50).size(48);

        Array<WallSlot> wallSlots = new Array<>();

        if(player) {
            wallSlots.addAll(addWallSlots(field, true, players[0], 0));
            field.row();
            wallSlots.addAll(addWallSlots(field, true, players[0], 1));
            field.row();
            addTowerSlots(field, true, players[0]);
            field.row();
            addPlayerSlot(field, true, players[0]);
        }else {
            addPlayerSlot(field, false, players[1]);
            field.row();
            addTowerSlots(field, false, players[1]);
            field.row();
            wallSlots.addAll(addWallSlots(field, false, players[1], 1));
            field.row();
            wallSlots.addAll(addWallSlots(field, false, players[1], 0));
        }
        field.setWalls(wallSlots);
        field.pack();
        return field;
    }
    private void initTooltips(Table t){
        for (Cell c:t.getCells()){
            if(c.getActor() instanceof Slot) ((Slot) c.getActor()).initTooltips();
        }
    }
    public void hideTooltips(){
        for (int i = 0; i < 2; i++) {
            for (Cell c:fields[i].getCells()){
                if(c.getActor() instanceof Slot) ((Slot) c.getActor()).hideTooltip();
            }
        }
//        for (Cell c:field1.getCells()){
//            if(c.getActor() instanceof Slot) ((Slot) c.getActor()).hideTooltip();
//        }
//        for (Cell c:field2.getCells()){
//            if(c.getActor() instanceof Slot) ((Slot) c.getActor()).hideTooltip();
//        }
    }
    private Array<WallSlot> addWallSlots(Field t, boolean player, Player user, int row){
//        WallSlot[] wallSlots = new WallSlot[3];
        Array<WallSlot> wallSlots = new Array<WallSlot>();
        for (int i = 0; i < 3; i++) {
            wallSlots.add(new WallSlot(player, this, user, i, row));
            wallSlots.peek().setSize(32, 7);
            t.add(wallSlots.peek());
//            wallSlots[i] = new WallSlot(player, this, user, i, row);
//            wallSlots[i].setSize(32, 7);
//            t.add(wallSlots[i]);
        }
        return wallSlots;
    }
    private void addTowerSlots(Field t, boolean player, Player user){
        TowerSlot[] towerSlots = new TowerSlot[3];
        for (int i = 0; i < 3; i++) {
            towerSlots[i] = new TowerSlot(player, this, user, i, 2);
//            towerSlots[i].setSize(36, 36);
            t.add(towerSlots[i]);
        }
        t.setTowers(towerSlots);
    }
    private void addPlayerSlot(Field t, boolean player, Player user){
        PlayerSlot s = new PlayerSlot(player, this, user, 1, 3);
        t.add(new Actor());
        t.add(s);
        t.add(new Actor());
        s.setObject(user);
        user.setSlot(s);
        t.setPlayerSlot(s);
    }

    private void initPlayerField(){
        fields[0] = generateField(true);
        fields[0].setPosition(Gdx.graphics.getWidth()/2f - fields[0].getWidth()/2, CORNER_SPACE);

        stage.addActor(fields[0]);
        fields[0].playerSlot.getObject().init();
        for(Slot s:fields[0].getMainObjects()){
            Empty e = new Empty();
            s.setEmptyObject(e);
            e.setSlot(s);
            e.init();
        }
        initTooltips(fields[0]);//after create empty object
    }
    private void initEnemyFiled(){
        fields[1] = generateField(false);
        fields[1].setPosition(Gdx.graphics.getWidth()/2f - fields[1].getWidth()/2,
                Gdx.graphics.getHeight() - fields[1].getHeight() - CORNER_SPACE);

        stage.addActor(fields[1]);
        fields[1].playerSlot.getObject().init();
        for(Slot s:fields[1].getMainObjects()){
            Empty e = new Empty();
            s.setEmptyObject(e);
            e.setSlot(s);
            e.init();
        }
        initTooltips(fields[1]);//after create empty object
    }

    private void setPlayerShootPositions(){
        for (int i = 0; i < 2; i++) {
            TowerSlot t0 = fields[0].getTowers()[0];
            TowerSlot t1 = fields[0].getTowers()[1];
            TowerSlot t2 = fields[0].getTowers()[2];

            players[i].setShootCoord(new float[]{
                    t0.getX() + t0.getParent().getX() + t0.getWidth()/2,
                    t1.getX() + t1.getParent().getX() + t1.getWidth()/2,
                    t2.getX() + t2.getParent().getX() + t2.getWidth()/2});
        }
    }

    private void initUsers(){
//        players[0] = new Player();

//        PlayerSlot s1 = new PlayerSlot();
        players[0] = new Player(new Player.PlayerPrototype(
                ShardWar.main.getAssetLoader().get("player1Logo.png", Texture.class),
                "Джавист"));
//        players[0].setPosition(Gdx.graphics.getWidth()/2f, 0);
//        stage.addActor(players[0]);
        players[0].initShards();
        players[1] = new Player(new Player.PlayerPrototype(
                ShardWar.main.getAssetLoader().get("player2Logo.png", Texture.class),
                "Даныло"));
//        players[1].setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight() - players[1].getHeight());
//        stage.addActor(players[1]);
        players[1].initShards();
        currentPlayer = 1;


        ShardPanel panel1 = new ShardPanel(players[0]);
        ShardPanel panel2 = new ShardPanel(players[1]);
        panel1.setPosition(Gdx.graphics.getWidth() - panel1.getWidth() - 40, 70);
        panel2.setPosition(Gdx.graphics.getWidth() - panel2.getWidth() - 40,
                Gdx.graphics.getHeight() - panel2.getHeight() - 70);


        stage.addActor(panel1);
        stage.addActor(panel2);
    }
}
