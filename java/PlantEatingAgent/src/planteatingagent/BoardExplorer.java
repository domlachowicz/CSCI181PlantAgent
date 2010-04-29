package planteatingagent;

import java.util.*;
import java.io.*;

public class BoardExplorer {

    public static void main(String[] args) {
        List<Image> images = new ArrayList<Image>();
        PrintStream board_map_file = null;
        PrintStream image_classifications_file = null;

        try {
            String server = "localhost";
            int port = 2000;

            Agent agent = new Agent(server, port);

            board_map_file = new PrintStream(new FileOutputStream("board_map.txt"));
            image_classifications_file = new PrintStream(new FileOutputStream("image_classifications.arff"));

            while (agent.isAlive()) {
                int move = (int) Math.floor(6 * Math.random());

                if (0 == move) {
                    agent.moveUp();
                } else if (1 == move || 4 == move) {
                    agent.moveDown();
                } else if (2 == move) {
                    agent.moveLeft();
                } else if (3 == move || 5 == move) {
                    agent.moveRight();
                }

                PlantType plantType = agent.getPlantType();
                //if (!(PlantType.UNKNOWN_SQUARE == plantType || PlantType.NO_PLANT == plantType)) {
                if (PlantType.UNKNOWN_PLANT == plantType) {
                    Image image = agent.getPlantImage();
                    images.add(image);

                    if (PlantType.UNKNOWN_PLANT == plantType) {
                        switch (agent.eatPlant()) {
                            case EAT_NUTRITIOUS_PLANT:
                                image.setClassification(PlantType.NUTRITIOUS_PLANT);
                                break;

                            case EAT_POISONOUS_PLANT:
                                image.setClassification(PlantType.POISONOUS_PLANT);
                                images.add(image);
                                break;
                        }
                    } else {
                        // either PlantType.NUTRITIOUS_PLANT or PlantType.POISONOUS_PLANT
                        // someone else already ate it. add to our classifications
                        image.setClassification(plantType);
                    }
                }
            }
        } catch (Exception ex) {
            // System.err.println(ex.getMessage());
            // ex.printStackTrace(System.err);
        } finally {
            ImageArffWriter.writeArffFile(images, image_classifications_file);

            image_classifications_file.close();
            board_map_file.close();
        }
    }
}
