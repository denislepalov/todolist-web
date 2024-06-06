package lepdv.todolistweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class ExtraBeans {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

/*
  ability to connect to the H2 database and see the tables
  (url: jdbc:h2:tcp://localhost:9092/mem:db)
 */

//    @Bean(initMethod = "start", destroyMethod = "stop")
//    public Server h2Server() throws SQLException {
//        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
//    }


}
