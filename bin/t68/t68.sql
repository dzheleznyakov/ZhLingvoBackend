CREATE TABLE IF NOT EXISTS quiz_settings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    quiz_regime VARCHAR(10),
    max_score INTEGER NOT NULL,
    matching_regime VARCHAR(8)
);

CREATE TABLE IF NOT EXISTS quiz (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    target_lang_id INTEGER NOT NULL,
    settings_id INTEGER NOT NULL,
    name VARCHAR NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (target_lang_id) REFERENCES language(id),
    FOREIGN KEY (settings_id) REFERENCES quiz_settings(id)
);

CREATE TABLE IF NOT EXISTS quiz_record (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    quiz_id INTEGER NOT NULL,
    word_main_form VARCHAR NOT NULL,
    pos VARCHAR(6),
    transcription VARCHAR,
    current_score FLOAT,
    number_of_runs INTEGER,
    number_of_successes INTEGER,
    FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);

CREATE TABLE IF NOT EXISTS quiz_translation (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    record_id INTEGER NOT NULL,
    value TEXT NOT NULL,
    elaboration TEXT,
    FOREIGN KEY (record_id) REFERENCES quiz_record(id)
);

CREATE TABLE IF NOT EXISTS quiz_example (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    record_id INTEGER NOT NULL,
    remark VARCHAR(20),
    expression TEXT,
    explanation TEXT,
    FOREIGN KEY (record_id) REFERENCES quiz_record(id)
);