package com.bridgelabz;

import com.bridgelabz.controller.QuantityMeasurementController;
import com.bridgelabz.repository.IQuantityMeasurementRepository;
import com.bridgelabz.repository.QuantityMeasurementCacheRepository;
import com.bridgelabz.service.IQuantityMeasurementService;
import com.bridgelabz.service.QuantityMeasurementServiceImpl;

/**
 * Main application class for the Quantity Measurement system — UC15: N-Tier Architecture.
 *
 * Serves as the entry point and wiring layer. Implements the Factory and Facade design patterns:
 * - Factory: creates instances of repository, service, and controller
 * - Facade: delegates all execution to the controller
 *
 * This class does NOT contain any business logic. It only:
 * 1. Initializes the repository (Singleton)
 * 2. Creates the service with repository dependency (Constructor DI)
 * 3. Creates the controller with service dependency (Constructor DI)
 * 4. Invokes the controller to run all demonstrations
 *
 * Design Patterns Used:
 * - Singleton: QuantityMeasurementCacheRepository
 * - Factory: this class creates layer instances
 * - Facade: QuantityMeasurementController provides simplified interface
 * - Dependency Injection: constructor injection for service and controller
 * - Interface Segregation: IQuantityMeasurementService, IQuantityMeasurementRepository
 */
public class QuantityMeasurementApp {

    private final QuantityMeasurementController controller;

    /**
     * Constructs the application by wiring all layers together.
     */
    public QuantityMeasurementApp() {
        // Step 1: Initialize Repository (Singleton)
        IQuantityMeasurementRepository repository = QuantityMeasurementCacheRepository.getInstance();

        // Step 2: Initialize Service with Repository dependency
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);

        // Step 3: Initialize Controller with Service dependency
        this.controller = new QuantityMeasurementController(service);
    }

    /**
     * Runs the application by delegating to the controller.
     */
    public void run() {
        controller.runAllDemonstrations();
    }

    /**
     * Application entry point.
     */
    public static void main(String[] args) {
        QuantityMeasurementApp app = new QuantityMeasurementApp();
        app.run();
    }
}