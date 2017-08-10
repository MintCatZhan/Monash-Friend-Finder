## Monash Friend Finder

### 描述：

Monash Friend Finder (简称MFF) 是一个以莫纳什大学在读学生为目标用户的基础安卓开发项目。该项目开发的最基本动机是由于学了RESTful Web Service和Android开发之后，希望通过创建一个简单的APP的实践加强理解。该APP的主要功能包括：

- 用户注册、登陆，并允许登陆后修改个人资料
- 获取用户当前所在位置（经纬度）并在登陆的首界面返回当地天气情况
- 允许用于根据一定的标准（如兴趣爱好、专业、国籍和语言）来找到潜在的其他同学（“可能认识的人”），从而建立添加其他同学为好友；并允许根据“可能认识的人”所在的地址，显示在地图上
- 用户可以浏览自己的好友列表，并可以删除好友
- 以用户当前的实际位置为圆点，显示身边相应半径内的好友
- 简单的视觉可视化，显示当前用户的专业分布



### 效果图：

![mainpage](https://ws2.sinaimg.cn/large/006tNc79gy1fieowt8z3wj30h60vewgc.jpg)

![searchPageMap_1](https://ws2.sinaimg.cn/large/006tNc79gy1fieowp79lbj30h60vegoi.jpg)





### 所涉技术：

因为该项目是在学习过程的个人小项目，所以我在UI设计、数据库选择、Android控件的实践中仍然有很多不足，若得批评指正，不胜感激。下面列举实践过程中所使用到的相关技术：

- 数据库

  - 使用<u>Java derby</u>作为数据库，用于存储“Student”, "Location"和“Friendship”，**代码**如下：

    ```sql
    CREATE TABLE "STUDENT" (
      "STD_ID" NUMERIC(10),
      "FNAME" CHAR(35) NOT NULL,
      "LNAME" CHAR(35) NOT NULL,
      "DOB" DATE NOT NULL,
      "GENDER" BOOLEAN NOT NULL,
      "COURSE" CHAR(5) NOT NULL,
      "MODE" BOOLEAN NOT NULL,
      "ADDRESS" VARCHAR(200) NOT NULL,
      "SUBURB" CHAR(20) NOT NULL,
      "NATIONALITY" CHAR(20) NOT NULL,
      "LANG" CHAR(20) NOT NULL,
      "FAVOURITE_SPORT" VARCHAR(200) NOT NULL,
      "FAVOURITE_MOVIE" VARCHAR(200) NOT NULL,
      "FAVOURITE_UNIT" CHAR(10) NOT NULL,
      "CURRENT_JOB" VARCHAR(200) NOT NULL, 
      "EMAIL_ADDR" VARCHAR(255) NOT NULL,
      "PWD" CHAR(64) NOT NULL,
      "SUBSCRIPT_DATETIME" TIMESTAMP NOT NULL,
      CONSTRAINT PK_STUDENT PRIMARY KEY ("STD_ID")
    );

    CREATE TABLE "LOCATION" (
      "LATITUDE" DECIMAL(9,6) NOT NULL,
      "LONGITUDE" DECIMAL(9,6) NOT NULL,
      "DATE_TIME" TIMESTAMP NOT NULL,
      "LOC_NAME" VARCHAR(200) NOT NULL,
      "STD_ID" NUMERIC(10) NOT NULL,
      CONSTRAINT PK_LOCATION PRIMARY KEY ("STD_ID", "DATE_TIME"),
      CONSTRAINT FK_LOCATION_STUDENT FOREIGN KEY ("STD_ID") REFERENCES "STUDENT" (STD_ID)
    );


    CREATE TABLE "FRIENDSHIP" (
      "STD_ID" NUMERIC(10),
      "FRIEND_ID" NUMERIC(10),
      "STRATING_DATE" DATE NOT NULL,
      "ENDING_DATE" DATE,
      CONSTRAINT PK_FRIENDSHIP PRIMARY KEY ("STD_ID","FRIEND_ID"),
      CONSTRAINT FK_FRIENDSHIP_STUDENT1 FOREIGN KEY ("STD_ID" )
          REFERENCES "STUDENT" ("STD_ID"),
      CONSTRAINT FK_FRIENDSHIP_STUDENT2 FOREIGN KEY ("FRIEND_ID" )
          REFERENCES "STUDENT" ("STD_ID"),
      CONSTRAINT CHECK_FRIENDSHIP CHECK("STD_ID" < "FRIEND_ID")
    );
    ```

  - ERD:

    ![erd](https://ws1.sinaimg.cn/large/006tNc79gy1fiepyj859wj30ts0hq77m.jpg)

- RESTful Web Service:

  - 使用REST的基本思想，提供对数据库进行增删改查的标准接口。
  - 支持JSON和XML格式的数据库读取结果
  - Android应用通过建立HttpURLConnection和Web Service连接，从而对数据库进行读写

- SQLite Database:

  - 用于提供Android本地的简单数据存储服务

- Open APIs & Services：

  - OpenWeatherMap：用于根据用户当前位置获取天气信息，通过建立HttpURLConnection获取JSON格式的数据，摘取对应信息
  - Google Customer Search Engine：用于支持搜索电影信息，返回该电影的海报和简介，所获取的数据也以JSON格式返回。
  - Google Map API：用于提供地图服务
  - MPAndroidChart: 用于支持简单数据可视化功能(barChart & pieChart) https://github.com/PhilJay/MPAndroidChart



### 后记：

- 如同之前说过的那样，制作这个简单的小项目的本意是用来帮助学习，实际上在完成的过程中，也学习了很多，比如对RESTful的理解更为具象，对开发一个Android应用时可能涉及到的技术有了接触和了解，甚至是对于数据库的设计都较实践前更深刻

- 这个很简单的APP完成之后，感觉自己对于移动端开发产生了浓厚的兴趣，不过也发现了很多的不足的地方，希望大家不吝赐教，也希望自己日后能够完成功能更为复杂、设计更为精妙的应用，从而正式publish

- 感谢大牛提供的各种第三方库，在我学习的过程中提供了大量的帮助

  ​