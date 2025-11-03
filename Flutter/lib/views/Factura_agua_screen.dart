
import 'dart:math';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:hello_world/helpers/model/factura.dart';
import 'package:hello_world/services/AguaService.dart';


class FacturaAguaScreen extends StatefulWidget {
  static String routeName = '/factura-agua-screen';
  const FacturaAguaScreen({super.key});
  

  @override
  State<FacturaAguaScreen> createState() => _FacturaAguaScreenState();

}

class _FacturaAguaScreenState extends State<FacturaAguaScreen> {
  final AguaService _aguaService = AguaService();
  List<Factura> facturas = [];
  bool isLoading = false;

  @override
  void initState() {
    super.initState();
    cargarFacturas();
  }

 Future<void> cargarFacturas() async {
    try {
      final data = await  _aguaService.getFacturasPorServicio("AGUA");
      setState(() {
        facturas = data;
        isLoading = false;
      });
    } catch (e) {
      print("Error: $e");
      setState(() => isLoading = false);
    }
  }
void mostrarDetalleFactura(Factura factura) {
    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text("Detalle de Factura"),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text("Consumo total: ${factura.consumoTotal} m³"),
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
        title: const Text("Facturas de Agua"),
        backgroundColor: Colors.blueAccent,
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
                  subtitle: const Text("Toque para ver el detalle"),
                  onTap: () => mostrarDetalleFactura(factura),
                );
              },
            ),
    );
  }
}