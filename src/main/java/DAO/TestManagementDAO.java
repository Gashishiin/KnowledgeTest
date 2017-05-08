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
            Discipline discipline = new DisciplineDAO().retrieveDiscipline(disciplineID);
            List<Users> usersList = new UsersDAO().retrieveUsersByLogin(logins);
            begin();
            TestManagement testManagement;
            List<TestManagement> testManagementList = new ArrayList<TestManagement>();
            properties.put("Date",new Date().toString());
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

    public List<TestManagement> retrieveAssignmentsByLogin(String login){
        try{
            Users user = new UsersDAO().retrieveUser(login);
            begin();
            Query query = getSession().createQuery("from TestManagement where user = :user");
            query.setParameter("user",user);
            List<TestManagement> assignments = query.getResultList();
            commit();
            return assignments;
        }catch (HibernateException e){
            rollback();
            LOG.error("Cannot retrieve assignments for" + login );
            throw new HibernateException(e);
        }
    }

    public TestManagement retrieveAssignmentByID(long assignmentID){
        try{
            begin();
            Query query = getSession().createQuery("from TestManagement where assignmentID = :assignmentID");
            query.setParameter("assignmentID",assignmentID);
            TestManagement testManagement = (TestManagement)query.uniqueResult();
            commit();
            return testManagement;
        }catch (HibernateException e){
            rollback();
            LOG.error("Cannot retrieve assignment " + assignmentID);
            throw new HibernateException(e);
        }
    }

}
