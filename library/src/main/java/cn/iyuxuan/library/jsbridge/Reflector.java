package cn.iyuxuan.library.jsbridge;

import java.lang.reflect.Field;

class Reflector<T> {

    private Object obj;
    private String fieldName;

    private Field field;

    Reflector(Object obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException("obj cannot be null");
        }
        this.obj = obj;
        this.fieldName = fieldName;
        prepare();
    }

    private void prepare() {
        Class<?> c = obj.getClass();
        while (c != null) {
            try {
                Field f = c.getDeclaredField(fieldName);
                f.setAccessible(true);
                field = f;
                return;
            } catch (Exception e) {
//                KLog.e(e.getMessage());
            } finally {
                c = c.getSuperclass();
            }
        }
    }

    T get() throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        if (field == null) {
            throw new NoSuchFieldException();
        }
        try {
            @SuppressWarnings("unchecked")
            T r = (T) field.get(obj);
            return r;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("unable to cast object");
        }
    }

    void set(T val) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        if (field == null) {
            throw new NoSuchFieldException();
        }
        field.set(obj, val);
    }
}
