package planteatingagent;

import java.util.*;

public class Board {

    Set<Point> pointsSeen;

    public Board() {
        pointsSeen = new TreeSet<Point>();
    }

    public void addPoint(Point point) {
        // replace previous points, if they exist
        // because the classification may have changed
        if (containsPoint(point)) {
            pointsSeen.remove(point);
        }
        pointsSeen.add(point);
    }

    public void addPoint(int x, int y, PlantType type) {
        addPoint(new Point(x, y, type));
    }

    public boolean containsPoint(int x, int y) {
        return containsPoint(new Point(x, y, PlantType.UNKNOWN_SQUARE));
    }

    public boolean containsPoint(Point point) {
        return pointsSeen.contains(point);
    }

    public Set<Point> getPoints() {
        return pointsSeen;
    }

    @Override
    public String toString() {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point point : pointsSeen) {
            int pointX = point.getX();
            int pointY = point.getY();

            if (pointX < minX) {
                minX = pointX;
            }

            if (pointX > maxX) {
                maxX = pointX;
            }

            if (pointY < minY) {
                minY = pointY;
            }

            if (pointY > maxY) {
                maxY = pointY;
            }
        }

        return ""; // TODO
    }
}
