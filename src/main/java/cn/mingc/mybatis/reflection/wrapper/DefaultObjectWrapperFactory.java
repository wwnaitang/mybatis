package cn.mingc.mybatis.reflection.wrapper;

import java.util.Collection;
import java.util.Map;

public class DefaultObjectWrapperFactory implements ObjectWrapperFactory {

    @Override
    public boolean hasWrapperFor(Object object) {
        return object instanceof ObjectWrapper;
    }

    @Override
    public ObjectWrapper getWrapperFor(Object object) {
        ObjectWrapper objectWrapper;
        if (object instanceof ObjectWrapper) {
            objectWrapper = (ObjectWrapper) object;
        } else if (object instanceof Map) {
            objectWrapper = new MapWrapper((Map<String, Object>) object);
        } else if (object instanceof Collection) {
            objectWrapper = new CollectionWrapper((Collection<?>) object);
        } else if (object.getClass().isArray()) {
            objectWrapper = new ArrayWrapper((Object[]) object);
        } else {
            objectWrapper = new BeanWrapper(object);
        }
        return objectWrapper;
    }
}
