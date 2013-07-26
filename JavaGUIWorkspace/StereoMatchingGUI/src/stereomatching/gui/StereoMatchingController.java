package stereomatching.gui;

import javax.swing.*;

/**
 * Class that handles processing information
 * taken from the GUI and using it in order
 * to run the Vector Pascal source code
 * to match a pair of stereo images.
 * 
 * @author Adam Murray
 *
 */
public class StereoMatchingController extends Thread
{
	private JTextArea outputTextArea;
	
	private String leftImageFileName, rightImageFileName;
	private String [] runVectorPascalCode;

	/**
	 * Creates a StereoMatchingController object using
	 * two image file names chosen by the user.
	 * 
	 * @param leftImage - file name of the left stereo image.
	 * @param rightImage - file name of the right stereo image.
	 */
	public StereoMatchingController(String leftImage,
			String rightImage,
			JTextArea output)
	{
		leftImageFileName = leftImage;
		rightImageFileName = rightImage;
		outputTextArea = output;
	}

	/**
	 * Runs a bash script which is passed the left
	 * and right image file names as parameters,
	 * which in turn runs code written in Vector
	 * Pascal that performs stereo matching on
	 * the two images.
	 */	
	public void run()
	{
		try
		{
			String bashScriptLocation = "/home/adam/Dropbox/VectorPascal/MastersProjectPrograms/JavaGUIWorkspace/";

			runVectorPascalCode = new String[3];
			runVectorPascalCode[0] = bashScriptLocation + "run_vector_pascal_code";
			runVectorPascalCode[1] = leftImageFileName;
			runVectorPascalCode[2] = rightImageFileName;
			
			ProcessBuilder processBuilder = new ProcessBuilder(runVectorPascalCode);
			Process process = processBuilder.start();
			
			StreamGobbler errorGobbler = new 
					StreamGobbler(process.getErrorStream(), "ERROR", outputTextArea);            

			StreamGobbler outputGobbler = new 
					StreamGobbler(process.getInputStream(), ">", outputTextArea);

			errorGobbler.start();
			outputGobbler.start();
			
			int exitVal = process.waitFor();
			System.out.println("ExitValue: " + exitVal);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
}
