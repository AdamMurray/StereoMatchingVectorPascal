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
	
	private String leftImageFileName, rightImageFileName, outputImageFileName;
	private String [] runVectorPascalCode;

	/**
	 * Creates a StereoMatchingController object using
	 * two image file names chosen by the user.
	 * 
	 * @param leftImage - file name of the left stereo image.
	 * @param rightImage - file name of the right stereo image.
	 * @param outputImage - file name of the output image.
	 * @param output - GUI text area to display output.
	 */
	public StereoMatchingController(String leftImage,
			String rightImage,
			String outputImage,
			JTextArea output)
	{
		leftImageFileName = leftImage;
		rightImageFileName = rightImage;
		outputImageFileName = outputImage;
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

			runVectorPascalCode = new String[4];
			runVectorPascalCode[0] = bashScriptLocation + "run_vector_pascal_code";
			runVectorPascalCode[1] = leftImageFileName;
			runVectorPascalCode[2] = rightImageFileName;
			runVectorPascalCode[3] = outputImageFileName;
			
			ProcessBuilder processBuilder = new ProcessBuilder(runVectorPascalCode);
			Process process = processBuilder.start();
			
			StreamGobbler errorGobbler = new StreamGobbler(
					process.getErrorStream(),
					"ERROR", outputTextArea);            

			StreamGobbler outputGobbler = new StreamGobbler(
					process.getInputStream(),
					">", outputTextArea);

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
