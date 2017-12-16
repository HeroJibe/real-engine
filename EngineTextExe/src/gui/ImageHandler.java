package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.Random;

import main.Main;

public class ImageHandler
{
	
	private boolean[] s;
	private int[] x;
	private int[] y;
	private int[] w;
	private int[] h;
	private Color[] c;
	private Image[] im;
	private String[] text;
	private float[] size;
	private Font[] font;
	private int cacheSizeDyn = 0;
	private int numImage = 0;
	private int cacheSize;
	
	private Random r;
	
	public ImageHandler()
	{
		cacheSize = 256;
		s = new boolean[cacheSize];
		x = new int[cacheSize];
		y = new int[cacheSize];
		w = new int[cacheSize];
		h = new int[cacheSize];
		c = new Color[cacheSize];
		im = new Image[cacheSize];
		r = new Random();
	}
	
	public ImageHandler(int cacheSize)
	{
		this.cacheSize = cacheSize;
		s = new boolean[cacheSize];
		x = new int[cacheSize];
		y = new int[cacheSize];
		w = new int[cacheSize];
		h = new int[cacheSize];
		c = new Color[cacheSize];
		im = new Image[cacheSize];
		r = new Random();
	}
	
	public void addToCache(int nx, int ny, int nw, int nh, Color nc)
	{		
		for (int i = 0; i < cacheSize; i++)
    	{
    		if (! s[i])
    		{
    			x[i] = nx;
    			y[i] = ny;
    			w[i] = nw;
    			h[i] = nh;
    			c[i] = nc;
    			s[i] = true;
    			if (i != cacheSizeDyn - 1)
    				cacheSizeDyn++;
    			numImage++;
    			return;
    		}
    	}
		Main.println("Image cache not big enough");
	}
	
	public void addToCache(int nx, int ny, Image image, Color wireframe)
	{
		for (int i = 0; i < cacheSize; i++)
    	{
    		if (! s[i])
    		{
    			x[i] = nx;
    			y[i] = ny;
    			im[i] = image;
    			s[i] = true;
    			c[i] = wireframe;
    			if (i != cacheSizeDyn - 1)
    				cacheSizeDyn++;
    			numImage++;
    			return;
    		}
    	}
		Main.println("Image cache not big enough");
	}
	
	public void addToCache(int nx, int ny, String ntext)
	{
		//Main.println("tesssssss");
		for (int i = 0; i < cacheSize; i++)
    	{
    		if (! s[i])
    		{
    			x[i] = nx;
    			y[i] = ny;
    			text[i] = ntext;
    			s[i] = true;
    			if (i != cacheSizeDyn - 1)
    				cacheSizeDyn++;
    			numImage++;
    			return;
    		}
    	}
		Main.println("Image cache not big enough");
	}
	
	public void removeFromCache(int i)
	{
		try
		{
			s[i] = false;
			numImage--;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void clearCache()
	{
		for (int i = 0; i < cacheSize; i++)
		{
			s[i] = false;
			numImage = 0;
		}
		cacheSizeDyn = 0;
	}
	
	public int getX(int i)
	{
		return x[i];
	}
	
	public int getY(int i)
	{
		return y[i];
	}
	
	public int getW(int i)
	{
		return w[i];
	}
	
	public int getH(int i)
	{
		return h[i];
	}
	
	public Color getColor(int i)
	{
		return c[i];
	}
	
	public Image getImage(int i)
	{
		return im[i];
	}
	
	public int getDynSize()
	{
		return cacheSizeDyn;
	}
	
	public boolean isInUse(int i)
	{
		return s[i];
	}
	
	public int getNumImage()
	{
		return numImage;
	}
	
	public int getCacheSize()
	{
		return cacheSize;
	}
	
	public String getText(int i)
	{
		return text[i];
	}
	
	public float getSize(int i)
	{
		return size[i];
	}
	
	public Font getFont(int i)
	{
		return font[i];
	}
}
