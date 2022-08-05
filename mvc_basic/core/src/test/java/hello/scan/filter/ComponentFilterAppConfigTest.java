package hello.scan.filter;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

public class ComponentFilterAppConfigTest {

    @Test
    void filterIncludeScan(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);
        BeanA bean = ac.getBean(BeanA.class);

        System.out.println("bean = " + bean);
    }

    @Test
    void filterExcludeScan(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);
        BeanB bean = ac.getBean(BeanB.class);

        System.out.println("bean = " + bean);
    }

    @Configuration
    @ComponentScan(
            // ANNOTATION 이 기본값
            includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = MyIncludeComponent.class),
            excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = MyExcludeComponent.class)
    )
    static class ComponentFilterAppConfig{

    }
}
