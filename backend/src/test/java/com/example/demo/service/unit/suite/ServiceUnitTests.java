package com.example.demo.service.unit.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.example.demo.service.unit.AccountActivationServiceTest;
import com.example.demo.service.unit.CategoryServiceTest;
import com.example.demo.service.unit.CommentServiceTest;
import com.example.demo.service.unit.CulturalOfferServiceTest;
import com.example.demo.service.unit.EmailServiceTest;
import com.example.demo.service.unit.ImageServiceTest;
import com.example.demo.service.unit.NewsServiceTest;
import com.example.demo.service.unit.TypeServiceTest;
import com.example.demo.service.unit.UserFollowingServiceTest;
import com.example.demo.service.unit.UserServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ AccountActivationServiceTest.class, CategoryServiceTest.class, CommentServiceTest.class,
		CulturalOfferServiceTest.class, EmailServiceTest.class, ImageServiceTest.class, NewsServiceTest.class,
		TypeServiceTest.class, UserFollowingServiceTest.class, UserServiceTest.class })
public class ServiceUnitTests {

}
