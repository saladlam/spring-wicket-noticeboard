package info.saladlam.example.spring.noticeboard.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApplicationDateTimeServiceImpl implements ApplicationDateTimeService {

	@Override
	public LocalDateTime getCurrentLocalDateTime() {
		return LocalDateTime.now();
	}

}
