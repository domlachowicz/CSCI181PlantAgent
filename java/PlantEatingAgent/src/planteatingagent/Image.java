package planteatingagent;

public class Image {

    public static final int IMAGE_SIZE = 6;
    int[][] image = null;
    PlantType classification = PlantType.NO_PLANT;

    public Image() {
        this(null);
    }

    public Image(int[][] image) {
        this(image, PlantType.NO_PLANT);
    }

    public Image(int[][] image, PlantType classification) {
        setImage(image);
        setClassification(classification);
    }

    public int[][] getImage() {
        return image;
    }

    public void setImage(int[][] image) {
        this.image = image;
    }

    public void setClassification(PlantType classification) {
        this.classification = classification;
    }

    public PlantType getClassification() {
        return classification;
    }

    public String toArff() {
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < IMAGE_SIZE; x++) {
            if (x != 0) {
                sb.append(",");
            }
            for (int y = 0; y < IMAGE_SIZE; y++) {
                if (y != 0) {
                    sb.append(",");
                }
                sb.append(image[x][y]);
            }
        }

        sb.append(classification.toString());
        sb.append("\n");

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < Agent.IMAGE_SIZE; row++) {
            for (int col = 0; col < Agent.IMAGE_SIZE; col++) {
                sb.append(String.format("%3d", image[row][col]));
            }
        }

        return sb.toString();
    }
}
