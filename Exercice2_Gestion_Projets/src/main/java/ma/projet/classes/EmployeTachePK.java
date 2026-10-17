package ma.projet.classes;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmployeTachePK implements Serializable {
    private Integer employeId;
    private Integer tacheId;

    public EmployeTachePK() {
    }

    public EmployeTachePK(Integer employeId, Integer tacheId) {
        this.employeId = employeId;
        this.tacheId = tacheId;
    }

    public Integer getEmployeId() { return employeId; }
    public void setEmployeId(Integer employeId) { this.employeId = employeId; }
    public Integer getTacheId() { return tacheId; }
    public void setTacheId(Integer tacheId) { this.tacheId = tacheId; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof EmployeTachePK)) return false;
        EmployeTachePK other = (EmployeTachePK) object;
        return Objects.equals(employeId, other.employeId) && Objects.equals(tacheId, other.tacheId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeId, tacheId);
    }
}
