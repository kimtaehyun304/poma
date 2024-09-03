package goinmul.sportsmanage.domain.form;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Match;

import java.io.IOException;

public class MatchStringToEnumDeserializer extends JsonDeserializer<Match> {

    @Override
    public Match deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String matchValue = p.getText().toUpperCase();
        return Match.valueOf(matchValue);
    }
}
