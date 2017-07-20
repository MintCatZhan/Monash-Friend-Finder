/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MonashFFPkg.service;

import MonashFFPkg.Location;
import MonashFFPkg.LocationDistance;
import MonashFFPkg.LocationFrequency;
import MonashFFPkg.LocationLatestForEach;
import MonashFFPkg.LocationPK;
import java.sql.Timestamp;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

/**
 *
 * @author YuanZhan
 */
@Stateless
@Path("monashffpkg.location")
public class LocationFacadeREST extends AbstractFacade<Location> {

    @PersistenceContext(unitName = "MonashFFWSPU")
    private EntityManager em;

    private LocationPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;dateTime=dateTimeValue;stdId=stdIdValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        MonashFFPkg.LocationPK key = new MonashFFPkg.LocationPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> dateTime = map.get("dateTime");
        if (dateTime != null && !dateTime.isEmpty()) {
            key.setDateTime(new java.util.Date(dateTime.get(0)));
        }
        java.util.List<String> stdId = map.get("stdId");
        if (stdId != null && !stdId.isEmpty()) {
            key.setStdId(new java.lang.Long(stdId.get(0)));
        }
        return key;
    }

    public LocationFacadeREST() {
        super(Location.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Location entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, Location entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        MonashFFPkg.LocationPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Location find(@PathParam("id") PathSegment id) {
        MonashFFPkg.LocationPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Location> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Location> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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

    // advanced finding methods
    // find by latitude
    @GET
    @Path("findByLatitude/{latitude}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Location> findByLatitude(@PathParam("latitude") double latitude) {
        Query query = em.createNamedQuery("Location.findByLatitude");
        query.setParameter("latitude", latitude);
        return query.getResultList();
    }

    // find by longitude
    @GET
    @Path("findByLongitude/{longitude}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Location> findByLongitude(@PathParam("longitude") double longitude) {
        Query query = em.createNamedQuery("Location.findByLongitude");
        query.setParameter("longitude", longitude);
        return query.getResultList();
    }

    // find by dateTime
    @GET
    @Path("findByDateTime/{dateTime}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Location> findByDateTime(@PathParam("dateTime") String dateTime) {
        try {
            Query query = em.createNamedQuery("Location.findByDateTime");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date tmpDate = sdf.parse(dateTime);
            query.setParameter("dateTime", tmpDate);
            return query.getResultList();
        } catch (ParseException ex) {
            Logger.getLogger(LocationFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    // find by locName
    @GET
    @Path("findByLocName/{locName}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Location> findByLocName(@PathParam("locName") String locName) {
        Query query = em.createNamedQuery("Location.findByLocName");
        query.setParameter("locName", locName);
        return query.getResultList();
    }

    // find by stdId
    @GET
    @Path("findByStdId/{stdId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Location> findByStdId(@PathParam("stdId") int stdId) {
        Query query = em.createNamedQuery("Location.findByStdId");
        query.setParameter("stdId", stdId);
        return query.getResultList();
    }

    // 3) implicit join, query by loc name & mode
    @GET
    @Path("findByCourseAndLocName/{course}/{locName}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Location> findByCourseAndLocName(@PathParam("course") String course, 
            @PathParam("locName") String locName) {
        TypedQuery<Location> q
                = em.createQuery("SELECT l FROM Location l "
                        + "WHERE l.student.course = :course AND l.locName = :locName", Location.class);
        q.setParameter("course", course);
        q.setParameter("locName", locName);
        return q.getResultList();
    }

    // return a list of the place names and their frequency
    @GET
    @Path("getLocFrequency/{stdId}/{startDate}/{endDate}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<LocationFrequency> getLocFrequency(@PathParam("stdId") long stdId, 
            @PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {
        List<LocationFrequency> items = new ArrayList<>();
        TypedQuery<Location> q = em.createQuery("SELECT l FROM Location l "
                + "WHERE l.locationPK.stdId = :stdId "
                + "AND l.locationPK.dateTime >= :sDate "
                + "AND l.locationPK.dateTime <= :eDate"
                , Location.class);
        try {
            // get all locations within the date range
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date sDate = sdf.parse(startDate);
            Date eDate = sdf.parse(endDate);
            q.setParameter("stdId", stdId);
            q.setParameter("sDate", sDate);
            q.setParameter("eDate", eDate);
            List<Location> places = q.getResultList();

            // get all relation place name without duplication
            List<String> locNames = new ArrayList<>();
            // add all locNames into a new Arraylist
            for (Location place : places) {
                locNames.add(place.getLocName());
            }
            // use hashset to remove duplicated locnames for the previous arraylist
            HashSet h = new HashSet(locNames);
            locNames.clear();
            locNames.addAll(h);
            
            // search all 
            for (String locName : locNames) {
                int num = 0;
                for (Location place : places) {
                    if (place.getLocName().equals(locName)) {
                        num++;
                    }
                }
                LocationFrequency locFreq = new LocationFrequency(locName, num);
                items.add(locFreq);
            }
        } catch (ParseException ex) {
            System.out.println("Error in get Frequence due to date converting " + ex);
        }
        return items;
    }
//
//    @GET
//    @Path("findNearestStd/{stdId}/{currLatitude}/{currLongitude}")
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public List<LocationDistance> findNearestStd(@PathParam("stdId") int stdId,
//            @PathParam("currLatitude") double currLatitude,
//            @PathParam("currLongitude") double currLongitude) throws ParseException {
    
         //get all locations whose datetime are in range of 5 min ago and now
//        TypedQuery<Location> q = 
//                em.createQuery("SELECT l FROM Location l WHERE l.locationPK.dateTime >= :currentTime", Location.class);
//         //get current time and convert it into format that can be compared as condition
//         //1 min as criteria
//        Date fiveMinAgo = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
//        q.setParameter("currentTime", fiveMinAgo);

////        TypedQuery<Location> q = em.createQuery("SELECT l FROM Location l WHERE l.locationPK.stdId > :stdId OR l.locationPK.stdId < :stdId", Location.class);
//        TypedQuery<LocationLatestForEach> lfe
//                = em.createQuery("SELECT NEW MonashFFPkg.LocationLatestForEach(l.locationPK.stdId, MAX(l.locationPK.dateTime)) FROM Location l GROUP BY l.locationPK.stdId", LocationLatestForEach.class);
//        List<LocationLatestForEach> locAndLatestTimeS = lfe.getResultList();
//        System.out.print(locAndLatestTimeS.size());
//
//        // get the location list that exclude the given student or the duplicated student
//        List<Location> allLocations = new ArrayList<>();
//
//        // remove the record for the certain student by stdId
//        for (int i = 0; i < locAndLatestTimeS.size(); i++) {
//            LocationLatestForEach currTemp = locAndLatestTimeS.get(i);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String tmpDate = sdf.format(currTemp.getDateTime());
//            Timestamp ts = Timestamp.valueOf(tmpDate);
//            System.out.println(tmpDate);
//            if (currTemp.getStdId() != stdId) {
//                TypedQuery<Location> q
//                        = em.createQuery("SELECT l FROM Location l WHERE l.locationPK.stdId = :id AND l.locationPK.dateTime = :dateTime", Location.class);
//                q.setParameter("id", currTemp.getStdId());
//                q.setParameter("dateTime", ts);
//                allLocations.add(q.getSingleResult());
//            }
//
//        }
//
////        // remove the student him/herself and add appriate locations into the list
////        List<Location> allLocations = new ArrayList<>();
////        for (Location loc : allLocs) {
////            if (loc.getStudent().getStdId() != stdId) {
////                allLocations.add(loc);
////            }
////        }
//        // get all those locations distance with given lati&longi
//        List<Double> distances = new ArrayList<>();
//        for (Location location : allLocations) {
//            double dist = this.computeDistance(location.getLatitude().doubleValue(),
//                    location.getLongitude().doubleValue(),
//                    currLatitude, currLongitude);
//            distances.add(dist);
//        }
//
//        // sort the allLocations list base on the distance
//        // Bubble sort
//        double tempDis;
//        Location tempLoc;
//        for (int i = 0; i < distances.size(); i++) {
//            for (int j = 1; j < distances.size() - i; j++) {
//                if (distances.get(j - 1) > distances.get(j)) {
//                    tempDis = distances.get(j - 1);
//                    tempLoc = allLocations.get(j - 1);
//
//                    distances.set(j - 1, distances.get(j));
//                    allLocations.set(j - 1, allLocations.get(j));
//
//                    distances.set(j, tempDis);
//                    allLocations.set(j, tempLoc);
//                }
//            }
//        } //  so the sorted allLocations list has been genenrated
//
//        List<LocationDistance> result = new ArrayList<>();
//        // in this case, only return 10 record sorted by distance
//        // get the limitation in case of there are less than 10 record
//        int minNum = Math.min(allLocations.size(), 9);
//        for (int i = 0; i < minNum; i++) {
//            String fname = allLocations.get(i).getStudent().getFname();
//            String lname = allLocations.get(i).getStudent().getLname();
//            double la = allLocations.get(i).getLatitude().doubleValue();
//            double lg = allLocations.get(i).getLongitude().doubleValue();
//            LocationDistance ld = new LocationDistance(fname, lname, la, lg);
//            result.add(ld);
//        }
//
//        return result;
//    }
//    

    @GET
    @Path("findNearestStd/{stdId}/{currLatitude}/{currLongitude}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<LocationDistance> findNearestStd(@PathParam("stdId") int stdId,
            @PathParam("currLatitude") double currLatitude,
            @PathParam("currLongitude") double currLongitude) throws ParseException {
        // get the list of students' latest location record
        TypedQuery<LocationLatestForEach> lfe
                = em.createQuery("SELECT "
                        + "NEW MonashFFPkg.LocationLatestForEach(l.locationPK.stdId, MAX(l.locationPK.dateTime)) "
                        + "FROM Location l GROUP BY l.locationPK.stdId", LocationLatestForEach.class);
        List<LocationLatestForEach> locAndLatestTimeS = lfe.getResultList();
        System.out.print(locAndLatestTimeS.size());

        // get the location list that exclude the given student or the duplicated student
        List<Location> allLocations = new ArrayList<>();

        // remove the record for the certain student by stdId
        for (int i = 0; i < locAndLatestTimeS.size(); i++) {
            LocationLatestForEach currTemp = locAndLatestTimeS.get(i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String tmpDate = sdf.format(currTemp.getDateTime());
            Timestamp ts = Timestamp.valueOf(tmpDate);
            if (currTemp.getStdId() != stdId) {
                TypedQuery<Location> q
                        = em.createQuery("SELECT l FROM Location l "
                                + "WHERE l.locationPK.stdId = :id "
                                + "AND l.locationPK.dateTime = :dateTime", Location.class);
                q.setParameter("id", currTemp.getStdId());
                q.setParameter("dateTime", ts);
                allLocations.add(q.getSingleResult());
            }
        }

        List<Double> distances = new ArrayList<>();
        for (Location location : allLocations) {
            double dist = this.computeDistance(location.getLatitude().doubleValue(),
                    location.getLongitude().doubleValue(),
                    currLatitude, currLongitude);
            distances.add(dist);
        }

        // sort the allLocations list base on the distance
        // Bubble sort
        double tempDis;
        Location tempLoc;
        for (int i = 0; i < distances.size(); i++) {
            for (int j = 1; j < distances.size() - i; j++) {
                if (distances.get(j - 1) > distances.get(j)) {
                    tempDis = distances.get(j - 1);
                    tempLoc = allLocations.get(j - 1);

                    distances.set(j - 1, distances.get(j));
                    allLocations.set(j - 1, allLocations.get(j));

                    distances.set(j, tempDis);
                    allLocations.set(j, tempLoc);
                }
            }
        } //  so the sorted allLocations list has been genenrated

        List<LocationDistance> result = new ArrayList<>();
        // in this case, only return 10 record sorted by distance
        // get the limitation in case of there are less than 10 record
        int minNum = Math.min(allLocations.size(), 9);
        for (int i = 0; i < minNum; i++) {
            String fname = allLocations.get(i).getStudent().getFname();
            String lname = allLocations.get(i).getStudent().getLname();
            double la = allLocations.get(i).getLatitude().doubleValue();
            double lg = allLocations.get(i).getLongitude().doubleValue();
            LocationDistance ld = new LocationDistance(fname, lname, la, lg);
            result.add(ld);
        }

        return result;
    }

    // this method is used to compute the distance 
    public double computeDistance(double latitude, double longtitude, 
            double currLatitude, double currLongitude) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(latitude - currLatitude);
        double dLng = Math.toRadians(longtitude - currLongitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(currLatitude))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;

    }

}
