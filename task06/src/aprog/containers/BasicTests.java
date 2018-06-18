package aprog.containers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import aprog.containers.Test.TestKind;



enum BasicTests {

    /** Add elements to the collection. */
    ADD(new AbstractTest<Collection<Car>,Object>(TestKind.CONSTRUCTIVE, AbstractTest.COLLECTION, "Add elements one by one") {

        @Override
        protected void doExecute(Collection<Car> collection, int size, int operCount, Object setupResult) {
            for (int i=0; i<operCount; ++i) {
                collection.add(new Car());
            }
        }

    }),

    ADDFRONTQUEUE(new AbstractTest<Deque<Car>, Object>(TestKind.CONSTRUCTIVE, AbstractTest.DEQUE, "Add elements one by one") {

        @Override
        void doExecute(Deque<Car> d, int size, int operCount, Object setupResult) {
            d.add(new Car());
        }

    }),

    ADDFRONTLIST(new AbstractTest<List<Car>, Object>(TestKind.CONSTRUCTIVE, AbstractTest.LIST, "Add elements to the front of a list") {

        @Override
        void doExecute(List<Car> l, int size, int operCount, Object setupResult) {
            for (int i=0; i<operCount; ++i) {
                l.add(0, new Car());
            }
        }

    }),

    ADDLISTRANDOM(new AbstractTest<List<Car>, Object>(TestKind.CONSTRUCTIVE, AbstractTest.LIST, "Add elements to the front of a list") {

        @Override
        void doExecute(List<Car> l, int size, int operCount, Object setupResult) {
            Random r = new Random();
            for (int i=0; i<operCount; ++i) {
                l.add(r.nextInt(l.size()+1), new Car());
            }
        }

    }),

    /** Remove elements from the collection. */
    CLEAR(new AbstractTest<Collection<Car>,Object>(TestKind.DESTRUCTIVE, AbstractTest.COLLECTION, "Clear the collection") {
        @Override
        void doExecute(Collection<Car> c, int size, int operCount, Object setupResult) {
            c.clear();
        }
    }),

    CONTAINS( new AbstractTest<Collection<Car>, ArrayList<Car>>(TestKind.DESTRUCTIVE, AbstractTest.COLLECTION, "Check whether an element is contained in the list") {

        @Override
        void doExecute(Collection<Car> list, int size, int operCount, ArrayList<Car> setupResult) {
            for(Car car:setupResult) {
                list.contains(car);
            }
        }

        @Override
        ArrayList<Car> doSetUp(Collection<Car> collection, int size, int operCount) {
            ArrayList<Car> realelements = new ArrayList<Car>(collection);
            Collections.shuffle(realelements);
            ArrayList<Car> result = new ArrayList<Car>();
            int cnt = Math.min(operCount/2,realelements.size());
            for(int i=0;i<cnt; ++i) {
                result.add(realelements.get(i));
            }
            for(int i=result.size();i<operCount;++i){
                result.add(new Car());
            }
            return result;
        }

    }),

    REMOVEFRONTITERATOR ( new AbstractTest<Collection<Car>,Object>(TestKind.DESTRUCTIVE, AbstractTest.COLLECTION, "Remove first with iterator") {
        @Override
        void doExecute(Collection<Car> c, int size, int operCount, Object setupResult) {
            Iterator<Car> it = c.iterator();
            for(int i=0; i<operCount&&it.hasNext(); ++i) {
                it.next();
                it.remove();
            }
        }

    }),

    REMOVEFRONTDEQUE( new AbstractTest<Deque<Car>,Object>(TestKind.DESTRUCTIVE, AbstractTest.DEQUE, "Remove first from deque") {
        @Override
        void doExecute(Deque<Car> o, int size, int operCount, Object setupResult) {
            Deque<Car> list = o;
            for(int i=0;i<operCount && (! list.isEmpty()); ++i) {
                list.removeFirst();
            }
        }

    }),

    REMOVEBACKDEQUE(new AbstractTest<Deque<Car>,Object>(TestKind.DESTRUCTIVE, AbstractTest.DEQUE, "Remove last from deque") {
        @Override
        void doExecute(Deque<Car> deque, int size, int operCount, Object setupResult) {
            for(int i=0;i<operCount && (! deque.isEmpty()); ++i) {
                deque.removeLast();
            }
        }

    }),

    REMOVEFRONTLIST( new AbstractTest<List<Car>,Object>(TestKind.DESTRUCTIVE, AbstractTest.LIST, "Remove front of list by index") {
        @Override
        void doExecute(List<Car> l, int size, int operCount, Object setupResult) {
            final int len = l.size();
            for(int i=0;i<operCount && (! l.isEmpty()); ++i) {
                l.remove(0);
            }
        }

    }),

    REMOVEBACKLIST ( new AbstractTest<List<Car>, Object>(TestKind.DESTRUCTIVE, AbstractTest.LIST, "Remove last of list by index") {
        @Override
        void doExecute(List<Car> l, int size, int operCount, Object setupResult) {
            for(int i=0;i<operCount && (! l.isEmpty()); ++i) {
                l.remove(l.size()-1);
            }
        }

    }),

    SEQUENTIALINDEXEDGET ( new AbstractTest<List<Car>,Object>(TestKind.NEUTRAL, AbstractTest.LIST, "Get items by index") {
        @Override
        void doExecute(List<Car> list, int size, int operCount, Object setupResult) {
            final int len=list.size();
            for(int i=0; i<operCount; ++i) {
                int pos = i%len;
                list.get(pos).emptyMethod();
            }
        }

    }),
    
    ITERATE ( new AbstractTest<Collection<Car>,Object>(TestKind.NEUTRAL, AbstractTest.COLLECTION, "Get items through iterable") {
        @Override
        void doExecute(Collection<Car> collection, int size, int operCount, Object setupResult) {
            Iterator<Car> it = collection.iterator();
            for(int i=0; i<operCount; ++i) {
                if (! it.hasNext()) {
                    it = collection.iterator();
                }
                it.next().emptyMethod();
            }
        }

    }),

    REMOVERANDOMINDEX(new AbstractTest<List<Car>, Object>(TestKind.DESTRUCTIVE, AbstractTest.LIST, "Remove random indices out of a list") {

        @Override
        void doExecute(List<Car> list, int size, int operCount, Object setupResult) {
            Random r=new Random();
            for (int i=Math.min(operCount, list.size()); i>0; --i) {
             list.get(r.nextInt(list.size()));
            }
        }

    }),

    REMOVERANDOM ( new AbstractTest<Collection<Car>, ArrayList<Car>>(TestKind.DESTRUCTIVE, AbstractTest.COLLECTION, "Randomly remove items out of a collection") {

        @Override
        void doExecute(Collection<Car> list, int size, int operCount, ArrayList<Car> setupResult) {
            for(Car car:setupResult) {
                list.remove(car);
            }
        }

        @Override
        ArrayList<Car> doSetUp(Collection<Car> collection, int size, int operCount) {
            ArrayList<Car> realelements = new ArrayList<Car>(collection);
            Collections.shuffle(realelements);
            ArrayList<Car> result = new ArrayList<Car>();
            int cnt = Math.min(operCount/2,realelements.size());
            for(int i=0;i<cnt; ++i) {
                result.add(realelements.get(i));
            }
            for(int i=result.size();i<operCount;++i){
                result.add(new Car());
            }
            
            return result;
        }

    }),

    ;

//    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static List<Test<?>> _tests;

    private Test<?> mTest;
    
    private BasicTests(Test<?> test) {
        mTest=test;
        mTest.setName(name());
    }

    @SuppressWarnings("unchecked")
    public static List<? extends Test<?>> getTests() {
        if (_tests==null) {
            @SuppressWarnings("rawtypes")
            List tests = new ArrayList<Test<?>>(values().length);
            for(BasicTests test: values()) {
                tests.add(test.mTest);
            }
            _tests = tests;
        }
        return _tests;
    }

}
