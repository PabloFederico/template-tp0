package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;

public class RegExGenerator {
    // TODO: Uncomment this field
    private int maxLength;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }

    // TODO: Uncomment parameters
    public List<String> generate(/*String regEx, int numberOfResults*/) {
        return new ArrayList<String>() {
            {
                add("a");
                add("b");
                add("c");
            }
        };
    }
}