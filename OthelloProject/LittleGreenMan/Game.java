import java.util.*;
public class Game
{
	public int size;
	public int[][] board;

	// Finding horizontally and vertically possible combos, and storing them into a triple-array.
	// n x n board is the first 2 level of array.
	// The weight/value of the board is the 3rd level of array.
	public poss_mov[][][] moveTable;

	// Possible combos.
	public poss_mov[] playercombos;

	// Count number of pieces.
	public int[] pieceCount;
	
	//At each level, create a new game to simulate.
	public Game(Game game)
	{
		size = game.size;
		board = new int[size][size];
		moveTable = new poss_mov[size][size][2];
		playercombos = new poss_mov[2];
		pieceCount = new int[2];

		//replicate board onto board.
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				board[i][j] = game.board[i][j];
			}
		}

		//replicate moveTable .
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				for (int k = 0; k < 2; k++){
					moveTable[i][j][k] = new poss_mov();
					for (axis loc : game.moveTable[i][j][k].combos)
					{
						moveTable[i][j][k].combos.add(loc);
					}
				}
			}
		}

		//replicate playercombos.
		for (int i = 0; i < 2; i++){
			playercombos[i] = new poss_mov();
			for (axis loc : game.playercombos[i].combos)
			{
				playercombos[i].combos.add(loc);
			}
		}


		//replicate each side's piece count.
		pieceCount[0] = game.pieceCount[0];
		pieceCount[1] = game.pieceCount[1];
	}

	//calls this first - initiate game.
	public Game(int n)
	{
		size = n;
		board = new int[n][n];
		//possible moves based on the pieces that are on the board.
		moveTable = new poss_mov[n][n][2];
		//possible moves generated from moveList(Shorter list).
		playercombos = new poss_mov[2];
		pieceCount = new int[2];

		//all value on board is -1.
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++)
			{
				board[i][j] = -1;
			}
		}

		// available list is based on the moveTable board.
		// so max check time is size x size.
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				for (int k = 0; k < 2; k++){
					moveTable[i][j][k] = new poss_mov();
				}
			}
		}

		// new poss_mov in playercombos.
		for (int i = 0; i < 2; i++)
		{
			playercombos[i] = new poss_mov();
		}

		//default start position.
		board[3][3] = 1;
		board[4][4] = 1;
		board[4][3] = 0;
		board[3][4] = 0;

//default combos -> starting combos limited -> easy to guess.

		//[][][represent color] ->  1 = white. 0 = black.
		// at each box, add the possible combos. 
		// so moveTable[3][3][1].add(3,5) means at box (3,3), it is a white. And a the next vertically or horizontally possible same color is...
		moveTable[3][3][1].add(3,5);
		moveTable[3][3][1].add(5,3);
		moveTable[4][4][1].add(2,4);
		moveTable[4][4][1].add(4,2);
		moveTable[3][4][0].add(3,2);
		moveTable[3][4][0].add(5,4);
		moveTable[4][3][0].add(2,3);
		moveTable[4][3][0].add(4,5);

		moveTable[5][3][1].add(3,3);
		moveTable[3][5][1].add(3,3);
		moveTable[2][4][1].add(4,4);
		moveTable[4][2][1].add(4,4);

		moveTable[3][2][0].add(3,4);
		moveTable[5][4][0].add(3,4);
		moveTable[2][3][0].add(4,3);
		moveTable[4][5][0].add(4,3);

		//possible combos if 1(white), 0 (black).
		playercombos[1].add(3,5);
		playercombos[1].add(5,3);
		playercombos[1].add(2,4);
		playercombos[1].add(4,2);

		playercombos[0].add(2,3);
		playercombos[0].add(4,5);
		playercombos[0].add(3,2);
		playercombos[0].add(5,4);

		//there are 2 white, 2 black.
		pieceCount[0] = 2;
		pieceCount[1] = 2;
	}


	public boolean isDone()
	{
		return playercombos[0].combos.isEmpty() && playercombos[1].combos.isEmpty();
	}

	public void update(int i, int j, int player)
	{
		List<axis> list = new ArrayList<axis>();

		//add new location/axis.
		list.add(new axis(i,j));

		//identify enemy and self identity.
		int enemy = 1;
		if (player == 1)
			enemy = 0;

		//delete this move, because we are on it.
		playercombos[player].delete(i,j);
		playercombos[enemy].delete(i,j);

		//on baord, mark (i,j) as player.
		board[i][j] = player;
		pieceCount[player]++;

		//PART A: simply update what happened to the board
		for (axis loc : moveTable[i][j][player].combos)
		{
			int x = loc.x;
			int y = loc.y;

			moveTable[x][y][player].delete(i,j);

			//!!
			//moveTable[i][j][player].delete(x,y);


			//calculate combos that replaces enemy (diagonally, horizontally and vertically).
			int dx = i - x;
			int dy = j - y;

			int max = Math.max(Math.abs(dx),Math.abs(dy));

			dx /= max;
			dy /= max;

			//move towards (i,j) by one.
			x += dx;
			y += dy;

			//while the (x,y) [from piece] hasn't reached the (i,j)[new put piece]
			while (x != i || y != j)
			{
				list.add(new axis(x,y));

				//for each axis as you move towards (i,y).
				for (axis loc2 : moveTable[x][y][enemy].combos)
				{
					int a = loc2.x;
					int b = loc2.y;

					//remove (a,b) from enemy's board since it's being replaced by you.
					moveTable[a][b][enemy].delete(x,y);

					//!!
					//moveTable[x][y][enemy].delete(a,b);

					//if nothing in moveTable - is going to happen after delete
					// ->  delete itself from playercombos too.
					if (moveTable[a][b][enemy].combos.isEmpty())
					{
						playercombos[enemy].delete(a,b);
					}
				}

				//remove the moveTable at current position's poss_mov.
				moveTable[x][y][enemy] = new poss_mov();

				//on board, add yourself onto the board.
				board[x][y] = player;
				pieceCount[player]++;
				pieceCount[enemy]--;

				//keep moving torwards (i,j).
				x += dx;
				y += dy;
			}

			//!!

		}

//PART B: update the possible moves now.
		//reset moveTable at (i,j)
		moveTable[i][j][player] = new poss_mov();
		moveTable[i][j][enemy] = new poss_mov();

		//list contains all the new points that are affected.
		for (axis loc : list){


			int x = loc.x;
			int y = loc.y;

			int tmpx = x;
			int tmpy = y;

			//different directions that can possibly have your combos.
			List<axis> directions = new ArrayList<axis>();

			directions.add(new axis(-1,-1));
			directions.add(new axis(-1,0));
			directions.add(new axis(-1,1));
			directions.add(new axis(0,-1));
			directions.add(new axis(0,1));
			directions.add(new axis(1,-1));
			directions.add(new axis(1,0));
			directions.add(new axis(1,1));

			//move torwards all the directions.
			//check for enemy possible Moves.
			for (axis dir : directions)
			{
				int dx = dir.x;
				int dy = dir.y;


				x += dx;
				y += dy;

				//move in a certain direction as long as it's still you, and inbounds
				while (isInbounds(x,y) && board[x][y] == player)
				{
					x += dx;
					y += dy;
				}

				//when hit enemy.
				if (isInbounds(x,y) && board[x][y] == enemy)
				{
					int locx = x;
					int locy = y;

					//move back to orig place.
					x = tmpx;
					y = tmpy;

					//move back one step.
					x -= dx;
					y -= dy;

					//keep going back when read player.
					while (isInbounds(x,y) && board[x][y] == player)
					{
						x -= dx;
						y -= dy;
					}

					//if read nothing, then this is a possible move for enemy - mark it.
					if (isInbounds(x,y) && board[x][y] == -1)
					{
						if (moveTable[x][y][enemy].combos.isEmpty())
							playercombos[enemy].add(x,y);
						moveTable[locx][locy][enemy].add(x,y);
						moveTable[x][y][enemy].add(locx,locy);
					}
				}



//SECOND HALF OF TESTING -> NOW TEST WHEN HIT YOU.

				//reset - go back orig 
				x = tmpx;
				y = tmpy;	

				//move in direction.
				x += dx;
				y += dy;

				//skip all enemy...
				while (isInbounds(x,y) && board[x][y] == enemy)
				{
					x += dx;
					y += dy;
				}

				//until hit player.
				if (isInbounds(x,y) && board[x][y] == player)
				{
					int locx = x;
					int locy = y;

					//return to orig
					x = tmpx;
					y = tmpy;

					//go back one step.
					x -= dx;
					y -= dy;

					//keep going back as long as find enemy.
					while ((isInbounds(x,y) && board[x][y] == enemy) || (isInbounds(x,y) && board[x][y] == player))
					{
						x -= dx;
						y -= dy;
					}

					//if blank.
					if (isInbounds(x,y) && board[x][y] == -1)
					{
						moveTable[locx][locy][player].delete(x,y);
						moveTable[x][y][player].delete(locx,locy);

						if (moveTable[x][y][player].combos.isEmpty())
							playercombos[player].delete(x,y);
					}

				}

				//if does not hit player.
				//add as possible move.
				else if (isInbounds(x,y) && board[x][y] == -1 && (tmpx+dx != x || tmpy+dy != y))
				{
					if (moveTable[x][y][player].combos.isEmpty())
						playercombos[player].add(x,y);

					moveTable[tmpx][tmpy][player].add(x,y);
					moveTable[x][y][player].add(tmpx,tmpy);

					//System.out.println("\t\t Checkpoint B");
				}

				//return to normal.
				x = tmpx;
				y = tmpy;
			}
		}
		return;
	}

	//is within bounds of the board... x, y not greater than 7, nor less than 0.
	public boolean isInbounds(int x, int y)
	{
		return 0 <= x && x < size && 0 <= y && y < size;
	}



	public void printcombos(int color){
		String player = "";
		if (color == 1)
			player = "white";
		else
			player = "black";

		System.out.print(player + " combos: ");
		for (axis loc : playercombos[color].combos){
			int x = loc.x;
			int y = loc.y;
			System.out.print("[" + x + "," + y + "]");
			System.out.print(": ");
			System.out.println(moveTable[x][y][color]);
		}
		System.out.println();

	}

	public void getWinner()
	{
		int bCount = 0;
		int wCount = 0;

		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				if (board[i][j] == 0)
					bCount++;
				if (board[i][j] == 1)
					wCount++;
			}
		}
		if (bCount > wCount)
			System.out.println("BLACK WINS!!");
		else if(wCount > bCount)
			System.out.println("WHITE WINS!!");
		else
			System.out.println("tie.");
	}

	public String toString(){
		String output = "";
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				if (board[i][j] == -1)
					output += "[ ]";
				else if (board[i][j] == 1)
					output += " W ";
				else
					output += " B ";
			}
			output += "\n";
		}
		output += "Black combos: " + playercombos[0].toString() + "\n";
		output += "White combos: " + playercombos[1].toString() + "\n";
		return output;
	}
}