package base;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long disciplineID;
    @Column(nullable = false)
    private String disciplineName;
    @Column
    private long parentDisciplineID;

    @OneToMany(mappedBy = "discipline",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private Set<Question> questionSet = new HashSet<Question>();

    public Discipline() {
    }

    public Discipline(String disciplineName, long parentDisciplineID){
        this.disciplineName = disciplineName;
        this.parentDisciplineID = parentDisciplineID;
    };

    public long getDisciplineID() {
        return disciplineID;
    }

    public void setDisciplineID(long disciplineID) {
        this.disciplineID = disciplineID;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public long getParentDisciplineID() {
        return parentDisciplineID;
    }

    public void setParentDisciplineID(long parentDisciplineID) {
        this.parentDisciplineID = parentDisciplineID;
    }

}
