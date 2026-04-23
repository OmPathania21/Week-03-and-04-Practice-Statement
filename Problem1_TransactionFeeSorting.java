import java.util.*;

/**
 * Problem 1: Transaction Fee Sorting for Audit Compliance
 * Implements Bubble Sort (≤100 items) and Insertion Sort (100-1000 items)
 * with outlier detection for fees > $50
 */
public class Problem1_TransactionFeeSorting {
    
    static class Transaction {
        String id;
        double fee;
        String timestamp;
        
        Transaction(String id, double fee, String timestamp) {
            this.id = id;
            this.fee = fee;
            this.timestamp = timestamp;
        }
        
        @Override
        public String toString() {
            return String.format("%s:%.1f@%s", id, fee, timestamp);
        }
    }
    
    /**
     * Bubble Sort: O(n²) - for small batches ≤ 100
     * Tracks passes and swaps for optimization analysis
     */
    static class BubbleSortResult {
        List<Transaction> sorted;
        int passes;
        int swaps;
        
        BubbleSortResult(List<Transaction> sorted, int passes, int swaps) {
            this.sorted = sorted;
            this.passes = passes;
            this.swaps = swaps;
        }
    }
    
    static BubbleSortResult bubbleSortByFee(List<Transaction> transactions) {
        List<Transaction> arr = new ArrayList<>(transactions);
        int n = arr.size();
        int passes = 0;
        int swaps = 0;
        
        // Bubble Sort with early termination
        for (int i = 0; i < n - 1; i++) {
            passes++;
            boolean swapped = false;
            
            for (int j = 0; j < n - i - 1; j++) {
                if (arr.get(j).fee > arr.get(j + 1).fee) {
                    // Swap
                    Transaction temp = arr.get(j);
                    arr.set(j, arr.get(j + 1));
                    arr.set(j + 1, temp);
                    swaps++;
                    swapped = true;
                }
            }
            
            // Early termination if no swaps occurred
            if (!swapped) break;
        }
        
        return new BubbleSortResult(arr, passes, swaps);
    }
    
    /**
     * Insertion Sort: O(n²) - for medium batches 100-1000
     * Sorts by fee + timestamp (stable sort)
     */
    static class InsertionSortResult {
        List<Transaction> sorted;
        int shifts;
        
        InsertionSortResult(List<Transaction> sorted, int shifts) {
            this.sorted = sorted;
            this.shifts = shifts;
        }
    }
    
    static InsertionSortResult insertionSortByFeeAndTimestamp(List<Transaction> transactions) {
        List<Transaction> arr = new ArrayList<>(transactions);
        int n = arr.size();
        int shifts = 0;
        
        // Build sorted subarray incrementally
        for (int i = 1; i < n; i++) {
            Transaction key = arr.get(i);
            int j = i - 1;
            
            // Shift elements larger than key
            while (j >= 0 && compareTransactions(arr.get(j), key) > 0) {
                arr.set(j + 1, arr.get(j));
                shifts++;
                j--;
            }
            arr.set(j + 1, key);
        }
        
        return new InsertionSortResult(arr, shifts);
    }
    
    /**
     * Compare two transactions by fee, then by timestamp
     * Returns: < 0 if t1 < t2, > 0 if t1 > t2, 0 if equal
     */
    static int compareTransactions(Transaction t1, Transaction t2) {
        if (t1.fee != t2.fee) {
            return Double.compare(t1.fee, t2.fee);
        }
        return t1.timestamp.compareTo(t2.timestamp);
    }
    
    /**
     * Identify high-fee outliers (> $50)
     */
    static List<Transaction> flagHighFeeOutliers(List<Transaction> transactions) {
        List<Transaction> outliers = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.fee > 50.0) {
                outliers.add(t);
            }
        }
        return outliers;
    }
    
    public static void main(String[] args) {
        // Sample transactions
        List<Transaction> transactions = Arrays.asList(
            new Transaction("id1", 10.5, "10:00"),
            new Transaction("id2", 25.0, "09:30"),
            new Transaction("id3", 5.0, "10:15")
        );
        
        System.out.println("=== Problem 1: Transaction Fee Sorting ===\n");
        System.out.println("Input transactions:");
        for (Transaction t : transactions) {
            System.out.println("  " + t);
        }
        
        // Bubble Sort
        System.out.println("\n--- Bubble Sort (by fee ascending) ---");
        BubbleSortResult bubbleResult = bubbleSortByFee(transactions);
        System.out.println("Sorted: " + bubbleResult.sorted);
        System.out.println("Passes: " + bubbleResult.passes + ", Swaps: " + bubbleResult.swaps);
        
        // Insertion Sort
        System.out.println("\n--- Insertion Sort (by fee + timestamp) ---");
        InsertionSortResult insertionResult = insertionSortByFeeAndTimestamp(transactions);
        System.out.println("Sorted: " + insertionResult.sorted);
        System.out.println("Shifts: " + insertionResult.shifts);
        
        // High-fee outliers
        System.out.println("\n--- High-fee Outliers (> $50) ---");
        List<Transaction> outliers = flagHighFeeOutliers(bubbleResult.sorted);
        System.out.println("Count: " + outliers.size());
        if (outliers.isEmpty()) {
            System.out.println("No outliers found");
        } else {
            for (Transaction t : outliers) {
                System.out.println("  " + t);
            }
        }
        
        // Test with larger dataset
        System.out.println("\n=== Larger Dataset Test ===");
        List<Transaction> largeSet = Arrays.asList(
            new Transaction("t1", 100.0, "09:00"),
            new Transaction("t2", 55.5, "09:15"),
            new Transaction("t3", 15.0, "09:30"),
            new Transaction("t4", 75.0, "09:45"),
            new Transaction("t5", 5.0, "10:00")
        );
        
        System.out.println("\nOriginal: " + largeSet);
        BubbleSortResult bubbleResult2 = bubbleSortByFee(largeSet);
        System.out.println("Bubble Sorted: " + bubbleResult2.sorted);
        System.out.println("Passes: " + bubbleResult2.passes + ", Swaps: " + bubbleResult2.swaps);
        
        List<Transaction> outliers2 = flagHighFeeOutliers(bubbleResult2.sorted);
        System.out.println("High-fee outliers: " + outliers2);
    }
}
