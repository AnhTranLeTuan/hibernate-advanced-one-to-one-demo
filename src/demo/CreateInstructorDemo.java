package demo;

import org.hibernate.Session; 

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entity.Instructor;
import entity.InstructorDetail;


public class CreateInstructorDemo {

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
			
			// Start the transaction to insert the object to the table 
			StaticFunctions.saveObjectToDatabase(instructor, session);
			
			System.out.println("\n\n Saved instructor: " + instructor);
		} finally {
			sessionFactory.close();
		}

	}

}
 