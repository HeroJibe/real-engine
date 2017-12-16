package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import core.MapLoader;
import gui.GameWindow;
import main.Main;
import wizards.BrushWizard;

public class MapDialog
	extends JFrame
	implements Runnable
{
	public static final int MAX_MAPS = 256;
	public static int INSTANCES = 0;
	
	GameWindow window;
	String[] dataTypes;
	String[] mapNames;
	File[] maps;
	Frame fr;
	
	public MapDialog()
	{
		this.window = Main.getGameWindow();
	}
	
	public void closeMe()
	{
		setVisible(false);
	}
	
	public void openMe()
	{
		setVisible(true);
	}

	@SuppressWarnings("static-access")
	public void run() 
	{
		INSTANCES++;
		Main.println("started", Color.CYAN);
		maps = Main.getResourceHandler().getMaps();
		int numMaps = 0;
		
		for (int i = 0; i < maps.length; i++)
		{
			if (maps[i] != null)
			{
				if (maps[i].exists())
				{
					numMaps++;
				}
			}
		}
		mapNames = new String[numMaps];
		
		int offset = 0;
		for (int i = 0; i < mapNames.length; i++)
		{
			if (maps[i] != null)
			{
				if (maps[i].exists())
				{
					mapNames[i - offset] = maps[i].getName();
				}
				else
				{
					offset++;
				}
			}
			else
			{
				offset++;
			}
		}
		
		dataTypes = mapNames;
		fr = new Frame();
		fr.toFront();
		fr.setVisible(true);
	}
	
	public void show()
	{
		fr.setVisible(true);
		INSTANCES++;
	}
	
	private class Frame
		extends JFrame
		implements WindowListener
	{		
		private static final long serialVersionUID = -5867217212876395767L;
		
		public Panel panel;
		public InformationArea area;
		
		public Frame()
		{
			super("Open Map");
			GridBagConstraints g = new GridBagConstraints();
			GridBagLayout gLayout = new GridBagLayout();
			gLayout.setConstraints(this, g);
			setLayout(gLayout);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setSize(490, 370);
			setResizable(false);
			//setAlwaysOnTop(true);
			
			panel = new Panel();
			area = new InformationArea();			
			
			g.gridx = 0;
			g.gridy = 0;
			add(panel, g);
			
			g.gridx = 1;
			g.gridy = 0;
			add(area, g);
		}
		
		private class Panel
			extends JPanel
			implements ActionListener
		{
			private static final long serialVersionUID = 5268639804507339907L;
			
			JList<String> list;
			JButton button;
			
			public Panel()
			{
				GridBagConstraints g = new GridBagConstraints();
				GridBagLayout gLayout = new GridBagLayout();
				gLayout.setConstraints(this, g);
				setLayout(gLayout);
				
				list = new JList<String>(dataTypes);
				list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				list.setVisibleRowCount(-1);
				list.setSize(290, 450);
				JScrollPane scrollPane = new JScrollPane(list);
				scrollPane.setPreferredSize(new Dimension(400, 160));
				
				JLabel label = new JLabel("Open");
				
				button = new JButton("Open");
				
				Border border = BorderFactory.createTitledBorder("Existing Maps in directory root/maps/");
				setBorder(border);
				
				g.gridx = 0;
				g.gridy = 0;
				add(label, g);
				
				g.gridx = 1;
				g.gridy = 1;
				//add(area, g);
				
				g.gridx = 0;
				g.gridy = 1;
				add(list, g);
				
				g.gridx = 0;
				g.gridy = 2;
				add(button, g);
				
				button.addActionListener(this);
				list.addMouseListener(new MouseAdapter() {
			         public void mouseClicked(MouseEvent me) {
			            if (me.getClickCount() == 1) {
			            	getData(list.getSelectedValue());
			            }
			         }
			      });
			}
			
			public String getData(String map)
			{
				area.area.setText("Loading...");
				if (map != null)
				{
					String toReturn = "";
					int brushes = MapLoader.getNumBrushes(map);
					int triggers = MapLoader.getNumTriggers(map);
					int entities = MapLoader.getNumEntities(map);
					String name = MapLoader.getName(map);
					if (brushes != -1 && triggers != -1 && entities != -1 && name != null)
					{
						toReturn = toReturn + "Name: " + name + "\n";
						toReturn = toReturn + "Brushes: " + brushes + "\n";
						toReturn = toReturn + "Triggers: " + triggers + "\n";
						toReturn = toReturn + "Entities: " + entities + "\n";
					}
					else
					{
						toReturn = "Error loading preview.";
					}
					area.area.setText(toReturn);
					return toReturn;
				}
				else
				{
					area.area.setText("");
					return "";
				}
			}

			public void actionPerformed(ActionEvent e) 
			{	
				int exit = Main.loadMap(list.getSelectedValue(), 0, 0);
				if (exit == 0)
				{
					JOptionPane.showMessageDialog(this, "Error loading map");
				}
				else
				{
					BrushWizard bWizard = new BrushWizard();
					bWizard.add();
					if (! Main.getEditorMainThread().isAlive())
						Main.getEditorMainThread().start();
					if (! Main.getElementEditorThread().isAlive())
						Main.getElementEditorThread().start();
					Main.getElementEditor().load(bWizard);
					Main.loadingMap = false;
				}
			}
		}
		
		private class InformationArea
			extends JPanel
		{
			private static final long serialVersionUID = 6198992214526999256L;
			
			public JTextArea area;

			public InformationArea()
			{
				GridBagConstraints g = new GridBagConstraints();
				GridBagLayout gLayout = new GridBagLayout();
				gLayout.setConstraints(this, g);
				setLayout(gLayout);
				
				JList<String> list = new JList<String>(dataTypes);
				list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				list.setVisibleRowCount(-1);
				list.setSize(300, 450);
				JScrollPane scrollPane = new JScrollPane(list);
				scrollPane.setPreferredSize(new Dimension(400, 160));
				
				JLabel label = new JLabel("Select Map Element");
				
				JButton button = new JButton("Select");
				
				Border border = BorderFactory.createTitledBorder("Preview");
				setBorder(border);
				
				area = new JTextArea(11, 15);
				area.setEditable(false);
				area.setFocusable(false);
				//JScrollPane scroll = new JScrollPane(area); 
				
				g.gridx = 0;
				g.gridy = 0;
				add(area, g);
			}
		}
		
		public void windowClosing(WindowEvent e)
		{
			INSTANCES--;
			fr.setVisible(false);
		}

		public void windowOpened(WindowEvent e) 
		{
			
		}

		public void windowClosed(WindowEvent e) 
		{
		
		}

		public void windowIconified(WindowEvent e) 
		{
			
		}

		public void windowDeiconified(WindowEvent e) 
		{
			
		}

		public void windowActivated(WindowEvent e) 
		{
			
		}

		public void windowDeactivated(WindowEvent e) 
		{
			
		}
	}
}
