package cn.mingc.mybatis.reflection.wrapper;

import java.util.Collection;
import java.util.List;

public class CollectionWrapper extends BaseWrapper {

    private Collection<?> collection;

    public CollectionWrapper(Collection<?> collection) {
        this.collection = collection;
    }

    @Override
    public Object get(String name) {
        if (collection instanceof List) {
            int index = Integer.parseInt(name);
            return ((List) collection).get(index);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(String name, Object value) {
        if (collection instanceof List) {
            int index = Integer.parseInt(name);
            ((List) collection).set(index, value);
            return;
        }
        throw new UnsupportedOperationException();
    }
}
