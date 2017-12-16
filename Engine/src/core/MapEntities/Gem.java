/**
 * Gems are the collectibles in-game
 * 
 * @author Ethan Vrhel
 */

package core.MapEntities;

import java.awt.Color;
import java.awt.Point;
import java.io.File;

import javax.sound.sampled.Clip;

import core.Animation;
import core.Entity;
import core.GameAmbientSound;
import core.GameSound;
import core.ResourceHandler;
import game.GameMain;
import main.Main;

public class Gem
	extends MapEntity
{	
	/**
	 * The gem's x scale
	 */
	public static final int SCALE_X = 16;
	
	/**
	 * The gem's y scale
	 */
	public static final int SCALE_Y = 16;
	
	/**
	 * Constant for the green gem
	 */
	public static final int GREEN_GEM = 0;
	
	/**
	 * Constant for the blue gem
	 */
	public static final int BLUE_GEM = 1;
	
	/**
	 * Constant for the yellow gem
	 */
	public static final int YELLOW_GEM = 2;
	
	private int gemType;
	private Entity gemEntity;
	private String name;
	private GameSound gem;
	private Animation anim;
	
	public Gem(int gemType, int x, int y, String uniqueName)
	{
		super(uniqueName, ON_PLAYER_COLLIDE, x, y, SCALE_X, SCALE_Y);
		this.gemType = gemType;
		Main.getResourceHandler();
		File image = new File(ResourceHandler.defaultTexture);
		if (gemType == BLUE_GEM)
			image = Main.getResourceHandler().getByName("Blue1.png");
		else if (gemType == GREEN_GEM)
			image = Main.getResourceHandler().getByName("Green1.png");
		else if (gemType == YELLOW_GEM)
			image = Main.getResourceHandler().getByName("Yellow1.png");
		
		if (gemType == GREEN_GEM)
			gemEntity = new Entity(Entity.DYNAMIC, image, 
				SCALE_X, SCALE_Y, false, "gem", x, y, Integer.MAX_VALUE / 4, SCALE_X, SCALE_Y, 100, false);
		else if (gemType == BLUE_GEM)
			gemEntity = new Entity(Entity.DYNAMIC, image, 
				SCALE_X, SCALE_Y, false, "gemBlue", x, y, Integer.MAX_VALUE / 4, SCALE_X, SCALE_Y, 100, false);
		else if (gemType == YELLOW_GEM)
			gemEntity = new Entity(Entity.DYNAMIC, image, 
				SCALE_X, SCALE_Y, false, "gemYellow", x, y, Integer.MAX_VALUE / 4, SCALE_X, SCALE_Y, 100, false);
		
		gemEntity.setCollisions(true);
		gemEntity.setKinetic(false);
		collisionEntity = gemEntity;
		name = uniqueName;
		if (! pickedUp())
		{
			Entity t = Main.getEntityHandler().getEntityByName(uniqueName);
			if (t == null)
			{
				Main.getEntityHandler().addDynamicEntity(gemEntity);
				Animation anim = null;
				add();
				if (gemType == GREEN_GEM)
					anim = Main.getAnimationHandler().getByName("gem.anim");
				else if (gemType == BLUE_GEM)
					anim = Main.getAnimationHandler().getByName("gemBlue.anim");
				else
					anim = Main.getAnimationHandler().getByName("gemYellow.anim");
				this.anim = new Animation(anim);
				Main.getAnimationHandler().addAnimation(this.anim);
				this.anim.setEntity(gemEntity);
				this.anim.shouldRun(true);
				this.anim.limitAnimation(false);
				Point p  =new Point();
				p.setLocation(gemEntity.getX(), gemEntity.getY());
				gem = new GameAmbientSound("gem " + name, Main.getResourceHandler().getByName("gem.wav"), GameSound.EFFECT);
				gemEntity.setVisible(true);
			}
			else
			{
				t.setVisible(true);
			}
		}
		else
		{
			Main.getEntityHandler().removeEntity(gemEntity);
			Main.getEntityHandler().removeEntity(collisionEntity);
		}
	}

	public void function() 
	{
		if ((exists() && gemEntity != null && ! pickedUp()) || (Main.getEntityHandler().exists(gemEntity) && pickedUp()))
		{
			pickup();
			GameMain.gems++;
			gemEntity.setVisible(false);
			while (Main.getEntityHandler().exists(gemEntity))
				Main.getEntityHandler().removeEntity(gemEntity);
			gem.playSound();
		}
	}
	
	protected boolean shouldStartThread()
	{
		for (int i = 0; i < GameMain.existingGems.length; i++)
		{
			if (GameMain.existingGems[i] != null)
			{
				if (GameMain.existingGems[i].equals(name))
				{
					return ! GameMain.pickedUpGems[i];
				}
			}
		}
		return true;
	}
	
	private void pickup()
	{
		for (int i = 0; i < GameMain.existingGems.length; i++)
		{
			if (GameMain.existingGems[i] != null)
			{
				if (GameMain.existingGems[i].equals(name))
				{
					GameMain.pickedUpGems[i] = true;
					GameMain.existingGems[i] = name;
					//Main.println("picked up " + this);
					return;
				}
			}
		}
	}
	
	private boolean pickedUp()
	{
		for (int i = 0; i < GameMain.existingGems.length; i++)
		{
			if (GameMain.existingGems[i] != null)
			{
				if (GameMain.existingGems[i].equals(name))
				{
					return GameMain.pickedUpGems[i];
				}
			}
		}
		return false;
	}
	
	private void add()
	{
		for (int i = 0; i < GameMain.existingGems.length; i++)
		{
			if (GameMain.existingGems[i] == null)
			{
				GameMain.existingGems[i] = name;
				GameMain.pickedUpGems[i] = false;
				return;
			}
			else if (GameMain.existingGems[i].equals(""))
			{
				GameMain.existingGems[i] = name;
				GameMain.pickedUpGems[i] = false;
				return;
			}
		}
		Main.println("Could not add gem " + name + " (cache not big enough)", Color.RED);
	}
	
	private boolean exists()
	{
		for (int i = 0; i < GameMain.existingGems.length; i++)
		{
			if (GameMain.existingGems[i] != null)
			{
				if (GameMain.existingGems[i].equals(name))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns the gem's name
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return "Gem Entity type"  + gemType + " (" + x + ", " + y + ")";
	}
}
