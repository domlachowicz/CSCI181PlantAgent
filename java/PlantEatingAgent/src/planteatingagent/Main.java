package planteatingagent;

class Main {

    public static void main(String[] args) {
		boolean train = false;
		boolean evaluate = false;
		for (String arg : args) {
			if (arg.equalsIgnoreCase("-train")) {
				train = true;
			} else if (arg.equalsIgnoreCase("-evaluate")) {
				evaluate = true;
			}
		}
		if (train || evaluate) {
			Classifier c = new Classifier();
			c.createNewClassifier();
			if (train) {
				trainAndSaveClassifierToDisk(c);
			} else {
				evaluateClassifier(c);
			}
		} else {
	        BoardExplorer.main(args);
		}
    }

	public static void trainAndSaveClassifierToDisk(Classifier c) {
		c.train();
		c.saveToDisk();
	}

	public static void evaluateClassifier(Classifier c) {
		c.runCrossValidation();
	}
}
