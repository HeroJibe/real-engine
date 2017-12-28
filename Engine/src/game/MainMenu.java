package game;

import core.Entity;
import core.guiElements.GuiButton;
import core.guiElements.GuiElement;
import core.guiElements.GuiImage;
import main.Main;

/**
 * The <code>MainMenu</code> class is the initial
 * GUI that is shown on the startup of the game
 * 
 * @author Ethan Vrhel
 * @see GuiElement
 * @see GameMain
 */
public class MainMenu 
	implements Runnable
{
	private GuiElement[] elems;
	private GuiButton newGame;
	private GuiButton settings;
	private GuiButton exit;
	private boolean loaded = false;
	
	public MainMenu()
	{
		GuiImage background = new GuiImage("background",
				Entity.STATIC, Main.getResourceHandler().getByName("menu.png"),
				0, 0, 0, 1920, 1080);
		newGame = new GuiButton("newGame", Entity.STATIC, 
				Main.getResourceHandler().getByName("new.png"), 0, 200, 0, 400, 64);
		settings = new GuiButton("settingsMenu", 1, Main.getResourceHandler().getByName("settingsunselected.png"), 
				0, 264, Integer.MAX_VALUE, 400, 64);
		exit = new GuiButton("exitMenu", 1, Main.getResourceHandler().getByName("exitunselected.png"), 
				0, 328, Integer.MAX_VALUE, 400, 64);
		
		Main.getGuiHandler().add(newGame);
		Main.getGuiHandler().add(settings);
		Main.getGuiHandler().add(exit);
		Main.getGuiHandler().add(background);
		newGame.setVisible(true);
		GuiElement[] buttons2 = {newGame, settings, exit, background};
		elems = buttons2;
	}
	
	@Override
	public void run()
	{
		Main.loadingMap = false;
		//System.out.println("started menu");
		if (Main.getGameMain().getInitialMap() == null)
		{
			Main.println("Game.dat is setup incorrectly, the initial map is null!");
			Main.exit(1);
		}
		
		while (true)
		{
			if (newGame.isHovered() && newGame.isVisible())
			{
				//System.out.println("hovered");
				newGame.setImage(Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("newselected.png", true)));
				if (newGame.isClicked())
				{
					hide();
					Main.loadMap(Main.getGameMain().getInitialMap(), 0, 0);
					//Main.getPlayer().getPlayerEntity().setX(
					//		MapLoader.xSpawn);
					//Main.getPlayer().getPlayerEntity().setY(
					//		MapLoader.ySpawn);
					Main.getGuiHandler().remove("background");
					Main.getGuiHandler().remove("newGame");
					Main.getGuiHandler().remove("settingsMenu");
					Main.getGuiHandler().remove("exitMenu");
					loaded = true;
					return;
				}
			}
			else
			{
				newGame.setImage(Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("new.png", true)));
			}
			
			if (settings.isHovered() && newGame.isVisible())
			{
				settings.setImage(Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("settingsselected.png", true)));
				if (settings.isClicked())
				{
					Main.getSettingsWindow().pack();
					Main.getSettingsWindow().showWindow();
				}
			}
			else
			{
				settings.setImage(Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("settingsunselected.png", true)));
			}
			
			if (exit.isHovered() && newGame.isVisible())
			{
				exit.setImage(Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("exitselected.png", true)));
				if (exit.isClicked())
				{
					Main.exit(0);
				}
			}
			else
			{
				exit.setImage(Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("exitunselected.png", true)));
			}
			
			if (loaded)
				return;
			
			Thread.yield();
		}
	}
	
	/**
	 * Shows the menu
	 */
	public void show()
	{
		for (int i = 0; i < elems.length; i++)
		{
			elems[i].setVisible(true);
		}
	}
	
	/**
	 * Hides the menu
	 */
	public void hide()
	{
		for (int i = 0; i < elems.length; i++)
		{
			elems[i].setVisible(false);
		}
	}
	
	/**
	 * Whether the main menu has loaded the first
	 * map
	 * 
	 * @return Whether the main menu has loaded the
	 * first map
	 */
	public boolean loaded()
	{
		return loaded;
	}
	
	
	public void invokeClose()
	{
		hide();
		Main.getGuiHandler().remove("background");
		Main.getGuiHandler().remove("newGame");
		Main.getGuiHandler().remove("settingsMenu");
		Main.getGuiHandler().remove("exitMenu");
		loaded = true;
	}
}
