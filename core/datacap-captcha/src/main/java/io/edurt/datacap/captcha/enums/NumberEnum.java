package io.edurt.datacap.captcha.enums;

public enum NumberEnum
{

    ONE("1", "壹"),
    TWO("2", "贰"),
    THREE("3", "叁"),
    FOUR("4", "肆"),
    FIVE("5", "伍"),
    SIX("6", "陆"),
    SEVEN("7", "柒"),
    EIGHT("8", "捌"),
    NINE("9", "玖"),
    ZERO("0", "零");

    private final String value_en;
    private final String value_zh;

    NumberEnum(String value_en, String value_zh)
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
