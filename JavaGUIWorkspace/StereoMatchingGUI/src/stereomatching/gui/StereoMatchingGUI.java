package stereomatching.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

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
	private JPanel north, center, south;
	private JLabel leftImageFileNameLabel, rightImageFileNameLabel;
	private JLabel timeForLastMatchLabel, averageMatchTimeLabel;
	private JButton runButton, openLeftImageButton, openRightImageButton;
	private JButton clearButton, saveButton, saveAsButton, exitButton;
	private JTextArea outputTextArea, infoTextArea;
	private JScrollPane outputTextAreaScrollPane;
	private JFileChooser leftImageChooser, rightImageChooser;

	private ImageIcon attentionIcon = new ImageIcon("./gui_icons/attention.png");
	private ImageIcon clearIcon = new ImageIcon("./gui_icons/eraser.png");
	private ImageIcon errorIcon = new ImageIcon("./gui_icons/close_delete.png");
	private ImageIcon exitIcon = new ImageIcon("./gui_icons/delete_2.png");
	private ImageIcon infoIcon = new ImageIcon("./gui_icons/information.png");
	private ImageIcon openLeftIcon = new ImageIcon("./gui_icons/arrow_left.png");
	private ImageIcon openRightIcon = new ImageIcon("./gui_icons/arrow_right.png");
	private ImageIcon runIcon = new ImageIcon("./gui_icons/play.png");
	private ImageIcon saveIcon = new ImageIcon("./gui_icons/save_diskette_floppy_disk.png");
	private ImageIcon saveAsIcon = new ImageIcon("./gui_icons/save_as.png");

	private String leftImageFileName, rightImageFileName;
	private String leftImageFilePath, rightImageFilePath;
	private String outputFileName;
	private StereoMatchingController controller;
	
	private long matchStartTime, matchEndTime, matchTotalTime;
	private long totalMatchTime;
	private double averageMatchTime;
	private int totalMatches;
	
	private final int GUI_WIDTH = 850;
	private final int GUI_HEIGHT = 550;

	public StereoMatchingGUI()
	{
		initialiseUI();
		layoutComponents();
	}

	private void initialiseUI()
	{
		setTitle("Stereo Image Matcher v0.1");
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

		file.addSeparator();
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		saveMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				processSaveOutput();
			}
		});

		file.add(saveMenuItem);

		JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
		saveAsMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				processSaveAsOutput();
			}
		});

		file.add(saveAsMenuItem);

		file.addSeparator();
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setMnemonic(KeyEvent.VK_E);
		exitMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				processExitProgram();
			}
		});

		file.add(exitMenuItem);
		menubar.add(file);

		JMenu edit = new JMenu("Edit");

		JMenuItem processClearInfoItem = new JMenuItem("Clear Info");
		processClearInfoItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				processClearInfo();
			}
		});

		edit.add(processClearInfoItem);
		
		JMenuItem processClearOutputItem = new JMenuItem("Clear Output");
		processClearOutputItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				processClearOutput();
			}
		});

		edit.add(processClearOutputItem);
		menubar.add(edit);

		JMenu view = new JMenu("View");

		JMenuItem showStatusBarItem = new JMenuItem("Show Status Bar");
		processClearOutputItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				processShowStatusBar();
			}
		});

		view.add(showStatusBarItem);
		menubar.add(view);

		JMenu run = new JMenu("Run");

		JMenuItem runMatchingItem = new JMenuItem("Run");
		runMatchingItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				processMatchImages();
			}
		});

		run.add(runMatchingItem);
		menubar.add(run);

		JMenu help = new JMenu("Help");

		JMenuItem aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				processShowAboutDialog();
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


		runButton = new JButton(runIcon);
		runButton.setBorder(BorderFactory.createEmptyBorder());
		runButton.setContentAreaFilled(false);
		runButton.setToolTipText("Run Stereo Matching");
		runButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{				
				processMatchImages();
			}
		});
		north.add(runButton);


		openLeftImageButton = new JButton(openLeftIcon);
		openLeftImageButton.setBorder(BorderFactory.createEmptyBorder());
		openLeftImageButton.setContentAreaFilled(false);
		openLeftImageButton.setToolTipText("Open Left Image");
		openLeftImageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{				
				processLeftImageSelect();
			}
		});
		north.add(openLeftImageButton);


		openRightImageButton = new JButton(openRightIcon);
		openRightImageButton.setBorder(BorderFactory.createEmptyBorder());
		openRightImageButton.setContentAreaFilled(false);
		openRightImageButton.setToolTipText("Open Right Image");
		openRightImageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{				
				processRightImageSelect();
			}
		});
		north.add(openRightImageButton);


		clearButton = new JButton(clearIcon);
		clearButton.setBorder(BorderFactory.createEmptyBorder());
		clearButton.setContentAreaFilled(false);
		clearButton.setToolTipText("Clear Output");
		clearButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{				
				processClearOutput();
			}
		});
		north.add(clearButton);


		saveButton = new JButton(saveIcon);
		saveButton.setBorder(BorderFactory.createEmptyBorder());
		saveButton.setContentAreaFilled(false);
		saveButton.setToolTipText("Save (Ctrl+S)");
		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{				
				processSaveAsOutput();
			}
		});
		north.add(saveButton);


		saveAsButton = new JButton(saveAsIcon);
		saveAsButton.setBorder(BorderFactory.createEmptyBorder());
		saveAsButton.setContentAreaFilled(false);
		saveAsButton.setToolTipText("Save As...");
		saveAsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{				
				processSaveAsOutput();
			}
		});
		north.add(saveAsButton);

		exitButton = new JButton(exitIcon);
		exitButton.setBorder(BorderFactory.createEmptyBorder());
		exitButton.setContentAreaFilled(false);
		exitButton.setToolTipText("Exit");
		exitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{				
				processExitProgram();
			}
		});
		north.add(exitButton);

		this.add(north, BorderLayout.NORTH);
	}

	private void addComponentsToCenter()
	{
		center = new JPanel();
		center.setLayout(new GridLayout(1, 2));
		
		JPanel centerLeft = new JPanel();
		centerLeft.setLayout(new BorderLayout());
		centerLeft.setBorder(new EtchedBorder());
		center.add(centerLeft);
		
		outputTextArea = new JTextArea();
		outputTextArea.setBackground(Color.BLACK);
		outputTextArea.setForeground(Color.ORANGE);
		outputTextArea.setEditable(false);
		outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		outputTextArea.setWrapStyleWord(true);
		outputTextArea.setLineWrap(true);

		outputTextAreaScrollPane = new JScrollPane();
		//		outputTextAreaScrollPane.setBorder(new TitledBorder(new EtchedBorder(), "Program Output"));
		outputTextAreaScrollPane.setViewportView(outputTextArea);
		centerLeft.add(outputTextAreaScrollPane, BorderLayout.CENTER);
		
		JPanel centerRight = new JPanel();		
		centerRight.setLayout(new GridLayout(2,1));
		center.add(centerRight);
		
		JPanel centerRightTop = new JPanel();
		centerRightTop.setLayout(new GridLayout(8, 1));
		centerRightTop.setBorder(new EtchedBorder());
		centerRight.add(centerRightTop);
		
		JPanel centerRightBottom = new JPanel();
		centerRightBottom.setLayout(new BorderLayout());
		centerRightBottom.setBorder(new EtchedBorder());
		centerRight.add(centerRightBottom);		
		
		infoTextArea = new JTextArea();
		infoTextArea.setBackground(Color.BLACK);
		infoTextArea.setForeground(Color.ORANGE);
		infoTextArea.setEditable(false);
		infoTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		infoTextArea.setWrapStyleWord(true);
		infoTextArea.setLineWrap(true);
		
		JScrollPane infoTextAreaScrollPane = new JScrollPane();
		infoTextAreaScrollPane.setViewportView(infoTextArea);
		centerRightBottom.add(infoTextAreaScrollPane, BorderLayout.CENTER);
		
		this.add(center, BorderLayout.CENTER);
	}

	private void addComponentsToSouth()
	{
		south = new JPanel();
		south.setLayout(new GridLayout(2, 2));
		south.setBorder(new TitledBorder(new EtchedBorder()));

		leftImageFileNameLabel = new JLabel("Left image file selected: ----");
		leftImageFileNameLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
		south.add(leftImageFileNameLabel);

		timeForLastMatchLabel = new JLabel("Time taken for last match: ----");
		timeForLastMatchLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
		south.add(timeForLastMatchLabel);
		
		rightImageFileNameLabel = new JLabel("Right image file selected: ----");
		rightImageFileNameLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
		south.add(rightImageFileNameLabel);
		
		averageMatchTimeLabel = new JLabel("Average match time: ----");
		averageMatchTimeLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
		south.add(averageMatchTimeLabel);
		
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
					infoTextArea.append(in.nextLine() + "\n");
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

		infoTextArea.append("\n\tProgram started: " + getCurrentDate() + "\n\n");
	}

	private void processMatchImages()
	{
		try
		{
			if (leftImageFileName.equals(null) || rightImageFileName.equals(null))
				throw new NullPointerException();

			infoTextArea.append("Matching the following images: " +
					"\n\tLeft image: " + leftImageFileName +
					"\n\tRight image: " + rightImageFileName);
			infoTextArea.append("\n\nMatching started: " + getCurrentDate() + "\n");

			controller = new StereoMatchingController(
					leftImageFilePath + leftImageFileName,
					rightImageFilePath + rightImageFileName);
			
			matchStartTime = System.currentTimeMillis();
			
			controller.runVectorPascalCode();
			
			matchEndTime = System.currentTimeMillis();

			for (String errorLine : controller.getErrorLines())
				outputTextArea.append(errorLine + "\n");

			for (String outputLine : controller.getOutputLines())
				outputTextArea.append(outputLine + "\n");
			
			++totalMatches;
			matchTotalTime = matchEndTime - matchStartTime;
			totalMatchTime += matchTotalTime;
			averageMatchTime = (totalMatchTime * 1.0) / totalMatches;
			
			averageMatchTimeLabel.setText(String.format("%s %.2fms", "Average match time: ", averageMatchTime));
			timeForLastMatchLabel.setText(String.format("%s %dms", "Time taken for last match: ",
					matchTotalTime));
		}
		catch (NullPointerException npx)
		{			
			JOptionPane.showMessageDialog(this, "You must specify two images to be matched",
					"Error", JOptionPane.ERROR_MESSAGE,
					errorIcon);
		}
	}

	private void processLeftImageSelect()
	{
		leftImageChooser = new JFileChooser();
		leftImageChooser.setCurrentDirectory(new File("./"));
		int returnValLeft = leftImageChooser.showOpenDialog(this);

		if (returnValLeft == JFileChooser.APPROVE_OPTION)
		{
			leftImageFilePath = leftImageChooser.getSelectedFile().getAbsolutePath();
			leftImageFileName = leftImageChooser.getSelectedFile().getName();
			leftImageFileNameLabel.setText("Left image file selected: " + leftImageFileName);
		}
	}

	private void processRightImageSelect()
	{
		rightImageChooser = new JFileChooser();
		rightImageChooser.setCurrentDirectory(new File("./"));
		int returnValRight = rightImageChooser.showOpenDialog(this);

		if (returnValRight == JFileChooser.APPROVE_OPTION)
		{
			rightImageFilePath = rightImageChooser.getSelectedFile().getAbsolutePath();
			rightImageFileName = rightImageChooser.getSelectedFile().getName();
			rightImageFileNameLabel.setText("Right image file selected: " + rightImageFileName);
		}
	}

	private void processSaveOutput()
	{
		FileWriter outputFileWriter = null;

		try
		{
			try
			{
				if (outputFileName == null)
				{
					processSaveAsOutput();
				}
				else
				{
					if (outputTextArea.getText().equals(""))
						throw new IllegalArgumentException();

					outputFileWriter = new FileWriter(outputFileName);

					outputFileWriter.write(outputTextArea.getText());
				}
			}
			finally
			{
				if (outputFileWriter != null) outputFileWriter.close();
			}
		}
		catch (IllegalArgumentException iax)
		{
			JOptionPane.showMessageDialog(this, "There is no output to save",
					"No Output", JOptionPane.INFORMATION_MESSAGE,
					attentionIcon);
		}
		catch (IOException iox)
		{
			JOptionPane.showMessageDialog(this, "File could not be written to",
					"Error", JOptionPane.ERROR_MESSAGE,
					errorIcon);
		}
	}

	private void processSaveAsOutput()
	{
		outputFileName = null;
		FileWriter outputFileWriter = null;

		try
		{
			try
			{
				if (outputTextArea.getText().equals(""))
					throw new IllegalArgumentException();

				JFileChooser fileSaveChooser = new JFileChooser();
				fileSaveChooser.setCurrentDirectory(new File("./"));
				int returnValFileSave = fileSaveChooser.showSaveDialog(this);

				if (returnValFileSave == JFileChooser.APPROVE_OPTION)
				{
					outputFileName = fileSaveChooser.getSelectedFile().getName();
					File saveFile = fileSaveChooser.getSelectedFile();
					outputFileWriter = new FileWriter(saveFile);
					outputFileWriter.write(outputTextArea.getText());
				}
			}
			finally
			{
				if (outputFileWriter != null) outputFileWriter.close();
			}
		}
		catch (IllegalArgumentException iax)
		{
			JOptionPane.showMessageDialog(this, "There is no output to save",
					"No Output", JOptionPane.INFORMATION_MESSAGE,
					attentionIcon);
		}
		catch (IOException iox)
		{
			JOptionPane.showMessageDialog(this, "File could not be written to",
					"Error", JOptionPane.ERROR_MESSAGE,
					errorIcon);
		}
		catch (NullPointerException npx)
		{
			JOptionPane.showMessageDialog(this, "You must enter an output file name",
					"Error", JOptionPane.ERROR_MESSAGE,
					errorIcon);
		}
	}

	private void processShowAboutDialog()
	{
		String aboutDialog = "Stereo Image Matcher" +
				"\nVersion: 0.1" +
				"\n\nThis program is a user interface for use in matching a pair of stereo images.";
		JOptionPane.showMessageDialog(this, aboutDialog, "About", JOptionPane.INFORMATION_MESSAGE,
				infoIcon);
	}

	private void processShowStatusBar()
	{
		//TODO complete processShowStatusBar
	}

	private void processExitProgram()
	{
		if (JOptionPane.showConfirmDialog(this,
				"Are you sure you want to exit?",
				"Confirm Exit",
		        JOptionPane.YES_NO_OPTION,
		        JOptionPane.INFORMATION_MESSAGE,
		        attentionIcon) == JOptionPane.YES_OPTION)
		{
		    System.exit(0);
		}
	}
	
	private void processClearOutput()
	{
		if (JOptionPane.showConfirmDialog(this,
				"Are you sure you want to clear the output?",
				"Confirm Clear Output",
		        JOptionPane.YES_NO_OPTION,
		        JOptionPane.INFORMATION_MESSAGE,
		        attentionIcon) == JOptionPane.YES_OPTION)
		{
			outputTextArea.setText("");
		}
	}

	private void processClearInfo()
	{
		if (JOptionPane.showConfirmDialog(this,
				"Are you sure you want to clear the information output?",
				"Confirm Clear Output",
		        JOptionPane.YES_NO_OPTION,
		        JOptionPane.INFORMATION_MESSAGE,
		        attentionIcon) == JOptionPane.YES_OPTION)
		{
			infoTextArea.setText("");
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
