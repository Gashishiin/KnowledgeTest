package base;

import javax.persistence.*;

@Entity
public class Question {
    public Question(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long questionID;

    @ManyToOne
    @JoinColumn(name = "disciplineID")
    private Discipline discipline;

    public long getQuestionID() {
        return questionID;
    }

    public void setQuestionID(long questionID) {
        this.questionID = questionID;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
}
