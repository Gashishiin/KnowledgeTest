package DAO;

import base.Answer;
import base.Discipline;
import base.Question;
import base.QuestionType;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class QuestionDAO extends HibernateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(QuestionDAO.class);

    public Question createQuestion(long disciplineID, String questionText, String[] answerTexts, String[] checks){
        try{
            Discipline discipline = new DisciplineDAO().retrieveDiscipline(disciplineID);
            begin();
            Question question = new Question(discipline,questionText);
            Answer answer;
            int correctAnswer = 0;
            boolean isCorrect;
            for (int i = 0; i < answerTexts.length; i++) {
                isCorrect = Boolean.valueOf(checks[i]);
                if (isCorrect) correctAnswer++;
                answer = new Answer(question,answerTexts[i],isCorrect);
                getSession().save(answer);
            }
            if (correctAnswer == 1) {
                question.setQuestionType(QuestionType.SINGLE_CHOICE);
            }
            else if (correctAnswer == 0){
                question.setQuestionType(QuestionType.FREE);
            }
            else {
                question.setQuestionType(QuestionType.MULTI_CHOICE);
            }
            getSession().save(question);
            commit();
            return question;
        }catch (HibernateException e){
            rollback();
            System.out.println("Cannot create question");
            LOG.error("Can not create question " + questionText);
            throw new HibernateException(e);
        }
    }

    public Question retrieveQuestion(long questionID){
        try{
            begin();
            Query query = getSession().createQuery("from Question where questionID = :questionID");
            query.setParameter("questionID",questionID);
            Question question = (Question)query.uniqueResult();
            commit();
            return question;
        }catch (HibernateException e){
            rollback();
            LOG.error("Cannot retrieve questionID " + questionID);
            throw new HibernateException(e);
        }
    }

    public List<Question> retrieveQuestionByDiscipline(long disciplineID){
        List<Question> questionList = new ArrayList<Question>();
        try{
            begin();
            Query query = getSession().createQuery("from Question where discipline.disciplineID = :disciplineID");
            query.setParameter("disciplineID",disciplineID);
            questionList = query.getResultList();
            commit();
            return questionList;
        }catch (HibernateException e){
            rollback();
            LOG.error("Cannot retieve questions by discipline " + disciplineID);
            throw new HibernateException(e);
        }
    }

    public List<Answer> retrieveAnswers(long questionID){
        try{
            begin();
            Query query = getSession().createQuery("from Answer  where question.questionID = :questionID");
            query.setParameter("questionID",questionID);
            List<Answer> answerList = query.getResultList();
            commit();
            return answerList;
        }catch (HibernateException e){
            rollback();
            LOG.error("Cannot retrieve answers of question " + questionID) ;
            throw new HibernateException(e);
        }
    }
    
    public void deleteQuestions(long[] questionIDs){
        try{
            List<Question> questions = new ArrayList<Question>();
            for (long id :
                    questionIDs) {
                    questions.add(retrieveQuestion(id));
            }
            begin();
            for (Question q :
                    questions) {
                getSession().delete(q);
            }
            commit();
        }catch (HibernateException e){
            rollback();
            LOG.error("Can not delete questions" + Arrays.toString(questionIDs));
        }
    }
}
