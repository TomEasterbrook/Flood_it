package aprog.containers;

/******************************** A basic inner class *******************************************/
class Car implements Comparable<Car> {

    // following are self explanatory
    @SuppressWarnings("unused")
    private String manufacturer;

    @SuppressWarnings("unused")
    private String model;

    private float engineCapacity;

    private long hash;

    // basic default initialisation of the Car (all have the same manufacturer, and model)
    public Car() {
        manufacturer = "Volkswagen";
        model = "Golf";
        engineCapacity = ((float) Math.random()) * 2.0f;
        hash = Double.doubleToLongBits(Math.random());
    }

    // dummy method to help avoid compiler optimisations
    public void emptyMethod() {
        ;
    }

    @Override
    public int hashCode() {
        return (int) hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Car other = (Car) obj;
        if (hash != other.hash || engineCapacity != other.engineCapacity)
            return false;
        return true;
    }

    @Override
    public int compareTo(Car o) {
        if (engineCapacity < o.engineCapacity) {
            return -1;
        } else if (engineCapacity == o.engineCapacity) {
            return 0;
        } else {
            return 1;
        }
    }

} // end inner class Car