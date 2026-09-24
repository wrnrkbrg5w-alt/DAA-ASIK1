import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Experiment {
    private static final Random RAND = new Random();

    public static void main(String[] args) {
        int[] sizes = {100, 2000, 10000, 50000};
        String[] types = {"Random", "Sorted", "Reverse-sorted", "Duplicate-heavy"};

        try (FileWriter writer = new FileWriter("results/results.csv")) {
            writer.write("Algorithm,InputSize,InputType,ExecutionTimeNs,MaxRecursionDepth,AdditionalMetric\n");

            for (int size : sizes) {
                for (String type : types) {
                    int[] arr = generateArray(size, type);

                    int[] mergeArr = Arrays.copyOf(arr, arr.length);
                    MergeSorter mergeSorter = new MergeSorter();
                    long start = System.nanoTime();
                    mergeSorter.sort(mergeArr);
                    long end = System.nanoTime();
                    writeResult(writer, "MergeSort", size, type, end - start, mergeSorter.maxRecursionDepth, mergeSorter.comparisons);

                    int[] quickArr = Arrays.copyOf(arr, arr.length);
                    QuickSorter quickSorter = new QuickSorter();
                    start = System.nanoTime();
                    quickSorter.sort(quickArr);
                    end = System.nanoTime();
                    writeResult(writer, "QuickSort", size, type, end - start, quickSorter.maxRecursionDepth, quickSorter.comparisons);

                    int[] selectArr = Arrays.copyOf(arr, arr.length);
                    DeterministicSelector selector = new DeterministicSelector();
                    start = System.nanoTime();
                    selector.select(selectArr, size / 2);
                    end = System.nanoTime();
                    writeResult(writer, "DeterministicSelect", size, type, end - start, selector.maxRecursionDepth, selector.comparisons);


                    Point[] points = generatePoints(size, type);
                    ClosestPairSolver closestPair = new ClosestPairSolver();
                    start = System.nanoTime();
                    closestPair.solve(points);
                    end = System.nanoTime();
                    writeResult(writer, "ClosestPair", size, type, end - start, closestPair.maxRecursionDepth, closestPair.operations);
                }
            }
            System.out.println("Эксперименты завершены. Результаты сохранены в results/results.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] generateArray(int size, String type) {
        int[] arr = new int[size];
        switch (type) {
            case "Random":
                for (int i = 0; i < size; i++) arr[i] = RAND.nextInt(size);
                break;
            case "Sorted":
                for (int i = 0; i < size; i++) arr[i] = i;
                break;
            case "Reverse-sorted":
                for (int i = 0; i < size; i++) arr[i] = size - i;
                break;
            case "Duplicate-heavy":
                for (int i = 0; i < size; i++) arr[i] = RAND.nextInt(Math.max(1, size / 100)); // Много дубликатов[cite: 1]
                break;
        }
        return arr;
    }

    private static Point[] generatePoints(int size, String type) {
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            double x = RAND.nextDouble() * 1000;
            double y = RAND.nextDouble() * 1000;
            if (type.equals("Sorted")) {
                x = i; y = i;
            } else if (type.equals("Reverse-sorted")) {
                x = size - i; y = size - i;
            } else if (type.equals("Duplicate-heavy")) {
                x = RAND.nextInt(Math.max(1, size / 100));
                y = RAND.nextInt(Math.max(1, size / 100));
            }
            points[i] = new Point(x, y);
        }
        return points;
    }

    private static void writeResult(FileWriter writer, String algo, int size, String type, long time, int depth, long metric) throws IOException {
        writer.write(String.format("%s,%d,%s,%d,%d,%d\n", algo, size, type, time, depth, metric));
    }
}