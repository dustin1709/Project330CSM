import java.util.*;

public class DLPaper {
  private final static String TABLE_NAME = "papers";

  private int      paperId;
  private String   title;
  private String   submissionAbstract;
  private String   track;
  private String   status;
  private int      submissionType;
  private int      submitterId;
  private String   fileId;
  private String   tentativeStatus;

  // CONSTRUCTORS
  public DLPaper() {}
  public DLPaper(int paperId) { this.paperId = paperId; }

  // ACCESSORS
  public int    getPaperId()         { return paperId; }
  public String getTitle()           { return title; }
  public String getAbstract()        { return submissionAbstract; }
  public String getTrack()           { return track; }
  public String getStatus()          { return status; }
  public int    getSubmissionType()  { return submissionType; }
  public int    getSubmitterId()     { return submitterId; }
  public String getFileId()          { return fileId; }
  public String getTentativeStatus() { return tentativeStatus; }

  // MUTATORS
  public void setPaperId(int paperId) { this.paperId = paperId; }
  public void setTitle(String title)  { this.title = title; }
  public void setAbstract(String submissionAbstract) { this.submissionAbstract = submissionAbstract; }
  public void setTrack(String track) { this.track = track; }	
  public void setStatus(String status) { this.status = status; }	
  public void setSubmissionType(int submissionType) { this.submissionType = submissionType; }	
  public void setSubmitterId(int submitterId) { this.submitterId = submitterId; }	
  public void setFileId(String fileId) { this.fileId = fileId; }	
  public void setTentativeStatus(String tentativeStatus) {
     this.tentativeStatus = tentativeStatus;
  }

  // CRUD
  //post - enter a new paper
  public void post(Database db) throws DLException {
    if ((paperId = findPaper(db, title)) != 0){ put(db); return; }
    String sql = "INSERT INTO `" + TABLE_NAME + "` (paperId, title, abstract, track, status, submissionType, submitterId, fileId, tentativeStatus) SELECT MAX(paperId) + 1, ?, ?, ?, ?, ?, ?, ?, ? FROM `" + TABLE_NAME + "`;";
    List<Object> params = new ArrayList<Object>(8);
    params.add(title);
    params.add(submissionAbstract);
    params.add(track);
    params.add(status);
    params.add(submissionType);
    params.add(submitterId);
    params.add(fileId);
    params.add(tentativeStatus);
    int result = db.setData(sql, params);
    if (result != 1) {
        HashMap<String, String> m = new HashMap<String, String>(5);
        m.put("Class", this.getClass().getName());
        m.put("Method", "post(Database db):void");
        m.put("Database", db.toString());
        m.put("Paper: ", toString());
        m.put("Modified rows count", String.valueOf(result));
        throw new DLException("Method call modified unexpected number of rows.", m);
    }
    sql = "SELECT paperId FROM `" + TABLE_NAME + "` WHERE title = ? LIMIT 1;";
    setPaperId((int)(db.getValue(sql, title)));
  } //post

  public void fetch(Database db) throws DLException {
    String sql = "SELECT title, abstract, track, status, submissionType, submitterId, fileId, tentativeStatus FROM `" + TABLE_NAME + "` WHERE paperId = ? ;";
    List<Object> result = db.getRow(sql, paperId);
    title              = (String) result.get(0);
    submissionAbstract = (String) result.get(1);
    track              = (String) result.get(2);
    status             = (String) result.get(3);
    submissionType     = (int)    result.get(4);
    submitterId        = (int)    result.get(5);
    fileId             = (String) result.get(6);
    tentativeStatus    = (String) result.get(7);
  }//fetch

  public void put(Database db) throws DLException {
    String sql = "UPDATE `" + TABLE_NAME + "` SET title = ?, abstract = ?, track = ?, status = ?, submissionType = ? , submitterId = ?, fileId = ?, tentativeStatus = ? WHERE paperId = ? ;";
    List<Object> params = new ArrayList<Object>(9);
    params.add(title);
    params.add(submissionAbstract);
    params.add(track);
    params.add(status);
    params.add(submissionType);
    params.add(submitterId);
    params.add(fileId);
    params.add(tentativeStatus);
    params.add(paperId);
    int result = db.setData(sql, params);
    if (result != 1) {
        HashMap<String, String> m = new HashMap<String, String>(5);
        m.put("Class", this.getClass().getName());
        m.put("Method", "put(Database db):void");
        m.put("Database", db.toString());
        m.put("Paper ID", String.valueOf(paperId));
        m.put("Modified rows count", String.valueOf(result));
        throw new DLException("Method call modified unexpected number of rows.", m);
    }
  }//update

  public void delete(Database db) throws DLException {
    String sql = "DELETE FROM `" + TABLE_NAME + "` WHERE paperId = ? ;";
    int result = db.setData(sql, paperId);
    if (result != 1) {
        HashMap<String, String> m = new HashMap<String, String>(5);
        m.put("Class", this.getClass().getName());
        m.put("Method", "delete(Database db):void");
        m.put("Database", db.toString());
        m.put("Paper Id", String.valueOf(paperId));
        m.put("Modified rows count", String.valueOf(result));
        throw new DLException("Method call modified unexpected number of rows.", m);
    }
  }//delete

  // SPECIAL

  //UTILITIES
  public static int findPaper(Database db, String title) throws DLException {
    String sql = "SELECT paperId FROM `" + TABLE_NAME + "` WHERE title = ? ;";
    Object result = db.getValue(sql, title);
    if (result == null) return 0;
    return (int) result;
  }
}