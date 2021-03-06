package DAO;

import base.Answer;
import base.Discipline;
import base.Question;
import base.QuestionType;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class QuestionDAO extends HibernateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(QuestionDAO.class);

    public Question createQuestion(long disciplineID, String questionText, String[] answerTexts, String[] checks){
        try{

            begin();
            Discipline discipline = getSession().load(Discipline.class, disciplineID);
            Question question = new Question(discipline,questionText);
            Set<Answer> answerSet = new HashSet<Answer>();
            int correctAnswer = 0;
            boolean isCorrect;
            for (int i = 0; i < answerTexts.length; i++) {
                isCorrect = Boolean.valueOf(checks[i]);
                if (isCorrect) correctAnswer++;
                answerSet.add(new Answer(question,answerTexts[i],isCorrect));
            }
            if (correctAnswer > 1) {
                question.setQuestionType(QuestionType.MULTI_CHOICE);
            }
            else if (correctAnswer == 1){
                if (answerSet.size() > 1) question.setQuestionType(QuestionType.SINGLE_CHOICE);
                else question.setQuestionType(QuestionType.FREE);
            }
            question.setAnswerSet(answerSet);
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
        List<Question> questionList;
        try{
            begin();
            Query query = getSession().createQuery("from Question where discipline.disciplineID = :disciplineID");
            query.setParameter("disciplineID",disciplineID);
            questionList = query.getResultList();
            commit();
            return questionList;
        }catch (HibernateException e){
            rollback();
            LOG.error("Cannot retrieve questions by discipline " + disciplineID);
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

    public Answer retrieveAnswer(long answerID){
        try{
            begin();
            Query query = getSession().createQuery("from Answer where  answerID = :answerID");
            query.setParameter("answerID", answerID);
            Answer answer = (Answer)query.uniqueResult();
            commit();
            return answer;
        }catch (HibernateException e){
            rollback();
            LOG.error("Cannot retrieve andswer " + answerID);
            throw new HibernateException(e);
        }
    }
    
    public void deleteQuestions(long[] questionIDs){
        try{
            begin();
            List<Question> questions = new ArrayList<Question>();
            for (long id :
                    questionIDs) {
                questions.add(getSession().load(Question.class, id));
            }
            for (Question q :
                    questions) {
                getSession().delete(q);
            }
            commit();
        }catch (HibernateException e){
            rollback();
            LOG.error("Can not delete questions" + Arrays.toString(questionIDs));
            throw new HibernateException(e);
        }
    }

    public void updateQuestion(long questionID, String questionText, String[] answerTexts, String[] checks){
        try{
            begin();
            Question question = getSession().load(Question.class, questionID);
            boolean isCorrect;
            int correctAnswer = 0;
            Set<Answer> answerSet = new HashSet<Answer>();
            for (int i = 0; i < answerTexts.length; i++) {
                isCorrect = Boolean.valueOf(checks[i]);
                if (isCorrect) correctAnswer++;
                answerSet.add(new Answer(question,answerTexts[i],isCorrect));
            }
            if (correctAnswer > 1) {
                question.setQuestionType(QuestionType.MULTI_CHOICE);
            }
            else if (correctAnswer == 1){
                if (answerSet.size() > 1) question.setQuestionType(QuestionType.SINGLE_CHOICE);
                else question.setQuestionType(QuestionType.FREE);
            }
            question.setQuestionText(questionText);
            question.getAnswerSet().clear();
            question.getAnswerSet().addAll(answerSet);
            getSession().update(question);
            getSession().flush();
            commit();
        }catch (HibernateException e){
            rollback();
            LOG.error("Cannot update question " + questionID);
            throw new HibernateException(e);
        }

    }
}
