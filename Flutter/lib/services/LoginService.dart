import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';


class LoginService {

static String? token;

Future<http.Response> login(String email, String password) async {
    final url = Uri.parse('http://localhost:8080/login');

    final body = jsonEncode({
      'email': email,
      'password': password,
    });

    final response = await http.post(
      url,
      headers: {"Content-Type": "application/json"},
      body: body,
    );

    print('Status code: ${response.statusCode}');
    print('Body: ${response.body}');

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      token = data['jwToken'];
      print('Token guardado: $token');

      // 🔹 Guardar token en almacenamiento local
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString('token', token!);
    }

    return response;
  }
  // 🔹 Obtener token guardado
  static Future<String?> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('token');
  }

  // 🔹 Eliminar token al hacer logout
  static Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('token');
    token = null;
  }
}
  

