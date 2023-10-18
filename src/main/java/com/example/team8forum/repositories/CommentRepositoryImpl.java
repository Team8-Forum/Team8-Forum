package com.example.team8forum.repositories;

import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.models.Comment;
import com.example.team8forum.repositories.contracts.CommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Comment> findCommentsByPostId(int postId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Comment c WHERE c.post.id = :postId";
            return session.createQuery(hql, Comment.class)
                    .setParameter("postId", postId)
                    .list();
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
    public void create(Comment comment) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }
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
    public void delete(Comment comment) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(comment);
            session.getTransaction().commit();
        }
    }
}
