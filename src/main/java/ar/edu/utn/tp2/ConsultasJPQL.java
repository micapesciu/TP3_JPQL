package ar.edu.utn.tp2;

import java.util.Date;
import java.util.List;

import jakarta.persistence.EntityManager;

public final class ConsultasJPQL {

    private ConsultasJPQL() {
    }

    //nivel 1
    public static List<FacturaVenta> obtenerFacturas(EntityManager entityManager) {
        String jpql = "SELECT f FROM FacturaVenta f";

        return entityManager.createQuery(jpql, FacturaVenta.class)
                .getResultList();
    }

    public static List<Object[]> obtenerDatosFacturas(EntityManager entityManager) {
        String jpql = """
                SELECT f.numero, f.fechaEmision, f.importeTotal
                FROM FacturaVenta f
                """;

        return entityManager.createQuery(jpql, Object[].class)
                .getResultList();
    }

    public static List<Articulo> obtenerArticulosPorRubro(
            EntityManager entityManager, String denominacionRubro) {
        String jpql = """
                SELECT a
                FROM Articulo a
                JOIN a.rubro r
                WHERE r.denominacion = :denominacionRubro
                """;

        return entityManager.createQuery(jpql, Articulo.class)
                .setParameter("denominacionRubro", denominacionRubro)
                .getResultList();
    }

    public static List<FacturaVenta> obtenerFacturasEntreFechas(
            EntityManager entityManager, Date fechaDesde, Date fechaHasta) {
        String jpql = """
                SELECT f
                FROM FacturaVenta f
                WHERE f.fechaEmision BETWEEN :fechaDesde AND :fechaHasta
                """;

        return entityManager.createQuery(jpql, FacturaVenta.class)
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("fechaHasta", fechaHasta)
                .getResultList();
    }

    // nivel 2 - punto 5
    public static List<FacturaVenta> obtenerFacturasEmitidasNoAnuladas(
            EntityManager entityManager, double importeMinimo) {
        String jpql = """
                SELECT f
                FROM FacturaVenta f
                WHERE f.estado = :estado
                  AND f.importeTotal > :importeMinimo
                  AND f.fechaAnulacion IS NULL
                """;

        return entityManager.createQuery(jpql, FacturaVenta.class)
                .setParameter("estado", "EMITIDA")
                .setParameter("importeMinimo", importeMinimo)
                .getResultList();
    }

    // nivel 2 - punto 6
    public static List<Cliente> obtenerClientesPorDenominacionOCuit(
            EntityManager entityManager, String textoParcial) {
        String jpql = """
                SELECT c
                FROM Cliente c
                WHERE LOWER(c.denominacion) LIKE LOWER(:textoParcial)
                   OR c.cuitCuil LIKE :prefijoCuit
                """;

        return entityManager.createQuery(jpql, Cliente.class)
                .setParameter("textoParcial", "%" + textoParcial + "%")
                .setParameter("prefijoCuit", "20-%")
                .getResultList();
    }

    // nivel 2- punto 7
    public static List<String> obtenerEstadosDeFacturas(EntityManager entityManager) {
        String jpql = """
                SELECT DISTINCT f.estado
                FROM FacturaVenta f
                ORDER BY f.estado ASC
                """;

        return entityManager.createQuery(jpql, String.class)
                .getResultList();
    }

    // Nivel 2 - punto 8
    public static Object[] obtenerResumenFacturasEmitidas(EntityManager entityManager) {
        String jpql = """
                SELECT COUNT(f), SUM(f.importeTotal), AVG(f.importeTotal)
                FROM FacturaVenta f
                WHERE f.estado = :estado
                """;

        return entityManager.createQuery(jpql, Object[].class)
                .setParameter("estado", "EMITIDA")
                .getSingleResult();
    }

    // Nivel 2 - punto 9
    public static List<PuntoVenta> obtenerPuntosVentaPorNumeros(
            EntityManager entityManager, List<Integer> numeros) {
        String jpql = """
                SELECT p
                FROM PuntoVenta p
                WHERE p.numero IN :numeros
                """;

        return entityManager.createQuery(jpql, PuntoVenta.class)
                .setParameter("numeros", numeros)
                .getResultList();
    }
}
