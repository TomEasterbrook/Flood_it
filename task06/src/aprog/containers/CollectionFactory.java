package aprog.containers;

import java.util.ArrayList;
import java.util.Collection;

import aprog.util.TypeReference;


class CollectionFactory<T extends Collection<?>> extends AbstractFactory<T> {

    CollectionFactory(TypeReference<T> type) {
        super(type);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void populate(T collection, int listSize) {
        if (collection instanceof ArrayList) {
            ((ArrayList<T>) collection).ensureCapacity(listSize);
        }
        for (int i = 0; i < listSize; ++i) {
            ((Collection)collection).add(new Car());
        }
    }

    @Override
    public int size(T collection) {
        return collection.size();
    }

    @Override
    public void clear(T collection) {
        collection.clear();
    }

}