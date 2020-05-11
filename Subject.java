import java.util.*;

public class Subject {
  private final static String TABLE_NAME = "_subjects";

  private int    subjectId;
  private String subjectName;

  // CONSTRUCTORS
  public Subject() {}
  public Subject(int subjectId) { this.subjectId = subjectId; }
  public Subject(String subjectName) { this.subjectName = subjectName; }
  public Subject(int subjectId, String subjectName) {
    this.subjectId = subjectId;
    this.subjectName = subjectName;
  }

  // ACCESSORS
  public void setSubjectId(int subjectId) { this.subjectId = subjectId; }	
  public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

  // MUTATORS
  public int getSubjectId() { return subjectId; }	
  public String getSubjectName() { return subjectName; }	

  // CRUD
  public void post(Database db) throws DLException {
    String sql = "INSERT INTO `" + TABLE_NAME + "` (subjectId, subjectName) SELECT MAX(subjectId) + 1, ? FROM `" + TABLE_NAME + "`;";
    int result = db.setData(sql, subjectName);
    if (result != 1) {
       HashMap<String, String> m = new HashMap<String, String>(5);
       m.put("Class", this.getClass().getName());
       m.put("Method", "post(Database db):void");
       m.put("Database", db.toString());
       m.put("Subject Name", subjectName);
       m.put("Modified rows count", String.valueOf(result));
       throw new DLException("Method call modified unexpected number of rows.", m);
    }
    sql = "SELECT subjectId FROM `" + TABLE_NAME + "` WHERE subjectName = ? LIMIT 1;";
    setSubjectId((int)(db.getValue(sql, subjectName)));
 }//post

 public void fetch(Database db) throws DLException {
    String sql = "SELECT subjectName FROM `" + TABLE_NAME + "` WHERE subjectId = ? ;";
    List<Object> result = db.getRow(sql, subjectId);
    subjectName = (String) result.get(0);
 }//fetch

 public void put(Database db) throws DLException {
    String sql = "UPDATE `" + TABLE_NAME + "` SET subjectName= ? WHERE subjectId = ? ;";
    ArrayList<Object> params = new ArrayList<>(2);
    params.add(subjectName);
    params.add(subjectId);
    int result = db.setData(sql, params);
    if (result != 1) {
       HashMap<String, String> m = new HashMap<String, String>(5);
       m.put("Class", this.getClass().getName());
       m.put("Method", "put(Database db):void");
       m.put("Database", db.toString());
       m.put("Subject Name", subjectName);
       m.put("Modified rows count", String.valueOf(result));
      throw new DLException("Method call modified unexpected number of rows.", m);
    }
 }//put
 
 public void delete(Database db) throws DLException {
    String sql = "DELETE FROM `" + TABLE_NAME + "` WHERE subjectId = ? ;";
    int result = db.setData(sql, subjectId);
    if (result != 1) {
       HashMap<String, String> m = new HashMap<String, String>(5);
       m.put("Class", this.getClass().getName());
       m.put("Method", "delete(Database db):void");
       m.put("Database", db.toString());
       m.put("Subject Id", String.valueOf(subjectId));
       m.put("Modified rows count", String.valueOf(result));
       throw new DLException("Method call modified unexpected number of rows.", m);
    }
 }//delete

  // SPECIAL
  public static int findSubjectId(Database db, String subjectName) throws DLException {
    String sql = "SELECT subjectId FROM `" + TABLE_NAME + "` WHERE subjectName = ? ;";
    Object result = db.getValue(sql, subjectName);
    if (result == null) return 0;
    return (int) result;
  }
}