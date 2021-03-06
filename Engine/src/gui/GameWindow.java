package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import core.NavigationMesh;
import core.ResourceHandler;
import main.Main;
import utilities.ResourceMonitor;

/**
 * The GameWindow class draws everything onto the screen
 * 
 * @author Ethan Vrhel
 * @see ImageHandler
 */
public class GameWindow 
	extends JFrame
	implements WindowListener
{
	private static final long serialVersionUID = -1355289629188692967L;
	
	private DrawPane pane;			// The pane to be rendered on
	private int leftBounds;			// The screen's left bounds
	private int rightBounds;		// The screen's right bounds
	private int topBounds;			// The screen's top bounds
	private int bottomBounds;		// The screen's bottom bounds
	
	public static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public static DisplayMode dm = gd.getDisplayMode();
	
	/**
	 * The x resolution
	 */
	public static int XRES_GL = (int) (gd.getDisplayMode().getWidth() / 1 * 1);
	
	/**
	 * The y resolution
	 */
	public static int YRES_GL = (int) (gd.getDisplayMode().getHeight() / 1 * 1);
	
	/**
	 * Using fast text rendering
	 */
	public static boolean FAST_TEXT = false;
	
	/**
	 * The monitor's refresh rate
	 */
	public static final int REFRESH_RATE = dm.getRefreshRate();
	
	/**
	 * The end user's name
	 */
	public static final String USER_NAME = System.getProperty("user.name");
	
	/**
	 * The amount of memory in kilobytes
	 */
	public static final double MEMORY_KB = Runtime.getRuntime().maxMemory() / 1024;
	
	/**
	 * The amount of memory in megabytes
	 */
	public static final double MEMORY_MB = MEMORY_KB / 1024;
	
	/**
	 * The amount of memory in gigabytes
	 */
	public static final double MEMORY_GB = MEMORY_MB / 1024;
	
	/**
	 * The background color
	 */
	public Color backgroundColor;
	
	/**
	 * Whether the background should be drawn
	 */
	public boolean drawBackground = false;
	
	/**
	 * The image background of the scene
	 */
	public BufferedImage background;
	
	/**
	 * The console's font size
	 */
	public float fontSize = 1.5F;
	
	/**
	 * The indents caused by the window border
	 */
	public Insets indents;
	
	private int xRes;
	private int yRes;
	private String console[] = new String[20];
	private Color consoleColor[] = new Color[20];
	private String typedConsole = "";
	private int typeInterval = 10;
	private int typeFrame = 0;
	private boolean consoleOpen = false;
	
	//private BufferStrategy bs;
	
	private ImageHandler imgHandle;
	
	/**
	 * The amount of render updates
	 */
	public long renderUpdates = 0;
	
	/**
	 * Whether the console should render lazily
	 */
	@Deprecated
	public boolean lazyConsole = true;
	
	/**
	 * Whether the buffered rendering method should be used
	 */
	public boolean bufferedRendering = false;
	
	/**
	 * The amount of time spent rendering
	 */
	@Deprecated
	public long renderTime = 0;
	
	/**
	 * The lighting Image
	 */
	public volatile Image lighting;
	
	/**
	 * Whether the renderer is rendering
	 */
	public boolean updating;
	
	/**
	 * The new Image after buffered rendering
	 */
	public volatile Image disp;
	
	/**
	 * The Image's x offset
	 */
	public volatile double dispOffsetX;
	
	/**
	 * The Image's y offset
	 */
	public volatile double dispOffsetY;
	
	/**
	 * Whether the engine should render
	 */
	public volatile boolean render;
	
	/**
	 * The camera's position
	 */
	private double cameraX = 0;
	
	/**
	 * The camera's position
	 */
	private double cameraY = 0;
	
	/**
	 * The camera's x bounds
	 * (max, min)
	 * 
	 */
	public volatile int[] cameraXBounds = {Integer.MIN_VALUE, Integer.MAX_VALUE};
	
	/**
	 * The camera's y bounds
	 * (max, min)
	 * 
	 */
	public volatile int[] cameraYBounds = {Integer.MIN_VALUE, Integer.MAX_VALUE};
	
	/**
	 * The renderer's x clip
	 */
	public volatile int clipX = 0;
	
	/**
	 * The renderer's y clip
	 */
	public volatile int clipY = 0;
	
	//public volatile Entity focusEntity = null;
	
	public GameWindow(String name, int x, int y, boolean borderless, int cacheSize)
	{
        super(name);
        updating = false;
        imgHandle = new ImageHandler(cacheSize);
        new JOptionPane();

        //you can set the content pane of the frame 
        //to your custom class.

        pane = new DrawPane();
        setContentPane(pane);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);

        //setSize(x, y);
        xRes = x;
        yRes = y;
        leftBounds = 0;
        rightBounds = x;
        topBounds = 0;
        bottomBounds = y;
        setResizable(false);

        toFront();
        if (borderless)
        	setUndecorated(true);
        
        try
        {
        	File icon = new File("resources\\textures\\icon.png");
        	Image imageIcon = ImageIO.read(icon);
        	setIconImage(imageIcon);
        }
        catch (Exception e)
        {
        	Main.println("Failed to set icon.", Color.RED);
        }

        //setVisible(true);
        
        for (int i = 0; i < consoleColor.length; i++)
        	consoleColor[i] = Color.BLACK;
        
        dispOffsetX = 0;
    	dispOffsetY = 0;
    	render = true;
    	
    	pack();    	
    	
    	setLocationRelativeTo(null);
    	indents = getInsets();
    	//JFrame temp = new JFrame();
    	//temp.pack();
    //	Insets insets = temp.getInsets();
    	//temp = null;
    	//this.setSize(new Dimension(insets.left + insets.right + XRES_GL,
    	 //            insets.top + insets.bottom + YRES_GL));
    	//this.setVisible(true);
    	//this.setResizable(false);
	    ////Main.println("inset x: " + (insets.left + insets.right));
	    //Main.println("inset y: " + (insets.bottom + insets.top));
	    //setSize(insets.left + insets.right + XRES_GL, insets.bottom + insets.top + YRES_GL)
	    
	    //pack();
	}
	
	/**
	 * Creates the buffered rendering view
	 */
	public void createView()
	{
		BufferedImage disp = new BufferedImage(GameWindow.XRES_GL, GameWindow.YRES_GL, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = disp.createGraphics();
		for (int i = 0; i < imgHandle.getDynSize(); i++)
		{
			if (imgHandle.isInUse(i))
			{    				
				g.setColor(imgHandle.getColor(i));
				if (imgHandle.getImage(i) != null)
				{
					if (! Main.drawWireframe)
					{    						
						g.drawImage(imgHandle.getImage(i), imgHandle.getX(i), imgHandle.getY(i), null);
					}
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
					g.drawString(imgHandle.getText(i), imgHandle.getY(i), imgHandle.getY(i));
				}
				else
				{
					g.fillRect(imgHandle.getX(i), imgHandle.getY(i), imgHandle.getW(i), imgHandle.getH(i));
				}
			}
		}
		
		disp = Main.getShaderHandler().cummulativeShade(disp);
		this.disp = disp.getScaledInstance(XRES_GL, YRES_GL, Image.SCALE_FAST);
		g.dispose();
	}

    /**
     * The panel on which everything is rendered
     * 
     * @author Ethan Vrhel
     */
    private class DrawPane extends JPanel
    {
		private static final long serialVersionUID = -4256441938046104593L;
		private Object rendering = RenderingHints.VALUE_RENDER_SPEED;
    	private RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_RENDERING, rendering);
    	
    	private Font currentFont;
    	private Font newFont;
    	
    	public DrawPane()
    	{
    		super(true);
    		setPreferredSize(new Dimension(XRES_GL, YRES_GL));
    	}
    	
    	/**
    	 * Draws everything
    	 */
    	@SuppressWarnings({ "unused", "deprecation" })
		public void paintComponent(Graphics g)
    	{
    		super.paintComponent(g);
    		long start = System.currentTimeMillis();
    		renderUpdates++;
    		renderTime++;
    		
    		/*
    		if (focusEntity != null)
    		{
	    		cameraX = -1 * (int) Math.ceil(focusEntity.getX() - (GameWindow.XRES_GL / 2));
				cameraY = -1 * (int) Math.ceil(focusEntity.getY() - (GameWindow.YRES_GL / 2));
    		}
    		*/
    		
    		/*
    	    currentFont = g.getFont();
    	    newFont = currentFont.deriveFont(currentFont.getSize() * fontSize);
    	    g.setFont(newFont);
    	    */
    		
    		bufferedRendering = false;
    		if (Main.getShaderHandler() != null)
	    		if (Main.getShaderHandler().getNumEnabled() > 0)
	    			bufferedRendering = true;

    		if (bufferedRendering)
    		{
    			if (render)
    			{
		    		createView();
		    		if (disp != null)
		    		{
		    			g.drawImage(disp, (int) dispOffsetX, (int) dispOffsetY, null);
		    		}
    			}
    		}
    		else
    		{
    			for (int i = 0; i < imgHandle.getDynSize(); i++)
    			{
    				if (imgHandle.isInUse(i))
    				{    				
    					g.setColor(imgHandle.getColor(i));
    					if (imgHandle.getImage(i) != null)
    					{
    						if (! Main.drawWireframe)
    						{    	
    							if (imgHandle.useOffset(i))
    							{
    								//if (imgHandle.getX(i) + cameraX - imgHandle.getImage(i).getWidth(null) < XRES_GL - clipX / 2 && 
    								//		imgHandle.getX(i) + cameraX + imgHandle.getImage(i).getWidth(null) > clipX / 2)
    								//{
    								//	if (imgHandle.getY(i) + cameraY - imgHandle.getImage(i).getHeight(null) < YRES_GL - clipY / 2 && 
    								//			imgHandle.getY(i) + cameraY + imgHandle.getImage(i).getHeight(null) > clipY / 2)
    								//	{
    										g.drawImage(imgHandle.getImage(i), imgHandle.getX(i) + (int) cameraX, imgHandle.getY(i) + (int) cameraY, null);
    								//	}
    								//}
    							}
								else
								{
    								g.drawImage(imgHandle.getImage(i), imgHandle.getX(i) + (int) cameraX, imgHandle.getY(i) + (int) cameraY, null);
								}
    						}
    						else
    						{
    							int x = imgHandle.getX(i);
    							int y = imgHandle.getY(i);
    							int width = imgHandle.getImage(i).getWidth(pane);
    							int height = imgHandle.getImage(i).getHeight(pane);
    							g.drawRect(x + (int) cameraX, y + (int) cameraY, width, height);
    							g.drawLine(x + (int) cameraX, y + height + (int) cameraY, x + (int) cameraX + width, y + (int) cameraY);
    						}
    						
    					}
    					else if (imgHandle.getText(i) != null)
    					{    					
    						g.drawString(imgHandle.getText(i), imgHandle.getX(i), imgHandle.getY(i));
    					}
    					else
    					{
    						g.drawImage(ResourceHandler.defaultTextureImage.getScaledInstance(imgHandle.getW(i), imgHandle.getH(i), Image.SCALE_FAST), 
    								imgHandle.getX(i) + (int) cameraX, imgHandle.getY(i) + (int) cameraY, null);
    					}
    				}
    			
    			}
    		}

    		if (Main.DEBUG && ! Main.loadingMap)
    		{
    			g.setColor(Color.BLACK);
    			g.drawString(Main.debugMessage, (int) (10 * Main.resolutionScaleX), (int) (1070 * Main.resolutionScaleY));
    			g.setColor(Color.WHITE);
    			g.drawString(Main.debugMessage, (int) (9 * Main.resolutionScaleX), (int) (1069 * Main.resolutionScaleY));
    		}
    		if (Main.loadingMap)
    		{
    			g.setColor(Color.BLACK);
    			g.fillRect(leftBounds, topBounds, rightBounds, bottomBounds);
    			g.setColor(Color.WHITE);
    			g.drawString("Loading...  " + Main.loadMessage, (int) (10 * Main.resolutionScaleX), (int) (1070 * Main.resolutionScaleY));    		
    		}
    		if (Main.DEBUG)
    		{
    			int offset = 0;
    			typeFrame++;
    			for (int i = 0; i < console.length / (int) fontSize; i++)
    			{
    				offset = (int) (i * 10 * fontSize);
    				if (console[i] != null)
    				{
    					if (! FAST_TEXT)
    	    			{
	    					g.setColor(Color.BLACK);
	    					g.drawString(console[i], 10, 200 - offset);
    	    			}
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
        				}
        			}
    			}
    			
    			
    			if (consoleOpen)
    			{
    				offset = (int) (10 * fontSize);
    				if (typeFrame > typeInterval)
    				{
    					if (! FAST_TEXT)
    					{
	    					g.setColor(Color.BLACK);
	    					g.drawString("> " + typedConsole + "_", 9, 200 + offset);
    					}
    					g.setColor(Color.WHITE);
    					g.drawString("> " + typedConsole + "_", 10, 200 + offset);
    					if (typeFrame > typeInterval * 2)
    						typeFrame = 0;
    				}
    				else
    				{
    					if (! FAST_TEXT)
    					{
	    					g.setColor(Color.BLACK);
	    					g.drawString("> " + typedConsole, 9, 200 + offset);
    					}
    					g.setColor(Color.WHITE);
    					g.drawString("> " + typedConsole, 10, 200 + offset);
    				}
    			}
    		}
    		g.dispose();
    		if (Main.getCurrentResourceMonitor() != null)
				Main.getCurrentResourceMonitor().increment(ResourceMonitor.Type.RENDER, System.currentTimeMillis() - start);
    		updating = false;
    	}
    }

    /**
     * Renders everything
     */
    public synchronized void paint()
    {
    	updating = true;
    	if (! render)
    	{
    		updating = false;
			return;
    	}
    	
    	//if (! Main.gamePaused)
    		//pane.paintImmediately(0, 0, XRES_GL, YRES_GL);
    	//else
    		pane.repaint();
    }
    
    /**
     * Adds a rectangle to the cache
     * 
     * @param nx The x position
     * @param ny The y position
     * @param nw The width
     * @param nh The height
     * @param nc The color
     */
    public void addToCache(int nx, int ny, int nw, int nh, Color nc)
    {
    	imgHandle.addToCache(nx, ny, nw, nh, nc);   	
    }
    
    /**
     * Adds an <code>Image</code> to the cache
     * 
     * @param nx The x position
     * @param ny The y position
     * @param image The <code>Image</code>
     * @param useOffset Whether the renderer should use the screen offset
     * @param wireframe The wireframe color
     */
    public void addToCache(int nx, int ny, Image image, boolean useOffset, Color wireframe)
    {
    	imgHandle.addToCache(nx, ny, image, useOffset, wireframe);
    }
    
    /**
     * Adds text to the cache
     * 
     * @param nx The x position
     * @param ny The y position
     * @param text The text
     */
    public void addToCache(int nx, int ny, String text)
    {
    	imgHandle.addToCache(nx, ny, text);
    }
    
    /**
     * Returns the ImageHandler
     * 
     * @return The <code>ImageHandler</code>
     */
    public ImageHandler getHandler()
    {
    	return imgHandle;
    }
    
    /**
     * Returns the top screen bounds
     * 
     * @return The top screen bounds
     */
    public int getTopBounds()
    {
    	return topBounds;
    }
    
    /**
     * Sets the top screen bounds
     * 
     * @param bounds The bounds
     */
    public void setTopBounds(int bounds)
    {
    	topBounds = bounds;
    }
    
    /**
     * Returns the bottom screen bounds
     * 
     * @return The bottom screen bounds
     */
    public int getBottomBounds()
    {
    	return bottomBounds;
    }
    
    /**
     * Sets the bottom screen bounds
     * 
     * @param bounds The bounds
     */
    public void setBottomBounds(int bounds)
    {
    	bottomBounds = bounds;
    }
    
    /**
     * Returns the right screen bounds
     * 
     * @return The right screen bounds
     */
    public int getRightBounds()
    {
    	return rightBounds;
    }
    
    /**
     * Sets the right screen bounds
     * 
     * @param bounds The bounds
     */
    public void setRightBounds(int bounds)
    {
    	rightBounds = bounds;
    }
    
    /**
     * Returns the left screen bounds
     * 
     * @return The left screen bounds
     */
    public int getLeftBounds()
    {
    	return leftBounds;
    }
    
    /**
     * Sets the left screen bounds
     * 
     * @param bounds The bounds
     */
    public void setLeftBounds(int bounds)
    {
    	leftBounds = bounds;
    }
    
    /**
     * Returns the screen x resolution
     * 
     * @return The x resolution
     */
    public int getScreenResX()
    {
    	return xRes;
    }
    
    /**
     * Returns the screen y resolution
     * 
     * @return The y resolution
     */
    public int getScreenResY()
    {
    	return yRes;
    }
    
    /**
     * Adds a <code>String</code> to the console
     * 
     * @param str The <code>String</code>
     * @param c The color
     */
    public void addToConsole(String str, Color c)
    {
    	for (int i = console.length - 1; i > 0; i--)
    	{
    		console[i] = console[i - 1];
    		consoleColor[i] = consoleColor[i - 1];
    	}
    	console[0] = str;
    	consoleColor[0] = c;
    }
    
    /**
     * Types a character
     * 
     * @param key A key as a <code>String</code>
     */
    @Deprecated
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
    
    /**
     * Returns the <code>RenderingHints</code>
     * 
     * @return The <code>RenderingHints</code>
     */
    public RenderingHints getRenderingHints()
    {
    	return pane.rh;
    }
    
    /**
     * Updates the window
     */
    @Deprecated
    public void update()
    {
    	if (Main.runInWindow)
    	{
    		Main.resolutionScaleX = (double) Main.windowSizeX / 1920.0;
	        Main.resolutionScaleY = (double) Main.windowSizeY / 1080.0;
    		setExtendedState(JFrame.NORMAL);
    		setSize(Main.windowSizeX, Main.windowSizeY);
    		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    	}
    	else
    	{
    		Main.resolutionScaleX = (double) GameWindow.XRES_GL / 1920.0;
	        Main.resolutionScaleY = (double) GameWindow.YRES_GL / 1080.0;
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setSize(XRES_GL, YRES_GL);
    	}
    }
    
    @Override
    public String toString()
    {
    	return "GameWindow: " + XRES_GL + "x" + YRES_GL;
    }
    
    /**
     * Returns the camera's x position
     * 
     * @return The camera's x position
     */
    public double getCameraX()
    {
    	return cameraX;
    }
    
    /**
     * Returns the camera's y position
     * 
     * @return The camera's y position
     */
    public double getCameraY()
    {
    	return cameraY;
    }
    
    /**
     * Sets the camera's x position
     * 
     * @param x The x position
     */
    public synchronized void setCameraX(double x)
    {
    	cameraX = x;
    }
    
    /**
     * Sets the camera's y position
     * 
     * @param y The y position
     */
    public synchronized void setCameraY(double y)
    {
    	cameraY = y;
    }

	@Override
	public void windowOpened(WindowEvent e)
	{
		
	}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		Main.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) 
	{
		
	}

	@Override
	public void windowIconified(WindowEvent e) 
	{	

		
	}

	@Override
	public void windowDeiconified(WindowEvent e) 
	{
		
	}

	@Override
	public void windowActivated(WindowEvent e) 
	{
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) 
	{
		
	}
}
