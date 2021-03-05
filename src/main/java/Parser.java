import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;


/*
* Die Klasse parst einen Tweet, um ihn dann für das Map-Reduce zu nutzen.
* */
public class Parser {
    private String location = "No Location";

    private ArrayList<String> stopwords = new ArrayList<String>();


    public String getLocation(String tweet) throws ParseException
    {
        JSONParser jsonParser = new JSONParser();
        JSONObject obj = (JSONObject) jsonParser.parse(tweet);
        JSONObject user = (JSONObject) obj.get("user");
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
    public LinkedList<String> getHashtags (String tweet) throws ParseException
    {
        LinkedList<String> hashtagsList = new LinkedList<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject obj = (JSONObject) jsonParser.parse(tweet);

        if(obj.containsKey("created_at"))
        {
            JSONObject ent = (JSONObject) obj.get("entities");
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
        }
        return hashtagsList;
    }
    public LinkedList<String> getText(String tweet) throws ParseException
    {
        LinkedList<String> textList = new LinkedList<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject obj = (JSONObject) jsonParser.parse(tweet);

        if (obj.containsKey("created_at"))
        {
            String t = (String) obj.get("text");
            String cleanText = cleanText(t.toLowerCase(Locale.ROOT));
            StringTokenizer itr = new StringTokenizer(cleanText);
            while (itr.hasMoreTokens())
            {
                textList.add(itr.nextToken());
            }
        }
        return textList;
    }

    private String cleanText(String t) {
        String s = removeAllStopWords(t);
        return s.replaceAll("#\\p{IsAlphabetic}+", "");
    }

    private String removeAllStopWords(String t)
    {//TODO Stopwörter werden nicht entfernt
        try {
            BufferedReader bf = new BufferedReader(new FileReader("english_stopwords"));
            String end = null;
            while ((end = bf.readLine()) != null) {
                String stop = bf.readLine();
                stopwords.add(stop);
            }
            bf.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        for (String stop: stopwords)
        {
            Pattern regex = Pattern.compile("\\b"+ stop+"\\b");
            t= t.replaceAll(String.valueOf(regex),"");
        }
        return t;
    }
}