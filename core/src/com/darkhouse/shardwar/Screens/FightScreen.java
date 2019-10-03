package com.darkhouse.shardwar.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
import com.darkhouse.shardwar.Model.BackButton;
import com.darkhouse.shardwar.Model.PlayerSearchTimer;
import com.darkhouse.shardwar.Model.ShardPanel;
import com.darkhouse.shardwar.Model.UserPanel;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
//import io.socket.client.IO;
import com.darkhouse.shardwar.User;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.*;

public class FightScreen extends AbstractScreen {

    private static final int CORNER_SPACE = 10;
    private static final int INCOME = 4;
    public static final int ROLL_ROUND = 3;
    public static final int PREROLL_ROUNDS = 5;
    private static float[] VERTICAL_MOVE_HEIGHTS /*= {200, 260, 320}*/;
    private static int CELL_SIZE;
    private static final int MINIMUM_DRAG_DISTANCE = 150;
    public static final int MINIMUM_VALUE = 3;
    public static float TURN_TIME = 45f;

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
//    private static final int MINIMUM_VALUE = 3;
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

    private void flushTargetSlot(){
        this.targetSlot = null;
        onTouch();
    }

    public void setTargetSlot(Slot<Tower.TowerPrototype, Tower> targetSlot) {
        this.targetSlot = targetSlot;
        offTouch();
        if(targetSlot.getNumberSelected() < targetSlot.getSelected().length) {
            targetSlot.setTargeter(new Image(ShardWar.main.getAssetLoader().getCenterTarget(getCurrentPlayer())));
//            targetSlot.getTargeter().debug();
            targetSlot.select(targetSlot.getColumn());
            if(targetSlot.getObject().isGlobal()) targetSlot.selectRow(0);

            targetSlot.getTargeter().setHeight(VERTICAL_MOVE_HEIGHTS[0]);
            if (currentPlayer == 1) {
                targetSlot.getTargeter().setDrawable(ShardWar.main.getAssetLoader().getCenterTarget(1));
                targetSlot.getTargeter().setPosition(targetSlot.getX() + targetSlot.getParent().getX(),
                        targetSlot.getY() + targetSlot.getParent().getY() + CELL_SIZE + 4);
            }
            if (currentPlayer == 2) {
                targetSlot.getTargeter().setDrawable(ShardWar.main.getAssetLoader().getCenterTarget(2));
                targetSlot.getTargeter().setPosition(targetSlot.getX() + targetSlot.getParent().getX(),
                        targetSlot.getY() + targetSlot.getParent().getY() - targetSlot.getTargeter().getHeight() - 4);
            }
            targetSlot.getTargeter().setWidth(CELL_SIZE);
            targetSlot.getTargeter().setTouchable(Touchable.disabled);
            stage.addActor(targetSlot.getTargeter());
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

    private int gameMode;

    public int getGameMode() {
        return gameMode;
    }

    public FightScreen() {
        super(ShardWar.main.getAssetLoader().getMainMenuBg());
        gameMode = 0;
//        if(!configured) {
//            initSocket();
//            configured = true;
//        }
        connect();
        players = new Player[2];
//        timerToStart();
    }



    public FightScreen(User first) {
        super(ShardWar.main.getAssetLoader().getMainMenuBg());
        gameMode = 1;
        players = new Player[2];
        MainMenu.RegisterDialog dialog = new MainMenu.RegisterDialog(){
            @Override
            protected void setUser() {
                initOwnPlayer(first);
                initEnemy(getUser());
                started = true;
            }
        };
        dialog.show(stage);
    }

    private static Socket socket;

    public static void initSocket(){
        try {
            socket = IO.socket("http://localhost:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void connect(){
        try {
            socket.off();
            configSocketsEvents();
            /*if(!socket.connected()) */socket.connect();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void configSocketsEvents(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
//                JSONObject data = (JSONObject) args[0];
                JSONObject data = new JSONObject();
                try {
                    data.put("name", ShardWar.user.getName());
                    data.put("avatarID", ShardWar.user.getAvatarID());
                    data.put("rankedPoints", ShardWar.user.getRankedPoints());
                    socket.emit("initUser", data);
//                    String name = data.getString("name");
//                    int avatarID = data.getInt("avatarID");
//                    int rankedPoints = data.getInt("rankedPoints");
//                    initUser(User.loadUserFromCache(name, avatarID, rankedPoints));
                    initOwnPlayer(User.loadUserFromCache(ShardWar.user.getName(),
                                                    ShardWar.user.getAvatarID(),
                                                    ShardWar.user.getRankedPoints()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    System.out.println("My id " + id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String name = data.getString("name");
                    int avatarID = data.getInt("avatarID");
                    int rankedPoints = data.getInt("rankedPoints");
                    initEnemy(User.loadUserFromCache(name, avatarID, rankedPoints));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("playerDisconnected", args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                String id = data.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).on("gameStart", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
//                init();
//                startTurn();
                hideSearch();
                started = true;
            }
        }).on("startVote", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String name = data.getString("name");
                    int avatarID = data.getInt("avatarID");
                    int rankedPoints = data.getInt("rankedPoints");
                    enemyConnected(User.loadUserFromCache(name, avatarID, rankedPoints));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("enemyDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                enemyDisconnected();
            }
        }).on("enemyVoted", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                int a = (int) args[0];
                paintVoteTable(false, a == 1);
            }
        }).on("timeEnd", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                endTime();
            }
        }).on("timeChange", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    double currTime = data.getDouble("time");
                    timeBar.setValue((float) currTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("currentPlayer", args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                currentPlayer = data.getInt("player");
                startTurnPart2(-1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendEndTime(){
        socket.emit("endTimeButton");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        t = new Timer();//another place to init
        if(gameMode == 0)initSearch();
//        if(gameMode == 1);
    }


    private PlayerSearchTimer timer;
    private Table enemy;
    private Table player;
    private UserPanel p1;
    private UserPanel p2;
    private Table time;
    private Dialog readyDialog;

    private void hideSearch(){
        initEnemy(p2.getUser());
        p1.remove();
        p2.remove();
        timer.remove();
        player.remove();
        enemy.remove();
        time.remove();
    }


    private void initSearch(){


        readyDialog = new Dialog("", ShardWar.main.getAssetLoader().getSkin()){
            {
                setMovable(false);
                text("Confirm enemy?");
                button("YES", true);
                button("NO", false);
            }

            @Override
            protected void result(Object object) {
//                if(object.equals(true)){
//                    socket.emit("VoteResult", true);
//                }else {
//                    socket.emit("VoteResult", false);
//                }
                boolean res = (boolean) object;
                socket.emit("voteResult", res ? 1 : 0);
                paintVoteTable(true, res);
                hide();
                if(!res) {
                    ShardWar.main.switchScreen(0);
//                    socket.disconnect();
                }
            }
        };

        p1 = new UserPanel();
        p1.init(ShardWar.user, 100);

        player = new Table();
        player.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2f);
        player.setPosition(0, 0);
        stage.addActor(player);

        enemy = new Table();
        enemy.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2f);
        enemy.setPosition(0, Gdx.graphics.getHeight()/2f);
        stage.addActor(enemy);

        player.add(p1).center();

        time = new Table();
        timer = new PlayerSearchTimer();
        time.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        time.add(timer).center();

        initTableVoteHighlight();

        BackButton b = new BackButton(true);
        stage.addActor(b);

        stage.addActor(time);
    }

    private TextureRegionDrawable greenHighlight;
    private TextureRegionDrawable redHighlight;
    private TextureRegionDrawable emptyHighlight;

    private void initTableVoteHighlight(){
        Pixmap p1 = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        p1.setColor(0x299000ff);
        p1.fillRectangle(0, 0, 10, 10);
        greenHighlight = new TextureRegionDrawable(new TextureRegion(new Texture(p1)));
        p1.dispose();
        Pixmap p2 = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        p2.setColor(0xff0000ff);
        p2.fillRectangle(0, 0, 10, 10);
        redHighlight = new TextureRegionDrawable(new TextureRegion(new Texture(p2)));
        p2.dispose();
        Pixmap p3 = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        p3.setColor(0x00000000);
        p3.fillRectangle(0, 0, 10, 10);
        emptyHighlight = new TextureRegionDrawable(new TextureRegion(new Texture(p3)));
        p3.dispose();
    }

    private void enemyConnected(User user){
//        System.out.println(timer);
//        System.out.println(user);
        timer.stop();
        p2 = new UserPanel();
        p2.init(user, 100);
        enemy.add(p2).center();
//        System.out.println(stage);

        readyDialog.show(stage);
    }
    private void enemyDisconnected(){
        readyDialog.hide();
//        p2.remove();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                enemy.setBackground(emptyHighlight);
                player.setBackground(emptyHighlight);
                enemy.removeActor(p2);
                timer.go();
            }
        }, 300);
    }

    private void paintVoteTable(boolean owner, boolean ready){
        if(owner) player.setBackground(ready ? greenHighlight : redHighlight);
        else      enemy.setBackground(ready ? greenHighlight : redHighlight);
    }

    private void initInputs(){

        HideListener hideListener = new HideListener();

        InputMultiplexer p = new InputMultiplexer();
//        AttackPathListener attackPathListener = new AttackPathListener();
//        AndroidAttackPathListener androidAttackPathListener = new AndroidAttackPathListener();
//        Gdx.input.setInputProcessor(new InputMultiplexer(attackPathListener, stage, hideListener));
//        Gdx.input.setInputProcessor(new InputMultiplexer(new GestureDetector()),
//                stage, hideListener));
        p.addProcessor(stage);
        if(Gdx.app.getType() == Application.ApplicationType.Android){
//            p.addProcessor(new AttackPathListener());
            p.addProcessor(new GestureDetector(new AndroidAttackPathListener()));
        }else {
            p.addProcessor(new AttackPathListener());
//            p.addProcessor(new GestureDetector(new AndroidAttackPathListener()));
        }

        p.addProcessor(hideListener);
        Gdx.input.setInputProcessor(p);

//        startTurn();
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

    @Override
    public void hide() {
        super.hide();
        socket.disconnect();
//        socket.close();
    }

    public class HideListener extends InputAdapter {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            hideTooltips();
            return true;
        }
    }
    public class AndroidAttackPathListener extends GestureDetector.GestureAdapter{
        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if(targetSlot != null) {
                int width = CELL_SIZE;
                float x = targetSlot.getX() + targetSlot.getParent().getX();
                Drawable texture = null;
                int diff = targetSlot.getCurrentSelected() - targetSlot.getColumn();
                if (velocityX > MINIMUM_DRAG_DISTANCE) {
                    if(diff == 0) {
                        if (targetSlot.getColumn() != 2) {
                            texture = ShardWar.main.getAssetLoader().getRightTarget(currentPlayer);
                            width = CELL_SIZE*3;
                            targetSlot.select(targetSlot.getColumn() + 1);
                        }
                    }else if(diff == -1){
                        texture = ShardWar.main.getAssetLoader().getCenterTarget(currentPlayer);
                        targetSlot.select(targetSlot.getColumn());
                    }else if(diff == 1 && targetSlot.getObject().isGlobal() && targetSlot.getColumn() == 0){
                        texture = ShardWar.main.getAssetLoader().getRightTarget2(currentPlayer);
                        width = CELL_SIZE*5;
                        targetSlot.select(targetSlot.getColumn() + 2);
                    }else if(diff == -2 && targetSlot.getObject().isGlobal()){
                        texture = ShardWar.main.getAssetLoader().getLeftTarget(currentPlayer);
                        width = CELL_SIZE*3;
                        x = x - CELL_SIZE*2;
                        targetSlot.select(targetSlot.getColumn() - 1);
                    }
                } else if (velocityX < -MINIMUM_DRAG_DISTANCE){
                    if(diff == 0){
                        if (targetSlot.getColumn() != 0) {
                            texture = ShardWar.main.getAssetLoader().getLeftTarget(currentPlayer);
                            width = CELL_SIZE*3;
                            x = x - CELL_SIZE*2;
                            targetSlot.select(targetSlot.getColumn() - 1);
                        }
                    }else if(diff == 1){
                        texture = ShardWar.main.getAssetLoader().getCenterTarget(currentPlayer);
                        targetSlot.select(targetSlot.getColumn());
                    }else if(diff == -1 && targetSlot.getObject().isGlobal() && targetSlot.getColumn() == 2){
                        texture = ShardWar.main.getAssetLoader().getLeftTarget2(currentPlayer);
                        width = CELL_SIZE*5;
                        x = x - CELL_SIZE*4;
                        targetSlot.select(targetSlot.getColumn() - 2);
                    }else if(diff == 2 && targetSlot.getObject().isGlobal()){
                        texture = ShardWar.main.getAssetLoader().getRightTarget(currentPlayer);
                        width = CELL_SIZE*3;
                        targetSlot.select(targetSlot.getColumn() + 1);
                    }
                }
                verticalMove(velocityY);
                if(texture == null) return false;
                targetSlot.getTargeter().setWidth(width);
                targetSlot.getTargeter().setX(x);
                targetSlot.getTargeter().setDrawable(texture);

//                endSelect(button);
            }
            return false;
        }

//        @Override
//        public boolean touchDown(float x, float y, int pointer, int button) {
//            return endSelect(button);
//        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            return endSelect(button);
        }

        private void verticalMove(float yDiff) {
            if(targetSlot.getObject().isGlobal()){
//                int diff = targetSlot.getCurrentSelectedRow() - targetSlot.getRow();
                if(     yDiff < -MINIMUM_DRAG_DISTANCE && currentPlayer == 1 ||
                        yDiff > MINIMUM_DRAG_DISTANCE && currentPlayer == 2){//swipe up
                    if(targetSlot.getCurrentSelectedRow() != 2){
                        if(targetSlot.getCurrentSelectedRow() == 0){
                            targetSlot.getTargeter().setHeight(VERTICAL_MOVE_HEIGHTS[1]);
                            targetSlot.selectRow(1);
                        }else if(targetSlot.getCurrentSelectedRow() == 1){
                            targetSlot.getTargeter().setHeight(VERTICAL_MOVE_HEIGHTS[2]);
                            targetSlot.selectRow(2);
                        }

                        if (currentPlayer == 2) {
                            targetSlot.getTargeter().setY(targetSlot.getY() + targetSlot.getParent().getY() -
                                targetSlot.getTargeter().getHeight() - 4);
                        }
                    }
                }else if (  yDiff > MINIMUM_DRAG_DISTANCE && currentPlayer == 1 ||
                            yDiff < -MINIMUM_DRAG_DISTANCE && currentPlayer == 2){//swipe down
                    if(targetSlot.getCurrentSelectedRow() != 0){
                        if(targetSlot.getCurrentSelectedRow() == 2){
                            targetSlot.getTargeter().setHeight(VERTICAL_MOVE_HEIGHTS[1]);
                            targetSlot.selectRow(1);
                        }else if(targetSlot.getCurrentSelectedRow() == 1){
                            targetSlot.getTargeter().setHeight(VERTICAL_MOVE_HEIGHTS[0]);
                            targetSlot.selectRow(0);
                        }

                        if (currentPlayer == 2) {
                            targetSlot.getTargeter().setY(targetSlot.getY() + targetSlot.getParent().getY() -
                                    targetSlot.getTargeter().getHeight() - 4);
                        }

                    }

                }
            }
        }
    }

    public class AttackPathListener extends InputAdapter{
        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            if(targetSlot != null){
                int width = CELL_SIZE;
                float x = targetSlot.getX() + targetSlot.getParent().getX();
                Drawable texture = null;
                if (screenX > targetSlot.getX() + targetSlot.getParent().getX() + targetSlot.getWidth()) {
                    if (targetSlot.getColumn() != 2) {
//                        targetSlot.getTargeter().setDrawable(ShardWar.main.getAssetLoader().getRightTarget(currentPlayer));
//                        texture = ShardWar.main.getAssetLoader().getRightTarget(currentPlayer);
                         if(targetSlot.getObject().isGlobal() && targetSlot.getColumn() == 0 && screenX > getCurrentField().getXofColumn(1)){
                             texture = ShardWar.main.getAssetLoader().getRightTarget2(currentPlayer);
                             width = CELL_SIZE*5;
//                             x = x - 30;
                             targetSlot.select(targetSlot.getColumn() + 2);
                         }else {
                             texture = ShardWar.main.getAssetLoader().getRightTarget(currentPlayer);
                             width = CELL_SIZE*3;
                             targetSlot.select(targetSlot.getColumn() + 1);
                         }
                    }
                } else if (screenX < targetSlot.getX() + targetSlot.getParent().getX()) {
                    if (targetSlot.getColumn() != 0) {
                        if(targetSlot.getObject().isGlobal() && targetSlot.getColumn() == 2 && screenX < getCurrentField().getXofColumn(0)){
                            texture = ShardWar.main.getAssetLoader().getLeftTarget2(currentPlayer);
                            width = CELL_SIZE*5;
                            x = x - CELL_SIZE*4;
//                            x = x - 30;
                            targetSlot.select(targetSlot.getColumn() - 2);
                        }else {
                            texture = ShardWar.main.getAssetLoader().getLeftTarget(currentPlayer);
                            width = CELL_SIZE*3;
                            x = x - CELL_SIZE*2;
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
                verticalMove(screenY);
//                }
            }
            return super.mouseMoved(screenX, screenY);
        }

        private void verticalMove(int screenY) {
            if(targetSlot.getObject().isGlobal()){
                if(currentPlayer == 1) {
                    if (screenY > getCurrentField().getYOfRow(1, true)) {
                        targetSlot.getTargeter().setHeight(VERTICAL_MOVE_HEIGHTS[0]);
                        targetSlot.selectRow(0);
                    } else if (screenY > getCurrentField().getYOfRow(2, true)) {
                        targetSlot.getTargeter().setHeight(VERTICAL_MOVE_HEIGHTS[1]);
                        targetSlot.selectRow(1);
                    } else {
                        targetSlot.getTargeter().setHeight(VERTICAL_MOVE_HEIGHTS[2]);
                        targetSlot.selectRow(2);
                    }
                }else {
                    if (screenY < getCurrentField().getYOfRow(1, false)) {
                        targetSlot.getTargeter().setHeight(VERTICAL_MOVE_HEIGHTS[0]);
                        targetSlot.selectRow(0);
                    } else if (screenY < getCurrentField().getYOfRow(2, false)) {
                        targetSlot.getTargeter().setHeight(VERTICAL_MOVE_HEIGHTS[1]);
                        targetSlot.selectRow(1);
                    } else {
                        targetSlot.getTargeter().setHeight(VERTICAL_MOVE_HEIGHTS[2]);
                        targetSlot.selectRow(2);
                    }
                    targetSlot.getTargeter().setY(targetSlot.getY() + targetSlot.getParent().getY() -
                            targetSlot.getTargeter().getHeight() - 4);
                }
            }else targetSlot.getTargeter().setHeight(VERTICAL_MOVE_HEIGHTS[0]);
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return (endSelect(button));
        }
    }
    private boolean endSelect(int button){
        if(button == 1 && targetSlot != null){
            for (int i = 0; i < targetSlot.getAllTargeters().length; i++) {
                if(targetSlot.getAllTargeters()[i] != null) {
                    targetSlot.getAllTargeters()[i].remove();
                    targetSelected.removeValue(targetSlot.getAllTargeters()[i], true);
                }
            }
            targetSlot.flushSelect();
            targetSlot.flushTargeter();
            flushTargetSlot();
//            ShardWar.fightScreen.getCurrentField().setTouchable(Touchable.enabled);
//            ShardWar.fightScreen.getField(1).setTouchable(Touchable.enabled);
//            ShardWar.fightScreen.getField(2).setTouchable(Touchable.enabled);
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
            flushTargetSlot();
            ShardWar.fightScreen.getCurrentField().setTouchable(Touchable.enabled);
            return true;
        }
        return false;
    }


    public void init(){
        targetSelected = new Array<>();

        fields = new Field[2];
        projectiles = new Array<>();

        buyedInit = new int[2];
//        a = new AlphaAction();

        initSizes();
//        initUsers();
        initPlayerField();
        initEnemyField();
        initVerticalMoveSize();
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

    private void initSizes(){
        CELL_SIZE = Gdx.graphics.getHeight()/12;

    }
    private void initVerticalMoveSize(){//after initFields
        VERTICAL_MOVE_HEIGHTS = new float[3];
//        VERTICAL_MOVE_HEIGHTS[0] = Gdx.graphics.getHeight()/2.6f;
//        System.out.println(fields[1].getY() + " " + fields[0].getTowers()[0].getY());
        VERTICAL_MOVE_HEIGHTS[0] = Math.abs(fields[1].getY() -
                (fields[0].getTowers()[0].getY() + fields[0].getTowers()[0].getHeight())) - 10;
        VERTICAL_MOVE_HEIGHTS[1] = VERTICAL_MOVE_HEIGHTS[0] + CELL_SIZE + 20;
        VERTICAL_MOVE_HEIGHTS[2] = VERTICAL_MOVE_HEIGHTS[1] + CELL_SIZE + 20;
//        System.out.println(Arrays.toString(VERTICAL_MOVE_HEIGHTS));
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

    private TextButton timeSkip;

    private void initTimeSkipButton(){
        AssetLoader l = ShardWar.main.getAssetLoader();
        timeSkip = new TextButton(l.getWord("endTurn"), l.getSkin());
        timeSkip.setPosition(Gdx.graphics.getWidth() - timeSkip.getWidth() - 10,
                Gdx.graphics.getHeight()/2f - timeSkip.getHeight() - 10);
//        timeSkip.pack();
        timeSkip.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(gameMode == 0) {
                    if(currentPlayer == 1) sendEndTime();
                }
                if(gameMode == 1) endTime();

                if(gameMode == 2){
                    if(currentPlayer == 1) endTime();
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        stage.addActor(timeSkip);
    }

    private int[] buyedInit;

    private class TurnBuyButton extends Table{
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
            num = MINIMUM_VALUE;
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
                    if(getGameMode() == 1 || getCurrentPlayer() == 1) {
                        if (num > MINIMUM_VALUE) {
                            num--;
                            updateNum();
                        }
                    }
                    return true;
                }
            });
            more.addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if(getGameMode() == 1 || getCurrentPlayer() == 1) {
                        if (getCurrentPlayerObject().getShards() > num) {
                            num++;
                            updateNum();
                        }
                    }
                    return true;
                }
            });
            buyTurn.addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if(getGameMode() == 1 || getCurrentPlayer() == 1) {
                        if (getCurrentPlayerObject().deleteShards(num)) {
                            buyedInit[currentPlayer - 1] = num;
//                        AlphaAction a = new AlphaAction();
                            a.setAlpha(0.4f);
                            a.restart();
                            addAction(a);
                            setTouchable(Touchable.disabled);
                        }
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
            num = MINIMUM_VALUE;
            updateNum();
//            AlphaAction a = new AlphaAction();
//            a.reset();
            a.setAlpha(1f);
            a.restart();
            addAction(a);
            setTouchable(Touchable.enabled);
        }
    }



    private void turnBuyButtonUpdate(){
        if(gameMode == 0){
            if(currentPlayer == 1) turnBuyButton.setTouchable(Touchable.enabled);
            else                   turnBuyButton.setTouchable(Touchable.disabled);
        }
    }
    private void timeSkipButtonUpdate(){
        if(gameMode == 0){
            if(currentPlayer == 1) timeSkip.setTouchable(Touchable.enabled);
            else                   timeSkip.setTouchable(Touchable.disabled);
        }
    }


    private boolean started;

    @Override
    public void render(float delta) {
        super.render(delta);
        if(started) {
            initInputs();
            init();
            startTurn();
            started = false;
        }
    }

    private void initBuyTurnInitiative(){
        turnBuyButton = new TurnBuyButton();
        turnBuyButton.setPosition(Gdx.graphics.getWidth() - turnBuyButton.getWidth() - 10,
                Gdx.graphics.getHeight()/2f /*+ turnBuyButton.getHeight()*/ + 10);
        stage.addActor(turnBuyButton);
    }



    private void initTimeBar(){
        int height = 10;
        timeBar = new ProgressBar(0, TURN_TIME, 0.01f, false,
                ShardWar.main.getAssetLoader().getTimeBarStyle(height)){
            @Override
            public void act(float delta) {
                if(allowStartTurn) {
//                    if(gameMode == 0) setValue();
                    if(gameMode == 0) setValue(getValue() - delta);
                    if(gameMode == 1) setValue(getValue() - delta);
                    if (getValue() == 0) {
                        if(gameMode == 0) return;
                        if(gameMode == 1) endTime();
//                        setValue(TURN_TIME);
                    }

                }
            }

//            @Override
//            public float getPrefHeight() {
//                return 20;
//            }
        };
        timeBar.setPosition(0, Gdx.graphics.getHeight()/2f - height/2f);
        timeBar.setValue(TURN_TIME);
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
//    private boolean waitForServer = false;

    private void startTurn(){
        offTouch();

        int buyedTurn = -1;
        if(gameMode == 1) {
            if(buyedInit[0] != buyedInit[1]){
                if(buyedInit[0] > buyedInit[1]) currentPlayer = 1;
                else currentPlayer = 2;
                buyedTurn = currentPlayer;
            }else {
                Random r = new Random();
                int n = r.nextInt(2);
                currentPlayer = n + 1;
            }
            startTurnPart2(buyedTurn);
        }else {
            askForCurrentPlayer();
        }

    }

    private void startTurnPart2(int buyedTurn){
        round++;

//        updateCurrentRoundPanel();
        if(gameMode == 1) {
            buyedInit[0] = 0;
            buyedInit[1] = 0;
        }

        if(round % ROLL_ROUND == 0 && numberChange == 0){
            rollSpells();
        }
        showPlayerTurn(buyedTurn);
    }

    private void askForCurrentPlayer(){
        socket.emit("askCurrentPlayer");
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
        turnBuyButtonUpdate();
        timeSkipButtonUpdate();

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
        int w, h;
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            w = 380;
            h = 100;
        }else {
            w = 250;
            h = 70;
        }
        spellPanel1.setSize(w, h);
        spellPanel2.setSize(w, h);
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
    public static float CHANCES[][] = new float[][]{
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
    public static HashMap<Integer, ArrayList<Spell.SpellPrototype>> allSpells = new HashMap<>();

    private Spell.SpellPrototype getSpell(int i){
        return spellPool.get(i%spellPool.size());
    }

    public static void initAllSpells(){
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
    }

    private void initSpellDialog(){
        spellBuy = new SpellBuy();
        spellBuy.setSize(200, 200);
        spellBuy.init(stage);

        initSpellsInNextRounds(PREROLL_ROUNDS);
    }
    private void initSpellsInNextRounds(int num){
        Random r = new Random();
        for (int t = 0; t < num; t++) {
            int m = t + round/ROLL_ROUND;
            if(spellPool.size()/3 >= PREROLL_ROUNDS) m += PREROLL_ROUNDS - 1;
            if(m >= CHANCES.length) m = CHANCES.length - 1;
            for (int i = 0; i < 3; i++) {
                float f = r.nextFloat();
                int currTier;
                if(f < CHANCES[m][0]){
                    currTier = 1;
                }else if (f < CHANCES[m][1] + CHANCES[m][0]){
                    currTier = 2;
                }else if(f < CHANCES[m][2] + CHANCES[m][1] + CHANCES[m][0]){
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
        fightOrder = 0;

        if(round % ROLL_ROUND == 0 && numberChange == 1){
            initSpellsInNextRounds(1);
            spellRollPanel.nextRoll();
        }

        if(round != 1){
            if(!anyTowerAttacks()){
                afterTurn();
                return;
            }
            if(anyGlobalTowerAttacks()) {
                fightGlobal();
                fightOrder++;
            }else {
                fightNonGlobal();
                fightOrder += 2;
            }
//            shot = 0;


//            Timer t = new Timer();

            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(projectiles.size == 0){
                        if(fightOrder == 1){
                            fightNonGlobal();
                            fightOrder++;
                        }else {//2
                            afterTurn();
                            cancel();
                        }
                    }
                }
            }, 500, 1000);
        }else nextPlayer();
    }
    private void afterTurn(){
        actEffects();
        nextPlayer();
        fightOrder = 0;
    }

    private void hideSpellTargeters(){
        spellPanel1.clearListeners();
        spellPanel2.clearListeners();
    }

    private void nextPlayer(){
        if (gameMode == 1 || gameMode == 0) {
            if (numberChange >= 1) {
                numberChange = 0;
                income();
                startTurn();
            } else changePlayerTurn();
        }else {
//            if (numberChange >= 1) {
//                numberChange = 0;
//                startTurn();
//            } else changePlayerTurn();
//            income();
//            startTurn();
        }
        timeBar.setValue(TURN_TIME);
    }

//    private int shot = 0;

    private int fightOrder;

    private boolean anyGlobalTowerAttacks(){
        Field f = getCurrentField();
        for (TowerSlot s:f.getTowers()){
            if(s.getObject() != null && s.getObject().isGlobal() && !s.isNoSelected()) return true;
        }
        return false;
    }
    private boolean anyTowerAttacks(){
        Field f = getCurrentField();
        for (TowerSlot s:f.getTowers()){
            if(s.getObject() != null && !s.isNoSelected()) return true;
        }
        return false;
    }

    private void fightGlobal(){
        for (final Slot<Tower.TowerPrototype, Tower> s:fields[getCurrentPlayer() - 1].getTowers()){
            if(/*s.getSelected() != -1*/!s.empty() && !s.isNoSelected()) {
//                System.out.println(Arrays.toString(s.getSelected()));
//                for (int i = 0; i < s.getSelected().length; i++) {
//                    shootProjectile(s, 0);

                if(s.getObject().isGlobal()) s.getObject().setCanShoot(true);
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
    private void fightNonGlobal(){
        for (final Slot<Tower.TowerPrototype, Tower> s:fields[getCurrentPlayer() - 1].getTowers()) {
            if (!s.empty() && !s.isNoSelected()) {
                if (!s.getObject().isGlobal()) s.getObject().setCanShoot(true);
            }
        }
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
        if(gameMode == 1 || gameMode == 0) {
            currentPlayer = 3 - currentPlayer;
//        if(currentPlayer == p1) currentPlayer = p2;
//        else currentPlayer = p1;
            numberChange++;
            changePlayerTurnPart2();
        }else {
//            numberChange++;
//            askForCurrentPlayer();
        }
    }
    private void changePlayerTurnPart2(){
        showPlayerTurn(-1);
    }

    private Field generateField(boolean player){
        Field field = new Field();
//        Table field = new Table();
        field.center();

//        if(Gdx.app.getType() == Application.ApplicationType.Android){
//            cellSize = 64;
//        }else cellSize = 48;

        field.defaults().space(20, 50, 10, 50).size(CELL_SIZE)/*.expand().fill()*/;

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
//        field.debug();
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
        if(Gdx.app.getType() == Application.ApplicationType.Android){

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
            int w, h;
//            if(Gdx.app.getType() == Application.ApplicationType.Android){
//                w = 48;
//                h = 11;
//            }else {
//                w = 32;
//                h = 7;
//            }
//            wallSlots.peek().setSize(w, h);
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
    private void initEnemyField(){
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

    private void initOwnPlayer(User user){
        players[0] = new Player(new Player.PlayerPrototype(user));
        players[0].initShards();
//        System.out.println("my");
//        System.out.println(user.getName());
//        System.out.println(user.getAvatarID());
//        System.out.println(user.getRankedPoints());
    }

    public void initEnemy(User user){
        players[1] = new Player(new Player.PlayerPrototype(user));
        players[1].initShards();
        initUsers();
//        System.out.println("enemy");
//        System.out.println(user.getName());
//        System.out.println(user.getAvatarID());
//        System.out.println(user.getRankedPoints());
    }


    private void initUsers(){
//        players[0] = new Player(new Player.PlayerPrototype(
//                ShardWar.main.getAssetLoader().get("player1Logo.png", Texture.class),
//                "Джавист"));
//        players[0].initShards();
//        players[1] = new Player(new Player.PlayerPrototype(
//                ShardWar.main.getAssetLoader().get("player2Logo.png", Texture.class),
//                "Даныло"));
//        players[1].initShards();
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
