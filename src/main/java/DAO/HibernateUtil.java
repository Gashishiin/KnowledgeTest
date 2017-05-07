package DAO;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private final static SessionFactory sessionFactory = new Configuration()
            .configure()
            .buildSessionFactory();
    private static final ThreadLocal session = new ThreadLocal();
    private final static Logger LOG = LoggerFactory.getLogger(HibernateUtil.class);

    public static Session getSession(){
        Session session = (Session)HibernateUtil.session.get();
        if (session == null){
            session = sessionFactory.openSession();
            HibernateUtil.session.set(session);
        }
        return session;
    }

    protected void begin(){
        if (!getSession().getTransaction().isActive())
        getSession().beginTransaction();
    }

    protected void commit(){
        getSession().getTransaction().commit();
    }

    protected void rollback(){
        try{
            getSession().getTransaction().rollback();
        }catch (HibernateException e){
            LOG.error("Cannot rollback", e);
        }
        try {
            getSession().close();
        }catch (HibernateException e){
            LOG.error("Cannot close session",e );
        }
        HibernateUtil.session.set(null);
    }
}
