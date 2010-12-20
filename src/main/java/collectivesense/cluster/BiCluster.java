package collectivesense.cluster;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-20
 */
public class BiCluster {

    private int id;
    private List<BigDecimal> vec;
    private BiCluster left;
    private BiCluster right;
    private BigDecimal distance;

    public BiCluster(int id, List<BigDecimal> vec) {
        this.id = id;
        this.vec = vec;
    }

    public BiCluster(int id, List<BigDecimal> vec, BiCluster left, BiCluster right, BigDecimal distance) {
        this.id = id;
        this.vec = vec;
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public List<BigDecimal> getVec() {
        return vec;
    }

    public BiCluster getLeft() {
        return left;
    }

    public BiCluster getRight() {
        return right;
    }

    public BigDecimal getDistance() {
        return distance;
    }
}
