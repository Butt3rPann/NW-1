USE itbms;

INSERT INTO orders (order_id, user_id, order_date, shipping_address, order_note, order_status) VALUES
(1, 1, '2025-09-27 10:00:00', '123 ถนนสุขสวัสดิ์, กรุงเทพฯ', 'ส่งด่วน', 'COMPLETED'),
(2, 1, '2025-09-28 11:00:00', '123 ถนนสุขสวัสดิ์, กรุงเทพฯ', 'ห้ามช้า', 'COMPLETED'),
(3, 1, '2025-09-29 12:30:00', '123 ถนนสุขสวัสดิ์, กรุงเทพฯ', NULL, 'COMPLETED'),
(4, 2, '2025-09-26 09:00:00', '456 ถนนพหลโยธิน, กรุงเทพฯ', 'ส่งก่อนเที่ยง', 'COMPLETED'),
(5, 2, '2025-09-28 16:00:00', '456 ถนนพหลโยธิน, กรุงเทพฯ', NULL, 'COMPLETED'),
(6, 3, '2025-09-27 14:00:00', '789 ถนนพระราม 4, กรุงเทพฯ', 'ตรวจสอบสินค้า', 'COMPLETED'),
(7, 4, '2025-09-27 15:00:00', '789 ถนนพระราม 4, กรุงเทพฯ', 'ขอเวลารับของหลังบ่ายโมง', 'COMPLETED'),
(8, 5, '2025-09-26 18:00:00', '321 ถนนสีลม, กรุงเทพฯ', NULL, 'COMPLETED'),
(9, 5, '2025-09-29 20:00:00', '321 ถนนสีลม, กรุงเทพฯ', 'ของแถมด้วยนะ', 'COMPLETED');

INSERT INTO order_items (order_id, sale_item_id, price, quantity, description) VALUES
(1, 2, 29700, 1, 'iPhone 14 - Midnight'),
(1, 3, 33000, 1, 'iPhone 13 Pro - Sierra Blue'),
(2, 8, 29700, 1, 'iPhone 14 Plus - Blue'),
(2, 9, 19800, 1, 'iPhone 13 mini - Green'),
(3, 16, 39600, 1, 'Galaxy S23 Ultra - Black'),
(3, 24, 16500, 1, 'Galaxy A73 5G - Gray'),
(4, 1, 42900, 1, 'iPhone 14 Pro Max - Space Black'),
(4, 37, 5940, 2, 'Redmi 12C - Ocean Blue'),
(5, 24, 16500, 1, 'Galaxy A73 5G - Gray'),
(5, 8, 29700, 1, 'iPhone 14 Plus - Blue'),
(6, 8, 29700, 1, 'iPhone 14 Plus - Blue'),
(6, 10, 16500, 1, 'iPhone 12 mini - Red'),
(7, 1, 42900, 1, 'iPhone 14 Pro Max - Space Black'),
(7, 39, 7590, 2, 'POCO M5 - Power Black'),
(8, 3, 33000, 1, 'iPhone 13 Pro - Sierra Blue'),
(8, 8, 29700, 1, 'iPhone 14 Plus - Blue'),
(9, 24, 16500, 1, 'Galaxy A73 5G - Gray'),
(9, 10, 16500, 1, 'iPhone 12 mini - Red');
