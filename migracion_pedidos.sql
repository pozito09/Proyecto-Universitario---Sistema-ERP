-- ============================================================
-- Migración: Separar estado de pago y estado de cocina en pedidos
-- ============================================================

-- 1. Agregar nuevas columnas
ALTER TABLE pedidos
  ADD COLUMN estado_pago VARCHAR(20) DEFAULT 'En cola' AFTER pagado,
  ADD COLUMN estado_cocina VARCHAR(20) DEFAULT 'Pendiente' AFTER estado_pago;

-- 2. Migrar datos existentes
-- estado_pago: 'Pagado' si pagado=TRUE, 'Anulado' si estado='Anulado', 'En cola' en otro caso
UPDATE pedidos SET estado_pago = 'Pagado' WHERE pagado = TRUE;
UPDATE pedidos SET estado_pago = 'Anulado' WHERE estado = 'Anulado';

-- estado_cocina: heredar de estado para Pendiente/Preparando/Listo, NULL para otros
UPDATE pedidos SET estado_cocina = estado WHERE estado IN ('Pendiente', 'Preparando', 'Listo');

-- 3. Eliminar columnas antiguas
ALTER TABLE pedidos
  DROP COLUMN pagado,
  DROP COLUMN estado,
  DROP COLUMN fue_listo;
