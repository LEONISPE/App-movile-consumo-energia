import 'dart:convert';
import 'package:App/config/api_config.dart';
import 'package:http/http.dart' as http;

import 'LoginService.dart';


class EnergiaService {

  Future<Map<String, dynamic>> getDataEnergiaDiario() async{
   final url = Uri.parse('${ApiConfig.baseUrl}/energia/consumo');

    final token = LoginService.token;


   final response = await http.get(
    url,
    headers: {"Content-Type": "application/json",
      "Authorization": "Bearer $token"}
   );

   if(response.statusCode == 200){
   return jsonDecode(response.body);
   }else {
     throw Exception("error al obtener datos de energia");
   }

  }

  Future<Map<String, dynamic>> getDataEnergiaAcomulado() async{
   final url = Uri.parse('${ApiConfig.baseUrl}/energia/consumo/acomulado');

  final token = LoginService.token;


   final response = await http.get(
    url,
    headers: {"Content-Type": "application/json",
      "Authorization": "Bearer $token"}
   );

   if(response.statusCode == 200){
   return jsonDecode(response.body);
   }else {
     throw Exception("error al obtener datos de energia");
   }

  }

  

}

