import org.hibernate.*;
import org.hibernate.query.Query;
import org.hibernate.cfg.Configuration;
import table.User;

import javax.persistence.metamodel.EntityType;

import java.util.Date;
import java.util.Map;

//import all tables


public class Main {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(final String[] args) throws Exception {
        final Session session = getSession();
        try {
            System.out.println("querying all the managed entities...");
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                System.out.println("tables establish success");
                System.out.println("begin to save data");
//                User s = new User(12, "abc", 12, "man", new Date(), "shanghai");
//                session.save(s);
//                // do some work
                tx.commit();
            } catch (Exception e) {
                if (tx!=null) tx.rollback();
                e.printStackTrace();
            } finally {
//                session.close();
            }
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                final Query query = session.createQuery("from " + entityName);
                System.out.println(query.getQueryString());
                System.out.println("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
            }
        } finally {
            session.close();
        }
    }
}