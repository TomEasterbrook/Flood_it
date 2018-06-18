package aprog.containers;

import java.math.BigDecimal;

import aprog.util.TypeReference;


interface Test<T> {

    /** The kinds of tests possible. */
    enum TestKind {
        /** The test will destroy the input. */
        DESTRUCTIVE,
        /** The test will add elements to a list (and needs a partial one to start. */
        CONSTRUCTIVE,
        /** The test will not modify the input. */
        NEUTRAL;

        /** Make sure the collection is ready to run the test.
         * @param collection The collection of cars to initialize for this test.
         * @param factory The factory to use to poplulate the collection.
         * @param listSize The amount of elements that the populated list needs to have.
         */
        public <U> void initializeCollection(U collection, AbstractFactory<U> factory, int listSize) {
            if (factory.size(collection)!=listSize) {
                if (factory.size(collection)!=0) { factory.clear(collection); }

                factory.populate(collection, listSize);
            }
        }

    }

    /** Execute the test */
    BigDecimal execute(T c, int size, int operCount);

    /** Get the kind of the test. */
    TestKind getTestKind();

    /** Get the class that this test works on. */
    TypeReference<T> getType();

    /** Get a description of what the test does. */
    String getOperationDescription();

    /**
     * Update the name of the test. If the current name was not set or is null,
     * this function should not throw. Otherwise it is implementation dependent.
     *
     * @throws IllegalStateException If the name is changed in unsupporting
     *             environments.
     */
    void setName(String name);

}
