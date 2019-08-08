package com.darkhouse.shardwar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.darkhouse.shardwar.Screens.*;
import com.darkhouse.shardwar.Tools.AbilityHelper;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

public class ShardWar extends Game {

	private MainMenu mainMenu;
	private OptionsMenu optionsMenu;
	private WikiScreen wikiScreen;
	public static FightScreen fightScreen;

	public MainMenu getMainMenu() {
		return mainMenu;
	}
	public OptionsMenu getOptionsMenu() {
		return optionsMenu;
	}

	public static ShardWar main;

	private AssetLoader assetLoader;

	public AssetLoader getAssetLoader() {
		return assetLoader;
	}

	@Override
	public void create () {
		main = this;
		assetLoader = new AssetLoader();
//		init();
		Texture.setAssetManager(assetLoader);


		setScreen(new StartingLoadScreen());
//		setScreen(mainMenu);
	}

	public void init(){
//		FontLoader.load();
//		assetLoader.loadAll();
//		assetLoader.setFilters();
		FontLoader.load();
		AbilityHelper.init();
		FightScreen.initAllSpells();
		initScreens();
	}
	private void initScreens(){
		mainMenu = new MainMenu();
		optionsMenu = new OptionsMenu();
		wikiScreen = new WikiScreen();
	}
	public void switchScreen(int index){
		switch (index){
			case 0: setScreen(mainMenu);
			break;
			case 1: setScreen(optionsMenu);
			break;
			case 2: {
				fightScreen = new FightScreen();
				fightScreen.init();
				setScreen(fightScreen);

			}
			break;
			case 3: {
				setScreen(wikiScreen);
			}
			break;
		}
	}

	public void setPreviousScreen(){
		Screen currentScreen = getScreen();
		if(currentScreen instanceof OptionsMenu) setScreen(mainMenu);
		if(currentScreen instanceof FightScreen) setScreen(mainMenu);
		if(currentScreen instanceof WikiScreen) setScreen(mainMenu);
	}


	
	@Override
	public void dispose () {
		assetLoader.dispose();
		FontLoader.dispose();
	}
}
