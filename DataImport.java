import java.nio.ByteBuffer;
import java.io.*;
import java.util.UUID;

import org.apache.cassandra.db.marshal.*;
import org.apache.cassandra.io.sstable.SSTableSimpleUnsortedWriter;
import org.apache.cassandra.dht.RandomPartitioner;
import static org.apache.cassandra.utils.ByteBufferUtil.bytes;
import static org.apache.cassandra.utils.UUIDGen.decompose;

public class DataImport {

    static String filename;

    // Number of columns & names
    static int numCols = 12;
    static String colNames[] = {"Col1", "Col2", "Col3", "Col4", "Col5", "Col6", "Col7", "Col8", "Col9", "Col10", "Col11", "Col12"};

    public static void main(String[] args) throws IOException {

        if (args.length < 3)
        {
            System.out.println("Expecting 3 arguments - <keyspace>, <column>, <csv_file>");
            System.exit(1);
        }

        String keyspace = args[0];
        String col = args[1];
        filename = args[2];

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        RandomPartitioner part = new RandomPartitioner();

        File directory = new File(keyspace);
        if (!directory.exists())
            directory.mkdir();

        SSTableSimpleUnsortedWriter usersWriter = new SSTableSimpleUnsortedWriter(
            directory,
            part,
            keyspace,
            col,
            AsciiType.instance,
            null,
            32
        );

        String line;
        int lineNumber = 0;
        CsvParse entry = new CsvParse();

        long timestamp = System.currentTimeMillis() * 1000;

        while ((line = reader.readLine()) != null) {

            // Parse & Add Values
            entry.parse(line, ++lineNumber);
            ByteBuffer uuid = ByteBuffer.wrap(decompose(UUID.randomUUID()));
            usersWriter.newRow(uuid);
            for (int i=0;i<numCols;i++) {
                usersWriter.addColumn(bytes(colNames[i]), bytes(entry.d[i]), timestamp);
            }

            // Print nK
            if (lineNumber % 10000 == 0) {
              System.out.println((lineNumber / 1000) + "K");
            }

        }

        System.out.println("Successfully parsed " + lineNumber + " lines.");
        usersWriter.close();
        System.exit(0);

    }

    static class CsvParse {

        String d[] = new String[numCols];

        void parse(String line, int lineNumber) {
          String[] col = line.split(",");
          for (int j=0;j<numCols;j++) {
            d[j] = col[j].trim();
          }
        }

    }
}