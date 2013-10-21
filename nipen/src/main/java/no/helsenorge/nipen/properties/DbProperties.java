package no.helsenorge.nipen.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(name = "db", ignoreUnknownFields = false)
public class DbProperties {

    private String url;
    private String username;
    private String password;

    @Bean
    public javax.sql.DataSource dataSource() {
        return new DriverManagerDataSource(
                url,
                username,
                password);
    }

    public String getURL() {
        return this.url;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public void setUsername(String user) {
        this.username = user;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }
}
