package core;

import java.io.File;
import java.util.Scanner;

import main.Main;

/**
 * Materials can provide translation
 * from textures to a sound file for
 * the player to play when it is walked
 * on
 * 
 * @author Ethan Vrhel
 * @see MaterialHandler
 */
public class Material 
{
	private String name;
	private GameSound sound;
	private String[] textures;
	
	public Material(String name)
	{
		this.name = name;
		Main.getMaterialHandler().addMaterial(this);
	}
	
	public Material(String name, GameSound sound)
	{
		this.name = name;
		this.sound = sound;
		Main.getMaterialHandler().addMaterial(this);
	}
	
	public Material(String name, GameSound sound, String[] textures)
	{
		this.name = name;
		this.sound = sound;
		this.textures = textures;
	}
	
	/**
	 * Returns the sound
	 * @return The sound
	 */
	public GameSound getSound()
	{
		return sound;
	}
	
	/**
	 * Returns the name
	 * @return The name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the textures
	 * @return The textures
	 */
	public String[] getTextures()
	{
		return textures;
	}
	
	@Override
	public boolean equals(Object mat)
	{
		if (mat == null)
			return false;
		return toString().equals(mat.toString());
	}
	
	@Override
	public String toString()
	{
		return "[Material]: " + name;
	}
	
	/**
	 * Loads a <code>Material</code> from a
	 * .mat file
	 * 
	 * @param file The .mat file
	 * @return The loaded <code>Material</code>
	 */
	public static Material load(File file)
	{
		try
		{
			Scanner in = new Scanner(file);
			String[] tokens;
			int length = 0;
			while (in.hasNextLine())
			{
				length++;
				in.nextLine();
			}
			in.close();
			tokens = new String[length];
			in = new Scanner(file);
			for (int i = 0; in.hasNextLine(); i++)
			{
				tokens[i] = in.nextLine();
			}
			in.close();
			
			String name = null;
			String sound = null;
			String[] textures = null;
			if (tokens[0].equals("name"))
			{
				String[] args = tokens[0].split(" ");
				name = args[1];
			}
			
			if (tokens[1].equals("Sound"))
			{
				String[] args = tokens[0].split(" ");
				sound = args[1];
			}
			
			if (tokens[2].equals("Textures:"))
			{
				in = new Scanner(file);
				in.nextLine();
				in.nextLine();
				in.nextLine();
				
				int textLength = 0;
				while (in.hasNextLine())
				{
					textLength++;
					in.nextLine();
				}
				textures = new String[textLength];
				in.close();
				in = new Scanner(file);
				
				for (int i = 0; in.hasNextLine(); i++)
				{
					textures[i] = in.nextLine();
				}
			}
			
			return new Material(name, Main.getGameSoundHandler().getByName(sound), textures);
		}
		catch (Exception e)
		{
			Main.println("Failed to create material: " + file.toString() + " " + e);
			return null;
		}
	}
}
