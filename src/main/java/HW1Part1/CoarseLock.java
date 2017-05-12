package HW1Part1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;

/**
 * COARSE-LOCK: Multi-threaded version that assigns subsets of the input List<String>
 * for processing by separate threads. HashMap is used as an accumulation data 
 * structure for grouping the accumulated temperatures and count of records 
 * by station. It uses the single lock on the entire data structure and ensures 
 * correct multithreaded execution and minimal delays by holding the lock only when
 * absolutely necessary.
 * @author dspatel
 *
 */
public class CoarseLock
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
			
			// Divided the task in 4 subtasks as maximum number of worker threads processor can schedule is 4.
			// Divide the list in 4 sublist			
			int noOfLines = linesOfFile.size(); 
			List<String> list1 = linesOfFile.subList(0, noOfLines/4);
		    List<String> list2 = linesOfFile.subList(noOfLines/4, noOfLines/2);
		    List<String> list3 = linesOfFile.subList(noOfLines/2, 3*noOfLines/4);
		    List<String> list4 = linesOfFile.subList(3*noOfLines/4, noOfLines);
			
		    //Create 4 threads to process each sub list
		    Thread thread1 = new Thread(new CoarseLockRunnable(list1, fibonacciFlag)); 
		    Thread thread2 = new Thread(new CoarseLockRunnable(list2, fibonacciFlag)); 
		    Thread thread3 = new Thread(new CoarseLockRunnable(list3, fibonacciFlag)); 
		    Thread thread4 = new Thread(new CoarseLockRunnable(list4, fibonacciFlag)); 
	   
		    // Start timing monitoring and threads
			start = System.currentTimeMillis();
			thread1.start();
			thread2.start();
			thread3.start();
			thread4.start();
			
			 try
			 {
				// Wait for each thread to finish
				 thread1.join();
			     thread2.join();
			     thread3.join();
			     thread4.join();
			 }
			 catch(InterruptedException e)
			 {
				 e.printStackTrace();
			 }
			
			// stop measuring time
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
	
	public class CoarseLockRunnable implements Runnable
	{
		private List<String> linesOfFile;
		private boolean fibonacciFlag;
		
		public CoarseLockRunnable(List<String> linesOfFile, boolean fibonacciFlag)
		{
			this.linesOfFile = linesOfFile;
			this.fibonacciFlag = fibonacciFlag;
		}
		
		public void run()
		{
			calculateAvgOfMaxTemp(linesOfFile, fibonacciFlag);
		}
		
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
					
					// Lock on entire data structure 
					synchronized (accumulator) 
					{	
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
}
