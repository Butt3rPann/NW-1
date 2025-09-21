USE itbms;

INSERT INTO users (user_id, email, password, user_type, status) VALUES 
('1', 'itbkk.somchai@ad.sit.kmutt.ac.th', '$argon2id$v=19$m=16384,t=2,p=1$qlELd9cG5d5rS54mWYklKQ$Im1VP8QaOpey8yuWDrp7ldnkl8Ybnq+8TF7ugSZesec', 'BUYER', 'ACTIVE'),
('2', 'itbkk.somkiat@ad.sit.kmutt.ac.th', '$argon2id$v=19$m=16384,t=2,p=1$g6Nt+ZzUwqwpnJ+3KCdmhw$Sdkl6n5uoRYt+IePb/QxqVCHJunw3uTi8fMpgvzaw40', 'BUYER', 'ACTIVE'),
('3', 'itbkk.somsuan@ad.sit.kmutt.ac.th', '$argon2id$v=19$m=16384,t=2,p=1$WIBiwmnLR9yEj0unaY/aHQ$NIqvyzqgtGXEY+0kQ9okLNs2AbsP//w/7yY68C5/tJ0', 'SELLER', 'ACTIVE'),
('4', 'itbkk.somsuk@ad.sit.kmutt.ac.th', '$argon2id$v=19$m=16384,t=2,p=1$xLtcr/ou6wFvPlfc45sQyg$y4NmsmvG+XoMSt23zsC1rGApQlt7LwYMuUjGFoGZT3Q', 'SELLER', 'ACTIVE'),
('5', 'itbkk.somsak@ad.sit.kmutt.ac.th', '$argon2id$v=19$m=16384,t=2,p=1$Ta0wVuU6rIlMLoe5Nw/vbg$cKhkVmuREm196rNQJxa3nHGwS83It5omM/dzMBx1s/A', 'SELLER', 'ACTIVE');

INSERT INTO sellers (user_id, nick_name, full_name, phone_number, bank_account, bank_name, id_card_number) VALUES
('3', 'Somsuan', 'Somsuan Hundee', '834567890', '371234567', 'Bankok Bank', '1000111100222'),
('4', 'Somsuk', 'Somsuk  Fundee', '845678901', '2371234567', 'Saim Commercial Bank', '1000111100333'),
('5', 'Somsak', 'Soksak  Saksit', '856789012', '373456789', 'Bankok Bank', '1000111100444');

INSERT INTO buyers (user_id, nick_name, full_name) VALUES
('1', 'Somchai', 'Somchai Jaidee'),
('2', 'Somkiat', 'Somkiat  Luckchart');