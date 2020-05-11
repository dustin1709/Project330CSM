import java.util.Date;

public class Configuration {
   private DLConfiguration conf;
   private Database db;

   // CONSTRUCTORS
   public Configuration(Database db) {
      try {
         this.db = db;
         conf = new DLConfiguration();
         conf.fetch(db);
      } catch (DLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
  }

   // ACCESSORS
   public Date getSubmissionOpen() { return conf.getSubmissionOpen(); }
   public Date getSubmissionClose() { return conf.getSubmissionClose(); }
   public String getPCEmail() { return conf.getPcEmail(); }
   public String getPCName() {return conf.getPcName(); }
   public String getPC2Email() { return conf.getPc2Email(); }
   public String getPC2Name() {return conf.getPc2Name(); }
   public String getShortName() { return conf.getShortName(); }
   public String getLogoFile() { return conf.getLogoFile(); }

   // MUTATORS
   public void setSubmissionOpen(String token, Date submissionOpen) {
      if (User.isAdmin(token)) {
         try {
            conf.setSubmissionOpen(submissionOpen);
            conf.put(db);
         } catch (DLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
  }

   public void setSubmissionClose(String token, Date submissionClose) {
      if (User.isAdmin(token)) {
         try {
            conf.setSubmissionClose(submissionClose);
            conf.put(db);
         } catch (DLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   public void setPCEmail(String token, String PCEmail) {
      if (User.isAdmin(token)) {
         try {
            conf.setPCEmail(PCEmail);
            conf.put(db);
         } catch (DLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   public void setPCName(String token, String PCName) { 
      if (User.isAdmin(token)) {
         try {
            conf.setPCName(PCName);
            conf.put(db);
         } catch (DLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   public void setPC2Email(String token, String PC2Email) {
      if (User.isAdmin(token)) {
         try {
            conf.setPC2Email(PC2Email);
            conf.put(db);
         } catch (DLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   public void setPC2Name(String token, String PC2Name) {
      if (User.isAdmin(token)) {
         try {
            conf.setPC2Name(PC2Name);
            conf.put(db);
         } catch (DLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   public void setShortName(String token, String shortName) {
      if (User.isAdmin(token)) {
         try {
            conf.setShortName(shortName);
            conf.put(db);
         } catch (DLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   public void setLogoFile(String token, String logoFile) {
      if (User.isAdmin(token)) {
         try {
            conf.setLogoFile(logoFile);
            conf.put(db);
         } catch (DLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   // CRUD
   public void post(String token){
      if (User.isAdmin(token)){
         try{
            conf.post(db);
         }catch (DLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   public void fetch(String token){
      try{
         conf.fetch(db);
      }catch (DLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public void put(String token){
      if (User.isAdmin(token)){
         try{
            conf.put(db);
         }catch (DLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   public void delete(String token){
      if (User.isAdmin(token)){
         try{
            conf.delete(db);
         }catch (DLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }
}