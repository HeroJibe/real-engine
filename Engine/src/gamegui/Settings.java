package gamegui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import game.GameMain;
import gui.GameWindow;
import main.Main;
import utilities.KeyBinds;

public class Settings 
	extends JDialog
{
	private static final long serialVersionUID = 9087349809056292460L;
	
	private Settings me;
	private JTabbedPane tabs;
	private Buttons buttons;
	private General generalOptions;
	private Control controlOptions;
	private Video videoOptions;
	private Audio audio;
	
	public Settings()
	{		
		super(Main.getGameWindow(), true);
		buttons = new Buttons();
		generalOptions = new General();
		controlOptions = new Control();
		audio = new Audio();
		videoOptions = new Video();	
		
		tabs = new JTabbedPane();
		tabs.setTabPlacement(JTabbedPane.TOP);
		tabs.addTab("General", generalOptions);
		tabs.addTab("Controls", controlOptions);
		tabs.addTab("Audio", audio);
		tabs.addTab("Video", videoOptions);		
		
		GridBagConstraints g = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		layout.setConstraints(this, g);
		setLayout(layout);
		
		g.gridx = 0;
		g.gridy = 0;
		add(tabs, g);
		
		g.gridx = 0;
		g.gridy = 1;
		add(buttons, g);
		
		setAlwaysOnTop(false);	
		setResizable(false);
		setUndecorated(false);
		try 
		{
			this.setIconImage(ImageIO.read((new File("resources\\textures\\Icon.png"))));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		setTitle("Settings");
		pack();
		setLocation((GameWindow.XRES_GL / 2) - (getWidth() / 2), (GameWindow.YRES_GL / 2) - (getHeight() / 2));
		
		
		me = this;
		
		ApplySettings.loadSettings(me);
	}
	
	private void hideWindow()
	{
		setVisible(false);
	}
	
	public void showWindow()
	{
		setVisible(true);
		ApplySettings.loadSettings(this);
	}
	
	public General getGeneralSettings()
	{
		return generalOptions;
	}
	
	public Video getVideoSettings()
	{
		return videoOptions;
	}
	
	public Audio getAudioSettings()
	{
		return audio;
	}
	
	public Control getControlSettings()
	{
		return controlOptions;
	}
	
	private class Buttons
		extends JPanel
	{		
		private static final long serialVersionUID = 1540254458915240967L;
		
		public JButton apply;
		public JButton cancel;
		public JButton ok;
		
		public Buttons()
		{
			apply = new JButton("Apply");
			apply.addActionListener(new ButtonListener());
			cancel = new JButton("Cancel");
			cancel.addActionListener(new ButtonListener());
			ok = new JButton("Ok");
			ok.addActionListener(new ButtonListener());
			
			GridBagConstraints g = new GridBagConstraints();
			GridBagLayout layout = new GridBagLayout();
			layout.setConstraints(this, g);
			setLayout(layout);
			
			g.gridx = 0;
			g.gridy = 0;
			add(cancel, g);
			
			g.gridx = 1;
			g.gridy = 0;
			add(ok, g);
			
			g.gridx = 2;
			g.gridy = 0;
			add(apply, g);
		}
		
		private class ButtonListener
			implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == cancel)
				{
					hideWindow();
				}
				else if (e.getSource() == ok)
				{
					ApplySettings.applySettings(me);
					hideWindow();
				}
				else if (e.getSource() == apply)
				{
					ApplySettings.applySettings(me);
				}
			}		
		}
	}

	public class General
		extends JPanel
	{
		private static final long serialVersionUID = 1540254458915240967L;
		
		public JLabel gameName;
		public JLabel engineName;
		public JLabel debugLabel;
		JComboBox<String> debug;
		String[] debugOptions = {"Yes", "No"};
		
		public General()
		{
			gameName = new JLabel(GameMain.NAME);
			engineName = new JLabel(Main.ENGINE_NAME + " " + Main.ENGINE_VERSION_NAME);
			
			debugLabel = new JLabel("Debug Mode");
			debug = new JComboBox<String>(debugOptions);
			
			GridBagConstraints g = new GridBagConstraints();
			GridBagLayout layout = new GridBagLayout();
			layout.setConstraints(this, g);
			setLayout(layout);
			
			g.gridx = 0;
			g.gridy = 0;
			add(gameName, g);
			
			g.gridx = 0;
			g.gridy = 1;
			add(engineName, g);
			
			g.gridx = 0;
			g.gridy = 2;
			add(debugLabel);
			
			g.gridx = 0;
			g.gridy = 3;
			add(debug);
		}
	}
	
	public class Control
		extends JPanel
	{
		private static final long serialVersionUID = 6550254493215240967L;
		
		public JLabel jump;
		public JTextField jumpKey;
		
		public JLabel left;
		public JTextField leftKey;
		
		public JLabel right;
		public JTextField rightKey;
		
		public Control()
		{
			jump = new JLabel("Jump");
			jumpKey = new JTextField(5);
			jumpKey.setText(KeyBinds.UP);
			
			left = new JLabel(" Left");
			leftKey = new JTextField(5);
			leftKey.setText(KeyBinds.LEFT);
			
			right = new JLabel("Right");
			rightKey = new JTextField(5);
			rightKey.setText(KeyBinds.RIGHT);
			
			GridBagConstraints g = new GridBagConstraints();
			GridBagLayout layout = new GridBagLayout();
			layout.setConstraints(this, g);
			setLayout(layout);
			
			g.gridx = 0;
			g.gridy = 0;
			add(jump, g);
			g.gridx = 1;
			g.gridy = 0;
			add(jumpKey, g);
			
			g.gridx = 2;
			g.gridy = 0;
			add(left, g);
			g.gridx = 3;
			g.gridy = 0;
			add(leftKey, g);
			
			g.gridx = 0;
			g.gridy = 1;
			add(right, g);
			g.gridx = 1;
			g.gridy = 1;
			add(rightKey, g);
		}
	}
	
	public class Video
		extends JPanel
	{
		private static final long serialVersionUID = 6550254493215240967L;
		
		public JLabel waterLabel;
		public JComboBox<String> water;
		public String[] waterOptions = {"No Reflections", "Static Reflections", "Fast Dynamic Reflections",
										"Full Dynamic Reflections"};
		
		public JLabel particleLabel;
		public JComboBox<String> particle;
		public String[] particleOptions = {"All", "Half", "None"}; 
		
		public JLabel animationLabel;
		public JComboBox<String> animation;
		public String[] animationOptions = {"Yes", "No"}; 
		
		public JLabel fpsLabel;
		public JComboBox<String> fps;
		public String[] fpsOptions = {"15", "30", "60", "120", "Unlimited"};
		
		public JLabel lodLabel;
		public JComboBox<String> lod;
		public String[] lodOptions = {"High", "Medium", "Low"};
		
		public Video()
		{
			waterLabel = new JLabel("Reflections Quality");
			water = new JComboBox<String>(waterOptions);
			
			particleLabel = new JLabel("Particles");
			particle = new JComboBox<String>(particleOptions);
			
			animationLabel = new JLabel("Animations");
			animation = new JComboBox<String>(animationOptions);
			
			fpsLabel = new JLabel("Maximum FPS");
			fps = new JComboBox<String>(fpsOptions);
			
			lodLabel = new JLabel("Level of Detail");
			lod = new JComboBox<String>(lodOptions);
			
			GridBagConstraints g = new GridBagConstraints();
			GridBagLayout layout = new GridBagLayout();
			layout.setConstraints(this, g);
			setLayout(layout);
			
			g.gridx = 0;
			g.gridy = 0;
			add(waterLabel, g);
			
			g.gridx = 0;
			g.gridy = 1;
			add(water, g);
			
			g.gridx = 1;
			g.gridy = 0;
			add(particleLabel, g);
			
			g.gridx = 1;
			g.gridy = 1;
			add(particle, g);
			
			g.gridx = 0;
			g.gridy = 2;
			add(animationLabel, g);
			
			g.gridx = 0;
			g.gridy = 3;
			add(animation, g);
			
			g.gridx = 1;
			g.gridy = 2;
			add(fpsLabel, g);
			
			g.gridx = 1;
			g.gridy = 3;
			add(fps, g);
			
			g.gridx = 0;
			g.gridy = 4;
			add(lodLabel, g);
			
			g.gridx = 0;
			g.gridy = 5;
			add(lod, g);
		}
	}
	
	public class Audio
		extends JPanel
	{
		private static final long serialVersionUID = 6550254493215240967L;
		
		public JLabel masterLabel;
		public JSlider master;
		
		public JLabel musicLabel;
		public JSlider music;
		
		public JLabel sfxLabel;
		public JSlider sfx;
		
		public Audio()
		{
			masterLabel = new JLabel("Master Volume");
			master = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
			
			musicLabel = new JLabel("Music Volume");
			music = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
			
			sfxLabel = new JLabel("Sound Effects Volume");
			sfx = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
			
			GridBagConstraints g = new GridBagConstraints();
			GridBagLayout layout = new GridBagLayout();
			layout.setConstraints(this, g);
			setLayout(layout);
			
			g.gridx = 0;
			g.gridy = 0;
			add(masterLabel, g);
			
			g.gridx = 0;
			g.gridy = 1;
			add(master, g);
			
			g.gridx = 0;
			g.gridy = 2;
			add(musicLabel, g);
			
			g.gridx = 0;
			g.gridy = 3;
			add(music, g);
			
			g.gridx = 0;
			g.gridy = 4;
			add(sfxLabel, g);
			
			g.gridx = 0;
			g.gridy = 5;
			add(sfx, g);
		}
	}
}
