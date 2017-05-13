package DAO;

import base.UserRole;
import base.Users;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UsersDAO extends HibernateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(UsersDAO.class);

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Users createUser(String login, String password, String fullname, UserRole userRole) {
        try {
            begin();
            password = encoder.encode(password);
            Users user = new Users(login, password, fullname, userRole);
            getSession().save(user);
            commit();
            return user;
        } catch (HibernateException e) {
            rollback();
            LOG.error("Cannot create user " + login, e);
            throw new HibernateException(e);
        }
    }

    public Users retrieveUser(String login) throws HibernateException {
        try {
            begin();
            Query query = getSession().createQuery("from Users where login = :login");
            query.setParameter("login", login);
            Users user = (Users) query.uniqueResult();
            commit();
            return user;
        } catch (HibernateException e) {
            rollback();
            LOG.error("Cannot retrieve user " + login, e);
            throw new HibernateException(e);
        }
    }

    public List<Users> retrieveUsersByLogin(String[] logins) {
        try {
            begin();
            Query query = getSession().createQuery("from Users where login in (:logins)");
            query.setParameter("logins", Arrays.asList(logins));
            List<Users> usersList = query.getResultList();
            commit();
            return usersList;
        } catch (HibernateException e) {
            rollback();
            LOG.error("Cannot retrieve users " + Arrays.toString(logins));
            throw new HibernateException(e);
        }
    }

    public Users retrieveUserByID(long userID) {
        try {
            begin();
            Query query = getSession().createQuery("from Users where userID=:userID");
            query.setParameter("userID", userID);
            Users user = (Users) query.uniqueResult();
            commit();
            return user;
        } catch (HibernateException e) {
            rollback();
            LOG.error("Cannot retrieve user " + userID);
            throw new HibernateException(e);
        }
    }

    public List<Users> retrieveAllUsers() throws HibernateException {
        try {
            begin();
            Query query = getSession().createQuery("from Users");
            List<Users> usersList = query.getResultList();
            commit();
            return usersList;
        } catch (HibernateException e) {
            rollback();
            LOG.error("Cannot retrieve all users", e);
            throw new HibernateException(e);
        }
    }

    public String deleteUsers(String... logins) throws HibernateException {
        List<Users> usersList = new ArrayList<Users>();
        for (String login :
                logins) {
            usersList.add(retrieveUser(login));
        }

        try {
            String deletedUsers = "";
            begin();
            for (Users user :
                    usersList) {
                getSession().delete(user);
                deletedUsers += user.getLogin() + "; ";
            }
            commit();
            return deletedUsers.toString();
        } catch (HibernateException e) {
            rollback();
            LOG.error("Cannot delete all users", e);
            throw new HibernateException(e);
        }
    }

    public Users updateUser(long userID, Map<String, String> params) {
        try {
            begin();
            Users user = getSession().load(Users.class, userID);
            user.setFullname(params.get("fullname"));
            String password = params.get("password");
            String password2 = params.get("password2");
            if (password.length() != 0 && password2.length() != 0)
                user.setPassword(encoder.encode(params.get("password")));
            user.setUserRole(UserRole.valueOf(params.get("userrole")));
            getSession().update(user);
            commit();
            return user;
        } catch (HibernateException e) {
            rollback();
            LOG.error("Cannot update user " + userID);
            throw new HibernateException(e);
        }
    }
}
