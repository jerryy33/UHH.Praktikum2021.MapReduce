package Job1;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is used to extract Information from a tweet, which is needed for the map-reduce in
 * Job1
 * @author Jeremy
 * @author Selen
 * @version 2.0
 */
public class Parser
{

    // List for english stopwords
    private List<String> stopwords_eng;

    //List for german stopwords
    private List<String> stopwords_ger;

    //List for spanish stopwords
    private List<String> stopwords_es;

    // Language of the tweet
    private String language = "";

    /**
     * Constructor which reads stopwords lists in so they can be used instantly.
     */
    public Parser()
    {
        makeEnglishStopwordList();
        makeSpanishStopwordLists();
        makeGermanStopwordList();
    }
    /**
     * Gets the date where the tweet is created
     * @param tweet the JSONObject that is exactly one tweet
     * @return Month + Day + Year
     * @throws ParseException If a parse Exception occurred
     * @require tweet != null
     * @require tweet.containsKey("created_at") == true
     * @ensure result != null && !result.equals("")
     */
    public String getDate(JSONObject tweet) throws ParseException
    {
        assert tweet != null:"Precondition injured: tweet != null";
        assert tweet.containsKey("created_at") :
                "Precondition injured: tweet.containsKey(\"created_at\") == true";

        String date = (String) tweet.get("created_at");
        String [] units = date.split("\\s+");

        return units[1] + units[2] + units[5];
    }

    /**
     * Extracts the machine determined language of exactly one tweet
     * @param tweet the JSONObject that is exactly one tweet
     * @return String which has the ISO-639-1 coding for a language
     * @throws ParseException If a parse Exception occurred
     * @require tweet != null
     * @require tweet.containsKey("created_at") == true
     * @ensure result != null && !result.equals("")
     */
    public String getLanguage(JSONObject tweet) throws ParseException
    {
        assert tweet != null:"Precondition injured: tweet != null";
        assert tweet.containsKey("created_at") :
                "Precondition injured: tweet.containsKey(\"created_at\") == true";

        language = (String) tweet.get("lang");
        return language;
    }

    /**
     * Extracts the hashtags from exactly one tweet
     * @param tweet the JSONObject that is exactly one tweet
     * @return hashtagList List which contains all hashtags
     * @throws ParseException If a parse Exception occurred
     * @require tweet != null
     * @require tweet.containsKey("created_at") == true
     * @ensure hashtagList != null
     */
    public LinkedList<String> getHashtags (JSONObject tweet) throws ParseException
    {
        assert tweet != null:"Precondition injured: tweet != null";
        assert tweet.containsKey("created_at") :
                "Precondition injured: tweet.containsKey(\"created_at\") == true";

        LinkedList<String> hashtagsList = new LinkedList<>();
        JSONObject ent = (JSONObject) tweet.get("entities");
        JSONArray hash = (JSONArray) ent.get("hashtags");

        if (!hash.isEmpty())
        {
            for (Object o : hash)
            {
                JSONObject text = (JSONObject) o;
                if (text.containsKey("text"))
                {
                    hashtagsList.add(text.get("text").toString());
                }
            }
        }
        return hashtagsList;
    }

    /** Extracts the actual text from exactly one tweet, during this only useful words will be
     * extracted. All words will be in lower Case
     * @param tweet the JSONObject that is exactly one tweet
     * @return textList List which contains all useful words from the original text
     * @throws ParseException If a parse Exception occurred
     * @require tweet != null
     * @require tweet.containsKey("created_at") == true
     * @ensure textList != null
     */
    public LinkedList<String> getText(JSONObject tweet) throws ParseException
    {
        assert tweet != null:"Precondition injured: tweet != null";
        assert tweet.containsKey("created_at") :
                "Precondition injured: tweet.containsKey(\"created_at\") == true";

        LinkedList<String> textList = new LinkedList<>();

        String text = (String) tweet.get("text");
        String cleanText = cleanText(text.toLowerCase(Locale.ROOT));
        StringTokenizer itr = new StringTokenizer(cleanText);
        while (itr.hasMoreTokens())
        {
            textList.add(itr.nextToken());
        }
        return textList;
    }
    /**
     * Cleans the original text, so only useful and meaningful words remain. During this Links,
     * words with # in it and stopwords will be removed
     * @param text The actual text of a tweet
     * @return result String that doesn't contain any of the mentioned objects above
     * @require text != null
     */
    private String cleanText(String text)
    {
        assert text != null:"Precondition injured: text != null";

        String s = removeAllStopWords(text.toLowerCase(Locale.ROOT));
        String r = s.replaceAll("[\\S]+://[\\S]+", "");
        return r.replaceAll("#[^\\s]+", "");
    }

    /**
     * Removes all stopwords from a String
     * @param text String which is a tweet-text
     * @return result original string without stopwords
     * @require text != null
     */
    public String removeAllStopWords(String text)
    {
        assert text != null:"Precondition injured: text != null";

        ArrayList<String> allWords = Stream.of(text.split(" ")).collect(Collectors.toCollection
                (ArrayList<String>::new));

        if(language.equals("en"))
        {
            stopwords_eng.removeAll(Collections.singleton(null));
            allWords.removeAll(stopwords_eng);
        }
        if(language.equals("es"))
        {
            stopwords_es.removeAll(Collections.singleton(null));
            allWords.removeAll(stopwords_es);
        }
        if(language.equals("de"))
        {
            stopwords_ger.removeAll(Collections.singleton(null));
            allWords.removeAll(stopwords_ger);
        }

        return allWords.stream().collect(Collectors.joining(" "));
    }

    /**
     * Puts all spanish stopwords in a list
     */
    private void makeSpanishStopwordLists()
    {
        String path_es = "/spanish_stopwords";
        try (InputStream resource = Parser.class.getResourceAsStream(path_es))
        {
            stopwords_es = new BufferedReader(new InputStreamReader(resource,
                    StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Puts all english stopwords in a list
     */
    private void makeEnglishStopwordList()
    {
        String path_eng = "/english_stopwords";
        try (InputStream resource = Parser.class.getResourceAsStream(path_eng))
        {
            stopwords_eng = new BufferedReader(new InputStreamReader(resource,
                    StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Puts all german stopwords in a list
     */
    private void makeGermanStopwordList()
    {
        String path_ger = "/german_stopwords";
        try (InputStream resource = Parser.class.getResourceAsStream(path_ger))
        {
            stopwords_ger = new BufferedReader(new InputStreamReader(resource,
                    StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Getter Method for english stopwords list
     * @return List of all english stopwords
     */
    public List<String> getEnglishStopwordList()
    {
        return stopwords_eng;
    }

    /**
     * Getter Method for spanish stopwords list
     * @return list of all spanish stopwords
     */
    public List<String> getSpanishStopwordList()
    {
        return stopwords_es;
    }

    /**
     * Getter Method for german stopwords list
     * @return List of all german stopwords
     */
    public List<String> getGermanStopwordList()
    {
        return stopwords_ger;
    }
}