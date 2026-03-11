package com.klef.fsad.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class ClientDemo 
{
public static void main(String[] args) 
{

Configuration cfg = new Configuration();
cfg.configure();

SessionFactory sf = cfg.buildSessionFactory();
Session session = sf.openSession();

Transaction tx = session.beginTransaction();

Payment p = new Payment();

p.setId(1);
p.setName("Karthik");
p.setDate("2026-03-11");
p.setStatus("SUCCESS");
p.setAmount(5000);

session.save(p);

tx.commit();

session.beginTransaction();

Query q = session.createQuery("delete from Payment where id=:pid");
q.setParameter("pid",1);

int result = q.executeUpdate();

System.out.println("Deleted Records = "+result);

session.getTransaction().commit();

session.close();
sf.close();

}
}