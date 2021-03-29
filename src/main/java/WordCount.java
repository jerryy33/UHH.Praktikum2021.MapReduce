import Job1.SumReducer;
import Job1.TokenMapper;
import Job2.SumReducer1;
import Job2.TokenMapper1;
import Job3.SumReducer2;
import Job3.TokenMapper2;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

/**
 * Main class which is responsible for executing the MapReduce jobs
 * @author Eugen Ruppert
 * @author Jeremy
 * @version 3.0
 */
public class WordCount
{

    /**
     * Starts the jobs in a specific order (job2 and job3 have to wait for job1 and than can be
     * started parallel)
     * @param args Input and Output
     * @throws Exception If an Exception occurs
     */
    public static void main(String[] args) throws Exception {

        //Job1: wordcount for hashtag#wort#lang#date
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "wordcount");

        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenMapper.class);
        job.setCombinerClass(SumReducer.class);
        job.setReducerClass(SumReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        FileInputFormat.addInputPath(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]+"/output"));

        // Second job needs to wait for the first one
        job.waitForCompletion(true);

        //Job2: wordcount for hashtag#wort
        Configuration conf1 = new Configuration();
        Job job1 = Job.getInstance(conf1,"hashtagWordCount");

        job1.setJarByClass(WordCount.class);
        job1.setMapperClass(TokenMapper1.class);
        job1.setCombinerClass(SumReducer1.class);
        job1.setReducerClass(SumReducer1.class);

        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(IntWritable.class);

        job1.setInputFormatClass(SequenceFileInputFormat.class);
        FileInputFormat.addInputPath(job1, new Path (args[1] + "/output"));
        FileOutputFormat.setOutputPath(job1, new Path(args[1]+ "/output1"));

        //Job3: wordcount for hashtag#date
        Configuration conf2 = new Configuration();
        Job job2 = Job.getInstance(conf2,"hashtagDateCount");

        job2.setJarByClass(WordCount.class);
        job2.setMapperClass(TokenMapper2.class);
        job2.setCombinerClass(SumReducer2.class);
        job2.setReducerClass(SumReducer2.class);

        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(IntWritable.class);

        job2.setInputFormatClass(SequenceFileInputFormat.class);
        FileInputFormat.addInputPath(job2, new Path (args[1] + "/output3"));
        FileOutputFormat.setOutputPath(job2, new Path(args[1]+ "/output4"));

        //Exit
        System.exit(job2.waitForCompletion(true) ? 0 : 1);
    }
}

