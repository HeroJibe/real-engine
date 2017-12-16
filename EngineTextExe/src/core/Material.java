package core;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import main.Main;

public class Material
{
	private String name;
	private File imageFile;
	private Image texture;
	private boolean imageAssociated;
	private File soundFile;
	private Clip audioClip;
	private AudioInputStream audioInputStream;
	
	public Material(String name, File soundFile)
	{
		imageAssociated = false;
		this.name = name;
		this.imageFile = imageFile;
		try
		{
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			audioClip = AudioSystem.getClip();
			audioClip.open(audioInputStream);
			this.soundFile = soundFile;
		} 
		catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) 
		{
			Main.println("Failed to create material: " + e, Color.RED);
		}
	}
	
	public Material(String name, File imageFile, File soundFile)
	{
		imageAssociated = true;
		this.name = name;
		this.imageFile = imageFile;
		try
		{
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			audioClip = AudioSystem.getClip();
			audioClip.open(audioInputStream);
			this.soundFile = soundFile;

			texture = ImageIO.read(imageFile);
		} 
		catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) 
		{
			Main.println("Failed to create material: " + e, Color.RED);
		}
	}
	
	public void playSound()
	{
		audioClip.start();
	}
}
