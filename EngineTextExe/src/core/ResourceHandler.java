package core;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.Main;

public class ResourceHandler
{
	public static final String debugTexture 	= "debug\\debugtexture.png";
	public static final String defaultTexture 	= "debug\\notexture.png";
	public static final String debugStart 		= "debug\\start.png";
	public static final String debugEnd			= "debug\\end.png";
	public static final String luxel			= "debug\\luxel.png";
	public static final String light			= "debug\\light.png";
	public static final String luxelIllum		= "debug\\luxelillum.png";
	public static Image debugTextureImage;
	public static Image defaultTextureImage;
	public static Image debugStartImage;
	public static Image debugEndImage;
	public static Image luxelImage;
	public static Image lightImage;
	public static Image luxelIllumImage;
	
	private Image[] images;
	private File[] files;
	private int numFiles = 0;
	private int numImages = 0;
	private int maxResources;
	
	private int triedResources = 0;
	private int loadedResources = 0;
	
	public ResourceHandler(int maxResources)
	{
		this.maxResources = maxResources;
		images = new Image[maxResources];
		files = new File[maxResources];
		this.maxResources = maxResources;
		
		if ((new File("resources\\textures\\" + debugTexture)).exists())
		{
			addResource(debugTexture);
			try 
			{
				debugTextureImage = ImageIO.read(files[0]);
			}
			catch (IOException e) 
			{
				Main.println("Error: failed converting debug texture.", Color.RED);
			}
		}
		else
		{
			Main.println("Error: " + debugTexture + " does not exist.  Verify game files.", Color.RED);
			System.exit(0);
		}
		
		if ((new File("resources\\textures\\" + defaultTexture)).exists())
		{
			addResource(defaultTexture);
			try 
			{
				defaultTextureImage = ImageIO.read(files[1]);
			}
			catch (IOException e) 
			{
				Main.println("Error: failed converting default texture.", Color.RED);
			}
		}
		else
		{
			Main.println("Error: " + defaultTexture + " does not exist.  Verify game files.", Color.RED);
			System.exit(0);
		}
		
		if ((new File("resources\\textures\\" + debugStart)).exists())
		{
			addResource(defaultTexture);
			try 
			{
				debugStartImage = ImageIO.read(files[1]);
			}
			catch (IOException e) 
			{
				Main.println("Error: failed converting default texture.", Color.RED);
			}
		}
		else
		{
			Main.println("Error: " + debugStart + " does not exist.  Verify game files.", Color.RED);
			System.exit(0);
		}
		
		if ((new File("resources\\textures\\" + debugEnd)).exists())
		{
			addResource(defaultTexture);
			try 
			{
				debugEndImage = ImageIO.read(files[1]);
			}
			catch (IOException e) 
			{
				Main.println("Error: failed converting default texture.", Color.RED);
			}
		}
		else
		{
			Main.println("Error: " + debugEnd + " does not exist.  Verify game files.", Color.RED);
			System.exit(0);
		}
		
		if ((new File("resources\\textures\\" + luxel)).exists())
		{
			addResource(luxel);
			try 
			{
				luxelImage = ImageIO.read(files[1]);
			}
			catch (IOException e) 
			{
				Main.println("Error: failed converting default texture.", Color.RED);
			}
		}
		else
		{
			Main.println("Error: " + luxel + " does not exist.  Verify game files.", Color.RED);
			System.exit(0);
		}
		
		if ((new File("resources\\textures\\" + light)).exists())
		{
			addResource(luxel);
			try 
			{
				lightImage = ImageIO.read(files[1]);
			}
			catch (IOException e) 
			{
				Main.println("Error: failed converting default texture.", Color.RED);
			}
		}
		else
		{
			Main.println("Error: " + light + " does not exist.  Verify game files.", Color.RED);
			System.exit(0);
		}
		
		if ((new File("resources\\textures\\" + luxelIllum)).exists())
		{
			addResource(luxelIllum);
			try 
			{
				luxelIllumImage = ImageIO.read(files[1]);
			}
			catch (IOException e) 
			{
				Main.println("Error: failed converting default texture.", Color.RED);
			}
		}
		else
		{
			Main.println("Error: " + luxelIllum + " does not exist.  Verify game files.", Color.RED);
			System.exit(0);
		}
	}
	
	// Finds a file by string and adds it to the resource cache
	public void addResource(String resc)
	{
		triedResources++;
		File file;
		if (resc.toString().contains(".png"))
		{
			 file = new File("resources/textures/" + resc);
		}
		else if (resc.toString().contains(".map"))
		{
			file = new File("maps/" + resc);
		}
		else if (resc.toString().contains(".anim"))
		{
			file = new File("resources/animations/" + resc);
		}
		else
		{
			file = new File(resc);
		}
		if (file.exists())
		{
			addFileResource(file);
		}
		else
		{
			Main.println("Resource: " + file.toString() + " does not exist.", Color.RED);
		}
	}
	
	// Adds a File to the resource cache
	private void addFileResource(File file)
	{
		if (numFiles == files.length)
		{
			Main.println("Resource cache is not big enough!", Color.RED);
			return;
		}
			
		if (file.exists())
		{
			Main.println("Loaded resource: " + file.toString(), Color.YELLOW);
			files[numFiles] = file;
			numFiles++;
			loadedResources++;
		}
		else
		{
			Main.println("Failed while loading resource: " + file.toString() + " : file does not exist.", Color.RED);
		}
	}
	
	// Converts a file to an image at an index
	public int convertToImage(int i)
	{
		Image image = null;
		try
		{
			Main.println("Converted resource: " + files[i].toString());
			image = ImageIO.read(files[i]);
		} 
		catch (IOException e)
		{
			Main.println("IOException while converting: " + files[i].toString() + " to an image.", Color.RED);
			return 0;
		}
		if (image != null) 
		{
			images[i] = image;
			numImages++;
			return 1;
		}
		return 0;
	}
	
	// Converts all pngs to Image objects
	public void autoConvertToImage()
	{
		int num = 0;
		int nums = 0;
		for (int i = 0; i < numFiles; i++)
		{
			if (files[i].toString().contains(".png"))
			{
				int ret = convertToImage(i);
				nums++;
				if (ret == 1)
					num++;
			}
		}
		
		if (num == nums)
			Main.println("Converted " + nums + " resources!", Color.GREEN);
		else
			Main.println("Failed to convert " + (nums - num) + " resources", Color.RED);
	}
	
	// Returns a File by its name
	public File getByName(String fileName)
	{
		if (fileName.contains(".png"))
		{
			 fileName = "resources\\textures\\" + fileName;
		}
		else if (fileName.contains(".map"))
		{
			fileName = "maps\\" + fileName;
		}
		
		for (int i = 0; i < numFiles; i++)
		{			
			if (files[i].toString().equals(fileName))
			{
				return files[i];
			}
		}
		return (new File("null"));
	}
	
	// Gets the index of a file by its name
	public int getIndexByName(String fileName, boolean interpolate)
	{
		if (interpolate)
		{
			if (fileName.contains(".png"))
			{
				fileName = "resources\\textures\\" + fileName;
			}
			else if (fileName.contains(".map"))
			{
				fileName = "maps\\" + fileName;
			}
		}
		
		for (int i = 0; i < numFiles; i++)
		{			
			if (files[i].toString().equals(fileName))
			{
				return i;
			}
		}
		return -1;
	}
	
	// Gets a file at an index
	public File getFile(int i)
	{
		return files[i];
	}
	
	// Gets an image at an index
	public Image getImage(int i)
	{
		return images[i];
	}
	
	public File fromImage(Image img)
	{
		for (int i = 0; i < files.length; i++)
		{
			try
			{
				if (images[i].equals(img))
				{
					return files[i];
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public int getTriedResources()
	{
		return triedResources;
	}
	
	public int getLoadedResources()
	{
		return loadedResources;
	}
}
