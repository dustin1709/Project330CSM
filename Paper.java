import java.io.*;
import javax.json.*;

public class Paper {
   private DLPaper dlPaper;
   private Database db;

   // CONSTRUCTORS
   public Paper(Database db) { this.db = db; }
   public Paper(Database db, int paperId) {
      this.db = db;
      dlPaper = new DLPaper(paperId);
      try {
         dlPaper.fetch(db);
      } catch (DLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   // ACCESSORS
   /*
    * Get paperId.
    * @return paperId
    */
   public int getPaperId() { return dlPaper.getPaperId(); }

   /*
    * Get paper title.
    * @return title
    */
   public String getTitle() { return dlPaper.getTitle(); }

   /*
    * Get Abstract.
    * @return submissionAbstract
    */
   public String getAbstract() { return dlPaper.getAbstract(); }

   /*
    * Get track.
    * @return track
    */
   public String getTrack() { return dlPaper.getTrack(); }

   public String getStatus() { return dlPaper.getStatus(); }
   public int getSubmissionType() { return dlPaper.getSubmissionType(); }
   public int getSubmitterId() { return dlPaper.getSubmitterId(); }
   public String getFileId() { return dlPaper.getFileId(); }
   public String getTentativeStatus() { return dlPaper.getTentativeStatus(); }

   // MUTATORS
   /*
    * Set paperId.
    * @param paperId
    */
   public void setPaperId(int paperId) { dlPaper.setPaperId(paperId); }
 
    /*
     * Set title.
     * @param title
     */
   public void setTitle(String title) { dlPaper.setTitle(title); }
    
    /*
     * Set SubmissionAbstract.
     * @param submissionAbstract
     */
   public void setAbstract(String submissionAbstract) { dlPaper.setAbstract(submissionAbstract); }

   public void setTrack(String track) { dlPaper.setTrack(track); }
   public void setStatus(String status) { dlPaper.setStatus(status); }
   public void setSubmissionType(int submissionType) { dlPaper.setSubmissionType(submissionType); }
   public void setSubmitterId(int submitterId) { dlPaper.setSubmitterId(submitterId); }
   public void setFileId(String fileId) { dlPaper.setFileId(fileId); }
   public void setTentativeStatus(String tentativeStatus) { dlPaper.setTentativeStatus(tentativeStatus); }

   // SPECIAL
   public String getPaper(int paperId) {
      try {
         User user = new User(db, dlPaper.getSubmitterId());
         String userName = user.getFirstName() + " " + user.getLastName();
         JsonObject jsonUser = Json.createObjectBuilder()
           .add("Paper ID", dlPaper.getPaperId())
           .add("Title", dlPaper.getTitle())
           .add("Abstract", dlPaper.getAbstract())
           .add("Track", dlPaper.getTrack())
           .add("Status", dlPaper.getStatus())
           .add("Submission Type", dlPaper.getSubmissionType())
           .add("Submitter", userName)
           .add("Tentative Status", dlPaper.getTentativeStatus())
           .build();
         StringWriter sw = new StringWriter();
         JsonWriter jsonWriter = Json.createWriter(sw);
         jsonWriter.writeObject(jsonUser);
         jsonWriter.close();
         return sw.toString();
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return null;
   }

   public void setPaper(int paperId, String submissionTitle, String submissionAbstract, int submissionType, String fileName, String[] subjects, String[] coauthorsFirstNames, String[] coauthorsLastNames)
      throws DLException {
         setPaper(paperId, submissionTitle, submissionAbstract, submissionType, fileName);
         paperId = dlPaper.getPaperId();
         int subjectId;
         for (int i = 0; i < subjects.length; i++){
            subjectId = Subject.findSubjectId(db, subjects[i]);
            if (subjectId == 0) {
               Subject subject = new Subject(subjects[i]);
               subject.post(db);
               subjectId = subject.getSubjectId();
            }
            PaperSubject.setPaperSubject(db, paperId, subjectId);
         }
         int authorId;
         for (int i = 0; i < coauthorsFirstNames.length; i++){
            authorId = User.findUser(db, coauthorsFirstNames[i], coauthorsLastNames[i]);
            if (authorId == 0) {
               User user = new User(db, coauthorsFirstNames[i], coauthorsLastNames[i]);
               authorId = user.getUserId();
            }
            PaperAuthor.setPaperAuthor(db, paperId, authorId, i);
         }
   }

   public void setPaper(int paperId, String submissionTitle, String submissionAbstract, int submissionType, String fileName){
      try {
         DLPaper dlPaper = new DLPaper();
         dlPaper.setPaperId(paperId);
         dlPaper.setTitle(submissionTitle);
         dlPaper.setAbstract(submissionAbstract);
         dlPaper.setSubmissionType(submissionType);
         dlPaper.setFileId(fileName);
         if (dlPaper.getPaperId() == 0) dlPaper.post(db);
         else dlPaper.put(db);
      } catch (DLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
       }
   }
   /*
    * Creates a new entry info paperId is not available. Otherwise, update the paper properties.
    *
    * @param paperId
    * @param submissionTitle
    * @param submissionAbstract
    * @param submissionType
    * @param filename
    * @param subjectsId[]
    * @param coauthorsId[]
    */
}
