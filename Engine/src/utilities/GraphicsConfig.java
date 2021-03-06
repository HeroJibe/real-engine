/**
 * Handles graphics configurations
 * 
 * @author Ethan Vrhel
 */

package utilities;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import main.Main;

public class GraphicsConfig 
{
	/**
	 * Sets the graphics settings from a file
	 * 
	 * @param graphics
	 */
	public static void loadGraphics(File graphics)
	{		
		try 
		{
			Scanner in = new Scanner(graphics);
			int lines = 0;
			while (in.hasNextLine())
			{
				lines++;
				in.nextLine();
			}
			in.close();
			
			if (lines == 0)
			{
				return;
			}
			
			in = new Scanner(graphics);
			for (int i = 0; i < lines; i++)
			{
				String line = in.nextLine();
				String delims = " = ";
				String tokens[] = line.split(delims);
				
				if (tokens[0].equals("mat_reflections"))
				{
					if (tokens[1].equals("none"))
					{
						Main.getSettingsWindow().getVideoSettings().water.setSelectedItem("No Reflections");
					}
					else if (tokens[1].equals("static"))
					{
						Main.getSettingsWindow().getVideoSettings().water.setSelectedItem("Static Reflections");
					}
					else if (tokens[1].equals("fast"))
					{
						Main.getSettingsWindow().getVideoSettings().water.setSelectedItem("Fast Dynamic Reflections");
					}
					else if (tokens[1].equals("full"))
					{
						Main.getSettingsWindow().getVideoSettings().water.setSelectedItem("Full Dynamic Reflections");
					}
				}
				else if (tokens[0].equals("mat_animations"))
				{
					if (tokens[1].equals("true"))
					{
						Main.getSettingsWindow().getVideoSettings().animation.setSelectedItem("Yes");
					}
					else if (tokens[1].equals("false"))
					{
						Main.getSettingsWindow().getVideoSettings().animation.setSelectedItem("No");
					}
				}
				else if (tokens[0].equals("r_lod"))
				{
					if (tokens[1].equals("low"))
					{
						Main.getSettingsWindow().getVideoSettings().lod.setSelectedItem("Low");
					}
					else if (tokens[1].equals("med"))
					{
						Main.getSettingsWindow().getVideoSettings().lod.setSelectedItem("Medium");
					}
					else if (tokens[1].equals("high"))
					{
						Main.getSettingsWindow().getVideoSettings().lod.setSelectedItem("High");
					}
				}
				else if (tokens[0].equals("r_particles"))
				{
					if (tokens[1].equals("none"))
					{
						Main.getSettingsWindow().getVideoSettings().particle.setSelectedItem("None");
					}
					else if (tokens[1].equals("half"))
					{
						Main.getSettingsWindow().getVideoSettings().particle.setSelectedItem("Half");
					}
					else if (tokens[1].equals("full"))
					{
						Main.getSettingsWindow().getVideoSettings().particle.setSelectedItem("All");
					}
				}
				else if (tokens[0].equals("fps_max"))
				{
					if (tokens[1].equals("0"))
					{
						Main.getSettingsWindow().getVideoSettings().fps.setSelectedItem("Unlimited");
					}
					else if (tokens[1].equals("15"))
					{
						Main.getSettingsWindow().getVideoSettings().fps.setSelectedItem("15");
					}
					else if (tokens[1].equals("30"))
					{
						Main.getSettingsWindow().getVideoSettings().fps.setSelectedItem("30");
					}
					else if (tokens[1].equals("60"))
					{
						Main.getSettingsWindow().getVideoSettings().fps.setSelectedItem("60");
					}
					else if (tokens[1].equals("120"))
					{
						Main.getSettingsWindow().getVideoSettings().fps.setSelectedItem("120");
					}
				}
			}
			in.close();
		} 
		catch (FileNotFoundException e) 
		{
			Main.println("Invalid video config file.", Color.RED);
		}
	}
	
	public static void loadProfile(String name)
		throws FileNotFoundException
	{
		File prof = new File("config\\profiles\\" + name + ".prof");
		if (! prof.exists())
		{
			throw new FileNotFoundException();
		}
		
		loadGraphics(prof);
	}
	
	/**
	 * Writes the graphics configuration to a file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void generateFile()
		throws FileNotFoundException, IOException
	{
		File video = new File("config\\graphics.config");
		if (! video.exists())
			video.createNewFile();
		
		PrintStream out = new PrintStream(video);
		
		if (Main.getSettingsWindow().getVideoSettings().water.getSelectedItem().equals("No Reflections"))
		{
			out.println("mat_reflections = none");
		}
		else if (Main.getSettingsWindow().getVideoSettings().water.getSelectedItem().equals("Static Reflections"))
		{
			out.println("mat_reflections = static");
		}
		else if (Main.getSettingsWindow().getVideoSettings().water.getSelectedItem().equals("Fast Dynamic Reflections"))
		{
			out.println("mat_reflections = fast");
		}
		else if (Main.getSettingsWindow().getVideoSettings().water.getSelectedItem().equals("Full Dynamic Reflections"))
		{
			out.println("mat_reflections = full");
		}
		
		if (Main.getSettingsWindow().getVideoSettings().animation.getSelectedItem().equals("No"))
		{
			out.println("mat_animations = false");
		}
		else if (Main.getSettingsWindow().getVideoSettings().animation.getSelectedItem().equals("Yes"))
		{
			out.println("mat_animations = true");
		}
		
		if (Main.getSettingsWindow().getVideoSettings().lod.getSelectedItem().equals("Low"))
		{
			out.println("r_lod = low");
		}
		else if (Main.getSettingsWindow().getVideoSettings().lod.getSelectedItem().equals("Medium"))
		{
			out.println("r_lod = med");
		}
		else if (Main.getSettingsWindow().getVideoSettings().lod.getSelectedItem().equals("High"))
		{
			out.println("r_lod = high");
		}
		
		if (Main.getSettingsWindow().getVideoSettings().particle.getSelectedItem().equals("None"))
		{
			out.println("r_particles = none");
		}
		else if (Main.getSettingsWindow().getVideoSettings().particle.getSelectedItem().equals("Half"))
		{
			out.println("r_particles = half");
		}
		else if (Main.getSettingsWindow().getVideoSettings().particle.getSelectedItem().equals("All"))
		{
			out.println("r_particles = full");
		}
		
		if (Main.getSettingsWindow().getVideoSettings().fps.getSelectedItem().equals("Unlimited"))
		{
			out.println("fps_max = 0");
		}
		else
		{
			out.println("fps_max = " + Main.getSettingsWindow().getVideoSettings().fps.getSelectedItem());
		}
		
		out.close();
	}
}
