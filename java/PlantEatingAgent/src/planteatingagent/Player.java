package planteatingagent;

import java.io.*;

public class Player {

    private static void print_instructions(PrintStream out) {
        out.println("Use i, j, k, l to move");
        out.println("Type e to eat a plant");
        out.println("Type o to observe a plant");
        out.println("Type exit to end the game");
        out.println("Type ? or help to view these intructions");
        out.println();
    }

    private static void print_plant_image(Agent agent, PrintStream out) throws Exception {
        Image image = agent.getPlantImage();

        out.println("Plant image:");
        out.println(image.toString());
    }

    public static void main(String[] args) {
        try {
            PrintStream out = System.out;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            String server = "localhost";
            int port = 2000;

            out.println();
            out.println();
            out.println("--------------------------");
            out.println("Welcome to the CS 181 game");
            out.println("--------------------------");
            out.println();

            Agent agent = new Agent(server, port);

            // Print status messages
            out.format("Connected to game server %s at port %d", server, port).println();
            out.println();

            // Print life settings
            out.format("Starting Life: %d", agent.getStartingLife()).println();
            out.format("Plant Bonus: %d", agent.getPlantBonus()).println();
            out.format("Plant Penalty: %d", agent.getPlantPenalty()).println();
            out.println();

            // Print instructions
            print_instructions(out);

            // Loop as long as the player is alive
            while (agent.isAlive()) {
                // Print the current round
                out.format("Round: %d", agent.getRound()).println();
                out.println();

                // Print game info
                out.format("Player is at (%d, %d) with life %d",
                        agent.getX(), agent.getY(), agent.getLife()).println();

                switch (agent.getPlantType()) {
                    case NO_PLANT:
                        out.println("No plant at in this location");
                        break;

                    case UNKNOWN_PLANT:
                        out.println("Plant at this location has not been eaten");
                        out.println();
                        break;

                    case NUTRITIOUS_PLANT:
                        out.println("Plant at this location has been eaten");
                        out.println("Plant was nutritious");
                        out.println();
                        break;

                    case POISONOUS_PLANT:
                        out.println("Plant at this location has been eaten");
                        out.println("Plant was poisonous");
                        out.println();
                        break;
                }

                out.println();

                // Get the next move
                out.print("Enter a move: ");
                out.flush();
                String input = in.readLine();

                out.println();

                // Handle user input
                if (input.isEmpty() || "exit".equals(input)) {
                    agent.endGame();
                    break;
                } else if ("i".equals(input)) {
                    agent.moveUp();
                    out.println("Moved UP");

                } else if ("j".equals(input)) {
                    agent.moveLeft();
                    out.println("Moved LEFT");
                } else if ("k".equals(input)) {
                    agent.moveDown();
                    out.println("Moved DOWN");
                } else if ("l".equals(input)) {
                    agent.moveRight();
                    out.println("Moved RIGHT");
                } else if ("e".equals(input)) {
                    switch (agent.eatPlant()) {
                        case NO_PLANT_TO_EAT:
                            out.println("No plant to eat");
                            break;

                        case PLANT_ALREADY_EATEN:
                            out.println("Plant already eaten");
                            break;

                        case EAT_NUTRITIOUS_PLANT:
                            out.println("Ate a nutritious plant!");
                            break;

                        case EAT_POISONOUS_PLANT:
                            out.println("Ate a poisonous plant!");
                            break;
                    }
                } else if ("o".equals(input)) {
                    switch (agent.getPlantType()) {
                        case NO_PLANT:
                            out.println("No plant to observe");
                            break;

                        default:
                            print_plant_image(agent, out);
                            break;
                    }
                } else if ("?".equals(input) || "help".equals(input)) {
                    print_instructions(out);
                } else {
                    out.format("Invalid input: %s", input).println();
                }

                out.println();
            }

            // Tell the server to end the game
            agent.endGame();

            // Print a message for the user
            out.println("Player is dead: GAME OVER!");
            out.println();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }
}
