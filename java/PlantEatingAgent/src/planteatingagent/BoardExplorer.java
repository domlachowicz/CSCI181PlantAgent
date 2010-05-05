package planteatingagent;

import java.util.*;
import java.io.*;
import weka.core.Instance;

public class BoardExplorer {
	private static int MAX_ATTEMPTS = 10;

	private static interface Policy {
		public PlantType classify(Agent agent, Classifier classifier) throws Exception;
	}

	private static abstract class AbstractPolicy implements Policy {
		public PlantType classify(Agent agent, Classifier classifier, Image image) throws Exception {
			if (true) {
				double utility = BoardExplorer.expectedUtility(agent, classifier, image.toInstance(classifier.getDataSet()));
				return getPlantTypeBasedOnUtility(utility);
			} else {
				return classifier.classifyInstance(image.toInstance(classifier.getDataSet()));
			}
		}
	}

	private static class FiniteMemoryPolicy extends AbstractPolicy {
		public PlantType classify(Agent agent, Classifier classifier) throws Exception {
			PlantType lastType = PlantType.UNKNOWN_PLANT;
			for (int attempts = 0; attempts <= MAX_ATTEMPTS; attempts++) {
				Image image = agent.getPlantImage();
				PlantType type = classify(agent, classifier, image);
				if (type.equals(lastType)) {
					return type;
				}
				lastType = type;
			}
			return PlantType.UNKNOWN_PLANT;
		}
	}

	private static class FiniteStateControllerPolicy extends AbstractPolicy {
		public PlantType classify(Agent agent, Classifier classifier) throws Exception {
			int sum = 0;
			for (int attempts = 0; attempts <= MAX_ATTEMPTS; attempts++) {
				Image image = agent.getPlantImage();
				PlantType type = classify(agent, classifier, image);
				if (type.equals(PlantType.POISONOUS_PLANT))
					sum--;
				else if (type.equals(PlantType.NUTRITIOUS_PLANT))
					sum++;

				if (Math.abs(sum) == 2) return type;
			}
			return PlantType.UNKNOWN_PLANT;
		}
	}

	private static class SimplePolicy extends AbstractPolicy {
		public PlantType classify(Agent agent, Classifier classifier) throws Exception {
			return classify(agent, classifier, agent.getPlantImage());
		}
	}

	private static class NonSamplingPolicyNutritious implements Policy {

		public PlantType classify(Agent agent, Classifier classifier) throws Exception {
			return PlantType.NUTRITIOUS_PLANT;
		}

	}

	private static class NonSamplingPolicyPoisonous implements Policy {

		public PlantType classify(Agent agent, Classifier classifier) throws Exception {
			return PlantType.POISONOUS_PLANT;
		}

	}

	private static class NonSamplingPolicy implements Policy {

		public PlantType classify(Agent agent, Classifier classifier) throws Exception {
			double positiveUtility = classifier.getPriorNutritious()*agent.getPlantBonus();
			double negativeUtility = classifier.getPriorPoisonous()*agent.getPlantPenalty();
			return getPlantTypeBasedOnUtility(positiveUtility-negativeUtility);
		}
	}

    private static int BOARD_SIZE = 1000;

    public static void main(String[] args) {
		Policy policy = new SimplePolicy();
		List<Point> pointsVisited = new ArrayList<Point>();
		List<MoveType> moves = new ArrayList<MoveType>();
		int port = 2000;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		Classifier classifier = new Classifier();
		classifier.readFromDisk();

		try {
			String server = "localhost";

			Agent agent = new Agent(server, port);
			int radius = 1;
			int left, right, down, up;

			left = right = down = up = radius;
			PlantType lastSquare = PlantType.UNKNOWN_SQUARE;

			while (agent.isAlive()) {
				PlantType plantType = agent.getPlantType();
				Point point = new Point(agent.getX(), agent.getY(), plantType);
				pointsVisited.add(point);
				if (PlantType.UNKNOWN_PLANT == plantType) {
					Policy tempPolicy = new NonSamplingPolicy();
					if (tempPolicy.classify(agent, classifier).equals(PlantType.NUTRITIOUS_PLANT)) {
						policy = tempPolicy;
					} else if (lastSquare.equals(PlantType.NUTRITIOUS_PLANT)) {
						policy = new NonSamplingPolicyNutritious();
					} else if (lastSquare.equals(PlantType.POISONOUS_PLANT)) {
						policy = new NonSamplingPolicyPoisonous();
					} else {
						policy = new SimplePolicy();
					}
					PlantType classifiedObservation = policy.classify(agent, classifier);
					if (classifiedObservation == PlantType.NUTRITIOUS_PLANT) {
						PlantEatingResult eatenPlant = agent.eatPlant();
						switch (eatenPlant) {
							case EAT_NUTRITIOUS_PLANT:
								lastSquare = PlantType.NUTRITIOUS_PLANT;
								point.setType(lastSquare);
								break;

							case EAT_POISONOUS_PLANT:
								lastSquare = PlantType.POISONOUS_PLANT;
								point.setType(lastSquare);
								break;
						}
					} else {
						lastSquare = PlantType.POISONOUS_PLANT;
					}
				} else {
					lastSquare = plantType;
				}
				// attempt to move in concentric circles
				if (left > 0) {
					agent.moveLeft();
					moves.add(MoveType.MOVE_LEFT);
					left--;
				} else if (down > 0) {
					agent.moveDown();
					moves.add(MoveType.MOVE_DOWN);
					down--;
				} else if (right > 0) {
					agent.moveRight();
					moves.add(MoveType.MOVE_RIGHT);
					right--;
				} else if (up > 0) {
					agent.moveUp();
					moves.add(MoveType.MOVE_UP);
					up--;
				} else {
					radius++;
					left = right = down = up = radius;
				}
			}
		} catch (Throwable t) {
			System.err.println(t.getMessage());
			t.printStackTrace(System.err);
		}
    }

	public static double expectedUtility(Agent agent, Classifier classifier, Instance i) throws Exception {
		double positiveUtility = agent.getPlantBonus()*classifier.getDistribution(i, PlantType.NUTRITIOUS_PLANT);
		double negativeUtility = agent.getPlantPenalty()*classifier.getDistribution(i, PlantType.POISONOUS_PLANT);
		return positiveUtility - negativeUtility;
	}

	public static PlantType getPlantTypeBasedOnUtility(double utility) {
		if (utility > 0) {
			return PlantType.NUTRITIOUS_PLANT;
		} else {
			return PlantType.POISONOUS_PLANT;
		}
	}

}
