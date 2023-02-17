package ru.practicum.ewm.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.comment.dto.CommentPublicDto;
import ru.practicum.ewm.comment.dto.CommentRequestDto;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "event.id", source = "event")
    Comment toComment(CommentRequestDto commentRequestDto);

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "author", source = "author.id")
    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDto(List<Comment> comments);

    List<CommentPublicDto> toCommentPublicDto(List<Comment> comments);
}
