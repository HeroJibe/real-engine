package core;

import java.awt.Color;
import java.awt.Image;

import main.Main;
import net.NetMain;

public class CompressedEntity 
{
	private Entity e;
	
	public CompressedEntity(Entity e)
	{
		this.e = e;
	}
	
	public Entity toEntity()
	{
		String compress = toString();
		if (compress == null)
			return null;
		String[] ca = compress.split(", ");
		Entity e = new Entity();
		for (int i = 0; i < ca.length; i++)
		{
			try
			{
				String[] cl = ca[i].split("=");
				if (cl[0].equals("name"))
					e.setName(cl[1]);
				else if (cl[0].equals("type"))
					e.setType(Integer.parseInt(cl[1]));
				else if (cl[0].equals("texture"))
				{
					Image img = Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName(cl[1], true));
					e.setImage(img);
				}
				else if (cl[0].equals("animation"))
					e.setAnimation(Main.getAnimationHandler().getByName(cl[1]));
				else if (cl[0].equals("x"))
					e.setX(Double.parseDouble(cl[1]));
				else if (cl[0].equals("y"))
					e.setY(Double.parseDouble(cl[1]));
				else if (cl[0].equals("z"))
					e.setZBuffer(Integer.parseInt(cl[1]));
				else if (cl[0].equals("w"))
					e.setWidth(Double.parseDouble(cl[1]));
				else if (cl[0].equals("h"))
					e.setHeight(Double.parseDouble(cl[1]));
				else if (cl[0].equals("xs"))
					e.setScaleX((int) Double.parseDouble(cl[1]));
				else if (cl[0].equals("ys"))
					e.setScaleY((int) Double.parseDouble(cl[1]));
				else if (cl[0].equals("solid"))
					e.setSolid(Boolean.parseBoolean(cl[1]));
				else if (cl[0].equals("lod"))
					e.setLOD(Integer.parseInt(cl[1]));
				else if (cl[0].equals("xvel"))
					e.setXVel(Double.parseDouble(cl[1]));
				else if (cl[0].equals("yvel"))
					e.setYVel(Double.parseDouble(cl[1]));
				else if (cl[0].equals("renderInReflections"))
					e.renderInReflections = true;
				else if (cl[0].equals("kinetic"))
					e.setKinetic(Boolean.parseBoolean(cl[1]));
				else if (cl[0].equals("updateWithPhysics"))
					e.updateWithPhysics(Boolean.parseBoolean(cl[1]));
				else if (cl[0].equals("mass"))
					e.setMass(Integer.parseInt(cl[1]));
				else if (cl[0].equals("health"))
					e.health = Integer.parseInt(cl[1]);
				else if (cl[0].equals("friction"))
					e.setFriction(Double.parseDouble(cl[1]));
				else if (cl[0].equals("gravity"))
					e.setGravity(Double.parseDouble(cl[1]));
				else if (cl[0].equals("pubID"))
					e.setPubID(Integer.parseInt(cl[1]));
				else
				{
					Main.println("Unknown value: " + cl[0], Color.RED);
					System.out.println("Unkown value: " + cl[0]);
					NetMain.println("Unkown value: " + cl[0]);
				}
			}
			catch (NumberFormatException e1)
			{
				Main.println("Invalid value.");
				System.out.println("Invalid value.");
				NetMain.println("Invalid value.");
			}
			catch (ArrayIndexOutOfBoundsException e1)
			{
				Main.println("Missing value.");
				System.out.println("Missing value.");
				NetMain.println("Missing value.");
			}
		}
		return e;
	}
	
	public String toString()
	{
		if (e == null)
			return null;
		String name = e.getName();
		int type = -1;
		if (e.isStatic())
		{
			type = Entity.STATIC;
		}
		else
		{
			type = Entity.DYNAMIC;
		}
		String texture = e.getImageFile().getName();
		String animation = e.getAnimation().getName();
		int pubID = e.getPublicID();
		double x = e.getActualX();
		double y = e.getActualY();
		int z = e.getZBuffer();
		double w = e.getActualWidth();
		double h = e.getActualHeight();
		double xs = e.getActualScaleX();
		double ys = e.getActualScaleY();
		boolean solid = e.isSolid();
		int LOD = e.getLOD();
		double xvel = e.getXVel();
		double yvel = e.getYVel();
		boolean renderInReflections = e.shouldRenderInReflections();
		boolean isKinetic = e.isKinetic();
		boolean updateWithPhysics = e.willUpdateWithPhysics();
		int mass = e.getMass();
		int health = e.health;
		double friction = e.getFriction();
		double gravity = e.getGravity();
		
		String compressed = "name=" + name + ", type=" + type + ", texture=" + texture + ", animation=" + animation +
				", x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + ", h=" + h + ", xs=" + xs + ", ys=" + ys + ", solid=" + solid +
				", lod=" + LOD + ", xvel=" + xvel + ", yvel=" + yvel + ", renderInReflections=" + renderInReflections +
				", kinetic=" + isKinetic + ", updateWithPhysics=" + updateWithPhysics + ", mass=" + mass + ", health=" + health +
				", friction=" + friction + ", gravity=" + gravity + ", pubID=" + pubID;
		return compressed;
	}
	
	public static void assign(Entity e, CompressedEntity ce)
	{
		e = ce.toEntity();
	}
}
