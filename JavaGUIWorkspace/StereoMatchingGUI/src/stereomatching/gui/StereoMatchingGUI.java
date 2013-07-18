package stereomatching.gui;

import java.awt.event.*;
import javax.swing.*;

/**
 * Class that defines a GUI for handling user input.
 * 
 * 
 * @author Adam Murray
 *
 */
@SuppressWarnings("serial")
public class StereoMatchingGUI extends JFrame
{
	/* Instance variables */
	private JMenuBar menubar;
	
	/* Class constants */
	private final static int GUI_WIDTH = 1000;
	private final static int GUI_HEIGHT = 800;
//	private final int GUI_X_POSITION = 500;
//	private final int GUI_Y_POSITION = 500;
	private final String GUI_ICONS_LOCATION = "/home/adam/Dropbox/EclipseWorkspace/StereoMatchingGUI/icons/";
	
	/*
	 * Constructor
	 */
	public StereoMatchingGUI()
	{
		initialiseUI();
		layoutComponents();
	}
	
	private void initialiseUI()
	{
		setTitle("Stereo Matching Program");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setLocationByPlatform(true);
		setSize(GUI_WIDTH, GUI_HEIGHT);
		setResizable(true);
		setVisible(true);
	}
	
	private void layoutComponents()
	{
		// Create menu bar component
		menubar = new JMenuBar();
		// Create image icon component as an exit image
		ImageIcon icon = new ImageIcon(GUI_ICONS_LOCATION + "application-exit.png");
		
		// Create a menu component, name it
		JMenu file = new JMenu("File");
		// Set mnemonic of the menu
		file.setMnemonic(KeyEvent.VK_F);
		
		// Create menu item component
		JMenuItem eMenuItem = new JMenuItem("Exit", icon);
		eMenuItem.setMnemonic(KeyEvent.VK_E);
		eMenuItem.setToolTipText("Exit application");
		eMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});
		
		// Add menu component to file menu
		file.add(eMenuItem);
		
		// Add menu to menu bar
		menubar.add(file);
		
		// Set the menu bar in the frame
		setJMenuBar(menubar);
	}
	
	/**
	 * Main method.
	 * Causes the EDT to be invoked.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				StereoMatchingGUI gui = new StereoMatchingGUI();
				gui.setVisible(true);
			}
		});
	}
}
