package main.xiaoqihui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("main.xiaoqihui.**")
public class XiaoQiHuiApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoQiHuiApplication.class, args);
    }

}
