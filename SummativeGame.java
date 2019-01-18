import java.util.Scanner;
import java.util.ArrayDeque;

public class SummativeGame {
	static Scanner scan = new Scanner(System.in);
	
	public static void main(String args[])
	{
		int[] num1 = new int[3]; 
		int[] num2 = new int[3];
		//# boats each player has to sink
		int boatCount1 = 5, boatCount2 = 5, count = 0;
		char hit = 'M';
		boolean hitCPU = false, tactics = false, isCPU = false;
		ArrayDeque<Integer> stack= new ArrayDeque<Integer>();
		
		System.out.println("Welcome to BATTLESHIP!");
		
		System.out.println("In this version of battleship, ships are placed, based on their length, on a start coordinate point "
				+ "and an end point.\nFor example, selecting a boat of length 3, "
				+ "inputting (0,0) and (2,0) places the boat horizontally at the top left corner of the grid.");
		System.out.println("\nThere will be three grids for you to look at: "
				+ "1 attack grid and 2 defense grids"
				+ "\nAn attack grid and one of the defense grids show whether a spot was attacked,"
				+ " and if so, whether it missed or hit a ship"
				+ "\nThe other defense grid shows where you placed your ships and whether they have been hit."
				+ " A hit ship is marked by a capital letter.");
		
		System.out.println("Would you like to play with a CPU (1) or a second player(another number)?");
		
		if (scan.nextInt() == 1)
			isCPU = true;

		
		//declare and initialize grids
		Box[][] defense1 = new Box[10][10];
		Box[][] attack1 = new Box[10][10];
		Box[][] defense2 = new Box[10][10];
		Box[][] attack2 = new Box[10][10];
		for (int i = 0; i<defense1.length; i++)
		{
			for (int j = 0; j<defense1.length; j++)
			{
				if (j == 0)
					count++;
				//assign parity
				if(count % 2 == 0)
				{
					defense1[i][j] = new Box('O');
					attack1[i][j] = new Box('O');
					defense2[i][j] = new Box('O');
					attack2[i][j] = new Box('O');	
				}
				else
				{
					defense1[i][j] = new Box('E');
					attack1[i][j] = new Box('E');
					defense2[i][j] = new Box('E');
					attack2[i][j] = new Box('E');
				}
				count++;
			}
		}

		//declare and initialize ships
		Ship[] P1boats = new Ship[5];
		Ship[] P2boats = new Ship[5];
		for (int i = 0; i<P1boats.length; i++)
		{
			P1boats[i] = new Ship();
			P2boats[i] = new Ship();
		}
		
		//sets different boat lengths
		for (int i = 0; i<2; i++)
		{
			P1boats[i].setLength(i+2);
			P2boats[i].setLength(i+2);
		}
		for (int i = 2; i<5; i++)
		{
			P1boats[i].setLength(i+1);
			P2boats[i].setLength(i+1);
		}
		
		//set cpu's boats so I can skip it during testing
	/*	P2boats[0].setLocation(0, 0, true);
		P2boats[1].setLocation(0, 1, true);
		P2boats[2].setLocation(0, 2, true);
		P2boats[3].setLocation(0, 3, true);
		P2boats[4].setLocation(0, 4, true);
		Box.fillGrid(defense2, P2boats);
		
		P1boats[0].setLocation(0, 0, true);
		P1boats[1].setLocation(0, 1, true);
		P1boats[2].setLocation(0, 2, true);
		P1boats[3].setLocation(0, 3, true);
		P1boats[4].setLocation(0, 4, true);
		Box.fillGrid(defense1, P1boats);*/
		
		//player places their boats
		System.out.println("Player 1: Set boats");
		P1boats = setBoats(P1boats, defense1, false);
		Box.fillGrid(defense1, P1boats);

		//P2 places boats
		System.out.println("\nPlayer 2: Set boats");
		P2boats = setBoats(P2boats, defense2, isCPU);
		Box.fillGrid(defense2, P2boats);

		do{	
			//attack sequence for P1
			//num[0] = x coordinate, num[1] = y coordinate, num[2] = hit or miss	
			System.out.println("\nPlayer 1:");
			System.out.println("\nDefense:");
			Box.printGrids(defense1); //shows P1 their defenses
			System.out.println("\nAttack:");
			Box.printGridHit(attack1);
			num1 = attack(defense2, false);
			if (num1[2] == 1)
				hit = 'H';
			else
				hit = 'M';
			//update hit for each grid
			attack1[num1[1]][num1[0]].setHit(hit);
			defense2[num1[1]][num1[0]].setHit(hit);
			
			if (num1[2] == 1)
			{
				//if hit, identify which spot was hit by capitalizing the letter
				defense2[num1[1]][num1[0]].setFill(Character.toUpperCase(defense2[num1[1]][num1[0]].getFill()));
				
				//find the boat from coordinates and lower its HP by 1
				Ship.findBoat(num1[0], num1[1], P2boats).setHP();
				if (Ship.findBoat(num1[0], num1[1], P2boats).getHP() == 0)
				{
					System.out.println("Sunken Ship!");
					boatCount1--;
				}
			}
			Box.printGridHit(attack1); //show P1 where they hit
			//allows for player to think for a bit
			System.out.println("Enter a number to continue");
			num1[2] = scan.nextInt();
			
			//P2 attack sequence
			System.out.println("\nPlayer 2:");
			if (isCPU == true)
			{
				//activate tactics if the CPU hit something and the stack is empty
				/*the strategy the CPU uses is to search everywhere around a "hit" 
				* location thus meaning it is most effective against opponents who 
				* place all their boats adjacent to one another*/
				if (hitCPU == true && stack.isEmpty())
					tactics = true;
				//deactivate tactics after the stack is empty and the CPU didn't hit something
				else if (hitCPU == false && stack.isEmpty())
					tactics = false;
					
				if (tactics == true)
				{
					//get all blocks around the hit
					if (hitCPU == true)
						stack = searchArea(stack, defense1, num2[0], num2[1]);	//where num2[0] and num2[1] are the coordinates of the previous attack
					
					num2[0] = stack.pop();
					num2[1] = stack.pop();
					
					//while already visited, keep popping from queue
					while (defense1[num2[1]][num2[0]].getHit() != '-')
					{
						num2[0] = stack.pop();
						num2[1] = stack.pop();
						//if stack turns out to be empty then apply normal attack sequence
						if (stack.isEmpty())
						{
							num2 = attack(defense1, true);
							tactics = false;
							break;
						}
					}
					num2 = getResult(defense1, num2[0], num2[1], isCPU);
				}
				//otherwise randomly attack
				else
					num2 = attack(defense1, true);
				
				if (num2[2] == 1)
					{
						hit = 'H';
						hitCPU = true;
					}
				else
					{
						hit = 'M';
						hitCPU = false;
					}
			}
			else
			{
				//if a player, use normal attack method
				System.out.println("\nDefense:");
				Box.printGrids(defense2); //shows P2 their defenses
				System.out.println("\nAttack:");
				Box.printGridHit(attack2);
				num2 = attack(defense1, false);
			
				if (num2[2] == 1)
					{
						hit = 'H';
					}
				else
					{
						hit = 'M';
					}
			}
			
			//update hit for each grid
			attack2[num2[1]][num2[0]].setHit(hit);
			defense1[num2[1]][num2[0]].setHit(hit);

			if (num2[2] == 1)
			{
				//if hit, identify which spot was hit by capitalizing the letter
				defense1[num2[1]][num2[0]].setFill(Character.toUpperCase(defense1[num2[1]][num2[0]].getFill()));
				//find the boat from coordinates and lower its HP by 1
				Ship.findBoat(num2[0], num2[1], P1boats).setHP();
				if (Ship.findBoat(num2[0], num2[1], P1boats).getHP() == 0)
				{
					System.out.println("Sunken Ship!");
					boatCount2--;
				}
			}
			if (isCPU == false)
			{
				Box.printGridHit(attack2); //show P2 where they hit
				//allows for player to think for a bit
				System.out.println("Enter a number to continue");
				num1[2] = scan.nextInt();
			}
		}while(boatCount1 > 0 && boatCount2 > 0); //break if one player has sunken all the opponent's ships
		
		if (boatCount1 == 0 )
		{
			System.out.println("\nPlayer 1 Wins!!");
		}
		else
		{
			System.out.println("\nPlayer 2 Wins!!");
		}
		System.out.println("\nP1 Defense:");
		Box.printGrids(defense1); //shows P1 their final defenses
		System.out.println("\nP2 Defense:");
		Box.printGrids(defense2); //shows P2 their final defenses
		
		
	}
	
	public static Ship[] setBoats(Ship[] boats, Box[][] grid, boolean isCPU)
	{
		int boatnum = 0, x = 0, y = 0, xE = 0, yE = 0, num = 0;
		int[] coordinates = new int[8];
		boolean isH  = false, canPlace = true, valid = false;
		String[] boatLengths = {"1 = boat of length 2","2 = boat of length 3","3 = boat of length 3","4 = boat of length 4","5 = boat of length 5"};
		boolean[] boatsPlaced = new boolean [5];
		
		if (isCPU == false)
			Box.printGridFill(grid);
		
		for (int i = 0; i<5; i++)
		{
			//choose a boat to place
			if (isCPU == false)
			{
				System.out.println("Choose a boat");
				for (int k = 0; k < boatLengths.length; k++)
				{
					if (boatsPlaced[k] == false)
						System.out.println(boatLengths[k]);
				}
	
				do{
					valid = true;
					boatnum = scan.nextInt();					
					//while not a valid number
					while( boatnum>5 || boatnum<1 )
					{
						System.out.print("Invalid input\nChoose a boat");
						boatnum = scan.nextInt();
					}
					//if the boat has already been placed, loop
					if (boats[boatnum-1].getXaxis() != -1)
					{
						System.out.print("Already chosen, choose another boat");
						valid = false;
					}
				}while(valid == false);
			
			}
			else
			{
				boatnum = (int)(5*Math.random()+1);
				//while the boats has already been placed
				while(boats[boatnum-1].getXaxis() != -1)
				{
					boatnum = (int)(5*Math.random()+1);
				}
			}
			
			do
			{
				canPlace = true;
				
				//input location
				if (isCPU == false)
				{
					//find start point
					System.out.print("Choose a start point (x and y coordinate)");
					x = scan.nextInt();
					y = scan.nextInt();
					
					//if not on grid or where a boat already is
					if(canPlace(grid, x, y) == false)
					{
						System.out.print("Invalid, please try again (x and y coordinate)");
						x = scan.nextInt();
						y = scan.nextInt();
					}
					
					//check for valid input for end point depending on boat length
					coordinates = endCoordinates(x, y, boats[boatnum-1].getLength());
					
					valid = false;
					//input end point
					System.out.print("Choose an end point (x and y coordinate)");
					xE = scan.nextInt();
					yE = scan.nextInt();
					//must be one of the previously calculated points
					if ((xE == coordinates[0] && yE == coordinates[1]) || (xE == coordinates[2] && yE == coordinates[3]) || (xE == coordinates[4] && yE == coordinates[5]) || (xE == coordinates[6] && yE == coordinates[7]))
					{
						valid = true;
					}	
					
					//loop until valid point
					while(valid == false){
						System.out.print("Impossible end point, choose another:");
						xE = scan.nextInt();
						yE = scan.nextInt();

						if ((xE == coordinates[0] && yE == coordinates[1]) || (xE == coordinates[2] && yE == coordinates[3]) || (xE == coordinates[4] && yE == coordinates[5]) || (xE == coordinates[6] && yE == coordinates[7]))
						{
							valid = true;
						}		
					}

					//reset coordinates to match Ship's location
					if (xE == coordinates[0] || yE == coordinates[5])
					{
						x = xE;
						y = yE;
					}
					//Set direction to match Ship's location
					if (xE == coordinates[0] || xE == coordinates[2])
						isH = true;
					else
						isH = false;
				}
				else
				{
					x = (int)(Math.random()*10);
					y = (int)(Math.random()*10);
					num = (int)(Math.random()*2);
					if (num == 1)
						isH = true;
					else
						isH = false;
				}
				//set boat
				boats[boatnum-1].setLocation(x,y,isH);	
				//check if no issues
				canPlace = canPlace(boats, boatnum, grid, isCPU);
				//this boat has been placed
				boatsPlaced[boatnum - 1] = true;
			}while (canPlace == false);
			
			//fill grid
			Box.fillGrid(grid, boats, boatnum);
			if (isCPU == false) //output boat location if not CPU
				Box.printGridFill(grid);
		}
		return boats;
	}

	//canPlace for a boat
	public static boolean canPlace (Ship[] boats, int boatnum, Box[][] grid, boolean isCPU)
	{
		boolean canPlace = true;
		//check if possible location
		//for boat length
		for (int j = 0; j<boats[boatnum-1].getLength(); j++)
		{
			//for horizontal ship
			if(boats[boatnum-1].getHorizontal() == true)
			{
				//if off grid
				if(boats[boatnum-1].getLength() + boats[boatnum-1].getXaxis() - 1 > 9)
				{
					if (isCPU == false) //if an actual player, output response
						System.out.println("That is off the grid, please choose another location");
					canPlace = false;
					break;
				}
				//if overlap
				if (grid[boats[boatnum-1].getYaxis()][boats[boatnum-1].getXaxis()+j].getFill() != '-')
				{
					if(isCPU == false) //if an actual player, output response
						System.out.println("There is another ship in the way, please choose another location");
					canPlace = false;
					break;
				}
			}	
			//for vertical ship
			else
			{
				//if off grid
				if(boats[boatnum-1].getLength() + boats[boatnum-1].getYaxis() - 1 > 9)
				{
					if(isCPU == false) //if an actual player, output response
						System.out.println("That is off the grid, please choose another location");
					canPlace = false;
					break;
				}
				//if overlap
				if (grid[boats[boatnum-1].getYaxis()+j][boats[boatnum-1].getXaxis()].getFill() != '-')
				{
					if(isCPU == false) //if an actual player, output response
						System.out.println("There is another ship in the way, please choose another location");
					canPlace = false;
					break;
				}
			}	
		}
		
		return canPlace;
	}
	
	//canPlace for one coordinate
	public static boolean canPlace (Box[][] grid, int x, int y)
	{
		boolean canPlace = true;
		//check if on grid
		if (x<0 || x>9 || y<0 || y>9)
			canPlace = false;
		//check if spot is free
		if (grid[y][x].getFill() != '-')
			canPlace = false;
		
		return canPlace;
	}
	
	//possible end coordinate inputs
	public static int[] endCoordinates (int x, int y, int length)
	{
		int[] coordinates = new int[8];
		length --; 
		
		//W
		coordinates[0] = x-length;
		coordinates[1] = y;
		//E
		coordinates[2] = x+length;
		coordinates[3] = y;
		//N
		coordinates[4] = x;
		coordinates[5] = y-length;
		
		//S
		coordinates[6] = x;
		coordinates[7] = y+length;

		return coordinates;
	}
	
	//attack sequence (identify attack coordinates and return the result)
	public static int[] attack(Box[][] enemy, boolean isCPU)
	{
		int x = 0, y = 0;
		int[] result = new int[3];
		boolean valid = false;
		
		//get attack location
		while (valid == false)
		{
			if (isCPU == false)
			{
				System.out.print("Where would you like to attack? (x then y coordinate)");
				x = scan.nextInt();
				y = scan.nextInt();
				//checks if on grid and if it has been hit
				if (x >= 0 && x <= 9 && y >= 0 && y <= 9 && enemy[y][x].getHit() == '-')
					valid = true;
			}
			else
			{
				x = (int)(Math.random()*10);
				y = (int)(Math.random()*10);
				//if hadn't been hit already and an even parity 
				//-> all ships must occupy at least 2 spaces therefore only need to attack every other space
				if (enemy[y][x].getHit() == '-' && enemy[y][x].getParity() == 'E')
				{
					valid = true;	
				}
			}
		}
		result = getResult(enemy, x, y, isCPU);
		return result;
	}
	
	//identifies a whether a ship was hit or not from the attack coordinates
	public static int[] getResult (Box[][] enemy, int x, int y, boolean isCPU)
	{
		int[] result = new int [3];
		
		result[0] = x;
		result[1] = y;
		
		//if an empty spot then return a miss
		if(enemy[y][x].getFill() == '-')
		{
			System.out.println("(" + x +", " + y + ")");
			if(isCPU == false) //if an actual player, output response
				System.out.println("Miss!");
			result[2] = 0;
			
		}
		//if spot is occupied then return a hit
		else
		{
			System.out.println("(" + x +", " + y + ")");
			if(isCPU == false) //output response
				System.out.println("Hit!");
			result[2] = 1;
		}
		return result;
	}

	public static ArrayDeque<Integer> searchArea(ArrayDeque<Integer> stack, Box[][] enemy, int x, int y)
	{
		//add two coordinates for every adjacent space that can be hit
		if (y - 1 >= 0 && enemy[y-1][x].getHit() == '-' )
		{
			stack.add(x);
			stack.add(y-1);
		}
		if (y + 1 <= 9 && enemy[y+1][x].getHit() == '-')
		{
			stack.add(x);
			stack.add(y+1);
		}
		if (x - 1 >= 0 && enemy[y][x-1].getHit() == '-')
		{
			stack.add(x-1);
			stack.add(y);
		}
		if (x + 1 <=9 && enemy[y][x+1].getHit() == '-')
		{
			stack.add(x+1);
			stack.add(y);
		}
		System.out.print(stack.size());
		return stack;	
	}
}
