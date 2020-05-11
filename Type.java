import java.util.*;

public class Type {
  private final static String TABLE_NAME = "_types";

  private int     typeId;
  private String  typeName;

  // CONSTRUCTORS
  public Type() {}
  public Type(int typeId) { this.typeId = typeId; }
  public Type(String typeName) { this.typeName = typeName; }
  public Type(int typeId, String typeName){
    this.typeId = typeId;
    this.typeName = typeName;
  }

  // ACCESSORS
  public int getTypeId() { return typeId; }	
  public String getTypeName() { return typeName; }	

  // MUTATORS
  public void setTypeId(int typeId) { this.typeId = typeId; }	
  public void setTypeName(String typeName) { this.typeName = typeName; }

  // CRUD
  public void post(Database db) throws DLException {
    String sql = "INSERT INTO `" + TABLE_NAME + "` (typeId, typeName) SELECT MAX(typeId) + 1, ? FROM `" + TABLE_NAME + "`;";
    int result = db.setData(sql, typeId);
    if (result != 1) {
       HashMap<String, String> m = new HashMap<String, String>(5);
       m.put("Class", this.getClass().getName());
       m.put("Method", "post(Database db):void");
       m.put("Database", db.toString());
       m.put("Type Name", typeName);
       m.put("Modified rows count", String.valueOf(result));
       throw new DLException("Method call modified unexpected number of rows.", m);
    }
    sql = "SELECT typeId FROM `" + TABLE_NAME + "` WHERE typeName = ? LIMIT 1;";
    typeId = (int)(db.getValue(sql, typeName));
 }//post

 public void fetch(Database db) throws DLException {
    String sql = "SELECT typeName FROM `" + TABLE_NAME + "` WHERE typeId = ? ;";
    List<Object> result = db.getRow(sql, typeId);
    typeName = (String) result.get(0);
 }//fetch

 public void put(Database db) throws DLException {
    String sql = "UPDATE `" + TABLE_NAME + "` SET typeName= ? WHERE typeId = ? ;";
    ArrayList<Object> params = new ArrayList<>(2);
    params.add(typeName);
    params.add(typeId);
    int result = db.setData(sql, params);
    if (result != 1) {
       HashMap<String, String> m = new HashMap<String, String>(5);
       m.put("Class", this.getClass().getName());
       m.put("Method", "put(Database db):void");
       m.put("Database", db.toString());
       m.put("Type Name", typeName);
       m.put("Modified rows count", String.valueOf(result));
      throw new DLException("Method call modified unexpected number of rows.", m);
    }
 }//put
 
 public void delete(Database db) throws DLException {
    String sql = "DELETE FROM `" + TABLE_NAME + "` WHERE typeId = ? ;";
    int result = db.setData(sql, typeId);
    if (result != 1) {
       HashMap<String, String> m = new HashMap<String, String>(5);
       m.put("Class", this.getClass().getName());
       m.put("Method", "delete(Database db):void");
       m.put("Database", db.toString());
       m.put("Type Id", String.valueOf(typeId));
       m.put("Modified rows count", String.valueOf(result));
       throw new DLException("Method call modified unexpected number of rows.", m);
    }
 }//delete

 // OTHER
 public static int findTypeId(Database db, String typeName) throws DLException {
    String sql = "SELECT typeId FROM `" + TABLE_NAME + "` WHERE typeName = ? ;";
    Object result = db.getValue(sql, typeName);
    if (result == null) return 0;
    return (int) result;
 }

 public static String findTypeName(Database db, int typeId) throws DLException {
    String sql = "SELECT typeName FROM `" + TABLE_NAME + "` WHERE typeId = ? ;";
    Object result = db.getValue(sql, typeId);
    return (String) result;
 }
}
