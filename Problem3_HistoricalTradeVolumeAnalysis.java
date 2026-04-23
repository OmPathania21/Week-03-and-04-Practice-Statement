import java.util.*;

/**
 * Problem 3: Historical Trade Volume Analysis
 * Implements Merge Sort (stable, O(n log n)) and Quick Sort (in-place, average O(n log n))
 * Merges sorted trade lists and computes total volume
 */
public class Problem3_HistoricalTradeVolumeAnalysis {
    
    static class Trade {
        String id;
        long volume;
        
        Trade(String id, long volume) {
            this.id = id;
            this.volume = volume;
        }
        
        @Override
        public String toString() {
            return id + ":" + volume;
        }
    }
    
    /**
     * Merge Sort: Stable, guaranteed O(n log n)
     * Preserves original order for equal volumes
     */
    static class MergeSortResult {
        Trade[] sorted;
        int comparisons;
        
        MergeSortResult(Trade[] sorted, int comparisons) {
            this.sorted = sorted;
            this.comparisons = comparisons;
        }
    }
    
    static class MergeSortHelper {
        int comparisons = 0;
        
        Trade[] mergeSort(Trade[] arr) {
            if (arr.length <= 1) return arr;
            
            Trade[] temp = new Trade[arr.length];
            mergeSortHelper(arr, 0, arr.length - 1, temp);
            return arr;
        }
        
        private void mergeSortHelper(Trade[] arr, int left, int right, Trade[] temp) {
            if (left < right) {
                int mid = left + (right - left) / 2;
                
                // Sort left half
                mergeSortHelper(arr, left, mid, temp);
                
                // Sort right half
                mergeSortHelper(arr, mid + 1, right, temp);
                
                // Merge
                merge(arr, left, mid, right, temp);
            }
        }
        
        private void merge(Trade[] arr, int left, int mid, int right, Trade[] temp) {
            int i = left;
            int j = mid + 1;
            int k = left;
            
            while (i <= mid && j <= right) {
                comparisons++;
                if (arr[i].volume <= arr[j].volume) {
                    temp[k++] = arr[i++];
                } else {
                    temp[k++] = arr[j++];
                }
            }
            
            while (i <= mid) {
                temp[k++] = arr[i++];
            }
            
            while (j <= right) {
                temp[k++] = arr[j++];
            }
            
            for (i = left; i <= right; i++) {
                arr[i] = temp[i];
            }
        }
    }
    
    static MergeSortResult mergeSortByVolumeAscending(Trade[] trades) {
        MergeSortHelper helper = new MergeSortHelper();
        Trade[] result = trades.clone();
        helper.mergeSort(result);
        return new MergeSortResult(result, helper.comparisons);
    }
    
    /**
     * Quick Sort: In-place, average O(n log n), worst O(n²)
     * Descending volume order
     */
    static class QuickSortResult {
        Trade[] sorted;
        int comparisons;
        
        QuickSortResult(Trade[] sorted, int comparisons) {
            this.sorted = sorted;
            this.comparisons = comparisons;
        }
    }
    
    static class QuickSortHelper {
        int comparisons = 0;
        
        void quickSort(Trade[] arr) {
            if (arr.length <= 1) return;
            quickSortHelper(arr, 0, arr.length - 1);
        }
        
        private void quickSortHelper(Trade[] arr, int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                quickSortHelper(arr, low, pi - 1);
                quickSortHelper(arr, pi + 1, high);
            }
        }
        
        private int partition(Trade[] arr, int low, int high) {
            // Median-of-three pivot selection
            int mid = low + (high - low) / 2;
            Trade pivot = medianOfThree(arr, low, mid, high);
            
            // Move pivot to end
            int pivotIndex = high;
            for (int i = low; i < high; i++) {
                if (arr[i] == pivot) {
                    swap(arr, i, high);
                    break;
                }
            }
            
            // Partition: descending order
            int i = low - 1;
            for (int j = low; j < high; j++) {
                comparisons++;
                if (arr[j].volume > pivot.volume) {
                    i++;
                    swap(arr, i, j);
                }
            }
            swap(arr, i + 1, high);
            return i + 1;
        }
        
        private Trade medianOfThree(Trade[] arr, int i, int j, int k) {
            if (arr[i].volume <= arr[j].volume && arr[j].volume <= arr[k].volume) return arr[j];
            if (arr[k].volume <= arr[j].volume && arr[j].volume <= arr[i].volume) return arr[j];
            if (arr[j].volume <= arr[i].volume && arr[i].volume <= arr[k].volume) return arr[i];
            if (arr[k].volume <= arr[i].volume && arr[i].volume <= arr[j].volume) return arr[i];
            return arr[k];
        }
        
        private void swap(Trade[] arr, int i, int j) {
            Trade temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
    
    static QuickSortResult quickSortByVolumeDescending(Trade[] trades) {
        QuickSortHelper helper = new QuickSortHelper();
        Trade[] result = trades.clone();
        helper.quickSort(result);
        return new QuickSortResult(result, helper.comparisons);
    }
    
    /**
     * Merge two sorted trade lists
     */
    static Trade[] mergeTwoSortedLists(Trade[] morning, Trade[] afternoon) {
        Trade[] merged = new Trade[morning.length + afternoon.length];
        int i = 0, j = 0, k = 0;
        
        while (i < morning.length && j < afternoon.length) {
            if (morning[i].volume <= afternoon[j].volume) {
                merged[k++] = morning[i++];
            } else {
                merged[k++] = afternoon[j++];
            }
        }
        
        while (i < morning.length) {
            merged[k++] = morning[i++];
        }
        
        while (j < afternoon.length) {
            merged[k++] = afternoon[j++];
        }
        
        return merged;
    }
    
    /**
     * Compute total volume
     */
    static long computeTotalVolume(Trade[] trades) {
        long total = 0;
        for (Trade t : trades) {
            total += t.volume;
        }
        return total;
    }
    
    public static void main(String[] args) {
        // Sample trades
        Trade[] trades = {
            new Trade("trade3", 500),
            new Trade("trade1", 100),
            new Trade("trade2", 300)
        };
        
        System.out.println("=== Problem 3: Historical Trade Volume Analysis ===\n");
        System.out.println("Input trades:");
        for (Trade t : trades) {
            System.out.println("  " + t);
        }
        
        // Merge Sort - Ascending
        System.out.println("\n--- Merge Sort (Volume Ascending - Stable) ---");
        MergeSortResult mergeResult = mergeSortByVolumeAscending(trades);
        System.out.print("Sorted: [");
        for (int i = 0; i < mergeResult.sorted.length; i++) {
            System.out.print(mergeResult.sorted[i]);
            if (i < mergeResult.sorted.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Comparisons: " + mergeResult.comparisons);
        
        // Quick Sort - Descending
        System.out.println("\n--- Quick Sort (Volume Descending - Median-of-3 Pivot) ---");
        QuickSortResult quickResult = quickSortByVolumeDescending(trades);
        System.out.print("Sorted: [");
        for (int i = 0; i < quickResult.sorted.length; i++) {
            System.out.print(quickResult.sorted[i]);
            if (i < quickResult.sorted.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Comparisons: " + quickResult.comparisons);
        
        // Merge morning and afternoon sessions
        System.out.println("\n--- Merge Two Sessions (Morning + Afternoon) ---");
        Trade[] morningTrades = {
            new Trade("m1", 100),
            new Trade("m2", 300)
        };
        Trade[] afternoonTrades = {
            new Trade("a1", 200),
            new Trade("a2", 400)
        };
        
        System.out.println("Morning: [m1:100, m2:300]");
        System.out.println("Afternoon: [a1:200, a2:400]");
        
        Trade[] merged = mergeTwoSortedLists(morningTrades, afternoonTrades);
        System.out.print("Merged (ascending): [");
        for (int i = 0; i < merged.length; i++) {
            System.out.print(merged[i]);
            if (i < merged.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        
        long totalVolume = computeTotalVolume(merged);
        System.out.println("Total Volume: " + totalVolume);
        
        // Larger dataset
        System.out.println("\n=== Larger Dataset Test ===");
        Trade[] largeTrades = {
            new Trade("t1", 1500),
            new Trade("t2", 500),
            new Trade("t3", 1000),
            new Trade("t4", 2000),
            new Trade("t5", 800)
        };
        
        System.out.println("Original: [1500, 500, 1000, 2000, 800]");
        MergeSortResult largeSort = mergeSortByVolumeAscending(largeTrades);
        System.out.print("Merge sorted: [");
        for (int i = 0; i < largeSort.sorted.length; i++) {
            System.out.print(largeSort.sorted[i].volume);
            if (i < largeSort.sorted.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Total: " + computeTotalVolume(largeSort.sorted));
    }
}
