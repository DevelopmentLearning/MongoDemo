package com.culmanu.mongo.controllers;


import com.culmanu.mongo.dtos.*;
import com.culmanu.mongo.enitities.User;
import com.culmanu.mongo.services.AggregationService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agg")
@RequiredArgsConstructor
public class AggregationController {
    private final AggregationService aggregationService;

    // 1. $match
    @GetMapping("/users/age/{age}")
    public List<User> getUsersByAge(@PathVariable int age) {
        return aggregationService.matchUsersByAge(age);
    }

    // 2. $group
    @GetMapping("/users/group-by-country")
    public List<CountrySummary> getUsersGroupedByCountry() {
        return aggregationService.groupByCountry();
    }

    // 3. $project
    @GetMapping("/users/projection")
    public List<UserProjection> getUserProjections() {
        return aggregationService.projectNameAndEmail();
    }

    // 4. $sort
    @GetMapping("/users/sort-by-age")
    public List<User> getUsersSortedByAge() {
        return aggregationService.sortByAgeDesc();
    }

    // 5. $limit
    @GetMapping("/users/limit/{limit}")
    public List<User> getLimitedUsers(@PathVariable int limit) {
        return aggregationService.limitUsers(limit);
    }

    // 6. $skip
    @GetMapping("/users/skip/{skip}")
    public List<User> getSkippedUsers(@PathVariable int skip) {
        return aggregationService.skipUsers(skip);
    }

    // 7. $unwind
    @GetMapping("/users/unwind-tags")
    public List<Document> getUsersWithUnwoundTags() {
        return aggregationService.unwindTags();
    }

    // 8. Combined $project + $sort + $unwind
    @GetMapping("/users/project-sort-unwind")
    public List<Document> getProjectedSortedUnwoundUsers() {
        return aggregationService.projectSortUnwind();
    }

    // 9. $lookup
    @GetMapping("/users/with-orders")
    public List<UserWithOrders> getUsersWithOrders() {
        return aggregationService.lookupOrders();
    }

    // 10. $addFields
    @GetMapping("/users/add-balance-status")
    public List<Document> getUsersWithBalanceStatus() {
        return aggregationService.addBalanceStatus();
    }

    // 11. $count
    @GetMapping("/users/count/{country}")
    public Long countUsersByCountry(@PathVariable String country) {
        return aggregationService.countUsersFromCountry(country);
    }

    // 12. $facet
    @GetMapping("/users/facet-example")
    public Document getFacetExample() {
        return aggregationService.facetExample();
    }

    // 13. $bucket
    @GetMapping("/users/bucket-age")
    public List<Document> getUsersBucketedByAge() {
        return aggregationService.bucketByAge();
    }

    // 14. $sortByCount
    @GetMapping("/users/sort-tags-by-count")
    public List<Document> getTagsSortedByCount() {
        return aggregationService.sortTagsByCount();
    }

    // 15. $graphLookup
    @GetMapping("/users/hierarchy")
    public List<Document> getUserHierarchy() {
        return aggregationService.graphLookupHierarchy();
    }

    // 16. $replaceRoot
    @GetMapping("/users/replace-root-address")
    public List<Address> getUsersWithReplacedRoot() {
        return aggregationService.replaceRootToAddress();
    }

    // 17. $merge
    @PostMapping("/users/merge-country-summary")
    public void mergeCountrySummary() {
        aggregationService.mergeCountrySummary();
    }

    // 18. $out
    @PostMapping("/users/out-senior-users")
    public void outSeniorUsers() {
        aggregationService.outSeniorUsers();
    }

    // 19. $sample
    @GetMapping("/users/sample/{size}")
    public List<User> getSampleUsers(@PathVariable int size) {
        return aggregationService.sampleUsers(size);
    }
}

