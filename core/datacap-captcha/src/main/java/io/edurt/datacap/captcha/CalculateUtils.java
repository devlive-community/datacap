package io.edurt.datacap.captcha;

import io.edurt.datacap.captcha.entity.ResultEntity;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class CalculateUtils
{
    // Image width
    private static int WIDTH = 200;
    // Image height
    private static int HEIGHT = 40;

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
        graphics.setColor(CaptchaUtils.getBackgroundColor()); // The background color should be lighter
        graphics.fillRect(0, 0, WIDTH, HEIGHT); // Painted background
        graphics.setColor(CaptchaUtils.getBackgroundColor()); // Border color
//        graphics.drawRect(0, 0, DEFAULT_WIDTH - 1, DEFAULT_HEIGHT - 1); // Draw border
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
        entity.setImage(image);
        return entity;
    }
}
