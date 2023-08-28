package io.edurt.datacap.captcha.entity;

import lombok.Data;

@Data
public class ResultEntity
        implements Cloneable
{
    // Calculation formula
    private String[] expression;
    // Calculated result
    private Integer result;
    // Image object
    private String image;

    @Override
    public ResultEntity clone()
    {
        try {
            return (ResultEntity) super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
