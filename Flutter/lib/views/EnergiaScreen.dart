import 'dart:ui';
import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hello_world/services/EnergiaService.dart';
import 'package:intl/intl.dart';


class Energiascreen extends StatefulWidget {
   static String routeName = '/energia-screen';
  const Energiascreen({super.key});

  @override
  State<Energiascreen> createState() => _Energiascreen();
}

class _Energiascreen extends State<Energiascreen> {
final EnergiaService _energiaService = EnergiaService();
  
  double consumoActualHoy = 0.0;
  double costoActualHoy = 0.0;
  double consumoHora = 0.0;
  double consumoAcomulado = 0.0;
  double costoAcomulado = 0.0;

 bool _isLoading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadEnergiaData();
  }
  Future<void> _loadEnergiaData() async {
  setState(() {
    _isLoading = true;
    _error = null;
  });

  try {

    final responseDiario = await _energiaService
        .getDataEnergiaDiario()
        .timeout(const Duration(seconds: 15));

    final responseAcomulado = await _energiaService
        .getDataEnergiaAcomulado()
        .timeout(const Duration(seconds: 15));

    setState(() {
      consumoActualHoy = responseDiario['consumoActualHoy'] ?? 0.0;
      costoActualHoy = responseDiario['costoActualHoy'] ?? 0.0;
      consumoHora = responseDiario['consumoHora'] ?? 0.0;
      consumoAcomulado = responseAcomulado['consumoAcomulado'] ?? 0.0;
      costoAcomulado = responseAcomulado['costoAcomulado'] ?? 0.0;
      _isLoading = false;
    });
  } on TimeoutException {
    setState(() {
      _error = '⏰ Tiempo de espera agotado AWS otraves esta caido  (15s). Por favor, reintente.';
      _isLoading = false;
    });
  } catch (e) {
    setState(() {
      _error = '❌ Error al cargar los datos o el servidor no responde.';
      _isLoading = false;
    });
  }
}

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.blue,
      extendBodyBehindAppBar: true,
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        systemOverlayStyle: const SystemUiOverlayStyle(statusBarBrightness: Brightness.dark),
      ),
      body: _isLoading
    ? const Center(
        child: CircularProgressIndicator(color: Colors.white),
      )
    : _error != null
        ? Center(
            child: Column( 
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Text(
                  _error!,
                  textAlign: TextAlign.center,
                  style: const TextStyle(
                    color: Colors.black, 
                    fontSize: 28
                  ),
                ),
                const SizedBox(height: 20), 
                ElevatedButton.icon(
                  onPressed: () {
                    _loadEnergiaData(); 
                  },
                  icon: const Icon(Icons.refresh),
                  label: const Text('Reintentar'),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.blueAccent,
                    foregroundColor: Colors.white,      
                  ),
                ),
              ],
            ),
          )
         : Padding(
        padding: const  EdgeInsets.fromLTRB(40, 3 * kToolbarHeight, 40, 20),
         child: Stack(
          children: [
            Align(
              alignment: const AlignmentDirectional(3, -0.3),
              child: Container(
                height: 300,
                width: 300,
                decoration: const BoxDecoration(
                  shape: BoxShape.circle,
                  color: Colors.blueAccent,
                ),
              ),
            ),
            Align(
              alignment: const AlignmentDirectional(-3, -0.3),
              child: Container(
                height: 300,
                width: 300,
                decoration: const BoxDecoration(
                  shape: BoxShape.circle,
                  color: Color(0xFF1565C0),
                ),
              ),
            ),
            BackdropFilter(
              filter: ImageFilter.blur(sigmaX: 100.0, sigmaY: 100.0),
              child: Container(color: Colors.transparent),
            ),

            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text(
                  '⚡ Servicio de Energia',
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 26,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                const SizedBox(height: 10),
                Center(
                  child: FaIcon(FontAwesomeIcons.bolt, color: Colors.lightBlueAccent, size: 90),
                ),
                const SizedBox(height: 15),
              Center(
                  child: Text(
                    DateFormat('EEEE dd •').add_jm().format(DateTime.now()),
                    style: const TextStyle(color: Colors.white70, fontSize: 16),
                  ),
                ),
                const SizedBox(height: 25),


            _buildInfoRow(
                  icon: FontAwesomeIcons.tachometerAlt,
                  label: 'Consumo actual hoy',
                  value: '$consumoActualHoy KW/H',
                ),
                _buildInfoRow(
                  icon: FontAwesomeIcons.coins,
                  label: 'Costo actual hoy',
                  value: '\$$costoActualHoy',
                ),
                _buildInfoRow(
                  icon: FontAwesomeIcons.clock,
                  label: 'Consumo por hora',
                  value: '$consumoHora KW/H',
                ),
                const Divider(color: Colors.grey),
                _buildInfoRow(
                  icon: FontAwesomeIcons.chartLine,
                  label: 'Consumo acumulado',
                  value: '$consumoAcomulado KW/H',
                ),
                _buildInfoRow(
                  icon: FontAwesomeIcons.moneyBillWave,
                  label: 'Costo acumulado',
                  value: '\$$costoAcomulado',
                ),


              ],
            )
          ],
         ),
      ),
    );
  }

  Widget _buildInfoRow({required IconData icon, required String label, required String value}) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        children: [
          FaIcon(icon, color: Colors.white, size: 20),
          const SizedBox(width: 10),
          Expanded(
            child: Text(label, style: const TextStyle(color: Colors.white70, fontSize: 16)),
          ),
          Text(value, style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        ],
      ),
    );
  }


}