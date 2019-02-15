package com.darkhouse.shardwar.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.darkhouse.shardwar.Logic.*;
import com.darkhouse.shardwar.Logic.GameEntity.Entity;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Model.ShardPanel;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;

import java.util.*;

public class FightScreen extends AbstractScreen {

    private static final int CORNER_SPACE = 70;

//    private Field field1;
//    private Field field2;
    private Field fields[];

    public Array<Projectile> projectiles;

    private static class Field extends Table{
        private TowerSlot[] towers;
        private Array<WallSlot> walls;

        public void setTowers(TowerSlot[] towers) {
            this.towers = towers;
        }
        public void setWalls(Array<WallSlot> walls) {
            this.walls = walls;
        }

        public TowerSlot[] getTowers() {
            return towers;
        }
        public Array<WallSlot> getWalls() {
            return walls;
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


    public Player getCurrentPlayerIndex(){
        return players[currentPlayer - 1];
    }

    public int getCurrentPlayer() {//1-2
        return currentPlayer;
    }
    public int getOppositePlayer(){
        return 3 - currentPlayer;
    }


    private ProgressBar timeBar;
    private float turnTime = 45f;
    private Dialog turnDialog;
//    private Label turnLabel;
//    private Window turnWindow;

    private Slot targetSlot;
//    private Image targeter;
    private Array<Image> targetSelected;

    public void setTargetSlot(Slot targetSlot) {
        this.targetSlot = targetSlot;
        if(targetSlot.isNoSelected()) {
            targetSlot.setTargeter(new Image(ShardWar.main.getAssetLoader().getCenterTarget(getCurrentPlayer())));
            targetSlot.select(targetSlot.getLine());
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
        }else {
            AlphaAction a = new AlphaAction();
            a.setAlpha(1f);
            targetSlot.getTargeter().addAction(a);
        }
    }

    public FightScreen() {
        super(ShardWar.main.getAssetLoader().getMainMenuBg());
        init();
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
                if(screenX > targetSlot.getX() + targetSlot.getParent().getX() + targetSlot.getWidth()){
                    if(targetSlot.getLine() != 2) {
                        if(currentPlayer == 1) targetSlot.getTargeter().setDrawable(ShardWar.main.getAssetLoader().getRightTarget(1));
                        if(currentPlayer == 2) targetSlot.getTargeter().setDrawable(ShardWar.main.getAssetLoader().getRightTarget(2));
                        targetSlot.getTargeter().setWidth(48 * 3);
                        targetSlot.getTargeter().setX(targetSlot.getX() + targetSlot.getParent().getX());
                        targetSlot.select(targetSlot.getLine() + 1);
                    }
                }
                else if(screenX < targetSlot.getX() + targetSlot.getParent().getX()){
                    if(targetSlot.getLine() != 0) {
                        if(currentPlayer == 1) targetSlot.getTargeter().setDrawable(ShardWar.main.getAssetLoader().getLeftTarget(1));
                        if(currentPlayer == 2) targetSlot.getTargeter().setDrawable(ShardWar.main.getAssetLoader().getLeftTarget(2));
                        targetSlot.getTargeter().setWidth(48 * 3);
                        targetSlot.getTargeter().setX(targetSlot.getX() + targetSlot.getParent().getX() - 48 * 2);
                        targetSlot.select(targetSlot.getLine() - 1);
                    }
                }else {
                    if(currentPlayer == 1) targetSlot.getTargeter().setDrawable(ShardWar.main.getAssetLoader().getCenterTarget(1));
                    if(currentPlayer == 2) targetSlot.getTargeter().setDrawable(ShardWar.main.getAssetLoader().getCenterTarget(2));
                    targetSlot.getTargeter().setWidth(48);
                    targetSlot.getTargeter().setX(targetSlot.getX() + targetSlot.getParent().getX());
                    targetSlot.select(targetSlot.getLine());
                }
            }
            return super.mouseMoved(screenX, screenY);
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if(targetSlot != null && targetSlot.getTargeter() != null ){

//                targeter.setVisible(false);

//                targeter.remove();

//                targetSlot.select(-1);
                targetSelected.add(targetSlot.getTargeter());
                AlphaAction a = new AlphaAction();
                a.setAlpha(0.4f);
                targetSlot.getTargeter().addAction(a);
                targetSlot.endSelect();
                targetSlot = null;
            }
            return super.touchDown(screenX, screenY, pointer, button);
        }
    }


    private void init(){
        targetSelected = new Array<Image>();
        players = new Player[2];
        fields = new Field[2];
        projectiles = new Array<Projectile>();

        initUsers();
        initPlayerField();
        initEnemyFiled();
        setPlayerShootPositions();
        initTimeBar();
        createDialog();
        initTimeSkipButton();
    }

    private void initTimeSkipButton(){
        TextButton timeSkip = new TextButton("End Turn", ShardWar.main.getAssetLoader().getSkin());
        timeSkip.setPosition(Gdx.graphics.getWidth() - timeSkip.getWidth() - 10,
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

    private void initTimeBar(){
        timeBar = new ProgressBar(0, turnTime, 0.01f, false,
                ShardWar.main.getAssetLoader().getSkin(), "exp-bar"){
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

            @Override
            public float getPrefHeight() {
                return 20;
            }
        };
        int height = 10;
        timeBar.setPosition(0, Gdx.graphics.getHeight()/2f - height/2f);
        timeBar.setValue(turnTime);
//        timeBar.pack();
//        timeBar.debug();

        timeBar.setSize(Gdx.graphics.getWidth(), height);
        timeBar.getStyle().background.setMinHeight(height);
        timeBar.getStyle().knobBefore.setMinHeight(height - 2);
//        System.out.println(timeBar.getStyle());

        stage.addActor(timeBar);
    }

    private void initDialog(){
        String roundText = "Round " + round + " (" + (numberChange + 1) + "/2)";
        String turnText = "Now turn of ";
        turnText += "Player " + currentPlayer;
//        turnDialog.text(text);
//        turnLabel.setText(text);
//        turnLabel.pack();
//        turnLabel.setPosition(Gdx.graphics.getWidth()/2f - turnLabel.getWidth()/2,
//                Gdx.graphics.getHeight()/2f - turnLabel.getHeight()/2);
//        turnDialog = new Dialog(roundText, ShardWar.main.getAssetLoader().getSkin(), "description");

        turnDialog.getTitleLabel().setText(roundText);
        turnDialog.getContentTable().clear();
//        turnDialog.text(roundText).row();
        turnDialog.text(turnText);
//        turnDialog.pack();
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
        Random r = new Random();
        round++;
        int n = r.nextInt(2);
        currentPlayer = n + 1;
//        if     (n == 0)currentPlayer = 1;
//        else if(n == 1)currentPlayer = 2;
//        else throw new IllegalArgumentException("Wrong value of player: " + n);

        showPlayerTurn();
    }
    private void showTurnLabel(){
        turnDialog.show(stage);
//        turnLabel.setVisible(true);
    }
    private void hideTurnLabel(){
        turnDialog.hide();
//        turnLabel.setVisible(false);
    }
    private void clearTargetSelected(){
        for (Image i:targetSelected){
            i.remove();
        }
    }

    private void showPlayerTurn(){
        initDialog();
        showTurnLabel();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                hideTurnLabel();
                allowStartTurn = true;
                onTouch();
            }
        }, 1000);
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

    private void endTime(){
        allowStartTurn = false;
        hideTooltips();
        offTouch();
        if(round != 1){
            fight();

            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(projectiles.size == 0){
                        cancel();
                        nextPlayer();
                    }
                }
            }, 1000, 1000);
        }else nextPlayer();
    }
    private void nextPlayer(){
        if(numberChange >= 1) {
            numberChange = 0;
            income();
            startTurn();
        }else changePlayerTurn();
        timeBar.setValue(turnTime);
    }
//    private class endBulletsListener extends Timer{
//
//
//
//    }

    private final long shootDelay = 700;



    private void fight(){
        for (Slot<Tower.TowerPrototype, Tower> s:fields[getCurrentPlayer() - 1].getTowers()){
            if(/*s.getSelected() != -1*/!s.isNoSelected()) {
//                System.out.println(Arrays.toString(s.getSelected()));
//                for (int i = 0; i < s.getSelected().length; i++) {
//                    shootProjectile(s, 0);
                s.getObject().setCanShoot(true);
            }
        }

        clearTargetSelected();
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

    public Entity searchTarget(Slot<Tower.TowerPrototype, Tower> attacker, int line){
        for (int i = 0; i < 6; i ++){
            WallSlot curr = fields[getOppositePlayer() - 1].getWalls().get(i);
            if(curr.getObject() != null && curr.getLine() == line && curr.getRow() == 1){
                return curr;
            }
        }
        for (int i = 0; i < 6; i ++){
            WallSlot curr = fields[getOppositePlayer() - 1].getWalls().get(i);
            if(curr.getObject() != null && curr.getLine() == line && curr.getRow() == 2){
                return curr;
            }
        }
        for (int i = 0; i < 3; i ++){
            TowerSlot curr = fields[getOppositePlayer() - 1].getTowers()[i];
            if(curr.getObject() != null && curr.getLine() == line){
                return curr;
            }
        }
        return players[getOppositePlayer() - 1];
    }

    private void income(){
        for (int i = 0; i < 2; i++) {
            players[i].addShards(5);
        }
    }

    private void changePlayerTurn(){
        currentPlayer = 3 - currentPlayer;
//        if(currentPlayer == p1) currentPlayer = p2;
//        else currentPlayer = p1;
        numberChange++;
        showPlayerTurn();
    }

    private Field generateField(boolean player){
        Field field = new Field();
//        Table field = new Table();
        field.center();
        field.defaults().space(20, 50, 10, 50).size(48);

        Array<WallSlot> wallSlots = new Array<WallSlot>();

        if(player) {
            wallSlots.addAll(addWallSlots(field, true, players[0], 1));
            field.row();
            wallSlots.addAll(addWallSlots(field, true, players[0], 2));
            field.row();
            addTowerSlots(field, true, players[0]);
        }else {
            addTowerSlots(field, false, players[1]);
            field.row();
            wallSlots.addAll(addWallSlots(field, false, players[1], 2));
            field.row();
            wallSlots.addAll(addWallSlots(field, false, players[1], 1));
        }
        field.setWalls(wallSlots);
        field.pack();
        return field;
    }
    private void initTooltips(Table t){
        for (Cell c:t.getCells()){
            if(c.getActor() instanceof Slot) ((Slot) c.getActor()).initTooltip();
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
            towerSlots[i] = new TowerSlot(player, this, user, i, 3);
            t.add(towerSlots[i]);
        }
        t.setTowers(towerSlots);
    }
    private void initPlayerField(){
        fields[0] = generateField(true);
        fields[0].setPosition(Gdx.graphics.getWidth()/2f - fields[0].getWidth()/2, CORNER_SPACE);

        stage.addActor(fields[0]);
        initTooltips(fields[0]);
    }
    private void initEnemyFiled(){
        fields[1] = generateField(false);
        fields[1].setPosition(Gdx.graphics.getWidth()/2f - fields[1].getWidth()/2,
                Gdx.graphics.getHeight() - fields[1].getHeight() - CORNER_SPACE);

        stage.addActor(fields[1]);
        initTooltips(fields[1]);
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
        players[0] = new Player();
        players[0].setPosition(Gdx.graphics.getWidth()/2f, 0);
        stage.addActor(players[0]);
        players[1] = new Player();
        players[1].setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight() - players[1].getHeight());
        stage.addActor(players[1]);
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
