package club.kwcoder.writable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherWritable implements Writable {
    private String code;
    private String date;
    private Double precipitation = 0.0;
    private Double maxTemperature = 0.0;
    private Double minTemperature = 0.0;
    private Double avgTemperature = 0.0;

    public static byte[] tableName = Bytes.toBytes("weather");


    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(code);
        dataOutput.writeUTF(date);
        dataOutput.writeDouble(precipitation);
        dataOutput.writeDouble(maxTemperature);
        dataOutput.writeDouble(minTemperature);
        dataOutput.writeDouble(avgTemperature);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.code = dataInput.readUTF();
        this.date = dataInput.readUTF();
        this.precipitation = dataInput.readDouble();
        this.maxTemperature = dataInput.readDouble();
        this.minTemperature = dataInput.readDouble();
        this.avgTemperature = dataInput.readDouble();
    }

}
