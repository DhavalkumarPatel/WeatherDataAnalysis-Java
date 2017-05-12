package HW1Part1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Map.Entry;

/** NO-SHARING: Per-thread data structure multi-threaded version that assigns subsets of
* the input List<String> for processing by separate threads. Each thread work on its own 
* separate instance of the accumulation data structure. Hence no locks are needed. 
* However, it need extra time for reducing the separate data structures into a single one
* using the main thread.
 * @author dspatel
 *
 */
public class NoSharing
{
	private Map<String, SumCountPair> accumulator = new HashMap<>();
	
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
			
		    NoSharingRunnable nsr1 = new NoSharingRunnable(list1, fibonacciFlag);
		    NoSharingRunnable nsr2 = new NoSharingRunnable(list2, fibonacciFlag);
		    NoSharingRunnable nsr3 = new NoSharingRunnable(list3, fibonacciFlag);
		    NoSharingRunnable nsr4 = new NoSharingRunnable(list4, fibonacciFlag);
		    
		    //Create 4 threads to process each sub list
		    Thread thread1 = new Thread(nsr1); 
		    Thread thread2 = new Thread(nsr2); 
		    Thread thread3 = new Thread(nsr3); 
		    Thread thread4 = new Thread(nsr4); 
	   
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
			
			// Merge the all sub results to main accumulator
			this.mergeResults(nsr1.localAccumulator);
			this.mergeResults(nsr2.localAccumulator);
			this.mergeResults(nsr3.localAccumulator);
			this.mergeResults(nsr4.localAccumulator);
			
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
	
	public class NoSharingRunnable implements Runnable
	{
		private List<String> linesOfFile;
		private boolean fibonacciFlag;
		private Map<String, SumCountPair> localAccumulator;
		
		public NoSharingRunnable(List<String> linesOfFile, boolean fibonacciFlag)
		{
			this.linesOfFile = linesOfFile;
			this.fibonacciFlag = fibonacciFlag;
			this.localAccumulator = new HashMap<>();
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
					
					// search in local accumulator
					SumCountPair sumCountPair = this.localAccumulator.get(stationId);
					
					// If station is not found yet then create the new default object
					if(null == sumCountPair)
					{
						sumCountPair = new SumCountPair();
					}
					
					// Update the current sum and count of a station
					sumCountPair.updateSumCountPair(tmax, 1);
						
					// Delay introduced before changing accumulator 
					// We can introduce the delay while updating main accumulator as well
					if(fibonacciFlag)
					{
						getNthFibonacci(17);
					}
						
					// Update the local accumulator
					this.localAccumulator.put(stationId, sumCountPair);
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
	
	/**
	 * Merge the results of local accumulator with common shared main accumulator
	 * @param localAccumulator
	 */
	public void mergeResults(Map<String, SumCountPair> localAccumulator)
	{
		Iterator<Entry<String,SumCountPair>> i = localAccumulator.entrySet().iterator();
        while (i.hasNext()) 
        {
            Entry<String,SumCountPair> e = i.next();
            
			if(accumulator.containsKey(e.getKey()))
			{
				SumCountPair accumulatorVal = accumulator.get(e.getKey());
				accumulatorVal.updateSumCountPair(e.getValue().getSum(), e.getValue().getCount());
				accumulator.put(e.getKey(), accumulatorVal);				
			}
			else
			{
				accumulator.put(e.getKey(), e.getValue());
			}
		}
	}
}