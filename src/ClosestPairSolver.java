import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class ClosestPairSolver {
    public int maxRecursionDepth = 0;
    public long operations = 0;

    public double solve(Point[] points) {
        maxRecursionDepth = 0;
        operations = 0;
        if (points == null || points.length < 2) {
            return Double.POSITIVE_INFINITY;
        }

        Point[] byX = points.clone();
        Point[] byY = points.clone();
        Arrays.sort(byX, Comparator.comparingDouble(p -> p.x));
        Arrays.sort(byY, Comparator.comparingDouble(p -> p.y));

        return closestPair(byX, byY, 1);
    }

    private double closestPair(Point[] byX, Point[] byY, int depth) {
        maxRecursionDepth = Math.max(maxRecursionDepth, depth);
        int n = byX.length;

        if (n <= 3) {
            return bruteForce(byX);
        }

        int mid = n / 2;
        Point midPoint = byX[mid];

        Point[] xLeft = Arrays.copyOfRange(byX, 0, mid);
        Point[] xRight = Arrays.copyOfRange(byX, mid, n);

        Set<Point> leftSet = new HashSet<>(Arrays.asList(xLeft));

        Point[] yLeft = new Point[mid];
        Point[] yRight = new Point[n - mid];
        int lIdx = 0, rIdx = 0;

        for (Point p : byY) {
            operations++;
            if (leftSet.contains(p)) {
                yLeft[lIdx++] = p;
            } else {
                yRight[rIdx++] = p;
            }
        }

        double dLeft = closestPair(xLeft, yLeft, depth + 1);
        double dRight = closestPair(xRight, yRight, depth + 1);
        double delta = Math.min(dLeft, dRight);

        Point[] strip = new Point[n];
        int stripCount = 0;
        for (Point p : byY) {
            operations++;
            if (Math.abs(p.x - midPoint.x) < delta) {
                strip[stripCount++] = p;
            }
        }

        for (int i = 0; i < stripCount; ++i) {
            for (int j = i + 1; j < stripCount && (strip[j].y - strip[i].y) < delta; ++j) {
                operations++;
                delta = Math.min(delta, distance(strip[i], strip[j]));
            }
        }

        return delta;
    }

    private double bruteForce(Point[] pts) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                operations++;
                min = Math.min(min, distance(pts[i], pts[j]));
            }
        }
        return min;
    }

    private double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}