package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentRequestDto;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentPrivateController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@PathVariable Long userId,
                                                    @RequestBody @Valid CommentRequestDto createCommentDto) {
        log.info("Creating new comment");
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(userId, createCommentDto));
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long userId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        log.info("Getting comments of user with id = {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComments(userId, from, size));
    }

    @GetMapping("/{commentsId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long userId,
                                                     @PathVariable Long commentsId) {
        log.info("Getting comment with id = {} of user with id = {}", commentsId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentById(userId, commentsId));
    }

    @PatchMapping("/{commentsId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long userId,
                                                    @PathVariable Long commentsId,
                                                    @RequestBody CommentRequestDto CommentDto) {
        log.info("Updating comment with id {}", commentsId);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(userId, commentsId, CommentDto));
    }

    @DeleteMapping("/{commentsId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long userId,
                                              @PathVariable Long commentsId) {
        log.info("Deleting comment with id = {} by user with id = {}", commentsId, userId);
        commentService.deleteComment(userId, commentsId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
