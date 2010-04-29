package planteatingagent;

import java.io.PrintWriter;
import java.util.Collection;

public class ImageArffWriter {

    public static void writeArffFile(Collection<Image> images, PrintWriter out) {
        out.println("@relation plants");
        out.println();

        for (int x = 0; x < Image.IMAGE_SIZE; x++) {
            for (int y = 0; y < Image.IMAGE_SIZE; y++) {
                out.format("@attribute\t%d_%d\t{0, 1}", x, y).println();
            }
        }
        out.println("@attribute\tclass\t{poisonous, nutritious}");
        out.println();
        out.println("@data");
        out.println();

        for (Image image : images) {
            out.println(image.toArff());
        }
    }
}
