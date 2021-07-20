
import java.util.ArrayList;
import java.util.List;

public class HowSumSolution {
    public static List<Integer> result = new ArrayList<>();

    public static void main(String[] args) {
        int[] numbers = new int[]{5, 3, 4};
        System.out.print(howSum(numbers, 7));
    }

    public static List<Integer> howSum(int[] numbers, int target) {
        if (target == 0) return new ArrayList<>();
        if (target < 0) return null;

        for (int number : numbers) {
            int remainder = target - number;

            List<Integer> resultSum = howSum(numbers, remainder);
            System.out.println("number:::> "+number +" remainder:::> "+remainder);

            if (resultSum != null) {
                System.out.println(number);
                result.add(number);
                return result;
            }
        }
        return null;
    }
}
