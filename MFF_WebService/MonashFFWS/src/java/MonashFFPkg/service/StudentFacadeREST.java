/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MonashFFPkg.service;

import MonashFFPkg.FavUnitFrequency;
import MonashFFPkg.Student;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author YuanZhan
 */
@Stateless
@Path("monashffpkg.student")
public class StudentFacadeREST extends AbstractFacade<Student> {

    @PersistenceContext(unitName = "MonashFFWSPU")
    private EntityManager em;

    public StudentFacadeREST() {
        super(Student.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Student entity) {
        // set the default value here
        if (entity.getCurrentJob() == null) {
            entity.setCurrentJob("unemployed");
        }
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Student entity) {
        // set the default value here
        if (entity.getCurrentJob() == null) {
            entity.setCurrentJob("unemployed");
        }
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Student find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    // query the tables based on each attribute that the table/entity has
    // find by first name
    @GET
    @Path("findByFname/{fname}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByFname(@PathParam("fname") String fname) {
        Query query = em.createNamedQuery("Student.findByFname");
        query.setParameter("fname", fname);
        return query.getResultList();
    }

    // find by last name
    @GET
    @Path("findByLname/{lname}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByLname(@PathParam("lname") String lname) {
        Query query = em.createNamedQuery("Student.findByLname");
        query.setParameter("lname", lname);
        return query.getResultList();
    }

    // find by dob
    @GET
    @Path("findByDob/{dob}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByDob(@PathParam("dob") String dob) {
        try {
            Query query = em.createNamedQuery("Student.findByDob");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dob);
            query.setParameter("dob", date);
            return query.getResultList();
        } catch (ParseException ex) {
            Logger.getLogger(StudentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    // find by gender
    @GET
    @Path("findByGender/{gender}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByGender(@PathParam("gender") String gender) {
        Query query = em.createNamedQuery("Student.findByGender");
        Boolean genderFlag = false;
        if (gender.toLowerCase().equals("male")) {
            genderFlag = true;
        }
        query.setParameter("gender", genderFlag);
        return query.getResultList();
    }

    // find by course
    @GET
    @Path("findByCourse/{course}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByCourse(@PathParam("course") String course) {
        Query query = em.createNamedQuery("Student.findByCourse");
        query.setParameter("course", course);
        return query.getResultList();
    }

    // find by mode
    @GET
    @Path("findByMode/{mode}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByMode(@PathParam("mode") String mode) {
        Query query = em.createNamedQuery("Student.findByMode");
        Boolean modeFlag = false;
        if (mode.toLowerCase().equals("parttime")) {
            modeFlag = true;
        }
        query.setParameter("mode", modeFlag);
        return query.getResultList();
    }

    // find by address
    @GET
    @Path("findByAddress/{address}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByAddress(@PathParam("address") String address) {
        Query query = em.createNamedQuery("Student.findByAddress");
        query.setParameter("address", address);
        return query.getResultList();
    }

    // find by suburb
    @GET
    @Path("findBySuburb/{suburb}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findBySuburb(@PathParam("suburb") String suburb) {
        Query query = em.createNamedQuery("Student.findBySuburb");
        query.setParameter("suburb", suburb);
        return query.getResultList();
    }

    // find by nationality
    @GET
    @Path("findByNationality/{nationality}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByNationality(@PathParam("nationality") String nationality) {
        Query query = em.createNamedQuery("Student.findByNationality");
        query.setParameter("nationality", nationality);
        return query.getResultList();
    }

    // find by lang
    @GET
    @Path("findByLang/{lang}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByLang(@PathParam("lang") String lang) {
        Query query = em.createNamedQuery("Student.findByLang");
        query.setParameter("lang", lang);
        return query.getResultList();
    }

    // find by favouriteSport
    @GET
    @Path("findByFavouriteSport/{favouriteSport}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByFavouriteSport(@PathParam("favouriteSport") String favouriteSport) {
        Query query = em.createNamedQuery("Student.findByFavouriteSport");
        query.setParameter("favouriteSport", favouriteSport);
        return query.getResultList();
    }

    // find by favouriteMovie
    @GET
    @Path("findByFavouriteMovie/{favouriteMovie}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByFavouriteMovie(@PathParam("favouriteMovie") String favouriteMovie) {
        Query query = em.createNamedQuery("Student.findByFavouriteMovie");
        query.setParameter("favouriteMovie", favouriteMovie);
        return query.getResultList();
    }

    // find by favouriteUnit
    @GET
    @Path("findByFavouriteUnit/{favouriteUnit}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByFavouriteUnit(@PathParam("favouriteUnit") String favouriteUnit) {
        Query query = em.createNamedQuery("Student.findByFavouriteUnit");
        query.setParameter("favouriteUnit", favouriteUnit);
        return query.getResultList();
    }

    // find by currentJob
    @GET
    @Path("findByCurrentJob/{currentJob}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByCurrentJob(@PathParam("currentJob") String currentJob) {
        Query query = em.createNamedQuery("Student.findByCurrentJob");
        query.setParameter("currentJob", currentJob);
        return query.getResultList();
    }

    // find by emailAddr
    @GET
    @Path("findByEmailAddr/{emailAddr}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByEmailAddr(@PathParam("emailAddr") String emailAddr) {
        Query query = em.createNamedQuery("Student.findByEmailAddr");
        query.setParameter("emailAddr", emailAddr);
        return query.getResultList();
    }

    // find by pwd
    @GET
    @Path("findByPwd/{pwd}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByPwd(@PathParam("pwd") String pwd) {
        Query query = em.createNamedQuery("Student.findByPwd");
        query.setParameter("pwd", pwd);
        return query.getResultList();
    }

    // find by subscriptDatetime
    @GET
    @Path("findBySubscriptDatetime/{subscriptDatetime}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findBySubscriptDatetime(@PathParam("subscriptDatetime") String subscriptDatetime) {
        try {
            Query query = em.createNamedQuery("Student.findBySubscriptDatetime");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(subscriptDatetime);
            query.setParameter("subscriptDatetime", date);
            return query.getResultList();
        } catch (ParseException ex) {
            Logger.getLogger(StudentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    // a single method that accept one condition, and then base on this condition to find out the resluts.
    @GET
    @Path("findByCondition/{searchMode}/{searchContent}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByCondition(@PathParam("searchMode") String searchMode, @PathParam("searchContent") String searchContent) {
        String queryCriteria = "";
        switch (searchMode) {
            case "stdId":
                queryCriteria = "findByStdId";
                break;
            case "fname":
                queryCriteria = "findByFname";
                break;
            case "lname":
                queryCriteria = "findByLname";
                break;
            case "dob":
                queryCriteria = "findByDob";
                break;
            case "gender":
                queryCriteria = "findByGender";
                break;
            case "course":
                queryCriteria = "findByCourse";
                break;
            case "mode":
                queryCriteria = "findByMode";
                break;
            case "address":
                queryCriteria = "findByAddress";
                break;
            case "suburb":
                queryCriteria = "findBySuburb";
                break;
            case "nationality":
                queryCriteria = "findByNationality";
                break;
            case "lang":
                queryCriteria = "findByLang";
                break;
            case "favouriteSport":
                queryCriteria = "findByFavouriteSport";
                break;
            case "favouriteMovie":
                queryCriteria = "findByFavouriteMovie";
                break;
            case "favouriteUnit":
                queryCriteria = "findByFavouriteUnit";
                break;
            case "currentJob":
                queryCriteria = "findByCurrentJob";
                break;
            case "emailAddr":
                queryCriteria = "findByEmailAddr";
                break;
            case "pwd":
                queryCriteria = "findByPwd";
                break;
            case "subscriptDatetime":
                queryCriteria = "findBySubscriptDatetime";
                break;
            default:
                System.out.println("wrong conditions");

        }
        Query query = em.createNamedQuery("Student." + queryCriteria);
        if (!queryCriteria.equals("findBySubscriptDatetime") && !queryCriteria.equals("findByDob")) {
            query.setParameter(searchMode, searchContent);
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(searchContent);
                query.setParameter("subscriptDatetime", date);
            } catch (ParseException ex) {
                System.out.println("Parse error in converting string to date");
            }
        }
        return query.getResultList();
    }

    // dynamic query
    // find by fname and lname
    @GET
    @Path("findByFullName/{fname}/{lname}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByFullName(@PathParam("fname") String fname, 
            @PathParam("lname") String lname) {
        TypedQuery<Student> q = 
                em.createQuery("SELECT s FROM Student s "
                        + "WHERE s.fname = :fname AND s.lname = :lname", Student.class);
        q.setParameter("fname", fname);
        q.setParameter("lname", lname);
        return q.getResultList();
    }

    // dynamic query
    //findBy two modes
    @GET
    @Path("findByAnyTwoAttributes/{searchMode1}/{searchContent1}/{searchMode2}/{searchContent2}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findByAnyTwoAttributes(@PathParam("searchMode1") String searchMode1, @PathParam("searchContent1") String searchContent1,
            @PathParam("searchMode2") String searchMode2, @PathParam("searchContent2") String searchContent2) {
        TypedQuery<Student> q = em.createQuery("SELECT s FROM Student s WHERE s." + searchMode1 + " = :" + searchContent1 + " AND " + "s." + searchMode2 + " = :" + searchContent2, Student.class);

        // check whether mode is in date format, if false, set the parameter directly, otherwise, convert it to date type for finding.
        if (!searchMode1.equals("subscriptDatetime")
                && !searchMode1.equals("dob")
                && !searchMode2.equals("subscriptDatetime")
                && !searchMode1.equals("dob")) {
            q.setParameter(searchContent1, searchContent1);
            q.setParameter(searchContent2, searchContent2);
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (searchMode1.equals("subscriptDatetime") || searchMode1.equals("dob")) {
                    Date date1 = sdf.parse(searchContent1);
                    q.setParameter(searchContent1, date1);
                } else {
                    q.setParameter(searchContent1, searchContent1);
                }

                if (searchMode2.equals("subscriptDatetime") || searchMode2.equals("dob")) {
                    Date date2 = sdf.parse(searchContent1);
                    q.setParameter(searchContent1, date2);
                } else {
                    q.setParameter(searchContent2, searchContent2);
                }

            } catch (ParseException ex) {
                System.out.println("Parse Error in converting two attrs from string to date");
            }

        }

        return q.getResultList();
    }

    // Q4 static query, explicit join
    // find the student whose course is MIT and has record in friendship table
    @GET
    @Path("findFromStudentJoinFridship")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findFromStudentJoinFridship() {
        TypedQuery<Student> q
                = em.createQuery("SELECT s FROM Student s JOIN Friendship f "
                        + "ON s.stdId = f.friendshipPK.stdId JOIN Student s2 "
                        + "ON s2.stdId = f.friendshipPK.friendId WHERE s.course = 'MIT'", Student.class);
        return q.getResultList();
    }

    // ******* ******* ******* ******* ******* ******* 
    // task 3 c : looks like it is right should be test
    // ******* ******* ******* ******* ******* ******* 
    
    @GET
    @Path("findFutureFriends/{stdId}/{keyword1}/{keyword2}/{keyword3}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findFutureFriends(@PathParam("stdId") int stdId,
            @PathParam("keyword1") String keyword1,
            @PathParam("keyword2") String keyword2,
            @PathParam("keyword3") String keyword3) {
        List<Student> allStudents = this.findAll();
        List<Student> futureFriends = new ArrayList<>();
        // find the certain student
        Student certainStudent = this.find((long) stdId);

        // find this student's values for three attributes
        String criteriaContent1 = this.getCriteriaContent(certainStudent, keyword1);
        String criteriaContent2 = this.getCriteriaContent(certainStudent, keyword2);
        String criteriaContent3 = this.getCriteriaContent(certainStudent, keyword3);
        // find all the students meet the requirements from all students list
        // add him/her into futurefriends list
        for (Student student : allStudents) {
            if (this.getCriteriaContent(student, keyword1).equals(criteriaContent1)
                    && this.getCriteriaContent(student, keyword2).equals(criteriaContent2)
                    && this.getCriteriaContent(student, keyword3).equals(criteriaContent3)
                    && student.getStdId() != (long) stdId) {
                futureFriends.add(student);
            }
        }

        return futureFriends;
    }
    
    // ******* ******* ******* ******* ******* ******* 
    // task 3 c : looks like it is right should be test
    // ******* ******* ******* ******* ******* ******* 

    public String getCriteriaContent(Student certainStudent, String keyword) {
        String criteria = "";
        switch (keyword) {
            case "suburb":
                criteria = certainStudent.getSuburb();
                break;
            case "nationality":
                criteria = certainStudent.getNationality();
                break;
            case "language":
                criteria = certainStudent.getLang();
                break;
            case "sport":
                criteria = certainStudent.getFavouriteSport();
                break;
            case "movie":
                criteria = certainStudent.getFavouriteMovie();
                break;
            case "unit":
                criteria = certainStudent.getFavouriteUnit();
                break;
            case "job":
                criteria = certainStudent.getCurrentJob();
                break;
            default:
                System.out.println("Keyword has syntax error.");
        }
        return criteria;
    }

    // ******* ******* ******* ******* ******* ******* 
    // task 3 d : looks like it is right should be test
    // ******* ******* ******* ******* ******* ******* 
//    @GET
//    @Path("findFutureFriendsEnhanced/{stdId}/{keywords}")
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public List<Student> findFutureFriendsEnhanced(@PathParam("stdId") int stdId,
//            @PathParam("keywords") String keywords) {
//        // the parameter "keywords" here is a string looks like "kw1,kw2,kw3" that seperate by ","
//        List<Student> allStudents = this.findAll();
//        Student certainStudent = this.find((long) stdId);
//        List<Student> futureFriends = new ArrayList<>();
//
//        // need to reconsider
//        String[] criterias = keywords.split(",");
//        for (Student student : allStudents) {
//            boolean satisfied = true;
//            for (String criteria : criterias) {
//                if (!this.getCriteriaContent(certainStudent, criteria)
//                        .equals(this.getCriteriaContent(student, criteria))) {
//                    satisfied = false;
//                }
//            }
//            if (satisfied && student.getStdId() != (long) stdId) {
//                futureFriends.add(student);
//            }
//        }
//
//        return futureFriends;
//    }
    // ******* ******* ******* ******* ******* ******* 
    // task 3 d : looks like it is right should be test
    // ******* ******* ******* ******* ******* ******* 

    @GET
    @Path("findFavUnitAndFre")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<FavUnitFrequency> findFavUnitAndFre() {
        List<FavUnitFrequency> items = new ArrayList<>();
        List<Student> allStudents = this.findAll();
        List<String> favUnits = new ArrayList<>();
        for (Student student : allStudents) {
            favUnits.add(student.getFavouriteUnit());
        }
        // remove duplicated records
        HashSet h = new HashSet(favUnits);
        favUnits.clear();
        favUnits.addAll(h);

        for (String favUnit : favUnits) {
            int num = 0;
            for (Student student : allStudents) {
                if (student.getFavouriteUnit().equals(favUnit)) {
                    num++;
                }
            }
            FavUnitFrequency fuf = new FavUnitFrequency(favUnit, num);
            items.add(fuf);
        }
        return items;
    }

    // ******* ******* ******* ******* ******* ******* 
    // task 3 d : looks like it is right should be test
    // ******* ******* ******* ******* ******* ******* 
    
    @GET
    @Path("findFutureFriendsEnhancedByUsingQueryParam/{stdId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> findFutureFriendsEnhancedByUsingQueryParam(
            @PathParam("stdId") int stdId,
            @QueryParam("keyword1") String keyword1,
            @QueryParam("keyword2") String keyword2,
            @QueryParam("keyword3") String keyword3,
            @QueryParam("keyword4") String keyword4,
            @QueryParam("keyword5") String keyword5,
            @QueryParam("keyword6") String keyword6,
            @QueryParam("keyword7") String keyword7
    ) {
        // get all students
        List<Student> allStudents = this.findAll();

        // find the certain student by its stdId, he/she is the one who wanna find friends
        Student certainStudent = this.find((long) stdId);

        // the list of future friends
        List<Student> futureFriends = new ArrayList<>();

        String[] criterias = {keyword1, keyword2, keyword3, keyword4, keyword5, keyword6, keyword7};
        if (criterias.length != 0) {
            for (Student student : allStudents) {
                boolean satisfied = true;
                for (String criteria : criterias) {
                    if (!this.getCriteriaContent(certainStudent, criteria)
                            .equals(this.getCriteriaContent(student, criteria))) {
                        satisfied = false;
                    }
                }
                if (satisfied && student.getStdId() != (long) stdId) {
                    futureFriends.add(student);
                }
            }
        }
        return futureFriends;
    }
    
    
    // ******* ******* ******* ******* ******* ******* 
    // task 3 d : looks like it is right should be test
    // ******* ******* ******* ******* ******* ******* 

}
