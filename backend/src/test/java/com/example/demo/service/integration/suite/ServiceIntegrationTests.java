package com.example.demo.service.integration.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.example.demo.service.integration.AccountActivationServiceTest;
import com.example.demo.service.integration.CategoryServiceTest;
import com.example.demo.service.integration.CommentServiceTest;
import com.example.demo.service.integration.CulturalOfferServiceTest;
import com.example.demo.service.integration.ImageServiceTest;
import com.example.demo.service.integration.NewsServiceTest;
import com.example.demo.service.integration.TypeServiceTest;
import com.example.demo.service.integration.UserFollowingServiceTest;
import com.example.demo.service.integration.UserServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ AccountActivationServiceTest.class, CategoryServiceTest.class, CommentServiceTest.class,
		CulturalOfferServiceTest.class, ImageServiceTest.class, NewsServiceTest.class,
		TypeServiceTest.class, UserFollowingServiceTest.class, UserServiceTest.class })
public class ServiceIntegrationTests {

}
