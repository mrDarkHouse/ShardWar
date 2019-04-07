package com.darkhouse.shardwar.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.ShardWar;

public class StartingLoadScreen extends AbstractLoadingScreen{

    public StartingLoadScreen() {
        super();
        ShardWar.main.getAssetLoader().load("MainMenuBg.png", Texture.class);
        ShardWar.main.getAssetLoader().load("mobHpBarBg.png", Texture.class);
        ShardWar.main.getAssetLoader().load("mobHpBarKnob.png", Texture.class);
        Preferences pref = Gdx.app.getPreferences("config");
        String locale = pref.getString("locale", "en");
        ShardWar.main.getAssetLoader().initLang("en"/*locale*/);
        init();
    }

    @Override
    public void show() {
//        super.show();
//        GDefence.getInstance().assetLoader.loadAll();
        ShardWar.main.getAssetLoader().loadAll();
    }

    @Override
    protected void onLoad() {
//        GDefence.getInstance().initScreens();
        ShardWar.main.getAssetLoader().setFilters();
        ShardWar.main.init();
        ShardWar.main.switchScreen(0);
    }
}
