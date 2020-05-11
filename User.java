import java.io.*;
import javax.json.*;

public class User {
  private DLUser dlUser = new DLUser();
  private Database db;

  // CONSTRUCTORS
  public User(Database db) {
    this.db = db;
  }

  public User(Database db, int userId) {
    this.db = db;
    dlUser.setUserId(userId);
    try {
      dlUser.fetch(db);
    } catch (DLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public User(Database db, String firstName, String lastName){
    this.db = db;
    dlUser.setFirstName(firstName);
    dlUser.setLastName(lastName);
  }

  // ACCESSORS
  public int getUserId() {
    return dlUser.getUserId();
  }

  public String getLastName() {
    return dlUser.getLastName();
  }

  public String getFirstName() {
    return dlUser.getFirstName();
  }

  public String getEmail() {
    return dlUser.getEmail();
  }

  public String getPassword() {
    return dlUser.getPassword();
  }

  public String getExpiration() {
    return dlUser.getExpiration();
  }

  public int getIsAdmin() {
    return dlUser.getIsAdmin();
  }

  public int getAffiliationId() {
    return dlUser.getAffiliationId();
  }

  // MUTATORS
  public void setUserId(String token, int userId) {
    if (User.isAdmin(token))
      dlUser.setUserId(userId);
  }

  public void setLastName(String token, String lastName) {
    if (User.isAdmin(token) || User.isSelf(token, dlUser.userId)) {
      dlUser.setLastName(lastName);
    }
  }

  public void setFirstName(String token, String firstName) {
    if (User.isAdmin(token) || User.isSelf(token, dlUser.userId)) {
      dlUser.setFirstName(firstName);
    }
  }

  public void setEmail(String token, String email) {
    if (User.isAdmin(token) || User.isSelf(token, dlUser.userId)) {
      dlUser.setEmail(email);
    }
  }

  public void setPassword(String token, String password) {
    if (User.isAdmin(token)|| User.isSelf(token, dlUser.userId)) {
      dlUser.setPassword(password);
    }
  }

  public void setExpiration(String token, String expiration) {
    if (User.isAdmin(token)) {
      dlUser.setExpiration(expiration);
    }
  }

  public void setAdmin(String token, int isAdmin) {
    if (User.isAdmin(token))
      dlUser.setIsAdmin(isAdmin);
  }

  public void setAffiliationId(String token, int affiliationId) {
    if (User.isAdmin(token) || User.isSelf(token, dlUser.userId)) {
      dlUser.setAffiliationId(affiliationId);
    }
  }

  // SPECIAL
  public void setUser(String lastName, String firstName, String email, String password, String affiliation)
      throws DLException {
    int affiliationId = Affiliation.findAffiliationId(db, affiliation);
    if (affiliationId == 0) {
      Affiliation newAffiliation = new Affiliation(affiliation);
      newAffiliation.post(db);
      affiliationId = newAffiliation.getAffiliationId();
    }
    setUser(lastName, firstName, email, password, affiliation);
  }

  public void setUser(String lastName, String firstName, String email, String password, int affiliationId) {
    try {
      dlUser.setLastName(lastName);
      dlUser.setFirstName(firstName);
      dlUser.setEmail(email);
      dlUser.setPassword(password);
      dlUser.setAffiliationId(affiliationId);
      if (dlUser.getUserId() == 0)
        dlUser.post(db);
      else
        dlUser.put(db);
    } catch (DLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String getUser() {
    try {
      JsonObject jsonUser = Json.createObjectBuilder()
        .add("UserId", dlUser.getUserId())
        .add("Last Name", dlUser.getLastName())
        .add("First Name", dlUser.getFirstName())
        .add("eMail", dlUser.getEmail())
        .add("Expiration", dlUser.getExpiration())
        .add("isAdmin", dlUser.getIsAdmin())
        .add("Affiliation", Affiliation.findAffiliationName(db, dlUser.getAffiliationId())).build();
      StringWriter sw = new StringWriter();
      JsonWriter jsonWriter = Json.createWriter(sw);
      jsonWriter.writeObject(jsonUser);
      jsonWriter.close();
      return sw.toString();
    } catch (DLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
  
  //if no userid, set this up...
  /*
   * setUser.
   * @param
   */
  // public void setUser(String lastName, String firstName, String mail, String password, String affiliation) throws DLException {
  //     if (userId != 0) {
  //        lname = lastName;
  //        fname = firstName;
  //        email = mail;
  //        pwd = hash(password);
  //        this.affiliation = affiliation;
  //        try {
  //           Database db = new Database();
  //           db.connect();
  //           ArrayList<String> val = new ArrayList<>();
  //           val.add(affiliation);
  //           List<List<Object>> aff = db.getData("SELECT affiliationId FROM _Affiliations WHERE affiliationName=?", val);
  //           affiliationId = (int) aff.get(2).get(0);
  //           put(db);
  //           System.out.println("Updated.");
  //           db.close();
  //        } catch(Exception e) {
  //           //e.printStackTrace();
  //           DLException dl = new DLException(e);
  //           System.out.println("Cannot update.");
  //        }   
  //     } else {
  //        //insert a new user - similar to create an account
  //        try{
  //           //set expiration date
  //           Date date = Calendar.getInstance().getTime();  
  //           DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
  //           long t = date.getTime();
  //           Date addFive = new Date(t + (5 * 60000));
  //           String newExp = dateFormat.format(addFive);
  //           Database db = new Database();
  //           db.connect();
  //           List<String> val1 = new ArrayList<String>();
  //           List<List<Object>> max_ids = db.getData("SELECT MAX(userId) FROM users", val1);
  //           int NewId = (int)(max_ids.get(2).get(0)) + 1;
  //           ArrayList<String> vall = new ArrayList<>();
  //           vall.add(affiliation);
  //           List<List<Object>> aff = db.getData("SELECT affiliationId FROM _Affiliations WHERE affiliationName=?", vall);
  //           affiliationId = (int)(aff.get(2).get(0));
  //           ArrayList<String> val = new ArrayList<String>();
  //           val.add(""+NewId);
  //           val.add(lastName);
  //           val.add(firstName);
  //           val.add(mail);
  //           val.add(password);
  //           val.add(newExp);
  //           val.add(""+affiliationId);
  //           db.setData("INSERT INTO users(userId, lastName, firstName, email, pswd, expiration, affiliationId) VALUES (?, ?, ?, ?, ?, ?, ?)", val);
  //           System.out.println("new user account created.");
  //           userId = NewId;
  //           db.close();
  //        } catch(Exception e) {
  //           //e.printStackTrace();
  //           DLException dl = new DLException(e);
  //           System.out.println("0 row inserted. Please check your syntax error.");
  //        }
  //     }
  //  }


  // UTILITIES
  public static int findUser(Database db, String firstName, String lastName) throws DLException {
    return DLUser.findUser(db, firstName, lastName);
  }

  public static boolean isAdmin(String token) {
    if (token == null) return false;
    if (token.length() != 13) return false;
    // TODO Maybe throw an exception 
    return token.substring(10, 13).equals("adm"); 
  }

  public static boolean isSelf(String token, int userId) {
    if (token == null) return false;
    if (token.length() != 13) return false;
    int id;
    try { id = Integer.parseInt(token.substring(0, 10)); }
    catch (NumberFormatException nfe) { return false; }
    // TODO Maybe throw an exception 
    return id == userId; 
  }

  public static int idFromToken(String token) {
    if (token == null) return 0;
    if (token.length() != 13) return 0;
    int id;
    try { id = Integer.parseInt(token.substring(0, 10)); }
    catch (NumberFormatException nfe) { return 0; }
    // TODO Maybe throw an exception 
    return id; 
  }

  public static void resetPassword(Database db, String email){
    DLUser.resetPassword(db, email);
  }

  public static String login(Database db, String email, String password) throws DLException {
    return DLUser.login(db, email, password);
  }
}