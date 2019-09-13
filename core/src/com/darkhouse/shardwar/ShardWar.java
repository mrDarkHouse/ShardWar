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
	private GameModeChooseScreen gameModeChooseScreen;
	public static FightScreen fightScreen;
	public static User user;

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
		FightScreen.initSocket();
		initScreens();
	}
	private void initScreens(){
		mainMenu = new MainMenu();
		gameModeChooseScreen = new GameModeChooseScreen();
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
				setScreen(gameModeChooseScreen);
//				fightScreen = new FightScreen();
//				setScreen(fightScreen);
			}
			break;
			case 3: {
				setScreen(wikiScreen);
			}
			break;
		}
	}
	public void startPlayerGame(){
		fightScreen = new FightScreen();
		setScreen(fightScreen);
	}
	public void startSplitGame(){
		fightScreen = new FightScreen(ShardWar.user);
		setScreen(fightScreen);
	}
	public void startBotGame(){

	}

	public void setPreviousScreen(){
		Screen currentScreen = getScreen();
		if(currentScreen instanceof OptionsMenu) 			setScreen(mainMenu);
		if(currentScreen instanceof FightScreen) 			setScreen(gameModeChooseScreen);
		if(currentScreen instanceof GameModeChooseScreen) 	setScreen(mainMenu);
		if(currentScreen instanceof WikiScreen)				setScreen(mainMenu);
	}


	
	@Override
	public void dispose () {
		assetLoader.dispose();
		FontLoader.dispose();
	}
}
