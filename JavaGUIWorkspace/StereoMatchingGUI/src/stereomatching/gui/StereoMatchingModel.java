package stereomatching.gui;

/**
 * Model for the Stereo Matcher program.
 * 
 * @author Adam Murray
 * @version 0.1
 *
 */
public class StereoMatchingModel
{
	private String leftImageFileName, rightImageFileName, outputImageFileName;
	private String leftImageFilePath, rightImageFilePath, outputImageFilePath;
	private long timeForMatch;
	
	public StereoMatchingModel()
	{
		this.leftImageFileName = null;
		this.rightImageFileName = null;
		this.outputImageFileName = null;
		this.leftImageFilePath = null;
		this.rightImageFilePath = null;
		this.outputImageFilePath = null;
		this.timeForMatch = 0;
	}

	public String getLeftImageFileName()
	{ return leftImageFileName; }

	public void setLeftImageFileName(String leftImageFileName)
	{ this.leftImageFileName = leftImageFileName; }

	public String getRightImageFileName()
	{ return rightImageFileName; }

	public void setRightImageFileName(String rightImageFileName)
	{ this.rightImageFileName = rightImageFileName; }

	public String getOutputImageFileName()
	{ return outputImageFileName; }

	public void setOutputImageFileName(String outputImageFileName)
	{ this.outputImageFileName = outputImageFileName; }

	public String getLeftImageFilePath()
	{ return leftImageFilePath; }

	public void setLeftImageFilePath(String leftImageFilePath)
	{ this.leftImageFilePath = leftImageFilePath; }

	public String getRightImageFilePath()
	{ return rightImageFilePath; }

	public void setRightImageFilePath(String rightImageFilePath)
	{ this.rightImageFilePath = rightImageFilePath; }

	public String getOutputImageFilePath()
	{ return outputImageFilePath; }

	public void setOutputImageFilePath(String outputImageFilePath)
	{ this.outputImageFilePath = outputImageFilePath; }

	public long getTimeForMatch()
	{ return timeForMatch; }

	public void setTimeForMatch(long timeForMatch)
	{ this.timeForMatch = timeForMatch; }
}
