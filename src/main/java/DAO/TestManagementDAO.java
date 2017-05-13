package DAO;

import base.Discipline;
import base.TestManagement;
import base.Users;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TestManagementDAO extends HibernateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(TestManagementDAO.class);

    public List<TestManagement> createTestAssignments(long disciplineID, String[] logins, Map<String, String> properties) {
        try {
            List<Users> usersList = new UsersDAO().retrieveUsersByLogin(logins);
            begin();
            Discipline discipline = getSession().load(Discipline.class, disciplineID);
            TestManagement testManagement;
            List<TestManagement> testManagementList = new ArrayList<TestManagement>();
            properties.put("Date", new Date().toString());
            for (Users user :
                    usersList) {
                testManagement = new TestManagement(user, discipline, properties);
                getSession().save(testManagement);
                testManagementList.add(testManagement);
            }
            commit();
            return testManagementList;
        } catch (HibernateException e) {
            rollback();
            LOG.error("Cannot assign tests on " + disciplineID + " for " + Arrays.toString(logins));
            throw new HibernateException(e);
        }
    }

    public List<TestManagement> retrieveAssignmentsByLogin(String login, Boolean isTestDone) {
        try {
            begin();
            Query query;
            if (isTestDone == null)
                query = getSession().createQuery("from TestManagement where user.login = :login and isTestDone is null ");
            else {
                query = getSession().createQuery("from TestManagement where user.login = :login and isTestDone=:isTestDone");
                query.setParameter("isTestDone", isTestDone);
            }
            query.setParameter("login", login);
            List<TestManagement> assignments = query.getResultList();
            commit();
            return assignments;
        } catch (HibernateException e) {
            rollback();
            LOG.error("Cannot retrieve assignments for" + login);
            throw new HibernateException(e);
        }
    }

    public TestManagement retrieveAssignmentByID(long assignmentID) {
        try {

            return getSession().get(TestManagement.class,assignmentID);
        } catch (HibernateException e) {
            rollback();
            LOG.error("Cannot retrieve assignment " + assignmentID);
            throw new HibernateException(e);
        }

    }

    public TestManagement updateResults(long assignmentID, double score, Boolean isTestDone) {
        try {
            begin();
            TestManagement test = getSession().load(TestManagement.class,assignmentID);
            test.setTestDone(isTestDone);
            test.setResultScore(score);
            test.getProperties().put("Date", new Date().toString());
            getSession().update(test);
            commit();
            return test;
        } catch (HibernateException e) {
            rollback();
            LOG.error("Cannot update results for test " + assignmentID);
            throw new HibernateException(e);
        }
    }

    public List<TestManagement> retrieveAssignmentsByUserID(long userID) {
        try {
            begin();
            Query query = getSession().createQuery("from TestManagement where user.userID = :userID");
            query.setParameter("userID", userID);
            List<TestManagement> assignments = query.getResultList();
            for (TestManagement test :
                    assignments) {
                getSession().refresh(test);
            }
            commit();
            return assignments;
        } catch (HibernateException e) {
            rollback();
            LOG.error("Cannot retrieve assignments for" + userID);
            throw new HibernateException(e);
        }
    }

    public String deleteAssignment(long assignmentID){
        try{
            begin();
            TestManagement assignment = getSession().load(TestManagement.class,assignmentID);
            getSession().delete(assignment);
            commit();
            return "Deleted" + assignmentID;
        }catch (HibernateException e){
            rollback();
            LOG.error("Cannot delete assignment " + assignmentID);
            throw new HibernateException(e);
        }
    }
}
