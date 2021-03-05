import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private final IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        //TODO raufzählen ohne Unterscheidung zwischen location, aber trotzdem location behalten
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        if(sum > 0)
        {
            result.set(sum);
            context.write(key, result);
        }
    }
}