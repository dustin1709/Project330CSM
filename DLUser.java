import java.math.*;
import java.security.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

public class DLUser{
   private final static String TABLE_NAME = "users";

   protected int userId;
   protected String lname;
   protected String fname;
   protected String email;
   protected String pwd;
   protected String expr;
   
   //affiliation name
   protected String affiliation;
   protected int    isAdmin;
   protected int    affiliationId;
   
   // CONSTRUCTORS
   public DLUser() {}
   public DLUser(int Id) { userId = Id; }

   // ACCESSORS
   public int getUserId() { return userId; }	
   public String getLastName() { return lname; }	
   public String getFirstName() { return fname; }
   public String getEmail() { return email; }	
   public String getPassword() {  return pwd; }
   public String getExpiration() {  return expr; }
   public int getIsAdmin() {  return isAdmin; }
   public int getAffiliationId() { return affiliationId; }

   // MUTATORS
   public void setUserId(int userId) { this.userId = userId; }	
   public void setLastName(String lastName) { this.lname = lastName; }	
   public void setFirstName(String firstName) { this.fname = firstName; }	
   public void setEmail(String email) { this.email = email; }	
   public void setPassword(String pwd) { this.pwd = hash(pwd); }	
   public void setExpiration(String expiration) { this.expr = expiration; }	
   public void setIsAdmin(int isAdmin) { this.isAdmin = isAdmin; }	
   public void setAffiliationId(int affiliationId) { this.affiliationId = affiliationId; }

   // toString
   public String toString(){
      StringBuilder sb = new StringBuilder();
      sb.append("*** DLUser object\n");
      sb.append("User ID: " + userId + '\n');
      sb.append("Name: " + fname + " " + lname + "\n");
      sb.append("Email: " + email + "\n");
      if (pwd == null)
         sb.append("Password: not set\n");
      else
         sb.append("Password: " + pwd + "\n");
      sb.append("Password expiration: " + expr + '\n');
      sb.append("Affiliation ID: " + affiliationId + '\n');
      sb.append("Affiliation: " + affiliation + '\n');
      sb.append("Is admin: " + (isAdmin == 1 ? "True": "False"));
      return sb.toString();
   }

   // CRUD
   //post - enter a new user
   public void post(Database db) throws DLException {
      if ((userId = findUser(db, email)) != 0){ put(db); return; }
      String sql = "INSERT INTO `" + TABLE_NAME + "` (userId, lastName, firstName, email, pswd, expiration, isAdmin, affiliationId) SELECT MAX(userId) + 1, ?, ?, ?, ?, ?, ?, ? FROM `" + TABLE_NAME + "`;";
      List<Object> params = new ArrayList<Object>(7);
      params.add(lname);
      params.add(fname);
      params.add(email);
      params.add(pwd);
      params.add(expr);
      params.add(isAdmin);
      params.add(affiliationId);
      int result = db.setData(sql, params);
      if (result != 1) {
         HashMap<String, String> m = new HashMap<String, String>(5);
         m.put("Class", this.getClass().getName());
         m.put("Method", "post(Database db):void");
         m.put("Database", db.toString());
         m.put("User: ", toString());
         m.put("Modified rows count", String.valueOf(result));
         throw new DLException("Method call modified unexpected number of rows.", m);
      }
      sql = "SELECT userId FROM `" + TABLE_NAME + "` WHERE email = ? LIMIT 1;";
      setUserId((int)(db.getValue(sql, email)));
   } //post

   public void fetch(Database db) throws DLException {
      String sql = "SELECT lastName, firstName, email, pswd, expiration, isAdmin, affiliationId FROM `" + TABLE_NAME + "` WHERE userId = ? ;";
      List<Object> result = db.getRow(sql, userId);
      lname         = (String) result.get(0);
      fname         = (String) result.get(1);
      email         = (String) result.get(2);
      pwd           = (String) result.get(3);
      expr          = (String) result.get(4);
      isAdmin       = (int)    result.get(5);
      affiliationId = (int)    result.get(6);
   }//fetch

   public void put(Database db) throws DLException {
      String sql = "UPDATE `" + TABLE_NAME + "` SET lastName = ?, firstName = ?, email = ?, pswd = ?, expiration = ? , isAdmin = ?, affiliationId = ? WHERE userId = ? ;";
      List<Object> params = new ArrayList<Object>(8);
      params.add(lname);
      params.add(fname);
      params.add(email);
      params.add(pwd);
      params.add(expr);
      params.add(isAdmin);
      params.add(affiliationId);
      params.add(userId);
      int result = db.setData(sql, params);
      if (result != 1) {
         HashMap<String, String> m = new HashMap<String, String>(5);
         m.put("Class", this.getClass().getName());
         m.put("Method", "put(Database db):void");
         m.put("Database", db.toString());
         m.put("User ID", String.valueOf(userId));
         m.put("Modified rows count", String.valueOf(result));
         throw new DLException("Method call modified unexpected number of rows.", m);
      }
   }//update

   public void delete(Database db) throws DLException {
      String sql = "DELETE FROM `" + TABLE_NAME + "` WHERE userId = ? ;";
      int result = db.setData(sql, userId);
      if (result != 1) {
         HashMap<String, String> m = new HashMap<String, String>(5);
         m.put("Class", this.getClass().getName());
         m.put("Method", "delete(Database db):void");
         m.put("Database", db.toString());
         m.put("User Id", String.valueOf(userId));
         m.put("Modified rows count", String.valueOf(result));
         throw new DLException("Method call modified unexpected number of rows.", m);
      }
   }//delete
   
   public List<String> getPapers(int userId) throws DLException {
      List<String> result = new ArrayList<String>();
      try {
         Database db = new Database();
         db.connect();
         ArrayList<String> val = new ArrayList<String>();
         val.add(""+userId);
         List<List<Object>> allpaper = db.getData("SELECT p.paperId, p.title, p.status, p.fileId FROM Papers p JOIN PaperAuthors b using(paperid) where b.userId=?", val);
         for (Object o: allpaper.get(2)) result.add(o.toString());
         db.close();
      } catch(Exception e) {
         //e.printStackTrace();
         new DLException(e);
         // System.out.println("Unable to get paper list. Empty set.");
      }
      return result; 
   }
   
   public void fetchId() throws DLException {
      try {         
         Database db = new Database();
         db.connect();
         List<Object> val = new ArrayList<Object>();
         val.add(email);
         List<List<Object>> ids = db.getData("SELECT userId FROM users WHERE email=?", val);
         //fill in the access attribute
         userId = (int)(ids.get(2).get(0));
         //System.out.println("Name: " + fname + " " + lname);
         db.close();
      } catch(Exception e) {
         //e.printStackTrace();
         new DLException(e);
         System.out.println("Unable to fetch row.");
      }
   }
   
   public void checkAdmin() throws DLException {
      try {
         Database db = new Database();
         db.connect();
         List<Object> val = new ArrayList<Object>();
         val.add(email);
         List<List<Object>> arr = db.getData("SELECT isAdmin FROM users WHERE email=?", val);
         //fill in the access attribute
         isAdmin = (int)(arr.get(2).get(0));
         //System.out.println("Admin status: " + isAdmin);
         db.close();
      } catch(Exception e) {
         //e.printStackTrace();
         new DLException(e);
         System.out.println("Unable to fetch row.");
      }
   }//checkAdmin
  
   public static int findUser(Database db, String email) throws DLException {
      String sql = "SELECT userId FROM `" + TABLE_NAME + "` WHERE email = ? ;";
      Object result = db.getValue(sql, email);
      if (result == null) return 0;
      return (int) result;
   }

   public static int findUser(Database db, String firstName, String lastName) throws DLException {
      String sql = "SELECT userId FROM `" + TABLE_NAME + "` WHERE (lastName = ? AND firstName = ?);";
      List<Object> params = new ArrayList<Object>(2);
      params.add(lastName);
      params.add(firstName);
      Object result = db.getValue(sql, params);
      if (result == null) return 0;
      return (int) result;
   }

   /*
    * login method fetch name, userId, admin status once credentials are verified.
    *
    */
   public static String login(Database db, String email, String password) throws DLException {
      String sql = "SELECT userId, pswd, expiration, isAdmin FROM `" + TABLE_NAME + "` WHERE email = ? ;";
      List<Object> result = db.getRow(sql, email);
      if (result == null) 
         return null;
      //System.out.println("Result: " + result);
      //hash sent in password for comparison
      String hashedPass = hash(password);
      if (!result.get(1).equals(hashedPass)) 
         return null;
      //Password is expired, tell user to update their password
      String expiration = (String) result.get(2);
      if (new Date().after(stringToDate(expiration))) {
         // Once again: There should not be any System.out.print... nowhere except for Main and Test... classes.
         // System.out.println("Sorry, your password is expired. Pleased created a new one.");
         return null;
      }
      int userId = (int) result.get(0);
      if (!expiration.equals("20250101000000")) {
         sql = "UPDATE `" + TABLE_NAME + "` SET expiration = ? WHERE userId = ? ;";
         List<Object> params = new ArrayList<Object>(2);
         params.add("20250101000000");
         params.add(userId);
         if (db.setData(sql, params) != 1) {
            HashMap<String, String> m = new HashMap<String, String>(5);
            m.put("Class", "DLUser");
            m.put("Method", "String login(Database db, String email, String password)");
            m.put("Database", db.toString());
            m.put("User ID", String.valueOf(userId));
            m.put("Modified rows count", String.valueOf(result));
            throw new DLException("Method call modified unexpected number of rows.", m);
         }
      }
      return String.format("%010d%3s", userId, (result.get(3).equals(1)) ? "adm": "usr");
   }//login
   
   public static void resetPassword(Database db, String email) {
      try {
         if (findUser(db, email) == 0) return;
         //Generate a new temp password 6 characters long;
         String tempPass = randomPassword(6);
         //set expiration date to 5min. from now
         // Date date = Calendar.getInstance().getTime();  
         // DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
         // long t = date.getTime();
         // Date addFive = new Date(t + (5 * 60000));
         // String newExp = dateFormat.format(addFive);
         Calendar cal = new GregorianCalendar();
         cal.add(Calendar.MINUTE, 5);
         String newExp = dateToString(cal.getTime());
         //update in database
         String sql = "UPDATE `" + TABLE_NAME + "` SET pswd = ?, expiration = ? WHERE email = ? ;";
         List<Object> params = new ArrayList<Object>(3);
         params.add(hash(tempPass));
         params.add(newExp);
         params.add(email);
         if (db.setData(sql, params) != 1) return;
         sendMessage(email, tempPass);
         // System.out.println("Temporary password sent to: " + email);
      } catch(Exception e) {
         HashMap<String, String> m = new HashMap<String, String>(3);
         m.put("Class", "DLUser");
         m.put("Method", "resetPassword(Database db, String email):void");
         m.put("Email", email);
         new DLException(e, m);
      }
   }

   private static void sendMessage(String email, String password) throws MessagingException {
      Properties p = new Properties();
      p.put("mail.smtp.auth","true");
      p.put("mail.smtp.starttls.enable","true");  
      p.put("mail.smtp.host","smtp.gmail.com");  
      p.put("mail.smtp.port","587"); 
      //this gmail was made for the express purpose of only sending out temporary passwords to users
      String sendAccount = "group4project330@gmail.com";
      String accountPass = "5HyF6wcxQ49Z";
   
      Session session = Session.getInstance(p, 
         new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(sendAccount, accountPass);
            }
         });
      Message message = prepareMessage(session, sendAccount, email, password);
      Transport.send(message);
   }

   private static Message prepareMessage(Session session, String sendAccount, String receiveAccount, String tempPass) {
      try {
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress(sendAccount));
         message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveAccount));
         message.setSubject("Temporary Password Info");
         message.setText("Your temporary password is: " + tempPass + "\n\nThis password will expire in ~5 minutes. Use it to login and change to a permanent password.");
         return message;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   public static String randomPassword(int length) {
      final String possibles = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
      
      SecureRandom ran = new SecureRandom();
      StringBuilder sb = new StringBuilder();
      
      for(int i=0; i<length; i++) {
         //find random indexes in possibles String and at it to the string builder
         int randomIndex = ran.nextInt(possibles.length());
         sb.append(possibles.charAt(randomIndex));
      }
      return sb.toString();
   } 
   
   // public void setPassword(String password) throws DLException {
   //    try {
   //       if(flag==true) {
   //          Database db = new Database();
   //          db.connect();
   //          ArrayList<String> val = new ArrayList<String>();
   //          val.add(password);
   //          val.add(email);
   //          db.setData("UPDATE users SET pswd=? WHERE email=?", val);
   //          System.out.println("1 row updated");
   //          db.close();
   //       }
   //    } catch(Exception e) {
   //       //e.printStackTrace();
   //       DLException dl = new DLException(e);
   //       System.out.println("Cannot update password.");
   //    }
   // }//setPassword
   
   
   //get Affiliation id
   public void getAff_id() throws DLException {
      try {
         Database db = new Database();
         db.connect();
         List<Object> val = new ArrayList<Object>();
         val.add(affiliation);
         List<List<Object>> arr = db.getData("SELECT affiliationId FROM _Affiliations WHERE affiliationName=?", val);
         //fill in the access attribute
         affiliationId = (int)(arr.get(2).get(0));
         //System.out.println("Aff_id = " + affiliationId);
         db.close();
      } catch(Exception e) {
         //e.printStackTrace();
         new DLException(e);
         // System.out.println("Unable to fetch row.");
      }
   }//getAff_id
   
   
   //assign who is admin...
   public void assignAdmin(int isAdmin, String mail) throws DLException {
      try {
         Database db = new Database();
         db.connect();
         ArrayList<String> val = new ArrayList<String>();
         val.add(""+isAdmin);
         val.add(mail);
         db.setData("UPDATE users SET isAdmin=? WHERE email=?", val);
         // System.out.println("1 row updated");
         db.close();
      } catch(Exception e) {
         //e.printStackTrace();
         new DLException(e);
         // System.out.println("Cannot update.");
      }
   }//assignAdmin
   
   // UTILITIES
   public static String hash(String in){
      String result = null;
      try {
         MessageDigest md = MessageDigest.getInstance("SHA-1");
         byte[] digest = md.digest(in.getBytes());
         result = new BigInteger(1, digest).toString(16);
      } catch (NoSuchAlgorithmException e) {}
      return result;
   }    
  
   protected static String dateToString(Date date){
      Calendar calendar = new GregorianCalendar();
      calendar.setTime(date);
      return String.format("%04d%02d%02d%02d%02d%02d", 
             calendar.get(Calendar.YEAR),
             calendar.get(Calendar.MONTH) + 1,
             calendar.get(Calendar.DAY_OF_MONTH),
             calendar.get(Calendar.HOUR_OF_DAY),
             calendar.get(Calendar.MINUTE),
             calendar.get(Calendar.SECOND));
   }
  
   protected static Date stringToDate(String s){
      Calendar calendar = new GregorianCalendar(
         Integer.parseInt(s.substring( 0,  4)), 
         Integer.parseInt(s.substring( 4,  6)) - 1, 
         Integer.parseInt(s.substring( 6,  8)), 
         Integer.parseInt(s.substring( 8, 10)), 
         Integer.parseInt(s.substring(10, 12)), 
         Integer.parseInt(s.substring(12, 14)));
      return calendar.getTime();
   }
  
}//end of DLUser class
