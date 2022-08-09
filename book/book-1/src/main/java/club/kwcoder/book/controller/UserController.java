package club.kwcoder.book.controller;

import club.kwcoder.book.builder.MapBuilder;
import club.kwcoder.book.dataobject.User;
import club.kwcoder.book.dto.PageDTO;
import club.kwcoder.book.dto.ResultDTO;
import club.kwcoder.book.dto.UserDTO;
import club.kwcoder.book.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultDTO<String> save(@RequestBody User user) {
        return userService.save(user);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultDTO<String> update(@RequestBody User user) {
        return userService.update(user);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResultDTO<String> delete(@RequestParam("email") String email) {
        return userService.delete(email);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultDTO<PageDTO<UserDTO>> list(@RequestParam("page") Integer page,
                                            @RequestParam("size") Integer size,
                                            @RequestParam(value = "email", required = false) String email,
                                            @RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "remain", required = false) Integer remain) {
        Map<String, Object> conditions = MapBuilder.builder()
                .put("email", email)
                .put("name", name)
                .put("remain", remain)
                .build();
        return userService.list(page, size, conditions);
    }

}
