package planteatingagent;

import java.util.*;
import java.io.*;

public class BoardExplorer {

    private static int BOARD_SIZE = 100;

    public static void main(String[] args) {
        List<Image> images = new ArrayList<Image>();
        PrintStream board_map_file = null;
        PrintStream image_classifications_file = null;
        char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                board[x][y] = 'X';
            }
        }

        try {
            String server = "localhost";
            int port = 2000;

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
                    Image image = agent.getPlantImage();
                    images.add(image);

                    switch (agent.eatPlant()) {
                        case EAT_NUTRITIOUS_PLANT:
                            board[x][y] = 'N';
                            image.setClassification(PlantType.NUTRITIOUS_PLANT);
                            break;

                        case EAT_POISONOUS_PLANT:
                            board[x][y] = 'P';
                            image.setClassification(PlantType.POISONOUS_PLANT);
                            images.add(image);
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
        } catch (Exception ex) {
            // System.err.println(ex.getMessage());
            // ex.printStackTrace(System.err);
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
