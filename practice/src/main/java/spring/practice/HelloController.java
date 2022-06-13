package spring.practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String hello(Model model){
        User user = new User("hello");
        model.addAttribute("user",user);
        model.addAttribute("sample","샘플입니다");
        return "hello";
    }

    @Data
    @AllArgsConstructor
    static class User{
        String name;
    }
}
