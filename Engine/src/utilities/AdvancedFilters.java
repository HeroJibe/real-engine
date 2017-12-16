package utilities;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class AdvancedFilters
{
	public static BufferedImage flipVertical(BufferedImage src)
	{
	    AffineTransform tx = AffineTransform.getScaleInstance(-1.0,1.0);
	    tx.translate(-src.getWidth(),0);
	    AffineTransformOp tr = new AffineTransformOp(tx,null); 
	     
	    return tr.filter(src, null); 
    }
	    
	public static BufferedImage flipHorizontal(BufferedImage src)
	{
		AffineTransform tx = AffineTransform.getScaleInstance(1.0,-1.0);
		tx.translate(0,-src.getHeight());
		AffineTransformOp tr = new AffineTransformOp(tx,null);
		
		return tr.filter(src, null);
	}

	public static BufferedImage blur(BufferedImage src, int blurFac)
	{
		if (blurFac == 0)
			return src;
		int radius = blurFac;
	    int size = radius * 2 + 1;
	    float weight = 1.0f / (size * size);
	    float[] data = new float[size * size];

	    for (int i = 0; i < data.length; i++) {
	        data[i] = weight;
	    }

	    Kernel kernel = new Kernel(size, size, data);
	    ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	    //tbi is BufferedImage
	    BufferedImage i = op.filter(src, null);
		return i;
	}
	
	public static BufferedImage makeImageTranslucent(BufferedImage source, double alpha) 
	{
		    BufferedImage target = new BufferedImage(source.getWidth(), source.getHeight(), 
		    		java.awt.Transparency.TRANSLUCENT);
		    Graphics2D g = target.createGraphics();
		    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
		    g.drawImage(source, null, 0, 0);
		    g.dispose();
		    return target;
	}
	
	public static BufferedImage toCompatibleImage(BufferedImage image)
	{
	    GraphicsConfiguration gfx_config = GraphicsEnvironment.
	        getLocalGraphicsEnvironment().getDefaultScreenDevice().
	        getDefaultConfiguration();

	    if (image.getColorModel().equals(gfx_config.getColorModel()))
	        return image;

	    BufferedImage new_image = gfx_config.createCompatibleImage(
	            image.getWidth(), image.getHeight(), image.getTransparency());

	    Graphics2D g2d = (Graphics2D) new_image.getGraphics();

	    g2d.drawImage(image, 0, 0, null);
	    g2d.dispose();

	    return new_image; 
	}
}