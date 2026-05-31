

USE ERRONKA3;

DROP PROCEDURE IF EXISTS sp_historikoa_guztiak;
DROP PROCEDURE IF EXISTS sp_historikoa_ekintza_bila;
DROP PROCEDURE IF EXISTS sp_historikoa_taula_bila;
DROP FUNCTION  IF EXISTS fn_historikoa_kopurua;
DROP FUNCTION  IF EXISTS fn_historikoa_ekintza_kopurua;

DELIMITER //

CREATE PROCEDURE sp_historikoa_guztiak()
BEGIN
    SELECT id_hist, taula, ekintza, data_aldaketa, oharra
    FROM   historikoa
    ORDER  BY data_aldaketa DESC;
END //

CREATE PROCEDURE sp_historikoa_ekintza_bila(IN p_ekintza VARCHAR(100))
BEGIN
    SELECT id_hist, taula, ekintza, data_aldaketa, oharra
    FROM   historikoa
    WHERE  ekintza LIKE CONCAT('%', p_ekintza, '%')
    ORDER  BY data_aldaketa DESC;
END //

CREATE PROCEDURE sp_historikoa_taula_bila(IN p_taula VARCHAR(100))
BEGIN
    SELECT id_hist, taula, ekintza, data_aldaketa, oharra
    FROM   historikoa
    WHERE  taula LIKE CONCAT('%', p_taula, '%')
    ORDER  BY data_aldaketa DESC;
END //

CREATE FUNCTION fn_historikoa_kopurua()
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE total INT DEFAULT 0;
    SELECT COUNT(*) INTO total FROM historikoa;
    RETURN total;
END //

CREATE FUNCTION fn_historikoa_ekintza_kopurua(p_kategoria VARCHAR(20))
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE total INT DEFAULT 0;
    IF p_kategoria = 'Sortu' THEN
        SELECT COUNT(*) INTO total FROM historikoa
        WHERE  UPPER(ekintza) LIKE '%SORTU%'
            OR UPPER(ekintza) LIKE '%INSERT%';
    ELSEIF p_kategoria = 'Aldatu' THEN
        SELECT COUNT(*) INTO total FROM historikoa
        WHERE  UPPER(ekintza) LIKE '%ALDATU%'
            OR UPPER(ekintza) LIKE '%UPDATE%';
    ELSEIF p_kategoria = 'Ezabatu' THEN
        SELECT COUNT(*) INTO total FROM historikoa
        WHERE  UPPER(ekintza) LIKE '%EZABATU%'
            OR UPPER(ekintza) LIKE '%DELETE%';
    ELSE
        SELECT COUNT(*) INTO total FROM historikoa
        WHERE  ekintza LIKE CONCAT('%', p_kategoria, '%');
    END IF;
    RETURN total;
END //

DELIMITER ;

SELECT 'sp_historikoa_guztiak'      AS izena, 'PROCEDURE' AS mota UNION ALL
SELECT 'sp_historikoa_ekintza_bila' AS izena, 'PROCEDURE' AS mota UNION ALL
SELECT 'sp_historikoa_taula_bila'   AS izena, 'PROCEDURE' AS mota UNION ALL
SELECT 'fn_historikoa_kopurua'      AS izena, 'FUNCTION'  AS mota UNION ALL
SELECT 'fn_historikoa_ekintza_kopurua' AS izena, 'FUNCTION' AS mota;
