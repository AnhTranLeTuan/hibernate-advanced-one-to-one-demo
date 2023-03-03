package demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entity.Instructor;
import entity.InstructorDetail;

public class BidirectionalRelationshipDemo {
	public static void main(String[] args) {
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
			
			System.out.println("\n\n Saved instructor: " + instructor + "\n\n");
			
			// The InstructorDetail object obtained based on the saved Instructor object
			InstructorDetail returnedObject = StaticFunctions.readObjectFromDatabaseByPrimaryKey(instructor.getInstructorDetail().getId(), 
					InstructorDetail.class, sessionFactory.getCurrentSession());
			if (returnedObject == null) {
				
			} else {
			/* The Instructor object from the returned InstructorDetail object, which will be the same with the just-saved Instructor object, 
			 indicating bidirectional relaptionship */
			System.out.println("The instructor associated with the return InstructorDetail object:\n" + returnedObject.getInstructor() + "\n\n\n");
			
			/* I will try to delete the InstructorDetail object to verify the cascade effect on the bidirectional relationship which mean the
			 associated Instructor object will also be deleted */
			/* Note that we can break the bidirectional relationship by setting the value of the associated Instructor object's instructorDetail field
             to null. As a result, the cascade effect of the InstructorDetail object won't apply to the Instructor object, making it won't be deleted */
 			StaticFunctions.deleteRecordOnDatabaseByPersistentObject(returnedObject.getId(), InstructorDetail.class, sessionFactory.getCurrentSession());
			}
			
			// Rereading the associated Instructor object to check 
			Instructor returnedObject2 = StaticFunctions.readObjectFromDatabaseByPrimaryKey(instructor.getId(), 
					Instructor.class, sessionFactory.getCurrentSession());
			if(returnedObject2 == null) {
				System.out.println("The Instructor object associated with the return InstructorDetail object will also be deleted when we"
						+ " delete the InstructorDetail object");
			} else {
				
			}
			
			
		} finally{
			sessionFactory.close();
		}
		
	}
	
}
