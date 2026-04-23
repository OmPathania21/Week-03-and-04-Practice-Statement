import java.util.*;

/**
 * Problem 4: Portfolio Return Sorting
 * Implements Merge Sort (preserves stability) and Quick Sort with multiple pivot strategies
 * Handles composite sorting criteria (return + volatility)
 */
public class Problem4_PortfolioReturnSorting {
    
    static class Asset {
        String ticker;
        double returnRate;
        double volatility;
        
        Asset(String ticker, double returnRate, double volatility) {
            this.ticker = ticker;
            this.returnRate = returnRate;
            this.volatility = volatility;
        }
        
        @Override
        public String toString() {
            return String.format("%s:%.1f%%", ticker, returnRate);
        }
    }
    
    /**
     * Merge Sort: Preserves original order for equal returns (stable)
     * Sorts by return rate ascending
     */
    static class MergeSortResult {
        Asset[] sorted;
        int comparisons;
        
        MergeSortResult(Asset[] sorted, int comparisons) {
            this.sorted = sorted;
            this.comparisons = comparisons;
        }
    }
    
    static class MergeSortHelper {
        int comparisons = 0;
        
        Asset[] mergeSort(Asset[] arr) {
            if (arr.length <= 1) return arr;
            Asset[] temp = new Asset[arr.length];
            mergeSortHelper(arr, 0, arr.length - 1, temp);
            return arr;
        }
        
        private void mergeSortHelper(Asset[] arr, int left, int right, Asset[] temp) {
            if (left < right) {
                int mid = left + (right - left) / 2;
                mergeSortHelper(arr, left, mid, temp);
                mergeSortHelper(arr, mid + 1, right, temp);
                merge(arr, left, mid, right, temp);
            }
        }
        
        private void merge(Asset[] arr, int left, int mid, int right, Asset[] temp) {
            int i = left;
            int j = mid + 1;
            int k = left;
            
            while (i <= mid && j <= right) {
                comparisons++;
                if (arr[i].returnRate <= arr[j].returnRate) {
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
    
    static MergeSortResult mergeSortByReturn(Asset[] assets) {
        MergeSortHelper helper = new MergeSortHelper();
        Asset[] result = assets.clone();
        helper.mergeSort(result);
        return new MergeSortResult(result, helper.comparisons);
    }
    
    /**
     * Quick Sort: Descending return + ascending volatility for ties
     * Multiple pivot strategies: Random, Median-of-3, and Middle
     */
    static class QuickSortResult {
        Asset[] sorted;
        int comparisons;
        String pivotStrategy;
        
        QuickSortResult(Asset[] sorted, int comparisons, String strategy) {
            this.sorted = sorted;
            this.comparisons = comparisons;
            this.pivotStrategy = strategy;
        }
    }
    
    static class QuickSortHelper {
        int comparisons = 0;
        String pivotStrategy;
        Random rand = new Random();
        
        QuickSortHelper(String strategy) {
            this.pivotStrategy = strategy;
        }
        
        void quickSort(Asset[] arr) {
            if (arr.length <= 1) return;
            quickSortHelper(arr, 0, arr.length - 1);
        }
        
        private void quickSortHelper(Asset[] arr, int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                quickSortHelper(arr, low, pi - 1);
                quickSortHelper(arr, pi + 1, high);
            }
        }
        
        private int partition(Asset[] arr, int low, int high) {
            int pivotIndex = selectPivot(arr, low, high);
            Asset pivot = arr[pivotIndex];
            swap(arr, pivotIndex, high);
            
            int i = low - 1;
            for (int j = low; j < high; j++) {
                comparisons++;
                if (compareAssetsDesc(arr[j], pivot) < 0) {
                    i++;
                    swap(arr, i, j);
                }
            }
            swap(arr, i + 1, high);
            return i + 1;
        }
        
        private int selectPivot(Asset[] arr, int low, int high) {
            if (pivotStrategy.equals("RANDOM")) {
                return low + rand.nextInt(high - low + 1);
            } else if (pivotStrategy.equals("MEDIAN_OF_3")) {
                int mid = low + (high - low) / 2;
                return medianOf3Index(arr, low, mid, high);
            } else {
                // MIDDLE
                return low + (high - low) / 2;
            }
        }
        
        private int medianOf3Index(Asset[] arr, int i, int j, int k) {
            if (arr[i].returnRate <= arr[j].returnRate && arr[j].returnRate <= arr[k].returnRate) return j;
            if (arr[k].returnRate <= arr[j].returnRate && arr[j].returnRate <= arr[i].returnRate) return j;
            if (arr[j].returnRate <= arr[i].returnRate && arr[i].returnRate <= arr[k].returnRate) return i;
            if (arr[k].returnRate <= arr[i].returnRate && arr[i].returnRate <= arr[j].returnRate) return i;
            return k;
        }
        
        private int compareAssetsDesc(Asset a1, Asset a2) {
            // Return descending
            if (a1.returnRate != a2.returnRate) {
                return Double.compare(a2.returnRate, a1.returnRate);
            }
            // Volatility ascending for ties
            return Double.compare(a1.volatility, a2.volatility);
        }
        
        private void swap(Asset[] arr, int i, int j) {
            Asset temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
    
    static QuickSortResult quickSortByReturnDesc(Asset[] assets, String pivotStrategy) {
        QuickSortHelper helper = new QuickSortHelper(pivotStrategy);
        Asset[] result = assets.clone();
        helper.quickSort(result);
        return new QuickSortResult(result, helper.comparisons, pivotStrategy);
    }
    
    public static void main(String[] args) {
        // Sample assets
        Asset[] assets = {
            new Asset("AAPL", 12.0, 5.0),
            new Asset("TSLA", 8.0, 15.0),
            new Asset("GOOG", 15.0, 3.0)
        };
        
        System.out.println("=== Problem 4: Portfolio Return Sorting ===\n");
        System.out.println("Input assets:");
        for (Asset a : assets) {
            System.out.println("  " + a + " (vol:" + a.volatility + "%)");
        }
        
        // Merge Sort - Ascending (stable)
        System.out.println("\n--- Merge Sort (Return Ascending - Stable) ---");
        MergeSortResult mergeResult = mergeSortByReturn(assets);
        System.out.print("Sorted: [");
        for (int i = 0; i < mergeResult.sorted.length; i++) {
            System.out.print(mergeResult.sorted[i]);
            if (i < mergeResult.sorted.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Comparisons: " + mergeResult.comparisons);
        
        // Quick Sort - Descending with different pivot strategies
        System.out.println("\n--- Quick Sort (Return Descending + Volatility Ascending) ---");
        
        String[] strategies = {"RANDOM", "MEDIAN_OF_3", "MIDDLE"};
        for (String strategy : strategies) {
            System.out.println("\nPivot Strategy: " + strategy);
            QuickSortResult quickResult = quickSortByReturnDesc(assets, strategy);
            System.out.print("Sorted: [");
            for (int i = 0; i < quickResult.sorted.length; i++) {
                System.out.print(quickResult.sorted[i]);
                if (i < quickResult.sorted.length - 1) System.out.print(", ");
            }
            System.out.println("]");
            System.out.println("Comparisons: " + quickResult.comparisons);
        }
        
        // Larger dataset with stability test
        System.out.println("\n=== Stability and Tie-Breaking Test ===");
        Asset[] tieTrades = {
            new Asset("A", 10.0, 5.0),
            new Asset("B", 10.0, 3.0),
            new Asset("C", 8.0, 2.0)
        };
        
        System.out.println("\nOriginal: [A:10%(vol:5%), B:10%(vol:3%), C:8%(vol:2%)]");
        MergeSortResult stableSort = mergeSortByReturn(tieTrades);
        System.out.print("Merge sorted (asc): [");
        for (int i = 0; i < stableSort.sorted.length; i++) {
            System.out.print(stableSort.sorted[i] + "(vol:" + stableSort.sorted[i].volatility + "%)");
            if (i < stableSort.sorted.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Note: Merge sort is stable - A:10% comes before B:10%");
        
        QuickSortResult descSort = quickSortByReturnDesc(tieTrades, "MEDIAN_OF_3");
        System.out.print("Quick sorted (desc): [");
        for (int i = 0; i < descSort.sorted.length; i++) {
            System.out.print(descSort.sorted[i] + "(vol:" + descSort.sorted[i].volatility + "%)");
            if (i < descSort.sorted.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Note: For ties at 10%, lower volatility comes first (secondary sort)");
    }
}
