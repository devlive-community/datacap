package io.edurt.datacap.captcha;

import io.edurt.datacap.captcha.entity.ResultEntity;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Slf4j
public class CalculateUtils
{
    // Image width
    private static final int WIDTH = 200;
    // Image height
    private static final int HEIGHT = 40;

    private CalculateUtils()
    {}

    /**
     * Generates a CaptchaResultEntity by creating and manipulating an image.
     *
     * @return The generated CaptchaResultEntity containing the image.
     */
    public static ResultEntity generate()
    {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics(); // Create a graphics class
//        graphics.setColor(CaptchaUtils.getBackgroundColor()); // The background color should be lighter
        graphics.fillRect(0, 0, WIDTH, HEIGHT); // Painted background
        graphics.setColor(CaptchaUtils.getBackgroundColor()); // Border color
//        graphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1); // Draw border
        ResultEntity entity = CaptchaUtils.generateCalculateImage();
        String[] result = entity.getExpression();
        int length = result.length; // The number of verification codes generated
        Font font = new Font("YaHei", Font.ITALIC, 20); // Create a font, the size of the font should be determined according to the height of the picture
        graphics.setFont(font); // Set font
        for (int i = 0; i < length; i++) {
            String string = String.valueOf(result[i]);
            graphics.drawString(string, 30 * i + 30, 25);
        }
        graphics.dispose(); // Image takes effect
        String base64 = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();) {
            ImageIO.write(image, "png", os);
            base64 = Base64.getEncoder().encodeToString(os.toByteArray());
        }
        catch (Exception exception) {
            log.error("Failed to generate image", exception);
        }
        entity.setImage(base64);
        return entity;
    }
}
