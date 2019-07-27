package pl.bpiotrowski;

import com.google.gson.Gson;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackMessage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Cat cat = getRandomCat();
        //downloadCat(cat);
        sendCat(cat);
    }

    public static Cat getRandomCat() throws IOException {
        URL url = new URL("http://aws.random.cat/meow");
        InputStream inputStream = url.openStream();
        try (Scanner scan = new Scanner(inputStream)) {
            String jsonText = scan.nextLine();
            Gson gson = new Gson();
            return gson.fromJson(jsonText, Cat.class);
        }
    }

    public static void sendCat(Cat cat) {
        SlackApi api = new SlackApi("https://hooks.slack.com/services/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");
        SlackAttachment attachment = new SlackAttachment("").setImageUrl(cat.getCatUrl()).setColor("good");
        api.call(new SlackMessage(cat.getCatUrl()).addAttachments(attachment));
    }

    public static void downloadCat(Cat cat) throws IOException {
        URL url = new URL(cat.getCatUrl());
        String destName = "src/main/resources/cats" + cat.getCatUrl().substring(cat.getCatUrl().lastIndexOf("/"));
        String ext = cat.getCatUrl().substring(cat.getCatUrl().lastIndexOf(".") + 1);

        BufferedImage image = ImageIO.read(url);

        File out = new File(destName);
        ImageIO.write(image, ext, out);
        System.out.println("Image: " + out.getName() + " height: " + image.getHeight() + " width: " + image.getWidth() + " size: " + out.length() / 1024 + "KB");
    }

    public static void getBlurCatImage(Cat cat) throws IOException {
        URL url = new URL(cat.getCatUrl());
        String destName = "src/main/resources/cats" + cat.getCatUrl().substring(cat.getCatUrl().lastIndexOf("/"));
        String ext = cat.getCatUrl().substring(cat.getCatUrl().length() - 3);
        BufferedImage image = ImageIO.read(url);

        int radius = 11;
        int size = radius * 2 + 1;
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];

        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }

        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
        BufferedImage blurred = op.filter(image, null);

        File out = new File(destName);
        ImageIO.write(blurred, ext, out);
    }
}
