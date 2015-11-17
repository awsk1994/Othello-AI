import java.util.*;

/*
 * THIS IS A CLASS THAT CONTAINS FUNCTIONS TO CONTROL A LIST OF axis (which is in axis.java).
 */
public class poss_mov
{
	public List<axis> combos;

	public poss_mov()
	{
		combos = new ArrayList<axis>();
	}

	//delete combos.
	public void delete(int x, int y){
		for (int i = combos.size()-1; i >= 0; i--){
			axis loc = combos.get(i);
			if (x == loc.x && y == loc.y){
				combos.remove(i);
				break;
			}
		}
	}

	//add to the list.
	public void add(int i, int j){
		boolean not_empty = false;
		for (int k = combos.size()-1; k >= 0; k--)
		{
			axis loc = combos.get(k);
			if (i == loc.x && j == loc.y){
				not_empty = true;
				break;
			}
		}

		if (!not_empty)
			combos.add(new axis(i,j));
	}

	//useful for printing out later.
	public String toString(){
		String output = "";
		for (axis loc : combos){
			output += "[" + loc.x + "," + loc.y + "] ";
		}
		return output;
	}

}