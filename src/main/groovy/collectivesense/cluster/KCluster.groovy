package collectivesense.cluster

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-16
 */
class KCluster {

    def cluster(Map<String, List> matrix, RowsDistance distance, int k = 10) {

        def ranges = []

        def rows = new ArrayList(matrix.values())
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
        for (int iteration = 0; iteration < 10000; iteration++) {
            println "Iteration $iteration"

            def bestMatches = (0..<k).collect { [] }

            rows.eachWithIndex { row, j ->
                def bestMatch = 0
                (0..<k).each { i ->
                    def d = distance.calculate(clusters[i], row)
                    if (d < distance.calculate(clusters[bestMatch], row)) {
                        bestMatch = i
                    }
                }
                bestMatches[bestMatch] << j
            }

            if (lastMatches && bestMatches.containsAll(lastMatches) && lastMatches.containsAll(bestMatches)) {
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

        return lastMatches
    }

    def print(Map matrix, def cluster) {
        def keys = new ArrayList(matrix.keySet())
        cluster.each {
            println "$it.size: "
            it.each { id ->
                print keys[id]
                print ','
            }
            println()
        }
    }

}
