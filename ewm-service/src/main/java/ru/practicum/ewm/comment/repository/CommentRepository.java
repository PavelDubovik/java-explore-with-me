package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentStatus;

import java.util.List;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {
    List<Comment> findAllByAuthor_IdOrderByCreatedDesc(Long userId, Pageable pageable);

    Comment findByIdAndAuthor_Id(Long commentId, Long userId);

    Comment findByIdAndEvent_IdAndStatus(Long commentId, Long eventId, CommentStatus commentStatus);

    Comment findByIdAndAuthor_IdAndEvent_Id(Long commentId, Long userId, Long eventId);

    void deleteById(Long id);
}