package base;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Question {
    public Question(){}
    public Question(Discipline discipline, String questionText){
        this.discipline = discipline;
        this.questionText = questionText;
    }

    public Set<Answer> getAnswerSet() {
        return answerSet;
    }

    public void setAnswerSet(Set<Answer> answerSet) {
        this.answerSet = answerSet;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long questionID;

    @ManyToOne
    @JoinColumn(name = "disciplineID")
    private Discipline discipline;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Answer> answerSet = new HashSet<Answer>();

    @Column
    private String questionText;

    protected QuestionType questionType;



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
