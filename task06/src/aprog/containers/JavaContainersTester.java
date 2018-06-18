package aprog.containers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import aprog.containers.Test.TestKind;
import aprog.util.TypeReference;


/**
 * Task 6 basic framework. This class runs the actual tests.
 *
 * @author Paul de Vrieze
 * @version 0.2 5th Mar 2012
 **/

public class JavaContainersTester {


    /**
     * A list of classes that are tested.
     */
    private static final TypeReference<?>[] TESTEDCLASSES = new TypeReference<?>[] {
             new TypeReference<ArrayList<Car>>() {},
             new TypeReference<LinkedList<Car>>() {},
             new TypeReference<LinkedHashSet<Car>>() {},
             new TypeReference<ArrayDeque<Car>>() {},
             new TypeReference<TreeSet<Car>>() {},
             new TypeReference<HashSet<Car>>() {},};

    /** How often should each test be repeated. */
    private static final int ITERCOUNT = 5;

    /** How many operations need to be performed. When <=0 operation count is quantity */
    private static final int OPERCOUNT = 5000;

    /** What is the maximum amount of milliseconds to allow before disabling a test? 15 seconds*/
    private static final BigDecimal MAXDURATION = BigDecimal.valueOf(15000L);

    /** The sizes of the collections to create. */
    private static final int[] TEST_QTYS = {   5000, 10000, 15000, 20000,  25000,
                                              30000, 35000, 40000, 45000,  50000,
                                              55000, 60000, 65000, 70000,  75000,
                                              80000, 85000, 90000, 95000, 100000, };

    /**
     * Switch that determines the output level. When set to {@code true} it is
     * very noisy indeed.
     */
    static final boolean NOISY = false;


    /**
     * Small helper class that holds the results of tests together with the
     * actual test.
     */
    private static final class TestHolder<T> {

        final Test<T> mTest;

        final AverageCounter mCounter;

        TestHolder(Test<T> test) {
            mTest = test;
            mCounter = new AverageCounter();
        }

        static <U> TestHolder<U> create(Test<U> test) {
            return new TestHolder<U>(test);
        }
    }

    /**
     * Method that actually tests a collection.
     *
     * @param factory The factory used to create and populate the collection to
     *            test.
     * @param tests The list of candidate tests. If tests don't apply to the
     *            given type they are not executed.
     * @param repeats How many times each test should be repeated.
     * @param listSize The size of the collections.
     * @return A list of test results.
     */
    private static <U> List<TestHolder<?>> testCollection(Map<Class<?>, Map<Integer, List<TestHolder<?>>>> results, AbstractFactory<U> factory, List<? extends Test<?>> tests, int repeats, int listSize, int operCount) {
        System.out.print("\n================================\n  Average timings\n   (");
        System.out.println(factory.getType().getSimpleName() + ") n=" + listSize);
        System.out.println("================================");
        final List<TestHolder<?>> testResults = getTestHolders(tests);
        for (int i = 0; i < repeats; ++i) {
            U cars = factory.create();
            for (TestHolder<?> testResult : testResults) {
                Test<? super U> test = factory.asApplicableTest(testResult.mTest);
                if (test != null) {
                    if (lastTestDuration(results, factory.getType(), test).compareTo(MAXDURATION)<0) {

                        {
                            TestKind testKind = test.getTestKind();
                            final int targetSize;
                            if (testKind==TestKind.CONSTRUCTIVE) {
                                // When the list needs to be expanded allow for n=m and start with empty list.
                                // Otherwise,
                                if (operCount>=listSize) {
                                    targetSize = 0;
                                } else {
                                    targetSize = listSize - operCount;
                                }
                            } else {
                                targetSize=listSize;
                            }
                            testKind.initializeCollection(cars, factory, targetSize);
                        }
                        if (NOISY) {
                            System.out.print("Running " + test.getOperationDescription() + "... ");
                        }
                        final BigDecimal duration = test.execute(cars, listSize, operCount);
                        testResult.mCounter.add(duration);
                        if (NOISY) {
                            System.out.println(NanoTimer.getTimerDurationText("", duration));
                        }
                    } else {
                        System.err.println("Skipping test " + testResult.mTest.getOperationDescription() + " because of excessive time costs");
                    }
                } else if (NOISY) {
                    System.out.println("Skipping test " + testResult.mTest.getOperationDescription() + " because of type incompatibility");
                }
            }
        }

        // Print the actual test results to the standard output.
        for (TestHolder<?> testResult : testResults) {
            if (testResult.mCounter.count > 0) {
                System.out.println(NanoTimer.getTimerDurationText(testResult.mTest + " - (" + testResult.mCounter.count + " repeats): ", testResult.mCounter.getAverage()));
            }
        }
        return testResults;
    }

    /**
     * Given a collection of tests, create a list of TestHolders for those
     * tests.
     *
     * @param tests The tests that should be wrapped in holders.
     * @return The list of TestHolders.
     */
    private static List<TestHolder<?>> getTestHolders(List<? extends Test<?>> tests) {
        List<TestHolder<?>> testResults = new ArrayList<TestHolder<?>>(tests.size());
        for (Test<?> test : tests) {
            testResults.add(TestHolder.create(test));
        }
        return testResults;
    }

    /**
     * Constructor.
     **/
    public JavaContainersTester() {}

    /**
     * Calls the test methods for the containers.
     *
     * @param iterationCount The amount of iterations to use for testing the
     *            class
     * @param operCount How many operations to run on each list. This remains constant over
     *                  all collection sizes.
     * @param elementCounts The amount of elements to put into the list
     **/
    public static void testContainers(int iterationCount, int operCount, int[] elementCounts) {
        System.out.println("Java Containers Tester started ...");

        List<? extends Test<?>> tests = BasicTests.getTests();
        Map<Class<?>, Map<Integer, List<TestHolder<?>>>> results = new HashMap<Class<?>, Map<Integer, List<TestHolder<?>>>>();

        Iterable<AbstractFactory<? >> factories = getFactories(TESTEDCLASSES);
        for (int elementCount : elementCounts) {
            // Allow the operation count to be less than zero, in which case operations equals elements.
            final int realOperCount = operCount <=0 ? elementCount : operCount;
            for (AbstractFactory<?> factory : factories) {
                try {
                    List<TestHolder<?>> result = testCollection(results, factory, tests, iterationCount, elementCount, realOperCount);
                    addToResults(results, factory.getType(), result, elementCount);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }

        // For each class separately, write a file with the results.
        for (Entry<Class<?>, Map<Integer, List<TestHolder<?>>>> classResult : results.entrySet()) {
            writeClassResultsToFile(classResult.getKey(), classResult.getValue());
        }

    }

    /**
     * Actually write a file with test results.
     *
     * @param testedClass The class these test results correspond to.
     * @param iterResults The results of the testing of this class.
     */
    private static void writeClassResultsToFile(Class<?> testedClass, Map<Integer, List<TestHolder<?>>> iterResults) {
        // A set of all columns. It being linked preserves the order. The set ensures uniqueness.
        LinkedHashSet<Integer> columns = new LinkedHashSet<Integer>();
        // A set of all rows. It being linked preserves the order, being a set ensures uniqueness.
        LinkedHashSet<String> rows = new LinkedHashSet<String>();

        // Determine the rows and columns.
        for (Entry<Integer, List<TestHolder<?>>> result : iterResults.entrySet()) {
            columns.add(result.getKey());
            for (TestHolder<?> testHolder : result.getValue()) {
                rows.add(testHolder.mTest.toString());
            }
        }


        String fileName = testedClass.getSimpleName() + "_test.csv";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            try {
                // Write the first (header) row.
                out.write('"');
                out.write(testedClass.getSimpleName());
                out.write('"'); // No comma so that next columns can start with separator.
                for (Integer column : columns) {
                    out.write(",\"");
                    out.write(column.toString());
                    out.write('"');
                }
                out.write('\n');// First (header) row finished.

                for (String rowLabel : rows) {
                    // First write the label.
                    out.write('"');
                    out.write(rowLabel);
                    out.write('"');

                    for (Integer column : columns) {
                        out.write(',');
                        List<TestHolder<?>> colResult = iterResults.get(column);
                        TestHolder<?> rowResult = null;
                        for (TestHolder<?> candidate : colResult) {
                            if (candidate.mTest.toString().equals(rowLabel)) {
                                rowResult = candidate;
                                break;
                            }
                        }
                        if (rowResult != null && rowResult.mCounter.count > 0) {
                            out.write(rowResult.mCounter.getAverage().toString());
                        }
                    }
                    out.write('\n');
                }
            } finally {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /** Determine how long the test took on the last invocation. */
    private static BigDecimal lastTestDuration(Map<Class<?>, Map<Integer, List<TestHolder<?>>>> results, TypeReference<?> type, Test<?> test) {
        Map<Integer, List<TestHolder<?>>> classResults = results.get(type);
        BigDecimal result = BigDecimal.ZERO;
        if (classResults!=null) {
            for(List<TestHolder<?>> tests: classResults.values()) {
                for(TestHolder<?> candidate: tests) {
                    if (candidate.mTest.equals(test)) {
                        BigDecimal time = candidate.mCounter.getAverage();
                        if (time!=null && result.compareTo(time)<0) {
                            result = time;
                        }
                    }
                }
            }
        }
        return result;

    }

    /**
     * Update the results map. This is a separate method to keep things concise.
     *
     * @param results The results map to update.
     * @param type The class the results are for.
     * @param result The actual list of results for this class and count.
     * @param elementCount The amount of elements the results correspond to.
     */
    private static void addToResults(Map<Class<?>, Map<Integer, List<TestHolder<?>>>> results, TypeReference<?> type, List<TestHolder<?>> result, int elementCount) {
        Map<Integer, List<TestHolder<?>>> classResults = results.get(type.getRawType());
        if (classResults == null) {
            classResults = new LinkedHashMap<Integer, List<TestHolder<?>>>();
            results.put(type.getRawType(), classResults);
        }

        if (classResults.containsKey(Integer.valueOf(elementCount))) {
            classResults.remove(Integer.valueOf(elementCount));
        }
        classResults.put(Integer.valueOf(elementCount), result);

    }

    /**
     * Helper method that given an array of classes creates a list of factories
     * for testing those classes.
     *
     * @param classes The classes to use.
     * @return The factories.
     */
    private static Iterable<AbstractFactory<?>> getFactories(TypeReference<?>[] classes) {
        ArrayList<AbstractFactory<?>> result = new ArrayList<AbstractFactory<?>>(classes.length);

        for (TypeReference<?> clazz : classes) {
            if (Collection.class.isAssignableFrom(clazz.getRawType())) {
                result.add(getFactoryFromClass(clazz.asSubClass(AbstractTest.COLLECTION)));
            } else {
                throw new IllegalArgumentException("The class " + clazz.getRawType().getName() + " is not a collection");
            }

        }
        return result;
    }

    /**
     * Create a factory for a given class. This method is needed to be able to
     * use the class type parameter in the invocation of the constructor.
     *
     * @param clazz The class for the factory.
     * @return A factory for the class.
     */
    private static <T extends Collection<?>> AbstractFactory<T> getFactoryFromClass(TypeReference<T> clazz) {
        return new CollectionFactory<T>(clazz);
    }

    /**
     * Run the tests.
     *
     * @param args This parameter is completely ignored.
     */
    public static void main(String[] args) {
        JavaContainersTester.testContainers(ITERCOUNT, OPERCOUNT, TEST_QTYS);
    }

} // end of class JavaContainersTester
