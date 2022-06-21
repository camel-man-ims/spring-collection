package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource ms;

    @Test
    void helloMessage(){
        String result = ms.getMessage("hello", null, null);
        System.out.println(result);
//        Assertions.assertThat(result).isEqualTo("안녕");
    }

    @Test
    void notFoundMessageCode(){
        Assertions.assertThatThrownBy(() -> ms.getMessage("no_code",null,null))
                .isInstanceOf(NoSuchMessageException.class);
    }
}
