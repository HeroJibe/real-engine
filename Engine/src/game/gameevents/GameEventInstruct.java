package game.gameevents;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import core.Entity;
import core.GameEvent;
import main.Main;
import core.guiElements.GuiImage;
import utilities.KeyBinds;

public class GameEventInstruct
	extends GameEvent
{
	private boolean called;
	
	public GameEventInstruct()
	{
		super("GameEventInstruct");
		called = false;
	}

	@Override
	public void update()
	{
		if (called)
			return;
		BufferedImage buff = new BufferedImage(380, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buff.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 512, 64);
		g.setColor(Color.BLACK);
		Font font = g.getFont();
		Font newFont = font.deriveFont(font.getSize() * 2.5f);
		g.setFont(newFont);
		g.drawString("Left: " + KeyBinds.LEFT + " Right: " + KeyBinds.RIGHT + " Jump: " + KeyBinds.UP, 0, 25);
		GuiImage gui = new GuiImage("help", Entity.STATIC, buff, 200, 800, 0, 380 * 2, 64);
		Main.getGuiHandler().add(gui);
		disable();
		called = true;
	}
}
