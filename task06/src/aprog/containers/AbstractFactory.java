package aprog.containers;

import aprog.util.TypeReference;


public abstract class AbstractFactory<T> {

    protected final TypeReference<T> mType;

    public abstract void populate(T collection, int listSize);

    public AbstractFactory(TypeReference<T> pType) {
        mType = pType;
    }

    public T create() {
        T collection;
        try {
            collection = mType.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return collection;
    }

    public TypeReference<T> getType() {
        return mType;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Test<? super T> asApplicableTest(Test<?> test) {
        if (test.getType().isAssignableFrom(mType)) {
            return ((Test) test);
        } else {
            return null;
        }
    
    }
    
    public abstract int size(T collection);
    
    public abstract void clear(T collection);

}