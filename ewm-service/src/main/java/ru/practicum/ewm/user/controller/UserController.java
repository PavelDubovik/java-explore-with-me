package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Creating new user {}", newUserRequest);
        return ResponseEntity.status(201).body(userService.createUser(newUserRequest));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam List<Long> ids,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.status(200).body(userService.getUsers(ids, from, size));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        log.info("Deleting user with id = {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.status(204).build();
    }
}
