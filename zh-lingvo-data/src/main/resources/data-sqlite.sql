INSERT INTO user (name)
SELECT 'admin'
WHERE NOT EXISTS (SELECT 1 FROM user WHERE name='admin');

INSERT INTO user (name)
SELECT 'Dmitriy'
WHERE NOT EXISTS (SELECT 1 FROM user WHERE name='Dmitriy');

INSERT INTO language (name, code)
SELECT 'English', 'En'
WHERE NOT EXISTS (SELECT 1 FROM language WHERE code='En');

INSERT INTO language (name, code)
SELECT 'Spanish', 'Es'
WHERE NOT EXISTS (SELECT 1 FROM language WHERE code='Es');

INSERT INTO language (name, code)
SELECT 'Russian', 'Ru'
WHERE NOT EXISTS (SELECT 1 FROM language WHERE code='Ru');

INSERT INTO part_of_speech (name)
SELECT 'NOUN'
WHERE NOT EXISTS (SELECT 1 FROM part_of_speech WHERE name='NOUN');

INSERT INTO part_of_speech (name)
SELECT 'VERB'
WHERE NOT EXISTS (SELECT 1 FROM part_of_speech WHERE name='VERB');

INSERT INTO part_of_speech (name)
SELECT 'ADJECTIVE'
WHERE NOT EXISTS (SELECT 1 FROM part_of_speech WHERE name='ADJECTIVE');