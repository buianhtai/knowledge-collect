import java.util.Arrays;
import java.util.Comparator;

class job{
    int startTime;
    int endTime;
    int jobProfit;

    public job(int val){
        this.startTime = val;
        this.endTime = val;
        this.jobProfit = val;
    }

}

public class Solution{
    
    // Function to find the latest non conflicting job.
    public static int nonConflictingJob(job[] jobs, int i)
    {
        for (int j = i - 1; j >= 0; j--)
        {
            if (jobs[j].endTime <= jobs[i].startTime)
            {
                return j;
            }
        }

        return -1;
    }

    public static long maxProfitHelper(job[] jobs, int current)
    {
    
        if (current == 0){
            return jobs[0].jobProfit;
        }
    
        // First finding the profit by excluding the current job.
        long excProfit = maxProfitHelper(jobs, current - 1);
    
        // Then, finding the profit by including the current job.
        long incProfit = jobs[current].jobProfit;
    
        // Finding the index of closest non conflicting job with current job.
        int index = nonConflictingJob(jobs, current);
    
        // If the index is not equal to -1, recursively calling for that job.
        if (index != -1){
            incProfit += maxProfitHelper(jobs, index);
        }
    
        return Math.max(incProfit, excProfit);
    }
    

    public static long findMaxProfit(int[] start, int[] end, int[] profit){
    
        int n = start.length;
    
        // Creating jobs array of size N.
        job[] jobs = new job[n];

        for (int i = 0; i < n; i++){   
            job temp =  new job(0);
            temp.startTime = start[i];
            temp.endTime = end[i]; 
            temp.jobProfit = profit[i];
            jobs[i] = temp;
        }

        Arrays.sort(jobs, new Comparator<job>() {    
            @Override
            public int compare(job a, job b) {
                return a.endTime - b.endTime; 
            }               
        });

    
        long maxProfit = maxProfitHelper(jobs, n - 1);
    
        return maxProfit;
    }
}
