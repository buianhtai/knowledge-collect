import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solution {

  //non-overlapping subarrays with sum equals target
    public static List<List<Integer>> findSubarraySum(int[] arrays, int target) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> subArray;
        Set<Integer> prefixSum = new HashSet<>();

        int sum = 0;
        prefixSum.add(sum);

        subArray = new ArrayList<>();

        for (int value : arrays) {
            sum += value;

            subArray.add(value);

            if (sum > target) {
                sum = 0;

                subArray = new ArrayList<>();

                prefixSum.clear();
                prefixSum.add(0);
            }

            if (prefixSum.contains(sum - target)) {

                result.add(subArray);

                sum = 0;

                subArray = new ArrayList<>();

                prefixSum.clear();
                prefixSum.add(0);
            }

            prefixSum.add(sum);
        }
        return result;
    }

    public static void main(String[] args) {
        int[] arrays = new int[]{1, 1, 3, 1, 5, 7, 3, 1, 2};
        System.out.println(findSubarraySum(arrays, 6));
      //[[1, 1, 3, 1], [3, 1, 2]]
    }
}
