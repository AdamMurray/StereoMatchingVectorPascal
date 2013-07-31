package stereomatching.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.*;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.imageio.ImageIO;
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
 * @version 0.1
 *
 */
@SuppressWarnings("serial")
public class StereoMatchingGUI extends JFrame
{
	// GUI Components
	private JButton runButton, openLeftImageButton, openRightImageButton;
	private JButton clearButton, saveButton, saveAsButton, exitButton;
	private JLabel leftImageFileNameLabel, rightImageFileNameLabel;
	private JLabel timeForLastMatchLabel, averageMatchTimeLabel;
	private JMenuBar menubar;
	private JPanel north, center, south;
	private JProgressBar progressBar;
	private JTextArea outputTextArea, infoTextArea, notesTextArea;	

	// GUI Image Icons
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

	// File Names and Paths
	private String outputReportFileName = null;

	// Variables for Match Calculations
	private long totalTimeForMatches;
	private double averageMatchTime;
	private int totalMatches = 0;

	// GUI Constants
	private final int GUI_WIDTH = 850;
	private final int GUI_HEIGHT = 650;

	private Task task;
	private JFrame progressBarFrame;

	// Stereo Matching Controller
	private StereoMatchingController controller;
	private StereoMatchingModel model = new StereoMatchingModel(); // could initialise this elsewhere, e.g. in the constructor
	private Controller matchController = new Controller(model);

	/**
	 * Creates a new StereoMatchingGUI object,
	 * initialises the user interface, and shows
	 * a welcome screen.
	 */
	public StereoMatchingGUI()
	{
		initialiseUI();
		layoutComponents();
		showWelcomeScreen();
	}

	/**
	 * Initialises the user interface.
	 * Sets default size, location and
	 * behaviour.
	 */
	private void initialiseUI()
	{
		setTitle("Stereo Image Matcher v0.1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setSize(GUI_WIDTH, GUI_HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Lays out the components in the
	 * user interface.
	 */
	private void layoutComponents()
	{
		addMenuBar();
		addComponentsToNorth();
		addComponentsToCenter();
		addComponentsToSouth();
		addProgressBar();
	}

	/**
	 * Creates a menu bar for the user interface.
	 */
	private void addMenuBar()
	{
		menubar = new JMenuBar();

		// Creates and adds items to the 'File'
		// section of the menu
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
				processSave();
			}
		});
		file.add(saveMenuItem);

		JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
		saveAsMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				processSaveAs();
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

		// Creates and adds items to the 'Edit'
		// section of the menu
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

		// Creates and adds items to the 'View'
		// section of the menu
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

		// Creates and adds items to the 'Run'
		// section of the menu
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

		// Creates and adds items to the 'Help'
		// section of the menu
		JMenu help = new JMenu("Help");

		JMenuItem helpPagesMenuItem = new JMenuItem("Help Pages");
		helpPagesMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				processShowHelpPages();
			}
		});
		help.add(helpPagesMenuItem);

		menubar.add(help);

		help.addSeparator();

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

	/**
	 * Creates the components to be added
	 * to the top section of the user interface.
	 */
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
		clearButton.setToolTipText("Clear All Windows");
		clearButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{				
				processClearOutput();
				processClearInfo();
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
				processSave();
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
				processSaveAs();
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

	/**
	 * Creates the components to be added to the
	 * middle section of the user interface.
	 */
	private void addComponentsToCenter()
	{
		center = new JPanel();
		center.setLayout(new GridLayout(1, 2));

		JPanel centerLeft = new JPanel();
		centerLeft.setLayout(new BorderLayout());
		center.add(centerLeft);

		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new EtchedBorder());
		centerLeft.add(outputPanel, BorderLayout.NORTH);
		JLabel outputLabel = new JLabel("Matching Program Output");
		outputPanel.add(outputLabel);

		outputTextArea = new JTextArea();
		outputTextArea.setBackground(Color.BLACK);
		outputTextArea.setForeground(Color.ORANGE);
		outputTextArea.setEditable(false);
		outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		outputTextArea.setWrapStyleWord(true);
		outputTextArea.setLineWrap(true);

		JScrollPane outputTextAreaScrollPane = new JScrollPane();
		outputTextAreaScrollPane.setViewportView(outputTextArea);
		centerLeft.add(outputTextAreaScrollPane, BorderLayout.CENTER);

		JPanel centerRight = new JPanel();		
		centerRight.setLayout(new GridLayout(2,1));
		center.add(centerRight);

		JPanel centerRightTop = new JPanel();
		centerRightTop.setLayout(new BorderLayout());
		centerRight.add(centerRightTop);

		JPanel notesPanel = new JPanel();
		notesPanel.setBorder(new EtchedBorder());
		centerRightTop.add(notesPanel, BorderLayout.NORTH);
		JLabel notesLabel = new JLabel("Notes");
		notesPanel.add(notesLabel);

		notesTextArea = new JTextArea();
		notesTextArea.setBackground(Color.WHITE);
		notesTextArea.setForeground(Color.BLACK);
		notesTextArea.setEditable(true);
		notesTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		notesTextArea.setWrapStyleWord(true);
		notesTextArea.setLineWrap(true);

		JScrollPane notesTextAreaScrollPane = new JScrollPane();
		notesTextAreaScrollPane.setViewportView(notesTextArea);
		centerRightTop.add(notesTextAreaScrollPane, BorderLayout.CENTER);

		JPanel centerRightBottom = new JPanel();
		centerRightBottom.setLayout(new BorderLayout());
		centerRight.add(centerRightBottom);		

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new EtchedBorder());
		centerRightBottom.add(infoPanel, BorderLayout.NORTH);
		JLabel infoLabel = new JLabel("Program Information");
		infoPanel.add(infoLabel);

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

	/**
	 * Creates the components to be added to the
	 * bottom section of the user interface.
	 */
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

	private void addProgressBar()
	{
		progressBar = new JProgressBar();
		progressBar.setString("Executing...");
		progressBar.setStringPainted(true);

		progressBarFrame = new JFrame();
		progressBarFrame.setSize(200, 40);
		progressBarFrame.setUndecorated(true);
		progressBarFrame.setLocationRelativeTo(this.getContentPane());
		progressBarFrame.add(progressBar);
	}

	/**
	 * Creates a welcome screen that is shown
	 * when the program is started.
	 */
	private void showWelcomeScreen()
	{
		String aboutDialog = "Welcome to the Stereo Image Matcher.\n\n" +
				"Stereo Image Matcher provides an interface" +
				" for matching a pair of stereo images." +
				"\nImages must be in .bmp format and can be selected via the" +
				"\n'File' menu or the left and right arrow icons in the button pane." +
				"\n\nFor further information, please consult the 'Help' menu.";

		JOptionPane.showMessageDialog(this, aboutDialog, "Welcome", JOptionPane.INFORMATION_MESSAGE,
				infoIcon);
	}

	class Task extends SwingWorker<Void, Void>
	{
		@Override
		public Void doInBackground()
		{
			setEnabled(false);
			progressBarFrame.setVisible(true);

			//FIXME: Need to add a method to get the 
			// outputImageFilePath from the user via
			// a JFileChooser
			controller = new StereoMatchingController(
					model.getLeftImageFilePath(),
					model.getRightImageFilePath(),
					model.getOutputImageFilePath(),
					outputTextArea);

			controller.start();

			long matchStartTime = System.currentTimeMillis();

			boolean finished = false;
			while (!finished)
			{
				if (controller.isAlive())
				{
					System.out.println("Thread not finished");
					long delayMillis = 2000;
					try
					{
						controller.join(delayMillis);
					}
					catch (InterruptedException e) {e.printStackTrace();}
				}
				else
				{
					finished = true;
					long matchEndTime = System.currentTimeMillis();
					model.setTimeForMatch(matchEndTime - matchStartTime);
					totalTimeForMatches += model.getTimeForMatch();
					averageMatchTime = (totalTimeForMatches * 1.0) / totalMatches;

					averageMatchTimeLabel.setText(String.format("%s %.2fs", "Average match time: ", averageMatchTime / 1000.0));
					timeForLastMatchLabel.setText(String.format("%s %.2fs", "Time taken for last match: ", model.getTimeForMatch() / 1000.0));

					infoTextArea.append("Time to complete match: " + model.getTimeForMatch() / 1000.0 + "s\n\n");
				}
			}	
			return null;
		}
		@Override
		public void done()
		{
			Toolkit.getDefaultToolkit().beep();
			setCursor(null);
			System.out.println("Finished execution!");
			progressBarFrame.dispose();

			setEnabled(true);
			runButton.setEnabled(true);
			openLeftImageButton.setEnabled(true);
			openRightImageButton.setEnabled(true);
			clearButton.setEnabled(true);
			saveButton.setEnabled(true);
			saveAsButton.setEnabled(true);
			exitButton.setEnabled(true);
			
			showOutputImage();
		}
	}

	/**
	 * Handles running the image matching program
	 * on the image files chosen by the user.
	 */
	private void processMatchImages()
	{
		try
		{
			if (model.getLeftImageFileName().equals(null)
					|| model.getRightImageFileName().equals(null))
				throw new NullPointerException();

			processOutputImageSelect();

			++totalMatches;

			infoTextArea.append("##### Match number: " + totalMatches + " #####\n\n");
			infoTextArea.append("Matching the following images: " +
					"\n\tLeft image: " + model.getLeftImageFileName() +
					"\n\tRight image: " + model.getRightImageFileName());

			infoTextArea.append("\n\nMatching started: " + getCurrentDate() + "\n");

			outputTextArea.append("\n##### Match number: " + totalMatches + " #####\n\n");		


			runButton.setEnabled(false);
			openLeftImageButton.setEnabled(false);
			openRightImageButton.setEnabled(false);
			clearButton.setEnabled(false);
			saveButton.setEnabled(false);
			saveAsButton.setEnabled(false);
			exitButton.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			progressBar.setIndeterminate(true);
			task = new Task();
			task.addPropertyChangeListener(new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent evt)
				{
					if ("progress".equals(evt.getPropertyName()))
					{
						progressBar.setValue(progressBar.getValue() + 1);
					}
				}
			});
			task.execute();
		}
		catch (NullPointerException npx)
		{			
			JOptionPane.showMessageDialog(this, "You must specify two images to be matched",
					"Error", JOptionPane.ERROR_MESSAGE,
					errorIcon);
		}
	}

	private void showOutputImage()
	{
		BufferedImage outputImage = null;
		try
		{
			outputImage = ImageIO.read(new File(model.getOutputImageFilePath()));
		}
		catch (IOException iox)
		{
			JOptionPane.showMessageDialog(this, "Image cannot be found or does not exist.",
					"Image I/O Error", JOptionPane.ERROR_MESSAGE,
					errorIcon);
		}

		int outputImageWidth = outputImage.getWidth();
		int outputImageHeight = outputImage.getHeight();

		int resizedWidth = outputImageWidth / 10;
		int resizedHeight = outputImageHeight / 10;

		Image outputImageToDisplay = outputImage.getScaledInstance(resizedWidth, resizedHeight, Image.SCALE_AREA_AVERAGING);

		ImageIcon outputImageIcon = new ImageIcon(outputImageToDisplay);

		JFrame outputImageFrame = new JFrame();
		outputImageFrame.setLayout(new BorderLayout());

		JPanel outputImagePanel = (JPanel)outputImageFrame.getContentPane();

		JLabel outputImageLabel = new JLabel();		
		outputImageLabel.setIcon(outputImageIcon);

		outputImagePanel.add(outputImageLabel);

		outputImageFrame.setTitle("Output Image");
		outputImageFrame.setLocationRelativeTo(null);
		outputImageFrame.pack();  
		outputImageFrame.setVisible(true);  
	}

	/**
	 * Handles user selection of the left
	 * stereo image.
	 */
	private void processLeftImageSelect()
	{
		matchController.processLeftImageSelect();
		leftImageFileNameLabel.setText("Left image file selected: " + model.getLeftImageFileName());
	}

	/**
	 * Handles user selection of the right
	 * stereo image.
	 */
	private void processRightImageSelect()
	{
		matchController.processRightImageSelect();
		rightImageFileNameLabel.setText("Right image file selected: " + model.getRightImageFileName());
	}

	/**
	 * Handles selection of the output image.
	 */
	private void processOutputImageSelect()
	{
		matchController.processOutputImageSelect();
	}

	/**
	 * Handles saving the program output and notes
	 * taken by the user during running of the program.
	 * If a save has already taken place, the new save
	 * will write to the previously chosen file.
	 */
	private void processSave()
	{
		FileWriter outputFileWriter = null;

		try
		{
			try
			{
				if (outputReportFileName == null)
				{
					processSaveAs();
				}
				else
				{
					if (outputTextArea.getText().equals("") &&
							infoTextArea.getText().equals("") &&
							notesTextArea.getText().equals(""))
						throw new IllegalArgumentException();

					outputFileWriter = new FileWriter(outputReportFileName);
					outputFileWriter.write(createReport());

					JOptionPane.showMessageDialog(this,
							"Report saved to file: '" + outputReportFileName + "'",
							"Report Saved", JOptionPane.INFORMATION_MESSAGE,
							infoIcon);
				}
			}
			finally
			{
				if (outputFileWriter != null) outputFileWriter.close();
			}
		}
		catch (IllegalArgumentException iax)
		{
			JOptionPane.showMessageDialog(this, "There is nothing to save",
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

	/**
	 * Handles saving the program output and notes
	 * taken by the user during running of the program.
	 * Even if a save has already taken place, the new
	 * save can write to a different file.
	 */
	private void processSaveAs()
	{
		FileWriter outputFileWriter = null;

		try
		{
			try
			{
				if (outputTextArea.getText().equals("") &&
						infoTextArea.getText().equals("") &&
						notesTextArea.getText().equals(""))
					throw new IllegalArgumentException();

				JFileChooser fileSaveChooser = new JFileChooser();
				fileSaveChooser.setCurrentDirectory(new File("./"));
				int returnValFileSave = fileSaveChooser.showSaveDialog(this);

				if (returnValFileSave == JFileChooser.APPROVE_OPTION)
				{
					outputReportFileName = fileSaveChooser.getSelectedFile().getName();
					File saveFile = fileSaveChooser.getSelectedFile();
					outputFileWriter = new FileWriter(saveFile);
					outputFileWriter.write(createReport());

					JOptionPane.showMessageDialog(this,
							"Report saved to file: '" + outputReportFileName + "'",
							"Report Saved", JOptionPane.INFORMATION_MESSAGE,
							infoIcon);
				}
			}
			finally
			{
				if (outputFileWriter != null) outputFileWriter.close();
			}
		}
		catch (IllegalArgumentException iax)
		{
			JOptionPane.showMessageDialog(this, "There is nothing to save",
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

	/**
	 * Creates a report from the program output,
	 * program information, and notes taken by
	 * the user, which is then written to a file
	 * when a Save or Save As is called.
	 * 
	 * @return report - the report generated.
	 */
	private String createReport()
	{
		String report;

		String reportHeadingTop = "/**********************************";
		String reportHeadingMiddle = "\n\n\tStereo Matcher Report\n\n";
		String reportHeadingCreationTime = "\tCreated: " + getCurrentDate() + "\n\n";
		String reportHeadingBottom = "***********************************/";

		String reportNotes = notesTextArea.getText();
		String reportProgramInfo = infoTextArea.getText();
		String reportProgramOutput = outputTextArea.getText();

		report = reportHeadingTop + reportHeadingMiddle +
				reportHeadingCreationTime + reportHeadingBottom +
				"\n\n" + "Notes\n" + "----------------\n\n" +
				reportNotes +
				"\n\n\n" + "Program Information\n" + "----------------\n\n" +
				reportProgramInfo +
				"\n\n\n" + "Program Output\n" + "----------------\n\n" +
				reportProgramOutput;
		return report;
	}

	/**
	 * Displays information about the program.
	 */
	private void processShowAboutDialog()
	{
		String aboutDialog = "Stereo Image Matcher\n" +
				"\nVersion: 0.1" +
				"\nAuthor: Adam Murray" + 
				"\nCopyright (c) 2013 Adam Murray. All rights reserved." +
				"\n\nThis program is a user interface for use in matching a pair of stereo images.";
		JOptionPane.showMessageDialog(this, aboutDialog, "About", JOptionPane.INFORMATION_MESSAGE,
				infoIcon);
	}

	/**
	 * Shows help pages on the program.
	 */
	private void processShowHelpPages()
	{
		//TODO complete help pages
	}

	private void processShowStatusBar()
	{
		//TODO complete processShowStatusBar
	}

	/**
	 * Handles exit from the program.
	 */
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

	/**
	 * Clears text from the program output
	 * section of the user interface.
	 */
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

	/**
	 * Clears text from the program information
	 * section of the user interface.
	 */
	private void processClearInfo()
	{
		if (JOptionPane.showConfirmDialog(this,
				"Are you sure you want to clear the information output?",
				"Confirm Clear Info",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				attentionIcon) == JOptionPane.YES_OPTION)
		{
			infoTextArea.setText("");
		}
	}

	/**
	 * Returns the current date and time as a
	 * formatted string in the form:
	 * DD/MM/YYYY HH:MM:SS.
	 * 
	 * @return dateString - a string representing current date and time.
	 */
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
