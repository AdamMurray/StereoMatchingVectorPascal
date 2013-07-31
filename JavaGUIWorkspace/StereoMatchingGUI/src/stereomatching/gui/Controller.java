package stereomatching.gui;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Controller for the Stereo Matcher,
 * following the MVC pattern.
 * Controller manipulates a Model object
 * in response to user input.
 * 
 * @author Adam Murray
 * @version 0.1
 *
 */
public class Controller
{
	private StereoMatchingModel model;
	
	private File workingDirectory = new File("./");

	private FileNameExtensionFilter bmpFileFilter =
			new FileNameExtensionFilter(".bmp images", "bmp");

	public Controller(StereoMatchingModel model)
	{
		//TODO: Finish Controller constructor.
		this.model = model;
	}

	public void processLeftImageSelect()
	{
		JFileChooser leftImageChooser = new JFileChooser();
		leftImageChooser.setCurrentDirectory(workingDirectory);
		leftImageChooser.setFileFilter(bmpFileFilter);
		leftImageChooser.setDialogTitle("Select Left Stereo Image");
		int returnVal = leftImageChooser.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			model.setLeftImageFilePath(
					leftImageChooser.getSelectedFile().getAbsolutePath());
			model.setLeftImageFileName(
					leftImageChooser.getSelectedFile().getName());
			workingDirectory = leftImageChooser.getCurrentDirectory();
		}
	}

	public void processRightImageSelect()
	{
		JFileChooser rightImageChooser = new JFileChooser();
		rightImageChooser.setCurrentDirectory(workingDirectory);
		rightImageChooser.setFileFilter(bmpFileFilter);
		rightImageChooser.setDialogTitle("Select Right Stereo Image");
		int returnVal = rightImageChooser.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			model.setRightImageFilePath(
					rightImageChooser.getSelectedFile().getAbsolutePath());
			model.setRightImageFileName(
					rightImageChooser.getSelectedFile().getName());
			workingDirectory = rightImageChooser.getCurrentDirectory();
		}
	}
	
	public void processOutputImageSelect()
	{
		JFileChooser outputImageChooser = new JFileChooser();
		outputImageChooser.setCurrentDirectory(workingDirectory);
		outputImageChooser.setFileFilter(bmpFileFilter);
		outputImageChooser.setDialogTitle("Save Output Image");
		int returnVal = outputImageChooser.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			model.setOutputImageFilePath(
					outputImageChooser.getSelectedFile().getAbsolutePath());
			model.setOutputImageFileName(
					outputImageChooser.getSelectedFile().getName());
		}
		else
			throw new IllegalArgumentException();
	}
}