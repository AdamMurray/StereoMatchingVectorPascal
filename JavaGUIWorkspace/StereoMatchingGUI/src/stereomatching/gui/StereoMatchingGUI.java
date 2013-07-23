package stereomatching.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

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
	private JMenuBar menubar;
	private JPanel center, centerRight, centerLeft, south;
	private JTextField leftImageTextField, rightImageTextField; // will be changed to file choosers
	private ImageIcon leftImageIcon, rightImageIcon;
	private JButton leftImageSelectButton, rightImageSelectButton, matchButton;
	private JTextArea outputTextArea;
	private JScrollPane textAreaScrollPane;
	private JFileChooser leftImageChooser, rightImageChooser;

	private String leftImageFileName, rightImageFileName;
	private StereoMatchingController controller;

	private final int GUI_WIDTH = 530;
	private final int GUI_HEIGHT = 320;
	private final String GUI_ICONS_LOCATION = "/home/adam/Dropbox/EclipseWorkspace/StereoMatchingGUI/icons/";
	private final int OUTPUT_TEXT_AREA_ROWS = 10;
	private final int OUTPUT_TEXT_AREA_COLS = 40;

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
		setLocation(400, 100);
		setResizable(true);
	}

	private void layoutComponents()
	{
		addMenuBar();
		addComponentsToNorth();
		addComponentsToCenter();
		addComponentsToSouth();
	}

	private void addMenuBar()
	{
		menubar = new JMenuBar();
		ImageIcon icon = new ImageIcon(GUI_ICONS_LOCATION + "application-exit.png");

		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

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

		file.add(eMenuItem);

		menubar.add(file);

		setJMenuBar(menubar);
	}

	private void addComponentsToNorth()
	{
		outputTextArea = new JTextArea(OUTPUT_TEXT_AREA_ROWS, OUTPUT_TEXT_AREA_COLS);
		outputTextArea.setEditable(false);
		outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		outputTextArea.setWrapStyleWord(true);
		outputTextArea.setLineWrap(true);

		textAreaScrollPane = new JScrollPane();
		textAreaScrollPane.setBorder(new TitledBorder(new EtchedBorder(), "Output"));
		textAreaScrollPane.setViewportView(outputTextArea);

		this.add(textAreaScrollPane, BorderLayout.NORTH);
	}

	private void addComponentsToCenter()
	{
		center = new JPanel();
		center.setLayout(new GridLayout(1, 2));

		centerLeft = new JPanel();
		centerLeft.setLayout(new BorderLayout());
		centerLeft.setBorder(new TitledBorder(new EtchedBorder(), "Left Image Input"));

		leftImageIcon = new ImageIcon();
		JLabel leftImageIconLabel = new JLabel(leftImageIcon);
		centerLeft.add(leftImageIconLabel, BorderLayout.CENTER);

		leftImageSelectButton = new JButton("Select left");
		leftImageSelectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{				
				processLeftImageSelect();
			}
		});
		
		centerLeft.add(leftImageSelectButton, BorderLayout.SOUTH);

		center.add(centerLeft);
		this.add(center, BorderLayout.CENTER);

		
		centerRight = new JPanel();
		centerRight.setLayout(new BorderLayout());
		centerRight.setBorder(new TitledBorder(new EtchedBorder(), "Right Image Input"));

		rightImageIcon = new ImageIcon();
		JLabel rightImageIconLabel = new JLabel(rightImageIcon);
		centerRight.add(rightImageIconLabel, BorderLayout.CENTER);

		rightImageSelectButton = new JButton("Select right");
		rightImageSelectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{				
				processRightImageSelect();
			}
		});
		
		centerRight.add(rightImageSelectButton, BorderLayout.SOUTH);

		center.add(centerRight);
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
		controller = new StereoMatchingController(leftImageFileName,
				rightImageFileName);
		controller.runVectorPascalCode();

		for (String errorLine : controller.getErrorLines())
		{
			outputTextArea.append(errorLine + "\n");
		}

		for (String outputLine : controller.getOutputLines())
			outputTextArea.append(outputLine + "\n");
	}

	private void processLeftImageSelect()
	{
		leftImageChooser = new JFileChooser();
		int returnValLeft = leftImageChooser.showOpenDialog(this);

		if (returnValLeft == JFileChooser.APPROVE_OPTION)
		{
			leftImageFileName = leftImageChooser.getSelectedFile().getAbsolutePath() +
					leftImageChooser.getSelectedFile().getName();

			outputTextArea.append("Left image file: " +	leftImageChooser.getSelectedFile().getName() + "\n");
		}
	}

	private void processRightImageSelect()
	{
		rightImageChooser = new JFileChooser();
		int returnValRight = rightImageChooser.showOpenDialog(this);

		if (returnValRight == JFileChooser.APPROVE_OPTION)
		{
			rightImageFileName = rightImageChooser.getSelectedFile().getAbsolutePath() +
					rightImageChooser.getSelectedFile().getName();

			outputTextArea.append("Right image file: " + rightImageChooser.getSelectedFile().getName() + "\n");
		}
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
