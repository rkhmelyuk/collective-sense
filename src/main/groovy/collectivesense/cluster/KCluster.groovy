package collectivesense.cluster

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-16
 */
class KCluster {

    def cluster(Map<String, List> matrix, int k = 10) {

        def ranges = []

        def rows = matrix.values().toList()
        def size = rows[0].size()
        for (int i = 0; i < size; i++) {
            def rowValues = rows.collect { it[i] }
            def min = rowValues.min()
            def max = rowValues.max()
            ranges << [min, max]
        }

        def clusters = (0..<k).collect {
            ranges.collect { range ->
                Math.random() * (range[1] - range[0]) + range[0]
            }
        }

        def lastMatches = null
        for (int i = 0; i < 1000; i++) {
            println "Iteration $i"

            def bestMatches = (0..<k).collect { [] }

            rows.eachWithIndex { row, index ->
                def bestMatch = 0
                (0..<k).each {
                    def d = distance(clusters[it], row)
                    if (d < distance(clusters[bestMatch], row)) {
                        bestMatch = it
                    }
                }
                bestMatches[bestMatch] << index
            }

            if (bestMatches == lastMatches) {
                break
            }

            lastMatches = bestMatches

            for (int m = 0; m < k; m++) {
                def avgs = (0..<size).collect { 0.0 }
                if (bestMatches[m].size() > 0) {
                    bestMatches[m].each { rowId ->
                        for (int mm = 0; mm < rows[rowId].size(); mm++) {
                            avgs[mm] += rows[rowId][mm]
                        }
                    }
                    for (int mj = 0; mj < size; mj++) {
                        avgs[mj] /= bestMatches[m].size()
                    }
                    clusters[m] = avgs
                }
            }
        }

        lastMatches.each {
            println "$it.size: $it"
        }
    }

    def distance(List row1, List row2) {

        def n = 0
        def sum1 = 0.0
        def sum2 = 0.0
        def sum1Sq = 0.0
        def sum2Sq = 0.0
        def pSum = 0.0
        for (int i = 0; i < row1.size(); i++) {
            def rate1 = row1[i]
            def rate2 = row2[i]

            sum1 += rate1
            sum2 += rate2
            sum1Sq += rate1 ** 2
            sum2Sq += rate2 ** 2
            pSum += rate1 * rate2
            n++
        }

        if (n == 0) {
            return 0
        }

        def den = Math.sqrt((double) (sum1Sq - (sum1 ** 2) / n) * (sum2Sq - (sum2 ** 2) / n))
        if (den == 0) {
            return 0
        }

        return (pSum - (sum1 * sum2 / n)) / den
    }

}
