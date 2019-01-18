
public class Ship {
	
	private int length;
	private int xaxis;
	private int yaxis;
	private boolean isHorizontal;
	private int hP;
	
	public Ship()
	{
		length = 0;
		xaxis = -1;
		yaxis = -1;
		isHorizontal = false;
	}
	
	public Ship setLength(int l)
	{
		length = l;
		hP = l;
		return this;
	}
	
	public Ship setLocation(int x, int y, boolean h)
	{
		xaxis = x;
		yaxis = y;
		isHorizontal = h;
		return this;
	}

	public int getLength()
	{
		return length;
	}
	
	public int getXaxis()
	{
		return xaxis;
	}
	
	public int getYaxis()
	{
		return yaxis;
	}
	
	public boolean getHorizontal()
	{
		return isHorizontal;
	}
	
	public int getHP()
	{
		return hP;
	}
	
	public void setHP()
	{
		hP--;
	}
	
	//find the boat that was hit
	public static Ship findBoat(int x, int y, Ship[] boats) 
	{
		Ship result = new Ship();
		//check each boat
		for (int i = 0; i < boats.length; i++)
		{
			//for the length of the boat
			for (int j = 0; j < boats[i].length; j++)
			{
				if (boats[i].getHorizontal() == true)
				{
					//check whether the boat was hit
					if (boats[i].xaxis + j == x && boats[i].yaxis == y)
					{
						return boats[i];
					}
				}
				else
				{
					//check whether the boat was hit
					if (boats[i].xaxis == x && boats[i].yaxis + j == y)
						return boats[i];
				}
				
			}	
		}
		return result;
	}
}
