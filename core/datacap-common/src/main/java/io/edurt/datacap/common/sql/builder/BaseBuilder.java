package io.edurt.datacap.common.sql.builder;

class BaseBuilder
        extends BaseAbstractBuilder<BaseBuilder>
{
    @Override
    public BaseBuilder getSelf()
    {
        return this;
    }
}