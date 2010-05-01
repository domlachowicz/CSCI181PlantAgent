package planteatingagent;

import java.util.Random;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.Id3;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author sparks
 */
public class Classifier {

    private Instances data;
    private Bagging classifier;
    private static final String MODEL_FILE = "data/baggingModel.model";
    private static final String DATA_FILE = "data/data.arff";

    public Classifier() {
    }

    public void createNewClassifier() {
        try {
            classifier = new Bagging();
            classifier.setClassifier(new Id3());
            DataSource source = new DataSource(Classifier.class.getResourceAsStream(DATA_FILE));
            data = source.getDataSet();
            if (data.classIndex() == -1) {
                data.setClassIndex(data.numAttributes() - 1);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
        }

    }

    public Instances getDataSet() {
        return data;
    }

    public void train() {
        try {
            classifier.buildClassifier(data);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }

    public void addInstance(Instance i) {
        data.add(i);
//		classifier.updateClassifier();
    }

    public PlantType classifyInstance(Instance i) {
        double classLabel = 0;
        try {
            classLabel = classifier.classifyInstance(i);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
        }
        i.setClassValue(classLabel);

        if (i.classAttribute().value((int) classLabel).equals("poisonous")) {
            return PlantType.POISONOUS_PLANT;
        } else if (i.classAttribute().value((int) classLabel).equals("nutritious")) {
            return PlantType.NUTRITIOUS_PLANT;
        } else {
            return PlantType.UNKNOWN_PLANT;
        }
    }

    public void saveToDisk() {
        try {
            // serialize model
            SerializationHelper.write(MODEL_FILE, classifier);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }

    public void readFromDisk() {
        try {
            //deserialize model
            classifier = (Bagging) SerializationHelper.read(Classifier.class.getResourceAsStream(MODEL_FILE));
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }

    public void runCrossValidation() {
        try {
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(classifier, data, 10, new Random(1));
            System.out.println("Percent correct: " + eval.pctCorrect() + "; Percent incorrect: " + eval.pctIncorrect() + "; Percent unclassified: " + eval.pctUnclassified() + "; RMSE: " + eval.rootMeanSquaredError());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }
}
