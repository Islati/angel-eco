package io.gridmc.angel.db;

import io.gridmc.angel.Angel;
import io.gridmc.angel.listeners.AngelPlayer;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.UUID;

/**
 * Business logic for the database operations.
 */
public class DatabaseController {

    private static final String CREATE_ECONOMY_TABLE = "CREATE TABLE angel_eco (id SERIAL PRIMARY KEY, uuid text NOT NULL, name text  NOT NULL, balance double precision NOT NULL);";

    private static final String INSERT_PLAYER_DATA = "INSERT INTO angel_eco(uuid, name, balance) VALUES (?,?,?);";

    private static final String SELECT_PLAYER_DATA = "SELECT * FROM angel_eco WHERE uuid=? LIMIT 1;";

    private static final String UPDATE_PLAYER_DATA = "UPDATE angel_eco SET balance = ? WHERE uuid=?;";

    private String url;
    private String user;
    private String pass;
    private String database;

    private Connection con;

    public DatabaseController(String url, String user, String pass, String database) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.database = database;
    }

    public boolean hasPlayerData(AngelPlayer player) {
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SELECT_PLAYER_DATA);


            statement.setString(1, player.getUuid().toString());

            ResultSet set = statement.executeQuery();

            return set.next();
        } catch (SQLException exception) {
        }
        return false;
    }

    public double getPlayerBalance(AngelPlayer player) {
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SELECT_PLAYER_DATA);

            statement.setString(1, player.getUuid().toString());
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return set.getDouble("balance");
            }
        } catch (SQLException exception) {
        }


        return 0;
    }

    public boolean updatePlayerData(AngelPlayer player) {
        if (!hasPlayerData(player)) {
            insertDefaultPlayerData(player);
        }

        try {
            PreparedStatement statement = con.prepareStatement(UPDATE_PLAYER_DATA);
            statement.setDouble(1, player.getBalance());
            statement.setString(2, player.getUuid().toString());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException exception) {
        }

        return false;
    }

    public boolean insertDefaultPlayerData(AngelPlayer player) {

        try {
            PreparedStatement statement = con.prepareStatement(INSERT_PLAYER_DATA);

            statement.setString(1, player.getUuid().toString());
            statement.setString(2, player.getName());
            statement.setDouble(3, player.getBalance());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    return rs.next();
                } catch (SQLException exception) {
                    return false;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    /**
     * Attempt a connection to the database.
     *
     * @return
     */
    public boolean connect() {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Angel.getInstance().getLogger().info("Unable to locate PostgreSQL Driver");
            return false;
        }

        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean tableExists() {
        boolean exists = false;
        Statement statement = null;
        try {
            statement = con.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM angel_eco LIMIT 1;");
            exists = true;
        } catch (PSQLException exception) {

        } catch (SQLException throwables) {
//            throwables.printStackTrace();
        }

        return exists;
    }

    /**
     * Handle setup of the database, like tables, and default data.
     */
    public boolean setup() {
        boolean success = false;

        if (!tableExists()) {
            try {
                Statement stmtCreateTable = con.createStatement();

                /* Attempt to create the economy table */
                stmtCreateTable.execute(CREATE_ECONOMY_TABLE);

                success = true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            /* If it exists then it's success */
            success = tableExists();
        } else {
            success = true;
        }

        return success;
    }
}
