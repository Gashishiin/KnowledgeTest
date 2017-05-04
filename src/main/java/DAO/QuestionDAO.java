package DAO;

import base.Answer;
import base.Discipline;
import base.Question;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class QuestionDAO extends HibernateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(QuestionDAO.class);

    public Question createQuestion(long disciplineID, String questionText){
        try{
            begin();
            Discipline discipline = new DisciplineDAO().retrieveDiscipline(disciplineID);
            Question question = new Question(discipline,questionText);
            getSession().save(question);
            commit();
            return question;
        }catch (HibernateException e){
            rollback();
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
}
