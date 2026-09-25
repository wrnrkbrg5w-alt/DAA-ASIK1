import java.util.Arrays;
import java.util.Random;

public class AlgorithmTest {
    private static final Random RAND = new Random();

    public static void main(String[] args) {
        System.out.println("ЗАПУСК ТЕСТОВ КОРРЕКТНОСТИ");
        testSorting();
        testSelect();
        testClosestPair();
        System.out.println("ВСЕ ТЕСТЫ УСПЕШНО ПРОЙДЕНЫ!");
    }

    public static void testSorting() {
        MergeSorter mergeSorter = new MergeSorter();
        QuickSorter quickSorter = new QuickSorter();
        int[][] testCases = {
                {},
                {42},
                {5, 2, 8, 1, 9, 3},
                {1, 2, 3, 4, 5},
                {5, 4, 3, 2, 1},
                {2, 2, 2, 2, 2}
        };

        for (int[] original : testCases) {
            int[] expected = original.clone();
            Arrays.sort(expected);

            int[] mergeArr = original.clone();
            mergeSorter.sort(mergeArr);
            if (!Arrays.equals(mergeArr, expected)) {
                throw new RuntimeException("Ошибка в MergeSort!");
            }

            int[] quickArr = original.clone();
            quickSorter.sort(quickArr);
            if (!Arrays.equals(quickArr, expected)) {
                throw new RuntimeException("Ошибка в QuickSort!");
            }
        }
        System.out.println("[OK] Тесты MergeSort и QuickSort пройдены.");
    }

    public static void testSelect() {
        DeterministicSelector selector = new DeterministicSelector();
        for (int i = 0; i < 100; i++) {
            int n = 1 + RAND.nextInt(500);
            int[] arr = RAND.ints(n, -1000, 1000).toArray();
            int k = RAND.nextInt(n);

            int[] sorted = arr.clone();
            Arrays.sort(sorted);
            int expected = sorted[k];

            int actual = selector.select(arr.clone(), k);
            if (actual != expected) {
                throw new RuntimeException("Ошибка в Deterministic Select!");
            }
        }
        System.out.println("[OK] 100 тестов Deterministic Select пройдены.");
    }

    public static void testClosestPair() {
        ClosestPairSolver solver = new ClosestPairSolver();
        for (int t = 0; t < 20; t++) {
            int n = 10 + RAND.nextInt(100);
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                points[i] = new Point(RAND.nextDouble() * 500, RAND.nextDouble() * 500);
            }

            double expected = bruteForceClosestPair(points);
            double actual = solver.solve(points);

            if (Math.abs(expected - actual) > 1e-6) {
                throw new RuntimeException("Ошибка в Closest Pair!");
            }
        }
        System.out.println("[OK] Тесты Closest Pair (сравнение с brute-force O(n^2)) пройдены.");
    }

    private static double bruteForceClosestPair(Point[] pts) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double dx = pts[i].x - pts[j].x;
                double dy = pts[i].y - pts[j].y;
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist < min) min = dist;
            }
        }
        return min;
    }
}