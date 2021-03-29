package Job1;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Maps word#hashtag#lanuage#date with a 1
 * @author Jeremy
 * @author Eugen Ruppert
 * @version 2.0
 */
// Mapper <Input Key, Input Value, Output Key, Output Value>
public class TokenMapper extends Mapper<Object, Text, Text, IntWritable>
{
    // IntWritable that has the value 1
    private final static IntWritable one = new IntWritable(1);

    // New Text which is one tweet
    private final Text word = new Text();

    // List that contains all hashtags
    private LinkedList<String> hashtagCount= new LinkedList<>();

    // List that contains the text from the tweet
    private LinkedList<String> textList= new LinkedList<>();

    // Language of a tweet
    private String lang;

    // Date of a tweet
    private String date;

    // List that contains all allowed languages
    private final ArrayList<String> languages = new ArrayList<>(Arrays.asList("en", "de", "es"));

    /**
     * Maps a value to a IntWritable
     * @param key Object key
     * @param value tweet as a text
     * @param context Context where the value and key are written
     * @throws IOException If a Input or Output Exception occurred
     * @throws InterruptedException If a Interrupted Exception occurred
     */
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException
    {
        getInformation(value);
        if(hashtagCount.size() > 0 && languages.contains(lang))
        {
                for (String hashtag : hashtagCount)
                {
                        for (String wort: textList)
                        {
                            String val = hashtag + "#" + wort + "#"+ lang + "#"+ date;
                            word.set(val);
                            context.write(word, one);
                        }
                }
        }
    }

    /**
     * Gets the Information needed for the mapping process
     * @param value Tweet as text
     */
    private void getInformation(Text value)
    {
        Parser parser = new Parser();
        try
        {
            JSONParser jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(value.toString());
            lang = parser.getLanguage(obj);
            if (languages.contains(lang))
            {
                hashtagCount = parser.getHashtags(obj);
                textList = parser.getText(obj);
                date = parser.getDate(obj);
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
}
