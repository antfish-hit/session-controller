package com.sony.spring.session;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Arrays;
import java.util.Optional;

public class ActionTypeAdapter extends XmlAdapter<String, ActionType> {
    @Override
    public ActionType unmarshal(String v) throws Exception {
        Optional<ActionType> actionType =
                Arrays.stream(ActionType.values())
                .filter(type -> v.equals(type.getValue()))
                .findAny();
        return actionType.orElse(ActionType.START);
    }

    @Override
    public String marshal(ActionType v) throws Exception {
        return v.getValue();
    }
}
