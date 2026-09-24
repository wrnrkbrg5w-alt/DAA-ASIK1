import java.util.Arrays;
import java.util.Comparator;

public class ClosestPairSolver {
    public int maxRecursionDepth = 0;
    public long operations = 0;

    public double solve(Point[] points) {
        maxRecursionDepth = 0;
        operations = 0;
        if (points == null || points.length < 2) {
            return Double.POSITIVE_INFINITY;
        }

        Point[] pointsByX = Arrays.copyOf(points, points.length);
        Point[] pointsByY = Arrays.copyOf(points, points.length);


        Arrays.sort(pointsByX, Comparator.comparingDouble(p -> p.x));
        Arrays.sort(pointsByY, Comparator.comparingDouble(p -> p.y));

        return closestPair(pointsByX, pointsByY, 1);
    }

    private double closestPair(Point[] pointsByX, Point[] pointsByY, int depth) {
        maxRecursionDepth = Math.max(maxRecursionDepth, depth);
        int n = pointsByX.length;

        if (n <= 3) {
            return bruteForce(pointsByX);
        }

        int mid = n / 2;
        Point midPoint = pointsByX[mid];

        Point[] yLeft = new Point[mid];
        Point[] yRight = new Point[n - mid];
        int leftIndex = 0, rightIndex = 0;

        for (Point p : pointsByY) {
            operations++;
            if (p.x <= midPoint.x && leftIndex < mid) {
                yLeft[leftIndex++] = p;
            } else {
                yRight[rightIndex++] = p;
            }
        }
        double deltaLeft = closestPair(Arrays.copyOfRange(pointsByX, 0, mid), yLeft, depth + 1);
        double deltaRight = closestPair(Arrays.copyOfRange(pointsByX, mid, n), yRight, depth + 1);
        double delta = Math.min(deltaLeft, deltaRight);

        Point[] strip = new Point[n];
        int stripCount = 0;
        for (Point p : pointsByY) {
            operations++;
            if (Math.abs(p.x - midPoint.x) < delta) {
                strip[stripCount++] = p;
            }
        }

        for (int i = 0; i < stripCount; ++i) {
            for (int j = i + 1; j < stripCount && (strip[j].y - strip[i].y) < delta; ++j) {
                operations++;
                double dist = distance(strip[i], strip[j]);
                if (dist < delta) {
                    delta = dist;
                }
            }
        }

        return delta;
    }

    private double bruteForce(Point[] points) {
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.length; ++i) {
            for (int j = i + 1; j < points.length; ++j) {
                operations++;
                double dist = distance(points[i], points[j]);
                if (dist < minDistance) {
                    minDistance = dist;
                }
            }
        }
        return minDistance;
    }

    private double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}