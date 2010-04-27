package planteatingagent;

public enum PlantType {

    NO_PLANT(0),
    UNKNOWN_SQUARE(1),
    UNKNOWN_PLANT(2),
    NUTRITIOUS_PLANT(3),
    POISONOUS_PLANT(4);
    int value;

    PlantType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
