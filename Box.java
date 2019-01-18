
public class Box {
	private char fill;
	private char hit;
	private char parity;
	
	public Box(char p)
	{
		fill = '-'; //where boats are placed
		hit = '-'; //where hits are logged
		parity = p;
	}
	
	public Box()
	{
		fill = '-'; //where boats are placed
		hit = '-'; //where hits are logged
	}
	
	public char getFill()
	{
		return fill;
	}
	
	public char getHit()
	{
		return hit;
	}
	
	public char getParity()
	{
		return parity;
	}
	
	public void setFill(char f)
	{
		fill = f;
	}
	
	public void setHit(char h)
	{
		hit = h;
	}
	
	public void setParity(char p)
	{
		parity = p;
	}
	
	//fill a grid by adding one boat
	public static Box[][] fillGrid(Box[][] grid, Ship[] boats, int boatnum)
	{	
		char b = (char)('`' + boatnum);
		//for the boat's length change the fill of each box to the respective boat number
		for (int j = 0; j<boats[boatnum-1].getLength(); j++)
		{
			if (boats[boatnum-1].getHorizontal() == true)
				//grid[y][x+j].fill = boat
				grid[boats[boatnum-1].getYaxis()][boats[boatnum-1].getXaxis()+j].fill = b;
			else
				grid[boats[boatnum-1].getYaxis()+j][boats[boatnum-1].getXaxis()].fill = b;
		}
		return grid;
	}	
	
	//fill grid from a set of boats
	public static Box[][] fillGrid(Box[][] grid, Ship[] boats)
	{
		//for each boat
		for (int j = 0; j<boats.length; j++)
		{
			//convert number to a letter
			char b = (char)('a' + j);
		
			//for the boat's length change the fill of each box to the respective boat number
			for (int k = 0; k<boats[j].getLength(); k++)
			{
				if (boats[j].getHorizontal() == true)
					grid[boats[j].getYaxis()][boats[j].getXaxis()+k].fill = b;
				else
					grid[boats[j].getYaxis()+k][boats[j].getXaxis()].fill = b;
			}
			
		}
		return grid;
	}
	
	//prints both "fill" and "hit" grids side by side - only used for "defense" grids
	public static void printGrids(Box[][] grid)
	{
		//Prints fill and hit grids
		System.out.print("\n  0 1 2 3 4 5 6 7 8 9 \t  0 1 2 3 4 5 6 7 8 9");
		for (int i = 0; i<grid.length; i++) 
		{
			//first grid
			System.out.print("\n" + i);
			for (int j = 0; j<grid[i].length; j++) 
			{
				System.out.print(" " + grid[i][j].fill);
			}
			//second grid
			System.out.print("\t" + i);
			for (int j = 0; j<grid[i].length; j++) 
			{
				System.out.print(" " + grid[i][j].hit);
			}
		}
		System.out.println("");
	}
	
	//print grid fill only
	public static void printGridFill(Box[][] grid)
	{
		System.out.print("\n  0 1 2 3 4 5 6 7 8 9 ");
		for (int i = 0; i<grid.length; i++) 
		{
			System.out.print("\n" + i);
			for (int j = 0; j<grid[i].length; j++) 
			{
				System.out.print(" " + grid[i][j].fill);
			}
		}
		System.out.println("");
	}
	
	//print hit grid only
	public static void printGridHit(Box[][] grid)
	{
		System.out.print("\n  0 1 2 3 4 5 6 7 8 9 ");
		for (int i = 0; i<grid.length; i++) 
		{
			System.out.print("\n" + i);
			for (int j = 0; j<grid[i].length; j++) 
			{
				System.out.print(" " + grid[i][j].hit);
			}
		}
		System.out.println("");
	}
}
