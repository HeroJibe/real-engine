package game;

import java.awt.Color;
import java.awt.Image;

import core.Entity;
import core.EntityHandler;
import core.InputListener;
import core.Light;
import core.NavigationMesh;
import core.RayTrace;
import main.Main;
import utilities.KeyBinds;

public class Player
	implements Runnable
{	
	public static final int GROUND_BIAS = 2;
	public static final int SIDE_BIAS = 5;
	public static final boolean DOUBLE_CHECK_GROUND = true;
	public static final boolean TRIPLE_CHECK_GROUND = true;
	public static final int MAX_WALL_JUMP = 2;
	public static final int WALL_JUMP_X_MULTIPLIER = 2;
	
	public static final int JUMPED_FROM_NOTHING = 0;
	public static final int JUMPED_FROM_LEFT = 1;
	public static final int JUMPED_FROM_RIGHT = 2;
	
	private Entity playerEntity;
	private InputListener inputListener;
	private EntityHandler entityHandler;
	
	// Custom player variables
	private double movementSpeed = 10;
	private double movementInc = 1;
	private double jumpPower = -10;
	private double sprintMultiplier = 3.0;
	private boolean isOnGround = false;
	private boolean isOnLeft = false;
	private boolean isOnRight = false;
	private int jumpedFrom = 0;
	private int timesJumped = 0;
	private boolean jumping = false;
	
	public NavigationMesh navMesh;
	
	public boolean pause = false;
	
	public Player(Entity entity, InputListener inputListener)
	{
		playerEntity = entity;
		this.inputListener = inputListener;
		entityHandler = Main.getEntityHandler();
	}
	
	public Player(InputListener inputListener)
	{
		this.inputListener = inputListener;
		entityHandler = Main.getEntityHandler();
	}
	
	// All player activity
	public void run()
	{				
		Main.println("Player thread started");
		
		 navMesh = new NavigationMesh("map");
		navMesh.buildNavMesh();
		
		while (true)
		{
			//System.out.println("(" + playerEntity.getX() +  ", " + playerEntity.getY() + ") " + navMesh.isWalkable(playerEntity.getX(), playerEntity.getY()));
			if (playerEntity == null)
			{
				Main.println("Error: Player entity is null");
				return;
			}
			
			if (! Main.gamePaused)
				detectMotion();
			
			// DO NOT DELETE
			try
			{
				Thread.sleep(Main.UPDATE_RATE);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	// Detects key input for player movement
	private RayTrace trace;
	private void detectMotion()
	{
		try
		{
			isOnGround = false;
			isOnLeft = false;
			isOnRight = false;
			boolean doubleGround = false;
			boolean tripleGround = false;
			double tempSpeed = movementSpeed;
			
			// Test ground
			Entity testGround = new Entity(Entity.STATIC, true, playerEntity.getX(), playerEntity.getY2() + GROUND_BIAS, 
					(int) playerEntity.getWidth(), 0, 100);
			if (testGround.hasCollided() && ! testGround.hasCollided(playerEntity))
			{
				timesJumped = 0;
				isOnGround = true;
				jumpedFrom = JUMPED_FROM_NOTHING;
			}
			
			// Test double ground
			Entity testGround2 = new Entity(Entity.STATIC, true, playerEntity.getX(), playerEntity.getY2() + (GROUND_BIAS * 2), 
					(int) playerEntity.getWidth(), 0, 100);
			if (testGround2.hasCollided() && ! testGround2.hasCollided(playerEntity))
			{
				doubleGround = true;
			}
			
			// Test triple ground
			Entity testGround3 = new Entity(Entity.STATIC, true, playerEntity.getX(), playerEntity.getY2() + (GROUND_BIAS * 4), 
					(int) playerEntity.getWidth(), 0, 100);
			if (testGround3.hasCollided() && ! testGround3.hasCollided(playerEntity))
			{
				tripleGround = true;
			}
			
			// Test if player is on left
			Entity testLeft = new Entity(Entity.STATIC, true, playerEntity.getX() - SIDE_BIAS, playerEntity.getY(), 
					0, (int) playerEntity.getHeight(), 100);
			if (testLeft.hasCollided() && ! testLeft.hasCollided(playerEntity))
			{
				isOnLeft = true;
			}
			
			// Test if player is on right
			Entity testRight = new Entity(Entity.STATIC, true, playerEntity.getX2() + SIDE_BIAS, playerEntity.getY(), 
					0, (int) playerEntity.getHeight(), 100);
			if (testRight.hasCollided() && ! testRight.hasCollided(playerEntity))
			{
				isOnRight = true;
			}			
			
			boolean beingPushed = playerEntity.beingPushed();
			boolean moving = false;
			
			if (inputListener.isAKeyPressed())
			{
				if (inputListener.isKeyPressed(KeyBinds.SPRINT))
				{
					tempSpeed *= sprintMultiplier;
				}
				
				if (inputListener.isKeyPressed(KeyBinds.UP) && isOnGround && playerEntity.getYVel() == 0)
				{				
					playerEntity.setYVel(jumpPower);
					jumping = true;
					isOnGround = false;
				}
				if (inputListener.isKeyPressed(KeyBinds.DOWN))
				{
					//playerEntity.translate(0, tempSpeed);
				}			
				if (inputListener.isKeyPressed(KeyBinds.LEFT))
				{
					if (inputListener.isKeyPressed(KeyBinds.UP) && isOnLeft && jumpedFrom != JUMPED_FROM_LEFT)
					{
						jumpedFrom = JUMPED_FROM_LEFT;
						if (timesJumped < MAX_WALL_JUMP)
						{
							timesJumped++;
							playerEntity.setYVel(jumpPower);
							playerEntity.setXVel(movementSpeed * WALL_JUMP_X_MULTIPLIER);
						}
					}
					else
					{
						if (playerEntity.getXVel() - movementInc > -1 * movementSpeed)
							playerEntity.setXVel(playerEntity.getXVel() - movementInc);
					}
					playerEntity.setImage(Main.getAnimationHandler().getByName("playerLeft").currFrame().getScaledInstance(
						(int) playerEntity.getWidth(), (int) playerEntity.getHeight(), Image.SCALE_DEFAULT));
					moving = true;
					
				//playerEntity.translate(-1 * tempSpeed, 0);
				}
				if (inputListener.isKeyPressed(KeyBinds.RIGHT))
				{
					if (inputListener.isKeyPressed(KeyBinds.UP) && isOnRight && jumpedFrom != JUMPED_FROM_RIGHT)
					{
						jumpedFrom = JUMPED_FROM_RIGHT;
						if (timesJumped < MAX_WALL_JUMP)
						{
							timesJumped++;
							playerEntity.setYVel(jumpPower);
							playerEntity.setXVel(-1 * movementSpeed * WALL_JUMP_X_MULTIPLIER);
						}
					}
					else
					{
						if (playerEntity.getXVel() + movementInc < movementSpeed)
							playerEntity.setXVel(playerEntity.getXVel() + movementInc);
					}
					playerEntity.setImage(Main.getAnimationHandler().getByName("playerRight").currFrame().getScaledInstance(
						(int) playerEntity.getWidth(), (int) playerEntity.getHeight(), Image.SCALE_DEFAULT));
					moving = true;
					
				//playerEntity.translate(tempSpeed, 0);
				}
				if (inputListener.isKeyPressed("Y"))
				{
					Main.getPhysicsHandler().gravity = 0.5;
				}
				if (inputListener.isKeyPressed("U"))
				{
					Main.getPhysicsHandler().gravity = -0.5;
				}
			}
			
			if (! isOnGround)
			{
				if ((doubleGround  && DOUBLE_CHECK_GROUND) || (tripleGround && TRIPLE_CHECK_GROUND))
					playerEntity.setImage(Main.getAnimationHandler().getByName("player").currFrame().getScaledInstance(
							(int) playerEntity.getWidth(), (int) playerEntity.getHeight(), Image.SCALE_DEFAULT));
				else if (playerEntity.getYVel() < 0)
					playerEntity.setImage(Main.getAnimationHandler().getByName("playerJump").currFrame().getScaledInstance(
							(int) playerEntity.getWidth(), (int) playerEntity.getHeight(), Image.SCALE_DEFAULT));
				else
					playerEntity.setImage(Main.getAnimationHandler().getByName("playerFall").currFrame().getScaledInstance(
							(int) playerEntity.getWidth(), (int) playerEntity.getHeight(), Image.SCALE_DEFAULT));
			}
			else if (Math.round(playerEntity.getXVel()) == 0)
				playerEntity.setImage(Main.getAnimationHandler().getByName("player").currFrame().getScaledInstance(
						(int) playerEntity.getWidth(), (int) playerEntity.getHeight(), Image.SCALE_DEFAULT));
		}
		catch (Exception e)
		{}
	}
	
	// Returns the player's entity
	public Entity getPlayerEntity()
	{
		return playerEntity;
	}
	
	public void setPlayerEntity(Entity entity)
	{
		this.playerEntity = entity;
		playerEntity.setX(Main.player_x);
		playerEntity.setY(Main.player_y);
	}
	
	public void translate(double x, double y)
	{
		playerEntity.translate(x, y);
	}
}
