package com.bridgelabz.quantitymeasurement.util;

import com.bridgelabz.quantitymeasurement.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple connection pool for H2 database connections.
 * Uses a BlockingQueue to manage a fixed number of connections.
 */
public class ConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

    private final String url;
    private final String username;
    private final String password;
    private final int poolSize;

    private final BlockingQueue<Connection> availableConnections;
    private final AtomicInteger totalCreated = new AtomicInteger(0);
    private final AtomicInteger activeCount = new AtomicInteger(0);
    private volatile boolean closed = false;

    public ConnectionPool(String url, String username, String password, int poolSize) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.poolSize = poolSize;
        this.availableConnections = new LinkedBlockingQueue<>(poolSize);
    }

    /**
     * Initialize the pool by creating all connections upfront.
     */
    public void initialize() {
        logger.info("Initializing connection pool: url={}, poolSize={}", url, poolSize);
        for (int i = 0; i < poolSize; i++) {
            try {
                Connection conn = createConnection();
                availableConnections.offer(conn);
                totalCreated.incrementAndGet();
            } catch (SQLException e) {
                throw new DatabaseException("Failed to initialize connection pool", e);
            }
        }
        logger.info("Connection pool initialized with {} connections", totalCreated.get());
    }

    /**
     * Acquire a connection from the pool.
     */
    public Connection acquireConnection() {
        if (closed) {
            throw new DatabaseException("Connection pool is closed");
        }
        try {
            Connection conn = availableConnections.take();
            // Check if connection is still valid, replace if not
            if (conn.isClosed()) {
                conn = createConnection();
            }
            activeCount.incrementAndGet();
            logger.debug("Connection acquired. Active: {}, Available: {}",
                    activeCount.get(), availableConnections.size());
            return conn;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DatabaseException("Interrupted while acquiring connection", e);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to acquire connection", e);
        }
    }

    /**
     * Release a connection back to the pool.
     */
    public void releaseConnection(Connection conn) {
        if (conn == null) return;
        try {
            if (!conn.isClosed()) {
                availableConnections.offer(conn);
            } else {
                // Replace closed connection with a new one
                Connection newConn = createConnection();
                availableConnections.offer(newConn);
            }
            activeCount.decrementAndGet();
            logger.debug("Connection released. Active: {}, Available: {}",
                    activeCount.get(), availableConnections.size());
        } catch (SQLException e) {
            logger.error("Error releasing connection: {}", e.getMessage());
        }
    }

    /**
     * Get pool statistics as a readable string.
     */
    public String getStatistics() {
        return String.format("ConnectionPool[total=%d, active=%d, available=%d, closed=%s]",
                totalCreated.get(), activeCount.get(), availableConnections.size(), closed);
    }

    /**
     * Close all connections and shut down the pool.
     */
    public void closeAll() {
        closed = true;
        logger.info("Closing connection pool...");
        Connection conn;
        int closedCount = 0;
        while ((conn = availableConnections.poll()) != null) {
            try {
                conn.close();
                closedCount++;
            } catch (SQLException e) {
                logger.error("Error closing connection: {}", e.getMessage());
            }
        }
        logger.info("Connection pool closed. {} connections closed.", closedCount);
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public boolean isClosed() {
        return closed;
    }
}
