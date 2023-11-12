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
    (4, 'Cyberpunk Update', 'I think it fixes a lot of issues'),
    (3, 'New patch in league of legends', 'Nilah got nerfed sadly'),
    (1, 'Cs2 release', 'CS2 just released im so hyped'),
    (4, 'GTA V', 'I think we will never get GTA V'),
    (2, 'Ark Survival Ascended', 'The game optimisation is bad i cant get more than 40fps'),
    (4, 'GTA V Trailer', 'End of November or early December we will get the trailer. I cant believe it'),
    (1, 'Is ASE still viable?', 'After the release of ASA im not sure if ASE will still have players');

INSERT INTO comments (comment, post_id, user_id)
VALUES
    ('I enjoyed reading your post.', 3, 1),
    ('Just downloaded it. It feels rushed', 1, 4),
    ('Can you share more about your travels?', 3, 4),
    ('Lords Of The Fallen is a great game!', 1, 2),
    ('I agree, the heat is getting unbearable.', 2, 1),
    ('What are your thoughts on the latest iPhone?', 3, 2),
    ('Cyberpunk Update is awesome!', 4, 3),
    ('I cant wait for the new patch in League of Legends!', 5, 1),
    ('Im so excited for CS2 release!', 6, 2),
    ('GTA V is a legendary game!', 7, 3),
    ('I hope they release GTA V soon!', 7, 1),
    ('Ark Survival Ascended is a challenging game.', 8, 1),
    ('GTA V Trailer sounds promising!', 9, 3),
    ('Im curious about the future of ASE.', 10, 2);


INSERT INTO posts_likes (user_id, post_id) VALUES
                                               (1, 1),
                                               (2, 1),
                                               (3, 2),
                                               (4, 3),
                                               (2, 4),
                                               (3, 5),
                                               (1, 6),
                                               (1, 7),
                                               (3, 8),
                                               (4, 9),
                                               (2, 10);