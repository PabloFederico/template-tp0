package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegExGenerator {
    private int max;
    private int myIndex;

    public RegExGenerator(int maxLength) {
        this.max = maxLength;
    }

    // GENERATE
    public List<String> generate(String regEx, int numberOfResults) {
        List<String> resultList = new ArrayList<>();
        List<String> tokens = getTokens(regEx);
        String result = "";
        for (int i = 0 ; i < numberOfResults; i++) {
            for (String token : tokens) {
                result = result.concat(tokenAsString(token));
            }
            resultList.add(result);
            result = "";
        }
        return resultList;
    }

    //MY FUNCTIONS
    private boolean quantifierInRegExp(String regEx) {

        if ( this.myIndex < regEx.length() ) {

            if ( quantifierChar(regEx.charAt(this.myIndex)) ) {
                return true;
            }
        }
        return false;
    }

    private String getTokenAsString(String regEx) {

        String myToken = getTokenWithOutQuantifier(regEx);

        if ( quantifierInRegExp(regEx) ) myToken = myToken.concat("" + regEx.charAt(this.myIndex++));
        return myToken;
    }

    private String tokenAsString(String token) {
        String emptyToken = "";
        if ( token.isEmpty() ) {
            return emptyToken;
        }
        else {
            char first = token.charAt(0);
            if (bracketLeftChar(first)) {
                return getStringFromToken(token);
            } else {
                return getStringFromLiteral(token);
            }
        }
    }

    private List<Character> getCharsList(String tokenString) {
        List<Character> result = new ArrayList<>();
        int index = 1;
        while ( index < tokenString.length() ) {
            char c = tokenString.charAt(index++);
            if ( c == '\\' ) {
                c = tokenString.charAt(index++);
            } else if ( bracketRightChar(c) ) {
                return result;
            }

            result.add(c);
        }

        return result;
    }

    private int getRandom(char c) {
        Random rand = new Random();
        int random = 0;
        if ( oneAndMoreQuantifier(c) ) {
            random = rand.nextInt(this.max) + 1;
        } else if ( nothingOrOneQuantifier(c) ) {
            random = rand.nextInt(2);
        } else if ( nothingAndMoreQuantifier(c) ) {
            random = rand.nextInt(this.max + 1);
        }

        return random;
    }

    private int stringLength(String tokenAsString) {
        char c = tokenAsString.charAt(tokenAsString.length() - 1);
        int stringLength = 1;
        if ( quantifierChar(c) ) {
            stringLength = getRandom(c);
        }
        return stringLength;
    }

    private String getStringFromToken(String tokenAsString) {
        List<Character> chars = getCharsList(tokenAsString);
        Random rand = new Random();
        String result = emptyString();
        for (int i = 0; i < stringLength(tokenAsString) ; i ++ ) {
            int randomIndex = rand.nextInt(chars.size());
            result = result.concat("" + chars.get(randomIndex));
        }
        return result;
    }

    private String getStringFromLiteral(String tokenAsStringLiteral) {


        char c = tokenAsStringLiteral.charAt(0);
        char charToConcat;

        String result = emptyString();
        Random rand = new Random();

        for (int i = 0; i < stringLength(tokenAsStringLiteral) ; i ++ ) {
            if ( c == '\\' ) {
                charToConcat = tokenAsStringLiteral.charAt(1);
            } else if ( c == '.' ) {
                charToConcat = (char) rand.nextInt(256);
            } else {
                charToConcat = c;
            }
            result = result.concat(emptyString() + charToConcat);
        }
        return result;
    }

    private String getSetTokenFromToken(String regEx)  {
        int regexLength = regEx.length();
        String bad = "bad";
        String token = emptyString();
        char c = '[';
        while ( this.myIndex < regexLength ) {
            char lastChar = c;
            c = regEx.charAt(this.myIndex++);
            token = token.concat("" + c);
            if ( lastChar != '\\' && bracketRightChar(c)) {
                // Finished
                return token;
            }

        }
        //No ]
        return bad;
    }

    private String getTokenWithOutQuantifier(String regEx) {
        char character = regEx.charAt(this.myIndex++);
        int regexLength = regEx.length();

        String token = emptyString();
        if ( bracketLeftChar(character) ) {
            token = emptyString() + character + getSetTokenFromToken(regEx);
        } else if ( character == '\\' ) {
            // escaped literal
            if ( this.myIndex < regexLength ) {
                token = emptyString() + character + regEx.charAt(this.myIndex++);
            }
        } else {

            token = emptyString() + character;
        }

        return token;
    }

    private List<String> getTokens(String regEx) {
        List<String> tokens = new ArrayList<>();
        if (regEx.isEmpty()) {
            return tokens;
        }

        int regexLength = regEx.length();
        this.myIndex = 0;
        String token;
        while ( this.myIndex < regexLength ) {
            token = getTokenAsString(regEx);
            tokens.add(token);
        }
        return tokens;
    }


    //COMMON FUNCTIONS
    private boolean quantifierChar(char c) {
        return c == '*' || c == '?' || c == '+';
    }

    private boolean bracketRightChar(char c){
        return (c == ']');
    }

    private boolean bracketLeftChar(char c){
        return (c == '[');
    }

    private boolean nothingAndMoreQuantifier(char c){
        return (c == '*');
    }

    private boolean oneAndMoreQuantifier(char c){
        return (c == '+');
    }

    private boolean nothingOrOneQuantifier(char c){
        return (c == '?');
    }

    private String emptyString(){
        return "";
    }

}