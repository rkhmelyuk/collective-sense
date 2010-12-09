package collectivesense

/**
 * The rated item value. It has name and appropriate rate.
 * 
 * @author Ruslan Khmelyuk
 * @created 2010-12-09
 */
class ItemValue {

    String name
    BigDecimal rate

    ItemValue(String name, BigDecimal rate) {
        this.name = name
        this.rate = rate
    }

    String toString() {
        "$name=$rate"
    }
}
