package planteatingagent;

public enum PlantEatingResult {
  NO_PLANT_TO_EAT(0),
  PLANT_ALREADY_EATEN(1),
  EAT_NUTRITIOUS_PLANT(2),
  EAT_POISONOUS_PLANT(3);
  int value;

  PlantEatingResult(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}