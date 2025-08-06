USE itbms;

CREATE OR REPLACE VIEW view_storageGb AS
SELECT DISTINCT storageGb FROM sale_item
WHERE storageGb IS NOT NULL
ORDER BY storageGb;