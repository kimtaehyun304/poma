package goinmul.sportsmanage.domain.form;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import goinmul.sportsmanage.domain.Gender;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;

public class GenderStringToEnumDeserializer extends JsonDeserializer<Gender> {

    @Override
    public Gender deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String genderValue = p.getText().toUpperCase();
        return Gender.valueOf(genderValue);
    }
}
