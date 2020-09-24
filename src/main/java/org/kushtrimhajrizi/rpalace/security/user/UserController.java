package org.kushtrimhajrizi.rpalace.security.user;

import org.kushtrimhajrizi.rpalace.exception.UserAlreadyExistsException;
import org.kushtrimhajrizi.rpalace.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid UserDTO userDTO, BindingResult bindingResult)
            throws UserAlreadyExistsException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        userService.save(userDTO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/private")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public String getPrivate() {
        return "OK";
    }
}
