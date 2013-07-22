package stereomatching.gui;

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
	
	public StereoMatchingController(String leftImage, String rightImage)
	{
		leftImageFileName = leftImage;
		rightImageFileName = rightImage;
	}
}
