-- 1. 스키마(데이터베이스) 생성
CREATE DATABASE IF NOT EXISTS smart_convenience_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. 사용할 DB 선택
USE smart_convenience_store;

-- 3. 사용자 테이블
CREATE TABLE user (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    pass VARCHAR(255) NOT NULL,
    point INT DEFAULT 0,
    payment INT DEFAULT 0
);

-- 4. 상품 테이블
CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    category VARCHAR(50),
    img VARCHAR(255)
);

-- 5. 상품 댓글 테이블
CREATE TABLE product_comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50),
    user_name VARCHAR(100),
    product_id INT,
    rating FLOAT CHECK (rating BETWEEN 0 AND 5),
    comment TEXT,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

-- 6. 매장 테이블
CREATE TABLE store (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    latitude FLOAT,
    longitude FLOAT
);

-- 7. 매장 댓글 테이블
CREATE TABLE store_comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50),
    user_name VARCHAR(100),
    store_id INT,
    rating FLOAT CHECK (rating BETWEEN 0 AND 5),
    comment TEXT,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (store_id) REFERENCES store(id)
);

-- 8. 포인트 이력 테이블
CREATE TABLE point (
    id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50),
    order_id VARCHAR(50),
    point INT,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 9. 주문 테이블
CREATE TABLE `orders` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50),
    store_name VARCHAR(100),
    order_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 10. 주문 상세 테이블
CREATE TABLE order_detail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT,
    unit_price INT,
    img VARCHAR(255),
    product_name VARCHAR(100),
    FOREIGN KEY (order_id) REFERENCES `orders`(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

	
-- 11. 출석 테이블
CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_attendance (user_id, date),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

INSERT INTO user (id, name, pass, point, payment) VALUES
('id1', '이유민', 'pass1', 100, 0),
('id2', '반충기', 'pass2', 100, 0),
('id3', '홍길동', 'pass3', 100, 0),
('id4', '김코딩', 'pass4', 100, 0),
('id5', '박리뷰', 'pass5', 100, 0),
('id6', '최유저', 'pass6', 100, 0),
('id7', '송새싹', 'pass7', 100, 0),
('id8', '이인턴', 'pass8', 100, 0),
('id9', '정리뷰', 'pass9', 100, 0),
('id10', '김현장', 'pass10', 100, 0);
-- 간편식
INSERT INTO product (name, price, category, img) VALUES
('참치마요 삼각김밥', 1200, '간편식', 'triangle_kimbap'),
('불고기 도시락', 4500, '간편식', 'bulgogi_lunchbox'),
('치킨마요 덮밥', 4200, '간편식', 'chicken_mayo'),
('햄치즈 샌드위치', 2500, '간편식', 'ham_cheese_sandwich');

-- 라면
INSERT INTO product (name, price, category, img) VALUES
('진라면 매운맛', 1000, '라면', 'jin_ramen_spicy'),
('너구리', 1100, '라면', 'neoguri_ramen'),
('삼양라면 오리지널', 950, '라면', 'samyang_ramen'),
('불닭볶음면', 1300, '라면', 'buldak_ramen');

-- 음료
INSERT INTO product (name, price, category, img) VALUES
('코카콜라 500ml', 1500, '음료', 'cola_500'),
('비타500', 800, '음료', 'vita500'),
('칠성사이다', 1400, '음료', 'chilsung_cider'),
('빙그레 바나나', 1200, '음료', 'banana_bar'),
('탐스 제로 아이스티', 1600, '음료', 'zero_icedtea');

-- 과자
INSERT INTO product (name, price, category, img) VALUES
('초코파이', 2000, '과자', 'choco_pie'),
('오징어 땅콩', 1800, '과자', 'squid_peanut'),
('포카칩 오리지널', 2200, '과자', 'pocachip_original'),
('칸쵸', 1200, '과자', 'kancho');

-- 아이스크림
INSERT INTO product (name, price, category, img) VALUES
('월드콘 초코', 1800, '아이스크림', 'worldcone_choco'),
('빠삐코 초코', 1300, '아이스크림', 'papico_choco'),
('투게더 바닐라', 2500, '아이스크림', 'together_cup');

-- 주류
INSERT INTO product (name, price, category, img) VALUES
('카스 맥주 500ml', 2500, '주류', 'cass_beer'),
('새로 500ml', 2400, '주류', 'sero_beer'),
('매화수', 2200, '주류', 'maehwasu');

-- 기타
INSERT INTO product (name, price, category, img) VALUES
('물티슈 10매', 1000, '기타', 'wet_tissue'),
('칫솔 1개입', 1500, '기타', 'toothbrush'),
('면도기 1회용', 1200, '기타', 'razor');


INSERT INTO product_comment (user_id, user_name, product_id, rating, comment) VALUES
('id1', '이유민', 1, 4.5, '맛있고 간편해요!'),
('id2', '반충기', 2, 4.0, '뜨끈하게 잘 먹었습니다.'),
('id3', '홍길동', 3, 3.5, '조금 달아요.');

INSERT INTO store (name, latitude, longitude) VALUES
('GS25 구미3공단점', 36.1042, 128.4179),
('CU 구미전자점', 36.1038, 128.4185),
('이마트24 구미본점', 36.1035, 128.4191),
('세븐일레븐 구미공단점', 36.1048, 128.4182),
('CU 구미IT의료점', 36.1051, 128.4176),
('GS25 구미전자공단점', 36.1057, 128.4189),
('CU 구미3공단로점', 36.1040, 128.4165),
('이마트24 구미3단지점', 36.1032, 128.4169),
('GS더프레시 구미점', 36.1085, 128.4200),
('홈플러스 구미점', 36.1134, 128.4195),
('롯데마트 구미점', 36.1172, 128.4208),
('GS25 인동대림점', 36.1128, 128.4265),
('CU 구미인동점', 36.1110, 128.4254),
('이마트24 인동점', 36.1105, 128.4243),
('GS25 구미인동점', 36.1130, 128.4239),
('세븐일레븐 인동점', 36.1100, 128.4230),
('CU 구미송정점', 36.1087, 128.4132),
('이마트24 구미송정점', 36.1083, 128.4140),
('GS25 구미한성점', 36.1091, 128.4168),
('CU 구미한성점', 36.1095, 128.4173);


INSERT INTO store_comment (user_id, user_name, store_id, rating, comment) VALUES
('id1', '이유민', 1, 5.0, '직원분이 친절해요!'),
('id2', '반충기', 2, 4.2, '매장이 깔끔해요.'),
('id3', '홍길동', 1, 4.5, '음료 종류가 다양해요.'),
('id4', '김코딩', 1, 3.5, '밤에는 사람이 없어서 편해요.'),
('id5', '박리뷰', 3, 2.0, '담배 냄새가 좀 나요.'),
('id6', '최유저', 5, 4.8, '주차장이 넓어서 좋았어요.'),
('id7', '송새싹', 4, 4.0, '편하게 들를 수 있어요.'),
('id8', '이인턴', 2, 5.0, '편의점인데 카페 같아요.'),
('id9', '정리뷰', 3, 3.2, '일반적인 편의점이에요.'),
('id10', '김현장', 6, 4.6, '편리한 위치에 있어요.');


INSERT INTO point (id, user_id, order_id, point) VALUES
('p001', 'id1', 'o001', 10),
('p002', 'id2', 'o002', 5);


INSERT INTO orders (user_id, store_name, order_time) VALUES
('id1', '울산대점', '2025-05-25 14:30:00'),
('id2', '서울캠퍼스점', '2025-05-25 15:00:00');

INSERT INTO order_detail (order_id, product_id, quantity, unit_price, img, product_name) VALUES
(1, 1, 2, 1200, 'triangle_kimbap', '삼각김밥 참치마요'),
(1, 3, 1, 1500, 'cola_500', '콜라 500ml'),
(2, 2, 1, 1000, 'jin_ramen', '컵라면 진라면');


INSERT INTO attendance (user_id, date) VALUES
('id1', '2025-05-25'),
('id2', '2025-05-25');