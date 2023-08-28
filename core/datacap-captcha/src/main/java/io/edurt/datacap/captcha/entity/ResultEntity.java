package io.edurt.datacap.captcha.entity;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ResultEntity
{
    // Calculation formula
    private String[] expression;
    // Calculated result
    private int result;
    // Image object
    private BufferedImage image;

    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    public String[] getExpression()
    {
        return expression;
    }

    public void setExpression(String[] expression)
    {
        this.expression = expression;
    }

    public int getResult()
    {
        return result;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    @Override
    public String toString()
    {
        return "ResultEntity{" +
                "expression=" + Arrays.toString(expression) +
                ", result=" + result +
                ", image=" + image +
                '}';
    }
}
