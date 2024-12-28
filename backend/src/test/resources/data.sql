----------------------------------------IMAGES----------------------------------------
insert into image_table (path) values ('image1');

----------------------------------------AUTHORITIES----------------------------------------
insert into authority_table (name) values ('authority1');
insert into authority_table (name) values ('admin');
insert into authority_table (name) values ('guest');

----------------------------------------USERS----------------------------------------
insert into user_table (email, password, first_name, last_name, enabled)
values ('email1@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'first_name1', 'last_name1', true);
insert into user_table (email, password, first_name, last_name, enabled)
values ('email2@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'first_name2', 'last_name2', true);
insert into user_table (email, password, first_name, last_name, enabled)
values ('asd@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'asd', 'asd', true);
insert into user_authority (user_id, authority_id) values (1, 3);
insert into user_authority (user_id, authority_id) values (2, 2);
insert into user_authority (user_id, authority_id) values (3, 3);

----------------------------------------ACCOUNT ACTIVATIONS----------------------------------------
insert into account_activation_table (user_id, code) values (1, 'code1');

----------------------------------------CATEGORIES----------------------------------------
insert into category_table (name) values ('category1');
insert into category_table (name) values ('category2');
insert into category_table (name) values ('category3');

----------------------------------------TYPES----------------------------------------
insert into type_table (category_id, name) values (1, 'type1');
insert into type_table (category_id, name) values (2, 'type2');
insert into type_table (category_id, name) values (3, 'type3');

----------------------------------------CULTURAL OFFERS----------------------------------------
insert into cultural_offer_table (type_id, name, location, lat, lng) values (1, 'cultural_offer1', 'location1', 10, 10);
insert into cultural_offer_table (type_id, name, location, lat, lng) values (2, 'cultural_offer2', 'location2', 20, 20);
insert into cultural_offer_table (type_id, name, location, lat, lng) values (3, 'cultural_offer3', 'location3', 30, 30);

----------------------------------------NEWS----------------------------------------
insert into news_table (cultural_offer_id, created_at, text) values (2, '2020-09-12', 'news1');
insert into news_table (cultural_offer_id, created_at, text) values (3, '2020-09-14', 'news2');
insert into news_table (cultural_offer_id, created_at, text) values (3, '2020-09-10', 'news3');
insert into news_table (cultural_offer_id, created_at, text) values (3, '2020-09-16', 'news4');

----------------------------------------COMMENTS----------------------------------------
insert into comment_table (user_id, cultural_offer_id, created_at, rate, text) values (1, 1, '2020-12-12', 3, 'comment1');
insert into comment_table (user_id, cultural_offer_id, created_at, rate, text) values (2, 1, '2020-12-14', 2, 'comment2');
insert into comment_table (user_id, cultural_offer_id, created_at, rate, text) values (2, 1, '2020-12-10', 4, 'comment3');
insert into comment_table (user_id, cultural_offer_id, created_at, rate, text) values (1, 2, '2020-12-11', 1, 'comment4');

----------------------------------------USER FOLLOWINGS----------------------------------------
insert into user_following_table (user_id, cultural_offer_id) values (1, 1);
insert into user_following_table (user_id, cultural_offer_id) values (1, 2);
insert into user_following_table (user_id, cultural_offer_id) values (1, 3);
insert into user_following_table (user_id, cultural_offer_id) values (2, 1);
