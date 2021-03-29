package Job2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

/**
 * Maps the output of SumReducer to the combination hashtag#word IntWritable
 * @author Jeremy
 * @version 1.0
 */
// Mapper <Input Key, Input Value, Output Key, Output Value>
public class TokenMapper1 extends Mapper<Text, IntWritable, Text, IntWritable> {

    private final Text word = new Text();

    /**
     * Maps the data using delimiters to only use part of a string
     * @param key Object key
     * @param value Integer which is assigned to a key
     * @param context Context where the value and key are written
     * @throws IOException If a Input or Output Exception occurred
     * @throws InterruptedException If a Interrupted Exception occurred
     */
    public void map(Text key, IntWritable value, Context context) throws IOException, InterruptedException
    {
        String hashWort = key.toString();
        String[] values=  hashWort.split("#");
        if(values.length > 0 && values[0] != null && values[1] != null)
        {
            word.set(values[0] + "#" + values[1]);
            context.write(word, value);
        }
    }
}