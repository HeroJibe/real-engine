package wizards;

import java.io.File;

import core.Entity;
import main.Main;

public class BrushWizard 
	extends Wizard
{
	private String type;
	private String texture;
	private double x;
	private double y;
	private int z;
	private int w;
	private int h;
	private boolean solid;
	private int mass;
	private boolean kinetic;
	private Entity tiedEntity;

	public BrushWizard() 
	{
		super(11);
	}
	
	public BrushWizard(Entity e)
	{
		super(11);
		String type = null;
		if (e.isStatic())
			type = "STATIC";
		else
			type = "DYNAMIC";
		
		setValue("type", type);
		setValue("texture", "" + e.getImageFile().getName());
		setValue("x", "" + e.getX());
		setValue("y", "" + e.getY());
		setValue("z", "" + e.getZBuffer());
		setValue("w", "" + e.getWidth());
		setValue("h", "" + e.getHeight());
		setValue("solid", "" + e.isSolid());
		setValue("mass", "" + e.getMass());
		setValue("kinetic", "" + e.isKinetic());
		setValue("name", "");
		update();
	}

	public void setupDataTypes() 
	{
		setupDataType(0, "type", "String");
		setupDataType(1, "texture", "String");
		setupDataType(2, "x", "double");
		setupDataType(3, "y", "double");
		setupDataType(4, "z", "int");
		setupDataType(5, "w", "int");
		setupDataType(6, "h", "int");
		setupDataType(7, "solid", "boolean");
		setupDataType(8, "mass","int");
		setupDataType(9, "kinetic", "boolean");
		setupDataType(10, "name", "String");
	}

	public void setDefaultValues() 
	{
		setValue("type", "STATIC");
		setValue("texture", "default.png");
		setValue("x", "0");
		setValue("y", "0");
		setValue("z", "0");
		setValue("w", "1");
		setValue("h", "1");
		setValue("solid", "true");
		setValue("mass", "10");
		setValue("kinetic", "true");
		setValue("name", "");
	}
	
	public void update()
	{
		type = getStringAt(0);
		texture = getStringAt(1);
		x = getDoubleAt(2);
		y = getDoubleAt(3);
		z = getIntAt(4);
		w = getIntAt(5);
		h = getIntAt(6);
		solid = getBooleanAt(7);
		mass = getIntAt(8);
		kinetic = getBooleanAt(9);
	}

	public void add() 
	{
		update();
		Main.getEntityHandler().removeEntity(tiedEntity);
		Entity e = null;
		if (type.equals("STATIC"))
		{
			e = new Entity(Entity.STATIC, Main.getResourceHandler().getByName(texture), w, h, solid, name, x, y, z, w, h, 100, false);
			Main.getEntityHandler().addStaticEntity(e);
		}
		else if (type.equals("DYNAMIC"))
		{
			e = new Entity(Entity.DYNAMIC, Main.getResourceHandler().getByName(texture), w, h, solid, name, x, y, z, w, h, 100, false);
			Main.getEntityHandler().addDynamicEntity(e);
		}
		e.setSelected(true);
		tiedEntity = e;
	}

	public String export() 
	{
		update();
		if (type.equals("STATIC"))
		{
			String toReturn = "Brush = " + "STATIC " + texture + " " + x + " " + y + " " + z + " " + w + " " + h  + 
					" " + solid;
			return toReturn;
		}
		else if (type.equalsIgnoreCase("DYNAMIC"))
		{
			String toReturn = "Brush = " + "STATIC " + texture + " " + x + " " + y + " " + z + " " + w + " " + h  + 
					" " + solid + " " + mass + " " + kinetic + " " + name;
			return toReturn;
		}
		return null;
	}

}
