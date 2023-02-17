package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentPublicDto;
import ru.practicum.ewm.comment.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events/{eventId}/comments")
@RequiredArgsConstructor
public class CommentPublicController {
    private  final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentPublicDto>> getComments(@PathVariable Long eventId,
                                                              @RequestParam(defaultValue = "0") int from,
                                                              @RequestParam(defaultValue = "10") int size) {
        log.info("Getting APPROVED comments of event with id {}", eventId);
        return ResponseEntity.status(200).body(commentService.getCommentsByEvent(eventId, from, size));
    }
}
