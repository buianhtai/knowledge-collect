Given an array --> return the minimun 
///https://www.youtube.com/watch?v=oBt53YbR9Kk&t=7739s

    public static void main(String[] args) {
        System.out.println(bestSum(new int[]{5,3,4,7}, 7)); //--> [7]
        System.out.println(bestSum(new int[]{2,3,5}, 8));// ---> [3,5]
        System.out.println(bestSum(new int[]{1,4,5}, 8)); //---> [4,4]
    }
public static List<Integer> bestSum(int[] numbers, int target) {
        if (target == 0) return new ArrayList<>();
        if (target < 0) return null;
        List<Integer> shortestSum = null;
        for (int number : numbers) {
            int remainder = target - number;
            List<Integer> remainderCombination = bestSum(numbers, remainder);
            if (remainderCombination != null) {
                remainderCombination.add(number);
                if (shortestSum == null || remainderCombination.size() < shortestSum.size()) {
                    shortestSum= remainderCombination;
                }
            }
        }
        return shortestSum;
    }
