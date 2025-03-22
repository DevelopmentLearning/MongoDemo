package com.culmanu.mongo.services;

import com.culmanu.mongo.dtos.Address;
import com.culmanu.mongo.dtos.CountrySummary;
import com.culmanu.mongo.dtos.UserProjection;
import com.culmanu.mongo.dtos.UserWithOrders;
import com.culmanu.mongo.dtos.CountResult;
import com.culmanu.mongo.enitities.User;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AggregationService {
    private final MongoTemplate mongoTemplate;

    // 1. $match
    public List<User> matchUsersByAge(int age) {
        return mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("age").is(age))
                ),
                "users",
                User.class
        ).getMappedResults();
    }

    // 2. $group
    public List<CountrySummary> groupByCountry() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("address.country")
                        .avg("balance")
                        .as("avgBalance")
                        .count()
                        .as("count"),
                Aggregation.project()
                        .and("_id").as("id") // Map _id to id
                        .andInclude("avgBalance", "count")
        );

        return mongoTemplate.aggregate(aggregation, "users", CountrySummary.class)
                .getMappedResults();
    }


    // 3. $project
    public List<UserProjection> projectNameAndEmail() {
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                        Aggregation.project()
                                .andExpression("toUpper(name)").as("fullName")
                                .and("email").as("email")
                                .andExclude("_id"))
                , "users", UserProjection.class).getMappedResults();
    }

    // 4. $sort
    public List<User> sortByAgeDesc() {
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                        Aggregation.sort(Sort.Direction.DESC, "age"))
                , "users", User.class).getMappedResults();
    }

    // 5. $limit
    public List<User> limitUsers(int limit) {
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                        Aggregation.limit(limit))
                , "users", User.class).getMappedResults();
    }

    // 6. $skip
    public List<User> skipUsers(int skip) {
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                        Aggregation.skip(skip))
                , "users", User.class).getMappedResults();
    }

    // 7. $unwind
    public List<Document> unwindTags() {
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                        Aggregation.unwind("tags"))
                , "users", Document.class).getMappedResults();
    }

    // 8. Combined $project + $sort + $unwind
    public List<Document> projectSortUnwind() {
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.project()
                        .andExpression("toUpper(name)").as("fullName")
                        .and("email").as("email")
                        .and("tags").as("tags")
                        .andExclude("_id"),
                Aggregation.sort(Sort.Direction.DESC, "age"),
                Aggregation.unwind("tags")
        ), "users", Document.class).getMappedResults();
    }

    // 9. $lookup
    public List<UserWithOrders> lookupOrders() {
        LookupOperation lookup = LookupOperation.newLookup()
                .from("orders")
                .localField("email")
                .foreignField("user_email")
                .as("orders");

        return mongoTemplate.aggregate(Aggregation.newAggregation(lookup),
                "users", UserWithOrders.class).getMappedResults();
    }

    // 10. $addFields
    public List<Document> addBalanceStatus() {
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.addFields()
                        .addField("balanceStatus")
                        .withValue(ConditionalOperators
                                .when(Criteria.where("balance").gte(1000))
                                .then("High")
                                .otherwise("Low"))
                        .build()
        ), "users", Document.class).getMappedResults();
    }

    // 11. $count
    public Long countUsersFromCountry(String country) {
        CountResult result = mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.match(Criteria.where("address.country").is(country)),
                Aggregation.count().as("count")
        ), "users", CountResult.class).getUniqueMappedResult();

        return result != null ? result.getCount() : 0L;
    }

    // 12. $facet
    public Document facetExample() {
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.facet()
                        .and(
                                Aggregation.bucket("age")
                                        .withBoundaries(25, 30, 35, 40, 45)
                                        .withDefaultBucket("Other")
                                        .andOutputCount().as("count")
                        ).as("ageGroups")
                        .and(
                                Aggregation.match(Criteria.where("tags").in("admin")),
                                Aggregation.count().as("count")
                        ).as("activeUsers")
        ), "users", Document.class).getUniqueMappedResult();
    }

    // 13. $bucket
    public List<Document> bucketByAge() {
        return mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.bucket("age")
                                .withBoundaries(25, 30, 35, 40, 45)
                                .withDefaultBucket("Other")
                                .andOutputCount().as("count")
                ),
                "users",
                Document.class
        ).getMappedResults();
    }

    // 14. $sortByCount
    public List<Document> sortTagsByCount() {
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.unwind("tags"),
                Aggregation.sortByCount("tags")
        ), "users", Document.class).getMappedResults();
    }

    // 15. $graphLookup
    public List<Document> graphLookupHierarchy() {
        GraphLookupOperation graphLookup = GraphLookupOperation.builder()
                .from("users")
                .startWith("$managerId")
                .connectFrom("managerId")
                .connectTo("_id")
                .as("reportingHierarchy");

        return mongoTemplate.aggregate(Aggregation.newAggregation(graphLookup),
                "users", Document.class).getMappedResults();
    }

    // 16. $replaceRoot
    public List<Address> replaceRootToAddress() {
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.replaceRoot("address")
        ), "users", Address.class).getMappedResults();
    }

    // 17. $merge
    public void mergeCountrySummary() {
        mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.group("address.country").count().as("total"),
                Aggregation.merge()
                        .into(MergeOperation.MergeOperationTarget.collection("country_summary"))
                        .on("_id")
                        .build()
        ), "users", Document.class);
    }

    // 18. $out
    public void outSeniorUsers() {
        mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.match(Criteria.where("age").gt(30)),
                Aggregation.out("senior_users")
        ), "users", Document.class);
    }

    // 19. $sample
    public List<User> sampleUsers(int size) {
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.sample(size)
        ), "users", User.class).getMappedResults();
    }

}
