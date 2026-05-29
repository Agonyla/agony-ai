CREATE TABLE chat_message
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    session_id VARCHAR(100) NOT NULL,
    role       VARCHAR(20)  NOT NULL,
    content    TEXT         NOT NULL,
    tool_name  VARCHAR(100),
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON COLUMN chat_message.session_id IS '会话 ID（即 memoryId）';
COMMENT ON COLUMN chat_message.role IS 'SYSTEM/USER/AI/TOOL';
COMMENT ON COLUMN chat_message.content IS '消息内容';
COMMENT ON COLUMN chat_message.tool_name IS '工具名称（TOOL 消息时有值）';

CREATE INDEX idx_session_id
    ON chat_message (session_id);

CREATE INDEX idx_session_created
    ON chat_message (session_id, created_at);