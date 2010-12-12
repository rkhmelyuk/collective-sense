package collectivesense

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-09
 */
class Rated<T> implements Comparable<Rated<T>> {

    T item
    BigDecimal rate

    int compareTo(Rated<T> o) {
        rate <=> o.rate
    }

    String toString() {
        "$item=$rate"
    }

}
