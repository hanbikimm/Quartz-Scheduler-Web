package com.hansol;

import java.util.Date;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.hansol.schedule.Utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SchedulerProjectApplication {
	
	@PostConstruct
    public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		log.info("[INFO] 현재 시간: {}", DateUtils.toString(new Date()));
    }
	
	public static void main(String[] args) {
		SpringApplication.run(SchedulerProjectApplication.class, args);
	}
}