DELETE FROM comments;
DELETE FROM users;
DELETE FROM categories;
DELETE FROM locations;
DELETE FROM events;

INSERT INTO users (name, email) VALUES ('user1', 'user1@mail.ru');
INSERT INTO users (name, email) VALUES ('user2', 'user2@mail.ru');

INSERT INTO categories (name) VALUES ('Лето');

INSERT INTO locations (lat, lon) VALUES ('45.65', '23.88');

INSERT INTO events (annotation, category_id, created_on, description, event_date, initiator_id, location_id, paid,
                    participant_limit, request_moderation, title)
VALUES ('Фестиваль под открытым небом', 1, '2020-01-09 12:00:00', 'Поп музыка',
        '2024-06-11 12:00:00', 2, 1, 'false', 0, 'true', 'event1');
INSERT INTO events (annotation, category_id, created_on, description, event_date, initiator_id, location_id, paid,
                    participant_limit, request_moderation, title)
VALUES ('Парк атракционов', 1, '2020-01-09 12:00:00', 'атракционы в парке горького',
        '2024-06-11 12:00:00', 2, 1, 'false', 0, 'true', 'event2');

UPDATE events SET state = 'PUBLISHED' WHERE id = 1;
