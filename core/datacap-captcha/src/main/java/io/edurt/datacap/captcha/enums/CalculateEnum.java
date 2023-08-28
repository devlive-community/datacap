package io.edurt.datacap.captcha.enums;

public enum CalculateEnum
{

    ADD("+", "加"),
    SUBTRACT("-", "减"),
    MULTIPLY("*", "乘"),
    DIVIDE("÷", "除"),
    EQUAL("=", "等");

    private final String value_en;
    private final String value_zh;

    CalculateEnum(String value_en, String value_zh)
    {
        this.value_en = value_en;
        this.value_zh = value_zh;
    }

    public String getValueEn()
    {
        return value_en;
    }

    public String getValueZh()
    {
        return value_zh;
    }
}
