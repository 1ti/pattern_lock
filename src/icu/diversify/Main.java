package icu.diversify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static int totalBadPath = 0;
    public static int totalPerm = 0;
    public static int currentBadPath = 0;
    public static int currentPerm = 0;

    private static int[][] adjacency = {
            {0, 1, 0, 1, 1, 1, 0, 1, 0},
            {1, 0, 1, 1, 1, 1, 1, 0, 1},
            {0, 1, 0, 1, 1, 1, 0, 1, 0},
            {1, 1, 1, 0, 1, 0, 1, 1, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 1, 1, 0, 1, 0, 1, 1, 1},
            {0, 1, 0, 1, 1, 1, 0, 1, 0},
            {1, 0, 1, 1, 1, 1, 1, 0, 1},
            {0, 1, 0, 1, 1, 1, 0, 1, 0},
    };


    public static void permute(int r, List<Integer> values, List<Integer> permutation) throws IOException {
        if (permutation.size() == r) {
            // Base case: we have generated a complete permutation of length r
            if (!evaluateAcceptedPattern(adjacency, permutation.stream().mapToInt(i->i).toArray())) {
                totalBadPath++;
                currentBadPath++;
            }
            totalPerm++;
            currentPerm++;
        } else if (values.isEmpty()) {
            // Base case: there are no remaining values to choose from
            return;
        } else {
            // Recursive case: generate all permutations of the remaining elements
            for (int i = 0; i < values.size(); i++) {
                // Choose the i-th element and append it to the permutation
                int value = values.get(i);
                permutation.add(value);

                // Generate all permutations of the remaining elements
                List<Integer> remainingValues = new ArrayList<>(values);
                remainingValues.remove(i);
                permute(r, remainingValues, permutation);

                // Remove the element from the permutation and try the next one
                permutation.remove(permutation.size() - 1);
            }
        }
    }


    public static void main(String[] args) throws IOException {
        for (int i = 4; i <= 9; i++) {
            int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
            List<Integer> permutation = new ArrayList<>();
            permute(i, Arrays.stream(array).boxed().collect(Collectors.toList()), permutation);
            System.out.println("Path length: " + i);
            System.out.println("Total permutations: " + currentPerm);
            System.out.println("Total bad paths: " + currentBadPath);
            System.out.println("Total paths: " + (currentPerm - currentBadPath));
            System.out.println("===================================");
            currentPerm = 0;
            currentBadPath = 0;
        }

        System.out.println("Total permutations: " + totalPerm);
        System.out.println("Total bad paths: " + totalBadPath);
        System.out.println("Total paths: " + (totalPerm - totalBadPath));


    }

    private static boolean evaluateAcceptedPattern(int[][] adjacency, int[] inputPattern) {
        // Create variables
        int firstCounter, secondCounter;

        // Create buffer array to store temporary path
        int[] bufferPattern = new int[inputPattern.length];

        // Initialise array with zeros
        Arrays.fill(bufferPattern, 0);

        // Initialise variables
        firstCounter = 0; // Two counters are used to keep track of the current and next values in the pattern
        secondCounter = 1;

        // Here I go through the pattern array comparing all the consecutive values and applying the adjecency rule
        for (int i = 0; i < inputPattern.length; i++) {
            int firstValue = inputPattern[firstCounter];
            if (firstValue == inputPattern[inputPattern.length-1]) {
                break;
            }
            int secondValue = inputPattern[secondCounter];
            if (firstCounter != inputPattern.length-1){
                secondValue = inputPattern[secondCounter];
            }
            if (adjacency[firstValue-1][secondValue-1] == 0) {
                // do the average thing
                if ((firstValue+secondValue)%2!=0){
                    // Exit the loop because the combination is accepted
                    break;
                }

                int averageElement = (firstValue+secondValue)/2;
                if (IntStream.of(bufferPattern).noneMatch(x -> x==averageElement)) {
                    // No need to check the rest of the combinations as we can just return early
                    return false;
                }

            }
            bufferPattern[firstCounter] = firstValue;
            firstCounter++;
            secondCounter++;
        }
        return true;
    }
}
