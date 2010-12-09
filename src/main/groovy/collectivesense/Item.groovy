package collectivesense

/**
 * Represents the item or person that we will calculate similarity for.
 * 
 * @author Ruslan Khmelyuk
 * @created 2010-12-09
 */
class Item {

    String name
    List<ItemValue> values

    boolean containsSimilarValues(Item item) {
        for (def value in values) {
            if (item.containsValueWithName(value.name)) {
                return true
            }
        }
        return false
    }

    boolean containsValueWithName(String name) {
        for (value in values) {
            if (value.name == name) {
                return true
            }
        }
        return false
    }

    ItemValue getValueByName(String name) {
        for (value in values) {
            if (value.name == name) {
                return value
            }
        }
        return null
    }

    String toString() {
        name
    }
}
