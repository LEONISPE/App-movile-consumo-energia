
import 'dart:convert';
import 'package:hello_world/services/LoginService.dart';
import 'package:http/http.dart' as http;

class GasService {
 Future<Map<String, dynamic>> getDataGasConsumoDiario() async {
    final url = Uri.parse('http://localhost:8080/gas/consumo');

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
      throw Exception('Error al obtener los datos del Gas');
    }
    
  }
  Future<Map<String, dynamic>> getDataGasConsumoAcomulado() async {
    final url = Uri.parse('http://localhost:8080/gas/consumo/acomulado');

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
      throw Exception('Error al obtener los datos del Gas');
    }
  } 
}

