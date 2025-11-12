
import 'dart:math';
import 'package:App/helpers/model/Factura.dart';
import 'package:App/services/AguaService.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';


class FacturaScreen extends StatefulWidget {
  final String servicio; 
  final String titulo; 
  

  const FacturaScreen({
    super.key,
    required this.servicio,
    required this.titulo,
  });

  static String routeName = '/factura-screen';

  @override
  State<FacturaScreen> createState() => _FacturaScreenState();
}

class _FacturaScreenState extends State<FacturaScreen> {
  final AguaService _service = AguaService(); // Renómbralo si quieres
  List<Factura> facturas = [];
  bool isLoading = true;

  @override
  void initState() {
    super.initState();
    cargarFacturas();
  }

  Future<void> cargarFacturas() async {
    try {
      final data = await _service.getFacturasPorServicio(widget.servicio);
      setState(() {
        facturas = data;
      });
    } catch (e) {
      print("Error: $e");
    } finally {
      setState(() => isLoading = false);
    }
  }

  void mostrarDetalleFactura(Factura factura) {
    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        title: Text("Detalle de Factura (${widget.servicio})"),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text("Consumo total: ${factura.consumoTotal}"),
            Text("Costo total: \$${factura.costoTotal}"),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text("Cerrar"),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.titulo),
        centerTitle: true,
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
              itemCount: facturas.length,
              itemBuilder: (context, index) {
                final factura = facturas[index];
                return ListTile(
                  title: Text(
                    "Periodo: ${factura.fechaInicio} - ${factura.fechaFin}",
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                  subtitle: const Text("Toca para ver detalles"),
                  onTap: () => mostrarDetalleFactura(factura),
                );
              },
            ),
    );
  }
}