package planteatingagent;

public enum Command {

    MOVE_UP(0),
    MOVE_LEFT(1),
    MOVE_DOWN(2),
    MOVE_RIGHT(3),
    EAT_PLANT(4),
    GET_PLANT_TYPE(5),
    GET_PLANT_IMAGE(6),
    GET_LIFE(7),
    GET_X(8),
    GET_Y(9),
    GET_ROUND(10),
    GET_STARTING_LIFE(11),
    GET_PLANT_BONUS(12),
    GET_PLANT_PENALTY(13),
    ALIVE(14),
    END_GAME(15);
    private int value;

    Command(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}