package goinmul.sportsmanage.domain.form;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MercenaryMatchEditForm {

    List<Long> memberIdList = new ArrayList<>();
    List<Long> mercenaryIdList = new ArrayList<>();
}
