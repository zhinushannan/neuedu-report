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
public class ForecastWritable implements Writable {

    private String code;
    private String date;
    private Double maxTemperature;
    private Double minTemperature;
    private Double avgTemperature;
    private Double precipitation;


    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(code);
        dataOutput.writeUTF(date);
        dataOutput.writeDouble(maxTemperature);
        dataOutput.writeDouble(minTemperature);
        dataOutput.writeDouble(avgTemperature);
        dataOutput.writeDouble(precipitation);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.code = dataInput.readUTF();
        this.date = dataInput.readUTF();
        this.maxTemperature = dataInput.readDouble();
        this.minTemperature = dataInput.readDouble();
        this.avgTemperature = dataInput.readDouble();
        this.precipitation = dataInput.readDouble();
    }
}
