import org.apache.hadoop.conf.Configuration; 
import org.apache.hadoop.fs.Path; 
import org.apache.hadoop.io.IntWritable; 
import org.apache.hadoop.io.Text; 
import org.apache.hadoop.mapreduce.Job; 
import org.apache.hadoop.mapreduce.Mapper; 
import org.apache.hadoop.mapreduce.Reducer; 
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat; 
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat; 
 
import java.io.IOException; 
 
public class Q6 { 
 
     
    public static class ReviewMapper extends Mapper<Object, Text, Text, IntWritable> { 
 
        private final static IntWritable one = new IntWritable(1); 
        private Text userId = new Text(); 
 
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException 
{ 
             
            String[] fields = value.toString().split(","); 
 
             
            if (fields.length > 0) { 
                String userID = fields[0];  // Replace with the correct index if different 
                userId.set(userID); 
                context.write(userId, one);  // Emit the user ID and count 1 for each review 
            } 
        } 
    } 
 
     
    public static class ReviewReducer extends Reducer<Text, IntWritable, Text, IntWritable> { 
        private IntWritable result = new IntWritable(); 
 
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, 
InterruptedException { 
            int sum = 0; 
            
            for (IntWritable val : values) { 
                sum += val.get(); 
            } 
            result.set(sum); 
            context.write(key, result);   
        } 
    } 
 
    public static void main(String[] args) throws Exception { 
        Configuration conf = new Configuration(); 
        Job job = Job.getInstance(conf, "review count"); 
        job.setJarByClass(ReviewCount.class); 
        job.setMapperClass(ReviewMapper.class); 
        job.setReducerClass(ReviewReducer.class); 
        job.setOutputKeyClass(Text.class); 
        job.setOutputValueClass(IntWritable.class); 
        FileInputFormat.addInputPath(job, new Path(args[0]));   
        FileOutputFormat.setOutputPath(job, new Path(args[1]));   
        System.exit(job.waitForCompletion(true) ? 0 : 1); 
    } 
}