package com.example.fleamarket.controller;

import com.example.fleamarket.constant.SessionConst;
import com.example.fleamarket.dto.user.UserDto;
import com.example.fleamarket.dto.user.UserResponseDto;
import com.example.fleamarket.entity.User;
import com.example.fleamarket.exception.UserException;
import com.example.fleamarket.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserService userService;

    @ResponseBody
    @PostMapping("/signup")
    public UserResponseDto signup(@RequestBody UserDto userDto, HttpServletRequest request) {
        try {
            UserResponseDto user = userService.signup(userDto);
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_USER, user);
            return user;
        } catch (Exception e) {
            log.info("", e);
            return new UserResponseDto(null, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/login")
    public UserResponseDto login(@RequestBody UserDto userDto, HttpServletRequest request) {
        if (request.isRequestedSessionIdValid()) {
            return (UserResponseDto) request.getAttribute(SessionConst.LOGIN_USER);
        }
        try {
            UserResponseDto user = userService.login(userDto);
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_USER, user);
            return user;
        } catch (Exception e) {
            log.info("", e);
            return new UserResponseDto(null, e.getMessage());
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request) {
        if (request.isRequestedSessionIdValid()) {
            session.invalidate();
            return SessionConst.LOGOUT_SUCCESS;
        }
        return SessionConst.NOT_LOGIN_STATE;
    }

    @ResponseBody
    @GetMapping("/info")
    public User getUserInfo(@RequestParam Long userIdx) {
        try {
            return userService.getUserInfo(userIdx);
        } catch (UserException e) {
            log.info("", e);
            return null;
        }
    }
}
