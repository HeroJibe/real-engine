package game;

import java.awt.Image;
import core.Animation;
import core.Entity;
import core.GameAmbientSound;
import core.GameRunnable;
import core.GameSound;
import core.InputListener;
import core.NavigationMesh;
import core.RenderLoop;
import core.guiElements.GuiImage;
import main.Main;
import utilities.KeyBinds;

/**
 * The player class controls the playable character
 * in game.
 * 
 * @author Ethan Vrhel
 * @see GameMain
 * @see Entity
 */
public class Player
	implements GameRunnable, Runnable
{	
	/**
	 * The bias for the player being on the ground
	 */
	public static final int GROUND_BIAS = 2;
	
	/**
	 * The bias for the player being on the side
	 */
	public static final int SIDE_BIAS = 5;
	
	/**
	 * Whether the ground should be double checked
	 */
	public static final boolean DOUBLE_CHECK_GROUND = true;
	
	/**
	 * Whether the ground should be triple checked
	 */
	public static final boolean TRIPLE_CHECK_GROUND = true;
	
	/**
	 * The maximum number of wall jumps
	 */
	public static final int MAX_WALL_JUMP = 0;
	
	/**
	 * The multiplier for x knockback on wall jumps
	 */
	public static final int WALL_JUMP_X_MULTIPLIER = 2;
	
	/**
	 * The threshold for a brush to be registered as a stair
	 */
	public static final int STAIRS_THRESHOLD = 16;
	
	/**
	 * Constant for the player jumping without a wall
	 */
	public static final int JUMPED_FROM_NOTHING = 0;
	
	/**
	 * Constant for the player jumping from a left wall
	 */
	public static final int JUMPED_FROM_LEFT = 1;
	
	/**
	 * Constant for the player jumping from a right wall
	 */
	public static final int JUMPED_FROM_RIGHT = 2;
	
	/**
	 * The normal player width
	 */
	public static final int NORMAL_WIDTH = 26;
	
	/**
	 * The normal player height
	 */
	public static final int NORMAL_HEIGHT = 44;
	
	/**
	 * The jumping player width
	 */
	public static final int JUMP_WIDTH = 32;
	
	/**
	 * The jumping player height
	 */
	public static final int JUMP_HEIGHT = 64;
	
	private Entity playerEntity;
	private InputListener inputListener;
	
	// Custom player variables
	private double movementSpeed = 7.5;
	private double movementInc = 1;
	private double jumpPower = -12;
	private boolean isOnGround = false;
	private boolean isOnLeft = false;
	private boolean isOnRight = false;
	private boolean stairsLeft = false;
	private boolean stairsRight = false;
	private int jumpedFrom = 0;
	private int timesJumped = 0;
	private GameSound mat1;
	private Animation jump;
	private Animation fall;
	private Animation left;
	private Animation right;
	private Animation idle;
	private Thread deadThread;
	/**
	 * The navigation mesh
	 */
	public NavigationMesh navMesh;
	
	/**
	 * Whether the game is paused
	 */
	@Deprecated
	public boolean pause = false;
	
	/**
	 * Whether the player is dead
	 */
	public boolean dead;
	
	public Player(Entity entity, InputListener inputListener)
	{
		playerEntity = entity;
		this.inputListener = inputListener;
		dead = false;
	}
	
	public Player(InputListener inputListener)
	{
		this.inputListener = inputListener;
	}
	
	@Override
	public void onGameInit()
	{
		Main.println("Player thread started");
		
		navMesh = new NavigationMesh("map");
		navMesh.buildNavMesh();
		
		mat1 = new GameAmbientSound("jump", Main.getResourceHandler().getByName("pop.wav"), GameSound.EFFECT);
		
		//Main.getGameWindow().focusEntity = playerEntity;
		RenderLoop.setCameraEntity(playerEntity);
		
		jump = Main.getAnimationHandler().getByName("playerJump.anim");
		fall = Main.getAnimationHandler().getByName("playerFall.anim");
		left = Main.getAnimationHandler().getByName("playerLeft.anim");
		right = Main.getAnimationHandler().getByName("playerRight.anim");
		idle = Main.getAnimationHandler().getByName("player.anim");
		
		jump.setEntity(playerEntity);
		fall.setEntity(playerEntity);
		left.setEntity(playerEntity);
		right.setEntity(playerEntity);
		idle.setEntity(playerEntity);
		
		jump.limitAnimation(true);
		fall.limitAnimation(false);
		left.limitAnimation(false);
		right.limitAnimation(false);
		idle.limitAnimation(false);
		
		deadThread = new Thread(this);
		deadThread.setDaemon(true);
		deadThread.start();
		
		Main.getReflectionHandler().update(true);
	}

	@Override
	public void onGameUpdate()
	{		
		if (playerEntity == null)
		{
			Main.println("Error: Player entity is null");
			return;
		}
		
		if (! Main.gamePaused && ! dead)
			detectMotion();
		
		if (mat1.getSource() != null)
			mat1.getSource().setLocation(playerEntity.getX(), playerEntity.getY());
	}

	private void detectMotion()
	{
		try
		{
			isOnGround = false;
			isOnLeft = false;
			isOnRight = false;
			stairsLeft = false;
			stairsRight = false;
			Entity leftStairs = null;
			Entity rightStairs = null;
			boolean doubleGround = false;
			boolean tripleGround = false;
			
			// Test ground
			Entity testGround;
			if (Main.getPhysicsHandler().getGravity() >= 0)
			{
				testGround = new Entity(Entity.STATIC, true, playerEntity.getX() / Main.resolutionScaleX,
						(playerEntity.getY2() + GROUND_BIAS) / Main.resolutionScaleY, 
					(int) Math.ceil(playerEntity.getWidth()), 0, 100);
				jump.setOrientation(false, jump.flipVertical());
				fall.setOrientation(false, fall.flipVertical());
				left.setOrientation(false, left.flipVertical());
				right.setOrientation(false, right.flipVertical());
				idle.setOrientation(false, jump.flipVertical());
			}
			else
			{
				testGround = new Entity(Entity.STATIC, true, playerEntity.getX() / Main.resolutionScaleX, 
						(playerEntity.getY() - GROUND_BIAS) / Main.resolutionScaleY, 
					(int) Math.ceil((playerEntity.getWidth() / Main.resolutionScaleX)), 0, 100);
				jump.setOrientation(true, jump.flipVertical());
				fall.setOrientation(true, jump.flipVertical());
				left.setOrientation(true, jump.flipVertical());
				right.setOrientation(true, jump.flipVertical());
				idle.setOrientation(true, jump.flipVertical());
			}
			
			if (testGround.hasCollided() && ! testGround.hasCollided(playerEntity))
			{
				timesJumped = 0;
				isOnGround = true;
				jumpedFrom = JUMPED_FROM_NOTHING;
			}
			
			// Test double ground
			Entity testGround2 = new Entity(Entity.STATIC, true, playerEntity.getX() / Main.resolutionScaleX, 
					(playerEntity.getY2() + (GROUND_BIAS * 2)) / Main.resolutionScaleY, 
					(int) (playerEntity.getWidth() / Main.resolutionScaleX), 0, 100);
			if (testGround2.hasCollided() && ! testGround2.hasCollided(playerEntity))
			{
				doubleGround = true;
			}
			
			// Test triple ground
			Entity testGround3 = new Entity(Entity.STATIC, true, playerEntity.getX() / Main.resolutionScaleX, 
					(playerEntity.getY2() + (GROUND_BIAS * 4) / Main.resolutionScaleY), 
					(int) (playerEntity.getWidth() / Main.resolutionScaleX), 0, 100);
			if (testGround3.hasCollided() && ! testGround3.hasCollided(playerEntity))
			{
				tripleGround = true;
			}
			
			// Test if player is on left
			Entity testLeft = new Entity(Entity.STATIC, true, playerEntity.getX() / Main.resolutionScaleX - SIDE_BIAS, 
					playerEntity.getY() / Main.resolutionScaleY, 
					0, (int) (playerEntity.getHeight() / Main.resolutionScaleY), 100);
			//Main.println(testLeft.toString() + testLeft.getWidth() + " " + testLeft.getHeight());
			if (testLeft.hasCollided() && ! testLeft.hasCollided(playerEntity))
			{
				isOnLeft = true;
			}
			
			// Test if player is on right
			Entity testRight = new Entity(Entity.STATIC, true, playerEntity.getX2() / Main.resolutionScaleX + SIDE_BIAS, 
					playerEntity.getY() / Main.resolutionScaleY, 
					0, (int) (playerEntity.getHeight() / Main.resolutionScaleY), 100);
			if (testRight.hasCollided() && ! testRight.hasCollided(playerEntity))
			{
				isOnRight = true;
			}			
			
			// Test if player can climb stairs/
			Entity testStairs = new Entity(Entity.STATIC, true, playerEntity.getX() / Main.resolutionScaleX - 8, 
					playerEntity.getY() / Main.resolutionScaleY, 
					8, STAIRS_THRESHOLD, 100);
			//Main.println(playerEntity.toString() + playerEntity.getWidth() + " " + playerEntity.getHeight());
			//Main.println(testStairs.toString() + testStairs.getWidth() + " " + testStairs.getHeight());
			//Main.println(testStairs.toString(), Color.BLUE);
			if (! testStairs.hasCollided() && isOnGround && isOnLeft)
			{
				//Main.println("stairz", Color.GREEN);
				Entity temp = new Entity(Entity.STATIC, true, playerEntity.getX() / Main.resolutionScaleX - 8, 
						playerEntity.getY2() / Main.resolutionScaleY - 1, 
						8, 1, 100);
				leftStairs = temp.getCollided();
				stairsLeft = true;
			}

			testStairs = new Entity(Entity.STATIC, true, playerEntity.getX2() / Main.resolutionScaleX, 
					playerEntity.getY() / Main.resolutionScaleY, 
					8, STAIRS_THRESHOLD, 100);
			//Main.println(testStairs.toString(), Color.RED);
			if (! testStairs.hasCollided() && isOnGround)
			{
				Entity temp = new Entity(Entity.STATIC, true, playerEntity.getX2() / Main.resolutionScaleX + 8, 
						playerEntity.getY2() / Main.resolutionScaleY - 1, 
						8, 1, 100);
				rightStairs = temp.getCollided();
				stairsRight = true;
			}
			
			if (inputListener.isAKeyPressed())
			{				
				if (inputListener.isKeyPressed(KeyBinds.UP) && isOnGround && playerEntity.getYVel() == 0)
				{				
					//mat1.playSound();
					if (Main.getPhysicsHandler().getGravity() >= 0)
						playerEntity.setYVel(jumpPower);
					else
						playerEntity.setYVel(- jumpPower);
					isOnGround = false;
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
						
						if (stairsLeft && leftStairs != null)
						{
							if (((leftStairs.getY() / Main.resolutionScaleY) - 
									(playerEntity.getY2() / Main.resolutionScaleY)) > -1 * STAIRS_THRESHOLD)
							{
								playerEntity.translateY(((leftStairs.getY() / Main.resolutionScaleY) 
										- (playerEntity.getY2() / Main.resolutionScaleY) - 1));
							}
						}
					}					
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
						
						if (stairsRight && rightStairs != null)
						{
							if (((rightStairs.getY() / Main.resolutionScaleY) - 
									(playerEntity.getY2() / Main.resolutionScaleY)) > -1 * STAIRS_THRESHOLD)
							{
								playerEntity.translateY(((rightStairs.getY() / Main.resolutionScaleY) - 
										(playerEntity.getY2() / Main.resolutionScaleY) - 1));
							}
						}
					}
				}
				if (inputListener.isKeyPressed("Y"))
				{
					Main.getPhysicsHandler().setGravity(0.5);
				}
				if (inputListener.isKeyPressed("U"))
				{
					Main.getPhysicsHandler().setGravity(-0.5);
				}
				if (inputListener.isKeyPressed("M"))
				{
					Main.drawWireframe = true;
				}
				if (inputListener.isKeyPressed("N"))
				{
					Main.drawWireframe = false;
				}
				if (inputListener.isKeyPressed("C"))
				{
					Main.getGameWindow().render = false;
				}
				if (inputListener.isKeyPressed("V"))
				{
					Main.getGameWindow().render = true;
				}
				//Main.light3.setPos(playerEntity.getX(), playerEntity.getY());
			}
			
			if (! isOnGround)
			{
				if ((doubleGround  && DOUBLE_CHECK_GROUND) || (tripleGround && TRIPLE_CHECK_GROUND))
				{
					playerEntity.setScaleX(NORMAL_WIDTH);
					playerEntity.setScaleY(NORMAL_HEIGHT);
					jump.shouldRun(false);
					fall.shouldRun(false);
					left.shouldRun(false);
					right.shouldRun(false);
					if (! idle.isAnimating())
					{
						idle.shouldRun(true);
						//(new Thread(idle)).start();
					}
				}
				else if (playerEntity.getYVel() < 0)
				{
					idle.shouldRun(false);
					fall.shouldRun(false);
					left.shouldRun(false);
					right.shouldRun(false);
					playerEntity.setScaleX(JUMP_WIDTH);
					playerEntity.setScaleY(JUMP_HEIGHT);
					if (! jump.isAnimating())
					{
						jump.shouldRun(true);
						jump.setFrame(0);
						//(new Thread(jump)).start();
					}
				}
				else
				{
					idle.shouldRun(false);
					jump.shouldRun(false);
					left.shouldRun(false);
					right.shouldRun(false);
					playerEntity.setScaleX(NORMAL_WIDTH);
					playerEntity.setScaleY(NORMAL_HEIGHT);
					if (! fall.isAnimating())
					{
						fall.shouldRun(true);
						fall.setFrame(0);
						//(new Thread(fall)).start();
					}
				}
			}
			else if (Math.round(playerEntity.getXVel()) == 0)
			{
				jump.shouldRun(false);
				fall.shouldRun(false);
				left.shouldRun(false);
				right.shouldRun(false);
				playerEntity.setScaleX(NORMAL_WIDTH);
				playerEntity.setScaleY(NORMAL_HEIGHT);
				if (! idle.isAnimating())
				{
					idle.shouldRun(true);
					//(new Thread(idle)).start();
				}
			}
			else
			{
				if (playerEntity.getXVel() < 0)
				{
					jump.shouldRun(false);
					fall.shouldRun(false);
					right.shouldRun(false);
					idle.shouldRun(false);
					playerEntity.setScaleX(NORMAL_WIDTH);
					playerEntity.setScaleY(NORMAL_HEIGHT);
					jump.setOrientation(jump.flipHorizontal(), false);
					fall.setOrientation(fall.flipHorizontal(), false);
					left.setOrientation(left.flipHorizontal(), false);
					right.setOrientation(right.flipHorizontal(), false);
					idle.setOrientation(idle.flipHorizontal(), false);
					if (! left.isAnimating())
					{
						left.shouldRun(true);
						//(new Thread(left)).start();
					}
				}
				else
				{
					jump.shouldRun(false);
					fall.shouldRun(false);
					left.shouldRun(false);
					idle.shouldRun(false);
					playerEntity.setScaleX(NORMAL_WIDTH);
					playerEntity.setScaleY(NORMAL_HEIGHT);
					jump.setOrientation(jump.flipHorizontal(), true);
					fall.setOrientation(fall.flipHorizontal(), true);
					left.setOrientation(left.flipHorizontal(), true);
					right.setOrientation(right.flipHorizontal(), true);
					idle.setOrientation(idle.flipHorizontal(), true);
					if (! right.isAnimating())
					{
						right.shouldRun(true);
						//(new Thread(right)).start();
					}
				}
			}
			
			if (isOnGround)
			{
				Entity coll = testGround.getCollided();
				//System.out.println("playing: " + coll.getMaterial());
				if (coll.getMaterial() != null)
				{
					if (coll.getMaterial().getSound() != null)
					{
						coll.getMaterial().getSound().playSound();
						
					}
				}
			}
			/*
			if (moving && isOnGround)
			{
				int toPlay = (new Random()).nextInt(6);
				if (toPlay == 0)
				{
					if (! mat3.playing() && ! mat4.playing())
						mat2.playSound();
				}
				else if (toPlay == 1)
				{
					if (! mat2.playing() && ! mat4.playing())
						mat3.playSound();
				}
				else if (toPlay == 2)
				{
					if (! mat2.playing() && ! mat3.playing())
						mat4.playSound();
				}
			}
			*/
		}
		catch (Exception e)
		{}
	}
	
	/**
	 * This thread handles when the player dies
	 */
	@Override
	public void run()
	{
		while (true)
		{
			if (playerEntity.health <= 0 || dead)
			{
				dead = true;
				jump.shouldRun(false);
				fall.shouldRun(false);
				left.shouldRun(false);
				right.shouldRun(false);
				idle.shouldRun(false);
				Image g = Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("dead2.png", true))
						.getScaledInstance(256, 56, Image.SCALE_FAST);
				//String name, int type, File imageFile, double x, double y, int z, int w, int h
				GuiImage e = new GuiImage("Death", Entity.STATIC, Main.getResourceHandler().getByName("dead2.png"), 
						1920 / 2 - g.getWidth(null) / 2,
						1080 / 2 - g.getHeight(null) / 2, 100, 
						256, 56);
				Main.getGuiHandler().add(e);
				long time = System.currentTimeMillis();
				while (Main.getInputListener().isAKeyPressed() && (System.currentTimeMillis() - time < 2000)) {Thread.yield();}
				while (! Main.getInputListener().isAKeyPressed() 
						&& ! Main.getInputListener().isKeyPressed(KeyBinds.PAUSE_MENU) &&
						(System.currentTimeMillis() - time < 2000)) {Thread.yield();}
				//Main.getEntityHandler().removeAllEntities();
				Main.getPhysicsHandler().setGravity(Main.getPhysicsHandler().lastGravity);
				Main.loadMap(Main.getMapName(), 0, 0);
				//Main.getTriggerHandler().disableAll();
				playerEntity.setX(Main.player_x);
				playerEntity.setY(Main.player_y);
				playerEntity.health = 100;
				//Main.getTriggerHandler().enableAll();
				//Main.loadingMap = false;
				//Main.getTriggerHandler().enableAll();
				Main.getGuiHandler().remove("Death");
				dead = false;
			}
			else
				Thread.yield();
		}
	}
	
	/**
	 * Returns the <code>Player</code>'s <code>Entity</code>
	 * 
	 * @return The <code>Player</code>'s <code>Entity</code>
	 */
	public Entity getPlayerEntity()
	{
		return playerEntity;
	}
	
	/**
	 * Sets the <code>Player</codes>'s <code>Entity</code>
	 * 
	 * @param entity The <code>Entity</code>
	 */
	public void setPlayerEntity(Entity entity)
	{
		this.playerEntity = entity;
		playerEntity.setX(Main.player_x);
		playerEntity.setY(Main.player_y);
	}
	
	/**
	 * Translates the <code>Player</code>
	 * 
	 * @param x X translation
	 * @param y Y translation
	 */
	public void translate(double x, double y)
	{
		playerEntity.translate(x, y);
	}
	
	@Override
	public String toString()
	{
		return "Player: " + playerEntity;
	}
}
