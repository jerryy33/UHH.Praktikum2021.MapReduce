package Job2;

import  org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

/**
 * Reduces the data coming from TokenMapper1
 * @author Jeremy
 * @version 1.0
 */
public class SumReducer1 extends Reducer<Text, IntWritable, Text, IntWritable>
{
    private final IntWritable result = new IntWritable();

    /**
     * Reduces the data to those combinations that occur more than a 100 times
     * @param key  the combination coming from TokenMapper1
     * @param values contains all values which were assigned during the mapping
     * @param context context in which we write our results
     * @throws IOException If a Input or Output Exception occurred
     * @throws InterruptedException If a Interrupted Exception occurred
     */
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
    {
        int sum = 0;
        for (IntWritable val : values)
        {
            sum += val.get();
        }
        if(sum > 100)
        {
            result.set(sum);
            context.write(key, result);
        }
    }
}
