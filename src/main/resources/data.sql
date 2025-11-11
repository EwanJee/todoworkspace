INSERT IGNORE INTO members (username, password, nickname, role, created_at, updated_at, deleted_at)
VALUES
    ('user0', 'password', 'John', 'USER', NOW(), NOW(), NULL),
    ('user1', 'password', 'Jane', 'ADMIN', NOW(), NOW(), NULL),
    ('user2', 'password', 'Alice', 'USER', NOW(), NOW(), NULL);