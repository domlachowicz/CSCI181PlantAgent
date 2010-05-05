package planteatingagent;

public class Point implements Comparable<Point> {

    int x;
    int y;
    PlantType type;

    public Point() {
        this(0, 0, PlantType.UNKNOWN_SQUARE);
    }

	public Point(int x, int y) {
		this(x, y, PlantType.UNKNOWN_SQUARE);
	}

    public Point(int x, int y, PlantType type) {
        setX(x);
        setY(y);
        setType(type);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setType(PlantType type) {
        this.type = type;
    }

    public PlantType getType() {
        return type;
    }

    public int compareTo(Point other) {
        int diff = this.y - other.y;
        if (diff != 0) {
            return diff;
        } else {
            return this.x - other.x;
        }
    }

	@Override
	public boolean equals(Object other) {
		if (other == this) return true;
		if (other instanceof Point) {
			return compareTo((Point)other) == 0;
		}

		throw new IllegalArgumentException();
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 11 * hash + this.x;
		hash = 11 * hash + this.y;
		return hash;
	}
}
