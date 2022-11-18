package cn.mingc.mybatis.reflection.wrapper;

public class ArrayWrapper extends BaseWrapper {

    private Object[] array;

    public ArrayWrapper(Object[] array) {
        this.array = array;
    }

    @Override
    public Object get(String name) {
        int index = Integer.parseInt(name);
        return array[index];
    }

    @Override
    public void set(String name, Object value) {
        int index = Integer.parseInt(name);
        array[index] = value;
    }
}
