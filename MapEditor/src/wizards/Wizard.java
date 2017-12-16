package wizards;

import main.Main;

public abstract class Wizard 
{
	private static int nextID = 0;
	
	private int ID;
	private int numKeyValues;
	private String[] dataName;
	private String[] dataType;
	private String[] dataValue;
	
	protected String name;
	
	public Wizard(int numKeyValues)
	{
		ID = nextID;
		nextID++;
		this.numKeyValues = numKeyValues;
		dataName = new String[numKeyValues];
		dataType = new String[numKeyValues];
		dataValue = new String[numKeyValues];
		setupDataTypes();
		setDefaultValues();
		update();
	}
	
	public void setValue(String name, String value)
	{
		if (dataName == null)
		{
			return;
		}
			
		for (int i = 0; i < numKeyValues; i++)
		{
			if (dataName[i] != null)
			{
				if (dataName[i].equals(name))
				{
					dataValue[i] = value;
				}
			}
		}
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getNumElements()
	{
		return numKeyValues;
	}
	
	public String getValue(String name)
	{
		for (int i = 0; i < numKeyValues; i++)
		{
			if (dataName[i] != null)
			{
				if (dataName[i].equals(name))
				{
					return dataValue[i];
				}
			}
		}
		return null;
	}
	
	public String getStringAt(int i)
	{
		if (dataType[i] == null || dataValue[i] == null)
			return null;
		if (dataType[i].equals("String"))
		{
			return dataValue[i];
		}
		return null;
	}
	
	public int getIntAt(int i)
	{
		if (dataType[i] == null || dataValue[i] == null)
			return -1;
		if (dataType[i].equals("int"))
		{
			return Integer.parseInt(dataValue[i]);
		}
		return - 1;
	}
	
	public double getDoubleAt(int i)
	{
		if (dataType[i] == null || dataValue[i] == null)
			return -1;
		if (dataType[i].equals("double"))
		{
			return Double.parseDouble(dataValue[i]);
		}
		return -1;
	}
	
	public boolean getBooleanAt(int i)
	{
		if (dataType[i] == null || dataValue[i] == null)
			return false;
		if (dataType[i].equals("boolean"))
		{
			return Boolean.parseBoolean(dataValue[i]);
		}
		return false;
	}
	
	public String getNameAt(int i)
	{
		return dataName[i];
	}
	
	public String getTypeAt(int i)
	{
		return dataType[i];
	}
	
	public int getID()
	{
		return ID;
	}
	
	protected void setupDataType(int i, String name, String dataType)
	{
		this.dataType[i] = dataType;
		this.dataName[i] = name;
	}
	
	public abstract void setupDataTypes();		// Sets up the data types
	public abstract void setDefaultValues();	// Sets default data values
	public abstract void update();				// Updates a changed value
	public abstract void add();					// Adds the element to the visual editor
	public abstract String export();			// Exports to a String to be read by the map reader
}
