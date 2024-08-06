package ru.gb.timesheet.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TimesheetRestConfiguration {
    @Bean
    @LoadBalanced
    public RestClient timesheetRestClient() {
        return RestClient.create();
    }
}
