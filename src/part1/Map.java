package part1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        Configuration configuration = context.getConfiguration();

        try {
            if (value.charAt(0) == 'R') {
                String[] data = line.split(" ");
                String userId = data[6];

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

                Date revisionTimestamp=simpleDateFormat.parse(data[4]);
                Date timestamp1 = simpleDateFormat.parse(configuration.get("timestamp1"));
                Date timestamp2 = simpleDateFormat.parse(configuration.get("timestamp2"));

                if (revisionTimestamp.after(timestamp1) && revisionTimestamp.before(timestamp2)) {
                    context.write(new Text(userId), new IntWritable(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
