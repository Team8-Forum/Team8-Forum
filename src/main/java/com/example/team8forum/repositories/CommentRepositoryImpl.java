package com.example.team8forum.repositories;

import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.models.Comment;
import com.example.team8forum.models.Post;
import com.example.team8forum.repositories.contracts.CommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Comment get(String comment) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery("from Comment where comment = :comment", Comment.class);
            query.setParameter("comment", comment);

            List<Comment> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Comment", "comment", comment);
            }

            return result.get(0);
        }
    }
    @Override
    public Set<Comment> findCommentsByPostId(int postId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT c FROM Comment c JOIN c.post p WHERE p.id= :postId";
            Query<Comment> query = session.createQuery(hql, Comment.class);
            query.setParameter("postId", postId);
            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public Comment findCommentById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Comment comment = session.get(Comment.class, id);
            if (comment == null) {
                throw new EntityNotFoundException("Comment", id);
            }
            return comment;
        }
    }

    @Override
    public Comment create(Comment comment) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }
        return comment;
    }

    @Override
    public void update(Comment comment) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Comment commentToDelete = findCommentById(id);
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(commentToDelete);
            session.getTransaction().commit();
        }
    }
}
