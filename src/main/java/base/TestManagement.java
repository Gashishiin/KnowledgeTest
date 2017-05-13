package base;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class TestManagement {

    public TestManagement(){};

    public TestManagement(Users user, Discipline discipline, Map<String, String> properties) {
        this.user = user;
        this.discipline = discipline;
        this.properties = properties;
        isTestDone = false;
        resultScore = 0.0;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long assignmentID;

    @ManyToOne
    @JoinColumn(name = "userID")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "disciplineID")
    private Discipline discipline;

    @ElementCollection
    @MapKeyColumn(name="params")
    private Map<String,String> properties = new HashMap<String, String>();

    @Column
    private Boolean isTestDone;

    @Column
    private double resultScore;

    public Boolean isTestDone() {
        return isTestDone;
    }

    public Boolean getTestDone() {
        return isTestDone;
    }

    public void setTestDone(Boolean testDone) {
        isTestDone = testDone;
    }

    public double getResultScore() {
        return resultScore;
    }

    public void setResultScore(double resultScore) {
        this.resultScore = resultScore;
    }

    public long getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(long assignmentID) {
        this.assignmentID = assignmentID;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
