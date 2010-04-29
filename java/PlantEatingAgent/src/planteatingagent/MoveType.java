package planteatingagent;

public enum MoveType {
    MOVE_UP(0),
    MOVE_DOWN(1),
    MOVE_LEFT(2),
    MOVE_RIGHT(3);
    int value;

    MoveType(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }
}