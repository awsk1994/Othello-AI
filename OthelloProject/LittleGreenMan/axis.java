/*
 * THIS IS A CLASS THAT CONTAINS a x-axis, y-axis, and weight of the block.
 */

public class axis
{
	public int x;
	public int y;
	public int weight;

	axis(int temp_x, int temp_y)
	{
		x = temp_x;
		y = temp_y;
		weight = -1;
	}

	axis(int temp_x, int temp_y, int tweight)
	{
		x = temp_x;
		y = temp_y;
		weight = tweight;
	}
}