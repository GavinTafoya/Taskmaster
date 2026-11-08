-- Upsert Ticket Types
INSERT INTO ticket_types (id, name) VALUES (1, 'Bug') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;
INSERT INTO ticket_types (id, name) VALUES (2, 'Feature') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;
INSERT INTO ticket_types (id, name) VALUES (3, 'Task') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;

-- Upsert Statuses matching your exact list
INSERT INTO statuses (id, name) VALUES (0, 'Wall') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;
INSERT INTO statuses (id, name) VALUES (1, 'Backlog') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;
INSERT INTO statuses (id, name) VALUES (2, 'In progress') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;
INSERT INTO statuses (id, name) VALUES (3, 'In Test') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;
INSERT INTO statuses (id, name) VALUES (4, 'Approved') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;
INSERT INTO statuses (id, name) VALUES (5, 'Done') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;