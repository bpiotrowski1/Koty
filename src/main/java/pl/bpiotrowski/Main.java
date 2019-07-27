package pl.bpiotrowski;

import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 10; i++) {
            getBlurCatImage(getCat());
        }
    }

    public static Cat getCat() throws IOException {
        URL url = new URL("http://aws.random.cat/meow");
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Chrome");
        Scanner scan = new Scanner(connection.getInputStream());
        String jsonText = scan.nextLine();

        Gson gson = new Gson();
        return gson.fromJson(jsonText, Cat.class);
    }

    public static void getCatImage(Cat cat) throws IOException {
        URL url = new URL(cat.getFile());
        String destName = "src/main/resources/cats" + cat.getFile().substring(cat.getFile().lastIndexOf("/"));
        String ext = cat.getFile().substring(cat.getFile().length() - 3);
        BufferedImage image = ImageIO.read(url);
        File out = new File(destName);
        ImageIO.write(image, ext, out);
        System.out.println("Image: " + destName + " height: " + image.getHeight() + " width: " + image.getWidth() + " size: " + out.length() / Math.pow(10, 6) + "mb");
    }

    public static void getBlurCatImage(Cat cat) throws IOException {
        URL url = new URL(cat.getFile());
        String destName = "src/main/resources/cats" + cat.getFile().substring(cat.getFile().lastIndexOf("/"));
        String ext = cat.getFile().substring(cat.getFile().length() - 3);
        BufferedImage image = ImageIO.read(url);

        int radius = 11;
        int size = radius * 2 + 1;
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];

        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }

        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage blurred = op.filter(image, null);

        File out = new File(destName);
        ImageIO.write(blurred, ext, out);
    }
}
