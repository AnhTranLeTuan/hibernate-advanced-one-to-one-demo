package demo;

import org.hibernate.Session; 


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entity.Instructor;
import entity.InstructorDetail;


public class DeleteInstructorDemo {

	public static void main(String[] args) {
		// Create the Configuration object which can be used to retrieved the SessionFactory object
		/* We would need to call methods for setting the configuration file and the annotated classes (these methods will eventually 
		 return the Configuration object iteslf for easier calling the other set up methods). After having enough information, we can start build 
		 the SessionFactory object. */
		/* The SessionFactory object (long-lived object - only created once) can then be used to create the 
	     Session object (short-lived object - may be created multiple time) */
		Configuration configuration = new Configuration();
		SessionFactory sessionFactory = configuration.configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class).
				addAnnotatedClass(InstructorDetail.class).buildSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		
		try {
			Instructor instructor = new Instructor("David", "Smith", "example@gmail.com");
			InstructorDetail instructorDetail = new InstructorDetail("example.com/DavidSmith", "walking");
			instructor.setInstructorDetail(instructorDetail);
			
			/* I will first save the Instructor object to database, then use its ID to read it from database. The returned object
			 will be the persistent object, which mean if we call the delete method from the session object on it, the coressponding 
			 record on the database will also be deleted. */
			/* Note that the record on the database will only be deleted after we get, commit the transaction */
			/* In this case, because I declared the cascade type of for the relationship between the Instructor and InstructorDetail classes 
			 to be CascadeType.ALL, which also include the delete operation, so when the record of the Instructor object is deleted, the one of
			 the InstructorDetail also will be deleted. */
			session.beginTransaction();
			session.save(instructor);
			session.getTransaction().commit();
			session.close();
			
			System.out.println("Saved/Created student\n" + instructor + "\n\n\n");
			
			// Delete the just-saved student record 
			StaticFunctions.deleteRecordOnDatabaseByPersistentObject(instructor.getId(), Instructor.class, sessionFactory.getCurrentSession());
			
			// Checking by rereading it on the database, which the returned value will be null if it already get deleted
			if (StaticFunctions.readObjectFromDatabaseByPrimaryKey(instructor.getId(), Instructor.class, sessionFactory.getCurrentSession()) == null) {
				System.out.println("The record has been deleted");
			} else {}
			
		} finally {
			sessionFactory.close();
		}

	}

}
 