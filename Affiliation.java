import java.util.*;
/**
 * This class represents an DAO for the _Affiliation table
 * @author ISTE-330 Group
 * @version 0.1
 */

public class Affiliation{
   private final static String TABLE_NAME = "_Affiliations";
   private int affiliationId;
   private String affiliationName;

   // CONSTRUCTORS
   /**
   * Creates an Affiliation
   */
   public Affiliation(){}
   
   /**
   * Creates an Affiliation with provided Id and no Name
   * @param affiliationId
   */
   public Affiliation(int affiliationId){ setAffiliationId(affiliationId); }

  /**
   * Creates an Affiliation with provided Name and no Id
   * @param affiliationName
   */
   public Affiliation(String affiliationName){ setAffiliationName(affiliationName); }

  /**
   * Creates an Affiliation with provided Id and Name
   * @param affiliationId
   * @param affiliationName
   */
   public Affiliation(int affiliationId, String affiliationName){
      setAffiliationId(affiliationId);
      setAffiliationName(affiliationName);
   }
   
   // ACCESSORS
   /**
   * Getter for the affiliationId
   * @return affiliationId
   */
   public int getAffiliationId() { return affiliationId; }

   /**
   * Getter for the affiliationName
   * @return affiliationName
   */
   public String getAffiliationName() { return affiliationName; }

   // MUTATORS
   /**
   * Setter for the affiliationId
   * @param affiliationId
   */
   public void setAffiliationId(int affiliationId) { this.affiliationId = affiliationId; }

   /**
   * Setter method for the affiliationName
   * @param affiliationName
   */
   public void setAffiliationName(String affiliationName) { this.affiliationName = affiliationName; }

   // CRUD
   public void post(Database db) throws DLException {
      String sql = "INSERT INTO `" + TABLE_NAME + "` (affiliationID, affiliationName) SELECT MAX(affiliationID) + 1, ? FROM `" + TABLE_NAME + "`;";
      int result = db.setData(sql, affiliationName);
      if (result != 1) {
         HashMap<String, String> m = new HashMap<String, String>(5);
         m.put("Class", this.getClass().getName());
         m.put("Method", "post(Database db):void");
         m.put("Database", db.toString());
         m.put("Affiliation Name", affiliationName);
         m.put("Modified rows count", String.valueOf(result));
         throw new DLException("Method call modified unexpected number of rows.", m);
      }
      sql = "SELECT affiliationID FROM `" + TABLE_NAME + "` WHERE affiliationName = ? LIMIT 1;";
      setAffiliationId((int)(db.getValue(sql, affiliationName)));
   }//post

   public void fetch(Database db) throws DLException {
      String sql = "SELECT affiliationName FROM `" + TABLE_NAME + "` WHERE affiliationID = ? ;";
      List<Object> result = db.getRow(sql, affiliationId);
      affiliationName = (String) result.get(0);
   }//fetch

   public void put(Database db) throws DLException {
      String sql = "UPDATE `" + TABLE_NAME + "` SET affiliationName= ? WHERE affiliationID = ? ;";
      ArrayList<Object> params = new ArrayList<>(2);
      params.add(affiliationName);
      params.add(affiliationId);
      int result = db.setData(sql, params);
      if (result != 1) {
         HashMap<String, String> m = new HashMap<String, String>(5);
         m.put("Class", this.getClass().getName());
         m.put("Method", "put(Database db):void");
         m.put("Database", db.toString());
         m.put("Affiliation Name", affiliationName);
         m.put("Modified rows count", String.valueOf(result));
        throw new DLException("Method call modified unexpected number of rows.", m);
      }
   }//put
   
   public void delete(Database db) throws DLException {
      String sql = "DELETE FROM `" + TABLE_NAME + "` WHERE affiliationID = ? ;";
      int result = db.setData(sql, affiliationId);
      if (result != 1) {
         HashMap<String, String> m = new HashMap<String, String>(5);
         m.put("Class", this.getClass().getName());
         m.put("Method", "delete(Database db):void");
         m.put("Database", db.toString());
         m.put("Affiliation Id", String.valueOf(affiliationId));
         m.put("Modified rows count", String.valueOf(result));
         throw new DLException("Method call modified unexpected number of rows.", m);
      }
   }//delete

   // OTHER
   public static int findAffiliationId(Database db, String affiliationName) throws DLException {
      String sql = "SELECT affiliationID FROM `" + TABLE_NAME + "` WHERE affiliationName = ? ;";
      Object result = db.getValue(sql, affiliationName);
      if (result == null) return 0;
      return (int) result;
   }

   public static String findAffiliationName(Database db, int affiliationId) throws DLException {
      String sql = "SELECT affiliationName FROM `" + TABLE_NAME + "` WHERE affiliationID = ? ;";
      Object result = db.getValue(sql, affiliationId);
      return (String) result;
   }
}//end of Affiliation class