package ru.itmentor.spring.boot_security.demo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String showUsers(Model model) {
        log.info("Запрос на отображение юзеров");
        model.addAttribute("users", userService.findAll());
        return "users/index";
    }
    @GetMapping("/{id}")
    public String indexOfUser(@PathVariable("id") long id, Model model) {
        log.info(" юзер с id = {}найден", id);
        model.addAttribute("user", userService.findById(id));
        return "users/show";
    }
    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        log.info("форма для создания юзера");
        return "users/new";
    }
    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("Ошибка при сохранении пользователя с данными: {}", user);
            return "users/new";
        }
        log.info("Юзер успешно сохранен с id{}", user.getId());
        userService.save(user);
        return "redirect:/users";
    }
    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") long id, Model model) {
        log.info("форма для редактирования юзера");
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "users/edit";
    }
    @PutMapping("/{id}/edit")
    public String updateUser(@PathVariable("id") long id, @Valid @ModelAttribute("user") User updatedUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("Ошибка при обновлении пользователя с id = {}: {}", id, updatedUser);
            return "users/edit";
        }
        updatedUser.setId(id);
        userService.update(id, updatedUser);
        log.info("Юзер с id = {} обновлен", id);
        return "redirect:/users";
    }
    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        log.info("пользователь с id = {} удален", id);
        return "redirect:/users";
    }
}
