package com.bridgelabz.repository;

import com.bridgelabz.entity.QuantityMeasurementEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton repository implementation using an in-memory cache backed by file persistence.
 * Implements IQuantityMeasurementRepository to provide CRUD operations for QuantityMeasurementEntity.
 *
 * Uses Java Object Serialization to persist operation history to disk, and loads
 * existing history on initialization for continuity across application restarts.
 *
 * The Singleton pattern ensures a single centralized data store across the application.
 */
public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    private static final String DATA_FILE = "quantity_measurements.dat";
    private static QuantityMeasurementCacheRepository instance;

    private final List<QuantityMeasurementEntity> cache;

    /**
     * Private constructor — loads any existing history from disk.
     */
    private QuantityMeasurementCacheRepository() {
        this.cache = new ArrayList<>();
        loadFromDisk();
    }

    /**
     * Returns the singleton instance of QuantityMeasurementCacheRepository.
     *
     * @return the singleton repository instance
     */
    public static synchronized QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    /**
     * Resets the singleton instance. Used primarily for testing.
     */
    public static synchronized void resetInstance() {
        instance = null;
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        cache.add(entity);
        saveToDisk(entity);
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return Collections.unmodifiableList(new ArrayList<>(cache));
    }

    @Override
    public void clearHistory() {
        cache.clear();
        File file = new File(DATA_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Appends a single entity to the data file using ObjectOutputStream.
     * Uses AppendableObjectOutputStream when the file already exists to avoid
     * writing duplicate stream headers.
     */
    private void saveToDisk(QuantityMeasurementEntity entity) {
        File file = new File(DATA_FILE);
        boolean append = file.exists() && file.length() > 0;
        try (FileOutputStream fos = new FileOutputStream(file, append);
             ObjectOutputStream oos = append
                     ? new AppendableObjectOutputStream(fos)
                     : new ObjectOutputStream(fos)) {
            oos.writeObject(entity);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Warning: Could not save measurement to disk — " + e.getMessage());
        }
    }

    /**
     * Loads all previously persisted entities from disk into the in-memory cache.
     */
    private void loadFromDisk() {
        File file = new File(DATA_FILE);
        if (!file.exists() || file.length() == 0) {
            return;
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof QuantityMeasurementEntity entity) {
                        cache.add(entity);
                    }
                } catch (EOFException e) {
                    break; // end of file reached
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Warning: Could not load measurements from disk — " + e.getMessage());
        }
    }

    /**
     * Custom ObjectOutputStream that suppresses the stream header when appending.
     * This ensures the data file remains valid for reading multiple objects sequentially.
     */
    private static class AppendableObjectOutputStream extends ObjectOutputStream {

        AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}
