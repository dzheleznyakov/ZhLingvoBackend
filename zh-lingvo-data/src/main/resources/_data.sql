INSERT INTO user (name)
SELECT 'admin'
WHERE NOT EXISTS (SELECT 1 FROM user WHERE name='admin');