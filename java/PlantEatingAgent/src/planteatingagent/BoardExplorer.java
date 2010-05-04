package planteatingagent;

import java.util.*;
import java.io.*;

public class BoardExplorer {
	private static int MAX_ATTEMPTS = 10;

	private static interface Policy {
		public PlantType classify(Agent agent, Classifier classifier) throws Exception;
	}

	private static class FiniteMemoryPolicy implements Policy {
		public PlantType classify(Agent agent, Classifier classifier) throws Exception {
			PlantType lastType = PlantType.UNKNOWN_PLANT;
			for (int attempts = 0; attempts <= MAX_ATTEMPTS; attempts++) {
				Image image = agent.getPlantImage();
				PlantType type = classifier.classifyInstance(image.toInstance(classifier.getDataSet()));
				if (type.equals(lastType)) {
					return type;
				}
				lastType = type;
			}
			return PlantType.UNKNOWN_PLANT;
		}
	}

	private static class FiniteStateControllerPolicy implements Policy {
		public PlantType classify(Agent agent, Classifier classifier) throws Exception {
			int sum = 0;
			for (int attempts = 0; attempts <= MAX_ATTEMPTS; attempts++) {
				Image image = agent.getPlantImage();
				PlantType type = classifier.classifyInstance(image.toInstance(classifier.getDataSet()));
				if (type.equals(PlantType.POISONOUS_PLANT))
					sum--;
				else if (type.equals(PlantType.NUTRITIOUS_PLANT))
					sum++;

				if (Math.abs(sum) == 2) return type;
			}
			return PlantType.UNKNOWN_PLANT;
		}
	}

    private static int BOARD_SIZE = 1000;

    public static void main(String[] args) {
		Policy policy = new FiniteStateControllerPolicy();
		int port = 2000;
		boolean explore = false;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
			if (args.length > 1) {
				if (args[1].equals("-explore")) {
					explore = true;
				}
			}
		}
		if (explore) {
			doExplore(port);
		} else {
			Classifier classifier = new Classifier();
			classifier.readFromDisk();

			try {
				String server = "localhost";

				Agent agent = new Agent(server, port);
				Random r = new Random();

				int my_key = r.nextInt();

				int radius = 1;
				int left, right, down, up;

				left = right = down = up = radius;

				while (agent.isAlive()) {
					int x = agent.getX() + (BOARD_SIZE / 2);
					int y = agent.getY() + (BOARD_SIZE / 2);

					PlantType plantType = agent.getPlantType();
					if (PlantType.UNKNOWN_PLANT == plantType) {
						PlantType classifiedObservation = policy.classify(agent, classifier);
						if (classifiedObservation == PlantType.NUTRITIOUS_PLANT) {
							PlantEatingResult eatenPlant = agent.eatPlant();
							switch (eatenPlant) {
								case EAT_NUTRITIOUS_PLANT:
									System.out.println("GOOD - classified plant as nutritious and was nutrious.");
									break;

								case EAT_POISONOUS_PLANT:
									System.out.println("BAD - classified plant as nutritious and was poisonous.");
									break;
							}
						}
					}

					// attempt to move in concentric circles
					if (left > 0) {
						agent.moveLeft();
						left--;
					} else if (down > 0) {
						agent.moveDown();
						down--;
					} else if (right > 0) {
						agent.moveRight();
						right--;
					} else if (up > 0) {
						agent.moveUp();
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
    }

	public static void doExplore(int port) {
		List<Image> images = new ArrayList<Image>();
		PrintStream board_map_file = null;
		PrintStream image_classifications_file = null;
		char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
		for (int x = 0; x < BOARD_SIZE; x++) {
			for (int y = 0; y < BOARD_SIZE; y++) {
				board[x][y] = 'X';
			}
		}
		Classifier classifier = new Classifier();
		classifier.readFromDisk();

		try {
			String server = "localhost";

			Agent agent = new Agent(server, port);
			Random r = new Random();

			int my_key = r.nextInt();

			board_map_file = new PrintStream(new FileOutputStream(String.format("board_map_%d.txt", my_key)));
			image_classifications_file = new PrintStream(new FileOutputStream(String.format("image_classifications_%d.arff", my_key)));
			int radius = 1;
			int left, right, down, up;

			left = right = down = up = radius;

			while (agent.isAlive()) {
				int x = agent.getX() + (BOARD_SIZE / 2);
				int y = agent.getY() + (BOARD_SIZE / 2);

				PlantType plantType = agent.getPlantType();
				if (PlantType.UNKNOWN_PLANT == plantType) {
					Image image1 = agent.getPlantImage();
					PlantType type1 = classifier.classifyInstance(image1.toInstance(classifier.getDataSet()));
					Image image2 = agent.getPlantImage();
					PlantType type2 = classifier.classifyInstance(image2.toInstance(classifier.getDataSet()));
					/*int[][] jointImage = new int[Image.IMAGE_SIZE][Image.IMAGE_SIZE];
					for (int i = 0; i < Image.IMAGE_SIZE; i++) {
						for (int j = 0; j < Image.IMAGE_SIZE; j++) {
							jointImage[i][j] = image1.getImage()[i][j] | image2.getImage()[i][j];
						}
					}
					Image image = new Image(jointImage);
					images.add(image1);*/
					PlantEatingResult eatenPlant = agent.eatPlant();
					switch (eatenPlant) {
						case EAT_NUTRITIOUS_PLANT:
							image1.setClassification(PlantType.NUTRITIOUS_PLANT);
							System.out.println("GOOD - classified plant as nutritious and was nutrious.");
							break;

						case EAT_POISONOUS_PLANT:
							image1.setClassification(PlantType.POISONOUS_PLANT);
							System.out.println("BAD - classified plant as nutritious and was poisonous.");
							break;
					}
				} else if (PlantType.POISONOUS_PLANT == plantType) {
					board[x][y] = 'P';
				} else if (PlantType.NUTRITIOUS_PLANT == plantType) {
					board[x][y] = 'N';
				} else {
					board[x][y] = ' ';
				}

				// attempt to move in concentric circles
				if (left > 0) {
					agent.moveLeft();
					left--;
				} else if (down > 0) {
					agent.moveDown();
					down--;
				} else if (right > 0) {
					agent.moveRight();
					right--;
				} else if (up > 0) {
					agent.moveUp();
					up--;
				} else {
					radius++;
					left = right = down = up = radius;
				}
			}
		} catch (Throwable t) {
			System.err.println(t.getMessage());
			t.printStackTrace(System.err);
		} finally {
			ImageArffWriter.writeArffFile(images, image_classifications_file);

			image_classifications_file.close();

			for (int x = 0; x < BOARD_SIZE; x++) {
				for (int y = 0; y < BOARD_SIZE; y++) {
					board_map_file.append(board[x][y]);
				}
				board_map_file.println();
			}

			board_map_file.close();
		}
	}
}
