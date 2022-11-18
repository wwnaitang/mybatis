package cn.mingc.mybatis.reflection;

import cn.mingc.mybatis.reflection.factory.DefaultObjectFactory;
import cn.mingc.mybatis.reflection.factory.ObjectFactory;
import cn.mingc.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import cn.mingc.mybatis.reflection.wrapper.ObjectWrapperFactory;

public class SystemMetaObject {

    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();

    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(new NullObject(),
            DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    }

    private static class NullObject {

    }

}
