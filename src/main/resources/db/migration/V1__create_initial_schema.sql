-- Table: draws
CREATE TABLE draws (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       code VARCHAR(10) UNIQUE NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       description TEXT,
                       draw_date DATETIME,
                       budget_limit DECIMAL(10,2),
                       status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                       INDEX idx_code (code),
                       INDEX idx_status (status),
                       INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: participants
CREATE TABLE participants (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              draw_id BIGINT NOT NULL,
                              name VARCHAR(255) NOT NULL,
                              email VARCHAR(255) NOT NULL,
                              phone VARCHAR(50),
                              assigned_to_id BIGINT,
                              notification_sent BOOLEAN DEFAULT FALSE,
                              notification_sent_at TIMESTAMP NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                              FOREIGN KEY (draw_id) REFERENCES draws(id) ON DELETE CASCADE,
                              FOREIGN KEY (assigned_to_id) REFERENCES participants(id) ON DELETE SET NULL,

                              INDEX idx_draw_id (draw_id),
                              INDEX idx_email (email),
                              UNIQUE KEY unique_participant_per_draw (draw_id, email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;