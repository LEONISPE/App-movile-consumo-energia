import 'dart:convert';
import 'package:hello_world/helpers/model/factura.dart';
import 'package:hello_world/services/LoginService.dart';
import 'package:http/http.dart' as http;

class AguaService {

Future<Map<String, dynamic>> getDataAguaConsumoDiario() async {
    final url = Uri.parse('http://localhost:8080/agua/consumo');

     final token = LoginService.token;


    final response = await http.get(
      url,
      headers: {"Content-Type": "application/json",
      "Authorization": "Bearer $token"
      },
    );
    print('Status code: ${response.statusCode}');
    print('Body: ${response.body}');

    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception('Error al obtener los datos del agua');
    }
  }


Future<Map<String, dynamic>> getDataAguaConsumoAcomulado() async {
    final url = Uri.parse('http://localhost:8080/agua/consumo/acomulado');

     final token = LoginService.token;

    final response = await http.get(
      url,
      headers: {"Content-Type": "application/json",
       "Authorization": "Bearer $token"
      },
    );
    print('Status code: ${response.statusCode}');
    print('Body: ${response.body}');

    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception('Error al obtener los datos del agua');
    }
  }

Future<List<Factura>> getFacturasPorServicio(String servicio) async {
  //final url = Uri.parse('http://10.0.2.2:8080/usuario/servicio/$servicio');
  final url = Uri.parse('http://localhost:8080/usuario/servicio/$servicio');

   final token = LoginService.token;


  final response = await http.get(
    url,
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer $token"
    },
  );

  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');

  if (response.statusCode == 200) {
    final List<dynamic> data = jsonDecode(response.body);
   return data.map((item) => Factura.fromJson(item)).toList();
  } else {
    throw Exception('Error al obtener las facturas del servicio $servicio');
  }
}



}