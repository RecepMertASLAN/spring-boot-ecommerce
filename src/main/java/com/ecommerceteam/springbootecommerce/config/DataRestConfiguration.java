package com.ecommerceteam.springbootecommerce.config;

import com.ecommerceteam.springbootecommerce.entities.concretes.Product;
import com.ecommerceteam.springbootecommerce.entities.concretes.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class DataRestConfiguration implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public DataRestConfiguration(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] unSupportedMethods = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};

        // disable HTTP Methods For Product : PUT ,  Post , Delete

        config.getExposureConfiguration().forDomainType(Product.class).withItemExposure(((metdata, httpMethods) -> httpMethods.disable(unSupportedMethods)))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unSupportedMethods));

// disable HTTP Methods For Product_Category : PUT ,  Post , Delete
        config.getExposureConfiguration().forDomainType(ProductCategory.class).withItemExposure(((metdata, httpMethods) -> httpMethods.disable(unSupportedMethods)))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unSupportedMethods));

        // Internal Helper
        showIds(config);

    }

    private void showIds(RepositoryRestConfiguration config) {

        // Get Entity Types from Entity Manager
        Set<javax.persistence.metamodel.EntityType<?>> entities;
        entities = entityManager.getMetamodel().getEntities();

        List<Class> entityClasses = new ArrayList<>();

        // Get the Entity Types for the entities
        for (EntityType<?> tempEntityType : entities) {
            entityClasses.add(tempEntityType.getJavaType());
        }

        // show entity ids for the array of domain types
        Class[] domainEntityTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainEntityTypes);


    }
}
