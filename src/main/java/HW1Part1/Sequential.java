package HW1Part1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;

/**
 * SEQ: Sequential version that calculates the average of the TMAX temperatures by station Id. 
 * HashMap is used as an accumulation data structure for grouping the accumulated temperatures and count of records 
 * by station. 
 * @author dspatel
 */
public class Sequential 
{
	private Map<String, SumCountPair> accumulator = new HashMap<>();
	
	/**
	 * Calculate the average of the TMAX temperatures by station ID,
	 * Measures and print the the timings  
	 * @param linesOfFile as a list
	 * @param fibonacciFlag
	 * @return accumulator (last calculated)
	 */
	public Map<String, SumCountPair> testAndPrintPerformanceStats(List<String> linesOfFile, boolean fibonacciFlag)
	{
		long start,end,runningTime;
		List<Long> results = new ArrayList<>();
		
		for(int i=0; i<10; i++)
		{
			// shared accumulator is used for all iterations so clearing it before starting new iteration 
			if(!accumulator.isEmpty())
			{
				accumulator.clear();
			}
			
			// Calculate the average and measure the timings
			start = System.currentTimeMillis();
			calculateAvgOfMaxTemp(linesOfFile, fibonacciFlag);
			end = System.currentTimeMillis();
			runningTime = end - start;
			results.add(runningTime);
			
			//accumulator.forEach((k,v)->System.out.println("StationId = " + k + ", Average = " + v.getAverage()));
		}
		
		// print the running time statistics
		LongSummaryStatistics stats = results.stream().collect(LongSummaryStatistics::new, LongSummaryStatistics::accept, LongSummaryStatistics::combine);
		System.out.println("Average Running Time : "+stats.getAverage());
		System.out.println("Maximum Running Time : "+stats.getMax());
		System.out.println("Minimum Running Time : "+stats.getMin());
		
		return accumulator;
	}
	
	/**
	 * 
	 * @param linesOfFile
	 * @param fibonacciFlag
	 */
	private void calculateAvgOfMaxTemp(List<String> linesOfFile, boolean fibonacciFlag)
	{
		String stationId, tempType;
		double tmax;
		
		for(String record : linesOfFile)
		{
			tempType = record.split(",")[2];
			
			if(tempType.equalsIgnoreCase("TMAX"))
			{
				stationId = record.split(",")[0];
				tmax = Double.parseDouble(record.split(",")[3]);
				
				SumCountPair sumCountPair = accumulator.get(stationId);
				
				// If station is not found yet then create the new default object
				if(null == sumCountPair)
				{
					sumCountPair = new SumCountPair();
				}
				
				// Update the current sum and count of a station
				sumCountPair.updateSumCountPair(tmax, 1);
					
				// Delay introduced before changing accumulator 
				if(fibonacciFlag)
				{
					getNthFibonacci(17);
				}
					
				accumulator.put(stationId, sumCountPair);
			}
		}
	}
	
	/**
	 * calculate the nth Fibonacci number for introducing the delay
	 * @param n
	 * @return
	 */
	private double getNthFibonacci(int n)
	{
	    if(n == 0) 
	    {
	    	return 0;
	    }
	    if(n == 1)
	    {
	    	return 1;
	    }
	        
	    int first = 0;
	    int second = 1;
	    int result = 0;
	        
	    for (int i = 2; i <= n; i++)
	    {
	        result = second + first;
	        first = second;
	        second = result;
	    }
	    return result;
	}
}
