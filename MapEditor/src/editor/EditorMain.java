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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import gui.GameWindow;
import main.Main;

public class EditorMain
	extends JFrame
	implements Runnable
{
	public static final int ELEMENTS = 10;
	public static int INSTANCES = 0;
	
	GameWindow window;
	String[] dataTypes;
	
	public EditorMain()
	{
		this.window = Main.getGameWindow();
		String[] dataTypes = {"Brush", "TiledBrush", "TriggerGameEvent", "TriggerGravity", "TriggerHurt", "TriggerLoad",
				"TriggerMove", "TriggerShader", "TriggerSound", "TriggerStartTrigger", "TriggerStopTrigger",
				"Gem", "InvokeTrigger", "LevelText", "Track"};
		this.dataTypes = dataTypes;
	}

	public void run() 
	{
		INSTANCES++;
		Frame fr = new Frame();
		fr.setVisible(true);
		fr.toFront();
	}
	
	private class Frame
		extends JFrame
	{		
		public Panel panel;
		public InformationArea area;
		
		public Frame()
		{
			super("New Map Element");
			GridBagConstraints g = new GridBagConstraints();
			GridBagLayout gLayout = new GridBagLayout();
			gLayout.setConstraints(this, g);
			setLayout(gLayout);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setSize(490, 370);
			setResizable(false);
			
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
				
				JLabel label = new JLabel("Map Elements");
				
				button = new JButton("Create");
				
				Border border = BorderFactory.createTitledBorder("Select Map Element");
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
			            	click();
			            }
			         }
			      });
			}
			
			public void click()
			{
				JTextArea a = area.area;
				String selected = null;
				int selectedNum = list.getSelectedIndex();
				selected = null;
				if (selectedNum >= 0 && selectedNum < dataTypes.length)
					selected = dataTypes[selectedNum];
				
				if (a != null)
				{
					//System.out.println(selected);
					if (selected != null)
					{
						if (selected.equals("Brush"))
						{
							a.setText("type\n" +
									"texture\n" + 
									"x\n" + 
									"y\n" +
									"z\n" +
									"w\n" +
									"h\n" +
									"solid\n" +
									"mass\n" +
									"kinetic\n" + 
									"name\n");
						}
						else if (selected.equals("TiledBrush"))
						{
							a.setText("type\n" +
									"texture\n" + 
									"x\n" + 
									"y\n" +
									"z\n" +
									"w\n" +
									"h\n" +
									"solid\n" +
									"tx\n" +
									"ty\n");
						}
						else if (selected.equals("TriggerGameEvent"))
						{
							a.setText("type\n" +
									"frequency\n" + 
									"x\n" + 
									"y\n" +
									"z\n" +
									"w\n" +
									"h\n" +
									"target\n" +
									"name\n");
						}
						else if (selected.equals("TriggerGravity"))
						{
							a.setText("type\n" +
									"frequency\n" + 
									"x\n" + 
									"y\n" +
									"z\n" +
									"w\n" +
									"h\n" +
									"gravity\n" +
									"name\n");
						}
						else if (selected.equals("TriggerHurt"))
						{
							a.setText("type\n" +
									"frequency\n" + 
									"x\n" + 
									"y\n" +
									"z\n" +
									"w\n" +
									"h\n" +
									"damage\n" +
									"name\n");
						}
						else if (selected.equals("TriggerLoad"))
						{
							a.setText("type\n" +
									"frequency\n" + 
									"x\n" + 
									"y\n" +
									"z\n" +
									"w\n" +
									"h\n" +
									"map\n" +
									"offsetx\n" +
									"offsety\n" +
									"name\n");
						}
						else if (selected.equals("TriggerMove"))
						{
							a.setText("type\n" +
									"frequency\n" + 
									"x\n" + 
									"y\n" +
									"z\n" +
									"w\n" +
									"h\n" +
									"target\n" +
									"mx\n" +
									"my\n" +
									"delay\n" +
									"name\n");
						}
						else if (selected.equals("TriggerShader"))
						{
							a.setText("type\n" +
									"frequency\n" + 
									"x\n" + 
									"y\n" +
									"z\n" +
									"w\n" +
									"h\n" +
									"shader\n" +
									"name\n");
						}
						else if (selected.equals("TriggerSound"))
						{
							a.setText("type\n" +
									"frequency\n" + 
									"x\n" + 
									"y\n" +
									"z\n" +
									"w\n" +
									"h\n" +
									"sound\n" +
									"name\n");
						}
						else if (selected.equals("TriggerStartTrigger"))
						{
							a.setText("type\n" +
									"frequency\n" + 
									"x\n" + 
									"y\n" +
									"z\n" +
									"w\n" +
									"h\n" +
									"target\n" +
									"name\n");
						}
						else if (selected.equals("TriggerStopTrigger"))
						{
							a.setText("type\n" +
									"frequency\n" + 
									"x\n" + 
									"y\n" +
									"z\n" +
									"w\n" +
									"h\n" +
									"target\n" +
									"name\n");
						}
						else if (selected.equals("Gem"))
						{
							a.setText("type\n" +
									"x\n" + 
									"y\n" +
									"name\n");
						}
						else if (selected.equals("InvokeTrigger"))
						{
							a.setText("time\n" +
									"trigger");
						}
						else if (selected.equals("LevelText"))
						{
							a.setText("chaptertext\n");
						}
						else if (selected.equals("Track"))
						{
							a.setText("type\n" +
									"move\n" +
									"delay\n" +
									"target\n" + 
									"ignoreplayer\n");
						}
					}
					else
					{
						a.setText("No element selected.");
					}
				}
			}

			public void actionPerformed(ActionEvent e) 
			{
				click();
			}
		}
		
		private class InformationArea
			extends JPanel
		{
			public JTextArea area;
			public JButton create;
			
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
				
				Border border = BorderFactory.createTitledBorder("Values");
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
			dispose();
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
