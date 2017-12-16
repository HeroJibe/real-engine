package wizards;

import java.awt.Color;

import main.Main;

public class WizardHandler 
{
	private int maxWizards;
	private int numWizards;
	private Wizard[] wizards;
	
	public WizardHandler(int maxWizards)
	{
		this.maxWizards = maxWizards;
		numWizards = 0;
		wizards = new Wizard[maxWizards];
	}
	
	public void add(Wizard wiz)
	{
		for (int i = 0; i < wizards.length; i++)
		{
			if (wizards[i] == null)
			{
				wizards[i] = wiz;
				return;
			}
		}
		Main.println("Wizard cache not big enough!", Color.RED);
	}
	
	public Wizard getByID(int id)
	{
		for (int i = 0; i < wizards.length; i++)
		{
			if (wizards[i] != null)
			{
				if (wizards[i].getID() == id)
				{
					return wizards[i];
				}
			}
		}
		return null;
	}
}
