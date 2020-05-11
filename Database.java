import java.sql.*;
import java.util.*;
/*
 * ISTE-330
 * Group Project
 * 2020-03-02
 *
 */

public class Database implements AutoCloseable{
  private Connection conn = null;
  // Default values
  private final String DEFAULT_DB_HOST = "localhost";
  private final int DEFAULT_DB_PORT = 3306;
  private final String DEFAULT_DB_USER = "root";
  private final String DEFAULT_DB_PASSWORD = "password1234";

  // Addition of "?useSSL=false" is now required to allow non-secure connection to MariaDb
  private final String CONNECTION_STRING = "jdbc:mysql://%s:%d/%s?useSSL=false";
  private final String SQL_DRIVER_NAME = "com.mysql.jdbc.Driver";

  private String host, database, user, password;
  private int port;
  boolean flag;

  // Constructors
  public Database() {
     host = DEFAULT_DB_HOST;
     port = DEFAULT_DB_PORT;
     database = "CSM";
     user = DEFAULT_DB_USER;
     password = DEFAULT_DB_PASSWORD;
  }
  
  public Database(String host, int port, String database, String user, String password) {
    this.host = host;
    this.port = port;
    this.database = database;
    this.user = user;
    this.password = password;
  }

  public Database(String host, String database, String user, String password) {
    this.host = host;
    this.port = DEFAULT_DB_PORT;
    this.database = database;
    this.user = user;
    this.password = password;
  }

  public Database(String database, String user, String password) {
    this.host = DEFAULT_DB_HOST;
    this.port = DEFAULT_DB_PORT;
    this.database = database;
    this.user = user;
    this.password = password;
  }

  public Database(String database, String password) {
    this.host = DEFAULT_DB_HOST;
    this.port = DEFAULT_DB_PORT;
    this.database = database;
    this.user = DEFAULT_DB_USER;
    this.password = password;
  }

  public Database(String database) {
    this.host = DEFAULT_DB_HOST;
    this.port = DEFAULT_DB_PORT;
    this.database = database;
    this.user = DEFAULT_DB_USER;
    this.password = DEFAULT_DB_PASSWORD;
  }

  // Establish the connection to the database with information provided in
  // the object properties
  public void connect() throws DLException {
    // If connection is already established, leave
    if (conn != null) {
      Map<String, String> m = new HashMap<String, String>();
      m.put("Class", this.getClass().getName());
      m.put("Method", "connect()");
      m.put("connection", conn.toString());
      throw new DLException(new Exception("Connection already opened"), m);
    }

    // Load the driver
    try {
      Class.forName(SQL_DRIVER_NAME);
    } catch (ClassNotFoundException cnfe) {
      Map<String, String> m = new HashMap<String, String>(3);
      m.put("Class", this.getClass().getName());
      m.put("Method", "connect()");
      m.put("Diver Name", SQL_DRIVER_NAME);
      throw new DLException(cnfe, m);
    }

    // Establish the connection
    String connectionString = String.format(CONNECTION_STRING, this.host, this.port, this.database);
    try {
      conn = DriverManager.getConnection(connectionString, this.user, this.password);
      flag=true;
    } catch (SQLException sqle) {
      Map<String, String> m = new HashMap<String, String>(5);
      m.put("Class", this.getClass().getName());
      m.put("Method", "connect()");
      m.put("Diver Name", SQL_DRIVER_NAME);
      m.put("Connection String", connectionString);
      m.put("DB Username", this.user);
      throw new DLException(sqle, m);
    }
  }

  // Closes connection to the database
  public void close() {
   // Close the connection.
   try {
      if (this.conn == null) return;
      if (this.conn.isClosed()) return;
         this.conn.close();
         this.conn = null;
      } catch (SQLException sqle) {}
   }

   private PreparedStatement prepare(String sql, List<String> val) throws DLException {
      PreparedStatement psmt = null;
      try {
         psmt = conn.prepareStatement(sql);
         for(int i=0; i<val.size(); i++) {
            psmt.setString(i+1, val.get(i));
         }
         return psmt;
      } catch(SQLException sqle) {
         HashMap<String, String> m = new HashMap<String, String>(5);
         m.put("Class", this.getClass().getName());
         m.put("Method", "prepare(String sql, List<String> val):PreparedStatement");
         m.put("Connection", conn.toString());
         m.put("SQL", sql);
         String values = "";
         ListIterator<String> li = val.listIterator();
         while(li.hasNext()) values += li.next() + '\n';
         m.put("Values", values);
         throw new DLException(sqle, m);
      }
   }//prepare
   
   public int executeStmt(String sql, List<String> val) throws DLException {
      try {
         PreparedStatement pr = prepare(sql, val);
         return pr.executeUpdate();
      } catch(SQLException sqle) {
         HashMap<String, String> m = new HashMap<String, String>(5);
         m.put("Class", this.getClass().getName());
         m.put("Method", "executeStmt(String sql, List<String> val):int");
         m.put("Connection", conn.toString());
         m.put("SQL", sql);
         String values = "";
         ListIterator<String> li = val.listIterator();
         while(li.hasNext()) values += li.next() + '\n';
         m.put("Values", values);
         throw new DLException(sqle, m);
      }
   }//executeStmt

   public List<Object> getList(String sql, Object param) throws DLException {
      List<Object> result = new ArrayList<Object>();
      try {
         PreparedStatement preparedStatement = conn.prepareStatement(sql);
         if (param != null) preparedStatement.setObject(1, param);
         ResultSet rs = preparedStatement.executeQuery();
         while(rs.next()) result.add(rs.getObject(1));
         return result;
      } catch(SQLException sqle) {
         HashMap<String, String> m = new HashMap<String, String>(4);
         m.put("Class", this.getClass().getName());
         m.put("Method", "getList(String sql, Object param):List<Object>");
         m.put("SQL", sql);
         m.put("Parameter", String.valueOf(param));
         throw new DLException(sqle, m);
      }
   }//getList

   public List<Object> getList(String sql, List<Object> params) throws DLException {
      List<Object> result = new ArrayList<Object>();
      try {
         PreparedStatement preparedStatement = conn.prepareStatement(sql);
         if (params != null) {
            ListIterator<Object> listIterator = params.listIterator();
            while (listIterator.hasNext())
            preparedStatement.setObject(listIterator.nextIndex() + 1, listIterator.next());
         }
         ResultSet rs = preparedStatement.executeQuery();
         while(rs.next()) result.add(rs.getObject(1));
         return result;
      } catch(SQLException sqle) {
         HashMap<String, String> m = new HashMap<String, String>(4);
         m.put("Class", this.getClass().getName());
         m.put("Method", "getList(String sql, List<Object> params):List<Object>");
         m.put("SQL", sql);
         String s = "";
         ListIterator<Object> li = params.listIterator();
         while (li.hasNext()) s += String.valueOf(li.next()) + ", ";
         m.put("Parameters", s);
         throw new DLException(sqle, m);
      }
   }//getList

   public List<List<Object>> getData(String sql, Object param) throws DLException {
      List<List<Object>> result = new ArrayList<List<Object>>();
      try {
         PreparedStatement preparedStatement = conn.prepareStatement(sql);
         if (param != null) preparedStatement.setObject(1, param);
         ResultSet rs = preparedStatement.executeQuery();
         int colCount = rs.getMetaData().getColumnCount();
         List<Object> row;
         while(rs.next()) {
            row = new ArrayList<Object>(colCount);
            for(int i = 1; i <= colCount; i++) row.add(rs.getObject(i));
            result.add(row);
         }
         return result;
      } catch(SQLException sqle) {
         HashMap<String, String> m = new HashMap<String, String>(4);
         m.put("Class", this.getClass().getName());
         m.put("Method", "getData(String sql, Object param):List<List<Object>>");
         m.put("SQL", sql);
         m.put("Parameter", String.valueOf(param));
         throw new DLException(sqle, m);
      }
   }//getData

   public List<List<Object>> getData(String sql, List<Object> params) throws DLException {
      List<List<Object>> result = new ArrayList<List<Object>>();
      try {
         PreparedStatement preparedStatement = conn.prepareStatement(sql);
         if (params != null) {
            ListIterator<Object> listIterator = params.listIterator();
            while (listIterator.hasNext())
            preparedStatement.setObject(listIterator.nextIndex() + 1, listIterator.next());
         }
         ResultSet rs = preparedStatement.executeQuery();
         int colCount = rs.getMetaData().getColumnCount();
         List<Object> row;
         while(rs.next()) {
            row = new ArrayList<Object>(colCount);
            for(int i = 1; i <= colCount; i++) row.add(rs.getObject(i));
            result.add(row);
         }
         return result;
      } catch(SQLException sqle) {
         HashMap<String, String> m = new HashMap<String, String>(4);
         m.put("Class", this.getClass().getName());
         m.put("Method", "getData(String sql, List<Object> params):List<List<Object>>");
         m.put("SQL", sql);
         String s = "";
         ListIterator<Object> li = params.listIterator();
         while (li.hasNext()) s += String.valueOf(li.next()) + ", ";
         m.put("Parameters", s);
         throw new DLException(sqle, m);
      }
   }//getData

   public List<Object> getRow(String sql, Object parameter) throws DLException {
      try {
         PreparedStatement statement = conn.prepareStatement(sql);
         if (parameter != null) statement.setObject(1, parameter);
         ResultSet resultSet = statement.executeQuery();
         if (!resultSet.next()) return null;
         List<Object> result = new ArrayList<Object>();
         int columnCount = resultSet.getMetaData().getColumnCount();
         for(int i = 1; i <= columnCount; i++)
            result.add(resultSet.getObject(i));
         return result;
      } catch (SQLException sqle) {
         HashMap<String, String> m = new HashMap<String, String>(4);
         m.put("Class", this.getClass().getName());
         m.put("Method", "getRow(String sql, Object parameter):List<Object>");
         m.put("SQL", sql);
         m.put("Parameters", parameter == null? "null": parameter.toString());
         throw new DLException(sqle, m);
      }
   }//getRow

   public Object getValue(String sql, Object parameter) throws DLException {
      try {
         PreparedStatement statement = conn.prepareStatement(sql);
         statement.setObject(1, parameter);
         ResultSet resultSet = statement.executeQuery();
         if (!resultSet.next()) return null;
         return resultSet.getObject(1);
      } catch (SQLException sqle) {
         HashMap<String, String> m = new HashMap<String, String>(4);
         m.put("Class", this.getClass().getName());
         m.put("Method", "getValue(String sql, Object parameter):Object");
         m.put("SQL", sql);
         m.put("Parameters", parameter.toString());
         throw new DLException(sqle, m);
      }
   }//getValue
    
   public int setData(String sql, List<Object> params) throws DLException {
      try {
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        if (params != null) {
          ListIterator<Object> listIterator = params.listIterator();
          while (listIterator.hasNext())
          preparedStatement.setObject(listIterator.nextIndex() + 1, listIterator.next());
        }
        return preparedStatement.executeUpdate();
      } catch (SQLException sqle) {
         HashMap<String, String> m = new HashMap<String, String>(4);
         m.put("Class", this.getClass().getName());
         m.put("Method", "setData(String sql, List<Object> params):int");
         m.put("SQL", sql);
         m.put("Parameters", params.toString());
        throw new DLException(sqle, m);
      }
    }//setData
  
    public int setData(String sql, Object param) throws DLException {
      try {
         PreparedStatement preparedStatement = conn.prepareStatement(sql);
         if (param != null) preparedStatement.setObject(1, param);
         return preparedStatement.executeUpdate();
      } catch (SQLException sqle) {
         HashMap<String, String> m = new HashMap<String, String>(4);
         m.put("Class", this.getClass().getName());
         m.put("Method", "setData(String sql, Object param):int");
         m.put("SQL", sql);
         m.put("Parameter", param.toString());
         throw new DLException(sqle, m);
      }
    }//setData

   public boolean startTrans() {
      boolean boo = true;
      try {
         if(flag==true) {
            conn.setAutoCommit(false);
         } else {
            boo = false;
            conn.setAutoCommit(true);
         }
      } catch(SQLException sqle) {
         boo = false;
      }
      return boo;
   }//startTrans
   
   public void endTrans() {
      try {
         if(flag==true) {
            conn.commit();
            conn.setAutoCommit(true);
         } else if(flag==false) {
            rollbackTrans();
            conn.setAutoCommit(true);
         }
      } catch(SQLException sqle) {}
   }//startTrans
   
   public void rollbackTrans() {
      try {
         conn.rollback();
      } catch(SQLException sqle) {}
   }//rollbackTrans
}