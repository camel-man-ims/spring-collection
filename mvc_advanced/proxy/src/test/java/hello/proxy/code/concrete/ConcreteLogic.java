package hello.proxy.code.concrete;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcreteLogic {

    public String operation(){
        log.info("concreate logic 실행");
        return "data";
    }
}
