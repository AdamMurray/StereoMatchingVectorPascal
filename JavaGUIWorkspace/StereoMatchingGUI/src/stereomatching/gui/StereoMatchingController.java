package stereomatching.gui;

import java.util.*;

/**
 * Class that handles processing information
 * taken from the GUI and using it in order
 * to run the Vector Pascal source code
 * to match a pair of stereo images.
 * 
 * @author Adam Murray
 *
 */
public class StereoMatchingController
{
	private String leftImageFileName, rightImageFileName;
	private String runVectorPascalCode;

	private List<String> errorLines = new ArrayList<String>();
	private List<String> outputLines = new ArrayList<String>();

	/**
	 * Creates a StereoMatchingController object using
	 * two image file names chosen by the user.
	 * 
	 * @param leftImage - file name of the left stereo image.
	 * @param rightImage - file name of the right stereo image.
	 */
	public StereoMatchingController(String leftImage, String rightImage)
	{
		leftImageFileName = leftImage;
		rightImageFileName = rightImage;
	}

	/**
	 * Runs a bash script which is passed the left
	 * and right image file names as parameters,
	 * which in turn runs code written in Vector
	 * Pascal that performs stereo matching on
	 * the two images.
	 */
	public void runVectorPascalCode()
	{
		try
		{
			String bashScriptLocation = "/home/adam/Dropbox/VectorPascal/MastersProjectPrograms/JavaGUIWorkspace/";
			runVectorPascalCode = bashScriptLocation + "run_vector_pascal_code" + " " +
					leftImageFileName + " " + rightImageFileName;

			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(runVectorPascalCode);

			StreamGobbler errorGobbler = new 
					StreamGobbler(process.getErrorStream(), "ERROR");            

			StreamGobbler outputGobbler = new 
					StreamGobbler(process.getInputStream(), ">");

			errorGobbler.start();
			outputGobbler.start();

			int exitVal = process.waitFor();
			System.out.println("ExitValue: " + exitVal);
			
			errorLines = errorGobbler.getLines();
			outputLines = outputGobbler.getLines();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	public List<String> getErrorLines() { return errorLines; }

	public List<String> getOutputLines() { return outputLines; }
}
