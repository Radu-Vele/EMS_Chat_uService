package com.chatus.config;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import com.mongodb.client.MongoClient;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {
    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Value("${spring.data.mongodb.database}")
    private String dbName;

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(uri);
    }

    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), dbName);
    }
}
