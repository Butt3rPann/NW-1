USE itbms;

DROP VIEW IF EXISTS view_storageGb;

CREATE VIEW view_storageGb AS
SELECT DISTINCT storageGb FROM sale_item
ORDER BY storageGb;
