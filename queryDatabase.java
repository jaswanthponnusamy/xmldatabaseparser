import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class queryDatabase {

public static void createTable(String querystatement) throws SQLException, IOException, ClassNotFoundException {

    PreparedStatement statement1 = GetConnection.getInstance().prepareStatement(querystatement);
    statement1.executeUpdate();

}

}
