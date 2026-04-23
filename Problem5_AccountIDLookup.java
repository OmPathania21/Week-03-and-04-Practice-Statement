import java.util.*;

/**
 * Problem 5: Account ID Lookup in Transaction Logs
 * Implements Linear Search O(n) and Binary Search O(log n)
 * Tracks comparisons and handles duplicate occurrences
 */
public class Problem5_AccountIDLookup {
    
    static class TransactionLog {
        String accountId;
        String amount;
        
        TransactionLog(String accountId, String amount) {
            this.accountId = accountId;
            this.amount = amount;
        }
        
        @Override
        public String toString() {
            return accountId;
        }
    }
    
    /**
     * Linear Search: O(n) - finds first occurrence
     * Counts comparisons for analysis
     */
    static class LinearSearchResult {
        int index;
        int comparisons;
        boolean found;
        
        LinearSearchResult(int index, int comparisons, boolean found) {
            this.index = index;
            this.comparisons = comparisons;
            this.found = found;
        }
    }
    
    static LinearSearchResult linearSearchFirst(TransactionLog[] logs, String target) {
        int comparisons = 0;
        
        for (int i = 0; i < logs.length; i++) {
            comparisons++;
            if (logs[i].accountId.equals(target)) {
                return new LinearSearchResult(i, comparisons, true);
            }
        }
        
        return new LinearSearchResult(-1, comparisons, false);
    }
    
    /**
     * Linear Search: Find last occurrence
     */
    static LinearSearchResult linearSearchLast(TransactionLog[] logs, String target) {
        int comparisons = 0;
        int lastIndex = -1;
        
        for (int i = 0; i < logs.length; i++) {
            comparisons++;
            if (logs[i].accountId.equals(target)) {
                lastIndex = i;
            }
        }
        
        return new LinearSearchResult(lastIndex, comparisons, lastIndex != -1);
    }
    
    /**
     * Binary Search: O(log n) - requires sorted input
     * Returns first occurrence index
     */
    static class BinarySearchResult {
        int index;
        int comparisons;
        boolean found;
        
        BinarySearchResult(int index, int comparisons, boolean found) {
            this.index = index;
            this.comparisons = comparisons;
            this.found = found;
        }
    }
    
    static BinarySearchResult binarySearchExact(TransactionLog[] logs, String target) {
        int low = 0, high = logs.length - 1;
        int comparisons = 0;
        
        while (low <= high) {
            int mid = low + (high - low) / 2;
            comparisons++;
            
            int cmp = logs[mid].accountId.compareTo(target);
            
            if (cmp == 0) {
                // Found - find first occurrence by going left
                int firstIndex = mid;
                while (firstIndex > 0 && logs[firstIndex - 1].accountId.equals(target)) {
                    firstIndex--;
                }
                return new BinarySearchResult(firstIndex, comparisons, true);
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        
        return new BinarySearchResult(-1, comparisons, false);
    }
    
    /**
     * Count occurrences of target in sorted array
     */
    static int countOccurrences(TransactionLog[] logs, String target) {
        BinarySearchResult firstResult = binarySearchFirst(logs, target);
        if (!firstResult.found) return 0;
        
        BinarySearchResult lastResult = binarySearchLast(logs, target);
        return lastResult.index - firstResult.index + 1;
    }
    
    /**
     * Binary Search: Find first occurrence
     */
    static BinarySearchResult binarySearchFirst(TransactionLog[] logs, String target) {
        int low = 0, high = logs.length - 1;
        int result = -1;
        int comparisons = 0;
        
        while (low <= high) {
            int mid = low + (high - low) / 2;
            comparisons++;
            
            int cmp = logs[mid].accountId.compareTo(target);
            
            if (cmp == 0) {
                result = mid;
                high = mid - 1; // Continue searching left
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        
        return new BinarySearchResult(result, comparisons, result != -1);
    }
    
    /**
     * Binary Search: Find last occurrence
     */
    static BinarySearchResult binarySearchLast(TransactionLog[] logs, String target) {
        int low = 0, high = logs.length - 1;
        int result = -1;
        int comparisons = 0;
        
        while (low <= high) {
            int mid = low + (high - low) / 2;
            comparisons++;
            
            int cmp = logs[mid].accountId.compareTo(target);
            
            if (cmp == 0) {
                result = mid;
                low = mid + 1; // Continue searching right
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        
        return new BinarySearchResult(result, comparisons, result != -1);
    }
    
    public static void main(String[] args) {
        // Unsorted logs for linear search
        TransactionLog[] unsortedLogs = {
            new TransactionLog("accC", "100"),
            new TransactionLog("accB", "250"),
            new TransactionLog("accA", "500"),
            new TransactionLog("accB", "150")
        };
        
        // Sorted logs for binary search
        TransactionLog[] sortedLogs = {
            new TransactionLog("accA", "500"),
            new TransactionLog("accB", "250"),
            new TransactionLog("accB", "150"),
            new TransactionLog("accC", "100")
        };
        
        System.out.println("=== Problem 5: Account ID Lookup ===\n");
        System.out.println("Unsorted logs:");
        for (TransactionLog log : unsortedLogs) {
            System.out.println("  " + log);
        }
        
        System.out.println("\nSorted logs:");
        for (TransactionLog log : sortedLogs) {
            System.out.println("  " + log);
        }
        
        // Linear Search - First Occurrence
        System.out.println("\n--- Linear Search: First Occurrence of 'accB' ---");
        LinearSearchResult linearFirst = linearSearchFirst(unsortedLogs, "accB");
        System.out.println("Index: " + linearFirst.index);
        System.out.println("Comparisons: " + linearFirst.comparisons);
        if (linearFirst.found) {
            System.out.println("Found: " + unsortedLogs[linearFirst.index]);
        }
        
        // Linear Search - Last Occurrence
        System.out.println("\n--- Linear Search: Last Occurrence of 'accB' ---");
        LinearSearchResult linearLast = linearSearchLast(unsortedLogs, "accB");
        System.out.println("Index: " + linearLast.index);
        System.out.println("Comparisons: " + linearLast.comparisons);
        if (linearLast.found) {
            System.out.println("Found: " + unsortedLogs[linearLast.index]);
        }
        
        // Binary Search - Exact Match
        System.out.println("\n--- Binary Search: Exact Match of 'accB' ---");
        BinarySearchResult binaryExact = binarySearchExact(sortedLogs, "accB");
        System.out.println("Index: " + binaryExact.index);
        System.out.println("Comparisons: " + binaryExact.comparisons);
        if (binaryExact.found) {
            System.out.println("Found: " + sortedLogs[binaryExact.index]);
        }
        
        // Count Occurrences
        System.out.println("\n--- Count Occurrences of 'accB' ---");
        int count = countOccurrences(sortedLogs, "accB");
        System.out.println("Count: " + count);
        
        // Not Found Case
        System.out.println("\n--- Search for Non-existent 'accD' ---");
        LinearSearchResult notFoundLinear = linearSearchFirst(unsortedLogs, "accD");
        System.out.println("Linear - Comparisons: " + notFoundLinear.comparisons + ", Found: " + notFoundLinear.found);
        
        BinarySearchResult notFoundBinary = binarySearchExact(sortedLogs, "accD");
        System.out.println("Binary - Comparisons: " + notFoundBinary.comparisons + ", Found: " + notFoundBinary.found);
        
        // Larger dataset comparison
        System.out.println("\n=== Larger Dataset Test (100 items) ===");
        TransactionLog[] largeDataset = new TransactionLog[100];
        for (int i = 0; i < 100; i++) {
            String accId = "acc" + ((i % 10) < 3 ? "X" : "Y");
            largeDataset[i] = new TransactionLog(accId, "amount" + i);
        }
        
        // Worst case linear search
        System.out.println("\nLinear search for 'accZ' (worst case): ");
        LinearSearchResult linearWorst = linearSearchFirst(largeDataset, "accZ");
        System.out.println("  Comparisons: " + linearWorst.comparisons + " (found: " + linearWorst.found + ")");
        
        // Sorted for binary
        Arrays.sort(largeDataset, (a, b) -> a.accountId.compareTo(b.accountId));
        System.out.println("\nBinary search for 'accZ' on sorted data: ");
        BinarySearchResult binaryWorst = binarySearchExact(largeDataset, "accZ");
        System.out.println("  Comparisons: " + binaryWorst.comparisons + " (found: " + binaryWorst.found + ")");
        System.out.println("\nComparison: Linear=100, Binary≈7 (100 items, log₂(100)≈7)");
    }
}
