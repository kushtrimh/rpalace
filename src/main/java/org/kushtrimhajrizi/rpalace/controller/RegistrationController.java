package org.kushtrimhajrizi.rpalace.controller;

import org.kushtrimhajrizi.rpalace.dto.UserDTO;
import org.kushtrimhajrizi.rpalace.exception.UserAlreadyExistsException;
import org.kushtrimhajrizi.rpalace.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult)
            throws UserAlreadyExistsException {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        userService.save(userDTO);
        return "redirect:/login";
    }
}
