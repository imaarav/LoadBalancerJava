import java.io.*;
import java.util.*;

class LoadBalancer
{
	
	DataRoute[] route;
	
	public LoadBalancer()
	{
		this.route = new DataRoute[4];
						
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
		
		HashMap<String, Character> hm = new HashMap<String, Character>();
		Scanner sc = null;
		
		try
		{
			sc = new Scanner(new File("C:\\Users\\khushboo\\Desktop\\desk on 4.3\\project om\\New folder\\packetlist.txt"));
			File outputFile= new File("C:\\Users\\khushboo\\Desktop\\desk on 4.3\\project om\\New folder\\output.txt");
			String line;
			
			while ((line = sc.nextLine()) != null)
			{
				
				LineParser lineN = new LineParser(line);
				
				if (lineN.getArvlTime()>=(float)i)
				{
					
					float p1 = getPercentage(lB.route[0].getCurrentLoadBytes(), lB.route[1].getCurrentLoadBytes(), lB.route[2].getCurrentLoadBytes(), lB.route[3].getCurrentLoadBytes());
					float p2 = getPercentage(lB.route[1].getCurrentLoadBytes(), lB.route[0].getCurrentLoadBytes(), lB.route[2].getCurrentLoadBytes(), lB.route[3].getCurrentLoadBytes());
					float p3 = getPercentage(lB.route[2].getCurrentLoadBytes(), lB.route[0].getCurrentLoadBytes(), lB.route[1].getCurrentLoadBytes(), lB.route[3].getCurrentLoadBytes());
					float p4 = getPercentage(lB.route[3].getCurrentLoadBytes(), lB.route[0].getCurrentLoadBytes(), lB.route[1].getCurrentLoadBytes(), lB.route[2].getCurrentLoadBytes());
					
					fileWrite(outputFile, i, p1, p2, p3, p4);
					i++;
				}
				
				Character v = checkHashmap(hm, lineN);
				
				if(v.toString().equals("x"))
				{
					v = getRoute(lB.route);
					hm.put(Integer.toString(lineN.getSrcHost()) + "*" + Integer.toString(lineN.getDestHost()) + "*" + Integer.toString(lineN.getSrcPort()) + "*" + Integer.toString(lineN.getDestPort()), v);
				}
				
				setRoute(v, lineN, lB.route);
				cnt++;
				
				if (cnt >= 124230)
				{
					sc.close();
					System.exit(0);
				} 
				
			}
			sc.close();
		}catch (Exception e){sc.close();}
	}
	
	public static Character checkHashmap(HashMap<String, Character> hm, LineParser lineN )
	{
		String lineNhash = (Integer.toString(lineN.getSrcHost()) + "*" + Integer.toString(lineN.getDestHost()) + "*" + Integer.toString(lineN.getSrcPort()) + "*" + Integer.toString(lineN.getDestPort()));
		for (String key: hm.keySet())
		{
			if (lineNhash.equals(key))
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
	
	public static float getPercentage(int a, int b, int c, int d)
	{
		float p;
		float cent = 100;
			
		p = (((float)a)/((float)a + (float)b + (float)c + (float)d)) * cent;
		p = Math.round(p*cent)/cent;
		
		return p;
	}
	
	public static void fileWrite(File outputFile, int i, float p1, float p2, float p3, float p4)
	{
		PrintWriter pw = null;
		try
		{
			pw = new PrintWriter(new FileWriter(outputFile,true)); //the true will append the new data
			pw.printf("sec %02d:  A = %.2f  B = %.2f  C = %.2f  D = %.2f \r\n", i, p1, p2, p3, p4);
			pw.close();
		}catch(Exception e){pw.close();}
	}
}