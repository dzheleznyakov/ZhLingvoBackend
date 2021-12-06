CREATE TABLE IF NOT EXISTS user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS language (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR NOT NULL,
    code CHAR(2) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS dictionary (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    lang_id INTEGER,
    name VARCHAR NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (lang_id) REFERENCES language(id)
);

CREATE TABLE IF NOT EXISTS word (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    dic_id INTEGER,
    main_form VARCHAR NOT NULL,
    transcription VARCHAR,
    irreg_type VARCHAR,
    FOREIGN KEY (dic_id) REFERENCES dictionary(id)
);
CREATE INDEX IF NOT EXISTS word_dic_index ON word (dic_id);

CREATE TABLE IF NOT EXISTS semantic_block (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    word_id INTEGER,
    pos VARCHAR(6) NOT NULL,
    gender VARCHAR(10),
    FOREIGN KEY (word_id) REFERENCES word(id)
);
CREATE INDEX IF NOT EXISTS semantic_block_word_index on semantic_block (word_id);

CREATE TABLE IF NOT EXISTS meaning (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sem_bl_id INTEGER,
    remark VARCHAR(20),
    FOREIGN KEY (sem_bl_id) REFERENCES semantic_block(id)
);
CREATE INDEX IF NOT EXISTS meaning_sem_block_index ON meaning (sem_bl_id);

CREATE TABLE IF NOT EXISTS 'translation' (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    meaning_id INTEGER,
    value TEXT NOT NULL,
    elaboration TEXT,
    FOREIGN KEY (meaning_id) REFERENCES meaning(id)
);
CREATE INDEX IF NOT EXISTS translation_meaning_index ON 'translation' (meaning_id);

CREATE TABLE IF NOT EXISTS example (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    meaning_id INTEGER,
    remark VARCHAR(20),
    expression TEXT,
    explanation TEXT,
    FOREIGN KEY (meaning_id) REFERENCES meaning(id)
);
CREATE INDEX IF NOT EXISTS example_meaning_index ON example (meaning_id);
