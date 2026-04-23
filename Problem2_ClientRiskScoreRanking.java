import java.util.*;

/**
 * Problem 2: Client Risk Score Ranking
 * Implements Bubble Sort (ascending) and Insertion Sort (descending with balance)
 * for KYC risk prioritization
 */
public class Problem2_ClientRiskScoreRanking {
    
    static class Client {
        String name;
        int riskScore;
        double accountBalance;
        
        Client(String name, int riskScore, double accountBalance) {
            this.name = name;
            this.riskScore = riskScore;
            this.accountBalance = accountBalance;
        }
        
        @Override
        public String toString() {
            return String.format("%s:%d(%.2f)", name, riskScore, accountBalance);
        }
    }
    
    /**
     * Bubble Sort: Ascending risk score
     * Visualizes swaps for demonstration
     */
    static class BubbleSortResult {
        Client[] sorted;
        int swaps;
        
        BubbleSortResult(Client[] sorted, int swaps) {
            this.sorted = sorted;
            this.swaps = swaps;
        }
    }
    
    static BubbleSortResult bubbleSortByRiskAscending(Client[] clients) {
        Client[] arr = clients.clone();
        int n = arr.length;
        int swaps = 0;
        
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].riskScore > arr[j + 1].riskScore) {
                    // Swap
                    Client temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swaps++;
                    swapped = true;
                }
            }
            
            if (!swapped) break;
        }
        
        return new BubbleSortResult(arr, swaps);
    }
    
    /**
     * Insertion Sort: Descending risk score (primary), ascending account balance (secondary)
     * Stable sort preserves order for equal risk scores
     */
    static class InsertionSortResult {
        Client[] sorted;
        int shifts;
        
        InsertionSortResult(Client[] sorted, int shifts) {
            this.sorted = sorted;
            this.shifts = shifts;
        }
    }
    
    static InsertionSortResult insertionSortByRiskDescending(Client[] clients) {
        Client[] arr = clients.clone();
        int n = arr.length;
        int shifts = 0;
        
        for (int i = 1; i < n; i++) {
            Client key = arr[i];
            int j = i - 1;
            
            // Shift elements: descending risk, then ascending balance
            while (j >= 0 && compareClientsDesc(arr[j], key) > 0) {
                arr[j + 1] = arr[j];
                shifts++;
                j--;
            }
            arr[j + 1] = key;
        }
        
        return new InsertionSortResult(arr, shifts);
    }
    
    /**
     * Compare: risk DESC, balance ASC
     */
    static int compareClientsDesc(Client c1, Client c2) {
        // Risk descending
        if (c1.riskScore != c2.riskScore) {
            return Integer.compare(c2.riskScore, c1.riskScore);
        }
        // Balance ascending for ties
        return Double.compare(c1.accountBalance, c2.accountBalance);
    }
    
    /**
     * Identify top N highest risk clients
     */
    static List<Client> getTopRiskClients(Client[] sortedClients, int n) {
        List<Client> topRisks = new ArrayList<>();
        for (int i = 0; i < Math.min(n, sortedClients.length); i++) {
            topRisks.add(sortedClients[i]);
        }
        return topRisks;
    }
    
    public static void main(String[] args) {
        // Sample clients
        Client[] clients = {
            new Client("clientC", 80, 5000.0),
            new Client("clientA", 20, 100000.0),
            new Client("clientB", 50, 50000.0)
        };
        
        System.out.println("=== Problem 2: Client Risk Score Ranking ===\n");
        System.out.println("Input clients:");
        for (Client c : clients) {
            System.out.println("  " + c);
        }
        
        // Bubble Sort - Ascending
        System.out.println("\n--- Bubble Sort (Risk Score Ascending) ---");
        BubbleSortResult bubbleResult = bubbleSortByRiskAscending(clients);
        System.out.print("Sorted: [");
        for (int i = 0; i < bubbleResult.sorted.length; i++) {
            System.out.print(bubbleResult.sorted[i].name + ":" + bubbleResult.sorted[i].riskScore);
            if (i < bubbleResult.sorted.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Swaps: " + bubbleResult.swaps);
        
        // Insertion Sort - Descending
        System.out.println("\n--- Insertion Sort (Risk Score Descending + Balance Ascending) ---");
        InsertionSortResult insertionResult = insertionSortByRiskDescending(clients);
        System.out.print("Sorted: [");
        for (int i = 0; i < insertionResult.sorted.length; i++) {
            System.out.print(insertionResult.sorted[i].name + ":" + insertionResult.sorted[i].riskScore);
            if (i < insertionResult.sorted.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Shifts: " + insertionResult.shifts);
        
        // Top Risk Clients
        System.out.println("\n--- Top 3 Highest Risk Clients ---");
        List<Client> topRisks = getTopRiskClients(insertionResult.sorted, 3);
        for (int i = 0; i < topRisks.size(); i++) {
            Client c = topRisks.get(i);
            System.out.println("  " + (i + 1) + ". " + c.name + "(" + c.riskScore + ")");
        }
        
        // Larger test with ties
        System.out.println("\n=== Stability Test (Ties) ===");
        Client[] clientsWithTies = {
            new Client("X", 50, 10000.0),
            new Client("Y", 50, 5000.0),
            new Client("Z", 30, 20000.0)
        };
        
        System.out.println("\nOriginal: [X:50, Y:50, Z:30]");
        InsertionSortResult stableSort = insertionSortByRiskDescending(clientsWithTies);
        System.out.print("Sorted (desc): [");
        for (int i = 0; i < stableSort.sorted.length; i++) {
            System.out.print(stableSort.sorted[i].name + ":" + stableSort.sorted[i].riskScore);
            if (i < stableSort.sorted.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Note: Among tied risk scores, lower balance comes first (stable + secondary sort)");
    }
}
