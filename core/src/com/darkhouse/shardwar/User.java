package com.darkhouse.shardwar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;

public class User {
    private Texture avatar;
    private int avatarID;
    private Texture levelTexture;
    private String name;
    private int rankedPoints;

    public Texture getAvatar() {
        return avatar;
    }

    public int getAvatarID() {
        return avatarID;
    }

    public String getName() {
        return name;
    }

    public static int POINTS[] = {300, 800, 1500, 2500, 3500, 4500};
    public static String AVATARS[] = {"player1Logo.png", "player2Logo.png", "player3Logo.png"};

    public User setAvatar(Texture avatar) {
        this.avatar = avatar;
        return this;
    }
    public User setName(String name) {
        this.name = name;
        return this;
    }

    private User(/*Texture avatar, */int avatarID, String name) {
//        this.avatar = avatar;
        this.avatarID = avatarID;
        this.avatar = ShardWar.main.getAssetLoader().get(AVATARS[avatarID], Texture.class);
        this.name = name;
    }

    public static User registerNewUser(/*Texture avatar, */int avatarID, String name){
        return new User(/*avatar,*/ avatarID, name);
    }
    public static User loadUserFromCache(String name, int avatarID, int rankedPoints){
        User user = new User(avatarID, name);
        user.pointsChange(rankedPoints);
        return user;
    }
//    public static User loadUserFromServer(String loadCode){
//
//    }
    public int getRankedLevel(){
        for (int i = 0; i < POINTS.length; i++) {
            if(rankedPoints < POINTS[i]) return i + 1;
        }
        return POINTS.length - 1;
    }
    public int getRankedPoints(){
        return rankedPoints;
    }

    public void pointsChange(int change){
        if(rankedPoints + change < 0) rankedPoints = 0;
        else rankedPoints += change;
        updatePoints();
    }
    public void updatePoints(){

    }
    public void updatePoints(int enemyLevel, boolean win){
        if(enemyLevel == getRankedLevel()) pointsChange(win ? 100:-100);
        if(enemyLevel - getRankedLevel() == 1) pointsChange(win ? 125:-75);
        if(enemyLevel - getRankedLevel() >= 2) pointsChange(win ? 150:-50);
        if(enemyLevel - getRankedLevel() == -1) pointsChange(win ? 75:-125);
        if(enemyLevel - getRankedLevel() >= -2) pointsChange(win ? 50:-150);
    }

    @Override
    public String toString() {
        return name + " " + avatar;
    }

//    private String getCacheCode(){
//        return name + "/" +
//    }

    public void saveCache(){
        Preferences pref = Gdx.app.getPreferences("usercache");
        pref.putString("username", getName());
        pref.putInteger("userpic", avatarID);
        pref.putInteger("userpoints", getRankedPoints());
        pref.flush();
    }
}
