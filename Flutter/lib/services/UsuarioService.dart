

import 'dart:convert';
import 'package:App/config/api_config.dart';
import 'package:App/services/LoginService.dart';
import 'package:http/http.dart' as http;
import 'package:jwt_decoder/jwt_decoder.dart';

class Usuarioservice {

Future<Map<String, dynamic>> getDataUsuario() async{
  final url = Uri.parse("${ApiConfig.baseUrl}/user/usuarios");

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

   Future<List<dynamic>> getDataMiembros() async{
  final url = Uri.parse("${ApiConfig.baseUrl}/ver/miembros");
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
     throw Exception("error al obtener datos de los miembros");
   }

  }
Future<String?> getUserRole() async {
  final token = LoginService.token; // usamos directamente el token almacenado
  if (token == null || token.isEmpty) {
    throw Exception('Token no encontrado');
  }

  final decoded = JwtDecoder.decode(token);
  print('Rol decodificado: ${decoded['role']}'); // para depurar
  return decoded['role']; // o 'sub' o 'authorities', según tu JWT
}



Future<bool> setearEmailMiembro(String email, int idMiembro) async {
  final url = Uri.parse('${ApiConfig.baseUrl}/user/setear/email/$idMiembro');
  final token = LoginService.token;

  final body = jsonEncode({"email": email});

  final response = await http.put(
    url,
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer $token"
    },
    body: body,
  );

  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');

  return response.statusCode == 200;
}
Future<bool> comprobarEmailMiembro(String email) async {
  final url = Uri.parse('${ApiConfig.baseUrl}/user/comprobar/email/miembros');

  final response = await http.post(
    url,
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({'email': email}),
  );
  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');

  return response.statusCode == 200;
}


Future<bool> setearPasswordMiembro(String email, String password) async {
  final url = Uri.parse('${ApiConfig.baseUrl}/user/miembro/set-password?email=$email');
  final response = await http.post(
    url,
    headers: {"Content-Type": "application/json"},
    body: jsonEncode({"password": password}),
  );
  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');

  return response.statusCode == 200;
}
}
