package services;

import java.util.List;
import java.sql.Timestamp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import data.*;

@Path("/lego")
public class LegoService {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("lego");
    private static int obstacleCount = 0;

    @Path("/getlego")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getLego() {
        return "Lego service Legorest2!";
    }

    @Path("/setvalues")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Lego setValues(Lego lego) {
        EntityManager em = emf.createEntityManager();
        // set timestamp at insert time
        lego.setAika(new Timestamp(System.currentTimeMillis()));
        em.getTransaction().begin();
        em.persist(lego);
        em.getTransaction().commit();
        em.refresh(lego);
        em.close();
        return lego;
    }

    @SuppressWarnings("unchecked")
    @Path("/getvalues")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getValues() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Query q = em.createQuery("select s from Lego s order by s.id desc").setMaxResults(1);
        List<Lego> list = q.getResultList();
        em.getTransaction().commit();
        em.close();
        // Return default values if DB is empty
        if (list.isEmpty()) {
            return "0#1#0#0#0";
        }
        Lego lego = list.get(0);
        return lego.getId() + "#" + lego.getRun() + "#" + lego.getSpeed() + "#" + lego.getTurn() + "#" + lego.getObstacles();
    }

    @Path("/obstacledetected")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String obstacleDetected() {
        obstacleCount++;
        return String.valueOf(obstacleCount);
    }

    @Path("/obstaclecount")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getObstacleCount() {
        return String.valueOf(obstacleCount);
    }

}