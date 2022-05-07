package org.prgrms.awaker.global;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Utils {
    public static LocalDateTime now(){
        return LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }

    public static UUID toUUID(byte[] bytes){
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    public static LocalDateTime toLocalDateTime(Timestamp timestamp){
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    public static UUID toNullableUUID(ResultSet resultSet, String column) throws SQLException {
        return resultSet.getBytes(column) == null ? null : toUUID(resultSet.getBytes(column));
    }
}
