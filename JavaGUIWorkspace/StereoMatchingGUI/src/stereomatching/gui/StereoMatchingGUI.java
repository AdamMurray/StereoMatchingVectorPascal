package stereomatching.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Class that defines a GUI for handling user input.
 * Two files are chosen, one for the left image of a
 * stereo pair and one for the right image. The images
 * are displayed (if possible to do this) as image icons
 * in the GUI. When the match button is pressed the
 * GUI passes control to the StereoMatchingController
 * class, which then passes control to a bash script
 * which runs the Vector Pascal matching code.
 * 
 * Possible additions if time:
 *     > A combo box to select matching metric (if I can
 *     code up the VP in time)
 *     > A number of other things to work on single images
 * 
 * @author Adam Murray
 *
 */
@SuppressWarnings("serial")
public class StereoMatchingGUI extends JFrame
{
	/* Instance variables */
	private JMenuBar menubar;
	private JPanel east, west, south;
	private JTextField leftImageTextField, rightImageTextField; // will be changed to file choosers
	private JLabel leftImageLabel, rightImageLabel;
	private ImageIcon leftImageIcon, rightImageIcon;
	private JButton matchButton;
	private JFileChooser leftImageChooser, rightImageChooser;
	private String leftImageFileName, rightImageFileName;
	
	private StereoMatchingController controller;

	/* Class constants */
	private final static int GUI_WIDTH = 400;
	private final static int GUI_HEIGHT = 500;
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
	}

	private void layoutComponents()
	{
		addMenuBar();
		addComponentsToWest();
		addComponentsToEast();
		addComponentsToSouth();
	}

	private void addMenuBar()
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

	private void addComponentsToWest()
	{
		west = new JPanel();
		west.setLayout(new BorderLayout());
		
		leftImageLabel = new JLabel("Left Image");
		west.add(leftImageLabel, BorderLayout.NORTH);
		
		leftImageIcon = new ImageIcon();
		JLabel leftImageIconLabel = new JLabel(leftImageIcon);
		west.add(leftImageIconLabel, BorderLayout.CENTER);
		
		leftImageTextField = new JTextField();
		west.add(leftImageTextField, BorderLayout.SOUTH);
		
		this.add(west, BorderLayout.WEST);
	}
	
	private void addComponentsToEast()
	{
		east = new JPanel();
		east.setLayout(new BorderLayout());
		
		rightImageLabel = new JLabel("Right Image");
		east.add(rightImageLabel, BorderLayout.NORTH);
		
		rightImageIcon = new ImageIcon();
		JLabel rightImageIconLabel = new JLabel(rightImageIcon);
		east.add(rightImageIconLabel, BorderLayout.CENTER);
		
		rightImageTextField = new JTextField();
		east.add(rightImageTextField, BorderLayout.SOUTH);
		
		this.add(east, BorderLayout.EAST);
	}
	
	private void addComponentsToSouth()
	{
		south = new JPanel();
		
		matchButton = new JButton("Match Images");
		matchButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{				
				processMatchButton();
			}
		});
		
		south.add(matchButton);
		this.add(south, BorderLayout.SOUTH);
	}
	
	private void processMatchButton()
	{
		leftImageFileName = leftImageTextField.getText().trim();
		rightImageFileName = rightImageTextField.getText().trim();
		
		controller = new StereoMatchingController(leftImageFileName,
												rightImageFileName);
		controller.runVectorPascalCode();
		
		clearTextFields();
	}
	
	private void clearTextFields()
	{
		leftImageTextField.setText("");
		rightImageTextField.setText("");
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
