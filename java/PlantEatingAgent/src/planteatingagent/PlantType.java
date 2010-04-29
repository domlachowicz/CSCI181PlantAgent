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

    @Override
    public String toString()  {
        switch (value) {
            case 0:
                return "no plant";
            case 1:
                return "unknown square";
            case 2:
                return "unknown plant";
            case 3:
                return "nutritious";
            case 4:
                return "poisonous";
            default:
                throw new IllegalArgumentException();
        }
    }
}
