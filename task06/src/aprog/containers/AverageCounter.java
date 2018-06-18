package aprog.containers;

import java.math.BigDecimal;

/******************************** Timer functionality (ends) ************************************/

class AverageCounter {
    BigDecimal total = BigDecimal.ZERO;
    long count = 0;

    public void add(long value) {
        add(BigDecimal.valueOf(value));
    }

    public void add(BigDecimal value) {
        total = total.add(value);
        ++count;
    }

    public BigDecimal getAverage() {
        if (count==0) { return null; }
        return total.divide(BigDecimal.valueOf(count), 10, BigDecimal.ROUND_HALF_EVEN);
    }
}