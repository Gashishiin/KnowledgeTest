package DAO;

import base.Discipline;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DisciplineDAO extends HibernateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DisciplineDAO.class);

    public Discipline createDiscipline(String disciplineName, long parentDisciplineID)
            throws HibernateException {
        try {
            begin();
            Discipline discipline = new Discipline(disciplineName, parentDisciplineID);
            getSession().save(discipline);
            commit();
            return discipline;
        } catch (HibernateException e) {
            rollback();
            LOG.error("Can not create discipline " + disciplineName, e);
            throw new HibernateException(e);
        }
    }

    public List<Discipline> retrieveDisciplines() throws HibernateException {
        try {
            begin();
            Query query = getSession().createQuery("from Discipline");
            List<Discipline> disciplineList = query.getResultList();
            commit();
            return disciplineList;
        }catch (HibernateException e){
            rollback();
            LOG.error("Cannot retrieve all disciplines",e);
            throw new HibernateException(e);
        }
    }
}
