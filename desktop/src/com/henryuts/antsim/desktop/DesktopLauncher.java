package com.henryuts.antsim.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.henryuts.antsim.AntSim;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Ant Simulator 0.1";
		config.height = 680;
		config.width = 1200;
		new LwjglApplication(new AntSim(), config);
	}
}
