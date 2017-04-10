package DAO;

import base.Discipline;
import base.Question;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
            LOG.error("Cannot retrive questionID " + questionID);
            throw new HibernateException(e);
        }
    }


}
