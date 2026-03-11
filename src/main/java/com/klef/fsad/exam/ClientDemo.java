package com.klef.fsad.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Client application to demonstrate Hibernate HQL operations on Payment Entity.
 */
public class ClientDemo {

    public static void main(String[] args) {
        // 1. Initialize the SessionFactory using Helper class
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Using try-with-resources carefully for proper resource management
        try (Session session = sessionFactory.openSession()) {
            
            // 2. INSERT MULTIPLE RECORDS
            System.out.println("\n--- 1. Starting Insert Operations ---");
            Transaction tx = session.beginTransaction(); // Start transaction
            
            try {
                // Adjust some dates
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -5);
                Date date1 = cal.getTime();
                cal.add(Calendar.DATE, -10);
                Date date2 = cal.getTime();

                // Create Payment persistent objects
                Payment p1 = new Payment("Alice Smith", new Date(), "Completed", 1500.50);
                Payment p2 = new Payment("Bob Johnson", date1, "Pending", 850.00);
                Payment p3 = new Payment("Charlie Brown", date2, "Failed", 300.25);
                Payment p4 = new Payment("David Lee", new Date(), "Completed", 2100.00);
                
                // Save the objects
                session.persist(p1);
                session.persist(p2);
                session.persist(p3);
                session.persist(p4);
                
                tx.commit(); // Commit insert transaction
                System.out.println("Message: 4 Payment records inserted successfully!");
                
                // 3. READ OPERATION (HQL SELECT) - display data in Table Format
                System.out.println("\n--- 2. Fetching Data using HQL to Display Table ---");
                Query<Payment> selectQuery = session.createQuery("from Payment", Payment.class);
                List<Payment> payments = selectQuery.list();
                
                System.out.println("---------------------------------------------------------------------------------");
                System.out.printf("%-5s | %-20s | %-12s | %-12s | %-10s%n", "ID", "NAME", "DATE", "STATUS", "AMOUNT");
                System.out.println("---------------------------------------------------------------------------------");
                for (Payment p : payments) {
                     String dateStr = p.getDate() != null ? sdf.format(p.getDate()) : "N/A";
                     System.out.printf("%-5d | %-20s | %-12s | %-12s | $%-9.2f%n", 
                             p.getId(), p.getName(), dateStr, p.getStatus(), p.getAmount());
                }
                System.out.println("---------------------------------------------------------------------------------");

                // Store one of the generated IDs to use it in the delete operation (Charlie Brown)
                int pidToDelete = p3.getId();
                
                // 4. DELETE OPERATION USING HQL
                System.out.println("\n--- 3. Starting Delete Operation using HQL ---");
                // Open new transaction for deletion 
                tx = session.beginTransaction(); 
                
                // Use HQL query for deletion
                String hql = "delete from Payment where id = :pid";
                
                // Standard approach for DML statements in Hibernate 6
                MutationQuery query = session.createMutationQuery(hql);
                
                // Use named parameter :pid
                query.setParameter("pid", pidToDelete);
                
                // Execute update to carry out the query operations
                int rowsAffected = query.executeUpdate();
                tx.commit(); // Commit delete transaction
                
                // Print messages to console
                if (rowsAffected > 0) {
                    System.out.println("Message: Payment record with ID " + pidToDelete + " deleted successfully!");
                } else {
                    System.out.println("Message: No Payment record found to delete.");
                }

                // 5. Verify the deletion by showing the table again
                System.out.println("\n--- 4. Fetching Data After Deletion ---");
                List<Payment> remainingPayments = session.createQuery("from Payment", Payment.class).list();
                
                System.out.println("---------------------------------------------------------------------------------");
                System.out.printf("%-5s | %-20s | %-12s | %-12s | %-10s%n", "ID", "NAME", "DATE", "STATUS", "AMOUNT");
                System.out.println("---------------------------------------------------------------------------------");
                for (Payment p : remainingPayments) {
                     String dateStr = p.getDate() != null ? sdf.format(p.getDate()) : "N/A";
                     System.out.printf("%-5d | %-20s | %-12s | %-12s | $%-9.2f%n", 
                             p.getId(), p.getName(), dateStr, p.getStatus(), p.getAmount());
                }
                System.out.println("---------------------------------------------------------------------------------");

            } catch (Exception e) {
                // Rollback if anything fails
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 6. Close SessionFactory properly
            if (sessionFactory != null) {
                sessionFactory.close();
            }
            System.out.println("\n--- SessionFactory properly closed. Execution completed. ---");
        }
    }
}
