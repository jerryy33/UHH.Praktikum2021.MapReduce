package Job1;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Reduces the data from TokenMapper
 * @author Eugen Ruppert
 * @author Jeremy
 * @version 2.0
 */
public class SumReducer extends Reducer<Text, IntWritable, Text, IntWritable>
{
    // Number of times a combination occurred
    private final IntWritable result = new IntWritable();

    /**
     * Reduces all values.
     * @param key the combination coming from TokenMapper
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
        if(sum > 0)
        {
            result.set(sum);
            context.write(key, result);
        }
    }
}