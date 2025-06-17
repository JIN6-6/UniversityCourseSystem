-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: university_course_db
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `completed_courses`
--

DROP TABLE IF EXISTS `completed_courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `completed_courses` (
  `student_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `course_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `credit` int NOT NULL,
  `course_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `grade` varchar(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `completion_date` date DEFAULT NULL,
  PRIMARY KEY (`student_id`,`course_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `completed_courses_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `users` (`userID`) ON DELETE CASCADE,
  CONSTRAINT `completed_courses_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`courseID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `completed_courses`
--

LOCK TABLES `completed_courses` WRITE;
/*!40000 ALTER TABLE `completed_courses` DISABLE KEYS */;
INSERT INTO `completed_courses` VALUES ('2022111053','MA02014-01',3,'전공선택','A+','2023-12-30'),('2022111053','MA03016-01',3,'전공선택','A+','2024-06-30'),('2022111053','MA03023-01',3,'전공필수','A+','2024-06-30'),('2022111053','MA03032-01',3,'전공선택','A+','2024-12-30'),('2022111053','MA03036-01',3,'전공선택','A+','2025-06-30'),('2022111053','MT01007-01',3,'전공선택','A+','2024-06-30'),('2022111053','MT01019-01',3,'전공필수','A+','2024-06-30'),('2022111053','MT01023-01',3,'전공선택','A+','2024-12-30'),('2022111053','MT01036-01',3,'전공선택','A+','2023-06-30'),('2022111053','MT01043-01',3,'전공필수','A+','2023-12-30'),('2022111053','MT01045-01',3,'전공선택','A+','2024-06-30'),('2022111053','MT01051-01',3,'전공선택','A+','2024-12-30'),('2022111053','MT01058-01',3,'전공선택','A+','2025-06-30'),('std001','MT01019-01',3,'전공필수','A0','2024-06-30'),('std001','MT01023-01',3,'전공선택','B+','2025-06-30'),('std001','MT01036-01',3,'전공선택','A+','2024-06-30'),('std001','MT01043-01',3,'전공필수','A-','2024-12-30'),('std002','MA02004-01',3,'전공선택','A0','2024-06-30'),('std002','MA02008-01',3,'전공필수','A+','2024-06-30'),('std002','MA02011-01',3,'전공필수','B+','2024-12-30'),('std003','MT01009-01',3,'전공선택','A-','2025-06-30'),('std003','MT01019-02',3,'전공필수','A+','2024-06-30'),('std003','MT01036-02',3,'전공선택','A0','2024-06-30'),('std003','MT01043-02',3,'전공필수','B+','2024-12-30'),('std004','MA02004-01',3,'전공선택','A+','2024-06-30'),('std004','MA02008-01',3,'전공필수','A0','2024-06-30'),('std004','MA03016-01',3,'전공선택','B0','2025-06-30'),('std005','MT01019-03',3,'전공필수','B0','2024-06-30'),('std005','MT01043-03',3,'전공필수','B+','2024-12-30'),('std006','MA02008-01',3,'전공필수','B+','2024-06-30'),('std006','MA02011-01',3,'전공필수','A-','2024-12-30'),('studnet','MT01007-01',3,'전공선택','B0','2025-06-30'),('studnet','MT01019-01',3,'전공필수','A+','2024-06-30'),('studnet','MT01036-01',3,'전공선택','A0','2024-06-30'),('studnet','MT01043-01',3,'전공필수','B+','2024-12-30'),('studnet','MT04005-01',3,'전공선택','A-','2025-06-30');
/*!40000 ALTER TABLE `completed_courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `courseID` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `courseName` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `professorID` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `credit` int NOT NULL,
  `time` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `maxCapacity` int NOT NULL,
  `currentEnrollment` int DEFAULT '0',
  `courseType` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '전공선택',
  `waitlist_count` int DEFAULT '0',
  `major_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`courseID`),
  KEY `professorID` (`professorID`),
  CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`professorID`) REFERENCES `users` (`userID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES ('MA02004-01','기하학개론','prof411',3,'화3/목2',40,39,'전공선택',0,'수학과'),('MA02008-01','해석학Ⅱ','prof414',3,'월3/금4',25,22,'전공선택',0,'수학과'),('MA02011-01','현대대수학','prof413',3,'목2/금2',40,35,'전공필수',0,'수학과'),('MA02014-01','미분방정식','prof411',3,'화4/목4',40,27,'전공선택',0,'수학과'),('MA03016-01','미분적분학Ⅰ','prof411',3,'화2/목5',50,47,'전공선택',0,'수학과'),('MA03019-01','전산통계학','prof412',3,'화3~4',40,37,'전공선택',0,'수학과'),('MA03020-01','확률론','prof415',3,'화5/금5',40,38,'전공선택',0,'수학과'),('MA03023-01','복소해석학','prof416',3,'월4/화6',40,39,'전공필수',0,'수학과'),('MA03030-01','이산수학','prof413',3,'수5/목3',40,24,'전공선택',0,'수학과'),('MA03032-01','선형대수학Ⅰ','prof413',3,'수6/목5',40,15,'전공선택',0,'수학과'),('MA03034-01','수학전공진로탐색','prof413',1,'수4',40,28,'전공선택',0,'수학과'),('MA03036-01','고등미분적분학','prof414',3,'월2/금3',40,38,'전공선택',0,'수학과'),('MT01002-01','컴퓨터그래픽스','prof311',3,'목5-6',25,25,'전공선택',0,'소프트웨어융합학과'),('MT01002-02','컴퓨터그래픽스','prof311',3,'월5-6',25,24,'전공선택',0,'소프트웨어융합학과'),('MT01007-01','영상처리','prof322',3,'화5-6',33,33,'전공선택',0,'소프트웨어융합학과'),('MT01009-01','유닉스프로그래밍','prof323',3,'목5-6',25,35,'전공선택',0,'소프트웨어융합학과'),('MT01019-01','자료구조','prof317',3,'화3-4',25,25,'전공필수',0,'소프트웨어융합학과'),('MT01019-02','자료구조','prof317',3,'목3-4',25,25,'전공필수',0,'소프트웨어융합학과'),('MT01019-03','자료구조','prof313',3,'수3-4',25,25,'전공필수',0,'소프트웨어융합학과'),('MT01023-01','윈도우프로그래밍','prof319',3,'월5-6',25,22,'전공선택',0,'소프트웨어융합학과'),('MT01023-02','윈도우프로그래밍','prof319',3,'월3-4',25,23,'전공선택',0,'소프트웨어융합학과'),('MT01024-01','프로젝트종합설계Ⅰ','prof312',3,'수3-4',30,25,'전공필수',0,'소프트웨어융합학과'),('MT01024-02','프로젝트종합설계Ⅰ','prof317',3,'수3-4',30,25,'전공필수',0,'소프트웨어융합학과'),('MT01024-03','프로젝트종합설계Ⅰ','prof321',3,'화5-6',30,25,'전공필수',0,'소프트웨어융합학과'),('MT01024-04','프로젝트종합설계Ⅰ','prof324',3,'수3-4',30,28,'전공필수',0,'소프트웨어융합학과'),('MT01030-01','소프트웨어융합진로탐색','prof321',1,'화7',100,100,'전공필수',0,'소프트웨어융합학과'),('MT01036-01','소프트웨어개론','prof313',3,'목3-4',50,37,'전공선택',0,'소프트웨어융합학과'),('MT01036-02','소프트웨어개론','prof313',3,'화3-4',60,60,'전공선택',0,'소프트웨어융합학과'),('MT01043-01','C++프로그래밍기초','prof321',3,'목5-6',15,13,'전공필수',0,'소프트웨어융합학과'),('MT01043-02','C++프로그래밍기초','prof321',3,'수5-6',15,15,'전공필수',1,'소프트웨어융합학과'),('MT01043-03','C++프로그래밍기초','prof323',3,'수3-4',15,14,'전공필수',0,'소프트웨어융합학과'),('MT01043-04','C++프로그래밍기초','prof323',3,'수5-6',15,15,'전공필수',0,'소프트웨어융합학과'),('MT01043-05','C++프로그래밍기초','prof322',3,'월3-4',15,12,'전공필수',0,'소프트웨어융합학과'),('MT01043-06','C++프로그래밍기초','prof322',3,'월5-6',15,13,'전공필수',0,'소프트웨어융합학과'),('MT01044-01','JAVA프로그래밍기초','prof311',3,'월1~2',20,17,'전공선택',0,'소프트웨어융합학과'),('MT01044-02','JAVA프로그래밍기초','prof311',3,'월3~4',20,18,'전공선택',0,'소프트웨어융합학과'),('MT01044-03','JAVA프로그래밍기초','prof318',3,'목1~2',20,20,'전공선택',0,'소프트웨어융합학과'),('MT01044-04','JAVA프로그래밍기초','prof318',3,'목3~4',20,19,'전공선택',0,'소프트웨어융합학과'),('MT01045-01','데이터베이스기초','prof312',3,'목3-4',30,29,'전공선택',0,'소프트웨어융합학과'),('MT01045-02','데이터베이스기초','prof312',3,'화3-4',30,30,'전공선택',0,'소프트웨어융합학과'),('MT01048-01','웹표준언어','prof314',3,'목1-2',40,40,'전공선택',0,'소프트웨어융합학과'),('MT01049-01','소프트웨어역량인증','prof317',0,'금7',100,97,'전공필수',0,'소프트웨어융합학과'),('MT01051-01','컴퓨터수학','prof314',3,'월3-4',100,88,'전공선택',0,'소프트웨어융합학과'),('MT01053-01','DIY종합설계프로그래밍','prof314',3,'월5-7',25,24,'전공선택',0,'소프트웨어융합학과'),('MT01056-01','소프트웨어개발실무영어Ⅱ','prof316',1,'화2',40,35,'전공선택',0,'소프트웨어융합학과'),('MT01056-02','소프트웨어개발실무영어Ⅱ','prof316',1,'목5',40,25,'전공선택',0,'소프트웨어융합학과'),('MT01058-01','기계학습','prof320',3,'화3-4',25,22,'전공선택',0,'소프트웨어융합학과'),('MT01058-02','기계학습','prof320',3,'목3-4',25,23,'전공선택',0,'소프트웨어융합학과'),('MT01058-03','기계학습','prof319',3,'수5-6',25,25,'전공선택',0,'소프트웨어융합학과'),('MT01058-04','기계학습','prof311',3,'목1-2',25,25,'전공선택',0,'소프트웨어융합학과'),('MT04005-01','빅데이터 분석','prof315',3,'수1-2',25,25,'전공선택',0,'소프트웨어융합학과');
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrollments`
--

DROP TABLE IF EXISTS `enrollments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollments` (
  `enrollmentID` int NOT NULL AUTO_INCREMENT,
  `studentID` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `courseID` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `enrollmentDate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`enrollmentID`),
  UNIQUE KEY `studentID` (`studentID`,`courseID`),
  KEY `courseID` (`courseID`),
  CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`studentID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`courseID`) REFERENCES `courses` (`courseID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollments`
--

LOCK TABLES `enrollments` WRITE;
/*!40000 ALTER TABLE `enrollments` DISABLE KEYS */;
INSERT INTO `enrollments` VALUES (66,'2022111053','MT01019-01','2025-06-17 14:03:49'),(67,'2022111053','MA03034-01','2025-06-17 14:06:30'),(68,'2022111053','MT01043-05','2025-06-17 14:06:38'),(69,'2022111053','MT01044-03','2025-06-17 14:06:52'),(71,'2022111053','MA03020-01','2025-06-17 14:07:26'),(76,'2022111053','MT01058-03','2025-06-17 14:28:28');
/*!40000 ALTER TABLE `enrollments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `graduationrequirements`
--

DROP TABLE IF EXISTS `graduationrequirements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `graduationrequirements` (
  `major` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `requiredMajorCredits` int NOT NULL,
  `requiredElectiveCredits` int NOT NULL,
  `requiredGeneralCredits` int NOT NULL,
  PRIMARY KEY (`major`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `graduationrequirements`
--

LOCK TABLES `graduationrequirements` WRITE;
/*!40000 ALTER TABLE `graduationrequirements` DISABLE KEYS */;
/*!40000 ALTER TABLE `graduationrequirements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `userID` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `userType` enum('Admin','Student','Professor') COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `major` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `contact` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('2022111053','password','Student','이영진','소프트웨어융합학과','lyj64302'),('admin','admin','Admin','관리자',NULL,NULL),('admin1','admin','Admin','관리자',NULL,NULL),('prof201','profpass','Professor','이영희','컴퓨터공학과','010-9876-5432'),('prof202','profpass','Professor','박철수','전자공학과','010-1111-2222'),('prof311','profpass','Professor','김현진','소프트웨어융합학과','010-1234-5678'),('prof312','profpass','Professor','박지숙','소프트웨어융합학과','010-2345-6789'),('prof313','profpass','Professor','방정호','소프트웨어융합학과','010-3456-7890'),('prof314','profpass','Professor','백종호','소프트웨어융합학과','010-4567-8901'),('prof315','profpass','Professor','손예진','소프트웨어융합학과','010-5678-9012'),('prof316','profpass','Professor','안미리','소프트웨어융합학과','010-6789-0123'),('prof317','profpass','Professor','엄성용','소프트웨어융합학과','010-7890-1234'),('prof318','profpass','Professor','윤홍수','소프트웨어융합학과','010-8901-2345'),('prof319','profpass','Professor','이민진','소프트웨어융합학과','010-9012-3456'),('prof320','profpass','Professor','이한상','소프트웨어융합학과','010-0123-4567'),('prof321','profpass','Professor','정민교','소프트웨어융합학과','010-1122-3344'),('prof322','profpass','Professor','정소영','소프트웨어융합학과','010-2233-4455'),('prof323','profpass','Professor','정주립','소프트웨어융합학과','010-3344-5566'),('prof324','profpass','Professor','홍헬렌','소프트웨어융합학과','010-4455-6677'),('prof411','profpass','Professor','민혜성','수학과','010-1111-2222'),('prof412','profpass','Professor','정선희','수학과','010-2222-3333'),('prof413','profpass','Professor','이동일','수학과','010-3333-4444'),('prof414','profpass','Professor','조성민','수학과','010-4444-5555'),('prof415','profpass','Professor','임미현','수학과','010-5555-6666'),('prof416','profpass','Professor','이영섭','수학과','010-6666-7777'),('std001','1234','Student','김철수','컴퓨터공학과','010-1234-5678'),('std002','stdpass','Student','이지영','전자공학과','010-2345-6789'),('std003','stdpass','Student','박민준','컴퓨터공학과','010-3456-7890'),('std004','stdpass','Student','김민지','컴퓨터공학과','010-4567-8901'),('std005','stdpass','Student','최현우','전자공학과','010-5678-9012'),('std006','stdpass','Student','이지은','컴퓨터공학과','010-6789-0123'),('studnet','password','Student','김진아','소프트웨어융합학과','lyj64302');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `waitlist`
--

DROP TABLE IF EXISTS `waitlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `waitlist` (
  `waitlist_id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `course_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `registered_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`waitlist_id`),
  KEY `student_id` (`student_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `waitlist_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `users` (`userID`),
  CONSTRAINT `waitlist_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`courseID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `waitlist`
--

LOCK TABLES `waitlist` WRITE;
/*!40000 ALTER TABLE `waitlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `waitlist` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-17 15:30:24
