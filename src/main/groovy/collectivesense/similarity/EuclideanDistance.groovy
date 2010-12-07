package collectivesense.similarity

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-08
 */
class EuclideanDistance {

    def calculate(Map item1, Map item2) {
        def containsAny = false
        for (def key in item1.keySet()) {
            if (item2.containsKey(key)) {
                containsAny = true
                break
            }
        }

        if (!containsAny) {
            return 0
        }

        def sum = 0
        for (def entry in item1.entrySet()) {
            def value1 = entry.value
            def value2 = item2.get(entry.key)

            sum += (value1 - value2) ** 2
        }

        return 1 / (1 + sum)
    }

    static void main(String... args) {
        def users = [
                'John Doe': [test1: 3, test2: 4, test3: 4],
                'Mark White': [test1: 2, test2: 3, test3: 4],
                'Mary Black': [test1: 3, test2: 4, test3: 5],
                'John Smith': [test1: 1, test2: 4, test3: 3]
        ]

        def instance = new EuclideanDistance()
        for (def entry1 in users.entrySet()) {
            for (def entry2 in users.entrySet()) {
                if (entry1.key != entry2.key) {
                    println "$entry1.key <=> $entry2.key  \t -->   " +
                        instance.calculate(entry1.value, entry2.value)
                }
            }
            println '---------------------'
        }
    }
}
