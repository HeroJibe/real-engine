/**
 * Applies and loads settings
 * 
 * @author Ethan Vrhel
 */

package gamegui;

import java.awt.Color;
import java.io.IOException;

import core.GameSound;
import core.ReflectiveEntityHandler;
import core.RenderLoop;
import main.Main;
import utilities.GraphicsConfig;
import utilities.KeyBinds;

public class ApplySettings
{
	/**
	 * Applies the settings from the settings window to
	 * the actual in-game settings
	 * 
	 * @param settings
	 */
	public static void applySettings(Settings settings)
	{		
		String lod = settings.getVideoSettings().lod.getSelectedItem().toString();
		if (lod.equals("Low"))
		{
			Main.getEntityHandler().currLod = 0;
		}
		else if (lod.equals("Medium"))
		{
			Main.getEntityHandler().currLod = 1;
		}
		else if (lod.equals("High"))
		{
			Main.getEntityHandler().currLod = 2;
		}
		
		
		String reflectionType = settings.getVideoSettings().water.getSelectedItem().toString();
		if (reflectionType.equals("Fast Dynamic Reflections"))
		{
			Main.getReflectionHandler().setReflections(ReflectiveEntityHandler.FAST_DYNAMIC_REFLECTIONS);
		}
		else if (reflectionType.equals("Full Dynamic Reflections"))
		{
			Main.getReflectionHandler().setReflections(ReflectiveEntityHandler.FULL_DYNAMIC_REFLECTIONS);
		}
		else if (reflectionType.equals("Static Reflections"))
		{
			Main.getReflectionHandler().setReflections(ReflectiveEntityHandler.STATIC_REFLECTIONS);
		}
		else if (reflectionType.equals("No Reflections"))
		{
			Main.getReflectionHandler().setReflections(ReflectiveEntityHandler.NO_REFLECTIONS);
		}
		Main.getReflectionHandler().update(true);
		
		
		String particleType = settings.getVideoSettings().particle.getSelectedItem().toString();
		if (particleType.equals("All"))
		{
			Main.getParticleEffectHandler().particleRateDivider = 1;
		}
		else if (particleType.equals("Half"))
		{
			Main.getParticleEffectHandler().particleRateDivider = 2;
		}
		else if (particleType.equals("None"))
		{
			Main.getParticleEffectHandler().particleRateDivider = -1;
		}
		
		
		String animationType = settings.getVideoSettings().animation.getSelectedItem().toString();
		if (animationType.equals("Yes"))
		{
			Main.getAnimationHandler().runAnimations = true;
		}
		else if (animationType.equals("No"))
		{
			Main.getAnimationHandler().runAnimations = false;
		}
		
		String fps = settings.getVideoSettings().fps.getSelectedItem().toString();
		if (fps.equals("Unlimited"))
		{
			RenderLoop.maximumFPS = -1;
		}
		else if (fps.equals("15"))
		{
			RenderLoop.maximumFPS = 15;
		}
		else if (fps.equals("30"))
		{
			RenderLoop.maximumFPS = 30;
		}
		else if (fps.equals("60"))
		{
			RenderLoop.maximumFPS = 60;
		}
		else if (fps.equals("120"))
		{
			RenderLoop.maximumFPS = 120;
		}
		
		
		GameSound.masterVolume = settings.getAudioSettings().master.getValue() / 100f;
		GameSound.musicVolume = settings.getAudioSettings().music.getValue() / 100f;
		GameSound.effectVolume = settings.getAudioSettings().sfx.getValue() / 100f;
		
		String debug = settings.getGeneralSettings().debug.getSelectedItem().toString();
		if (debug.equals("Yes"))
		{
			Main.DEBUG = true;
			Main.WRITE_TO_LOG = true;
		}
		else
		{
			Main.DEBUG = false;
			Main.println("Debug mode deactivated.  Logs will not be written.");
			Main.WRITE_TO_LOG = false;
		}
		
		KeyBinds.UP = settings.getControlSettings().jumpKey.getText();
		KeyBinds.LEFT = settings.getControlSettings().leftKey.getText();
		KeyBinds.RIGHT = settings.getControlSettings().rightKey.getText();
		KeyBinds.PAUSE_MENU = settings.getControlSettings().pauseKey.getText();
	
		try 
		{
			KeyBinds.generateFile();
			GraphicsConfig.generateFile();
		} 
		catch (IOException e) 
		{
			Main.println("Failed exporting configuration files.", Color.RED);
		}
	}
	
	/**
	 * Loads the game settings into the settings window
	 * 
	 * @param settings
	 */
	public static void loadSettings(Settings settings)
	{
		if (Main.getReflectionHandler().getReflectionType() == ReflectiveEntityHandler.FAST_DYNAMIC_REFLECTIONS)
		{
			settings.getVideoSettings().water.setSelectedItem("Fast Dynamic Reflections");
		}
		else if (Main.getReflectionHandler().getReflectionType() == ReflectiveEntityHandler.FULL_DYNAMIC_REFLECTIONS)
		{
			settings.getVideoSettings().water.setSelectedItem("Full Dynamic Reflections");
		}
		else if (Main.getReflectionHandler().getReflectionType() == ReflectiveEntityHandler.STATIC_REFLECTIONS)
		{
			settings.getVideoSettings().water.setSelectedItem("Static Reflections");
		}
		else if (Main.getReflectionHandler().getReflectionType() == ReflectiveEntityHandler.NO_REFLECTIONS)
		{
			settings.getVideoSettings().water.setSelectedItem("No Reflections");
		}
		Main.getReflectionHandler().update(true);
		
		
		if (Main.getParticleEffectHandler().particleRateDivider == 1)
		{
			settings.getVideoSettings().particle.setSelectedItem("All");
		}
		else if (Main.getParticleEffectHandler().particleRateDivider == 2)
		{
			settings.getVideoSettings().particle.setSelectedItem("Half");
		}
		else if (Main.getParticleEffectHandler().particleRateDivider == -1)
		{
			settings.getVideoSettings().particle.setSelectedItem("None");
		}
		
		
		if (RenderLoop.maximumFPS == -1)
		{
			settings.getVideoSettings().fps.setSelectedItem("Unlimited");
		}
		else if (RenderLoop.maximumFPS == 15)
		{
			settings.getVideoSettings().fps.setSelectedItem("15");
		}
		else if (RenderLoop.maximumFPS == 30)
		{
			settings.getVideoSettings().fps.setSelectedItem("30");
		}
		else if (RenderLoop.maximumFPS == 60)
		{
			settings.getVideoSettings().fps.setSelectedItem("60");
		}
		else if (RenderLoop.maximumFPS == 120)
		{
			settings.getVideoSettings().fps.setSelectedItem("120");
		}
		
		
		if (Main.getEntityHandler().currLod == 0)
		{
			settings.getVideoSettings().lod.setSelectedItem("Low");
		}
		else if (Main.getEntityHandler().currLod == 1)
		{
			settings.getVideoSettings().lod.setSelectedItem("Medium");
		}
		else if (Main.getEntityHandler().currLod == 2)
		{
			settings.getVideoSettings().lod.setSelectedItem("High");
		}
		
		
		settings.getAudioSettings().master.setValue((int) (100 * GameSound.masterVolume));
		settings.getAudioSettings().music.setValue((int) (100 * GameSound.musicVolume));
		settings.getAudioSettings().sfx.setValue((int) (100 * GameSound.effectVolume));
		
		
		if (Main.DEBUG)
			settings.getGeneralSettings().debug.setSelectedItem("Yes");
		else
			settings.getGeneralSettings().debug.setSelectedItem("No");
		
		
		
		settings.getControlSettings().jumpKey.setText(KeyBinds.UP);
		settings.getControlSettings().leftKey.setText(KeyBinds.LEFT);
		settings.getControlSettings().rightKey.setText(KeyBinds.RIGHT);
		settings.getControlSettings().pauseKey.setText(KeyBinds.PAUSE_MENU);
	}
}
