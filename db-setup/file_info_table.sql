-- Create file_info table for PostgreSQL
CREATE TABLE IF NOT EXISTS file_info (
    id BIGSERIAL PRIMARY KEY,
    original_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL UNIQUE,
    content_type VARCHAR(100),
    size BIGINT,
    upload_date TIMESTAMP NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES my_user(id)
);

-- Index for faster lookup
CREATE INDEX idx_file_info_user_id ON file_info(user_id);
CREATE INDEX idx_file_info_upload_date ON file_info(upload_date);
