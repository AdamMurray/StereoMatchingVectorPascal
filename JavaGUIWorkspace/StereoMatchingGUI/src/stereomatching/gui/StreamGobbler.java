package stereomatching.gui;

import java.io.*;
import java.util.*;

/*
 * Class to handle the standard error and standard input streams.
 * StreamGobbler empties any stream passed into it in a separate
 * thread.
 */
class StreamGobbler extends Thread
{
	private InputStream inputStream;
	private String type;
	private String line;
	private List<String> lines = new ArrayList<String>();

	public StreamGobbler(InputStream inputStream, String type)
	{
		this.inputStream = inputStream;
		this.type = type;
	}

	public void run()
	{
		try
		{
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			line = null;
			while ((line = bufferedReader.readLine()) != null)
			{
				lines.add(line);
			}
		}
		catch (IOException iox)
		{
			iox.printStackTrace();
		}
	}

	public List<String> getLines() { return lines; }
}