import java.util.*;

public class PaperSubject {
   private final static String TABLE_NAME = "papersubjects";
   private int paperId;
   private List<Integer> subjectIds = new ArrayList<Integer>();

   // CONSTRUCTORS
   PaperSubject() {}

   PaperSubject(int paperId) { this.paperId = paperId; }
   
   PaperSubject(int paperId, List<Integer> subjectIds){
      this.paperId = paperId;
      this.subjectIds = subjectIds;
   }

   // ACCESSORS
   public int getPaperId() { return paperId; }	
   public List<Integer> getSubjectIds() { return subjectIds; }	
   
   // MUTATORS
   public void setPaperId(int paperId) { this.paperId = paperId; }	
   public void setSubjectIds(List<Integer> subjectIds) { this.subjectIds = subjectIds; }

   // toString
   public String toString(){
      StringBuilder sb = new StringBuilder();
      sb.append("Paper subject class\n");
      if (paperId == 0) {
         sb.append("Not initialized\n");
         return sb.toString();
      }
      sb.append("Paper ID: " + paperId + '\n');
      sb.append(subjectIds.size() + " subjects\n");
      ListIterator<Integer> li = subjectIds.listIterator();
      while (li.hasNext()) sb.append(li.next() + " ");
      sb.append('\n');
      return sb.toString();
   }

   // CRUD
   public void post(Database db) throws DLException {
      String sql = "INSERT INTO `" + TABLE_NAME + "` (paperId, subjectId) VALUES( ?, ? );";
      List<Object> params = new ArrayList<Object>(2);
      params.add(paperId);
      params.add(null);
      ListIterator<Integer> li = subjectIds.listIterator(); 
      while (li.hasNext()){
         params.set(1, li.next());
         int result = db.setData(sql, params);
         if (result != 1) {
            HashMap<String, String> m = new HashMap<String, String>(6);
            m.put("Class", this.getClass().getName());
            m.put("Method", "post(Database db):void");
            m.put("Database", db.toString());
            m.put("Paper ID", String.valueOf(paperId));
            String ids = "";
            li = subjectIds.listIterator();
            while (li.hasNext()) ids += String.valueOf(li.next()) + ", ";
            m.put("Subject IDs",String.valueOf(ids));
            m.put("Modified rows count", String.valueOf(result));
            throw new DLException("Method call modified unexpected number of rows.", m);
         }
      }
   }//post

   public void fetch(Database db) throws DLException {
      String sql = "SELECT subjectId FROM `" + TABLE_NAME + "` WHERE paperId = ? ;";
      List<Object> result = db.getList(sql, paperId);
      subjectIds.clear();
      ListIterator<Object> li = result.listIterator();
      while (li.hasNext()) subjectIds.add((int) li.next());
   }//fetch

   public void put(Database db) throws DLException {
      db.startTrans();
      String sql = "SELECT subjectId FROM `" + TABLE_NAME + "` WHERE paperId = ? ;";
      List<Object> results = db.getList(sql, paperId);
      sql =  "DELETE FROM `" + TABLE_NAME + "` WHERE paperId = ? AND subjectId = ?;";
      List<Object> params = new ArrayList<Object>(2);
      params.add(paperId);
      params.add(null);
      for (Object o: results)
         if (!subjectIds.contains(o)) {
            params.set(1, o);
            db.setData(sql, params);
         }
      sql =  "INSERT INTO `" + TABLE_NAME + "` (paperId, subjectId) VALUES( ?, ? );";
      for (Object o: subjectIds)
         if (!results.contains(o)) {
            params.set(1, o);
            db.setData(sql, params);
         }
      db.endTrans();
   }//put
   
   public void delete(Database db) throws DLException {
      String sql = "DELETE FROM `" + TABLE_NAME + "` WHERE paperId = ? ;";
      db.setData(sql, paperId);
   }//delete

   //SPECIAL
   public static void setPaperSubject(Database db, int paperId, int subjectId) throws DLException {
      String sql = "SELECT COUNT(*) FROM `" + TABLE_NAME + "` WHERE (paperId = ? AND subjectId = ?);";
      List<Object> params = new ArrayList<Object>(2);
      params.add(paperId);
      params.add(subjectId);
      Object result = db.getValue(sql, params);
      if (result == null || result.equals(0)) {
         sql = "INSERT INTO `" + TABLE_NAME + "` (paperId, subjectId) VALUES( ?, ? );";
         int count = db.setData(sql, params);
         if (count != 1) {
            HashMap<String, String> m = new HashMap<String, String>(6);
            m.put("Class", "PaperSubject");
            m.put("Method", "setPaperSubject(Database db, int paperId, int subjectId)");
            m.put("Database", db.toString());
            m.put("Paper ID", String.valueOf(paperId));
            m.put("Subject ID",String.valueOf(subjectId));
            m.put("Modified rows count", String.valueOf(result));
            throw new DLException("Method call modified unexpected number of rows.", m);
         }
      }
   }

}//PaperSubject