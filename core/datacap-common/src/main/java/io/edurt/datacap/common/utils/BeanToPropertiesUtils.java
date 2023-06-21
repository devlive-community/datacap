package io.edurt.datacap.common.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Properties;

public class BeanToPropertiesUtils
{
    private BeanToPropertiesUtils()
    {}

    public static Properties convertBeanToProperties(Object bean)
    {
        Properties properties = new Properties();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String propertyName = propertyDescriptor.getName();
                if (!propertyDescriptor.getPropertyType().getName().startsWith("io\\.edurt\\.datacap")) {
                    if (!propertyName.equals("class")) {
                        Method readMethod = propertyDescriptor.getReadMethod();
                        if (!readMethod.getGenericReturnType().getTypeName().contains("io.edurt.datacap")) {
                            Object value = readMethod.invoke(bean);
                            if (ObjectUtils.isNotEmpty(value)) {
                                properties.setProperty(propertyName, value.toString());
                            }
                        }
                    }
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return properties;
    }
}
