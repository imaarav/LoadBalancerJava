public class DataRoute
{
	//private char rpath;
	private int currentLoadBytes;
	//final private static int bitRateBps = 100;
	
	public DataRoute()
	{
		//this.rpath = 'x';
		this.currentLoadBytes = 0;
	}
	
	void addLoad(int bytes)
	{
		currentLoadBytes += bytes;
	}
	
	void reduceLoad(int bytes)
	{
		currentLoadBytes -= bytes;
	}
	
	/* int getRpath()
	{
		return this.rpath;
	} */
	
	int getCurrentLoadBytes()
	{
		return this.currentLoadBytes;
	}
}