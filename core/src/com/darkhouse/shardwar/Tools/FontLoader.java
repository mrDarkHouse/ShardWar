package com.darkhouse.shardwar.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class FontLoader {
    private static FreeTypeFontGenerator generator;

    public static Label.LabelStyle effectDurationStyle = generateStyle(0, 14, Color.WHITE, 1, Color.BLACK);

    public static BitmapFont amountFont;

    final static String font_chars_ru =
            "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";

    public static String firstCapitalLetter(String s){
        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }
    public static String firstLowerLetter(String s){
        return s.substring(0, 1).toLowerCase() + s.substring(1).replaceAll(" ", "");
    }
    private static final String[] COLORS = {
            /*0*/"[#1C9D1Cff]",//target type
            /*1*/"[#bfc84fff]",//field targeting
            /*2*/"[#4169E1ff]",//affected units
            /*3*/"[#F5A014ff]",//heal
            /*4*/"[#C71A1Aff]",//dmg
            /*5*/"[#bf36aeff]",//duration
            /*6*/"[#B7B353ff]",//effect
            /*7*/"[#30511fff]",//poison
    };

    public static String colorCode(int id){
        return COLORS[id];
//        switch (id){
//            case 0:return "[#990000ff]";//red
//            case 1:return "[#0ffe00ff]";//green
//            case 2:return "[#00ffffff]";//blue
//            case 3:return "[#900274ff]";//Amethyst
//            case 4:return "[#009900ff]";//Emerald
//            case 5:return "[#66ffffff]";//Diamond
//            case 6:return "[#4169E1ff]";//royalBlue
//            case 7:return "[#CCCC00ff]";//darkYellow
//            case 8:return "[#00886Bff]";//observatory green
//            case 9:return "[#F09135ff]";//orange
//            case 10:return "[#64A619ff]";//green (mob spells)
//            case 11:return "[#CD6600ff]";//dark orange (layer armor)
//            case 12:return "[#8B0000ff]";//dark red (debuffs)
//            case 13:return "[#000000ff]";//black
//            default:return "";
//        }
    }

    public static String colorCode(Color c){
        return "[#" + c.toString() + "]";
    }
    public static String colorString(String s, int id){
        return colorCode(id) + s + "[]";
    }







    public FontLoader() {
    }

    public static void load(){

    }
    public static Window.WindowStyle generateWindowStyle(int type, int size, Color fontColor){
        Window.WindowStyle style = new Window.WindowStyle();
        style.titleFont = generateFont(type, size, fontColor);
        return style;
    }

    public static Label.LabelStyle generateStyle(int type, int size, Color fontColor){
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = generateFont(type, size, fontColor);
//        style.fontColor = fontColor;
        return style;
    }
    public static Label.LabelStyle generateStyle(int type, int size, Color fontColor, int borderSize, Color borderColor){
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = generateFont(type, size, fontColor, borderSize, borderColor);
//        style.fontColor = fontColor;
        return style;
    }
//    public static Label.LabelStyle generateSecondaryStyle(int size, Color fontColor){
//        Label.LabelStyle style = new Label.LabelStyle();
//        style.font = generateSecondaryFont(size, Color.WHITE);
//        style.fontColor = fontColor;
//        return style;
//    }
//    public static Label.LabelStyle generateRobotoStyle(int size, Color fontColor){
//        Label.LabelStyle style = new Label.LabelStyle();
//        style.font = generateSecondaryFont(size, Color.WHITE);
//        style.fontColor = fontColor;
//        return style;
//    }

//    public static Label.LabelStyle generateIskoolaFont(int size, Color color, int borderSize, Color borderColor){
//
//    }


    private static BitmapFont generateDefaultBorderFont(int size, Color color, int borderSize, Color borderColor, FileHandle f){
        generator = new FreeTypeFontGenerator(f);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = font_chars_ru;
        parameter.size = size;
        parameter.color = color;
        parameter.borderWidth = borderSize;
        parameter.borderColor = borderColor;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        return generator.generateFont(parameter);
    }

    public static BitmapFont generateFont(int type, int size, Color color, int borderSize, Color borderColor){
        FileHandle f;
        switch (type){
            case 0:f = Gdx.files.internal("Fonts/Impact.ttf");
                break;
            case 1:f = Gdx.files.internal("Fonts/roboto-regular.ttf");
                break;
            case 2:f = Gdx.files.internal("Fonts/Bookman.ttf");
                break;
//            case 3:f = Gdx.files.internal("Fonts/Signboard.ttf");
//                break;
            default:throw new IllegalArgumentException("cant find font with type " + type);
        }
        return generateDefaultBorderFont(size, color, borderSize, borderColor, f);
    }
//    public static BitmapFont generateSecondaryFont(int size, Color color, int borderSize, Color borderColor){
//        return generateDefaultBorderFont(size, color, borderSize, borderColor, Gdx.files.internal("Fonts/roboto-regular.ttf"));
//    }
//    public static BitmapFont generateRobotoMedium(int size, Color color, int borderSize, Color borderColor){
//        return generateDefaultBorderFont(size, color, borderSize, borderColor, Gdx.files.internal("Fonts/Roboto-Medium.ttf"));
//    }

    private static BitmapFont generateDefaultFont(int size, Color color, FileHandle f){
        generator = new FreeTypeFontGenerator(f);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = font_chars_ru;
        parameter.characters += "°";
        parameter.size = size;
        parameter.color = color;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        return generator.generateFont(parameter);
    }

    public static BitmapFont generateFont(int type, int size, Color color){
        FileHandle f;
        switch (type) {
            case 0:f = Gdx.files.internal("Fonts/Impact.ttf");
                break;
//            case 1:f = Gdx.files.internal("Fonts/roboto-regular.ttf");
//                break;
            case 1:f = Gdx.files.internal("Fonts/Roboto-Medium.ttf");
                break;
            case 2:f = Gdx.files.internal("Fonts/Bookman.ttf");
                break;
            default:throw new IllegalArgumentException("cant find font with type " + type);
        }
        return generateDefaultFont(size, color, f);
    }
//    public static BitmapFont generateSecondaryFont(int size, Color color){
//
//        return generateDefaultFont(size, color, Gdx.files.internal("Fonts/roboto-regular.ttf"));
//    }
//    public static BitmapFont generateRobotoMedium(int size, Color color){
//
//        return generateDefaultFont(size, color, Gdx.files.internal("Fonts/Roboto-Medium.ttf"));
//    }

    public static String getOneColorButtonString(int index, String s, Color first, Color other){
//        BitmapFont b1 = generateFont(size);
//        b1.getData().markupEnabled = true;
//        Label.LabelStyle style = new Label.LabelStyle(b1, null);
//        style.fontColor = other;
        return ("[#" + other.toString() + "]" + s.substring(0, index) + "[#" + first.toString() + "]" +
                s.substring(index, index + 1) + "[#" + other.toString() + "]" + s.substring(index + 1));
    }
    public static String getOneColorButtonString(int index, String s, int first, int other){
//        BitmapFont b1 = generateFont(size);
//        b1.getData().markupEnabled = true;
//        Label.LabelStyle style = new Label.LabelStyle(b1, null);
//        style.fontColor = other;
        return (colorCode(other) + s.substring(0, index)  + colorCode(first) +
                s.substring(index, index + 1) + colorCode(other) +  s.substring(index + 1));
    }
    public static String getOneColorButtonString(String s, int color){
        return colorString(s.substring(0, 1), color) + s.substring(1);
    }

//    public static BitmapFont generateMenuFont(int size, Color color){
//        generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Iscoola.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = size;
//        parameter.color = color;
//        return  generator.generateFont(parameter);
//    }




    public static void dispose(){
//        generator.dispose();
    }
}
