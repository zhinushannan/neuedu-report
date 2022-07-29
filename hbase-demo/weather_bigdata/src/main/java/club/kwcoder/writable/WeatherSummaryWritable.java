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
public class WeatherSummaryWritable implements Writable {
    private String code;
    private String year;
    private Integer rainDays = 0;
    private Double precipitationTotal = 0.0;
    private Double maxTemperature = 0.0;
    private Double minTemperature = 0.0;
    private Double avgTemperature = 0.0;

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.code);
        dataOutput.writeUTF(this.year);
        dataOutput.writeInt(this.rainDays);
        dataOutput.writeDouble(this.precipitationTotal);
        dataOutput.writeDouble(this.maxTemperature);
        dataOutput.writeDouble(this.minTemperature);
        dataOutput.writeDouble(this.avgTemperature);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.code = dataInput.readUTF();
        this.year = dataInput.readUTF();
        this.rainDays = dataInput.readInt();
        this.precipitationTotal = dataInput.readDouble();
        this.maxTemperature = dataInput.readDouble();
        this.minTemperature = dataInput.readDouble();
        this.avgTemperature = dataInput.readDouble();
    }

}
