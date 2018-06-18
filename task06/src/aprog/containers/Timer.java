package aprog.containers;

/**
 *
 * @author pdvrieze
 * @deprecated Please use {@link NanoTimer}
 */
@Deprecated
class Timer {

    private long startTime;

    public Timer() {
        startTime = System.currentTimeMillis(); // record the start time of the
    }

    /**
     * Provides a textual summary of the execution time measured by the
     * timer.
     *
     * @param Prefix for the textual summary (e.g. operation performed)
     * @return String containing the textual summary
     */
    static String getTimerDurationText(String prefix, long duration) {
        return "" + prefix + " execution time: " + duration + " ms, " + (duration / 1000) + " s.";
    }

    public long finish() {
        long difference = System.currentTimeMillis() - startTime;
        if (startTime == 0) {
            throw new IllegalStateException("Timers can not be reused");
        }
        startTime = 0;
        return difference;
    }
}