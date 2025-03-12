INSERT INTO `user` (`providerType`, `type`, `createdAt`, `updatedAt`, `userId`, `phone`,`name`,`email`,`upw`,`pic`) values(0, 100, '2025-03-11 11:34:22', '2025-03-11 11:34:22', 55, '0101234567','홍길동','sss@naver.com','$2a$10$HBkPm4ZoKpRtB9IP.LfJduWZGyf967N32/XDbouxHvjcZjQTMo87a', 'img.jpg'),(1, 100, '2025-03-11 11:34:22', '2025-03-11 11:34:22', 1, '01015379264','카카오','kakao@naver.com','aabbcc', 'img.jpg'),(0, 130, '2025-03-11 11:34:22', '2025-03-11 11:34:22', 6, '01098765432','사장1','test6@gmail.com','$2a$10$rZ4CMz0jvtXL0OIph53fSOUTe6t3nyNoDwsUB3vQ4sPQAyBMifwGq', 'img.jpg');
INSERT INTO `category` (`categoryId`, `categoryName`,`createdAt`,`updatedAt`) VALUES (1,'청소','2025-03-11 11:34:22', '2025-03-11 11:34:22');
INSERT INTO `detail_type` (`detailTypeId`, `categoryId`, `detailTypeName`) VALUES (1,1,'집청소');
INSERT INTO `business` (`businessId`,`userId`, `detailTypeId`, `businessNum`, `businessName`, `title`,`contents`,`logo`,`address`,`busiCreatedAt`,`openingTime`,`closingTime`,`paper`,`tel`,`state`,`safeTel`,`lat`,`lng`,`createdAt`,`updatedAt`,`rejectContents`,`approveAt`) values(1, 6, 1, '5148202296','청소다움', '안녕하세요 청소다움입니다.', '내용','3096610a-eb9c-4b13-b9c0-9dc25d6631d9.jpg', '대구광역시 중구 동인동4가', '2018-02-15','11:04:00','22:00:00', '136f3a92-0943-479a-9cdf-366bc7985246.jpg', '0537891234',101,'0540000888',34.867122,124.544867,'2025-02-25 09:12:40', '2025-03-10 14:50:45', '승인됨', '2025-03-04');
INSERT INTO `admin` (`adminId`, `name`,`phone`,`createdAt`, `updatedAt`,`aId`,`aPw`,`type`) VALUES (1,'관리자','01052519935','2025-03-11 11:34:22', '2025-03-11 11:34:22','1234@gmail.com','$2a$10$cTulSxxmssAaDPNSg43HoOdQ16NZK8RLbnCPkebyQ/6ZDQSoNZ4WK',101);
INSERT INTO `room` (`roomId`,`userId`,`businessId`,`state`,`createdAt`) VALUES (1,1,1,'00201','2025-02-15 12:08:11');
INSERT INTO `chat` (`chatId`, `roomId`,`contents`,`createdAt`,`flag`) VALUES (1,1,'뭐요','2025-03-04 17:12:55',0);
INSERT INTO `product` (`productId`,`businessId`,`detailTypeId`,`price`) VALUES (1,1,1,150000);
INSERT INTO `option` (`optionId`,`productId`,`name`) VALUES (1,1,'방 개수'),(2,1,'화장실'),(3,1,'부엌');
INSERT INTO `option_detail`(`optionDetailId`,`optionId`,`name`,`price`,`contents`) VALUES (1,1,'1개',0,'기본제공'),(2,1,'2개',80000,null),(3,2,'안해도되요',0,null),(4,2,'해주세요',50000,null), (5,3,'안해도되요',0,null),(6,3,'해주세요',100000,null);
INSERT INTO `service` (`serviceId`,`userId`,`productId`,`paidAt`,`price`,`completed`,`address`,`comment`,`createdAt`, `updatedAt`,`addComment`,`pyeong`,`tid`,`lat`,`lng`,`doneAt`,`totalPrice`) VALUES (1,1,1,null,170000,0,'서울 광진구 긴고랑로 1','테스트~','2025-03-11 11:34:22', '2025-03-11 11:34:22','테스트용이라고',10,null,23,137,null,0), (2,1,1,'2025-03-10 16:00:12',170000,8,'서울 광진구 긴고랑로 5','리뷰/환불 테스트용~','2025-03-11 11:34:22', '2025-03-11 11:34:22','테스트용이라고',10,null,23,137,'2025-03-12 16:00:12',180000);
INSERT INTO `service_option`(`serviceOptionId`, `serviceId`,`optionDetailId`,`comment`,`price`,`createdAt`,`updatedAt`) VALUES (1,1,1,'옵션테스트',null,'2025-03-11 11:34:22', '2025-03-11 11:34:22'),(2,1,3,'옵션테스트2',null,'2025-03-11 11:34:22', '2025-03-11 11:34:22'),(3,1,6,'옵션테스트3',null,'2025-03-11 11:34:22', '2025-03-11 11:34:22'), (7,2,1,'옵션테스트',null,'2025-03-11 11:34:22', '2025-03-11 11:34:22'),(8,2,3,'옵션테스트2',null,'2025-03-11 11:34:22', '2025-03-11 11:34:22'),(9,2,6,'옵션테스트3',null,'2025-03-11 11:34:22', '2025-03-11 11:34:22');
INSERT INTO `service_detail`(`detailId`,`serviceId`,`startDate`,`endDate`,`mStartTime`,`mEndTime`,`sTime`,`eTime`,`allow`,`createdAt`, `updatedAt`) VALUES (1,1,'2025-03-11','2025-03-11','09:00:00','17:00:00',null,null,0,'2025-03-01 11:34:22', '2025-03-9 11:34:22'), (2,2,'2025-03-11','2025-03-11','09:00:00','17:00:00',null,null,0,'2025-03-01 11:34:22', '2025-03-9 11:34:22');
INSERT INTO `review` (`reviewId`,`serviceId`,`contents`,`score`,`createdAt`,`updatedAt`) VALUES (1,2,'리뷰테스트',4.8,'2025-03-11 11:34:22', '2025-03-11 11:34:22');
INSERT INTO `review_pic`(`reviewPicId`,`reviewId`,`pic`,`state`) VALUES (1,1,'img.jpg',0);
INSERT INTO `comment` (`commentId`,`reviewId`,`userId`,`contents`,`createdAt`,`updatedAt`) VALUES (1,1,6, '대충 답변','2025-03-11 11:34:22', '2025-03-11 11:34:22');
INSERT INTO `qa_type` (`qaTypeId`,`type`) VALUES (1,'업체 신고');
INSERT INTO `qa_type_detail`(`qaTypeDetailId`,`reason`,`qaTypeId`) VALUES (1,'기타',1);
INSERT INTO `qa`(`qaId`,`createdAt`,`contents`,`qaState`,`qaTypeDetailId`,`userId`,`qaTargetId`,`reportReasonId`,`title`) VALUES (1,'2025-03-12 09:12:11','업체가 너무 꼽다','00101',1,55,1,1,'이 업체 이게 맞나요?');
INSERT INTO `region` (`regionId`,`region`,`createdAt`,`updatedAt`) VALUES (1,'대구','2025-03-11 11:34:22', '2025-03-11 11:34:22');

