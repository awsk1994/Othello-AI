import java.util.*;
import java.lang.System;
import java.lang.Object;
import java.util.Scanner;

public class main
{
	// depth_limit would control how many steps we wish to look ahead.
	static int depth_limit = 0;

	// time_limit_1 is time limit for each move.
	static int time_limit_1 = 0;

	// time_limit_2 is an error from the instructions. (Can be deleted)
	// TODO: remove time_limit_2
	//static int time_limit_2 = 0;

	static long start_time;
	static long end_time;
	static long check_time;

	// Debug Statement --> Turn this to 'true' for debugging statement. 
	static boolean debug = false;

	public static void main(String [] args)
	{
		if(debug)
			System.out.println("ver.2.5");

		Scanner scan = new Scanner(System.in);

		// Read Input.
		String line[] = scan.nextLine().split(" ");
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		
		depth_limit = Integer.parseInt(line[2]);
		time_limit_1 = Integer.parseInt(line[3]);
		//time_limit_2 = Integer.parseInt(line[4]);

		//if no time limit, make time limit forever.
		if(time_limit_1==0)
		{
			time_limit_1 = 9999999;
		}
		//if time limit in place, then go as deep as looking 10 steps (including self and enemy steps) ahead. 
		else
		{
			depth_limit = 10;
		}

		// Create a board of 8x8.
		// Create an instance of Game, which when input argument is int (instead of Game Type), will build the initial
		// setup of a 8 x 8 (or input_argument_size x input_argument_size) board.
		Game game = new Game(8);

		// Initiate player identity
		int player = 0;

		// Create an instance of Game, called new Game.
		Game newGame = new Game(game);

		//if you are black.
		if (line[1].equals("B"))
		{	
			//set player to 0 == black.
			player = 0;
			while (true)
			{
				start_time = System.currentTimeMillis();

				// If black then start first... start pruning and make decision.
				// alpha/beta pruning.
				check_time = System.currentTimeMillis();
				// alpha/beta (mini-max) prunning.
				axis decision = alphaBeta(game,0,Integer.MIN_VALUE,Integer.MAX_VALUE,0);

				//Print move.
				//game.update(x-axis, y-axis, player [0 = black, 1 = white]) will choose amongst the decisions, and pick the one that is possible.
				//it also updates the game, and the black player's move.
// BLACK (SELF) MAKES THE MOVE AND UPDATES IT WITH GAME CLASS.
				if (decision.x != -1 && decision.y != -1)
				{	
					game.update(decision.x, decision.y, 0);
					//This is printed to the main program (tournament).
					System.out.printf("%d %d\n", decision.y, decision.x);
				}
				else
				{
					if(game.playercombos[player].combos.isEmpty())
					{
						System.out.printf("pass\n");
					}
					else
					{
						axis loc = game.playercombos[player].combos.get(0);
						game.update(loc.x, loc.y, 0);
						System.out.printf("%d %d\n", loc.y, loc.x);
					}
				}

				if(debug)
				{
					System.out.println(game);
					game.printcombos(0);
					game.printcombos(1);
				}

// WHITE (ENEMY) MAKES THE MOVE AND BLACK NEEDS TO UPDATE WHITE'S MOVE WITH GAME CLASS.
				//read the enemy's move input coordinates
				String input = scan.nextLine();
				if(input.equals("pass"))
				{
					//
				}
				//If no pass, then update.
				else
				{
					// update on the combos made.
					//moveInts store the coordinate of enemy move -> read in via scan.nextLine...
					String[] move = input.split(" ");
					int[] moveInts = {Integer.parseInt(move[1]),Integer.parseInt(move[0])};
					game.update(moveInts[0],moveInts[1],1);
				}
			}
		}

		//if first input is white (works the same way, except it has to update the black (enemy)'s move before  alpha/beta prunning.
		else
		{
			player = 1;
			while(true)
			{
				start_time = System.currentTimeMillis();
				check_time = System.currentTimeMillis();

				//read the enemy's move coordinate - because white, doesn't start first
				String input = scan.nextLine();
				if(input.equals("pass"))
				{
					//
				}
				else
				{	
					String[] move = input.split(" ");
					//update on move of the other player.
					int[] moveInts = {Integer.parseInt(move[1]),Integer.parseInt(move[0])};
					//update black player
					game.update(moveInts[0],moveInts[1], 0);
				}


				//make decision - via alpha Beta to decide
				axis decision = alphaBeta(game,0,Integer.MIN_VALUE,Integer.MAX_VALUE,1);
				
				//update move made by self.
				if (decision.x != -1 && decision.y != -1)
				{
					//game.makeMove(decision,1);
					game.update(decision.x, decision.y, 1);
					System.out.printf("%d %d\n", decision.y, decision.x);
				}
				else
				{
					if(game.playercombos[player].combos.isEmpty())
					{
						System.out.printf("pass\n");
					}
					else
					{
						axis loc = game.playercombos[player].combos.get(0);
						//game.makeMove(loc,1);
						game.update(loc.x, loc.y, 1);
						System.out.printf("%d %d\n", loc.y, loc.x);
					}
				}

				if(debug)
				{
					System.out.println(game);	
					game.printcombos(0);
					game.printcombos(1);	
				}
			}
		}
	}

	//game is the board.
	//depth for first call is 0.
	//alpha = min
	//beta = max
	//player is 1 (if white), 0 (if black).
	public static axis alphaBeta(Game game, int depth, int alpha, int beta, int player)
	{
		int alpha_here = alpha;
		int beta_here = beta;
		axis final_loc = new axis(-1,-1);

		int enemy = 1;
		if (player == 1)
			enemy = 0;
		
		check_time = System.currentTimeMillis();


		//if time_limit_1 is zero (meaning nothing on time_limit_1), 
		//then, time limit doesn't matter.

		if(depth <= depth_limit && (time_limit_1 - (check_time - start_time)) > 100)
		{
			//System.out.println("depth " + depth + ", depth_limit: " + depth_limit);
			check_time = System.currentTimeMillis();
			long time_passed = check_time - start_time;
			
			//if player is 0, then enemy is 1. Vice Versa.

			if (depth >= depth_limit)
			{
				axis worth = new axis(0,0,0);

				if(debug)
				{
					System.out.println("Depth is: " + depth + ", and it has reached the depth_limit of " + depth_limit + ".");
					System.out.println("depth: " + depth + " player: " + player + " , player: " + game.pieceCount[player] + " , enemy: " + game.pieceCount[enemy]);
				}

				worth.weight = (game.playercombos[player].combos.size() - game.playercombos[enemy].combos.size())/2 + (game.pieceCount[player] - game.pieceCount[enemy]);

				if(debug)
					System.out.println("\t\tloc weight: " + worth.weight);

				return worth;
			}

			axis best = null;

		// if player is white (= 1) -> MIN
			if (player == 1)
			{
				Game newGame = new Game(game);
				List<axis> poss_mov = new ArrayList<axis>();

				//for all the possible combos...add into poss_mov.
				for (axis loc : newGame.playercombos[player].combos)
				{
					poss_mov.add(loc);

					//corner is the MOST important
					if( (loc.x == 0 && loc.y == 0)
						|| (loc.x == 7 && loc.y == 7)
						|| (loc.x == 0 && loc.y == 7)
						|| (loc.x == 7 && loc.y == 0) )
					{
						return new axis(loc.x, loc.y, -99999);
					}
				}
				
				//at each possible move... traverse to down the tree
				for (axis loc : poss_mov)
				{
					newGame.update(loc.x,loc.y,player);
					if(debug)
						System.out.println("go down a level: " + enemy);

					axis ab = alphaBeta(newGame, depth+1,alpha_here,beta_here,enemy);

					int val = ab.weight;

					if(debug)
					{
						System.out.println("\tx: " + loc.x + ", " + "y: " + loc.y);
						System.out.println("\t depth: " + depth + ", val: " + val);
					}

					//alpha/beta prune.
					if (val < beta_here)
					{
						beta_here = val;
						best = loc;
						if(debug)
							System.out.println("best was updated");
					}
					
					if (beta_here <= alpha_here)
					{
						return new axis (best.x, best.y, alpha_here);
					}
					
					newGame = new Game(game);
					check_time = System.currentTimeMillis();
				}
				

				//if best is found, return the axis
				if (best != null)
					final_loc = new axis(best.x,best.y,beta_here);
				//if no best is found, return -1... 
				else
					final_loc = new axis(-1,-1,beta_here);
			}
			//if player is 0 (black) -> MAX
			else
			{
				Game newGame = new Game(game);
				List<axis> poss_mov = new ArrayList<axis>();
				for (axis loc : newGame.playercombos[player].combos)
				{
					poss_mov.add(loc);
					
					if( (loc.x == 0 && loc.y == 0)
						|| (loc.x == 7 && loc.y == 7)
						|| (loc.x == 0 && loc.y == 7)
						|| (loc.x == 7 && loc.y == 0) )
					{
						return new axis(loc.x, loc.y, 99999);
					}

					
				}

				for (axis loc : poss_mov)
				{
					newGame.update(loc.x,loc.y,player);
					axis ab = alphaBeta(newGame, depth+1,alpha_here,beta_here,enemy);
					int val = ab.weight;

				if(debug)
					System.out.println("\tx: " + loc.x + ", " + "y: " + loc.y);

					if(val != -1)
					{
						if(debug)
						System.out.println("\t B: depth: " + depth + ", val: " + val);
					}

					if (val > alpha_here)
					{
						alpha_here = val;
						best = loc;
						if(debug)
							System.out.println("best was updated");
					}
					
					if (beta_here <= alpha_here)
					{
						return new axis (best.x, best.y, beta_here);
						//!!
						//return new axis (-1,-1, beta_here);
					}
					
					newGame = new Game(game);
					check_time = System.currentTimeMillis();
				}

				if (best != null)
					final_loc = new axis(best.x,best.y,alpha_here);
				else
					final_loc = new axis(-1,-1,alpha_here);
			}
		}
		else
		{
			axis worth = new axis(-1,-1,0);
			worth.weight = (game.playercombos[player].combos.size() - game.playercombos[enemy].combos.size())/2 + (game.pieceCount[player] - game.pieceCount[enemy]);

			if(debug)
				System.out.println("\t\tTIME_LIMIT: return: loc weight: " + worth.weight);
			
			return worth;
		}

		if(final_loc.x == -1 && final_loc.y == -1)
		{
			if(!game.playercombos[player].combos.isEmpty())
			{
				final_loc = game.playercombos[player].combos.get(0);
			}
		}		

		if(debug)
			System.out.println("Alpha: " + alpha_here + ", beta: " + beta_here);

		return final_loc;

	}
}