import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

// Mapper <Input Key, Input Value, Output Key, Output Value>
public class TokenMapper1 extends Mapper<Text, IntWritable, Text, IntWritable> {

    private Text word = new Text();

    public void map(Text key, IntWritable value, Context context) throws IOException, InterruptedException {
        String hashWort = key.toString();
        String[] values=  hashWort.split("#");
        if(values.length > 0 && values[0] != null && values[1] != null)
        {
            word.set(values[0] + "#" + values[1]);
            context.write(word, value);
        }
    }
}