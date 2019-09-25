DELETE FROM user;

INSERT INTO user (id, name, username, password) VALUES
(1, 'user-admin', 'admin', '{noop}admin1234'),
(2, 'user-001', 'user001', '{noop}user111'),
(3, 'user-002', 'user002', '{noop}user222'),
(4, 'user-003', 'user003', '{noop}user333'),
(5, 'user-004', 'user004', '{noop}user444');
