package com.example.demo.repository.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.example.demo.repository.AccountActivationRepositoryTest;
import com.example.demo.repository.AuthorityRepositoryTest;
import com.example.demo.repository.CategoryRepositoryTest;
import com.example.demo.repository.CommentRepositoryTest;
import com.example.demo.repository.CulturalOfferRepositoryTest;
import com.example.demo.repository.ImageRepositoryTest;
import com.example.demo.repository.NewsRepositoryTest;
import com.example.demo.repository.TypeRepositoryTest;
import com.example.demo.repository.UserFollowingRepositoryTest;
import com.example.demo.repository.UserRepositoryTest;

@RunWith(Suite.class)
@SuiteClasses({ AccountActivationRepositoryTest.class, AuthorityRepositoryTest.class, CategoryRepositoryTest.class,
		CommentRepositoryTest.class, CulturalOfferRepositoryTest.class, ImageRepositoryTest.class,
		NewsRepositoryTest.class, TypeRepositoryTest.class, UserFollowingRepositoryTest.class,
		UserRepositoryTest.class })
public class RepositoryIntegrationTests {

}
