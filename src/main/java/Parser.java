import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.LinkedList;
import java.util.StringTokenizer;

/*
* Die Klasse parst einen Tweet, um ihn dann für das Map-Reduce zu nutzen.
* */
public class Parser {


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
            String cleanText = cleanText(t);
            StringTokenizer itr = new StringTokenizer(cleanText);
            while (itr.hasMoreTokens())
            {
                textList.add(itr.nextToken());
            }
        }
        return textList;
    }

    private String cleanText(String t)
    {
        return t.replaceAll("#\\p{IsAlphabetic}+", "");
    }
}
