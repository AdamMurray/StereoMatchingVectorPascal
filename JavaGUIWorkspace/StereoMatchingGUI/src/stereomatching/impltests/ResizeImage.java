package stereomatching.impltests;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class ResizeImage
{
	public static void main(String[] args)
	{
		BufferedImage image = null;
		
		try
		{
			image = ImageIO.read(new File("../../images/Left1.bmp"));
		}
		catch (IOException iox)
		{
			iox.printStackTrace();
		}
		
		int height = image.getHeight();
		int width = image.getWidth();
		
		int resizedHeight = height / 10;
		int resizedWidth = width / 10;
		
		Image newerImage = image.getScaledInstance(resizedWidth, resizedHeight, Image.SCALE_AREA_AVERAGING);
		
		ImageIcon newImage = new ImageIcon(newerImage);
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		
		JPanel panel = (JPanel)frame.getContentPane();
		
		JLabel label = new JLabel();		
		label.setIcon(newImage);
		
		panel.add(label);
		
		frame.setLocationRelativeTo(null);  
        frame.pack();  
        frame.setVisible(true);  
	}
}
