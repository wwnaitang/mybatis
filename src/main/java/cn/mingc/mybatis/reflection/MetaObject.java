package cn.mingc.mybatis.reflection;

import cn.mingc.mybatis.reflection.factory.ObjectFactory;
import cn.mingc.mybatis.reflection.property.PropertyTokenizer;
import cn.mingc.mybatis.reflection.wrapper.ObjectWrapper;
import cn.mingc.mybatis.reflection.wrapper.ObjectWrapperFactory;

public class MetaObject {

    private Object originalObject;

    private ObjectWrapper objectWrapper;

    private ObjectFactory objectFactory;

    private ObjectWrapperFactory objectWrapperFactory;

    private MetaObject(Object originalObject, ObjectFactory objectFactory,
            ObjectWrapperFactory objectWrapperFactory) {
        this.originalObject = originalObject;
        this.objectFactory = objectFactory;
        this.objectWrapperFactory = objectWrapperFactory;

        this.objectWrapper = objectWrapperFactory.getWrapperFor(originalObject);
    }

    public static MetaObject forObject(Object originalObject, ObjectFactory objectFactory,
            ObjectWrapperFactory objectWrapperFactory) {
        if (originalObject == null) {
            return SystemMetaObject.NULL_META_OBJECT;
        }
        return new MetaObject(originalObject, objectFactory, objectWrapperFactory);
    }

    public Object getValue(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject valueMetaObject = metaObjectForProperty(prop.getName());
            return valueMetaObject.getValue(prop.getChildren());
        } else {
            return objectWrapper.get(prop.getName());
        }
    }

    public void setValue(String name, Object value) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject valueMetaObject = metaObjectForProperty(prop.getName());
            valueMetaObject.setValue(prop.getChildren(), value);
        } else {
            objectWrapper.set(prop.getName(), value);
        }
    }

    private MetaObject metaObjectForProperty(String name) {
        Object value = getValue(name);
        return MetaObject.forObject(value, objectFactory, objectWrapperFactory);
    }

}
