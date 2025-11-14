-- Members 더미 데이터
INSERT IGNORE INTO members (username, password, nickname, department, role, created_at, updated_at, deleted_at)
VALUES
    ('user0', 'password', 'John', 'IT', 'USER', NOW(), NOW(), NULL),
    ('user1', 'password', 'Jane', 'HR', 'ADMIN', NOW(), NOW(), NULL),
    ('user2', 'password', 'Alice', 'CONSULTING', 'USER', NOW(), NOW(), NULL),
    ('user3', 'password', 'Bob', 'FINANCE', 'USER', NOW(), NOW(), NULL),
    ('user4', 'password', 'Charlie', 'AUDIT', 'USER', NOW(), NOW(), NULL);

-- Tags 더미 데이터
INSERT IGNORE INTO tags (id, member_id, name, created_at, updated_at, deleted_at)
VALUES
    (1, 1, 'Urgent', NOW(), NOW(), NULL),
    (2, 1, 'Work', NOW(), NOW(), NULL),
    (3, 1, 'Personal', NOW(), NOW(), NULL),
    (4, 2, 'Meeting', NOW(), NOW(), NULL),
    (5, 2, 'Project', NOW(), NOW(), NULL),
    (6, 3, 'Study', NOW(), NOW(), NULL),
    (7, 3, 'Health', NOW(), NOW(), NULL),
    (8, 4, 'Finance', NOW(), NOW(), NULL);

-- Todos 더미 데이터
INSERT IGNORE INTO todos (id, member_id, title, content, is_completed, due_date, visibility, priority, created_at, updated_at, deleted_at)
VALUES
    (1, 1, '프로젝트 문서 작성', '프로젝트 개발 문서 작성 및 검토', false, DATE_ADD(NOW(), INTERVAL 7 DAY), 'PUBLIC', 1.0, NOW(), NOW(), NULL),
    (2, 1, '코드 리뷰', 'PR #123 코드 리뷰 진행', false, DATE_ADD(NOW(), INTERVAL 2 DAY), 'PUBLIC', 2.0, NOW(), NOW(), NULL),
    (3, 1, '데이터베이스 백업', '주간 데이터베이스 백업 수행', true, DATE_SUB(NOW(), INTERVAL 1 DAY), 'PRIVATE', 3.0, NOW(), NOW(), NULL),
    (4, 2, '팀 회의 준비', '월간 팀 회의 안건 준비', false, DATE_ADD(NOW(), INTERVAL 3 DAY), 'PUBLIC', 1.5, NOW(), NOW(), NULL),
    (5, 2, '신입사원 교육', '신입사원 온보딩 교육 자료 준비', false, DATE_ADD(NOW(), INTERVAL 10 DAY), 'PUBLIC', 2.5, NOW(), NOW(), NULL),
    (6, 3, 'Java 스터디', 'Effective Java 3장 학습', false, DATE_ADD(NOW(), INTERVAL 5 DAY), 'PRIVATE', 1.0, NOW(), NOW(), NULL),
    (7, 3, '클라이언트 미팅', '클라이언트 요구사항 정리', true, DATE_SUB(NOW(), INTERVAL 2 DAY), 'PUBLIC', 4.0, NOW(), NOW(), NULL),
    (8, 4, '예산 보고서 작성', 'Q4 예산 보고서 작성', false, DATE_ADD(NOW(), INTERVAL 14 DAY), 'PUBLIC', 3.0, NOW(), NOW(), NULL),
    (9, 4, '비용 정산', '출장비 정산 처리', false, DATE_ADD(NOW(), INTERVAL 1 DAY), 'PRIVATE', 5.0, NOW(), NOW(), NULL),
    (10, 5, '감사 보고서 검토', '내부 감사 보고서 최종 검토', false, DATE_ADD(NOW(), INTERVAL 4 DAY), 'PRIVATE', 2.0, NOW(), NOW(), NULL);

-- TodoTags 더미 데이터
INSERT IGNORE INTO todo_tags (id, todo_id, tag_id)
VALUES
    (1, 1, 2),   -- 프로젝트 문서 작성 - Work
    (2, 1, 5),   -- 프로젝트 문서 작성 - Project
    (3, 2, 1),   -- 코드 리뷰 - Urgent
    (4, 2, 2),   -- 코드 리뷰 - Work
    (5, 3, 2),   -- 데이터베이스 백업 - Work
    (6, 4, 4),   -- 팀 회의 준비 - Meeting
    (7, 5, 4),   -- 신입사원 교육 - Meeting
    (8, 6, 6),   -- Java 스터디 - Study
    (9, 6, 3),   -- Java 스터디 - Personal
    (10, 7, 5),  -- 클라이언트 미팅 - Project
    (11, 7, 4),  -- 클라이언트 미팅 - Meeting
    (12, 8, 8),  -- 예산 보고서 작성 - Finance
    (13, 9, 8),  -- 비용 정산 - Finance
    (14, 9, 1);  -- 비용 정산 - Urgent