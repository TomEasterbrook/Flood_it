package aprog.containers;

import java.math.BigDecimal;

/**
 * Class that helps with measuring how long an operation takes.
 * @author pdvrieze
 *
 */
class NanoTimer {
    private static final BigDecimal MS_DIVISOR = BigDecimal.valueOf(1000);

    private long startTimeNanos;

    public NanoTimer() {
        startTimeNanos=System.nanoTime();
    }

    /**
     * Provides a textual summary of the execution time measured by the
     * timer.
     *
     * @param Prefix for the textual summary (e.g. operation performed)
     * @return String containing the textual summary
     */
    static String getTimerDurationText(String prefix, BigDecimal duration) {
        return "" + prefix + " time: " + duration.divideToIntegralValue(MS_DIVISOR).longValue() + " s "
                                                 + duration.remainder(MS_DIVISOR).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() + " ms. ";
    }

    public BigDecimal finish() {
        long differenceNanos = System.nanoTime() - startTimeNanos;
        if (startTimeNanos==0) {
            throw new IllegalStateException("Timers can not be reused");
        }
        if (JavaContainersTester.NOISY) {
            System.out.println("Nanosecond difference: "+differenceNanos);
        }
        return BigDecimal.valueOf(differenceNanos, 6);
    }
}