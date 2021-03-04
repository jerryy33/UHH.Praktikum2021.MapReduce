import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.LinkedList;

// Mapper <Input Key, Input Value, Output Key, Output Value>
public class TokenMapper extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private final Text word = new Text();
    private LinkedList<String> hashtagCount= new LinkedList<>();
    private LinkedList<String> textList= new LinkedList<>();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        Parser parser = new Parser();
            try
            {
                hashtagCount = parser.getHashtags(value.toString());
                textList = parser.getText(value.toString());
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            if(hashtagCount.size() > 0)
            {
                for (String hashtag : hashtagCount)
                {
                    if (hashtag != null)
                    {
                        for (String wort: textList)
                        {
                            word.set(hashtag + "#" + wort);
                            context.write(word, one);
                        }
                    }
                }
            }
        }
}
