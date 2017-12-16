package utilities;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DebugConsole 
	extends JFrame
	implements Runnable
{
	private static final long serialVersionUID = 8469095444963014582L;
	
	DebugWindow pane;
	
	public DebugConsole()
	{
		super();
	}
	
	public void run()
	{
		pane = new DebugWindow();
		setContentPane(pane);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setSize(600, 800);
		setResizable(false);
		setUndecorated(true);
		add(pane);
		
		setVisible(true);
		pack();
		this.toFront();
	}
	
	private class DebugWindow
		extends JPanel
	{
		private static final long serialVersionUID = -6171107587665666724L;

		public JTextField input = new JTextField();
		public JTextArea output = new JTextArea();
		
		public DebugWindow()
		{
			new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			add(input, c);
			c.gridx = 0;
			c.gridy = 2;
			add(output, c);
		}
	}
}
