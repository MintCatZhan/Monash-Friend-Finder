/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MonashFFPkg.service;

import MonashFFPkg.Friendship;
import MonashFFPkg.FriendshipPK;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
@Path("monashffpkg.friendship")
public class FriendshipFacadeREST extends AbstractFacade<Friendship> {

    @PersistenceContext(unitName = "MonashFFWSPU")
    private EntityManager em;

    private FriendshipPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;stdId=stdIdValue;friendId=friendIdValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        MonashFFPkg.FriendshipPK key = new MonashFFPkg.FriendshipPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> stdId = map.get("stdId");
        if (stdId != null && !stdId.isEmpty()) {
            key.setStdId(new java.lang.Long(stdId.get(0)));
        }
        java.util.List<String> friendId = map.get("friendId");
        if (friendId != null && !friendId.isEmpty()) {
            key.setFriendId(new java.lang.Long(friendId.get(0)));
        }
        return key;
    }

    public FriendshipFacadeREST() {
        super(Friendship.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Friendship entity) {
        try {
            if (entity.getFriendshipPK().getStdId() > entity.getFriendshipPK().getFriendId()) {
                FriendshipPK fpk
                        = new FriendshipPK(entity.getFriendshipPK().getFriendId(),
                                entity.getFriendshipPK().getStdId());
                entity.setFriendshipPK(fpk);
            }
            super.create(entity);
        } catch (Exception ex) {
            System.out.println("Errors in creation" + ex);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, Friendship entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        MonashFFPkg.FriendshipPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Friendship find(@PathParam("id") PathSegment id) {
        MonashFFPkg.FriendshipPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Friendship> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Friendship> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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

    // adding finding methods
    // find by stdId
    @GET
    @Path("findByStdId/{stdId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Friendship> findByStdId(@PathParam("stdId") int stdId) {
        Query query = em.createNamedQuery("Friendship.findByStdId");
        query.setParameter("stdId", stdId);
        return query.getResultList();
    }

    // find by friendId
    @GET
    @Path("findByFriendId/{friendId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Friendship> findByFriendId(@PathParam("friendId") int friendId) {
        Query query = em.createNamedQuery("Friendship.findByFriendId");
        query.setParameter("friendId", friendId);
        return query.getResultList();
    }

    // find by stratingDate
    @GET
    @Path("findByStratingDate/{stratingDate}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Friendship> findByStratingDate(@PathParam("stratingDate") String stratingDate) {
        try {
            Query query = em.createNamedQuery("Friendship.findByStratingDate");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(stratingDate);
            query.setParameter("stratingDate", date);
            return query.getResultList();
        } catch (ParseException ex) {
            Logger.getLogger(FriendshipFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    // find by endingDate
    @GET
    @Path("findByEndingDate/{endingDate}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Friendship> findByEndingDate(@PathParam("endingDate") String endingDate) {
        try {
            Query query = em.createNamedQuery("Friendship.findByEndingDate");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(endingDate);
            query.setParameter("endingDate", date);
            return query.getResultList();
        } catch (ParseException ex) {
            Logger.getLogger(FriendshipFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
