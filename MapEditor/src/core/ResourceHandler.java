package core;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import main.Main;

public class ResourceHandler
{	
	public static final String debugTexture 	= "debug\\debugtexture.png";
	public static Image debugTextureImage;
	
	private Image[] images;
	private File[] files;
	private File[] mapFiles;
	private Clip[] audioClips;
	private int numFiles = 0;
	private int numImages = 0;
	private int numSounds = 0;
	private int maxResources;
	
	private int triedResources = 0;
	private int loadedResources = 0;
	
	public ResourceHandler(int maxResources)
	{
		this.maxResources = maxResources;
		images = new Image[maxResources];
		files = new File[maxResources];
		mapFiles = new File[maxResources];
		audioClips = new Clip[maxResources];
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
		else if (resc.toString().contains(".wav"))
		{
			file = new File("resources/sound/" + resc);
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
	
	// Imports all resources
	public void autoImportResources()
	{
		File folder = new File("resources/textures/");
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].isFile())
			{
				triedResources++;
				addFileResource(files[i]);
			}
		}
		
		folder = new File("resources/animations/");
		files = folder.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].isFile())
			{
				triedResources++;
				addFileResource(files[i]);
			}
		}
		
		folder = new File("resources/sound/");
		files = folder.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].isFile())
			{
				triedResources++;
				addFileResource(files[i]);
			}
		}
		
		folder = new File("maps/");
		files = folder.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].isFile() && ! files[i].toString().contains(".entitydata") && ! files[i].toString().contains(".dat"))
			{
				triedResources++;
				addFileResource(files[i]);
				mapFiles[i] = files[i];
			}
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
	
	// Converts a file to a sound at an index
	public int convertToSound(int i)
	{
		Clip audioClip = null;
		try
		{
			Main.println("Converted resource: " + files[i].toString());
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(files[i]);
			audioClip = AudioSystem.getClip();
			audioClip.open(audioInputStream);
		} 
		catch (IOException | LineUnavailableException | UnsupportedAudioFileException e)
		{
			Main.println("Error while converting: " + files[i].toString() + " to a sound.", Color.RED);
			return 0;
		}
		if (audioClip != null) 
		{
			audioClips[i] = audioClip;
			numSounds++;
			return 1;
		}
		return 0;
	}
	
	// Converts a file into an animation
	public int convertToAnimation(int i)
	{
		try
		{
			AnimationLoader.loadFile(files[i].getName());
			AnimationLoader.load();
			Main.println("Converted resource: " + files[i].toString());
			return 1;
		}
		catch (Exception e)
		{
			return 0;
		}
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
	
	// Converts all sound files into Objects
	public void autoConvertToSound()
	{
		int num = 0;
		int nums = 0;
		for (int i = 0; i < numFiles; i++)
		{
			if (files[i].toString().contains(".wav"))
			{
				int ret = convertToSound(i);
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
	

	// Converts all animation files into Objects
	public void autoConvertToAnimation()
	{
		int num = 0;
		int nums = 0;
		for (int i = 0; i < numFiles; i++)
		{
			if (files[i].toString().contains(".anim"))
			{
				int ret = convertToAnimation(i);
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
		else if (fileName.contains(".anim"))
		{
			fileName = "resources\\animations\\" + fileName;
		}
		else if (fileName.contains(".wav"))
		{
			fileName = "resources\\sound\\" + fileName;
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
			else if (fileName.contains(".wav"))
			{
				fileName = "resources\\sound\\" + fileName;
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
	
	// Gets an image at an index
	public Clip getSound(int i)
	{
		return audioClips[i];
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
	
	public File[] getMaps()
	{
		return mapFiles;
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
