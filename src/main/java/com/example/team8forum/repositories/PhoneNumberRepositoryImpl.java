package com.example.team8forum.repositories;

import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.models.PhoneNumber;
import com.example.team8forum.repositories.contracts.PhoneNumberRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhoneNumberRepositoryImpl implements PhoneNumberRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PhoneNumberRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public PhoneNumber getByPhoneNumber(String number) {
        try (Session session = sessionFactory.openSession()) {
            Query<PhoneNumber> query = session.createQuery("from PhoneNumber where phoneNumber = :number",
                    PhoneNumber.class);
            query.setParameter("number", number);
            List<PhoneNumber> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("PhoneNumber", "number", number);
            }
            return result.get(0);
        }
    }

    @Override
    public PhoneNumber getByUserId(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<PhoneNumber> query = session.createQuery("from PhoneNumber where userId = :id",
                    PhoneNumber.class);
            query.setParameter("id", id);
            List<PhoneNumber> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "id", String.valueOf(id));
            }
            return result.get(0);
        }
    }

    @Override
    public PhoneNumber create(PhoneNumber phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(phoneNumber);
            session.getTransaction().commit();
        }
        return phoneNumber;
    }

    @Override
    public void update(PhoneNumber phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(phoneNumber);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(PhoneNumber phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(phoneNumber);
            session.getTransaction().commit();
        }
    }
}
