package core;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import main.Main;

public final class Map 
{
	private String name;
	
	private ArrayList<String> map;
	private ArrayList<String> ids;
	private ArrayList<MapElement> elems;
	
	public Map()
	{
		map = new ArrayList<String>();
		ids = new ArrayList<String>();
		elems = new ArrayList<MapElement>();
	}
	
	public void add(String line, String id)
		throws IllegalArgumentException
	{
		if (ids.contains(id))
			throw new IllegalArgumentException("Duplicate ID!");
		map.add(line);
		ids.add(id);
	}
	
	public void add(MapElement elem, String id)
		throws IllegalArgumentException
	{
		if (ids.contains(id))
			throw new IllegalArgumentException("Duplicate ID!");
		elems.add(elem);
		ids.add(id);
	}
	
	public void insert(int lineNum, String line, String id)
		throws IllegalArgumentException
	{
		if (ids.contains(id))
			throw new IllegalArgumentException("Duplicate ID!");
		map.add(line);
		ids.add(id);
	}
	
	public void insert(int lineNum, MapElement elem, String id)
		throws IllegalArgumentException
	{
		if (ids.contains(id))
			throw new IllegalArgumentException("Duplicate ID!");
		elems.add(elem);
		ids.add(id);
	}
	
	public String getByID(String id)
	{
		int line = ids.indexOf(id);
		return map.get(line);
	}
	
	public void remove(int lineNum)
	{
		map.remove(lineNum);
		ids.remove(lineNum);
	}
	
	public int getNumLines()
	{
		return elems.size() + map.size();
	}
	
	public String getNextAvaliableID()
	{
		int test = 0;
		String test2 = "" + test;
		while (ids.contains(test2))
		{
			test++;
			test2 = "" + test;
		}
		return test2;
	}
	
	public String getName()
	{
		return name;
	}
	
	public MapElement getElementByName(String name)
	{
		for (int i = 0; i < ids.size(); i++)
		{
			if (ids.get(i) != null)
			{
				if (ids.get(i).equals(name))
				{
					return elems.get(i);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Exports the map into a file
	 * 
	 * @param file the file to export to
	 * @return <code>false</code> if it failed and <code>true</code> if it succeeded
	 */
	public boolean export(File file)
	{
		if (file == null)
			return false;
		
		File datFile = new File(file + ".editordata");
		
		if (! datFile.exists())
		{
			try
			{
				datFile.createNewFile();
			}
			catch (IOException e)
			{
				return false;
			}
		}
		
		PrintStream out = null;
		PrintStream out2 = null;
		try
		{
			out = new PrintStream(file);
			out2 = new PrintStream(datFile);
		}
		catch (IOException e)
		{
			return false;
		}
		
		for (int i = 0; i < getNumLines(); i++)
		{
			if (elems.get(i) != null)
				out.println(elems.get(i).export());
			out2.println(ids.get(i));
		}
		
		out.close();
		out2.close();
		return true;
	}
	
	/**
	 * Loads a map from a file
	 * @param file the file to be loaded from
	 * @return <code>false</code> if it failed and <code>true</code> if it succeeded
	 */
	public boolean load(File file)
	{
		if (file == null)
			return false;
		
		File datFile = new File(file + ".editordata");
		this.name = file.getName();
		
		boolean shouldGen = false;
		if (! datFile.exists())
		{
			System.out.println("No editor data file!  Generating...");
			try
			{
				datFile.createNewFile();
			}
			catch (IOException e)
			{
				return false;
			}
			shouldGen = true;
		}
		
		Scanner in = null;
		Scanner in2 = null;
		PrintStream out2 = null;
		try
		{
			in = new Scanner(file);
			in2 = new Scanner(datFile);
			if (shouldGen)
				out2 = new PrintStream(datFile);
		}
		catch (IOException e)
		{
			return false;
		}
		
		try
		{
			int line = 0;
			String line2 = "" + line;
			while (in.hasNextLine())
			{
				map.add(in.nextLine());
				if (shouldGen)
				{
					out2.println(line);
					ids.add(line2);
				}
				else
					ids.add(in.nextLine());
				line++;
				line2 = "" + line;
			}
		}
		catch (NumberFormatException e)
		{
			in.close();
			in2.close();
			out2.close();
			return false;
		}
		
		in.close();
		in2.close();
		if (out2 != null)
			out2.close();
		Main.setCurrentMap(this);
		return true;
	}
}