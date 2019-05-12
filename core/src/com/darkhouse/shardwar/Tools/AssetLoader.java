package com.darkhouse.shardwar.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;

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
        load("backButton.png", Texture.class);
        load("shard.png", Texture.class);
        load("towerSlot.png", Texture.class);
        load("wallSlot.png", Texture.class);
        load("towers/laser.png", Texture.class);
        load("towers/assault.png", Texture.class);
        load("towers/sniper.png", Texture.class);
        load("walls/brickWall.png", Texture.class);
        load("walls/energyWall.png", Texture.class);
        load("walls/spikeWall.png", Texture.class);

        load("target/centerTarget.png", Texture.class);
        load("target/leftTarget.png", Texture.class);
        load("target/rightTarget.png", Texture.class);
        load("target/endTarget.png", Texture.class);

        load("target/targetCenter.png", Texture.class);
        load("target/targetLeft.png", Texture.class);
        load("target/targetRight.png", Texture.class);
        load("target/targetCenterInv.png", Texture.class);
        load("target/targetLeftInv.png", Texture.class);
        load("target/targetRightInv.png", Texture.class);

        load("projectiles/sniper.png", Texture.class);
        load("projectiles/assault.png", Texture.class);

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

        load("Ability/Effects/disarm.png", Texture.class);
        load("Ability/Effects/weakness.png", Texture.class);
        load("Ability/Effects/vulnerability.png", Texture.class);
        load("Ability/Effects/silence.png", Texture.class);
        load("Ability/Effects/nottoday.png", Texture.class);
        load("Ability/Effects/opticalsight.png", Texture.class);
        load("Ability/Effects/poisonsplash.png", Texture.class);
        load("Ability/Effects/shieldup.png", Texture.class);
        load("Ability/Effects/disablefield.png", Texture.class);

        load("towerSlotSelect.png", Texture.class);
        load("wallSlotSelect.png", Texture.class);
        load("towerSlotDisabled.png", Texture.class);
        load("wallSlotDisabled.png", Texture.class);

        load("mobHpBarBg.png", Texture.class);
        load("mobHpBarKnob.png", Texture.class);

        load("player1Logo.png", Texture.class);
        load("player2Logo.png", Texture.class);

        ObjectMap<String, Object> fontMap = new ObjectMap<String, Object>();
        fontMap.put("default-font", FontLoader.generateFont(0, 26, Color.BLACK));
        fontMap.put("description-font", FontLoader.generateFont(1, 20, Color.BLACK));
        fontMap.put("spell-font", FontLoader.generateFont(1, 14, Color.WHITE));


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

    public void setFilters(){
        get("towerSlot.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("wallSlot.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("shard.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("walls/brickWall.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("walls/energyWall.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("walls/spikeWall.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        get("target/targetCenter.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetCenterInv.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetRight.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetRightInv.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetLeft.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetLeftInv.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

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
