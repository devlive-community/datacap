package io.edurt.datacap.server.common;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Properties;

public class BeanToPropertiesCommon
{
    private BeanToPropertiesCommon()
    {}

    public static Properties convertBeanToProperties(Object bean)
    {
        Properties properties = new Properties();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String propertyName = propertyDescriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = propertyDescriptor.getReadMethod();
                    Object value = readMethod.invoke(bean);
                    properties.setProperty(propertyName, value.toString());
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return properties;
    }
}
