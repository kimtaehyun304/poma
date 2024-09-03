package goinmul.sportsmanage.config;

import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Sports;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new GenderStringToEnumConverter());
        registry.addConverter(new SportsStringToEnumConverter());
    }

    private class GenderStringToEnumConverter implements Converter<String, Gender> {
        @Override
        public Gender convert(String source) {
            try {
                return Gender.valueOf(source.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    private class SportsStringToEnumConverter implements Converter<String, Sports> {
        @Override
        public Sports convert(String source) {
            try {
                return Sports.valueOf(source.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}


