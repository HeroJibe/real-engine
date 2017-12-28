package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import core.CompressedEntity;
import core.Entity;
import main.Main;

/**
 * The <code>Save</code> class saves the current
 * game into a .save file that can be re-loaded
 * via this class to resume the game that was playing
 * 
 * @author Ethan Vrhel
 */
public final class Save 
{
	/**
	 * Constant indicating a successful save/load
	 */
	public static final int SUCCESS = 0;
	
	/**
	 * Constant indicating a failed save/load
	 */
	public static final int FAIL = 1;
	
	private Save() {}
	
	/**
	 * Saves a game under the designated game
	 * 
	 * @param name The name of the save
	 * @return The save status (<code>Save.SUCCESS</code> or 
	 * <code>Save.FAIL</code>)
	 */
	public static int save(String name)
	{
		File fie = new File("game\\save\\" + name + ".save");
		
		if (! fie.exists())
		{
			try 
			{
				fie.createNewFile();
			} 
			catch (IOException e) 
			{
				return FAIL;
			}
		}
		
		PrintWriter out;		
		try 
		{
			out = new PrintWriter(fie);
		} 
		catch (FileNotFoundException e) 
		{
			return FAIL;
		}
		
		out.println(Main.getMapName() + ".map");
		
		if (Main.getPlayer() == null)
		{
			out.close();
			return SUCCESS;
		}
		if (Main.getPlayer().getPlayerEntity() == null)
		{
			out.close();
			return SUCCESS;
		}
		
		CompressedEntity comp = new CompressedEntity(Main.getPlayer().getPlayerEntity());
		String compStr = comp.toString();
		out.println(compStr);
		
		out.close();
		return SUCCESS;
	}
	
	/**
	 * Loads from a save file by its name
	 * 
	 * @param name The name of the save file
	 * @return The load status (<code>Save.SUCCESS</code> or 
	 * <code>Save.FAIL</code>)
	 */
	public static int load(String name)
	{
		File fie = new File("game\\saves\\" + name + ".save");
		if (! fie.exists())
			return FAIL;
		
		Scanner in;
		try 
		{
			in = new Scanner(fie);
		}
		catch (FileNotFoundException e) 
		{
			return FAIL;
		}
		
		Main.getPlayer().getPlayerEntity().setX(0);
		Main.getPlayer().getPlayerEntity().setY(0);
		
		int exit = Main.loadMap(in.nextLine(), 0, 0);
		
		if (exit == -1)
		{
			in.close();
			return FAIL;
		}
		
		if (in.hasNextLine())
		{
			String compressedEnt = in.nextLine();
			CompressedEntity ce = new CompressedEntity(compressedEnt);
			Entity ent = ce.toEntity();
			double x = ent.getX();
			double y = ent.getY();
			double xvel = ent.getXVel();
			double yvel = ent.getYVel();
			
			Main.getPlayer().getPlayerEntity().setX(x);
			Main.getPlayer().getPlayerEntity().setY(y);
			Main.getPlayer().getPlayerEntity().setXVel(xvel);
			Main.getPlayer().getPlayerEntity().setYVel(yvel);
		}
		
		in.close();
		return SUCCESS;
	}
}