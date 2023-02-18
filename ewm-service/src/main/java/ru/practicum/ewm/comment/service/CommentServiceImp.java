package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.dto.CommentPublicDto;
import ru.practicum.ewm.comment.dto.CommentRequestDto;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.QComment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.comment.model.CommentStatus;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.util.QPredicates;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.querydsl.core.types.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto createComment(Long userId, CommentRequestDto commentRequestDto) {
        User user = userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(commentRequestDto.getEvent()).orElseThrow();
        Comment comment = commentMapper.toComment(commentRequestDto);
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        comment.setStatus(CommentStatus.WAITING);
        Comment savedComment = commentRepository.save(comment);
        log.info("Comment with {} created", comment.getId());
        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    public List<CommentDto> getComments(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findAllByAuthor_IdOrderByCreatedDesc(userId, pageable);
        log.info("Comment of user with id = {} got", userId);
        return commentMapper.toCommentDto(comments);
    }

    @Override
    public List<CommentPublicDto> getCommentsByEvent(Long eventId, int from, int size) {
        List<Comment> comments;
        PageRequest pageable = PageRequest.of(from, size, Sort.by("created").descending());
        QComment comment = QComment.comment;
        Predicate predicate = QPredicates.builder()
                .add(eventId, comment.event.id::in)
                .add(CommentStatus.APPROVED, comment.status::eq)
                .buildAnd();
        comments = commentRepository.findAll(predicate, pageable).toList();
        log.info("Comments by parameters got");
        return commentMapper.toCommentPublicDto(comments);
    }

    @Override
    public CommentDto getCommentById(Long userId, Long commentsId) {
        Comment comment = commentRepository.findByIdAndAuthor_Id(commentsId, userId);
        Optional.ofNullable(comment).orElseThrow(() ->
                new NoSuchElementException(String
                        .format("Comment with id %d and user id %d not found", commentsId, userId)));
        log.info("Comment with id = {} of user with id = {} got", commentsId, userId);
        return commentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentsId, CommentRequestDto updateComment) {
        Comment comment = Optional.ofNullable(commentRepository.findByIdAndAuthor_Id(commentsId, userId))
                .orElseThrow(() ->
                        new NoSuchElementException(String
                                .format("Comment with id %d and user id %d not found", commentsId, userId)));
        comment.setText(updateComment.getText());
        comment.setStatus(CommentStatus.WAITING);
        Comment updatedComment = commentRepository.save(comment);
        log.info("Comment with id = {} updated", commentsId);
        return commentMapper.toCommentDto(updatedComment);
    }

    @Override
    public void deleteComment(Long userId, Long commentsId) {
        Comment comment = commentRepository.findByIdAndAuthor_Id(commentsId, userId);
        Optional.ofNullable(comment).orElseThrow(() ->
                new NoSuchElementException(String
                        .format("Comment with id %d and user id %d not found", commentsId, userId)));
        commentRepository.deleteById(commentsId);
        log.info("Comment with id = {} deleted", commentsId);
    }

    @Override
    public List<CommentDto> getCommentsByParameters(Long userId, Long eventId, CommentStatus[] statuses, int from, int size) {
        List<Comment> comments;
        QComment comment = QComment.comment;
        Predicate predicate = QPredicates.builder()
                .add(userId, comment.author.id::eq)
                .add(eventId, comment.event.id::eq)
                .add(statuses, comment.status::in)
                .buildAnd();

        PageRequest pageable = PageRequest.of(from, size, Sort.by("created").descending());
        if (predicate == null) {
            comments = commentRepository.findAll(pageable).toList();
        } else {
            comments = commentRepository.findAll(predicate, pageable).toList();
        }
        log.info("Comments by parameters got");
        return commentMapper.toCommentDto(comments);
    }

    @Override
    public CommentDto updateCommentByAdmin(CommentDto commentDto) {
        Comment comment = Optional.ofNullable(commentRepository
                        .findByIdAndAuthor_IdAndEvent_Id(commentDto.getId(), commentDto.getAuthor(),
                                commentDto.getEvent()))
                .orElseThrow(() ->
                        new NoSuchElementException(String
                                .format("Comment with id %d and user id %d not found",
                                        commentDto.getId(), commentDto.getAuthor())));
        if (commentDto.getStatus().equals(CommentStatus.APPROVED)) {
            if (comment.getStatus().equals(CommentStatus.WAITING)) {
                comment.setStatus(CommentStatus.APPROVED);
            } else {
                throw new IllegalStateException("APPROVED could be only comments with status WAITING");
            }
        } else if (commentDto.getStatus().equals(CommentStatus.REJECTED)) {
            comment.setStatus(CommentStatus.REJECTED);
        }
        Comment updatedComment = commentRepository.save(comment);
        log.info("Status comment with id {} updated to {}", updatedComment.getId(), updatedComment.getStatus().name());
        return commentMapper.toCommentDto(updatedComment);
    }
}
