import java.util.Arrays;

public class DeterministicSelector {
    public int maxRecursionDepth = 0;
    public long comparisons = 0;

    public int select(int[] arr, int k) {
        maxRecursionDepth = 0;
        comparisons = 0;
        if (arr == null || arr.length == 0 || k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("Invalid input or k out of bounds");
        }
        return selectRecursive(arr, 0, arr.length - 1, k, 1);
    }

    private int selectRecursive(int[] arr, int left, int right, int k, int depth) {
        maxRecursionDepth = Math.max(maxRecursionDepth, depth);

        if (left == right) {
            return arr[left];
        }

        int pivotValue = medianOfMedians(arr, left, right);

        int pivotIndex = partition(arr, left, right, pivotValue);
        if (k == pivotIndex) {
            return arr[k];
        } else if (k < pivotIndex) {
            return selectRecursive(arr, left, pivotIndex - 1, k, depth + 1);
        } else {
            return selectRecursive(arr, pivotIndex + 1, right, k, depth + 1);
        }
    }

    private int medianOfMedians(int[] arr, int left, int right) {
        int n = right - left + 1;
        if (n <= 5) {
            return findMedian(arr, left, right);
        }

        int numGroups = (n + 4) / 5;
        int[] medians = new int[numGroups];

        for (int i = 0; i < numGroups; i++) {
            int groupLeft = left + i * 5;
            int groupRight = Math.min(left + i * 5 + 4, right);
            medians[i] = findMedian(arr, groupLeft, groupRight);
        }

        DeterministicSelector tempSelector = new DeterministicSelector();
        return tempSelector.selectRecursive(medians, 0, medians.length - 1, medians.length / 2, 1);
    }

    private int findMedian(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= left && arr[j] > key) {
                comparisons++;
                arr[j + 1] = arr[j];
                j--;
            }
            if (j >= left) comparisons++;
            arr[j + 1] = key;
        }
        return arr[left + (right - left) / 2];
    }

    private int partition(int[] arr, int left, int right, int pivotValue) {
        for (int i = left; i <= right; i++) {
            comparisons++;
            if (arr[i] == pivotValue) {
                swap(arr, i, right);
                break;
            }
        }

        int pivot = arr[right];
        int i = left;
        for (int j = left; j < right; j++) {
            comparisons++;
            if (arr[j] <= pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, right);
        return i;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}