package persistence;

import model.Song;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

public class SongRepository {
    private EntityManagerFactory emf;

    public SongRepository() {
        emf = Persistence.createEntityManagerFactory("musicPlayerPU");
    }

    public void addSong(Song song) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SongEntity entity = convertToEntity(song);
            if (song.getId() == null) {
                em.persist(entity);
            } else {
                em.merge(entity);
            }
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


    public Song getSongById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            SongEntity entity = em.find(SongEntity.class, id);
            return entity != null ? convertToSong(entity) : null;
        } finally {
            em.close();
        }
    }

    public List<Song> getAllSongs() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<SongEntity> query = em.createQuery("SELECT s FROM SongEntity s", SongEntity.class);
            List<SongEntity> entities = query.getResultList();
            return entities.stream().map(this::convertToSong).collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    public void updateSong(Song song) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SongEntity entity = convertToEntity(song);
            em.merge(entity);
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

    public void deleteSong(Long id) {
        EntityManager em = emf.createEntityManager();
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
        } finally {
            em.close();
        }
    }

    private Song convertToSong(SongEntity entity) {
        return new Song(entity.getId(), entity.getTitle(), entity.getArtist(), entity.getFilePath(), entity.getLyricsPath(), entity.getImagePath());
    }

    private SongEntity convertToEntity(Song song) {
        SongEntity entity = new SongEntity();
        entity.setId(song.getId());
        entity.setTitle(song.getTitle());
        entity.setArtist(song.getArtist());
        entity.setFilePath(song.getFilePath());
        entity.setLyricsPath(song.getLyricsPath());
        entity.setImagePath(song.getImagePath());
        return entity;
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
