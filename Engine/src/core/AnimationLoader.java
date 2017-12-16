/**
 * The AnimationLoader class provides a way
 * of reading .anim files and translating them
 * into Animation objects.
 * 	
 * @author 	Ethan Vrhel
 */

package core;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import main.Main;

public class AnimationLoader 
{
	public static final int ENTITY_ANIM = 0;	// Constant for an Entity Animation
	public static final int GUI_ANIM = 1;		// Constant for a GUI Animation
	public static final int RAW_ANIM = 2;		// Constant for a Raw Animation
	
	private static String name = "";			// The Animation's name
	private static String elementName = "";		// The element's name
	private static int type;					// The Animation's type
	private static File animFile;				// The .anim file
	
	/**
	 * Loads a file into the AnimationLoader memory
	 * 
	 * @param animName
	 */
	public static void loadFile(String animName)
	{
		name = animName;
		if (animName.contains(".anim"))
			animFile = new File("resources\\animations\\" + name);
		else
			animFile = new File("resources\\animations\\" + name + ".anim");
	}
	
	/**
	 * Loads the Animation from the cached file and nulls
	 * it when completed.
	 */
	public static void load()
	{
		type = -1;
		int i = 0;
		try
		{
			Scanner in = new Scanner(animFile);
			if (in.nextLine().equals("Animation"))
			{
				int rate = -1;
				String[] images = null;
				while (in.hasNextLine())
				{
					String line = in.nextLine();
					String delims = " = ";
					String[] tokens = line.split(delims);
					if (tokens[0].equals("Type"))
					{
						if (tokens[1].equals("entity"))
							type = ENTITY_ANIM;
						else if (tokens[1].equals("guiElement"))
							type = GUI_ANIM;
						else if (tokens[1].equals("raw"))
							type = RAW_ANIM;
						else
						{
							Main.println("Error at " + animFile.toString() + ": " + (i + 1) + ": unkown type: " + tokens[1], Color.RED);
						}
					}
					else if (tokens[0].equals("Rate"))
					{
						rate = Integer.parseInt(tokens[1]);
					}
					else if (tokens[0].equals("Element"))
					{
						elementName = tokens[1];
					}
					else if (tokens[0].equals("Frames:"))
					{
						i++;
						Scanner in2 = new Scanner(animFile);
						int frames = 0;
						while (in2.hasNextLine())
						{
							if (in2.nextLine().equals("Frames:"))
								break;
						}
						while (in2.hasNextLine())
						{
							in2.nextLine();
							frames++;
						}
						in2.close();
						images = new String[frames];
						int j = 0;
						while (in.hasNextLine())
						{
							images[j] = in.nextLine();
							j++;
						}
						break;
					}
					else
					{
						Main.println("Error at " + animFile.toString() + ": " + (i + 1) + ": unkown key: " + tokens[0], Color.RED);
						break;
					}
					i++;
				}
				
				BufferedImage[] imgs = new BufferedImage[images.length];
				int problems = 0;
				for (i = 0; i < imgs.length; i++)
				{
					if (Main.getResourceHandler().getIndexByName(images[i], true) >= 0)
						imgs[i] = (BufferedImage) (Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName(images[i], true)));
					else
					{
						Main.println("Error: Failed to load " + images[i] + " into animation " + name, Color.RED);
						problems++;
					}
				}
				
				Animation anim = null;
				if (type == ENTITY_ANIM)
				{
					anim = new Animation(name, Main.getEntityHandler().getEntityByName(elementName), imgs, rate);
				}
				else if (type == GUI_ANIM)
				{
					anim = new Animation(name, Main.getGuiHandler().getElementByName(elementName), imgs, rate);
				}
				else if (type == RAW_ANIM)
				{
					anim = new Animation(name, imgs, rate);
				}
				else
				{
					Main.println("Error at " + animFile.toString() + " bad animation type.", Color.RED);
				}
				
				
				if (problems > 0)
					Main.println("WARNING! " + problems + " problems with animation " + name, Color.YELLOW);
				
				Main.getAnimationHandler().addAnimation(anim);	
			}
			else
			{
				Main.println("Error at " + animFile.toString() + ": " + "Invalid file header", Color.RED);
			}
			in.close();
		}
		catch (Exception e)
		{
			if (i != 0)
				Main.println("Error at " + animFile.toString() + ":" + (i + 1) + ": " + e, Color.RED);
			else
				Main.println("Error at " + animFile.toString() + ": " + e, Color.RED);
		}
		
		animFile = null;
	}
}
