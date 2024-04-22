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

INSERT INTO language (name, code)
SELECT 'Portuguese', 'Pt'
WHERE NOT EXISTS (SELECT 1 FROM language WHERE code='Pt');