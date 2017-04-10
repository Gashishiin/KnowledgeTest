package base;

import javax.persistence.*;

@Entity
public class Answer {
    public Answer(){}
    public Answer(Question question, String answerText, boolean isCorrect){
        this.question = question;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long answerID;

    @ManyToOne
    @JoinColumn(name = "questionID")
    private Question question;

    @Column
    private boolean isCorrect;

    @Column
    private String answerText;

    public long getAnswerID() {
        return answerID;
    }

    public void setAnswerID(long answerID) {
        this.answerID = answerID;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
