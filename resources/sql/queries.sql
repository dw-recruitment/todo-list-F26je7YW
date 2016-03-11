-- :name add-todo! :! :n
-- :doc creates a new todo record
INSERT INTO todos
(name, description)
VALUES (:name, :description)

-- :name update-todo! :! :n
-- :doc update an existing todo record
UPDATE todos
SET checked = :checked
WHERE id = :id

-- :name get-todos :? :*
-- :doc retrieve a todo given the id.
SELECT * FROM todos
ORDER BY date_created

-- :name delete-todo! :! :n
-- :doc delete a todo given the id
DELETE FROM todos
WHERE id = :id
