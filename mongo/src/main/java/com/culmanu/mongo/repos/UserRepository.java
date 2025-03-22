package com.culmanu.mongo.repos;

import com.culmanu.mongo.enitities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    List<User> findByAge(int age);
    List<User> findByNameIgnoreCaseContaining(String name);
    List<User> findByAddress_Country(String country);
    List<User> findByBalanceGreaterThan(double balance);
    List<User> findByTagsContaining(String tag);
    List<User> findByEmailEndingWith(String domain);
    Optional<User> findByEmail(String email);

    @Query("{ 'age' : { $gt: ?0 } }")
    List<User> findUsersOlderThan(int age);

    @Query("{ 'tags': ?0 }")
    List<User> findUsersByRole(String role);

    @Query("{ 'address.country': ?0, 'age': { $gte: ?1, $lte: ?2 } }")
    List<User> findUsersByCountryAndAgeBetween(String country, int minAge, int maxAge);

    @Query(value = "{ 'balance': { $gte: ?0 } }", sort = "{ 'balance': -1 }")
    List<User> findRichUsersSortedByBalance(double minBalance);
}
