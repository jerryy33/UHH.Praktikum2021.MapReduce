package Job1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ParserTest {


    private final ArrayList<String> languages = new ArrayList<>(Arrays.asList("en", "de", "es"));
    private final Map<String,List<String>> map = new HashMap<>();
    private final String SPRACHE  = "en";
    private final String DATE1 = "Apr062017";
    private final String DATE2 = "Jun222017";

    public ParserTest()
    {
        getTextForTests();
    }

    /*
     * Liest einen Beispiel Tweet zum Testen ein.
     * @param path Pfad zum JSON File
     * @return tweet String der den Tweet darstellt.
     */
    private String readTweet(String path)
    {
        String tweet = "";
        try (InputStream resource = ParserTest.class.getResourceAsStream(path))
        {
            tweet = new BufferedReader(new InputStreamReader(resource,
                    StandardCharsets.UTF_8)).lines().collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    /*
     * Extrahiert den Text aus den Beispieltweets.
     */
    private void getTextForTests()
    {
        String tweet1 = getTweet1();
        String tweet2 = getTweet2();
        Parser parser = new Parser();
        LinkedList<String> text1 = new LinkedList<>();
        LinkedList<String> text2 = new LinkedList<>();
        JSONParser jsonParser = new JSONParser();

        try
        {
            JSONObject obj1 = (JSONObject) jsonParser.parse(tweet1);
            JSONObject obj2 = (JSONObject) jsonParser.parse(tweet2);
            text1 = parser.getText(obj1);
            text2 = parser.getText(obj2);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        map.put("text1",text1);
        map.put("text2",text2);
    }
    // Hilfsmethoden um die einzelnen Tweets zu lesen.
    private String getTweet1()
    {
        return readTweet("/testTweet1.json");
    }
    private String getTweet2()
    {
        return readTweet("/testTweet2.json");
    }

    // Hier beginnen die eigentlichen Tests.

    @Test
    public void getDate()
    {
        String tweet1 = getTweet1();
        String tweet2 =  getTweet2();
        Parser parser = new Parser();
        String date1 = null;
        String date2 = null;
        JSONParser jsonParser = new JSONParser();

        try
        {
            JSONObject obj1 = (JSONObject) jsonParser.parse(tweet1);
            JSONObject obj2 = (JSONObject) jsonParser.parse(tweet2);
            date1 = parser.getDate(obj1);
            date2 = parser.getDate(obj2);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        assertNotNull(date1);
        assertNotNull(date2);
        assertEquals(DATE1,date1);
        assertEquals(DATE2,date2);

    }

    @Test
    public void getLanguageIfIsInLanguages() {
        String tweet = getTweet2();
        Parser parser = new Parser();
        String language = null;
        JSONParser jsonParser = new JSONParser();

        try
        {
            JSONObject obj = (JSONObject) jsonParser.parse(tweet);
            language = parser.getLanguage(obj);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        assertNotNull(language);
        assertEquals(SPRACHE,language);
        assertTrue(languages.contains(language));
    }
    @Test
    public void getLanguageIfNotInLanguages()
    {
        String tweet = getTweet1();
        Parser parser = new Parser();
        String language = null;
        JSONParser jsonParser = new JSONParser();

        try
        {
            JSONObject obj = (JSONObject) jsonParser.parse(tweet);
            language = parser.getLanguage(obj);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        assertNotNull(language);
        assertFalse(languages.contains(language));
    }

    @Test
    public void getHashtagsIfNoHashtags() {
        String tweet = getTweet1();
        Parser parser = new Parser();
        LinkedList<String> hashtags = new LinkedList<>();
        JSONParser jsonParser = new JSONParser();

        try
        {
            JSONObject obj = (JSONObject) jsonParser.parse(tweet);
            hashtags = parser.getHashtags(obj);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        assertNotNull(hashtags);
        assertFalse(hashtags.contains(null));
        assertTrue(hashtags.isEmpty());

    }
    @Test
    public void getHashtags()
    {
        String tweet = getTweet2();
        Parser parser = new Parser();
        LinkedList<String> hashtags = new LinkedList<>();
        JSONParser jsonParser = new JSONParser();

        try
        {
            JSONObject obj = (JSONObject) jsonParser.parse(tweet);
            hashtags = parser.getHashtags(obj);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        assertNotNull(hashtags);
        assertFalse(hashtags.contains(null));
        assertTrue(hashtags.contains("Angular"));
        assertFalse(hashtags.contains("103"));
        assertFalse(hashtags.contains("111"));
    }

    @Test
    public void getText() {
        LinkedList<String> text1 = (LinkedList<String>) map.get("text1");
        LinkedList<String> text2 = (LinkedList<String>) map.get("text2");

        assertFalse(text1.isEmpty());
        assertFalse(text2.isEmpty());
        assertNotNull(text1);
        assertNotNull(text2);
        assertFalse(text1.contains(null));
        assertFalse(text2.contains(null));
        assertTrue(text1.contains("vision") && text1.contains("twitter"));
        assertTrue(text2.contains("creating") && text2.contains("display"));
    }

    @Test
    public void removeAllStopWords()
    {
        LinkedList<String> text1 = (LinkedList<String>) map.get("text1");
        LinkedList<String> text2 = (LinkedList<String>) map.get("text2");
        LinkedList<String> stopwords = new LinkedList<>(Arrays.asList
                ("today","our","a","using","we","for","the","add"));

        assertFalse(text1.containsAll(stopwords));
        assertFalse(text2.containsAll(stopwords));
    }

    @Test
    public void removeAllWordsWithHashtags()
    {
        LinkedList<String> text1 = (LinkedList<String>) map.get("text1");
        LinkedList<String> text2 = (LinkedList<String>) map.get("text2");
        Predicate<String> p1 = s -> s.contains("#");

        assertTrue(text1.stream().noneMatch(p1));
        assertTrue(text2.stream().noneMatch(p1));
    }

    @Test
    public void removeAllLinks()
    {
        LinkedList<String> text1 = (LinkedList<String>) map.get("text1");
        LinkedList<String> text2 = (LinkedList<String>) map.get("text2");
        Predicate<String> p1 = s -> s.contains("https");
        Predicate<String> p2 = s -> s.contains("http");

        assertTrue(text1.stream().noneMatch(p1));
        assertTrue(text2.stream().noneMatch(p1));
        assertTrue(text1.stream().noneMatch(p2));
        assertTrue(text2.stream().noneMatch(p2));
    }
}