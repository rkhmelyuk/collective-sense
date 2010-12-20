package collectivesense.cluster;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-20
 */
public class HierarchicalCluster {

    BiCluster cluster(Map<String, List> matrix, RowsDistance distance) {
        final MathContext context = new MathContext(10, RoundingMode.HALF_UP);

        Map<List, BigDecimal> distances = new LinkedHashMap<List, BigDecimal>();
        int currentClusterId = -1;

        List<BiCluster> cluster = new ArrayList<BiCluster>();

        int index = 0;
        for (Map.Entry<String, List> entry : matrix.entrySet()) {
            List<BigDecimal> value = new ArrayList<BigDecimal>(entry.getValue().size());
            for (Object val : entry.getValue()) {
                value.add(toBigDecimal(val, context));
            }
            cluster.add(new BiCluster(index++, value));
        }

        while (cluster.size() > 1) {
            List<Integer> lowestPair = Arrays.asList(0, 1);
            BigDecimal closest = distance.calculate(cluster.get(0).getVec(), cluster.get(1).getVec());

            for (int i = 0; i < cluster.size(); i++) {
                for (int j = i+1; j < cluster.size(); j++) {
                    List key = Arrays.asList(cluster.get(i).getVec(), cluster.get(j).getVec());
                    if (!distances.containsKey(key)) {
                        distances.put(key, distance.calculate(cluster.get(i).getVec(), cluster.get(j).getVec()));
                    }

                    BigDecimal d = distances.get(key);
                    if (d.compareTo(closest) < 0) {
                        closest = d;
                        lowestPair = Arrays.asList(i, j);
                    }
                }
            }

            List<BigDecimal> mergeVec = new ArrayList<BigDecimal>();
            for (int k = 0; k < cluster.get(0).getVec().size(); k++) {
                BigDecimal val = cluster.get(lowestPair.get(0)).getVec().get(k)
                        .add(cluster.get(lowestPair.get(1)).getVec().get(k))
                        .divide(new BigDecimal(2), context);
                mergeVec.add(val);
            }

            int lowestPair0 = lowestPair.get(0);
            int lowestPair1 = lowestPair.get(1);

            BiCluster newCluster = new BiCluster(
                    currentClusterId, mergeVec,
                    cluster.get(lowestPair0),
                    cluster.get(lowestPair1),
                    closest);

            currentClusterId--;
            cluster.remove(lowestPair1);
            cluster.remove(lowestPair0);
            cluster.add(newCluster);
        }

        return cluster.get(0);
    }

    private BigDecimal toBigDecimal(Object value, MathContext context) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Double) {
            return new BigDecimal((Double) value, context);
        }
        if (value instanceof Integer) {
            return new BigDecimal((Integer) value, context);
        }
        return new BigDecimal(value.toString(), context);
    }

    public void print(Map<String, List> matrix, BiCluster cluster) {
        if (cluster != null) {
            List<String> labels = new ArrayList<String>(matrix.keySet());
            printCluster(cluster, labels, 0);
        }
    }

    private void printCluster(BiCluster cluster, List<String> labels, int level) {
        for (int i = 0; i < level; i++) System.out.print(" ");
        if (cluster.getId() < 0) {
            System.out.println("-");
        }
        else {
            System.out.println(labels.get(cluster.getId()));
        }
        if (cluster.getLeft() != null) {
            printCluster(cluster.getLeft(), labels, level + 1);
        }
        if (cluster.getRight() != null) {
            printCluster(cluster.getRight(), labels, level + 1);
        }
    }

}
