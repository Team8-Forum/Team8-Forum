package com.example.team8forum.repositories;

import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.models.User;
import com.example.team8forum.models.UserFilterOptions;
import com.example.team8forum.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    @Override
    public List<User> getAll(UserFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getFirstName().ifPresent(value -> {
                filters.add("first_name like :first_name");
                params.put("first_name", String.format("%%%s%%", value));
            });

            filterOptions.getLastName().ifPresent(value -> {
                filters.add("last_name like :last_name");
                params.put("last_name", String.format("%%%s%%", value));
            });

            filterOptions.getEmail().ifPresent(value -> {
                filters.add("email like :email");
                params.put("email", String.format("%%%s%%", value));
            });

            filterOptions.getUsername().ifPresent(value -> {
                filters.add("username like :username");
                params.put("username", String.format("%%%s%%", value));
            });

            StringBuilder queryString = new StringBuilder("from User");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));

            Query<User> query = session.createQuery(queryString.toString(), User.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);

            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "username", username);
            }

            return result.get(0);
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }
    private String generateOrderBy(UserFilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {
            case "first_name":
                orderBy = "first_name";
                break;
            case "last_name":
                orderBy = "last_name";
                break;
            case "email":
                orderBy = "email";
                break;
            case "username":
                orderBy = "username";
                break;
            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}
