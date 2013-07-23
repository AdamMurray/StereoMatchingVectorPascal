package stereomatching.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
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
	private JPanel north, center, southRight, southLeft, south;
	private JLabel leftImageFileNameLabel, rightImageFileNameLabel;
	private JButton leftImageSelectButton, rightImageSelectButton, matchButton;
	private JButton runButton, clearButton, saveButton;
	private JTextArea outputTextArea;
	private JScrollPane textAreaScrollPane;
	private JFileChooser leftImageChooser, rightImageChooser;

	private String leftImageFileName, rightImageFileName;
	private String leftImageFilePath, rightImageFilePath;
	private StereoMatchingController controller;

	private final int GUI_WIDTH = 530;
	private final int GUI_HEIGHT = 450;
	private final String GUI_ICONS_LOCATION = "/home/adam/Dropbox/EclipseWorkspace/StereoMatchingGUI/gui_icons/";
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

		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		JMenuItem openLeftImageMenuItem = new JMenuItem("Open left image...");
		openLeftImageMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				processLeftImageSelect();
			}
		});
		
		file.add(openLeftImageMenuItem);
		
		JMenuItem openRightImageMenuItem = new JMenuItem("Open right image...");
		openRightImageMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				processRightImageSelect();
			}
		});
		
		file.add(openRightImageMenuItem);
		
		JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
		saveAsMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				processSaveOutput();
			}
		});
		
		file.add(saveAsMenuItem);
		
		//		ImageIcon exitIcon = new ImageIcon("./gui_icons/close_delete.png");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
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

		JMenuItem clearTextAreaItem = new JMenuItem("Clear Output");
		clearTextAreaItem.setToolTipText("Clears the output");
		clearTextAreaItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				clearTextArea();
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
		north = new JPanel();
		north.setLayout(new FlowLayout((int) LEFT_ALIGNMENT));

		BufferedImage runButtonIcon;
		try
		{
			runButtonIcon = ImageIO.read(new File("./gui_icons/play.png"));
			runButton = new JButton(new ImageIcon(runButtonIcon));
			runButton.setBorder(BorderFactory.createEmptyBorder());
			runButton.setContentAreaFilled(false);
			runButton.setToolTipText("Press to initiate matching of selected stereo image pair");
			runButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{				
					processMatchButton();
				}
			});
		}
		catch (IOException iox)
		{
			iox.printStackTrace();
		}
		north.add(runButton);

		BufferedImage clearButtonIcon;
		try
		{
			clearButtonIcon = ImageIO.read(new File("./gui_icons/eraser.png"));
			clearButton = new JButton(new ImageIcon(clearButtonIcon));
			clearButton.setBorder(BorderFactory.createEmptyBorder());
			clearButton.setContentAreaFilled(false);
			clearButton.setToolTipText("Press to clear output");
			clearButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{				
					clearTextArea();
				}
			});
		}
		catch (IOException iox)
		{
			iox.printStackTrace();
		}
		north.add(clearButton);
		
		BufferedImage saveButtonIcon;
		try
		{
			saveButtonIcon = ImageIO.read(new File("./gui_icons/save_as.png"));
			saveButton = new JButton(new ImageIcon(saveButtonIcon));
			saveButton.setBorder(BorderFactory.createEmptyBorder());
			saveButton.setContentAreaFilled(false);
			saveButton.setToolTipText("Press to save output");
			saveButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{				
					processSaveOutput();
				}
			});
		}
		catch (IOException iox)
		{
			iox.printStackTrace();
		}
		north.add(saveButton);
		

		this.add(north, BorderLayout.NORTH);
	}

	private void addComponentsToCenter()
	{
		outputTextArea = new JTextArea(OUTPUT_TEXT_AREA_ROWS, OUTPUT_TEXT_AREA_COLS);
		outputTextArea.setEditable(false);
		outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		outputTextArea.setWrapStyleWord(true);
		outputTextArea.setLineWrap(true);

		textAreaScrollPane = new JScrollPane();
		textAreaScrollPane.setBorder(new TitledBorder(new EtchedBorder(), "Program Output"));
		textAreaScrollPane.setViewportView(outputTextArea);

		this.add(textAreaScrollPane, BorderLayout.CENTER);
	}

	private void addComponentsToSouth()
	{
		south = new JPanel();
		south.setLayout(new FlowLayout((int) LEFT_ALIGNMENT));
		south.setBorder(new TitledBorder(new EtchedBorder()));

		leftImageFileNameLabel = new JLabel("Program started: " + getCurrentDate());
		leftImageFileNameLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
		south.add(leftImageFileNameLabel);

		this.add(south, BorderLayout.SOUTH);
	}

	private void showWelcomeText()
	{
		FileReader welcomeTextFileReader = null;

		try
		{
			try
			{
				welcomeTextFileReader = new FileReader("welcome_text.txt");

				Scanner in = new Scanner(welcomeTextFileReader);

				while (in.hasNextLine())
				{
					outputTextArea.append(in.nextLine() + "\n");
				}

				in.close();
			}			
			finally
			{
				if (welcomeTextFileReader != null) welcomeTextFileReader.close();
			}
		}
		catch (IOException iox)
		{
			System.err.println("Welcome file cannot be opened");
		}

		outputTextArea.append("\nProgram started: " + getCurrentDate() + "\n\n");
	}

	private void processMatchButton()
	{
		try
		{
			if (leftImageFileName.equals(null) || rightImageFileName.equals(null))
				throw new NullPointerException();

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
		catch (NullPointerException npx)
		{
			JOptionPane.showMessageDialog(this, "You must specify two images to be matched",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void processLeftImageSelect()
	{
		leftImageChooser = new JFileChooser();
		int returnValLeft = leftImageChooser.showOpenDialog(this);

		if (returnValLeft == JFileChooser.APPROVE_OPTION)
		{
			leftImageFilePath = leftImageChooser.getSelectedFile().getAbsolutePath();
			leftImageFileName = leftImageChooser.getSelectedFile().getName();
			leftImageFileNameLabel.setText("Selected file: " + leftImageFileName);
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
			rightImageFileNameLabel.setText("Selected file: " + rightImageFileName);
		}
	}

	private void processSaveOutput()
	{
		//TODO complete processSaveOutput()
	}
	
	private void clearTextArea()
	{
		outputTextArea.setText("");
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
