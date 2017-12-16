package editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import gui.GameWindow;
import main.Main;
import wizards.Wizard;

public class ElementEditor 
	implements Runnable
{
	public static final int ELEMENTS = 10;
	public static int INSTANCES = 0;
	
	GameWindow window;
	String[] names;
	String[] types;
	String[] values;
	Frame fr;
	Wizard wiz;
	
	public ElementEditor()
	{
		this.window = Main.getGameWindow();
		String[] names = {""};
		this.names = names;
		String[] types = {""};
		this.names = types;
		String[] values = {""};
		this.values = values;
	}

	public void run() 
	{
		INSTANCES++;
		fr = new Frame();
		fr.setVisible(true);
		fr.toFront();
		//String last = "";
		while (true)
		{}
	}
	
	public void load(Wizard wiz)
	{
		this.wiz = wiz;
		names = new String[wiz.getNumElements()];
		types = new String[wiz.getNumElements()];
		values = new String[wiz.getNumElements()];
		for (int i = 0; i < wiz.getNumElements(); i++)
		{
			names[i] = wiz.getNameAt(i);
			types[i] = wiz.getTypeAt(i);
			values[i] = wiz.getStringAt(i);
		}
		fr.panel.list.setListData(names);
	}
	
	private class Frame
		extends JFrame
		implements WindowListener
	{		
		public Panel panel;
		public InformationArea area;
		
		public Frame()
		{
			super("Element Editor");
			GridBagConstraints g = new GridBagConstraints();
			GridBagLayout gLayout = new GridBagLayout();
			gLayout.setConstraints(this, g);
			setLayout(gLayout);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(this);
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
				
				list = new JList<String>(names);
				list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				list.setVisibleRowCount(-1);
				list.setSize(290, 450);
				JScrollPane scrollPane = new JScrollPane(list);
				scrollPane.setPreferredSize(new Dimension(400, 160));
				
				JLabel label = new JLabel("Key Values");
				
				button = new JButton("Select");
				
				Border border = BorderFactory.createTitledBorder("Select Key Value");
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
				list.addMouseListener(new MouseAdapter() 
				{
			         public void mouseClicked(MouseEvent me) 
			         {
			            if (me.getClickCount() >= 2) 
			            {
			            	fr.area.edit();
			            }
			            if (me.getClickCount() >= 1)
			            {
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
				if (selectedNum >= 0 && selectedNum < names.length)
					selected = names[selectedNum];
				
				if (a != null)
				{
					//System.out.println(selected);
					if (selected != null)
					{
						a.setText(wiz.getValue(list.getSelectedValue()));
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
			implements ActionListener
		{
			public JTextArea area;
			public JButton create;
			
			public InformationArea()
			{
				GridBagConstraints g = new GridBagConstraints();
				GridBagLayout gLayout = new GridBagLayout();
				gLayout.setConstraints(this, g);
				setLayout(gLayout);
				
				JList<String> list = new JList<String>(names);
				list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				list.setVisibleRowCount(-1);
				list.setSize(300, 450);
				JScrollPane scrollPane = new JScrollPane(list);
				scrollPane.setPreferredSize(new Dimension(400, 160));
				
				JLabel label = new JLabel("Select Map Element");
				
				JButton button = new JButton("Edit");
				button.addActionListener(this);
				
				Border border = BorderFactory.createTitledBorder("Values");
				setBorder(border);
				
				area = new JTextArea(11, 15);
				area.setEditable(false);
				area.setFocusable(false);
				//JScrollPane scroll = new JScrollPane(area); 
				
				g.gridx = 0;
				g.gridy = 0;
				add(area, g);
				
				g.gridx = 0;
				g.gridy = 1;
				add(button, g);
			}

			public void actionPerformed(ActionEvent e) 
			{
				edit();
			}
			
			public void edit()
			{
				String newValue = null;
				newValue = JOptionPane.showInputDialog(this, "Value:");
				if (newValue != null)
				{
					wiz.setValue(fr.panel.list.getSelectedValue(), newValue);
					wiz.add();
					area.append("\nchanged to \"" + newValue + "\"");
				}
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
