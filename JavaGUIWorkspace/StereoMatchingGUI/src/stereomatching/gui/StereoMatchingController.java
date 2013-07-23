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

	public StereoMatchingController(String leftImage, String rightImage)
	{
		leftImageFileName = leftImage;
		rightImageFileName = rightImage;
	}

	public void runVectorPascalCode()
	{
		try
		{
			String bashScriptLocation = "/home/adam/Dropbox/VectorPascal/MastersProjectPrograms/JavaGUIWorkspace/";
			runVectorPascalCode = bashScriptLocation + "run_vector_pascal_code" + " " +
					leftImageFileName + " " + rightImageFileName;

			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(runVectorPascalCode);

			// any error message?
			StreamGobbler errorGobbler = new 
					StreamGobbler(process.getErrorStream(), "ERROR");            

			// any output?
			StreamGobbler outputGobbler = new 
					StreamGobbler(process.getInputStream(), ">");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
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
