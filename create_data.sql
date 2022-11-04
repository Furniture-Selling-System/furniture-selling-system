INSERT INTO customer(name,address,phone)VALUES
('Blago Magdalena','50 Cox Way #1 Bolivar, Tennessee(TN), 38008','067-1215513'),
('Mór Bo','19 Brown Rd Corning, New York(NY), 14830','063-8342876'),
('Philomena Zaccharias','3790 Swoboda Rd Verona, Wisconsin(WI), 53593','067-1381218');

INSERT INTO material(name,quantity,minimum) VALUES
('ฉากอลูมิเนียม 2นิ้ว x 2นิ้ว',50,10),
('กระจกใส หนา 3 มิล',10,3),
('ล้อยางดำ 1/4 นิ้ว',2,4),
('ประตูบานเลื่อน',2,1),
('วงกบรับประตูบานเลื่อน',2,1),
('ชุดล้อรางแขวน',0,1),
('กุญแจล็อคประตูบานเลื่อน',10,5),
('แผ่นบังรางแขวน',0,1),
('กระจกใส หนา 5 มิล',3,3);

INSERT INTO furniture(name,sale) VALUES
('ตู้กระจกใส 3 มิล',2800),
('ประตูบานเลื่อนแขวน',8000);
('ตู้กระจกใส 5 มิล',3500)

INSERT INTO sale_order(fk_customer_id,c_name,c_address,cost_total) VALUES
(1,'Blago Magdalena','50 Cox Way #1 Bolivar, Tennessee(TN), 38008',2800),
(2,'Mór Bo','19 Brown Rd Corning, New York(NY), 14830',2800),
(3,'Philomena Zaccharias','3790 Swoboda Rd Verona, Wisconsin(WI), 53593',16000);

INSERT INTO sale_order_list(fk_sale_order_id,fk_furniture_id,quantity,cost_withholding) VALUES
(1,1,1,2800),
(1,2,1,4000),
(1,3,1,3500),
(2,1,1,2800),
(3,2,2,8000);

INSERT INTO bill_of_material(fk_furniture_id,fk_material_id,spend) VALUES
(1,1,8),
(1,2,4),
(1,3,4),
(2,4,1),
(2,5,1),
(2,6,1),
(2,7,1),
(2,8,1),
(3,1,8),
(3,9,4),
(3,3,4);
