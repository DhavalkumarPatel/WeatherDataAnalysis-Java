package HW1Part1;

public class SumCountPair 
{
	private double sum;
	private int count;
	
	/**
	 * Initialize the object with default values
	 */
	public SumCountPair()
	{
		sum = 0;
		count = 0;
	}
	
	/**
	 * @return the average of a station from current value of sum and count
	 */
	public double getAverage() 
	{
		if(this.count > 0)
		{
			return this.sum / this.count;	
		}
		else
		{
			return 0;
		}
		
	}
	
	/**
	 * @return the current sum of a station
	 */
	public double getSum() 
	{
		return sum;
	}
	
	/**
	 * @return the current count of a station
	 */
	public int getCount() 
	{
		return count;
	}
	
	/**
	 * Update the sum and count of a station, adds the passed sum to sum and count to current count	
	 * @param sum
	 * @param count
	 */
	public void updateSumCountPair(double sum, int count) 
	{
		this.sum += sum;
		this.count += count;
	}
}
