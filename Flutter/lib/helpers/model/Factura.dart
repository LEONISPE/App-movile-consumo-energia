class Factura {
  final int facturaId;
  final String fechaInicio;
  final String fechaFin;
  final double consumoTotal;
  final double costoTotal;

  Factura({
    required this.facturaId,
    required this.fechaInicio,
    required this.fechaFin,
    required this.consumoTotal,
    required this.costoTotal,
  });

  factory Factura.fromJson(Map<String, dynamic> json) {
    return Factura(
      facturaId: json['facturaId'] ?? 0,
      fechaInicio: json['fecha_periodoInicio'] ?? '',
      fechaFin: json['fechaPeriodoFin'] ?? '',
      consumoTotal: (json['consumoTotal'] ?? 0).toDouble(),
      costoTotal: (json['costo_total'] ?? 0).toDouble(),
    );
  }
}