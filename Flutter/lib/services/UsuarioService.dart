

import 'dart:convert';

import 'package:hello_world/services/LoginService.dart';
import 'package:http/http.dart' as http;

class Usuarioservice {

Future<Map<String, dynamic>> getDataUsuario() async{
   final url = Uri.parse('http://localhost:8080/user/usuarios');

       final token = LoginService.token;

print('Token enviado: $token');
print('URL: $url');


     
 final response = await http.get(
    url,
    headers: {"Content-Type": "application/json",
      "Authorization": "Bearer $token"}
   );

   if (token == null || token.isEmpty) {
  throw Exception('Token no encontrado o vacío');
}
 print('Status code: ${response.statusCode}');
 print('Body: ${response.body}');

   if(response.statusCode == 200){
   return jsonDecode(response.body);
   }else {
     throw Exception("error al obtener datos del usuario");
   }

  }


}