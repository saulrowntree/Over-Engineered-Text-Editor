package srowntree;

public class TestData {
    public static final String
            WORD_COUNT = "wordcount", CHAR_COUNT = "charcount", VOWEL_COUNT = "vowelcount",
            PALINDROME_COUNT = "palindromecount", SPELLING_ERRORS = "spellingerrors", COMMA_COUNT = "commacount",
            TEST_TEXT = "This text contains 15 words, 123 characters, 28 vowels, 2 palindromes, 1 spelling error, " +
                    "and 5 commas. mom dad dog mistajke";
    public static final int
            WORD_COUNT_ANSWER = 15, CHAR_COUNT_ANSWER = 123, VOWEL_COUNT_ANSWER = 28,
            PALINDROME_COUNT_ANSWER = 2, SPELLING_ERROR_COUNT = 1, COMMA_COUNT_ANSWER = 5;
    public static final String[] SERVICE_TYPES = {
            WORD_COUNT, CHAR_COUNT, VOWEL_COUNT,
            PALINDROME_COUNT, SPELLING_ERRORS, COMMA_COUNT
    };
    public static final long TOO_LONG_MILLIS = 5000L;

}
