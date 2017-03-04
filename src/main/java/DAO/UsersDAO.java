package DAO;

import base.Users;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UsersDAO extends HibernateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(UsersDAO.class);

    public Users createUser(String login, String password, String fullname) throws Exception{
        try{
            begin();
            Users user = new Users(login,password,fullname);
            getSession().save(user);
            commit();
            return user;
        }catch (HibernateException e){
            rollback();
            LOG.error("Cannot create user " + login, e);
            throw new Exception();
        }
    }

    public Users retrieveUser(String login) throws Exception{
        try{
            begin();
            Query query = getSession().createQuery("from Users where login = :login");
            query.setParameter("login",login);
            Users user = (Users)query.uniqueResult();
            commit();
            return user;
        }catch (HibernateException e){
            rollback();
            LOG.error("Cannot retrieve user " + login, e);
            throw new Exception();
        }
    }

    public List<Users> retrieveAllUsers() throws Exception{
        try {
            begin();
            Query query = getSession().createQuery("from Users");
            List<Users> usersList = query.getResultList();
            commit();
            return usersList;
        }catch (HibernateException e){
            rollback();
            LOG.error("Cannot retrieve all users",e);
            throw new Exception();
        }
    }
}
