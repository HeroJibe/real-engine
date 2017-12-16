package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Main;

public class DebugWindow 
	extends JFrame
{
	private static final long serialVersionUID = -5389314666053179945L;
	
	private JLabel versionInfo;
	
	public GameMonitor gameMonitor;
	
	public DebugWindow()
	{
		GridBagConstraints g = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		layout.setConstraints(this, g);
		setLayout(layout);
		
		versionInfo = new JLabel(Main.ENGINE_NAME + " version " + Main.ENGINE_VERSION_NAME + " build " + Main.ENGINE_BUILD);
		
		gameMonitor = new GameMonitor();
		
		g.gridx = 0;
		g.gridy = 0;
		add(versionInfo, g);
		
		g.gridx = 0;
		g.gridy = 1;
		add(new LoadPanel(), g);
		
		g.gridx = 0;
		g.gridy = 2;
		add(new GeneralPanel(), g);
		
		g.gridx = 1;
		g.gridy = 1;
		add(gameMonitor, g);
		
		setTitle("Engine debug tools");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
		setSize(500, 250);
		this.setLocation(GameWindow.XRES_GL / 2 + 500, GameWindow.YRES_GL / 2 + 250);
		setAlwaysOnTop(true);
		toFront();
	}
	
	/**
	 * The Panel that allows map loading
	 * 
	 * @author Ethan Vrhel
	 *
	 */
	private static class LoadPanel
		extends JPanel
	{
		private static final long serialVersionUID = 8413355743782680196L;
		
		JLabel mapNameLabel;
		JTextField mapName;
		
		JLabel offsetLabel;
		JTextField offsetX;
		JTextField offsetY;
		
		JButton loadButton;
		
		public LoadPanel()
		{
			GridBagConstraints g = new GridBagConstraints();
			GridBagLayout layout = new GridBagLayout();
			layout.setConstraints(this, g);
			setLayout(layout);
			
			mapNameLabel = new JLabel("Map Name:");
			mapName = new JTextField(10);
			loadButton = new JButton("Load");
			
			offsetLabel = new JLabel("Map Offset (x, y): ");
			offsetX = new JTextField(4);
			offsetY = new JTextField(4);
			
			loadButton.addActionListener(new LoadListener());
			
			g.gridx = 0;
			g.gridy = 0;
			add(mapNameLabel, g);
			
			g.gridx = 1;
			g.gridy = 0;
			add(mapName, g);
			
			g.gridx = 0;
			g.gridy = 1;
			add(offsetLabel, g);
			
			g.gridx = 1;
			g.gridy = 1;
			add(offsetX, g);
			
			g.gridx = 2;
			g.gridy = 1;
			add(offsetY, g);
			
			g.gridx = 2;
			g.gridy = 0;
			add(loadButton, g);
		}
		
		/**
		 * The listener for the load button
		 * 
		 * @author Ethan Vrhel
		 */
		private class LoadListener
			implements ActionListener
		{
			public void actionPerformed(ActionEvent e) 
			{
				File mapFile = new File("maps\\" + mapName.getText());
				if (mapFile.exists())
				{
					Main.loadMap(mapName.getText(), 0, 0);
					return;
				}
				
				mapFile = new File("maps\\" + mapName.getText() + ".map");
				if (mapFile.exists())
				{
					Main.loadMap(mapName.getText() + ".map", 0, 0);
					return;
				}
			}		
		}
	}
	
	/**
	 * The Panel for general debug tools
	 * 
	 * @author Ethan Vrhel
	 */
	private static class GeneralPanel
		extends JPanel
	{
		private static final long serialVersionUID = -8357939085193228785L;
		
		JLabel playerPos;
		JTextField pX;
		JTextField pY;
		JButton setGo;
		
		JButton forceClose;
		
		public GeneralPanel()
		{
			GridBagConstraints g = new GridBagConstraints();
			GridBagLayout layout = new GridBagLayout();
			layout.setConstraints(this, g);
			setLayout(layout);
			playerPos = new JLabel("Player (x, y) ");
			pX = new JTextField(3);
			pY = new JTextField(3);
			setGo = new JButton("Go");
			setGo.addActionListener(new ButtonListeners());
			
			forceClose = new JButton("Force Quit");
			forceClose.addActionListener(new ButtonListeners());

			g.gridx = 0;
			g.gridy = 0;
			add(playerPos, g);
			
			g.gridx = 1;
			g.gridy = 0;
			add(pX, g);
			
			g.gridx = 2;
			g.gridy = 0;
			add(pY, g);
			
			g.gridx = 3;
			g.gridy = 0;
			add(setGo, g);
			
			g.gridx = 0;
			g.gridy = 1;
			add(forceClose, g);
		}
		
		/**
		 * The listener for the buttons
		 * 
		 * @author Ethan Vrhel
		 */
		private class ButtonListeners
			implements ActionListener
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (e.getSource() == forceClose)
				{
					System.exit(0);
				}
				else if (e.getSource() == setGo)
				{
					Main.getPlayer().getPlayerEntity().setX(Double.parseDouble(pX.getText()));
					Main.getPlayer().getPlayerEntity().setY(Double.parseDouble(pY.getText()));
				}
			}		
		}
	}
	
	/**
	 * The monitor for the game's state
	 * 
	 * @author Ethan Vrhel
	 */
	public class GameMonitor
		extends JPanel
	{
		private static final long serialVersionUID = 2906048937518894515L;
		
		JLabel fpsText;
		public JTextField fps;
		
		JLabel threadsText;
		public JTextField threads;
		
		JLabel memText;
		public JTextField mem;
		
		public GameMonitor()
		{
			GridBagConstraints g = new GridBagConstraints();
			GridBagLayout layout = new GridBagLayout();
			layout.setConstraints(this, g);
			setLayout(layout);
			
			fpsText = new JLabel(" fps ");
			fps = new JTextField(4);
			fps.setEditable(false);
			fps.setFocusable(false);
			
			threadsText = new JLabel(" threads ");
			threads = new JTextField(2);
			threads.setEditable(false);
			threads.setFocusable(false);
			
			memText = new JLabel(" memory ");
			mem = new JTextField(3);
			mem.setEditable(false);
			mem.setFocusable(false);
			
			g.gridx = 0;
			g.gridy = 0;
			add(fpsText, g);
			
			g.gridx = 1;
			g.gridy = 0;
			add(fps, g);
			
			g.gridx = 0;
			g.gridy = 2;
			add(threadsText, g);
			
			g.gridx = 1;
			g.gridy = 2;
			add(threads, g);
			
			g.gridx = 0;
			g.gridy = 3;
			add(memText, g);
			
			g.gridx = 1;
			g.gridy = 3;
			add(mem, g);
		}
		
		public void run()
		{
			while (true)
			{
				fps.setText(Main.getFpsCounter().getFps() + "");
				threads.setText(Main.getThreadsRunning() + "");
				Thread.yield();
			}
		}
	}
}
