package planteatingagent;

import java.util.Random;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.ClassificationViaClustering;
import weka.clusterers.SimpleKMeans;
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
	private weka.classifiers.Classifier classifier;
	private ClassificationViaClustering classifier2;
	private static final String MODEL_FILE = "data/NNModel.model";
	private static final String MODEL_FILE2 = "data/clustererModel.model";
	private static final String DATA_FILE = "data/data.arff";
	private double priorNutritious;
	private double priorPoisonous;

	public Classifier() {
		priorNutritious = 0.167;
		priorPoisonous = 0.833;
	}

	private void loadDataSet() throws Exception {
		DataSource source = new DataSource(Classifier.class.getResourceAsStream(DATA_FILE));
		data = source.getDataSet();
		if (data.classIndex() == -1) {
			data.setClassIndex(data.numAttributes() - 1);
		}
	}

	public void createNewClassifier() {
		try {
			classifier = new MultilayerPerceptron();
			classifier2 = new ClassificationViaClustering();
			classifier2.setClusterer(new SimpleKMeans());
			loadDataSet();
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
			classifier2.buildClassifier(data);
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
		double[] distribution = null;
		try {
			classLabel = classifier.classifyInstance(i);
			distribution = classifier.distributionForInstance(i);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}
		i.setClassValue(classLabel);

		if (i.classAttribute().value((int) classLabel).equals("poisonous")) {
			if (distribution != null) {
				System.out.println("classified plant as poisonous with likelihood: " + distribution[0]);
			}
			return PlantType.POISONOUS_PLANT;
		} else if (i.classAttribute().value((int) classLabel).equals("nutritious")) {
			if (distribution != null) {
				System.out.println("classified plant as nutritious with likelihood: " + distribution[1]);
			}
			return PlantType.NUTRITIOUS_PLANT;
		} else {
			return PlantType.UNKNOWN_PLANT;
		}
	}

	public PlantType classifyInstance(Instance i, double threshold) {
		double[] distribution = null;
		try {
			distribution = classifier.distributionForInstance(i);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}

		if (distribution != null) {
			if (distribution[0] > threshold) {
				return PlantType.POISONOUS_PLANT;
			} else {
				return PlantType.NUTRITIOUS_PLANT;
			}
		} else {
			return PlantType.UNKNOWN_PLANT;
		}
	}

	public double getDistribution(Instance i, PlantType type) {
		double[] distribution = null;
		double[] distribution2 = null;
		double[] distributionFinal = null;
		try {
			distribution = classifier.distributionForInstance(i);
			distribution2 = classifier2.distributionForInstance(i);
			distributionFinal = new double[2];
			distributionFinal[0] = Math.sqrt(distribution[0]*distribution2[0]);
			distributionFinal[1] = Math.sqrt(distribution[1]*distribution2[1]);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}

		if (distributionFinal != null) {
			if (type.equals(PlantType.POISONOUS_PLANT)) {
				return distributionFinal[0];
			} else if (type.equals(PlantType.NUTRITIOUS_PLANT)) {
				return distributionFinal[1];
			}
		}
		return 0;
	}

	public void saveToDisk() {
		try {
			// serialize model
			Instances header = new Instances(data, 0);
			SerializationHelper.writeAll(MODEL_FILE, new Object[]{classifier, header});
			SerializationHelper.writeAll(MODEL_FILE2, new Object[]{classifier2, header});
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}
	}

	public void readFromDisk() {
		try {
			// deserialize model
			Object o[] = SerializationHelper.readAll(Classifier.class.getResourceAsStream(MODEL_FILE));
			classifier = (weka.classifiers.Classifier) o[0];
			if (o.length > 1) {
				data = (Instances) o[1];
			} else {
				loadDataSet();
			}
			// deserialize model2
			Object o2[] = SerializationHelper.readAll(Classifier.class.getResourceAsStream(MODEL_FILE2));
			classifier2 = (ClassificationViaClustering) o2[0];
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

	public double getPriorNutritious() {
		return priorNutritious;
	}

	public void setPriorNutritious(double priorNutritious) {
		this.priorNutritious = priorNutritious;
	}

	public double getPriorPoisonous() {
		return priorPoisonous;
	}

	public void setPriorPoisonous(double priorPoisonous) {
		this.priorPoisonous = priorPoisonous;
	}

	
}
