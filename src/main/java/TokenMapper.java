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

// Mapper <Input Key, Input Value, Output Key, Output Value>
public class TokenMapper extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private final Text word = new Text();
    private LinkedList<String> hashtagCount= new LinkedList<>();
    private LinkedList<String> textList= new LinkedList<>();
    private String lang;
    private String date;
    private ArrayList<String> languages = new ArrayList<>(Arrays.asList("en", "de", "es"));

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

    /*
     * Holt die relevanten Information aus dem JSONFile mithilfe eines Parser.
     * @param value Ein Tweet als Text.
     */
    private void getInformation(Text value)
    {
        Parser parser = new Parser();
        try
        {
            JSONParser jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(value.toString());
            if (obj.containsKey("created_at")) {

                lang = parser.getLanguage(obj);
                if (languages.contains(lang))
                {
                    hashtagCount = parser.getHashtags(obj);
                    textList = parser.getText(obj);
                    date = parser.getDate(obj);
                }
            }

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
}
