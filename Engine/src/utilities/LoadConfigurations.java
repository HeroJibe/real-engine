package utilities;

import java.io.File;

import gamegui.ApplySettings;
import main.Main;

public class LoadConfigurations 
{
	public static void load()
	{
		KeyBinds.loadKeys(new File("config\\keyconfig.config"));
		GraphicsConfig.loadGraphics(new File("config\\graphics.config"));
		ApplySettings.applySettings(Main.getSettingsWindow());
	}
}
