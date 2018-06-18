package aprog.containers;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import aprog.util.TypeReference;



abstract class AbstractTest<T,V> implements Test<T>{

    private final TestKind mTestKind;
    private final TypeReference<T> mType;
    private final String mDescription;
    private String mName;

    static final TypeReference<Collection<Car>> COLLECTION = new TypeReference<Collection<Car>>() {};

    static final TypeReference<List<Car>> LIST = new TypeReference<List<Car>>() {};;

    static final TypeReference<Deque<Car>> DEQUE = new TypeReference<Deque<Car>>() {};

    static final TypeReference<Map<Car,Car>> MAP = new TypeReference<Map<Car,Car>>() {};

    @Deprecated
    AbstractTest(TestKind testKind, TypeReference<T> type) {
        this(null, testKind, type, null);
    }

    @Deprecated
    AbstractTest(String name, TestKind testKind, TypeReference<T> type) {
        this(name, testKind, type, null);
    }

    AbstractTest(TestKind testKind, TypeReference<T> type, String description) {
        this(null, testKind, type, description);
    }

    @Deprecated
    AbstractTest(String name, TestKind testKind, TypeReference<T> type, String description) {
        mTestKind=testKind;
        mType=type;
        mName = name;
        mDescription=description == null? name : description;
    }

    abstract void doExecute(T o, int size, int operCount, V setupResult);

    V doSetUp(T o, int size, int operCount) { return null; };

    @Override
    public final BigDecimal execute(T o, int size, int operCount) {
        V setupResult = doSetUp(o, size, operCount);
        NanoTimer timer = new NanoTimer();
        doExecute(o, size, operCount, setupResult);
        return timer.finish();
    }


    @Override
    public TestKind getTestKind() {
        return mTestKind;
    }


    @Override
    public TypeReference<T> getType() {
        return mType;
    }

    public String name() {
        return mName;
    }
    
    public void setName(String pName) {
        if (mName!=null && (! mName.equals(pName))) {
            throw new IllegalStateException("Attempting to change the test name");
        }
        mName = pName;
    }

    @Override
    public String getOperationDescription() {
        if (mName!=mDescription) {
            return mName+ "("+mType.getSimpleName()+") - "+ mDescription;
        } else {
            return mDescription+ "("+mType.getSimpleName()+")";
        }
    }

    @Override
    public String toString() {
        return mName;
    }


}
