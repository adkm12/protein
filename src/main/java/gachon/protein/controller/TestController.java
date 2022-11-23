package gachon.protein.controller;


import gachon.protein.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping(value = "/test")
    public UserDto test() {
        UserDto userDto = new UserDto();
        userDto.setAge(20);
        userDto.setName("양현호");

        return userDto;
    }
}
