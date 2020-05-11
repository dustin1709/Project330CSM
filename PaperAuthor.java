import java.util.*;

public class PaperAuthor {
   private final static String TABLE_NAME = "paperauthors";

   private int paperId;
   private int[] userIds;

   // CONSTRUCTORS
   public PaperAuthor() {}
   public PaperAuthor(int paperId) { this.paperId = paperId; }
   public PaperAuthor(int paperId, int[] authorIds){
      this.paperId = paperId;
      this.userIds = authorIds;
   }
      
   // ACCESSORS
   public int getPaperId() { return paperId; }	
   public int[] getUserIds() { return userIds; }	

   // MUTATORS
   public void setPaperId(int paperId) { this.paperId = paperId; }	
   public void setUserId(int[] userIds) { this.userIds = userIds; }	

   // CRUD
   public void post(Database db) throws DLException {
      String sql = "INSERT INTO `" + TABLE_NAME + "` (paperId, userId, displayOrder) VALUES( ?, ?, ? );";
      List<Object> params = new ArrayList<Object>(3);
      params.add(paperId);
      for (int i = 0; i < userIds.length; i++){
         params.set(1, userIds[i]);
         params.set(2, i);
         int result = db.setData(sql, params);
         if (result != 1) {
            HashMap<String, String> m = new HashMap<String, String>(6);
            m.put("Class", this.getClass().getName());
            m.put("Method", "post(Database db):void");
            m.put("Database", db.toString());
            m.put("Paper ID", String.valueOf(paperId));
            m.put("User ID", String.valueOf(userIds[i]));
            m.put("Modified rows count", String.valueOf(result));
            throw new DLException("Method call modified unexpected number of rows.", m);
         }
      }
   }//post

   public void fetch(Database db) throws DLException {
      String sql = "SELECT userId FROM `" + TABLE_NAME + "` WHERE paperId = ? ORDER BY displayOrder ASC;";
      List<Object> result = db.getList(sql, paperId);
      userIds = new int[result.size()];
      for (int i = 0; i < result.size(); i++) userIds[i] = (int) result.get(i);
   }//fetch

   public void put(Database db) throws DLException {
      db.startTrans();
      delete(db);
      post(db);
      db.endTrans();
   }//put
   
   public void delete(Database db) throws DLException {
      String sql = "DELETE FROM `" + TABLE_NAME + "` WHERE paperId = ? ;";
      db.setData(sql, paperId);
   }//delete

   //this method for admin, admin can delete any paper author
   public void deletePaperAuthor(String id) throws DLException {
      try {
         // if(user.getIsAdmin()==1) {
            Database db = new Database();
            db.connect();
            ArrayList<String> val = new ArrayList<String>();
            val.add(id);
            db.setData("DELETE FROM PaperAuthors WHERE paperId=?", val);
            System.out.println("1 row deleted in PaperAuthors.");
            db.close();
         // } else {
            // System.out.println("You don't have permission to delete.");
         // }
      } catch(Exception e) {
         new DLException(e);
         // System.out.println("Unable to delete.");
      }
   }//deletePaperAuthor
  

   // SPECIAL
   public static void setPaperAuthor(Database db, int paperId, int authorId, int displayOrder) throws DLException {
      String sql = "SELECT displayOrder FROM `" + TABLE_NAME + "` WHERE (paperId = ? AND userId = ?);";
      List<Object> params = new ArrayList<Object>(3);
      params.add(paperId);
      params.add(authorId);
      Object result = db.getValue(sql, params);
      if (result == null) {
         sql = "INSERT INTO `" + TABLE_NAME + "` (paperId, userId, displayOrder) VALUES( ?, ? , ?);";
         params.add(displayOrder);
         int count = db.setData(sql, params);
         if (count != 1) {
            HashMap<String, String> m = new HashMap<String, String>(6);
            m.put("Class", "PaperSubject");
            m.put("Method", " setPaperAuthor(Database db, int paperId, int authorId, int displayOrder)");
            m.put("Database", db.toString());
            m.put("Paper ID", String.valueOf(paperId));
            m.put("Subject ID",String.valueOf(authorId));
            m.put("Modified rows count", String.valueOf(result));
            throw new DLException("Method call modified unexpected number of rows.", m);
         }
         return;
      }
      if (result.equals(displayOrder)) return;
      sql = "UPDATE `" + TABLE_NAME + "` SET displayOrder = ? WHERE (paperId = ? AND userId = ?);";
      params = new ArrayList<Object>(2);
      params.add(displayOrder);
      params.add(paperId);
      params.add(authorId);
      int count = db.setData(sql, params);
      if (count != 1) {
         HashMap<String, String> m = new HashMap<String, String>(5);
         m.put("Class", "PaperAuthor");
         m.put("Method", "setPaperAuthor(Database db, int paperId, int authorId, int displayOrder)");
         m.put("Database", db.toString());
         m.put("Paper ID", String.valueOf(paperId));
         m.put("Author ID", String.valueOf(authorId));
         m.put("Display Order", String.valueOf(displayOrder));
         m.put("Modified rows count", String.valueOf(result));
        throw new DLException("Method call modified unexpected number of rows.", m);
      }
   }
}//end of PaperAuthor
