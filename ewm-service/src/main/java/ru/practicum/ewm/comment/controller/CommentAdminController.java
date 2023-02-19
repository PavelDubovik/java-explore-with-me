package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.model.CommentStatus;
import ru.practicum.ewm.comment.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class CommentAdminController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getCommentsByParameters(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) CommentStatus[] statuses,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Getting comments by parameters");
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getCommentsByParameters(userId, eventId, statuses, from, size));
    }

    @PatchMapping
    public ResponseEntity<CommentDto> updateCommentByAdmin(@RequestBody CommentDto commentDto) {
        log.info("Updating comment with id {} by Admin", commentDto.getId());
        return ResponseEntity.status(200).body(commentService.updateCommentByAdmin(commentDto));
    }
}
