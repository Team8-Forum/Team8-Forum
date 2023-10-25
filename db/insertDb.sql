INSERT INTO users (first_name, last_name, username, password, email, is_admin, is_blocked)
VALUES
    ('David', 'Stoyanov', 'deimeifei', 'deimeifei1', 'deimeifei@abv.bg', 1, 0),
    ('Alice', 'Johnson', 'alicej', 'pass123', 'alice.j@email.com', 0, 0),
    ('Bob', 'Brown', 'bobbrown', 'bobpassword', 'bob@email.com', 1, 0),
    ('Eve', 'Williams', 'evew', 'evepass', 'eve@email.com', 0, 1);

INSERT INTO phone_numbers (user_id, phone_number)
VALUES
    (1, '0896072925'),
    (3, '5551234567');


INSERT INTO posts (user_id, title, content)
VALUES
    (1, 'Game Freaks', 'Lords Of The Fallen just dropped'),
    (2, 'Global Warming', 'Do you feel the days getting hotter?'),
    (3, 'Thoughts on Technology', 'Discussing the latest tech trends.'),
    (4, 'Travel Adventures', 'Sharing my recent travel experiences.');

INSERT INTO comments (comment, post_id, user_id)
VALUES
    ('I enjoyed reading your post.', 3, 1),
    ('Just downloaded it. It feels rushed', 1, 4),
    ('Can you share more about your travels?', 3, 4);