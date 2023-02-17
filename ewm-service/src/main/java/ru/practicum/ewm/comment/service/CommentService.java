package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentPublicDto;
import ru.practicum.ewm.comment.dto.CommentRequestDto;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.model.CommentStatus;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long userId, CommentRequestDto commentRequestDto);

    List<CommentDto> getComments(Long userId, int from, int size);

    CommentDto getCommentById(Long userId, Long commentsId);

    CommentDto updateComment(Long userId, Long commentsId, CommentRequestDto commentDto);

    void deleteComment(Long userId, Long commentsId);

    List<CommentDto> getCommentsByParameters(Long userId, Long eventId, CommentStatus[] statuses, int from, int size);

    CommentDto updateCommentByAdmin(CommentDto commentDto);

    List<CommentPublicDto> getCommentsByEvent(Long eventId, int from, int size);
}
