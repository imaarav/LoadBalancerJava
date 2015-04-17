public class LineParser
{
	private float arvlTime;
	private int srcHost;
	private int destHost;
	private int srcPort;
	private int destPort;
	private int pktLength;
	//String route;
	
	public LineParser (String line)
	{
		String[] elements = line.split("\\s");
		this.arvlTime = Float.parseFloat(elements[0]);
		this.srcHost = Integer.parseInt(elements[1]);
		this.destHost = Integer.parseInt(elements[2]);
		this.srcPort = Integer.parseInt(elements[3]);
		this.destPort = Integer.parseInt(elements[4]);
		this.pktLength = Integer.parseInt(elements[5]);
		//this.route = "xx";
	}
	
	float getArvlTime()
	{
		return this.arvlTime;
	}
	
	int getSrcHost()
	{
		return this.srcHost;
	}
	
	int getDestHost()
	{
		return this.destHost;
	}
	
	int getSrcPort()
	{
		return this.srcPort;
	}
	
	int getDestPort()
	{
		return this.destPort;
	}
	
	int getPktLength()
	{
		return this.pktLength;
	}
	
}