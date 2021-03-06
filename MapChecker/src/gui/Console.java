package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import core.Command;
import core.commands.Echo;
import main.Main;

public class Console 
	extends JFrame
{
	private static final long serialVersionUID = 4989941948035851333L;

	public ConsoleWindow win;
	
	public Console()
	{
		super(Main.NAME);
		win = new ConsoleWindow();
		add(win);
		
		getRootPane().setDefaultButton(win.input.inputButton);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pack();
		
		win.input.input.requestFocusInWindow();
		setResizable(false);
	}
	
	public class ConsoleWindow
		extends JPanel
	{
		private static final long serialVersionUID = 1074153886891191407L;
		
		public JTextArea output;
		public JScrollPane outputScroll;
		public InputArea input;
		
		public ConsoleWindow()
		{
			GridBagConstraints g = new GridBagConstraints();
			GridBagLayout layout = new GridBagLayout();
			layout.setConstraints(this, g);
			setLayout(layout);
			
			try 
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} 
			catch (Exception e)
			{
				e.printStackTrace(System.out);
			}
			
			output = new JTextArea(20, 85);
			output.setEditable(false);
			
			outputScroll = new JScrollPane(output);
			
			input = new InputArea();
			
			g.gridx = 0;
			g.gridy = 0;
			add(outputScroll, g);
			
			g.gridx = 0;
			g.gridy = 1;
			add(input, g);
		}
		
		private class InputArea
			extends JPanel
			implements ActionListener
		{
			private static final long serialVersionUID = -5257830342536816895L;
			
			public JTextField input;
			public JButton inputButton;
			
			public InputArea()
			{
				GridBagConstraints g = new GridBagConstraints();
				GridBagLayout layout = new GridBagLayout();
				layout.setConstraints(this, g);
				setLayout(layout);
				
				input = new JTextField(43);
				inputButton = new JButton("Submit");
				inputButton.addActionListener(this);
				
				g.gridx = 0;
				g.gridy = 0;
				add(input, g);
				
				g.gridx = 1;
				g.gridy = 0;
				add(inputButton, g);
			}

			public void actionPerformed(ActionEvent e) 
			{
				execute(input.getText());
				input.setText("");
			}
		}
	}
	
	public void print(String text)
	{
		if (Main.useGUI)
			win.output.append(text);
		else
			System.out.print(text);
		win.outputScroll.getVerticalScrollBar().setValue(win.outputScroll.getVerticalScrollBar().getMaximum());
	}

	public void println(String text)
	{
		print(text + "\n");
		win.outputScroll.getVerticalScrollBar().setValue(win.outputScroll.getVerticalScrollBar().getMaximum());
	}
	
	public void execute(String cmdName)
	{
		if (cmdName == null)
			return;
		else if (cmdName.equals(""))
			return;
		
		if (Echo.shouldEcho())
		{
			println("> " + cmdName);
		}
		
		String[] argsRaw = cmdName.split(" ");
		Command cmd = Main.commandHandler.getByName(argsRaw[0]);
		
		String[] args = null;
		if (argsRaw.length > 1)
		{
			args = new String[argsRaw.length - 1];
			for (int i = 0; i < args.length; i++)
			{
				args[i] = argsRaw[i + 1];
			}
		}
		if (cmd != null)
			cmd.execute(args);
		else
			println("Unknown command: " + argsRaw[0]);
	}
}
