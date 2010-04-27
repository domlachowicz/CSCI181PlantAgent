package planteatingagent;

class Main {
    public static void main(String[] args) {
        try {
            Agent agent = new Agent("localhost", 2000);
            int starting_score = agent.getStartingLife();
            int bonus = agent.getPlantBonus();
            int penalty = agent.getPlantPenalty();
            int round = agent.getRound();
            boolean alive = agent.isAlive();
            System.out.println();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }
}