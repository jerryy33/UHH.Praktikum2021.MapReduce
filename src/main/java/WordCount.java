import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class WordCount {

    public static void main(String[] args) throws Exception {

        //Job1: Wordcount für hashtag#wort#lang#date
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

        // Zweiter job muss auf den ersten warten.
        job.waitForCompletion(true);

        //Job2: Wordcount für hashtag#wort
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

        //Exit
        System.exit(job1.waitForCompletion(true) ? 0 : 1);

    }
}

