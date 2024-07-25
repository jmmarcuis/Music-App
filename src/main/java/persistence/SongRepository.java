package persistence;


import javax.persistence.*;
import java.util.List;

public class SongRepository {
    private EntityManagerFactory emf;
    private EntityManager em;

    public SongRepository() {
        emf = Persistence.createEntityManagerFactory("musicPlayerPU");
        em = emf.createEntityManager();
    }


    public void addSong(String title, String artist, String filePath) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SongEntity entity = new SongEntity(title, artist, filePath);
            em.persist(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


    public SongEntity getSongById(Long id) {
        return em.find(SongEntity.class, id);
    }

    public List<SongEntity> getAllSongs() {
        TypedQuery<SongEntity> query = em.createQuery("SELECT s FROM SongEntity s", SongEntity.class);
        return query.getResultList();
    }

    public void updateSong(SongEntity song) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(song);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public void deleteSong(Long id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SongEntity song = em.find(SongEntity.class, id);
            if (song != null) {
                em.remove(song);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public void close() {
        em.close();
        emf.close();
    }
}