package com.darkhouse.shardwar.Tools;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.darkhouse.shardwar.ShardWar;

import java.io.File;
import java.util.Locale;

public class AssetLoader extends AssetManager {

    private I18NBundle b;

    public String getWord(String s){
        return b.get(s);
    }
    public void initLang(String locale){
        I18NBundleLoader.I18NBundleParameter param = new I18NBundleLoader.I18NBundleParameter(new Locale(locale), "UTF-8");
        load("Language/text", I18NBundle.class, param);
        finishLoading();
        b = get("Language/text", I18NBundle.class);
    }

    public void loadAll(){
        load("MainMenuBg.png", Texture.class);
        load("levelWinBg.png", Texture.class);
        load("levelLooseBg.png", Texture.class);
        load("backButton.png", Texture.class);
        load("shard.png", Texture.class);
        load("towerSlot.png", Texture.class);
        load("wallSlot.png", Texture.class);
        load("less.png", Texture.class);
        load("more.png", Texture.class);

        load("walls/brickWall.png", Texture.class);
        load("walls/energyWall.png", Texture.class);
        load("walls/spikeWall.png", Texture.class);
        load("walls/brickWallOriginal.png", Texture.class);
        load("walls/energyWallOriginal.png", Texture.class);
        load("walls/spikeWallOriginal.png", Texture.class);

        load("towers/shotgun.png", Texture.class);
        load("towers/assault.png", Texture.class);
        load("towers/rocket.png", Texture.class);
        load("towers/musket.png", Texture.class);
        load("towers/machinegun.png", Texture.class);
        load("towers/cannon.png", Texture.class);
        load("towers/doublebarrel.png", Texture.class);
        load("towers/sniper.png", Texture.class);
        load("towers/doublerocket.png", Texture.class);

        load("projectiles/shotgun.png", Texture.class);
        load("projectiles/assault.png", Texture.class);
        load("projectiles/rocket.png", Texture.class);
        load("projectiles/musket.png", Texture.class);
        load("projectiles/machinegun.png", Texture.class);
        load("projectiles/cannon.png", Texture.class);
        load("projectiles/doublebarrel.png", Texture.class);
        load("projectiles/sniper.png", Texture.class);
        load("projectiles/doublerocket.png", Texture.class);



//        load("target/centerTarget.png", Texture.class);
//        load("target/leftTarget.png", Texture.class);
//        load("target/rightTarget.png", Texture.class);
//        load("target/endTarget.png", Texture.class);

        load("target/targetCenter.png", Texture.class);
        load("target/targetLeft.png", Texture.class);
        load("target/targetRight.png", Texture.class);
        load("target/targetCenterInv.png", Texture.class);
        load("target/targetLeftInv.png", Texture.class);
        load("target/targetRightInv.png", Texture.class);
        load("target/targetLeft2.png", Texture.class);
        load("target/targetRight2.png", Texture.class);
        load("target/targetLeftInv2.png", Texture.class);
        load("target/targetRightInv2.png", Texture.class);
//        load("target/targetCenterG.png", Texture.class);
//        load("target/targetLeftG.png", Texture.class);
//        load("target/targetRightG.png", Texture.class);



        load("Ability/Spells/Background/tier1.png", Texture.class);
        load("Ability/Spells/Background/tier2.png", Texture.class);
        load("Ability/Spells/Background/tier3.png", Texture.class);
        load("Ability/Spells/Background/tier4.png", Texture.class);

        load("Ability/Spells/firebreath.png", Texture.class);
        load("Ability/Spells/disarm.png", Texture.class);
        load("Ability/Spells/heal.png", Texture.class);
        load("Ability/Spells/weakness.png", Texture.class);
        load("Ability/Spells/vulnerability.png", Texture.class);
        load("Ability/Spells/fatalblow.png", Texture.class);
        load("Ability/Spells/silence.png", Texture.class);
        load("Ability/Spells/combiner.png", Texture.class);
        load("Ability/Spells/greed.png", Texture.class);
        load("Ability/Spells/nottoday.png", Texture.class);
        load("Ability/Spells/opticalsight.png", Texture.class);
        load("Ability/Spells/poisonsplash.png", Texture.class);
        load("Ability/Spells/shieldup.png", Texture.class);
        load("Ability/Spells/disablefield.png", Texture.class);
        load("Ability/Spells/lifedrain.png", Texture.class);
        load("Ability/Spells/darkritual.png", Texture.class);
        load("Ability/Spells/firebullets.png", Texture.class);
        load("Ability/Spells/clawsmash.png", Texture.class);
        load("Ability/Spells/vampire.png", Texture.class);
        load("Ability/Spells/fanofknives.png", Texture.class);
        load("Ability/Spells/rejuvenation.png", Texture.class);
        load("Ability/Spells/additionalrocket.png", Texture.class);
        load("Ability/Spells/dispel.png", Texture.class);
        load("Ability/Spells/immune.png", Texture.class);

        load("Ability/Effects/disarm.png", Texture.class);
        load("Ability/Effects/weakness.png", Texture.class);
        load("Ability/Effects/vulnerability.png", Texture.class);
        load("Ability/Effects/silence.png", Texture.class);
        load("Ability/Effects/nottoday.png", Texture.class);
        load("Ability/Effects/opticalsight.png", Texture.class);
        load("Ability/Effects/poisonsplash.png", Texture.class);
        load("Ability/Effects/shieldup.png", Texture.class);
        load("Ability/Effects/disablefield.png", Texture.class);
        load("Ability/Effects/firebullets.png", Texture.class);
        load("Ability/Effects/vampire.png", Texture.class);
        load("Ability/Effects/rejuvenation.png", Texture.class);
        load("Ability/Effects/additionalrocket.png", Texture.class);
        load("Ability/Effects/immune.png", Texture.class);


        load("Ability/Animation/clawsmash.png", Texture.class);
        load("Ability/Animation/clawsmash.atlas", TextureAtlas.class);

        load("towerSlotSelect.png", Texture.class);
        load("wallSlotSelect.png", Texture.class);
        load("towerSlotReserve.png", Texture.class);
        load("wallSlotReserve.png", Texture.class);
        load("towerSlotDisabled.png", Texture.class);
        load("wallSlotDisabled.png", Texture.class);

        load("mobHpBarBg.png", Texture.class);
        load("mobHpBarKnob.png", Texture.class);

        load("player1Logo.png", Texture.class);
        load("player2Logo.png", Texture.class);
        load("player3Logo.png", Texture.class);

        load("Wiki/basics.png", Texture.class);
        load("Wiki/info1.png", Texture.class);
        load("Wiki/info2.png", Texture.class);
        load("Wiki/info3.png", Texture.class);
        load("Wiki/info4.png", Texture.class);

//        setLoader(GIF.class, new Gifloader(resolver));
//        setLoader();


        ObjectMap<String, Object> fontMap = new ObjectMap<String, Object>();
        float androidScale = 2.0f;
        float scale = 1f;
        if(Gdx.app.getType() == Application.ApplicationType.Android) scale = androidScale;
        fontMap.put("default-font", FontLoader.generateFont(2, (int)(24*scale), Color.BLACK));
        fontMap.put("description-font", FontLoader.generateFont(1, (int)(20*scale), Color.BLACK));
        fontMap.put("description-main", FontLoader.generateFont(0, (int)(26*scale), new Color(0xfddd95ff), 2, Color.BLACK));
        fontMap.put("spell-font", FontLoader.generateFont(1, (int)(14*scale), Color.WHITE));


        SkinLoader.SkinParameter parameter = new SkinLoader.SkinParameter(fontMap);
        load("skins/uiskin.json", Skin.class, parameter);

    }

    public Drawable getCenterTarget(int player){
        if(player == 1) return new TextureRegionDrawable(new TextureRegion(get("target/targetCenter.png", Texture.class)));
        else return new TextureRegionDrawable(new TextureRegion(get("target/targetCenterInv.png", Texture.class)));
    }
    public Drawable getRightTarget(int player){
        if(player == 1) return new TextureRegionDrawable(new TextureRegion(get("target/targetRight.png", Texture.class)));
        else return new TextureRegionDrawable(new TextureRegion(get("target/targetRightInv.png", Texture.class)));
    }
    public Drawable getLeftTarget(int player){
        if(player == 1) return new TextureRegionDrawable(new TextureRegion(get("target/targetLeft.png", Texture.class)));
        else return new TextureRegionDrawable(new TextureRegion(get("target/targetLeftInv.png", Texture.class)));
    }
    public Drawable getRightTarget2(int player){
        if(player == 1) return new TextureRegionDrawable(new TextureRegion(get("target/targetRight2.png", Texture.class)));
        else return new TextureRegionDrawable(new TextureRegion(get("target/targetRightInv2.png", Texture.class)));
    }
    public Drawable getLeftTarget2(int player){
        if(player == 1) return new TextureRegionDrawable(new TextureRegion(get("target/targetLeft2.png", Texture.class)));
        else return new TextureRegionDrawable(new TextureRegion(get("target/targetLeftInv2.png", Texture.class)));
    }

    public TextureAtlas getAnimation(String spellName){
        return get("Ability/Animation/" + spellName + ".atlas");
    }

    public ProgressBar.ProgressBarStyle getTimeBarStyle(int height){
//        int height = 10;
        Skin skin = ShardWar.main.getAssetLoader().getSkin();
        String styleName = "exp-bar";
//        Drawable b = skin.getDrawable("bar-bg");
//        Drawable k = skin.getDrawable("exp-bar-knob");
//
//        ProgressBar.ProgressBarStyle n = new ProgressBar.ProgressBarStyle();
//
//        n.background = b;
//        n.knobBefore = k;
        ProgressBar.ProgressBarStyle n = new ProgressBar.ProgressBarStyle(skin.get(styleName, ProgressBar.ProgressBarStyle.class));
        n.background.setMinHeight(height);
        n.knobBefore.setMinHeight(height - 2);
//        String styleName = "exp-bar";
//        ProgressBar.ProgressBarStyle s = new ProgressBar.ProgressBarStyle(skin.get(styleName, ProgressBar.ProgressBarStyle.class));
//        ProgressBar.ProgressBarStyle n = new ProgressBar.ProgressBarStyle();
        return n;
    }
    public ProgressBar.ProgressBarStyle getHpBarStyle(int height){
        Skin skin = ShardWar.main.getAssetLoader().getSkin();
//        Drawable b = skin.getDrawable("bar-bg2");
//        Drawable k = skin.getDrawable("health-bar-knob");
//        ProgressBar.ProgressBarStyle n = new ProgressBar.ProgressBarStyle();
//        n.background = b;
//        n.knobBefore = k;
        String styleName = "health-bar";
        ProgressBar.ProgressBarStyle n = new ProgressBar.ProgressBarStyle(skin.get(styleName, ProgressBar.ProgressBarStyle.class));

        n.background.setMinHeight(height);
        n.knobBefore.setMinHeight(height - 2);
//        String styleName = "health-bar";
//        ProgressBar.ProgressBarStyle s = new ProgressBar.ProgressBarStyle(skin.get(styleName, ProgressBar.ProgressBarStyle.class));
//        ProgressBar.ProgressBarStyle n = new ProgressBar.ProgressBarStyle();
//        n.background = s.background;
//        n.knobBefore = s.knobBefore;
//        ProgressBar.ProgressBarStyle n = new ProgressBar.ProgressBarStyle();
        return n;
    }


    public void setFilters(){
        get("towerSlot.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("wallSlot.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("shard.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("walls/brickWall.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("walls/energyWall.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("walls/spikeWall.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("walls/brickWallOriginal.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("walls/energyWallOriginal.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("walls/spikeWallOriginal.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        get("towers/shotgun.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("towers/assault.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("towers/rocket.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        get("target/targetCenter.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetCenterInv.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetRight.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetRightInv.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetLeft.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetLeftInv.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetRight2.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetRightInv2.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetLeft2.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetLeftInv2.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        get("towerSlot.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        get("player1Logo.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("player2Logo.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    }

    public ProgressBar.ProgressBarStyle getLoadingBarStyle(){
        TextureRegionDrawable barFone = new TextureRegionDrawable(new TextureRegion(get("mobHpBarBg.png", Texture.class)));
        TextureRegionDrawable barTop = new TextureRegionDrawable(new TextureRegion(get("mobHpBarKnob.png", Texture.class)));
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle(barFone, barTop);
        style.knobBefore = style.knob;
        return style;
    }

    public Texture getSpell(String name){
        return get("Ability/Spells/" + name + ".png", Texture.class);
    }
    public Texture getEffectIcon(String name){
        return get("Ability/Effects/" + name + ".png", Texture.class);
    }

    public Texture getSpellSlotBgTexture(int tier){
        return get("Ability/Spells/Background/tier" + tier + ".png", Texture.class);
    }

    public Skin getSkin(){
        return get("skins/uiskin.json", Skin.class);
    }
    public Texture getMainMenuBg(){
        return get("MainMenuBg.png");
    }

    public ImageButton.ImageButtonStyle getBackButtonSkin(){
        return generateImageButtonSkin(get("backButton.png", Texture.class));
    }

    public ImageButton.ImageButtonStyle generateImageButtonSkin(Texture t){
        ImageButton.ImageButtonStyle s = new ImageButton.ImageButtonStyle();
        s.up = new TextureRegionDrawable(new TextureRegion(t));
        return s;
    }



}
