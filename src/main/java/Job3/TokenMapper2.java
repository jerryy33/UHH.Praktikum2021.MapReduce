package Job3;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

// Mapper <Input Key, Input Value, Output Key, Output Value>
public class TokenMapper2 extends Mapper<Text, IntWritable, Text, IntWritable> {

    private Text word = new Text();

    public void map(Text key, IntWritable value, Context context) throws IOException, InterruptedException {
        String hashWort = key.toString();
        String[] values=  hashWort.split("#");
        if(values.length > 0 && values[0] != null && values[3] != null)
        {
            word.set(values[0] + "#" + values[3]);
            context.write(word, value);
        }
    }
}