	package gui;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import core.NavigationMesh;
import main.Main;

public class GameWindow extends JFrame
{
	GraphicsDevice device;
	Window window;
	DrawPane pane;
	public JOptionPane optionPane;
	private int leftBounds;
	private int rightBounds;
	private int topBounds;
	private int bottomBounds;
	
	private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private static DisplayMode dm = gd.getDisplayMode();
	public static final int XRES_GL = gd.getDisplayMode().getWidth();
	public static final int YRES_GL = gd.getDisplayMode().getHeight();
	public static final int REFRESH_RATE = dm.getRefreshRate();
	
	public Color backgroundColor;
	public boolean drawBackground;
	public float fontSize = 1.5F;
	
	private int xRes;
	private int yRes;
	private String console[] = new String[20];
	private Color consoleColor[] = new Color[20];
	private String typedConsole = "";
	private int typeInterval = 10;
	private int typeFrame = 0;
	private boolean consoleOpen = false;
	
	private boolean borderless;
	
	private ImageHandler imgHandle;
	
	private short num = 0;
	
	public long renderUpdates = 0;
	public boolean lazyConsole = true;
	
	public GameWindow(String name, int x, int y, boolean borderless, int cacheSize)
	{
        super(name);
        
        imgHandle = new ImageHandler(cacheSize);
        optionPane = new JOptionPane();

        //you can set the content pane of the frame 
        //to your custom class.

        pane = new DrawPane();
        setContentPane(pane);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(x, y);
        xRes = x;
        yRes = y;
        leftBounds = 0;
        rightBounds = x;
        topBounds = 0;
        bottomBounds = y;
        this.borderless = borderless;
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        if (borderless)
        	setUndecorated(true);

        setVisible(true);
        
        for (int i = 0; i < consoleColor.length; i++)
        	consoleColor[i] = Color.BLACK;
   }

    //create a component that you can actually draw on.
    private class DrawPane extends JPanel
    {
    	private Object rendering = RenderingHints.VALUE_RENDER_DEFAULT;
    	private Object textAntialias = RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
    	private Object antialias = RenderingHints.VALUE_ANTIALIAS_DEFAULT;
    	private Object interpolate = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
    	
    	RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_RENDERING, rendering);
    	
    	private Font currentFont;
    	private Font newFont;
    	
    	public DrawPane()
    	{
    		super(false);
    	}
    	
    	public void updateRenderingHints()
    	{
    		rh.clear();
    		rh.add(new RenderingHints(RenderingHints.KEY_RENDERING, rendering));
    		rh.add(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, textAntialias));
    		rh.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, antialias));
    		rh.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION, interpolate));
    	}
    	
    	// Draws everything
    	@SuppressWarnings("unused")
		public void paintComponent(Graphics g)
    	{
    		super.paintComponent(g);
    		
    		renderUpdates++;
    		
    		updateRenderingHints();
    		Graphics2D g2 = (Graphics2D)g;
    		g2.setRenderingHints(rh);
    	    currentFont = g.getFont();
    	    newFont = currentFont.deriveFont(currentFont.getSize() * fontSize);
    	    g.setFont(newFont);
    		
    		if (drawBackground) {
    			g.setColor(backgroundColor);
    			g.fillRect(leftBounds, topBounds, rightBounds - leftBounds, bottomBounds - topBounds);
    		}
    		
    		//draw on g here e.g.
    		for (int i = 0; i < imgHandle.getDynSize(); i++)
    		{
    			if (imgHandle.isInUse(i))
    			{    				
    				g.setColor(imgHandle.getColor(i));
    				if (imgHandle.getImage(i) != null)
    				{
    					if (! Main.DRAW_WIREFRAME)
    						g.drawImage(imgHandle.getImage(i), imgHandle.getX(i), imgHandle.getY(i), null);
    					else
    					{
    						int x = imgHandle.getX(i);
    						int y = imgHandle.getY(i);
    						int width = imgHandle.getImage(i).getWidth(pane);
    						int height = imgHandle.getImage(i).getHeight(pane);
    						g.drawRect(x, y, width, height);
    						g.drawLine(x, y + height, x + width, y);
    					}
    					
    				}
    				else if (imgHandle.getText(i) != null)
    				{
    					/*
    					if (imgHandle.getFont(i) != null)
    					{
    						g.setFont(imgHandle.getFont(i).deriveFont(imgHandle.getFont(i).getSize() * imgHandle.getSize(i)));
    					}
    					*/
    					
    					g.drawString(imgHandle.getText(i), imgHandle.getY(i), imgHandle.getY(i));
    				}
    				else
    				{
    					g.fillRect(imgHandle.getX(i), imgHandle.getY(i), imgHandle.getW(i), imgHandle.getH(i));
    				}
    			}
    		}
    		
    		if (num == 0)
    			num = 1;
    		else
    			num = 0;
    		
    		if (Main.DEBUG && ! Main.loadingMap)
    		{
    			g.setColor(Color.BLACK);
    			g.drawString(Main.debugMessage, (int) (10 * Main.resolutionScaleX), (int) (1070 * Main.resolutionScaleY));
    			g.setColor(Color.WHITE);
    			g.drawString(Main.debugMessage, (int) (9 * Main.resolutionScaleX), (int) (1069 * Main.resolutionScaleY));
    			g.setColor(Color.GREEN);
    			g.drawLine(leftBounds, topBounds, leftBounds, bottomBounds);
    			g.drawLine(leftBounds, topBounds, rightBounds, topBounds);
    			g.drawLine(rightBounds, topBounds, rightBounds, bottomBounds);
    			g.drawLine(leftBounds, bottomBounds, rightBounds, bottomBounds);
    		}
    		if (Main.loadingMap)
    		{
    			g.setColor(Color.BLACK);
    			g.fillRect(leftBounds, topBounds, rightBounds, bottomBounds);
    			g.setColor(Color.WHITE);
    			g.drawString("Loading...  " + Main.loadMessage, (int) (10 * Main.resolutionScaleX), (int) (1070 * Main.resolutionScaleY));    		
    		}
    		if (Main.DEBUG || Main.loadingMap)
    		{
    			int offset = 0;
    			typeFrame++;
    			for (int i = 0; i < console.length / (int) fontSize; i++)
    			{
    				offset = (int) (i * 10 * fontSize);
    				if (console[i] != null)
    				{
    					g.setColor(Color.BLACK);
    					g.drawString(console[i], 10, 200 - offset);
    					g.setColor(consoleColor[i]);
    					g.drawString(console[i], 9, 200 - offset);
    				}
    			}
    			
    			if (Main.DRAW_NAVMESH && ! Main.loadingMap)
    			{
        			for (int x = 0; x < (int) (1920 / NavigationMesh.NAVMESH_SIZE); x ++)
        			{
        				int rep = 0;
        				for (int y = 0; y < (int) (1080 / NavigationMesh.NAVMESH_SIZE); y ++)
        				{
        					if (Main.getPlayer().navMesh.isWalkable(x * NavigationMesh.NAVMESH_SIZE, y * NavigationMesh.NAVMESH_SIZE))
        					{
        						g.setColor(Color.GREEN);
        						g.drawRect(x * NavigationMesh.NAVMESH_SIZE, y * NavigationMesh.NAVMESH_SIZE, NavigationMesh.NAVMESH_SIZE, NavigationMesh.NAVMESH_SIZE);
        					}
        					else
        					{
          						//g.setColor(Color.RED);
        						//g.drawRect(x * NavigationMesh.NAVMESH_SIZE, y * NavigationMesh.NAVMESH_SIZE, NavigationMesh.NAVMESH_SIZE, NavigationMesh.NAVMESH_SIZE);
        					}
        				}
        			}
    			}
    			
    			if (consoleOpen)
    			{
    				offset = (int) (10 * fontSize);
    				if (typeFrame > typeInterval)
    				{
    					g.setColor(Color.BLACK);
    					g.drawString("> " + typedConsole + "_", 9, 200 + offset);
    					g.setColor(Color.WHITE);
    					g.drawString("> " + typedConsole + "_", 10, 200 + offset);
    					if (typeFrame > typeInterval * 2)
    						typeFrame = 0;
    				}
    				else
    				{
    					g.setColor(Color.BLACK);
    					g.drawString("> " + typedConsole, 9, 200 + offset);
    					g.setColor(Color.WHITE);
    					g.drawString("> " + typedConsole, 10, 200 + offset);
    				}
    			}
    			
    			//testAntialias();
    		}
    	}
    	
    	private void testAntialias()
    	{
    		BufferedImage image = new BufferedImage(XRES_GL, YRES_GL, BufferedImage.TYPE_4BYTE_ABGR);
    		Graphics2D g2 = image.createGraphics();
    		int color = image.getColorModel().getRGB(1);
    		Main.println("" + color, (new Color(color)));
    		g2.dispose();
    	}
    }

    public void paint()
    {
    	pane.repaint();
    }
    
    public void addToCache(int nx, int ny, int nw, int nh, Color nc)
    {
    	imgHandle.addToCache(nx, ny, nw, nh, nc);   	
    }
    
    public void addToCache(int nx, int ny, Image image, Color wireframe)
    {
    	imgHandle.addToCache(nx, ny, image, wireframe);
    }
    
    public void addToCache(int nx, int ny, String text)
    {
    	imgHandle.addToCache(nx, ny, text);
    }
    
    public ImageHandler getHandler()
    {
    	return imgHandle;
    }
    
    public int getTopBounds()
    {
    	return topBounds;
    }
    
    public void setTopBounds(int bounds)
    {
    	topBounds = bounds;
    }
    
    public int getBottomBounds()
    {
    	return bottomBounds;
    }
    
    public void setBottomBounds(int bounds)
    {
    	bottomBounds = bounds;
    }
    
    public int getRightBounds()
    {
    	return rightBounds;
    }
    public void setRightBounds(int bounds)
    {
    	rightBounds = bounds;
    }
    
    public int getLeftBounds()
    {
    	return leftBounds;
    }
    
    public void setLeftBounds(int bounds)
    {
    	leftBounds = bounds;
    }
    
    public int getScreenResX()
    {
    	return xRes;
    }
    
    public int getScreenResY()
    {
    	return yRes;
    }
    
    public ImageHandler getImageHandler()
    {
    	return imgHandle;
    }
    
    public void addToConsole(String str, Color c)
    {
    	for (int i = console.length - 1; i > 0; i--)
    	{
    		console[i] = console[i - 1];
    		consoleColor[i] = consoleColor[i - 1];
    	}
    	console[0] = str;
    	consoleColor[0] = c;
    	if (! lazyConsole)
    		Main.getEntityHandler().invokeUpdate();
    }
    
    public void type(String key)
    {
    	if (! consoleOpen)
    		return;
    	
    	if (key.equals("Backspace"))
    	{
    		if (typedConsole.length() - 1 >= 0)
    			typedConsole = typedConsole.substring(0, typedConsole.length() - 1);
    		return;
    	}
    	String typed = "";
    	
    	typed = key;
    	
    	key = key.toLowerCase();
    	
    	if (key.length() == 1)
    		typedConsole = typedConsole + key;
    	else if (key.equals("space"))
    		typedConsole = typedConsole + " ";
    	else if (key.equals("enter"))
    	{
    		addToConsole("> " + typedConsole, Color.WHITE);
    		typedConsole = "";
    	}
    }
    
    public RenderingHints getRenderingHints()
    {
    	return pane.rh;
    }
}
