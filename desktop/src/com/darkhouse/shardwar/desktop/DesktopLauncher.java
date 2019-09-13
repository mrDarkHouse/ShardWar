package com.darkhouse.shardwar.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.darkhouse.shardwar.ShardWar;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		System.setProperty("user.name", "\\xD0\\x90\\xD0\\xBD\\xD0\\xB4\\xD1\\x80\\xD0\\xB5\\xD0\\xB9");
		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");

		config.title = "Shard War";
		config.width = 1024;//1024
		config.height = 576;//576
		config.fullscreen = false;
		config.foregroundFPS = 60;
//		config.samples = 64;

		new LwjglApplication(new ShardWar(), config);
	}
}
