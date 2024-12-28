package com.example.demo.controller.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.example.demo.controller.AuthControllerTest;
import com.example.demo.controller.CategoryControllerTest;
import com.example.demo.controller.CommentControllerTest;
import com.example.demo.controller.CulturalOfferControllerTest;
import com.example.demo.controller.NewsControllerTest;
import com.example.demo.controller.TypeControllerTest;
import com.example.demo.controller.UserControllerTest;
import com.example.demo.controller.UserFollowingControllerTest;

@RunWith(Suite.class)
@SuiteClasses({ AuthControllerTest.class, CategoryControllerTest.class, CommentControllerTest.class,
		CulturalOfferControllerTest.class, NewsControllerTest.class, TypeControllerTest.class, UserControllerTest.class,
		UserFollowingControllerTest.class })
public class ControllerIntegrationTests {

}
