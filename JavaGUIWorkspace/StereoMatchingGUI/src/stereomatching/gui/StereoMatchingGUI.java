package stereomatching.gui;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.Date;

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
	private JLabel leftImageFileNameLabel, rightImageFileNameLabel;
	private JButton leftImageSelectButton, rightImageSelectButton, matchButton;
	private JTextArea outputTextArea;
	private JScrollPane textAreaScrollPane;
	private JFileChooser leftImageChooser, rightImageChooser;

	private String leftImageFileName, rightImageFileName;
	private String leftImageFilePath, rightImageFilePath;
	private StereoMatchingController controller;

	private final int GUI_WIDTH = 530;
	private final int GUI_HEIGHT = 450;
	private final String GUI_ICONS_LOCATION = "/home/adam/Dropbox/EclipseWorkspace/StereoMatchingGUI/icons/";
	private final int OUTPUT_TEXT_AREA_ROWS = 16;
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
		setLocation(500, 100);
		setResizable(true);
	}

	private void layoutComponents()
	{
		addMenuBar();
		addComponentsToNorth();
		addComponentsToCenter();
		addComponentsToSouth();
		showWelcomeText();
	}

	private void addMenuBar()
	{
		menubar = new JMenuBar();
		ImageIcon exitIcon = new ImageIcon(GUI_ICONS_LOCATION + "application-exit.png");

		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		JMenuItem exitMenuItem = new JMenuItem("Exit", exitIcon);
		exitMenuItem.setMnemonic(KeyEvent.VK_E);
		exitMenuItem.setToolTipText("Exit application");
		exitMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});

		file.add(exitMenuItem);

		menubar.add(file);

		JMenu edit = new JMenu("Edit");

		JMenuItem clearTextAreaItem = new JMenuItem("Clear Text Area");
		clearTextAreaItem.setToolTipText("Clears the output text area");
		clearTextAreaItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				outputTextArea.setText("");
			}
		});

		edit.add(clearTextAreaItem);
		menubar.add(edit);

		JMenu help = new JMenu("Help");

		JMenuItem aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.setToolTipText("About application");
		aboutMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				//TODO add method for menu bar "About" button
			}
		});

		help.add(aboutMenuItem);
		menubar.add(help);

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

		leftImageFileNameLabel = new JLabel("Selected file: ");
		centerLeft.add(leftImageFileNameLabel, BorderLayout.NORTH);

		leftImageSelectButton = new JButton("Open file...");
		leftImageSelectButton.setToolTipText("Select the left image of the stereo pair to be matched");
		leftImageSelectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{				
				processLeftImageSelect();
				leftImageFileNameLabel.setText("Selected file: " + leftImageFileName);
			}
		});

		centerLeft.add(leftImageSelectButton, BorderLayout.SOUTH);

		center.add(centerLeft);
		this.add(center, BorderLayout.CENTER);


		centerRight = new JPanel();
		centerRight.setLayout(new BorderLayout());
		centerRight.setBorder(new TitledBorder(new EtchedBorder(), "Right Image Input"));

		rightImageFileNameLabel = new JLabel("Selected file: ");
		centerRight.add(rightImageFileNameLabel, BorderLayout.NORTH);

		rightImageSelectButton = new JButton("Open file...");
		rightImageSelectButton.setToolTipText("Select the right image of the stereo pair to be matched");
		rightImageSelectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{				
				processRightImageSelect();
				rightImageFileNameLabel.setText("Selected file: " + rightImageFileName);
			}
		});

		centerRight.add(rightImageSelectButton, BorderLayout.SOUTH);

		center.add(centerRight);
	}

	private void addComponentsToSouth()
	{
		south = new JPanel();

		matchButton = new JButton("Match Images");
		matchButton.setToolTipText("Press to initiate matching of selected stereo image pair");
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

	private void showWelcomeText()
	{
		outputTextArea.append("Program started: " + getCurrentDate() + "\n\n");
	}

	private void processMatchButton()
	{
		outputTextArea.append("Matching the following images: " +
				"\n\tLeft image: " + leftImageFileName +
				"\n\tRight image: " + rightImageFileName);
		outputTextArea.append("\n\nMatching started: " + getCurrentDate() + "\n");

		controller = new StereoMatchingController(
				leftImageFilePath + leftImageFileName,
				rightImageFilePath + rightImageFileName);
		controller.runVectorPascalCode();

		for (String errorLine : controller.getErrorLines())
			outputTextArea.append(errorLine + "\n");

		for (String outputLine : controller.getOutputLines())
			outputTextArea.append(outputLine + "\n");
	}

	private void processLeftImageSelect()
	{
		leftImageChooser = new JFileChooser();
		int returnValLeft = leftImageChooser.showOpenDialog(this);

		if (returnValLeft == JFileChooser.APPROVE_OPTION)
		{
			leftImageFilePath = leftImageChooser.getSelectedFile().getAbsolutePath();
			leftImageFileName = leftImageChooser.getSelectedFile().getName();
		}
	}

	private void processRightImageSelect()
	{
		rightImageChooser = new JFileChooser();
		int returnValRight = rightImageChooser.showOpenDialog(this);

		if (returnValRight == JFileChooser.APPROVE_OPTION)
		{
			rightImageFilePath = rightImageChooser.getSelectedFile().getAbsolutePath();
			rightImageFileName = rightImageChooser.getSelectedFile().getName();
		}
	}

	private String getCurrentDate()
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String dateString = dateFormat.format(date).toString();
		return dateString;
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
