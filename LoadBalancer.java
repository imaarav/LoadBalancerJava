import java.io.*;
import java.util.*;

class LoadBalancer
{
	
	DataRoute[] route;
	HashMap<String, Character> hm;
	File inputFile;
	File outputFile;
	
	public LoadBalancer()
	{
		this.route = new DataRoute[4];
		hm = new HashMap<String, Character>();
		inputFile = new File(System.getProperty("user.dir"), "packetlist.txt"); //joining 2 paths
		outputFile = new File(System.getProperty("user.dir"), "output.txt");
		
		for(int i = 0; i < 4; i++) 
		{
			route[i] = new DataRoute();
		}
	}
	
	public static void main(String[] args)
	{
		LoadBalancer lB = new LoadBalancer();
		int cnt = 1;
		int i = 0;
		Scanner sc = null;
		try
		{
			sc = new Scanner(lB.inputFile);
			
			String line;
			while ((line = sc.nextLine()) != null)
			{
				LineParser lineN = new LineParser(line);	
				
				if (lineN.getArvlTime()>=(float)i)
				{
					float[] p = getPercentageAll(lB.route);
					fileWrite(lB.outputFile, i, p);
					i++;
				}
				
				Character v = checkHashmap(lB.hm, lineN.getHashKey());
				if(v.toString().equals("x"))
				{
					v = getRoute(lB.route);
					lB.hm.put(lineN.getHashKey(), v);
				}
				
				setRoute(v, lineN, lB.route);
				cnt++;
				
				if (cnt >= 4230)
				{
					sc.close();
					System.exit(0);
				} 
			}
			sc.close();
			
		}catch (Exception e){sc.close();}
	}
	
	public static Character checkHashmap(HashMap<String, Character> hm, String hashKey)
	{
		for (String key: hm.keySet())
		{
			if (hashKey.equals(key))
			{
				Character v = hm.get(key);
				return v;
			}
		}
		Character v = new Character('x');
		return v;
	}

	public static Character getRoute(DataRoute[] route)
	{
		int minroutebytes = Math.min(Math.min(route[0].getCurrentLoadBytes(), route[1].getCurrentLoadBytes()), Math.min(route[2].getCurrentLoadBytes(), route[3].getCurrentLoadBytes()));
		
		if(minroutebytes == route[0].getCurrentLoadBytes())
		{
			Character w = new Character('A');
			return w;
		}
		
		else if(minroutebytes == route[1].getCurrentLoadBytes())
		{
			Character w = new Character('B');
			return w;
		}
		
		else if(minroutebytes == route[2].getCurrentLoadBytes())
		{
			Character w = new Character('C');
			return w;
		}
		
		else if(minroutebytes == route[3].getCurrentLoadBytes())
		{
			Character w = new Character('D');
			return w;
		}
		
		else
		{
			Character w = new Character('A');
			return w;
		}
	}
	
	public static void setRoute(Character v, LineParser lineN, DataRoute[] route)
	{
		if(v.toString().equals("A"))
			route[0].addLoad(lineN.getPktLength());
		
		else if(v.toString().equals("B"))
			route[1].addLoad(lineN.getPktLength());
		
		else if(v.toString().equals("C"))
			route[2].addLoad(lineN.getPktLength());
		
		else if(v.toString().equals("D"))
			route[3].addLoad(lineN.getPktLength());
		
		else
			route[0].addLoad(lineN.getPktLength());
	}
	
	public static float[] getPercentageAll(DataRoute[] route)
	{
		float[] p = new float[4];
		p[0] = getPercentage(route[0].getCurrentLoadBytes(), route[1].getCurrentLoadBytes(), route[2].getCurrentLoadBytes(), route[3].getCurrentLoadBytes());
		p[1] = getPercentage(route[1].getCurrentLoadBytes(), route[0].getCurrentLoadBytes(), route[2].getCurrentLoadBytes(), route[3].getCurrentLoadBytes());
		p[2] = getPercentage(route[2].getCurrentLoadBytes(), route[0].getCurrentLoadBytes(), route[1].getCurrentLoadBytes(), route[3].getCurrentLoadBytes());
		p[3] = getPercentage(route[3].getCurrentLoadBytes(), route[0].getCurrentLoadBytes(), route[1].getCurrentLoadBytes(), route[2].getCurrentLoadBytes());
		return p;
	}
	
	public static float getPercentage(int a, int b, int c, int d)
	{
		float p;
		float cent = 100;
			
		p = (((float)a)/((float)a + (float)b + (float)c + (float)d)) * cent;
		p = Math.round(p*cent)/cent;
		
		return p;
	}
	
	public static void fileWrite(File outputFile, int i, float[] p)
	{
		PrintWriter pw = null;
		try
		{
			pw = new PrintWriter(new FileWriter(outputFile,true)); //the true will append the new data
			pw.printf("sec %02d:  A = %.2f  B = %.2f  C = %.2f  D = %.2f \r\n", i, p[0], p[1], p[2], p[3]);
			pw.close();
		}catch(Exception e){pw.close();}
	}
}