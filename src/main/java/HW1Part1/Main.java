package HW1Part1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Main program to run all different version of average calculations
 * @author dspatel
 *
 */
public class Main 
{	
	public static void main(String[] args) 
	{
		String filename = "input/1912.csv";
		boolean fibonacciFlag = false;
		boolean verifyAverageResultsFlag = false;
		
		if(args.length >= 3)
		{
			if(null != args[0] && !"".equalsIgnoreCase(args[0]))
			{
				filename = args[0];
			}
			if(null != args[1] && "Y".equalsIgnoreCase(args[1]))
			{
				fibonacciFlag = true;
			}
	    	if(null != args[2] && "Y".equalsIgnoreCase(args[2]))
			{
	    		verifyAverageResultsFlag = true;
			} 
		}
	
		// loads the file into java List
    	List<String> linesOfFile = loader(filename);
    	
    	
    	// Run all different versions to calculate the average and to measure the performance
    	System.out.println("SEQ");
    	Map<String, SumCountPair> seqAccumulator = new Sequential().testAndPrintPerformanceStats(linesOfFile, fibonacciFlag);
    	
        System.out.println("\nNO-LOCK");
        Map<String, SumCountPair> noLockAccumulator =  new NoLock().testAndPrintPerformanceStats(linesOfFile, fibonacciFlag);
        
        System.out.println("\nCOARSE-LOCK");
        Map<String, SumCountPair> coarseLockAccumulator = new CoarseLock().testAndPrintPerformanceStats(linesOfFile, fibonacciFlag);
        
    	System.out.println("\nFINE-LOCK");
    	Map<String, SumCountPair> fineLockAccumulator = new FineLock().testAndPrintPerformanceStats(linesOfFile, fibonacciFlag);
        
        System.out.println("\nNO-SHARING");
        Map<String, SumCountPair> noSharingAccumulator = new NoSharing().testAndPrintPerformanceStats(linesOfFile, fibonacciFlag);
        
        
        // Compare all accumulators calculated via different programs with sequential output
        if(verifyAverageResultsFlag)
        {
        	System.out.println("\nSEQ and NO-LOCK results are equal ? : " + verifyAverageResults(seqAccumulator, noLockAccumulator));
        	System.out.println("SEQ and COARSE-LOCK results are equal ? : " + verifyAverageResults(seqAccumulator, coarseLockAccumulator));
        	System.out.println("SEQ and FINE-LOCK results are equal ? : " + verifyAverageResults(seqAccumulator, fineLockAccumulator));
        	System.out.println("SEQ and NO-SHARING results are equal ? : " + verifyAverageResults(seqAccumulator, noSharingAccumulator));
        }
	}
	
	/**
	 * loader routine that takes an input filename, reads the file, and returns a List<String>
	 * containing the lines of the file.
	 * @param filename
	 * @return
	 */
	public static List<String> loader(String filename)
	{
		List<String> linesofFile = new ArrayList<String>();

		FileReader fr;
		try 
		{
			fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			
			while ((line = br.readLine()) != null) 
			{
				linesofFile.add(line);
			}
			fr.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		return linesofFile;
	}
	
	/**
	 * Compares two different accumlators calculated via different program implementations
	 * @param acc1
	 * @param acc2
	 * @return
	 */
	public static boolean verifyAverageResults(Map<String, SumCountPair> acc1, Map<String, SumCountPair> acc2)
	{
		Iterator<Entry<String,SumCountPair>> i = acc1.entrySet().iterator();
        while (i.hasNext()) 
        {
            Entry<String,SumCountPair> e = i.next();
            if (acc2.containsKey(e.getKey()))
            {
            	if (e.getValue().getAverage() != acc2.get(e.getKey()).getAverage())
                {
                	return false;
                }
            } 
        	else 
        	{
                return false;
            }
        }
        return true;
	}
}
