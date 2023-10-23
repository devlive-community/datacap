package io.edurt.datacap.common.sql.configure;

public enum SqlOperator
{
    EQ("="),
    NEQ("<>"),
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<=");

    private final String symbol;

    SqlOperator(String symbol)
    {
        this.symbol = symbol;
    }

    public String getSymbol()
    {
        return symbol;
    }
}
