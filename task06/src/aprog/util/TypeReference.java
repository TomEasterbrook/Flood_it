package aprog.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * References a generic type.
 *
 * @author crazybob@google.com (Bob Lee)
 * @see <a
 *      href="http://gafter.blogspot.co.uk/2006/12/super-type-tokens.html">gafter.blogspot.co.uk</a>
 */
public abstract class TypeReference<T> {

    private final Type type;

    private volatile Constructor<?> constructor;

    protected TypeReference() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }

    /**
     * Instantiates a new instance of {@code T} using the default, no-arg
     * constructor.
     */
    @SuppressWarnings("unchecked")
    public T newInstance() throws IllegalAccessException, InstantiationException {
        try {
            if (constructor == null) {
                constructor = getRawType().getConstructor();
            }
            return (T) constructor.newInstance();
        } catch (InvocationTargetException|NoSuchMethodException e) {
            final InstantiationException f = new InstantiationException();
            f.initCause(e);
            throw f;
        }
    }

    /**
     * Gets the referenced type.
     */
    public Type getType() {
        return this.type;
    }

    public String getSimpleName() {
        return getRawType().getSimpleName();
    }

    public boolean isAssignableFrom(TypeReference<?> other) {
        if (!(getRawType().isAssignableFrom(other.getRawType()))) {
            return false;
        }
        // TODO do more thorough checks on children
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Class<T> getRawType() {
        return (Class<T>) (type instanceof Class<?> ? (Class) type : (Class) ((ParameterizedType) type).getRawType());
    }

    @SuppressWarnings("unchecked")
    public <U> TypeReference<? extends U> asSubClass(Class<U> pClass) {
        if (pClass.isAssignableFrom(getRawType())) {
            return (TypeReference<U>) this;
        }
        throw new ClassCastException();
    }

    @SuppressWarnings("unchecked")
    public <U> TypeReference<? extends U> asSubClass(TypeReference<U> pClass) {
        if (pClass.getRawType().isAssignableFrom(getRawType())) {
            return (TypeReference<U>) this;
        }
        throw new ClassCastException();
    }
}