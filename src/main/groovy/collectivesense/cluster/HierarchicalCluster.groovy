package collectivesense.cluster

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-18
 */
class HierarchicalCluster {

    def cluster(Map<String, List> matrix, RowsDistance distance, int k = 10) {
        def distances = [:]
        def currentClusterId = -1

        def cluster = [] as ArrayList
        matrix.eachWithIndex { entry, index -> cluster << new BiCluster(vec: entry.value, id: index) }

        while (cluster.size() > 1) {
            def lowestPair = [0, 1] as ArrayList
            def closest = distance.calculate(cluster[0].vec, cluster[1].vec)

            for (int i = 0; i < cluster.size(); i++) {
                for (int j = i + 1; j < cluster.size(); j++) {
                    def key = [cluster[i].id, cluster[j].id]
                    if (!distances.containsKey(key)) {
                        distances[key] = distance.calculate(cluster[i].vec, cluster[j].vec)
                    }

                    def d = distances[key]

                    if (d < closest) {
                        closest = d
                        lowestPair = [i, j] as ArrayList
                    }
                }
            }

            def mergeVec = [] as ArrayList
            (cluster[0].vec.size()).times { i ->
                mergeVec << ((BigDecimal) (cluster[lowestPair[0]].vec[i] + cluster[lowestPair[1]].vec[i])) / 2
            }

            def newCluster = new BiCluster(vec: mergeVec,
                    id: currentClusterId, distance: closest,
                    left: cluster[lowestPair[0]],
                    right: cluster[lowestPair[1]])

            currentClusterId--
            cluster.remove(lowestPair[1])
            cluster.remove(lowestPair[0])
            cluster << newCluster
        }

        return cluster[0]
    }

    def print(Map matrix, def cluster) {
        if (cluster) {
            def labels = new ArrayList(matrix.keySet())
            printCluster(cluster, labels, 0)
        }
    }

    private void printCluster(BiCluster cluster, List labels, int level) {
        for (int i = 0; i < level; i++) print ' '
        if (cluster.id < 0) {
            println '-'
        }
        else {
            println labels[cluster.id]
        }
        if (cluster.left) {
            printCluster(cluster.left, labels, level + 1)
        }
        if (cluster.right) {
            printCluster(cluster.right, labels, level + 1)
        }
    }

}
