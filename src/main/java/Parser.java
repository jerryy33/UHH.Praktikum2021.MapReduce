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


/*
* Die Klasse parst einen Tweet, um ihn dann für das Map-Reduce zu nutzen.
* */
public class Parser {

    private String location = "No Location";
    private List<String> stopwords;

    //Datumsfunktion (Vielleicht entfernen)
    public String getDate(JSONObject tweet) throws ParseException
    {
        String date = (String) tweet.get("created_at");
        String [] einheiten = date.split("\\s+");

        return einheiten[1] + einheiten[2] +einheiten[5];
    }
    //Locationfunktion (Vielleicht entfernen)
    public String getLocation(JSONObject tweet) throws ParseException
    {
        JSONObject user = (JSONObject) tweet.get("user");
    try{
            location = (String) user.get("location");
        }
      catch(NullPointerException ex)
        {
            ex.printStackTrace();
        }
    return location;
    }

    /*
    * Extrahiert die Hashtags aus dem JSON File.
    * @param JSONObject Ein kompletter Tweet
    * */
    public LinkedList<String> getHashtags (JSONObject tweet) throws ParseException
    {
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
    public LinkedList<String> getText(JSONObject tweet) throws ParseException
    {
        LinkedList<String> textList = new LinkedList<>();

        String t = (String) tweet.get("text");
        String cleanText = cleanText(t.toLowerCase(Locale.ROOT));
        StringTokenizer itr = new StringTokenizer(cleanText);
        while (itr.hasMoreTokens())
        {
            textList.add(itr.nextToken());
        }
    return textList;
    }

    private String cleanText(String t) {
        String s = removeAllStopWords(t.toLowerCase(Locale.ROOT));
        return s.replaceAll("#\\p{IsAlphabetic}+", "");
    }

    public String removeAllStopWords(String t)
    {

        String path = "/english_stopwords";
        try (InputStream resource = Parser.class.getResourceAsStream(path)) {
            stopwords = new BufferedReader(new InputStreamReader(resource,
                                StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> allWords = Stream.of(t.split(" ")).collect(Collectors.toCollection(ArrayList<String>::new));
        stopwords.removeAll(Collections.singleton(null));
        allWords.removeAll(stopwords);
        return allWords.stream().collect(Collectors.joining(" "));
    }
}