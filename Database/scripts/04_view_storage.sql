USE itbms;

DROP VIEW IF EXISTS view_storageGb;

CREATE VIEW view_storageGb AS
SELECT DISTINCT storage_gb FROM sale_items
ORDER BY storage_gb;
