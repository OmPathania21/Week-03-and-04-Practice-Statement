import java.util.*;

/**
 * Problem 6: Risk Threshold Binary Lookup
 * Implements Linear Search and Binary Search variants (lower_bound, upper_bound)
 * Finds floor/ceiling values in sorted risk bands
 */
public class Problem6_RiskThresholdBinaryLookup {
    
    static class RiskBand {
        int threshold;
        String category;
        
        RiskBand(int threshold, String category) {
            this.threshold = threshold;
            this.category = category;
        }
        
        @Override
        public String toString() {
            return threshold + "(" + category + ")";
        }
    }
    
    /**
     * Linear Search: Find exact match or nearest values
     */
    static class LinearSearchResult {
        int exactIndex;
        int floorIndex;
        int ceilingIndex;
        int comparisons;
        
        LinearSearchResult(int exactIndex, int floorIndex, int ceilingIndex, int comparisons) {
            this.exactIndex = exactIndex;
            this.floorIndex = floorIndex;
            this.ceilingIndex = ceilingIndex;
            this.comparisons = comparisons;
        }
    }
    
    static LinearSearchResult linearSearchThreshold(int[] sortedRisks, int target) {
        int comparisons = 0;
        int exactIdx = -1;
        int floorIdx = -1;
        int ceilingIdx = -1;
        
        for (int i = 0; i < sortedRisks.length; i++) {
            comparisons++;
            
            if (sortedRisks[i] == target) {
                exactIdx = i;
            } else if (sortedRisks[i] < target) {
                floorIdx = i;
            } else if (ceilingIdx == -1) {
                ceilingIdx = i;
            }
        }
        
        return new LinearSearchResult(exactIdx, floorIdx, ceilingIdx, comparisons);
    }
    
    /**
     * Binary Search: Lower Bound (first element >= target)
     */
    static class BinarySearchResult {
        int index;
        int comparisons;
        boolean exact;
        
        BinarySearchResult(int index, int comparisons, boolean exact) {
            this.index = index;
            this.comparisons = comparisons;
            this.exact = exact;
        }
    }
    
    static BinarySearchResult lowerBound(int[] sortedRisks, int target) {
        int low = 0, high = sortedRisks.length;
        int comparisons = 0;
        
        while (low < high) {
            int mid = low + (high - low) / 2;
            comparisons++;
            
            if (sortedRisks[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        
        boolean exact = (low < sortedRisks.length && sortedRisks[low] == target);
        return new BinarySearchResult(low, comparisons, exact);
    }
    
    /**
     * Binary Search: Upper Bound (first element > target)
     */
    static BinarySearchResult upperBound(int[] sortedRisks, int target) {
        int low = 0, high = sortedRisks.length;
        int comparisons = 0;
        
        while (low < high) {
            int mid = low + (high - low) / 2;
            comparisons++;
            
            if (sortedRisks[mid] <= target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        
        return new BinarySearchResult(low, comparisons, false);
    }
    
    /**
     * Find floor value: largest value <= target
     */
    static Integer findFloor(int[] sortedRisks, int target) {
        BinarySearchResult lower = lowerBound(sortedRisks, target);
        
        if (lower.exact) {
            return sortedRisks[lower.index];
        }
        
        if (lower.index > 0) {
            return sortedRisks[lower.index - 1];
        }
        
        return null;
    }
    
    /**
     * Find ceiling value: smallest value >= target
     */
    static Integer findCeiling(int[] sortedRisks, int target) {
        BinarySearchResult lower = lowerBound(sortedRisks, target);
        
        if (lower.index < sortedRisks.length) {
            return sortedRisks[lower.index];
        }
        
        return null;
    }
    
    /**
     * Find insertion point for new client in risk table
     */
    static int findInsertionPoint(int[] sortedRisks, int clientRisk) {
        BinarySearchResult lower = lowerBound(sortedRisks, clientRisk);
        return lower.index;
    }
    
    public static void main(String[] args) {
        int[] sortedRisks = {10, 25, 50, 100};
        int target = 30;
        
        System.out.println("=== Problem 6: Risk Threshold Binary Lookup ===\n");
        System.out.println("Sorted risk bands: " + Arrays.toString(sortedRisks));
        System.out.println("Target threshold: " + target);
        
        // Linear Search
        System.out.println("\n--- Linear Search: threshold = " + target + " ---");
        LinearSearchResult linearResult = linearSearchThreshold(sortedRisks, target);
        System.out.println("Exact match: " + (linearResult.exactIndex >= 0 ? sortedRisks[linearResult.exactIndex] : "Not found"));
        if (linearResult.floorIndex >= 0) {
            System.out.println("Floor (≤ target): " + sortedRisks[linearResult.floorIndex]);
        }
        if (linearResult.ceilingIndex >= 0) {
            System.out.println("Ceiling (≥ target): " + sortedRisks[linearResult.ceilingIndex]);
        }
        System.out.println("Comparisons: " + linearResult.comparisons);
        
        // Binary Search - Floor
        System.out.println("\n--- Binary Search: Floor(" + target + ") ---");
        Integer floor = findFloor(sortedRisks, target);
        System.out.println("Floor value: " + floor + " (largest ≤ " + target + ")");
        BinarySearchResult floorSearch = lowerBound(sortedRisks, target);
        System.out.println("Comparisons: " + floorSearch.comparisons);
        
        // Binary Search - Ceiling
        System.out.println("\n--- Binary Search: Ceiling(" + target + ") ---");
        Integer ceiling = findCeiling(sortedRisks, target);
        System.out.println("Ceiling value: " + ceiling + " (smallest ≥ " + target + ")");
        BinarySearchResult ceilingSearch = upperBound(sortedRisks, target);
        System.out.println("Comparisons: " + ceilingSearch.comparisons);
        
        // Find insertion points for new clients
        System.out.println("\n--- Dynamic Risk Pricing: New Client Insertion Points ---");
        int[] newClientRisks = {5, 15, 50, 75, 120};
        
        for (int clientRisk : newClientRisks) {
            int insertPoint = findInsertionPoint(sortedRisks, clientRisk);
            System.out.println("New client risk=" + clientRisk + " → Insert at index " + insertPoint);
            
            String band = "";
            if (insertPoint == 0) {
                band = "Below " + sortedRisks[0];
            } else if (insertPoint == sortedRisks.length) {
                band = "Above " + sortedRisks[sortedRisks.length - 1];
            } else {
                band = "Between " + sortedRisks[insertPoint - 1] + " and " + sortedRisks[insertPoint];
            }
            System.out.println("  → Position: " + band);
        }
        
        // Exact match cases
        System.out.println("\n--- Exact Match Test ---");
        int[] testTargets = {10, 25, 50, 100};
        
        for (int testTarget : testTargets) {
            System.out.println("\nTarget: " + testTarget);
            
            BinarySearchResult lower = lowerBound(sortedRisks, testTarget);
            BinarySearchResult upper = upperBound(sortedRisks, testTarget);
            
            System.out.println("  Lower bound index: " + lower.index + " (exact: " + lower.exact + ")");
            System.out.println("  Upper bound index: " + upper.index);
            
            Integer flr = findFloor(sortedRisks, testTarget);
            Integer ceil = findCeiling(sortedRisks, testTarget);
            System.out.println("  Floor: " + flr + ", Ceiling: " + ceil);
        }
        
        // Complexity comparison: larger dataset
        System.out.println("\n=== Performance Test: 1000 Risk Bands ===");
        int[] largeSortedRisks = new int[1000];
        for (int i = 0; i < 1000; i++) {
            largeSortedRisks[i] = (i + 1) * 10;
        }
        
        int testThreshold = 5555;
        
        LinearSearchResult linearLarge = linearSearchThreshold(largeSortedRisks, testThreshold);
        System.out.println("Linear search for " + testThreshold + ": " + linearLarge.comparisons + " comparisons");
        
        Integer floorLarge = findFloor(largeSortedRisks, testThreshold);
        BinarySearchResult binaryLarge = lowerBound(largeSortedRisks, testThreshold);
        System.out.println("Binary search for " + testThreshold + ": " + binaryLarge.comparisons + " comparisons");
        System.out.println("Floor: " + floorLarge + ", Ceiling: " + findCeiling(largeSortedRisks, testThreshold));
        
        System.out.println("\nComparison:");
        System.out.println("  Linear: " + linearLarge.comparisons + " comparisons (O(n))");
        System.out.println("  Binary: " + binaryLarge.comparisons + " comparisons (O(log n))");
        System.out.println("  Speedup: " + String.format("%.1fx", (double) linearLarge.comparisons / binaryLarge.comparisons));
    }
}
