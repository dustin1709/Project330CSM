import java.util.*;

public class DLConfiguration {
   private final static String TABLE_NAME = "_configuration";

   private Date submissionOpen;
   private Date submissionClose;
   private String pcEmail;
   private String pcName;
   private String pc2Email;
   private String pc2Name;
   private String shortName;
   private String logoFile;

   // CONSTRUCTORS

   // ACCESSORS
   public Date getSubmissionOpen() { return submissionOpen; }
   public Date getSubmissionClose() { return submissionClose; }
   public String getPcEmail() { return pcEmail; }
   public String getPcName() {return pcName; }
   public String getPc2Email() { return pc2Email; }
   public String getPc2Name() {return pc2Name; }
   public String getShortName() { return shortName; }
   public String getLogoFile() { return logoFile; }

   // MUTATORS
   public void setSubmissionOpen(Date submissionOpen) { this.submissionOpen = submissionOpen; }
   public void setSubmissionClose(Date submissionClose) { this.submissionClose = submissionClose; }
   public void setPCEmail(String PCEmail) { this.pcEmail = PCEmail; }
   public void setPCName(String PCName) { this.pcName = PCName; }
   public void setPC2Email(String PC2Email) { this.pc2Email = PC2Email; }
   public void setPC2Name(String PC2Name) { this.pc2Name = PC2Name; }
   public void setShortName(String shortName) { this.shortName = shortName; }
   public void setLogoFile(String logoFile) { this.logoFile = logoFile; }

   // CRUD
   public void post(Database db) throws DLException {
      String sql = "INSERT INTO `" + TABLE_NAME + "` (SubmissionOpen, " + "SubmissionClose, " + "PCEmail, "
            + "PC2Email, " + "PCName, " + "PC2Name, " + "ShortName, " + "LogoFile) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
      List<Object> params = new ArrayList<Object>(8);
      params.add(submissionOpen);
      params.add(submissionClose);
      params.add(pcEmail);
      params.add(pc2Email);
      params.add(pcName);
      params.add(pc2Name);
      params.add(shortName);
      params.add(logoFile);
      int result = db.setData(sql, params);
      if (result != 1) {
         HashMap<String, String> m = new HashMap<String, String>(4);
         m.put("Class", this.getClass().getName());
         m.put("Method", "post(Database db):void");
         m.put("Database", db.toString());
         m.put("Modified rows count", String.valueOf(result));
         throw new DLException("Method call modified unexpected number of rows.", m);
      }
   }// post

   public void fetch(Database db) throws DLException {
      String sql = "SELECT SubmissionOpen, SubmissionClose, PCEmail, PC2Email, PCName, "
            + "PC2Name, ShortName, LogoFile FROM `" + TABLE_NAME + "`;";
      List<Object> result = db.getRow(sql, null);
      submissionOpen  = (Date) result.get(0);
      submissionClose = (Date) result.get(1);
      pcEmail         = (String) result.get(2);
      pc2Email        = (String) result.get(3);
      pcName          = (String) result.get(4);
      pc2Email        = (String) result.get(5);
      shortName       = (String) result.get(6);
      logoFile        = (String) result.get(7);
   } //fetch
    

   public void put(Database db) throws DLException {
      String sql = "UPDATE `" + TABLE_NAME + "` SET SubmissionOpen = ?, SubmissionClose = ?, "
                 + "PCEmail = ?, PC2Email = ?, PCName = ?, PC2Name = ?, ShortName = ?, LogoFile =?;";
      ArrayList<Object> params = new ArrayList<Object>(8);
      params.add(submissionOpen);
      params.add(submissionClose);
      params.add(pcEmail);
      params.add(pc2Email);
      params.add(pcName);
      params.add(pc2Name);
      params.add(shortName);
      params.add(logoFile);
      int result = db.setData(sql, params);
      if (result != 1) {
         HashMap<String, String> m = new HashMap<String, String>(3);
         m.put("Class", this.getClass().getName());
         m.put("Method", "put(Database db):void");
         m.put("Modified rows count", String.valueOf(result));
        throw new DLException("Method call modified unexpected number of rows.", m);
      }
   }//put

   public void delete(Database db) throws DLException {
      String sql = "DELETE FROM `" + TABLE_NAME + "`;";
      db.setData(sql, null);
   }//delete
}
