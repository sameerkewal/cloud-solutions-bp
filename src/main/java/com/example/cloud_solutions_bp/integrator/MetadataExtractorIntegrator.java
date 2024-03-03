package com.example.cloud_solutions_bp.integrator;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.spi.BootstrapContext;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.hibernate.type.StandardBasicTypes;

public class MetadataExtractorIntegrator
        implements org.hibernate.integrator.spi.Integrator{
    /**
     * @param metadata
     * @param sessionFactory
     * @param serviceRegistry
     * @deprecated
     */
    public static final MetadataExtractorIntegrator INSTANCE =
            new MetadataExtractorIntegrator();

    private Database database;

    private Metadata metadata;

    public Database getDatabase() {
        return database;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public void integrate(
            Metadata metadata,
            SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {

        this.database = metadata.getDatabase();
        this.metadata = metadata;

        metadata.getSqlFunctionMap().put("group_concat", new StandardSQLFunction( "group_concat", StandardBasicTypes.STRING));

    }

    @Override
    public void disintegrate(
            SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {

    }
}